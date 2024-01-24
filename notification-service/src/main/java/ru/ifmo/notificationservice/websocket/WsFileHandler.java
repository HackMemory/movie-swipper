package ru.ifmo.notificationservice.websocket;

import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.notificationservice.dto.FileInfoDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class WsFileHandler implements StompSessionHandler {
    @Value("${ws-server}")
    private String wsServerUrl;

    @Value("${subscribe-topic}")
    private String subscribeTopic;

    private StompSession stompSession;

    private final JavaMailSender emailSender;

    @EventListener(value = ApplicationReadyEvent.class)
    public void connect() {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            stompSession = stompClient.connectAsync(wsServerUrl, this).get();
            log.info("Connected to WS server");
        } catch (Exception e) {
            log.error("Connection to WS server failed {}", e.getMessage());
        }
    }


    public void subscribe(String topicId) {
        log.info("Subscribing to topic: {}", topicId);
        stompSession.subscribe(topicId, this);
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("Connection to STOMP server established.\n" +
                "Session: " + session + "\n" +
                "Headers: " + connectedHeaders + "\n");
        subscribe(subscribeTopic);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        if (!session.isConnected()) {
            connect();
        }

    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return FileInfoDto.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        FileInfoDto fileInfoDto = (FileInfoDto) payload;
        log.info("Payload: {}", fileInfoDto);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@movieswipper.ru");
        message.setTo(fileInfoDto.getEmail());
        message.setSubject("MovieSwipper avatar update");
        message.setText("You updated your avatar: " + fileInfoDto.getUuid());

        log.info("Message: {}", message);
        emailSender.send(message);
    }


    @PreDestroy
    void onShutDown() {
        if (stompSession != null) {
            stompSession.disconnect();
        }
    }

}
