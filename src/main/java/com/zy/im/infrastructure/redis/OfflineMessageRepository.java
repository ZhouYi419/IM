package com.zy.im.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.im.domain.ChatMessage;
import com.zy.im.infrastructure.constant.RedisKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OfflineMessageRepository {

    private final StringRedisTemplate redisTemplate;

    /**
     * 保存用户离线消息
     * @param userId 用户ID
     * @param chatMessage 消息对象
     */
    public void saveOfflineMessage(Long userId, ChatMessage chatMessage) {
        // 将消息序列化为 JSON 格式，保存到 Redis
        String messageKey = RedisKey.OFFLINE_MSG + userId;
        String messageJson = convertMessageToJson(chatMessage);  // 假设你有一个方法来序列化消息

        // 将消息添加到队列（List），可以使用 LPush 或 RPush 添加到列表中
        redisTemplate.opsForList().leftPush(messageKey, messageJson);
    }

    /**
     * 获取用户所有的离线消息
     * @param userId 用户ID
     * @param maxMessages 最大消息数
     * @return 离线消息列表
     */
    public List<String> getOfflineMessages(Long userId, int maxMessages) {
        String messageKey = RedisKey.OFFLINE_MSG + userId;
        // 获取最多 maxMessages 条离线消息
        return redisTemplate.opsForList().range(messageKey, 0, maxMessages - 1);
    }

    /**
     * 删除用户所有的离线消息
     * @param userId 用户ID
     */
    public void removeOfflineMessages(Long userId) {
        String messageKey = RedisKey.OFFLINE_MSG + userId;
        redisTemplate.delete(messageKey);
    }


//==========================private=============================================

    /**
     * 将 ChatMessage 对象转换为 JSON
     * @param chatMessage 聊天消息
     * @return JSON 字符串
     */
    private String convertMessageToJson(ChatMessage chatMessage) {
        // 使用 Jackson 进行序列化
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(chatMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert chat message to JSON", e);
        }
    }
}
