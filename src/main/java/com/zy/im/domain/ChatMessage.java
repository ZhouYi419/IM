package com.zy.im.domain;

import lombok.Data;

@Data
public class ChatMessage {
    /**
     * 发送者id
     */
    private Long fromUserId;
    /**
     * 接收者id
     */
    private Long toUserId;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 时间戳
     */
    private Long timestamp;
}