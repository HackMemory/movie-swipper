package ru.ifmo.movieswipper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ifmo.movieswipper.dto.response.SessionCreateResponse;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.service.SessionService;
import ru.ifmo.movieswipper.service.UserService;
import ru.ifmo.movieswipper.service.UserSessionService;

import java.util.Optional;

import static ru.ifmo.movieswipper.util.StringUtils.generateRandomString;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final UserSessionService userSessionService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> create(Authentication authentication) {
        Optional<User> user = userService.findByUsername(authentication.getName());
        String code = generateRandomString(8);
        Session session = Session.builder()
                .inviteCode(code)
                .creator(user.get()).build();

        sessionService.saveSession(session);
        userSessionService.join(session, authentication.getName());

        return ResponseEntity.ok(SessionCreateResponse.builder().code(code).build());
    }

    @GetMapping("/join/{code}")
    public ResponseEntity<?> join(Authentication authentication, @PathVariable String code) {
        Optional<Session> session = sessionService.findByCode(code);
        if(session.isPresent()){
            try {
                userSessionService.join(session.get(), authentication.getName());
            }catch (IllegalArgumentException exception){
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, exception.getMessage(), exception);
            }

            return ResponseEntity.ok().build();
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<?> delete(Authentication authentication, @PathVariable String code) {
        Session session = sessionService.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));

        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            if(!session.getCreator().getId().equals(user.getId())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to delete this session");
            }

            sessionService.delete(session);
        }catch (IllegalArgumentException exception){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, exception.getMessage(), exception);
        }

        return ResponseEntity.ok().build();
    }
}
