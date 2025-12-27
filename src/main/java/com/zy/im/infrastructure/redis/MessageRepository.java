package com.zy.im.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.im.domain.ChatMessage;
import com.zy.im.infrastructure.constant.RedisKey;
import com.zy.im.infrastructure.enums.MessageStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepository {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 保存消息
     */
    public void save(ChatMessage msg) {
        try {
            redisTemplate.opsForValue().set(
                    RedisKey.CHAT_MSG + msg.getMsgId(),
                    objectMapper.writeValueAsString(msg)
            );

            String sessionKey = buildSessionKey(
                    msg.getFromUserId(), msg.getToUserId());

            redisTemplate.opsForList().rightPush(
                    RedisKey.CHAT_SESSION + sessionKey,
                    msg.getMsgId()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新消息状态
     */
    public void updateStatus(String msgId, MessageStatus status) {
        ChatMessage msg = get(msgId);
        msg.setStatus(status);
        save(msg);
    }

    public ChatMessage get(String msgId) {
        try {
            String json = redisTemplate.opsForValue()
                    .get(RedisKey.CHAT_MSG + msgId);
            return objectMapper.readValue(json, ChatMessage.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


//====================private=============================
    private String buildSessionKey(Long a, Long b) {
        return a < b ? a + ":" + b : b + ":" + a;
    }
}
