package com.example.administrator.beijingplayer.mode;

/**
 * Created by Administrator on 2016/8/30.
 */
public class Shop {

    private int code;

    private String message;

    private HomeRes resultCode;

    public Shop() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HomeRes getHomeRes() {
        return resultCode;
    }

    public void setHomeRes(HomeRes resultCode) {
        this.resultCode = resultCode;
    }

    public Shop(int code, String message, HomeRes resultCode) {
        this.code = code;
        this.message = message;
        this.resultCode = resultCode;
    }

}
