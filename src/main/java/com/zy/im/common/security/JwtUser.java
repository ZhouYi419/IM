package com.zy.im.common.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser {
    // 用户名
    private String username;
    // 用户uuid
    private String uuid;
}
