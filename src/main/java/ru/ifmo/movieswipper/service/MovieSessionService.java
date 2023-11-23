package ru.ifmo.movieswipper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.exception.MoviesNotFound;
import ru.ifmo.movieswipper.exception.SessionNotFoundException;
import ru.ifmo.movieswipper.model.MovieSession;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.model.UserSession;
import ru.ifmo.movieswipper.repository.MovieSessionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieSessionService {
    private final MovieSessionRepository movieSessionRepository;

    private final SessionService sessionService;
    private final UserService userService;
    private final UserSessionService userSessionService;

    public void save(MovieSession movieSession){
        movieSessionRepository.save(movieSession);
    }

    public void addMovie(String username, Long tmdbMovieId, boolean liked){
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserSession userSession = userSessionService.findUserSessionByUser(user)
                .orElseThrow(()-> new SessionNotFoundException("The user is not a member of any session"));


        MovieSession movieSession = MovieSession.builder()
                .tmdbMovieId(tmdbMovieId)
                .user(user)
                .session(userSession.getSession())
                .liked(liked).build();

        this.save(movieSession);
    }

    public Optional<List<MovieSession>> getLikedMovies(String username){
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserSession userSession = userSessionService.findUserSessionByUser(user)
                .orElseThrow(()-> new SessionNotFoundException("The user is not a member of any session"));

        return movieSessionRepository.findAllByUserAndSessionAndLikedTrue(user, userSession.getSession());
    }

    public List<MovieSession> getMatchedMovies(String username){
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserSession userSession = userSessionService.findUserSessionByUser(user)
                .orElseThrow(()-> new SessionNotFoundException("The user is not a member of any session"));

        List<MovieSession> currentUserSessions = movieSessionRepository.findAllByUserAndSessionAndLikedTrue(user, userSession.getSession())
                .orElseThrow(()-> new MoviesNotFound("Not found movies in session"));


        List<MovieSession> matchedMovies = currentUserSessions.stream().flatMap(currentUserSession ->
                movieSessionRepository.findAllByTmdbMovieIdAndSessionAndLikedTrue(
                        currentUserSession.getTmdbMovieId(), currentUserSession.getSession()
                ).orElseThrow(()-> new MoviesNotFound("Not found movies in session")).stream())
                .filter(session ->
                        !session.getUser().getUsername().equals(username)
                ).toList();

        System.out.println(matchedMovies);

        return matchedMovies;
    }
}
