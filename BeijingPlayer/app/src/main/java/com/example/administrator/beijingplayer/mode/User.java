package com.example.administrator.beijingplayer.mode;

/**
 * Created by Administrator on 2016/9/6.
 */
public class User {

    /**
     * code : 10000
     * message : 成功
     * resultCode : {"Id":"85","mobile":"15135000523","nickname":"王锋","imgUrl":"/data/upfiles/201608/2919161229.jpg","gender":"1","age":"21","address":"内蒙古","token":"NjNmYjgzNjI1MjY2MjJkOGM4NjNjYzg4YzJhNTM1NTM=","ry_token":"+M/qBQrjB/duQdrGbCaK6ceLtn+1Si63plIwhNlkqKE/BBEgN/pwNkmYFbrra2MHAiSxnvGuaw43L0DdJ+ftzA==","attention_count":"0","fans_count":"0"}
     */

    private String code;
    private String message;


    private UserMessage resultCode;

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

    public UserMessage getResultCode() {
        return resultCode;
    }

    public void setResultCode(UserMessage resultCode) {
        this.resultCode = resultCode;
    }


}
