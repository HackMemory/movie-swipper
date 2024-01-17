package ru.ifmo.authservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.authservice.dto.request.LoginRequest;
import ru.ifmo.authservice.dto.request.ValidateTokenRequest;
import ru.ifmo.authservice.dto.response.AuthResponse;
import ru.ifmo.authservice.dto.response.ValidateTokenResponse;
import ru.ifmo.authservice.model.User;
import ru.ifmo.authservice.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/login")
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

    @Hidden
    @PostMapping("/token/validate")
    public ResponseEntity<ValidateTokenResponse> validateToken(@Valid @RequestBody ValidateTokenRequest request) {
        try {
            User user = authService.validateToken(request.token());
            ValidateTokenResponse response = ValidateTokenResponse.builder()
                    .username(user.getUsername())
                    .authorities(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .build();
            return ResponseEntity.ok(response);
        } catch (JwtException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


}
