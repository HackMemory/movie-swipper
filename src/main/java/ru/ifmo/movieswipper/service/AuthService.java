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

    @PostConstruct
    public void init() {
        if (userService.findByUsername(rootUsername).isEmpty()) {
            Role adminRole = roleService.getAdminRole().orElseGet(null);
            User admin = User.builder()
                    .username(rootUsername)
                    .password(passwordEncoder.encode(rootPassword))
                    .roles(Set.of(adminRole)).build();
            userService.saveUser(admin);
        }
    }

    public String login(String username, String password) throws IllegalArgumentException {
        //TODO fix exception
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            Instant now = Instant.now();
            long expiry = 36000L;
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            System.out.println(scope);
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(authentication.getName())
                    .claim("scope", scope)
                    .build();

            return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        }catch (Exception ex){
            throw new IllegalArgumentException(ex.getMessage());
        }
    }


    public void register(String username, String password) {
        //TODO fix
        Role roleUser = roleService.getMemberRole().orElseGet(null);
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
