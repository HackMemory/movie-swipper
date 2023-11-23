package ru.ifmo.movieswipper.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.model.UserSession;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends CrudRepository<UserSession, Long> {
    boolean existsUserSessionByUserAndSession(User user, Session session);
    boolean existsUserSessionByUser(User user);
    List<UserSession> findAllBySession(Session session);

    Optional<UserSession> findUserSessionByUser(User user);
}
