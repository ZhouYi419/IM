package com.zy.im.infrastructure.enums;

import lombok.Getter;

@Getter
public enum FriendApplyStatus {
    PENDING(0, "待处理"),
    ACCEPTED(1, "已同意"),
    REJECTED(2, "已拒绝");

    private final int code;
    private final String message;

    FriendApplyStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
