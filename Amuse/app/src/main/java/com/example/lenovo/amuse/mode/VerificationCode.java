package com.example.lenovo.amuse.mode;

/**
 * Created by lenovo on 2016/9/27.
 * 手机验证码
 */

public class VerificationCode {

    /**
     * code : 10000
     * message : 验证码已发送成功,请注意查收!
     * resultCode : 182280
     */

    private String code;
    private String message;
    private String resultCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
