package com.zy.im.infrastructure.ws;

import com.zy.im.application.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImWebSocketHandler extends TextWebSocketHandler {

    private final ConnectionService connectionService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        connectionService.onConnect(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        connectionService.onDisconnect(session);
    }
}
