package ru.ifmo.movieswipper.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.dto.UserDTO;
import ru.ifmo.movieswipper.exception.NotFoundException;
import ru.ifmo.movieswipper.exception.ParametersException;
import ru.ifmo.movieswipper.mapper.UserMapper;
import ru.ifmo.movieswipper.model.Role;
import ru.ifmo.movieswipper.model.User;

import java.time.Instant;
import java.util.List;
import java.util.Set;

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
        if (userService.findByUsername(username).isPresent()){
            throw new ParametersException("User already exist");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(Set.of(roleUser)).build();

        userService.saveUser(user);
    }

    public void changeRole(String username, String role) {
        User user = userService.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        Role roleEntity = roleService.findByName(role.toUpperCase())
                .orElseThrow(()-> new NotFoundException("Role not found"));

        user.getRoles().clear();
        user.getRoles().add(roleEntity);

        userService.saveUser(user);
    }

}
