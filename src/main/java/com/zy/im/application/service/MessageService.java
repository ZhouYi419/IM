package com.zy.im.application.service;

import com.zy.im.domain.ChatMessage;
import com.zy.im.infrastructure.redis.OfflineMessageRepository;
import com.zy.im.infrastructure.ws.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final WebSocketSessionManager sessionManager;
    private final OfflineMessageRepository offlineMessageRepository;

    /**
     * 处理消息
     * @param chatMessage 消息对象
     */
    public void onMessage(ChatMessage chatMessage) {
        Long toUserId = chatMessage.getToUserId(); // 获取目标用户ID

        // 查找目标用户的 WebSocket session
        WebSocketSession targetSession = sessionManager.getSession(toUserId);

        if (targetSession != null && targetSession.isOpen()) {
            try {
                TextMessage textMessage = new TextMessage(chatMessage.getContent());
                targetSession.sendMessage(textMessage); // 发送消息给目标用户
                log.info("消息发送到用户: {}", toUserId);
            } catch (IOException e) {
                log.error("消息发送异常: {}", toUserId, e);
            }
        } else {
            offlineMessageRepository.saveOfflineMessage(toUserId, chatMessage);
            log.warn("用户不在线:{}", toUserId);
        }
    }
}

