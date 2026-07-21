package com.rc.mambasaerchbackend.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------- 业务异常 ----------

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("BusinessException [{}] {} - {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return BaseResponse.error(e.getCode(), e.getMessage());
    }

    // ---------- 参数校验异常 ----------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String msg = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        return BaseResponse.error(ErrorCode.PARAMS_ERROR, msg);
    }

    @ExceptionHandler(BindException.class)
    public BaseResponse<Void> handleBindException(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String msg = fieldError != null ? fieldError.getDefaultMessage() : "参数绑定失败";
        return BaseResponse.error(ErrorCode.PARAMS_ERROR, msg);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse<Void> handleMissingParam(MissingServletRequestParameterException e) {
        return BaseResponse.error(ErrorCode.PARAMS_ERROR, "缺少必要参数: " + e.getParameterName());
    }

    // ---------- 系统兜底 ----------

    @ExceptionHandler(Exception.class)
    public BaseResponse<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("SystemException [{}] {}", request.getRequestURI(), e.getMessage(), e);
        return BaseResponse.error(ErrorCode.SYSTEM_ERROR);
    }
}
