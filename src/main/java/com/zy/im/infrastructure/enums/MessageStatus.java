package com.zy.im.infrastructure.enums;

public enum MessageStatus {
    SENT,       // 服务端已接收
    DELIVERED,  // 已投递到对方
    READ        // 对方已读
}
