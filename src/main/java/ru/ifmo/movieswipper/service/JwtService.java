package ru.ifmo.movieswipper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${token.expire}")
    private Long expire;

    private final JwtDecoder jwtDecoder;

    public String extractUserName(String token) {
        return jwtDecoder.decode("").getSubject();
    }


}
