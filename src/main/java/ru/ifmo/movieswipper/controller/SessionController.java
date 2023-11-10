package ru.ifmo.movieswipper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.movieswipper.dto.response.SessionCreateResponse;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.model.UserSession;
import ru.ifmo.movieswipper.service.SessionService;
import ru.ifmo.movieswipper.service.UserService;
import ru.ifmo.movieswipper.service.UserSessionService;

import java.util.Optional;

import static ru.ifmo.movieswipper.util.InviteCodeGenerator.generateRandomString;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final UserSessionService userSessionService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<SessionCreateResponse> create(Authentication authentication) {
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
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
