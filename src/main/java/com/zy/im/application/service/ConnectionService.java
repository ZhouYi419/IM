package com.zy.im.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.im.common.JwtUtil;
import com.zy.im.common.security.JwtUser;
import com.zy.im.domain.ChatMessage;
import com.zy.im.infrastructure.redis.OfflineMessageRepository;
import com.zy.im.infrastructure.redis.OnlineUserRepository;
import com.zy.im.infrastructure.ws.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {

    private final OnlineUserRepository onlineUserRepository;
    private final WebSocketSessionManager sessionManager;
    private final OfflineMessageRepository offlineMessageRepository;

    /**
     * 用户连接
     * @param session WebSocketSession
     */
    public void onConnect(WebSocketSession session) {
        String token = getToken(session);
        log.info("获取到token{}",token);
        JwtUser jwtUser = JwtUtil.parseToken(token);

        Long userId = jwtUser.getUserId();
        sessionManager.addSession(userId, session);  // 添加session到sessionManager
        onlineUserRepository.markOnline(userId, session.getId());

        // 检查并发送离线消息
        List<String> offlineMessages = offlineMessageRepository.getOfflineMessages(userId, 10);
        for (String messageJson : offlineMessages) {
            try {
                ChatMessage chatMessage = new ObjectMapper().readValue(messageJson, ChatMessage.class);
                TextMessage textMessage = new TextMessage(chatMessage.getContent());
                session.sendMessage(textMessage);  // 发送离线消息
                log.info("离线消息发送给用户: {}", userId);
            } catch (IOException e) {
                log.error("离线消息发送失败: {}", e.getMessage());
            }
        }

        log.info("用户 {} 已连接, session={}", userId, session.getId());
    }

    /**
     * 用户断开连接
     * @param session WebSocketSession
     */
    public void onDisconnect(WebSocketSession session) {
        String token = getToken(session);
        JwtUser jwtUser = JwtUtil.parseToken(token);
        Long userId = jwtUser.getUserId();
        onlineUserRepository.removeBySessionId(session.getId());
        sessionManager.removeSession(userId);  // 移除session

        log.info("用户 {} 断开连接, session={}", userId, session.getId());
    }

    /**
     * 从请求头获取 Token
     */
    private String getToken(WebSocketSession session) {
        // 获取请求头中的 "Authorization" 字段
        String authHeader = session.getHandshakeHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // 去掉 "Bearer " 前缀
        }
        throw new IllegalArgumentException("Authorization header is missing or invalid");
    }
}

