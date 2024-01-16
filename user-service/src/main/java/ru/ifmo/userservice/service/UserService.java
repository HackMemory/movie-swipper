package ru.ifmo.userservice.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Streamable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import ru.ifmo.userservice.constant.RoleConstants;
import ru.ifmo.userservice.dto.UserDTO;
import ru.ifmo.userservice.exception.ParametersException;
import ru.ifmo.userservice.mapper.UserMapper;
import ru.ifmo.userservice.model.Role;
import ru.ifmo.userservice.model.User;
import ru.ifmo.userservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService  {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${root.username}")
    private String rootUsername;

    @Value("${root.password}")
    private String rootPassword;

    @Value("${token.expire}")
    private Long expireTime;

    @PostConstruct
    @Transactional
    public void init() {
        if (roleService.getModeratorRole().isEmpty())
            roleService.saveRole(new Role(2L, RoleConstants.MODERATOR));
        if (roleService.getVipRole().isEmpty())
            roleService.saveRole(new Role(3L, RoleConstants.VIP));
        if (roleService.getMemberRole().isEmpty())
            roleService.saveRole(new Role(4L, RoleConstants.MEMBER));


        if (findByUsername(rootUsername).isEmpty()) {
            Role adminRole = roleService.getAdminRole().orElseGet(() -> roleService.saveRole(new Role(1L, RoleConstants.ADMIN)));
            User admin = User.builder()
                    .username(rootUsername)
                    .password(passwordEncoder.encode(rootPassword))
                    .roles(Set.of(adminRole)).build();
            saveUser(admin);
        }
    }

    public void register(String username, String password) {
        Role roleUser = roleService.getMemberRole().orElseThrow();
        if (findByUsername(username).isPresent()){
            throw new ParametersException("User already exist");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(Set.of(roleUser)).build();

        saveUser(user);
    }




    public User saveUser(User userEntity) {
        return userRepository.save(userEntity);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<UserDTO> getAllUsers() {
        return Streamable.of(userRepository.findAll()).stream()
                .map(UserMapper.INSTANCE::toDomain).toList();
    }
}
