package com.zy.im.domain;

import com.zy.im.infrastructure.enums.MessageStatus;
import lombok.Data;

@Data
public class ChatMessage {
    /** 消息唯一ID */
    private String msgId;

    /** 发送者 */
    private Long fromUserId;

    /** 接收者 */
    private Long toUserId;

    /** 内容 */
    private String content;

    /** 时间戳 */
    private Long timestamp;

    /** 消息状态 */
    private MessageStatus status;
}