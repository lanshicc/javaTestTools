package com.zhaobh.test.springboottesttool.result;

import java.io.Serializable;

public interface IResultCode extends Serializable {
    int getCode();

    default String getMsg() {
        return "系统服务异常";
    }
}
