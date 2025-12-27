package com.zy.im.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {
    public static final ObjectMapper MAPPER = new ObjectMapper();
}
