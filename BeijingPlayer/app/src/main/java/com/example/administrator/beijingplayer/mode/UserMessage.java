package com.example.administrator.beijingplayer.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/6.
 */
public class UserMessage implements Serializable{

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
        private boolean isLogin = false;


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

        public boolean isLogin() {
        return isLogin;
    }

        public void setLogin(boolean login) {
             this.isLogin = login;
        }
}
