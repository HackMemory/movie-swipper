package ru.ifmo.authservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.authservice.dto.request.LoginRequest;
import ru.ifmo.authservice.dto.response.AuthResponse;
import ru.ifmo.authservice.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = AuthResponse.builder()
                    .token(authService.login(request.getUsername(), request.getPassword()))
                    .build();
            return ResponseEntity.ok(response);
        } catch (AuthenticationException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        } catch (Exception exception) {
            log.info(exception.getMessage(), exception);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, exception.getMessage());
        }
    }

}
