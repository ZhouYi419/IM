package com.zy.im.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private Long expired;
}
