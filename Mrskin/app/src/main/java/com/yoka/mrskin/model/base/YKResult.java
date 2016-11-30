package com.yoka.mrskin.model.base;

/**
 * 
 * @author Y H L
 * 
 */
public class YKResult
{

    private final static int RESULT_CODE_OK = 0;
    private final static int RESULT_CODE_ERROR = 1;
    private final static int RESULT_CODE_ERROR_NET = 2;

    private int code;
    private Object message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public YKResult(int code, Object message)
    {
        super();
        this.code = code;
        this.message = message;
    }

    public YKResult()
    {
        super();
    }

    @Override
    public String toString() {
        return "Result [code=" + code + ", message=" + message + "]";
    }

    public boolean isSucceeded() {
        return code == RESULT_CODE_OK;
    }

    public void fail() {
        code = RESULT_CODE_ERROR;
    }

    public void succeed() {
        code = RESULT_CODE_OK;
    }

    public void failForNetError() {
        code = RESULT_CODE_ERROR_NET;
    }

    public boolean isFailForNetError() {
        return code == RESULT_CODE_ERROR_NET;
    }
}
