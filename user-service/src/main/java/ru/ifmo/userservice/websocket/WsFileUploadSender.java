package ru.ifmo.userservice.websocket;

import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.userservice.dto.FileInfoDto;


@Component
@RequiredArgsConstructor
@Slf4j
public class WsFileUploadSender implements StompSessionHandler {
    
    @Value("${ws-server}")
    private String wsServerUrl;

    @Value("${destination-topic}")
    private String destinationTopic;

    @Value("${subscribe-topic}")
    private String subscribeTopic;


    private StompSession stompSession;

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

    public void sendMessage(FileInfoDto fileInfoDto) {
        System.out.println(fileInfoDto);
        stompSession.send(destinationTopic, fileInfoDto);
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

    }

}
