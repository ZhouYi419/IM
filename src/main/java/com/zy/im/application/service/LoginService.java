package com.zy.im.application.service;

import com.zy.im.api.dto.response.LoginResponse;
import com.zy.im.common.JwtUtil;
import com.zy.im.common.exception.BusinessException;
import com.zy.im.common.exception.ErrorCode;
import com.zy.im.domain.User;
import com.zy.im.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserMapper userMapper;

    public LoginService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public LoginResponse login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (!user.getPassword().equals(password)) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
        // 生成 token
        String token = JwtUtil.generateToken(user.getUuid(), user.getUsername());

        // 获取 token 过期时间
        Long expired = JwtUtil.getExpiration(token);

        // 返回包含 token 和过期时间的响应
        return new LoginResponse(token, expired);
    }
}