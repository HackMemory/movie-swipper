package ru.ifmo.sessionservice.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ifmo.sessionservice.client.UserServiceClient;
import ru.ifmo.sessionservice.dto.UserDTO;
import ru.ifmo.sessionservice.mapper.UserMapper;
import ru.ifmo.sessionservice.model.Session;
import ru.ifmo.sessionservice.model.User;
import ru.ifmo.sessionservice.repository.SessionRepository;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    private final UserServiceClient userServiceClient;

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
        ResponseEntity<UserDTO> userDTO = userServiceClient.getUser(username);
        if (userDTO == null || userDTO.getStatusCode() != HttpStatus.OK) {
                throw new UsernameNotFoundException("User not found");
        }

        User user = UserMapper.INSTANCE.fromDomain(userDTO.getBody());
        return sessionRepository.findSessionByCreator(user);
    }
}
