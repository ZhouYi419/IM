package com.zy.im.infrastructure.enums;

import lombok.Getter;

@Getter
public enum FriendStatus {
    NORMAL(0, "正常"),
    DELETED(1, "已删除");

    private final int code;
    private final String desc;

    FriendStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
