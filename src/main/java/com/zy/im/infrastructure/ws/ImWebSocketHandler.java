package com.zy.im.infrastructure.ws;

import com.zy.im.application.service.MessageService;
import com.zy.im.common.JsonUtils;
import com.zy.im.common.JwtUtil;
import com.zy.im.common.security.JwtUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionManager sessionManager;
    private final MessageService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = JwtUtil.getToken(session);
        JwtUser user = JwtUtil.parseToken(token);

        sessionManager.add(user.getUuid(),session);
        log.info("用户 {} 建立 WS 连接", user.getUuid());

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String token = JwtUtil.getToken(session);
        JwtUser user = JwtUtil.parseToken(token);

        sessionManager.remove(user.getUuid(),session);
        log.info("用户 {} 断开 WS 连接", user.getUuid());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        ChatMessage chatMessage = JsonUtils.fromJson(
                message.getPayload(), ChatMessage.class
        );
        messageService.send(chatMessage);
    }
}
