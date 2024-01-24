package ru.ifmo.sessionservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.ifmo.sessionservice.dto.request.SessionAddMovieRequest;
import ru.ifmo.sessionservice.dto.response.SessionCreateResponse;
import ru.ifmo.sessionservice.exception.SessionNotFoundException;
import ru.ifmo.sessionservice.exception.UserExistInSessionException;
import ru.ifmo.sessionservice.model.Session;
import ru.ifmo.sessionservice.service.MovieSessionService;
import ru.ifmo.sessionservice.service.UserSessionService;

@RestController
@RequestMapping("session")
@RequiredArgsConstructor
public class SessionController {

    private final UserSessionService userSessionService;
    private final MovieSessionService movieSessionService;

    @GetMapping("/current")
    public ResponseEntity<?> current(Authentication authentication) {
        try {
            return ResponseEntity.ok(userSessionService.currentSession(authentication.getName()));

        }catch (UsernameNotFoundException | SessionNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/exit")
    public ResponseEntity<?> exit(Authentication authentication) {
        try {
            userSessionService.exitFromSession(authentication.getName());
            return ResponseEntity.ok().build();
        }catch (UsernameNotFoundException | SessionNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_VIP')")
    @PostMapping("/create")
    public ResponseEntity<?> create(Authentication authentication) {
        try {
            Session session = userSessionService.createSession(authentication.getName());
            return ResponseEntity.ok(SessionCreateResponse.builder().code(session.getInviteCode()).build());
        }catch (UsernameNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }catch (UserExistInSessionException ex){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
         }
    }

    @GetMapping("/join/{code}")
    public ResponseEntity<?> join(Authentication authentication, @PathVariable String code) {
        try{
            userSessionService.joinSession(authentication.getName(), code);
            return ResponseEntity.ok().build();
        }catch (UsernameNotFoundException | SessionNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }catch (UserExistInSessionException ex){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @DeleteMapping("/delete/{code}")
    public ResponseEntity<?> delete(@PathVariable String code) {
        try{
            userSessionService.deleteSession(code);
            return ResponseEntity.ok().build();
        }catch (SessionNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }


    @PostMapping("/add-movie")
    public ResponseEntity<?> addMovie(Authentication authentication, @Valid @RequestBody SessionAddMovieRequest request) {
        try {
            movieSessionService.addMovie(authentication.getName(), request.getTmdbMovieId(), request.getLiked());
            return ResponseEntity.ok().build();
        }catch (UsernameNotFoundException | SessionNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/liked-movies")
    public ResponseEntity<?> getLikedMovies(Authentication authentication) {
        try {
            return ResponseEntity.ok(movieSessionService.getLikedMovies(authentication.getName()));
        }catch (UsernameNotFoundException | SessionNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/matched-movies")
    public ResponseEntity<?> getMatchedMovies(Authentication authentication) {
        try {
            return ResponseEntity.ok(movieSessionService.getMatchedMovies(authentication.getName()));
        }catch (UsernameNotFoundException | SessionNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
