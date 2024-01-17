package ru.ifmo.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.ifmo.userservice.dto.UserDTO;
import ru.ifmo.userservice.dto.request.ChangeRoleRequest;
import ru.ifmo.userservice.dto.request.RegisterRequest;
import ru.ifmo.userservice.exception.NotFoundException;
import ru.ifmo.userservice.mapper.UserMapper;
import ru.ifmo.userservice.model.User;
import ru.ifmo.userservice.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserServiceController {

    private final UserService userService;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            userService.register(request.getUsername(), request.getPassword());
            return ResponseEntity.ok().build();
        } catch (UsernameNotFoundException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, exception.getMessage(), exception);
        }
    }

    @Hidden
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
            return ResponseEntity.ok(UserMapper.INSTANCE.toDomain(user));
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Hidden
    @GetMapping("/me")
    public ResponseEntity<?> getCurrent(Authentication authentication) {
        try {
            System.out.println(authentication);
            return ResponseEntity.ok(
                    userService.findByUsername(authentication.getName())
                            .map(UserMapper.INSTANCE::toDomain));

        } catch (NotFoundException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, ex.getMessage(), ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/changeRole")
    public ResponseEntity<?> changeRole(@Valid @RequestBody ChangeRoleRequest request) {
        try {
            userService.changeRole(request.getUsername(), request.getRole());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, exception.getMessage(), exception);
        } catch (NotFoundException exception){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }


}