package ru.ifmo.sessionservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ru.ifmo.sessionservice.model.MovieSession;
import ru.ifmo.sessionservice.model.Session;
import ru.ifmo.sessionservice.model.User;

public interface MovieSessionRepository extends CrudRepository<MovieSession, String> {
    Optional<List<MovieSession>> findAllByUserAndSessionAndLikedTrue(User user, Session session);
    Optional<List<MovieSession>> findAllByTmdbMovieIdAndSessionAndLikedTrue(Long tmdbMovieId, Session session);
}
