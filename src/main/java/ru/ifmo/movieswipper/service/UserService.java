package ru.ifmo.movieswipper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.dto.UserDTO;
import ru.ifmo.movieswipper.mapper.UserMapper;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService  {
    private final UserRepository userRepository;

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
