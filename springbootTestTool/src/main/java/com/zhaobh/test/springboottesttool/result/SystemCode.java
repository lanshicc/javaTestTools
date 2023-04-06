package com.zhaobh.test.springboottesttool.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SystemCode implements IResultCode {
    SUCCESS(SystemCode.SUCCESS_CODE, "请求成功"),
    PARAM_ERROR(SystemCode.PARAM_CODE, "参数错误"),
    FAILURE(SystemCode.FAILURE_CODE, "系统服务异常"),
    LOGIN_SUCCESS(SystemCode.LOGIN_SUCCESS_CODE,"登录成功"),
    TOKEN_EXPIRED(SystemCode.TOKEN_EXPIRED_CODE,"Token过期"),
    SESSION_EXPIRED(SystemCode.SESSION_EXPIRED_CODE,"会话超时，请重新登录！"),
    ;

    public static final int SUCCESS_CODE = 200;
    public static final int PARAM_CODE =1000;
    public static final int FAILURE_CODE = 9000;
    public static final int LOGIN_SUCCESS_CODE=0;
    public static final int TOKEN_EXPIRED_CODE=2;
    public static final int SESSION_EXPIRED_CODE=-1;

    final int code;
    final String msg;
}
