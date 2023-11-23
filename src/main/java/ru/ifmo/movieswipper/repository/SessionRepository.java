package ru.ifmo.movieswipper.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.model.User;

import java.util.Optional;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
    Optional<Session> findByInviteCode(String code);
    Optional<Session> findSessionByCreator(User user);
}
