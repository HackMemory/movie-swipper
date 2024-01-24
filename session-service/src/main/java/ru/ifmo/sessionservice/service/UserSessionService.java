package ru.ifmo.sessionservice.service;

import static ru.ifmo.sessionservice.util.StringUtils.generateRandomString;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.ifmo.sessionservice.client.UserServiceClient;
import ru.ifmo.sessionservice.dto.UserDTO;
import ru.ifmo.sessionservice.dto.response.SessionResponse;
import ru.ifmo.sessionservice.exception.SessionNotFoundException;
import ru.ifmo.sessionservice.exception.UserExistInSessionException;
import ru.ifmo.sessionservice.mapper.UserMapper;
import ru.ifmo.sessionservice.model.Session;
import ru.ifmo.sessionservice.model.User;
import ru.ifmo.sessionservice.model.UserSession;
import ru.ifmo.sessionservice.repository.UserSessionRepository;

@Service
@RequiredArgsConstructor
public class UserSessionService {
    private final UserSessionRepository userSessionRepository;
    private final SessionService sessionService;

    @Autowired
    private final UserServiceClient userServiceClient;

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
        ResponseEntity<UserDTO> userDTO = userServiceClient.getUser(username);
        if (userDTO == null || userDTO.getStatusCode() != HttpStatus.OK) {
                throw new UsernameNotFoundException("User not found");
        }

        User user = UserMapper.INSTANCE.fromDomain(userDTO.getBody());

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
        ResponseEntity<UserDTO> userDTO = userServiceClient.getUser(username);
        if (userDTO == null || userDTO.getStatusCode() != HttpStatus.OK) {
                throw new UsernameNotFoundException("User not found");
        }

        User user = UserMapper.INSTANCE.fromDomain(userDTO.getBody());

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
        ResponseEntity<UserDTO> userDTO = userServiceClient.getUser(username);
        if (userDTO == null || userDTO.getStatusCode() != HttpStatus.OK) {
                throw new UsernameNotFoundException("User not found");
        }

        User user = UserMapper.INSTANCE.fromDomain(userDTO.getBody());

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
        ResponseEntity<UserDTO> userDTO = userServiceClient.getUser(username);
        if (userDTO == null || userDTO.getStatusCode() != HttpStatus.OK) {
                throw new UsernameNotFoundException("User not found");
        }

        User user = UserMapper.INSTANCE.fromDomain(userDTO.getBody());

        UserSession userSession = userSessionRepository.findUserSessionByUser(user)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));
        userSessionRepository.delete(userSession);
    }
}
