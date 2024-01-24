package ru.ifmo.sessionservice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.ifmo.sessionservice.model.Session;
import ru.ifmo.sessionservice.model.User;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
    Optional<Session> findByInviteCode(String code);
    Optional<Session> findSessionByCreator(User user);
}
