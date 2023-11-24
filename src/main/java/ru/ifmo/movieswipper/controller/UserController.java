package ru.ifmo.movieswipper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.ifmo.movieswipper.exception.NotFoundException;
import ru.ifmo.movieswipper.mapper.UserMapper;
import ru.ifmo.movieswipper.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrent(Authentication authentication) {
        try {
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
}
