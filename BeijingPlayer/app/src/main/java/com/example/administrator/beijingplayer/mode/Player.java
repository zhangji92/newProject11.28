package com.example.administrator.beijingplayer.mode;

import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class Player {



    private String code;
    private String message;


    private List<ResultCodeBean> resultCode;

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

    public List<ResultCodeBean> getResultCode() {
        return resultCode;
    }

    public void setResultCode(List<ResultCodeBean> resultCode) {
        this.resultCode = resultCode;
    }

    public static class ResultCodeBean {
        private String id;
        private String shopname;
        private String pic;
        private String address;
        private String ordernum;
        private String content;
        private String juli;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(String ordernum) {
            this.ordernum = ordernum;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getJuli() {
            return juli;
        }

        public void setJuli(String juli) {
            this.juli = juli;
        }
    }
}
