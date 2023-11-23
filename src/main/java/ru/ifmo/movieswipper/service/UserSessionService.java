package ru.ifmo.movieswipper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.movieswipper.dto.UserDTO;
import ru.ifmo.movieswipper.dto.response.SessionResponse;
import ru.ifmo.movieswipper.exception.SessionNotFoundException;
import ru.ifmo.movieswipper.exception.UserExistInSessionException;
import ru.ifmo.movieswipper.mapper.UserMapper;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.model.UserSession;
import ru.ifmo.movieswipper.repository.UserSessionRepository;

import java.util.List;
import java.util.Optional;

import static ru.ifmo.movieswipper.util.StringUtils.generateRandomString;

@Service
@RequiredArgsConstructor
public class UserSessionService {
    private final UserSessionRepository userSessionRepository;

    private final UserService userService;
    private final SessionService sessionService;

    public Optional<UserSession> findUserSessionByUser(User user){
        return userSessionRepository.findUserSessionByUser(user);
    }

    public void saveUserSession(UserSession userSession){
        userSessionRepository.save(userSession);
    }

    public boolean checkIsUserPresentInSession(User user, Session session){
        return userSessionRepository.existsUserSessionByUserAndSession(user, session);
    }

    public boolean checkIsUserPresent(User user){
        return userSessionRepository.existsUserSessionByUser(user);
    }

    public SessionResponse currentSession(String username){
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserSession userSession = userSessionRepository.findUserSessionByUser(user)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        List<UserDTO> userList = userSessionRepository.findAllBySession(userSession.getSession()).stream()
                .map(UserSession::getUser)
                .map(UserMapper.INSTANCE::toDomain)
                .toList();

        return SessionResponse.builder()
                .id(userSession.getSession().getId())
                .code(userSession.getSession().getInviteCode())
                .users(userList).build();
    }


    @Transactional
    public Session createSession(String username){
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(this.checkIsUserPresent(user)){
            throw new UserExistInSessionException("User already exists in session");
        }

        String code = generateRandomString(8);
        Session session = Session.builder()
                .inviteCode(code)
                .creator(user).build();
        sessionService.saveSession(session);
        
        UserSession userSession = UserSession.builder()
                .session(session)
                .user(user).build();
        userSessionRepository.save(userSession);

        return session;
    }

    public void joinSession(String username, String sessionCode){
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Session session = sessionService.findByCode(sessionCode)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        if(this.checkIsUserPresentInSession(user, session)){
            throw new UserExistInSessionException("User already exists in session");
        }

        UserSession userSession = UserSession.builder()
                .session(session)
                .user(user).build();

        this.saveUserSession(userSession);
    }

    public void deleteSession(String sessionCode){
        Session session = sessionService.findByCode(sessionCode)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        sessionService.delete(session);
    }

    public void exitFromSession(String username){
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserSession userSession = userSessionRepository.findUserSessionByUser(user)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));
        userSessionRepository.delete(userSession);
    }
}
