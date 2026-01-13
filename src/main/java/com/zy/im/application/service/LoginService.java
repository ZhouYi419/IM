package com.zy.im.application.service;

import com.zy.im.api.dto.request.LoginRequest;
import com.zy.im.api.dto.response.LoginResponse;
import com.zy.im.common.JwtUtil;
import com.zy.im.common.exception.BusinessException;
import com.zy.im.common.exception.ErrorCode;
import com.zy.im.domain.User;
import com.zy.im.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        // 1. 获取请求参数
        String username = request.getUsername();
        String password = request.getPassword();

        // 2. 查询账号是否存在
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 3. 验证密码
        if (!passwordEncoder.matches(password,user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // 4. 生成 token
        String token = JwtUtil.generateToken(user.getUuid(), user.getUsername());
        Long expired = JwtUtil.getExpiration(token);
        return new LoginResponse(token, expired);
    }
}