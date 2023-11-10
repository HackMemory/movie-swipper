package ru.ifmo.movieswipper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.model.UserSession;
import ru.ifmo.movieswipper.repository.UserSessionRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSessionService {
    private final UserSessionRepository userSessionRepository;

    private final UserService userService;

    public void saveUserSession(UserSession userSession){
        userSessionRepository.save(userSession);
    }

    public boolean checkIsUserPresentInSession(User user, Session session){
        return userSessionRepository.existsUserSessionByUserAndSession(user, session);
    }

    public void join(Session session, String username){
        Optional<User> user = userService.findByUsername(username);
        if(this.checkIsUserPresentInSession(user.get(), session)){
            throw new IllegalArgumentException("User already exists");
        }

        UserSession userSession = UserSession.builder()
                .session(session)
                .user(user.get()).build();

        this.saveUserSession(userSession);
    }
}
