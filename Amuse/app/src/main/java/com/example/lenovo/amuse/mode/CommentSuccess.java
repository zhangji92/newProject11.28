package com.example.lenovo.amuse.mode;

/**
 * Created by 张继 on 2016/10/26.
 */

public class CommentSuccess {

    /**
     * code : 10000
     * message : 成功
     * resultCode : {"Id":"91","mobile":"17073353257","nickname":"猴子","imgUrl":"/data/upfiles/201610/1813332146.jpg","gender":"2","age":"20","address":"","token":"OTZlZmNmOTY1NTZiMDNiMGI5NWM1M2ExZTM3ZmRhZjE=","ry_token":"MJYfzzH3bXSC4J9hrqQyMzpGhAlM/0Ha4/yQ4g/6Uzt7WCgAmcKI6Y2VxR5iOec70e0ywU39IFg=","attention_count":"4","fans_count":"0"}
     */

    private String code;
    private String message;
    /**
     * Id : 91
     * mobile : 17073353257
     * nickname : 猴子
     * imgUrl : /data/upfiles/201610/1813332146.jpg
     * gender : 2
     * age : 20
     * address :
     * token : OTZlZmNmOTY1NTZiMDNiMGI5NWM1M2ExZTM3ZmRhZjE=
     * ry_token : MJYfzzH3bXSC4J9hrqQyMzpGhAlM/0Ha4/yQ4g/6Uzt7WCgAmcKI6Y2VxR5iOec70e0ywU39IFg=
     * attention_count : 4
     * fans_count : 0
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
        private String Id;
        private String mobile;
        private String nickname;
        private String imgUrl;
        private String gender;
        private String age;
        private String address;
        private String token;
        private String ry_token;
        private String attention_count;
        private String fans_count;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRy_token() {
            return ry_token;
        }

        public void setRy_token(String ry_token) {
            this.ry_token = ry_token;
        }

        public String getAttention_count() {
            return attention_count;
        }

        public void setAttention_count(String attention_count) {
            this.attention_count = attention_count;
        }

        public String getFans_count() {
            return fans_count;
        }

        public void setFans_count(String fans_count) {
            this.fans_count = fans_count;
        }
    }
}
