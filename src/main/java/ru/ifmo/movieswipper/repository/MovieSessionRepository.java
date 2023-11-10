package ru.ifmo.movieswipper.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ifmo.movieswipper.model.MovieSession;

public interface MovieSessionRepository extends CrudRepository<MovieSession, String> {
}
