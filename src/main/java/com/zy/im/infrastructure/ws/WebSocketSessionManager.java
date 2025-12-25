package com.zy.im.infrastructure.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {
    private final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    // 添加用户session
    public void addSession(Long userId, WebSocketSession session) {
        userSessions.put(userId, session);
    }

    // 移除用户session
    public void removeSession(Long userId) {
        userSessions.remove(userId);
    }

    // 获取用户session
    public WebSocketSession getSession(Long userId) {
        return userSessions.get(userId);
    }

    // 获取所有在线用户
    public Collection<WebSocketSession> getAllSessions() {
        return userSessions.values();
    }
}
