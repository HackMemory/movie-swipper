package ru.ifmo.movieswipper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.exception.SessionNotFoundException;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.repository.SessionRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    private final UserService userService;

    public Session saveSession(Session session) {
        return sessionRepository.save(session);
    }

    public void delete(Session session){
        sessionRepository.delete(session);
    }
    public Optional<Session> findByCode(String code) {
        return sessionRepository.findByInviteCode(code);
    }

    public Optional<Session> findByUser(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return sessionRepository.findSessionByCreator(user);
    }
}
