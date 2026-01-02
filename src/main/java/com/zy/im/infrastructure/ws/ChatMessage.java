package com.zy.im.infrastructure.ws;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ChatMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // 发送人uuid
    private String uuid;

    // 被发送人uuid
    private String toUuid;

    // 消息
    private String message;

    // 时间戳
    private LocalDateTime timeStamp;
}
