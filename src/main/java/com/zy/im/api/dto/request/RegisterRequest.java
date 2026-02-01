package com.zy.im.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    // 用户名
    @NotNull(message = "用户名不能为空")
    private String username;

    // 密码
    @NotNull(message = "密码不能为空")
    private String password;
}
