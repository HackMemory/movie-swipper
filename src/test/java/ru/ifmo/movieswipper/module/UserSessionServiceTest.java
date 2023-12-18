package ru.ifmo.movieswipper.module;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ifmo.movieswipper.dto.UserDTO;
import ru.ifmo.movieswipper.dto.response.SessionResponse;
import ru.ifmo.movieswipper.exception.SessionNotFoundException;
import ru.ifmo.movieswipper.exception.UserExistInSessionException;
import ru.ifmo.movieswipper.mapper.UserMapper;
import ru.ifmo.movieswipper.model.Session;
import ru.ifmo.movieswipper.model.User;
import ru.ifmo.movieswipper.model.UserSession;
import ru.ifmo.movieswipper.repository.UserSessionRepository;
import ru.ifmo.movieswipper.service.SessionService;
import ru.ifmo.movieswipper.service.UserService;
import ru.ifmo.movieswipper.service.UserSessionService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSessionServiceTest {

    @Mock
    private UserSessionRepository userSessionRepository;

    @Mock
    private UserService userService;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private UserSessionService userSessionService;

    @Test
    void testCreateSession() {
        String username = "testUser";
        User user = new User();
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(userSessionRepository.existsUserSessionByUser(user)).thenReturn(false);

        assertDoesNotThrow(() -> userSessionService.createSession(username));

        verify(sessionService, times(1)).saveSession(any(Session.class));
        verify(userSessionRepository, times(1)).save(any(UserSession.class));
    }

    @Test
    void testCreateSession_UserAlreadyExistsInSession() {
        String username = "testUser";
        User user = new User();
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(userSessionRepository.existsUserSessionByUser(user)).thenReturn(true);

        assertThrows(UserExistInSessionException.class, () -> userSessionService.createSession(username));

        verify(sessionService, never()).saveSession(any(Session.class));
        verify(userSessionRepository, never()).save(any(UserSession.class));
    }

    @Test
    void testJoinSession() {
        String username = "testUser";
        String sessionCode = "testCode";
        User user = new User();
        Session session = new Session();
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(sessionService.findByCode(sessionCode)).thenReturn(Optional.of(session));
        when(userSessionRepository.existsUserSessionByUserAndSession(user, session)).thenReturn(false);

        assertDoesNotThrow(() -> userSessionService.joinSession(username, sessionCode));

        verify(userSessionRepository, times(1)).save(any(UserSession.class));
    }

    @Test
    void testJoinSession_UserAlreadyExistsInSession() {
        String username = "testUser";
        String sessionCode = "testCode";
        User user = new User();
        Session session = new Session();
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(sessionService.findByCode(sessionCode)).thenReturn(Optional.of(session));
        when(userSessionRepository.existsUserSessionByUserAndSession(user, session)).thenReturn(true);

        assertThrows(UserExistInSessionException.class, () -> userSessionService.joinSession(username, sessionCode));

        verify(userSessionRepository, never()).save(any(UserSession.class));
    }

    @Test
    void testExitFromSession() {
        String username = "testUser";
        User user = new User();
        UserSession userSession = new UserSession();
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(userSessionRepository.findUserSessionByUser(user)).thenReturn(Optional.of(userSession));

        assertDoesNotThrow(() -> userSessionService.exitFromSession(username));

        verify(userSessionRepository, times(1)).delete(userSession);
    }

    @Test
    void testExitFromSession_SessionNotFound() {
        String username = "testUser";
        User user = new User();
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(userSessionRepository.findUserSessionByUser(user)).thenReturn(Optional.empty());

        assertThrows(SessionNotFoundException.class, () -> userSessionService.exitFromSession(username));

        verify(userSessionRepository, never()).delete(any(UserSession.class));
    }

    @Test
    void testCurrentSession() {
        String username = "testUser";
        User user = new User();
        UserSession userSession = new UserSession();
        userSession.setSession(new Session());
        List<UserSession> userSessionList = List.of(userSession);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(userSessionRepository.findUserSessionByUser(user)).thenReturn(Optional.of(userSession));
        when(userSessionRepository.findAllBySession(userSession.getSession())).thenReturn(userSessionList);

        SessionResponse sessionResponse = userSessionService.currentSession(username);

        assertNotNull(sessionResponse);
        assertEquals(userSession.getSession().getId(), sessionResponse.getId());
        assertEquals(userSession.getSession().getInviteCode(), sessionResponse.getCode());

        verify(userSessionRepository, times(1)).findAllBySession(userSession.getSession());
    }
}
