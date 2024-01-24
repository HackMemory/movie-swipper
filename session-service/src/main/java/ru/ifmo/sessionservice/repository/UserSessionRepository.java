package ru.ifmo.sessionservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ru.ifmo.sessionservice.model.Session;
import ru.ifmo.sessionservice.model.User;
import ru.ifmo.sessionservice.model.UserSession;

public interface UserSessionRepository extends CrudRepository<UserSession, Long> {
    boolean existsUserSessionByUserAndSession(User user, Session session);
    boolean existsUserSessionByUser(User user);
    List<UserSession> findAllBySession(Session session);
    Optional<UserSession> findUserSessionByUser(User user);
}
