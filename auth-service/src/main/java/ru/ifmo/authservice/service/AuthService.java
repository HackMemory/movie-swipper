package ru.ifmo.authservice.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.ifmo.authservice.client.UserServiceClient;
import ru.ifmo.authservice.dto.UserDTO;
import ru.ifmo.authservice.mapper.UserMapper;
import ru.ifmo.authservice.model.User;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserServiceClient userServiceClient;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    @Getter
    @Setter
    @Value("${token.expire}")
    private Long expireTime;


    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        Instant now = Instant.now();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        System.out.println(roles);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(getExpireTime()))
                .subject(authentication.getName())
                .claim("roles", roles)
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public User validateToken(String token) throws JwtException {
        String username = decoder.decode(token).getClaimAsString("sub");
        ResponseEntity<UserDTO> userDTO = userServiceClient.getUser(username);
        return UserMapper.INSTANCE.fromDomain(userDTO.getBody());
    }

}
