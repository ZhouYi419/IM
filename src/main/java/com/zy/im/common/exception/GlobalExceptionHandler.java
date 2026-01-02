package com.zy.im.common.exception;

import com.zy.im.common.BaseResponse;
import com.zy.im.common.ResultUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public BaseResponse<?> handleException(BusinessException e) {
        return ResultUtils.error(500, "系统错误");
    }
}