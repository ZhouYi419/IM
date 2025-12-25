package com.zy.im.infrastructure.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.im.application.service.ConnectionService;
import com.zy.im.application.service.MessageService;
import com.zy.im.common.security.JwtUser;
import com.zy.im.common.JwtUtil;
import com.zy.im.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImWebSocketHandler extends TextWebSocketHandler {

    private final ConnectionService connectionService;
    private final MessageService messageService;

    /**
     * 用户连接
     * @param session WebSocketSession 对象
     * @throws Exception 抛出异常
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        connectionService.onConnect(session); // 用户连接
    }

    /**
     * 用户断开
     * @param session WebSocketSession 对象
     * @param status 关闭状态
     * @throws Exception 抛出异常
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        connectionService.onDisconnect(session); // 用户断开
    }

    /**
     * 接收到消息
     * @param session WebSocketSession 对象
     * @param message 接收到的消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
        String payload = message.getPayload(); // 接收到的消息内容
        log.info("收到消息: {}", payload);

        // 创建 ChatMessage
        ChatMessage chatMessage = parseChatMessage(payload);

        // 解析 JWT Token，获取发送者信息
        JwtUser jwtUser = JwtUtil.parseToken(getTokenFromSession(session));
        chatMessage.setFromUserId(jwtUser.getUserId()); // 设置发送者ID

        // 将消息传递给目标用户
        messageService.onMessage(chatMessage);
    }

    /**
     * 从 WebSocketSession 中获取 token
     * @param session WebSocketSession 对象
     * @return  token
     */
    private String getTokenFromSession(WebSocketSession session) {
        // 从 HTTP 请求头中获取 "Authorization" 字段
        String authorizationHeader = session.getHandshakeHeaders().getFirst("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // 截取掉 "Bearer " 前缀，得到 token
            return authorizationHeader.substring(7);
        }
        return null; // 如果没有 token，返回 null 或者可以抛出异常
    }

    /**
     * 解析消息信息
     * @param payload 消息内容
     * @return ChatMessage 对象
     */
    private ChatMessage parseChatMessage(String payload) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(payload, ChatMessage.class);
        } catch (IOException e) {
            log.error("消息体解析异常", e);
            throw new IllegalArgumentException("消息异常");
        }
    }
}