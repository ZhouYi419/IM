package com.zy.im.application.service;

import com.zy.im.api.dto.request.LoginRequest;
import com.zy.im.api.dto.request.RegisterRequest;
import com.zy.im.api.dto.response.LoginResponse;
import com.zy.im.common.JwtUtil;
import com.zy.im.common.exception.BusinessException;
import com.zy.im.common.exception.ErrorCode;
import com.zy.im.common.security.JwtUser;
import com.zy.im.common.security.UserContext;
import com.zy.im.domain.User;
import com.zy.im.infrastructure.enums.UserStatus;
import com.zy.im.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PassportService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户登录
     */
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

        // 5.设置UserContext
        JwtUser jwtUser = JwtUtil.parseToken(token);
        UserContext.set(jwtUser);
        return new LoginResponse(token, expired);
    }

    /**
     * 用户注册
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest request) {
        // 1. 获取请求参数
        String username = request.getUsername();
        String password = request.getPassword();

        // 2. 查询账号是否存在
        User user = userMapper.findByUsername(username);
        if (user != null) {
            throw new BusinessException(ErrorCode.USER_ALREADY_REGISTER);
        }

        // 3. 密码加密
        password = passwordEncoder.encode(password);

        // 4. 新用户注册入库
        User newUser = new User();
        newUser.setUuid(UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 8));
        newUser.setStatus(UserStatus.NORMAL.getCode());
        newUser.setUsername(username);
        newUser.setPassword(password);
        userMapper.insertUser(newUser);
    }
}