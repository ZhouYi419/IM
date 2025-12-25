package com.zy.im.application.service;

import com.zy.im.common.JwtUtil;
import com.zy.im.common.security.JwtUser;
import com.zy.im.infrastructure.redis.OnlineUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {

    private final OnlineUserRepository onlineUserRepository;

    public void onConnect(WebSocketSession session) {
        String token = getToken(session);
        log.info("获取到token{}",token);
        JwtUser jwtUser = JwtUtil.parseToken(token);

        Long userId = jwtUser.getUserId();
        onlineUserRepository.markOnline(userId, session.getId());

        log.info("User {} connected, session={}", userId, session.getId());
    }

    public void onDisconnect(WebSocketSession session) {
        onlineUserRepository.removeBySessionId(session.getId());
        log.info("session {} disconnected", session.getId());
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

