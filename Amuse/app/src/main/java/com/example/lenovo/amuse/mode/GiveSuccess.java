package com.example.lenovo.amuse.mode;

/**
 * Created by 张继 on 2016/10/26.
 */

public class GiveSuccess {

    /**
     * code : 10000
     * message : 成功
     * resultCode : {"praise":"2","likecount":"1"}
     */

    private String code;
    private String message;
    /**
     * praise : 2
     * likecount : 1
     */

    private ResultCodeBean resultCode;

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

    public ResultCodeBean getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCodeBean resultCode) {
        this.resultCode = resultCode;
    }

    public static class ResultCodeBean {
        private String praise;
        private String likecount;

        public String getPraise() {
            return praise;
        }

        public void setPraise(String praise) {
            this.praise = praise;
        }

        public String getLikecount() {
            return likecount;
        }

        public void setLikecount(String likecount) {
            this.likecount = likecount;
        }
    }
}
