package ru.ifmo.movieswipper.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.model.Role;
import ru.ifmo.movieswipper.model.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder encoder;

    @Value("${root.username}")
    private String rootUsername;

    @Value("${root.password}")
    private String rootPassword;

    @Value("${token.expire}")
    private Long expireTime;

    @PostConstruct
    public void init() {
        if (userService.findByUsername(rootUsername).isEmpty()) {
            Role adminRole = roleService.getAdminRole().orElseThrow();
            User admin = User.builder()
                    .username(rootUsername)
                    .password(passwordEncoder.encode(rootPassword))
                    .roles(Set.of(adminRole)).build();
            userService.saveUser(admin);
        }
    }

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
                .expiresAt(now.plusSeconds(expireTime))
                .subject(authentication.getName())
                .claim("roles", roles)
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


    public void register(String username, String password) {
        Role roleUser = roleService.getMemberRole().orElseThrow();
        if (userService.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("User exists");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(Set.of(roleUser)).build();

        userService.saveUser(user);
    }

    public void changeRole(String username, String role) {
        Optional<User> optionalUserEntity = userService.findByUsername(username);
        User userEntity = optionalUserEntity.orElseThrow();
        Optional<Role> optionalRoleEntity = roleService.findByName(role);
        Role roleEntity = optionalRoleEntity.orElseThrow();
        userEntity.getRoles().clear();
        userEntity.getRoles().add(roleEntity);
        userService.saveUser(userEntity);
    }

}
