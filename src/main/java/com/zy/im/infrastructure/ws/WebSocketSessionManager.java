package com.zy.im.infrastructure.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void add(String uuid , WebSocketSession session){
        sessions.put(uuid,session);
    }

    public void remove(String uuid , WebSocketSession session){
        sessions.remove(uuid,session);
    }

    public WebSocketSession get(String uuid){
        return sessions.get(uuid);
    }
}
