package ru.ifmo.movieswipper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.ifmo.movieswipper.exception.PermissionDeniedException;
import ru.ifmo.movieswipper.exception.SessionNotFound;
import ru.ifmo.movieswipper.exception.UserExistInSessionException;
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
    private final SessionService sessionService;

    public void saveUserSession(UserSession userSession){
        userSessionRepository.save(userSession);
    }

    public boolean checkIsUserPresentInSession(User user, Session session){
        return userSessionRepository.existsUserSessionByUserAndSession(user, session);
    }

    public void join(Session session, String username){
        User user = userService.findByUsername(username).orElseThrow();
        if(this.checkIsUserPresentInSession(user, session)){
            throw new UserExistInSessionException("User already exists in session");
        }

        UserSession userSession = UserSession.builder()
                .session(session)
                .user(user).build();

        this.saveUserSession(userSession);
    }

    public void deleteSession(String username, String sessionCode){
        Session session = sessionService.findByCode(sessionCode)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new SessionNotFound("User not found"));


        if(!session.getCreator().getId().equals(user.getId())){
            throw new PermissionDeniedException("User does not have permission to delete this session");
        }

        sessionService.delete(session);
    }
}
