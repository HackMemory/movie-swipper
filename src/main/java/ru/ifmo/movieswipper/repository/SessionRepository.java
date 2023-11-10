package ru.ifmo.movieswipper.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.movieswipper.model.Session;

import java.util.Optional;

@Repository
public interface SessionRepository extends CrudRepository<Session, String> {
    Optional<Session> findByInviteCode(String code);
}
