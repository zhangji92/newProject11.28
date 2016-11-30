package com.example.lenovo.amuse.mode;

import java.util.List;

/**
 * Created by lenovo on 2016/10/18.
 * 酒水商家
 */

public class AgentMode {

    /**
     * code : 10000
     * message : 成功
     * resultCode : {"id":"9","agentname":"酒不倒洋酒批发","brand":"酒不倒酒业","breed":"洋酒","person":"张经理","phone":"15251701931","address":"江苏省南京市浦口区大桥北路华侨广场1号","pic":"/data/upfiles/201511/1723325467.jpg","lat":"32.128864","lng":"118.729057","agentshop":[{"id":"21","shopname":"红酒","money":"188.00","pic":"/data/upfiles/201511/1723364647.jpg"},{"id":"20","shopname":"洋酒","money":"138.00","pic":"/data/upfiles/201511/1723352670.jpg"}]}
     */

    private String code;
    private String message;
    /**
     * id : 9
     * agentname : 酒不倒洋酒批发
     * brand : 酒不倒酒业
     * breed : 洋酒
     * person : 张经理
     * phone : 15251701931
     * address : 江苏省南京市浦口区大桥北路华侨广场1号
     * pic : /data/upfiles/201511/1723325467.jpg
     * lat : 32.128864
     * lng : 118.729057
     * agentshop : [{"id":"21","shopname":"红酒","money":"188.00","pic":"/data/upfiles/201511/1723364647.jpg"},{"id":"20","shopname":"洋酒","money":"138.00","pic":"/data/upfiles/201511/1723352670.jpg"}]
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
        private String id;
        private String agentname;
        private String brand;
        private String breed;
        private String person;
        private String phone;
        private String address;
        private String pic;
        private String lat;
        private String lng;
        /**
         * id : 21
         * shopname : 红酒
         * money : 188.00
         * pic : /data/upfiles/201511/1723364647.jpg
         */

        private List<AgentshopBean> agentshop;

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public List<AgentshopBean> getAgentshop() {
            return agentshop;
        }

        public void setAgentshop(List<AgentshopBean> agentshop) {
            this.agentshop = agentshop;
        }

        public static class AgentshopBean {
            private String id;
            private String shopname;
            private String money;
            private String pic;

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

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }
    }
}
