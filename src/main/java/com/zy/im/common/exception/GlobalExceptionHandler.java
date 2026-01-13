package com.zy.im.common.exception;

import com.zy.im.common.BaseResponse;
import com.zy.im.common.ResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> handleBusinessException(
            BusinessException e,
            HttpServletRequest request
    ) {
        log.warn("业务异常，path={}, code={}, msg={}",
                request.getRequestURI(),
                e.getCode(),
                e.getMessage());

        return ResultUtils.error(e.getCode(), e.getMessage());
    }
}