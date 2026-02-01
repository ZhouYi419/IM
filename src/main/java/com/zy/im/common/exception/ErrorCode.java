package com.zy.im.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(0, "ok"),
    // auth相关
    USER_NOT_FOUND(1001, "用户不存在"),
    PASSWORD_ERROR(1002, "密码错误"),
    USER_ALREADY_REGISTER(1003, "用户已注册"),

    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    DATA_EXIST_ERROR(40900, "数据已存在"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
