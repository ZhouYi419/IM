package com.zy.im.infrastructure.redis;

import com.zy.im.infrastructure.constant.RedisKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OnlineUserRepository {

    private final StringRedisTemplate redisTemplate;

    public void markOnline(Long userId, String sessionId) {
        redisTemplate.opsForValue()
                .set(RedisKey.ONLINE_USER + userId, sessionId);
        redisTemplate.opsForValue()
                .set(RedisKey.ONLINE_SESSION + sessionId, userId.toString());
    }

    public void removeBySessionId(String sessionId) {
        String userId = redisTemplate.opsForValue()
                .get(RedisKey.ONLINE_SESSION + sessionId);

        if (userId != null) {
            redisTemplate.delete(RedisKey.ONLINE_USER + userId);
            redisTemplate.delete(RedisKey.ONLINE_SESSION + sessionId);
        }
    }
}

