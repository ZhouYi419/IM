package com.zy.im.application.service;

import com.zy.im.common.JsonUtils;
import com.zy.im.domain.ChatMessage;
import com.zy.im.infrastructure.enums.MessageStatus;
import com.zy.im.infrastructure.redis.MessageRepository;
import com.zy.im.infrastructure.redis.OfflineMessageRepository;
import com.zy.im.infrastructure.ws.WebSocketSessionManager;
import com.zy.im.infrastructure.ws.WsMessage;
import com.zy.im.infrastructure.ws.WsMessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final WebSocketSessionManager sessionManager;
    private final OfflineMessageRepository offlineMessageRepository;
    private final MessageRepository messageRepository;

    /**
     * 处理消息
     * @param chatMessage 消息对象
     */
    public void onMessage(ChatMessage chatMessage) {
        // 1. 服务端接收成功
        chatMessage.setStatus(MessageStatus.SENT);
        chatMessage.setMsgId(UUID.randomUUID().toString());
        chatMessage.setTimestamp(System.currentTimeMillis());

        //保存消息
        messageRepository.save(chatMessage);

        // 2. 尝试投递
        WebSocketSession targetSession = sessionManager.getSession(chatMessage.getToUserId());
        if (targetSession != null && targetSession.isOpen()) {
            sendToClient(targetSession, WsMessageType.CHAT, chatMessage);
            // 3. 投递成功
            chatMessage.setStatus(MessageStatus.DELIVERED);
        } else {
            offlineMessageRepository.saveOfflineMessage(chatMessage.getToUserId(), chatMessage);
        }

        // 4. 回 ACK 给发送方
        WebSocketSession senderSession = sessionManager.getSession(chatMessage.getFromUserId());
        if (senderSession != null) {
            sendToClient(senderSession, WsMessageType.ACK, chatMessage);
        }
    }

    /**
     * 处理已读
     * @param msgId 消息ID
     */
    public void onRead(String msgId) {

        ChatMessage msg = messageRepository.get(msgId);
        if (msg == null) {
            log.warn("READ 消息不存在 msgId={}", msgId);
            return;
        }

        // 已读幂等
        if (msg.getStatus() == MessageStatus.READ) {
            return;
        }

        // 更新状态
        messageRepository.updateStatus(msgId, MessageStatus.READ);

        // 通知发送者
        WebSocketSession sender =
                sessionManager.getSession(msg.getFromUserId());
        if (sender != null && sender.isOpen()) {
            try {
                sendToClient(sender, WsMessageType.READ, Map.of("msgId", msgId));
            }catch (Exception e){
                log.error("READ 回执发送失败", e);
            }
        }
        log.info("消息已读, msgId={}", msgId);
    }


    //==================private=============================
    private void sendToClient(WebSocketSession session, String type, Object data) {
        try {
            WsMessage<Object> ws = new WsMessage<>();
            ws.setType(type);
            ws.setData(data);

            String json = JsonUtils.MAPPER.writeValueAsString(ws);
            session.sendMessage(new TextMessage(json));
        } catch (Exception e) {
            log.error("WS发送失败", e);
        }
    }
}

