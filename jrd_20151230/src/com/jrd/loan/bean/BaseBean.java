package com.jrd.loan.bean;

public class BaseBean {

    int resultCode;
    String resultMsg;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    @Override
    public String toString() {
        return "BaseBean [resultCode=" + resultCode + ", resultMsg=" + resultMsg + "]";
    }

}
