package com.zy.im.api.controller;

import com.zy.im.api.dto.request.LoginRequest;
import com.zy.im.api.dto.request.RegisterRequest;
import com.zy.im.api.dto.response.LoginResponse;
import com.zy.im.application.service.PassportService;
import com.zy.im.common.BaseResponse;
import com.zy.im.common.ResultUtils;
import com.zy.im.common.exception.ErrorCode;
import com.zy.im.common.security.JwtUser;
import com.zy.im.common.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passport")
public class PassportController {

    private final PassportService passportService;

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody @Validated LoginRequest request) {
        LoginResponse response = passportService.login(request);
        return ResultUtils.success(response);
    }

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public BaseResponse<String> login(@RequestBody @Validated RegisterRequest request) {
        passportService.register(request);
        return ResultUtils.success("ok");
    }

    /**
     * 用户登出接口
     */
    @PostMapping("/logout")
    public BaseResponse<?> login() {
        JwtUser jwtUser = UserContext.get();
        if (jwtUser == null){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }

        UserContext.clear();
        return ResultUtils.success("ok");
    }
}