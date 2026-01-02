package com.zy.im.application.service;

import com.zy.im.infrastructure.ws.ChatMessage;
import com.zy.im.infrastructure.ws.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final WebSocketSessionManager sessionManager;

    public void send(ChatMessage chatMessage){
        String toUuid = chatMessage.getToUuid();
        WebSocketSession target = sessionManager.get(toUuid);

        if(target != null && target.isOpen()){
            try{
                target.sendMessage(
                        new TextMessage(chatMessage.getMessage())
                );
                log.info("消息发送给 {}", toUuid);
            }catch (Exception e){
                log.error("发送失败", e);
            }
        }else{
            log.info("用户 {} 不在线", toUuid);
        }

    }

}
