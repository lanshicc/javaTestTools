package com.zhaobh.test.springboottesttool.result;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.Optional;

@Data
@ToString
@NoArgsConstructor
public class CommonResult implements Serializable {
    private int rtcode;
    private boolean success;
    @Value("${result:操作成功}")
    private Object result;

    private CommonResult(IResultCode resultCode) {
        this(resultCode, resultCode.getMsg());
    }

    private CommonResult(IResultCode resultCode, Object result) {
        this.rtcode = resultCode.getCode();
        this.result = result;
        this.success = (SystemCode.SUCCESS == resultCode)||(SystemCode.LOGIN_SUCCESS == resultCode);
    }

    public static boolean isSuccess(CommonResult result) {
        return Optional.ofNullable(result)
                .map(r -> r.rtcode)
                .map(code -> SystemCode.SUCCESS_CODE == code)
                .orElse(Boolean.FALSE);
    }

    public static boolean isFail(CommonResult result) {
        return !CommonResult.isSuccess(result);
    }

    public static Object getData(CommonResult result) {
        return Optional.ofNullable(result)
                .filter(r -> r.success)
                .map(x -> x.result)
                .orElse(null);
    }

    public static CommonResult success() {
        return new CommonResult(SystemCode.SUCCESS);
    }

    public static CommonResult loginSuccess(Object result){
        return new CommonResult(SystemCode.LOGIN_SUCCESS,result);
    }

    public static CommonResult success(Object result) {
        return new CommonResult(SystemCode.SUCCESS, result);
    }
    public static CommonResult paramError() {
        return new CommonResult(SystemCode.PARAM_ERROR);
    }
    public static CommonResult paramError(Object result) {
        return new CommonResult(SystemCode.PARAM_ERROR,result);
    }
    public static CommonResult fail(String msg) {
        return new CommonResult(SystemCode.FAILURE, msg);
    }

    public static CommonResult fail(IResultCode resultCode) {
        return new CommonResult((resultCode));
    }

    public static CommonResult fail(IResultCode resultCode, String msg) {
        return new CommonResult(resultCode, msg);
    }
}
