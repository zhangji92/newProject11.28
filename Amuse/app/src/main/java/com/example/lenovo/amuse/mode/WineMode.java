package com.example.lenovo.amuse.mode;

import java.util.List;

/**
 * Created by lenovo on 2016/10/15.
 * 酒水
 */

public class WineMode {

    /**
     * code : 10000
     * message : 成功
     * resultCode : [{"id":"9","agentname":"酒不倒洋酒批发","brand":"酒不倒酒业","breed":"洋酒","person":"张经理","pic":"/data/upfiles/201511/1723325467.jpg","address":"江苏省南京市浦口区大桥北路华侨广场1号","phone":"15251701931"}]
     */

    private String code;
    private String message;
    /**
     * id : 9
     * agentname : 酒不倒洋酒批发
     * brand : 酒不倒酒业
     * breed : 洋酒
     * person : 张经理
     * pic : /data/upfiles/201511/1723325467.jpg
     * address : 江苏省南京市浦口区大桥北路华侨广场1号
     * phone : 15251701931
     */

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
        private String agentname;
        private String brand;
        private String breed;
        private String person;
        private String pic;
        private String address;
        private String phone;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAgentname() {
            return agentname;
        }

        public void setAgentname(String agentname) {
            this.agentname = agentname;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getBreed() {
            return breed;
        }

        public void setBreed(String breed) {
            this.breed = breed;
        }

        public String getPerson() {
            return person;
        }

        public void setPerson(String person) {
            this.person = person;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
