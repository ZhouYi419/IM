package com.zy.im.infrastructure.constant;

public final class RedisKey {
    // 在线用户
    public static final String ONLINE_USER = "online:user:";
    // 在线session
    public static final String ONLINE_SESSION = "online:session:";
    // 离线消息
    public static final String OFFLINE_MSG = "offline:msg:";
    // 单条消息
    public static final String CHAT_MSG = "chat:msg:";
    // 会话消息列表
    public static final String CHAT_SESSION = "chat:session:";
}
