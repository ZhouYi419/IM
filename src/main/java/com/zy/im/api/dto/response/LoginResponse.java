package com.zy.im.api.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private Long expired;

    public LoginResponse() {
    }

    public LoginResponse(String token) {
        this.token = token;
    }

    public LoginResponse(String token, Long expired) {
        this.token = token;
        this.expired = expired;
    }
}
