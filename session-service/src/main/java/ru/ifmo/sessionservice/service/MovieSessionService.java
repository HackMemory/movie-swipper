package ru.ifmo.sessionservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ifmo.sessionservice.client.UserServiceClient;
import ru.ifmo.sessionservice.dto.MovieMatchedDTO;
import ru.ifmo.sessionservice.dto.MovieSessionDTO;
import ru.ifmo.sessionservice.dto.UserDTO;
import ru.ifmo.sessionservice.exception.MoviesNotFound;
import ru.ifmo.sessionservice.exception.SessionNotFoundException;
import ru.ifmo.sessionservice.mapper.MovieSessionMapper;
import ru.ifmo.sessionservice.mapper.UserMapper;
import ru.ifmo.sessionservice.model.MovieSession;
import ru.ifmo.sessionservice.model.User;
import ru.ifmo.sessionservice.model.UserSession;
import ru.ifmo.sessionservice.repository.MovieSessionRepository;

@Service
@RequiredArgsConstructor
public class MovieSessionService {
    private final MovieSessionRepository movieSessionRepository;

    private final UserServiceClient userServiceClient;
    private final UserSessionService userSessionService;

    public void save(MovieSession movieSession){
        movieSessionRepository.save(movieSession);
    }

    public void addMovie(String username, Long tmdbMovieId, boolean liked) {
        ResponseEntity<UserDTO> userDTO = userServiceClient.getUser(username);
        if (userDTO == null || userDTO.getStatusCode() != HttpStatus.OK) {
                throw new UsernameNotFoundException("User not found");
        }

        User user = UserMapper.INSTANCE.fromDomain(userDTO.getBody());

        UserSession userSession = userSessionService.findUserSessionByUser(user)
                .orElseThrow(()-> new SessionNotFoundException("The user is not a member of any session"));


        MovieSession movieSession = MovieSession.builder()
                .tmdbMovieId(tmdbMovieId)
                .user(user)
                .session(userSession.getSession())
                .liked(liked).build();

        this.save(movieSession);
    }

    public List<MovieSessionDTO> getLikedMovies(String username) {
        ResponseEntity<UserDTO> userDTO = userServiceClient.getUser(username);
        if (userDTO == null || userDTO.getStatusCode() != HttpStatus.OK) {
                throw new UsernameNotFoundException("User not found");
        }

        User user = UserMapper.INSTANCE.fromDomain(userDTO.getBody());
        UserSession userSession = userSessionService.findUserSessionByUser(user)
                .orElseThrow(()-> new SessionNotFoundException("The user is not a member of any session"));

        return movieSessionRepository.findAllByUserAndSessionAndLikedTrue(user, userSession.getSession())
                .orElseThrow(()-> new MoviesNotFound("Not found movies in session"))
                .stream().map(MovieSessionMapper.INSTANCE::toDomain).toList();
    }

    public List<MovieMatchedDTO> getMatchedMovies(String username) {
        ResponseEntity<UserDTO> userDTO = userServiceClient.getUser(username);
        if (userDTO == null || userDTO.getStatusCode() != HttpStatus.OK) {
                throw new UsernameNotFoundException("User not found");
        }

        User user = UserMapper.INSTANCE.fromDomain(userDTO.getBody());
        UserSession userSession = userSessionService.findUserSessionByUser(user)
                .orElseThrow(()-> new SessionNotFoundException("The user is not a member of any session"));

        List<MovieSession> currentUserSessions = movieSessionRepository.findAllByUserAndSessionAndLikedTrue(user, userSession.getSession())
                .orElseThrow(()-> new MoviesNotFound("Not found movies in session"));


        return currentUserSessions.stream().flatMap(currentUserSession ->
                movieSessionRepository.findAllByTmdbMovieIdAndSessionAndLikedTrue(
                        currentUserSession.getTmdbMovieId(), currentUserSession.getSession())
                        .orElseThrow(()-> new MoviesNotFound("Not found movies in session"))
                        .stream())
                .filter(session -> !session.getUser().getUsername().equals(username))
                .collect(Collectors.groupingBy(MovieSession::getTmdbMovieId))
                .entrySet().stream()
                .map(entry -> {
                    Long tmdbMovieId = entry.getKey();
                    boolean liked = entry.getValue().stream().anyMatch(MovieSession::getLiked);
                    List<UserDTO> users = entry.getValue().stream()
                            .map(movieSession -> UserMapper.INSTANCE.toDomain(movieSession.getUser()))
                            .collect(Collectors.toList());

                    return MovieMatchedDTO.builder()
                            .tmdbMovieId(tmdbMovieId)
                            .users(users)
                            .liked(liked)
                            .build();
                })
                .toList();
    }
}
