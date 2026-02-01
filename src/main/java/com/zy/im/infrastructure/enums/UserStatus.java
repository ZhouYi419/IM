package com.zy.im.infrastructure.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    NORMAL(0, "正常"),
    FREEZE(1, "冻结");

    private final int code;
    private final String desc;

    UserStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
