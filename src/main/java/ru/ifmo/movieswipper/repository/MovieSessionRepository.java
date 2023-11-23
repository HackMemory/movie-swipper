package ru.ifmo.movieswipper.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ifmo.movieswipper.model.MovieSession;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.model.User;

import java.util.List;
import java.util.Optional;

public interface MovieSessionRepository extends CrudRepository<MovieSession, String> {
    Optional<List<MovieSession>> findAllByUserAndSessionAndLikedTrue(User user, Session session);
    Optional<List<MovieSession>> findAllByTmdbMovieIdAndSessionAndLikedTrue(Long tmdbMovieId, Session session);
}
