package com.zy.im.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonUtils {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {}

    /**
     * 对象 -> JSON 字符串
     */
    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转 JSON 失败", e);
            throw new RuntimeException("JSON 序列化失败");
        }
    }

    /**
     * JSON 字符串 -> 对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("JSON 解析失败: {}", json, e);
            throw new RuntimeException("JSON 反序列化失败");
        }
    }
}
