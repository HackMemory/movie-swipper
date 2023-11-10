package ru.ifmo.movieswipper.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.model.UserSession;

public interface UserSessionRepository extends CrudRepository<UserSession, Long> {
    boolean existsUserSessionByUserAndSession(User user, Session session);
}
