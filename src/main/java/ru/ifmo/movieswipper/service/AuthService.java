package ru.ifmo.movieswipper.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.model.Role;
import ru.ifmo.movieswipper.model.User;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

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

}
