package com.zy.im.api.controller;

import com.zy.im.api.dto.request.LoginRequest;
import com.zy.im.api.dto.response.LoginResponse;
import com.zy.im.application.service.LoginService;
import com.zy.im.common.BaseResponse;
import com.zy.im.common.ResultUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passport")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody @Validated LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String token = loginService.login(username, password);
        return ResultUtils.success(new LoginResponse(token));
    }
}
