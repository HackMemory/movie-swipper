package ru.ifmo.movieswipper.controller.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ifmo.movieswipper.dto.request.ChangeRoleRequest;
import ru.ifmo.movieswipper.dto.request.RegisterRequest;
import ru.ifmo.movieswipper.exception.NotFoundException;
import ru.ifmo.movieswipper.service.AuthService;
import ru.ifmo.movieswipper.service.UserService;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AuthService authService;
    private final UserService userService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            authService.register(request.getUsername(), request.getPassword());
            return ResponseEntity.ok().build();
        } catch (UsernameNotFoundException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, exception.getMessage(), exception);
        }
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/change-role")
    public ResponseEntity<?> changeRole(@Valid @RequestBody ChangeRoleRequest request) {
        try {
            authService.changeRole(request.getUsername(), request.getRole());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, exception.getMessage(), exception);
        } catch (NotFoundException exception){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/user-list")
    public ResponseEntity<?> getUsersList() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

}
