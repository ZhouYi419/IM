package com.zy.im.application.service;

import com.zy.im.common.JwtUtil;
import com.zy.im.common.security.JwtUser;
import com.zy.im.infrastructure.redis.OnlineUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import java.util.Objects;

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

    private String getToken(WebSocketSession session) {
        return Objects.requireNonNull(session.getUri())
                .getQuery()
                .replace("token=", "");
    }
}

