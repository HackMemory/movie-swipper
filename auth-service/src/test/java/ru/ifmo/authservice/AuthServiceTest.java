package ru.ifmo.authservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.util.ReflectionTestUtils;

import ru.ifmo.authservice.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;


    public Authentication authentication() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken("customUsername", "customPassword", authorities);
    }

    public JwtEncoder mockJwtEncoder() {
        return new MockJwtEncoder();
    }

    static class MockJwtEncoder implements JwtEncoder {
        @Override
        public Jwt encode(JwtEncoderParameters parameters) {
            Map<String, Object> headers = new HashMap<>();
            headers.put("alg", "RS256");

            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", "test_user");
            claims.put("exp", Instant.now().plusSeconds(3600));

            return new Jwt("mockTokenValue", Instant.now(), Instant.now().plusSeconds(3600), headers, claims);
        }
    }

    @Test
    void testLogin() {
        String username = "testUser";
        String password = "testPassword";

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication());


        ReflectionTestUtils.setField(authService, "encoder", mockJwtEncoder());
        ReflectionTestUtils.setField(authService, "expireTime", 3600L);

        String token = authService.login(username, password);
        assertNotNull(token);
    }

}