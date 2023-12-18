package ru.ifmo.movieswipper.module;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.ifmo.movieswipper.dto.UserDTO;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.repository.UserRepository;
import ru.ifmo.movieswipper.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testSaveUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals(user, savedUser);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindById() {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(userId);

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindByUsername() {
        String username = "testUser";
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername(username);

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername() {
        String username = "testUser";
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.loadUserByUsername(username));

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_ThrowsException() {
        String username = "nonexistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        when(userRepository.findAll()).thenReturn(userList);

        List<UserDTO> userDTOList = userService.getAllUsers();

        assertEquals(userList.size(), userDTOList.size());

        verify(userRepository, times(1)).findAll();
    }
}
