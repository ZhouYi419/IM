package com.zy.im.infrastructure.ws;

import lombok.Data;

@Data
public class WsMessage<T> {

    /**
     * 消息类型
     * CHAT / ACK / READ
     */
    private String type;

    /**
     * 消息体
     */
    private T data;
}
