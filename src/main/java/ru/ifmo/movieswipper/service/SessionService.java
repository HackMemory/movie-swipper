package ru.ifmo.movieswipper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.repository.SessionRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    public Session saveSession(Session session) {
        return sessionRepository.save(session);
    }

    public Optional<Session> findByCode(String code) {
        return sessionRepository.findByInviteCode(code);
    }

}
