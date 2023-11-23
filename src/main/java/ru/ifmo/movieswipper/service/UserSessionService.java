package ru.ifmo.movieswipper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.dto.UserDTO;
import ru.ifmo.movieswipper.dto.response.SessionResponse;
import ru.ifmo.movieswipper.exception.PermissionDeniedException;
import ru.ifmo.movieswipper.exception.SessionNotFoundException;
import ru.ifmo.movieswipper.exception.UserExistInSessionException;
import ru.ifmo.movieswipper.mapper.UserMapper;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.model.UserSession;
import ru.ifmo.movieswipper.repository.UserSessionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.ifmo.movieswipper.util.StringUtils.generateRandomString;

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

    public boolean checkIsUserPresent(User user){
        return userSessionRepository.existsUserSessionByUser(user);
    }

    public SessionResponse currentSession(String username){
        Session session = sessionService.findByUser(username)
                .orElseThrow(()-> new SessionNotFoundException("The user is not a member of any session"));

        List<UserDTO> userList = userSessionRepository.findAllBySession(session).stream()
                .map(UserSession::getUser)
                .map(UserMapper.INSTANCE::toDomain)
                .toList();

        return SessionResponse.builder()
                .id(session.getId())
                .code(session.getInviteCode())
                .users(userList).build();
    }


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
        this.saveUserSession(userSession);

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

    public void deleteSession(String username, String sessionCode){
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Session session = sessionService.findByCode(sessionCode)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        if(!session.getCreator().getId().equals(user.getId())){
            throw new PermissionDeniedException("User doesn't have permission to delete this session");
        }

        sessionService.delete(session);
    }
}
