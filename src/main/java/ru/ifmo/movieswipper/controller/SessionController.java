package ru.ifmo.movieswipper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.movieswipper.dto.response.SessionCreateResponse;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.service.SessionService;
import ru.ifmo.movieswipper.service.UserService;

import java.util.Optional;

import static ru.ifmo.movieswipper.util.InviteCodeGenerator.generateRandomString;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<SessionCreateResponse> create(Authentication authentication) {
        Optional<User> user = userService.findByUsername(authentication.getName());
        String code = generateRandomString(8);
        Session session = Session.builder()
                .inviteCode(code)
                .creator(user.get()).build();

        sessionService.saveSession(session);

        return ResponseEntity.ok(SessionCreateResponse.builder().code(code).build());
    }

    @GetMapping("/join/{code}")
    public ResponseEntity<?> join(Authentication authentication) {

    }
}
