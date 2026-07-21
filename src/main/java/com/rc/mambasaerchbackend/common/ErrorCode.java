package com.rc.mambasaerchbackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义错误码
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NULL_ERROR(40001, "请求数据为空"),
    NOT_LOGIN(40100, "未登录"),
    NO_AUTH(40101, "无权限"),
    FORBIDDEN(40300, "禁止操作"),
    NOT_FOUND(40400, "资源不存在"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),
    ;

    private final int code;
    private final String message;
}
