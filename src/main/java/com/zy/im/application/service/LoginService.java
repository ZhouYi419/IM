package com.zy.im.application.service;

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

    public String login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (!user.getPassword().equals(password)) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
        return JwtUtil.generateToken(user.getId(), user.getUsername());
    }
}