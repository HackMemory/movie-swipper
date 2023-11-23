package ru.ifmo.movieswipper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ifmo.movieswipper.dto.response.SessionCreateResponse;
import ru.ifmo.movieswipper.exception.SessionNotFoundException;
import ru.ifmo.movieswipper.exception.UserExistInSessionException;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.service.UserSessionService;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

    private final UserSessionService userSessionService;

    @GetMapping("/current")
    public ResponseEntity<?> current(Authentication authentication) {
        try {
            return ResponseEntity.ok(userSessionService.currentSession(authentication.getName()));

        }catch (UsernameNotFoundException | SessionNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/exit")
    public ResponseEntity<?> exit(Authentication authentication) {
        try {
            userSessionService.exitFromSession(authentication.getName());
            return ResponseEntity.ok().build();
        }catch (UsernameNotFoundException | SessionNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_VIP')")
    @PostMapping("/create")
    public ResponseEntity<?> create(Authentication authentication) {
        try {
            Session session = userSessionService.createSession(authentication.getName());
            return ResponseEntity.ok(SessionCreateResponse.builder().code(session.getInviteCode()).build());
        }catch (UsernameNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }catch (UserExistInSessionException ex){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
         }
    }

    @GetMapping("/join/{code}")
    public ResponseEntity<?> join(Authentication authentication, @PathVariable String code) {
        try{
            userSessionService.joinSession(authentication.getName(), code);
            return ResponseEntity.ok().build();
        }catch (UsernameNotFoundException | SessionNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }catch (UserExistInSessionException ex){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @DeleteMapping("/delete/{code}")
    public ResponseEntity<?> delete(Authentication authentication, @PathVariable String code) {
        try{
            userSessionService.deleteSession(code);
            return ResponseEntity.ok().build();
        }catch (SessionNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
