package com.example.lenovo.amuse.mode;

import java.util.List;

/**
 * Created by lenovo on 2016/10/19.
 * 酒水详情
 */

public class WineDetails {

    /**
     * code : 10000
     * message : 成功
     * resultCode : {"id":"21","agentid":"9","shopname":"红酒","sbrand":"法国印象","sbreed":"红酒","money":"188.00","introduce":"纯进口葡萄酒，农家独特技术酿制而成。","intropics":["/data/upfiles/201511/1723364628.jpg","/data/upfiles/201511/17233646464.jpg","/data/upfiles/201511/17233646343.jpg","/data/upfiles/201511/17233646807.jpg","/data/upfiles/201511/17233646279.jpg","/data/upfiles/201511/17233646811.jpg"],"person":"张经理","phone":"15251701931","address":"江苏省南京市浦口区大桥北路华侨广场1号","pic":"/data/upfiles/201511/1723364647.jpg","agentpic":"/data/upfiles/201511/1723325467.jpg","lat":"32.128864","lng":"118.729057"}
     */

    private String code;
    private String message;
    /**
     * id : 21
     * agentid : 9
     * shopname : 红酒
     * sbrand : 法国印象
     * sbreed : 红酒
     * money : 188.00
     * introduce : 纯进口葡萄酒，农家独特技术酿制而成。
     * intropics : ["/data/upfiles/201511/1723364628.jpg","/data/upfiles/201511/17233646464.jpg","/data/upfiles/201511/17233646343.jpg","/data/upfiles/201511/17233646807.jpg","/data/upfiles/201511/17233646279.jpg","/data/upfiles/201511/17233646811.jpg"]
     * person : 张经理
     * phone : 15251701931
     * address : 江苏省南京市浦口区大桥北路华侨广场1号
     * pic : /data/upfiles/201511/1723364647.jpg
     * agentpic : /data/upfiles/201511/1723325467.jpg
     * lat : 32.128864
     * lng : 118.729057
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
        private String agentid;
        private String shopname;
        private String sbrand;
        private String sbreed;
        private String money;
        private String introduce;
        private String person;
        private String phone;
        private String address;
        private String pic;
        private String agentpic;
        private String lat;
        private String lng;
        private List<String> intropics;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAgentid() {
            return agentid;
        }

        public void setAgentid(String agentid) {
            this.agentid = agentid;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getSbrand() {
            return sbrand;
        }

        public void setSbrand(String sbrand) {
            this.sbrand = sbrand;
        }

        public String getSbreed() {
            return sbreed;
        }

        public void setSbreed(String sbreed) {
            this.sbreed = sbreed;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
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

        public String getAgentpic() {
            return agentpic;
        }

        public void setAgentpic(String agentpic) {
            this.agentpic = agentpic;
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

        public List<String> getIntropics() {
            return intropics;
        }

        public void setIntropics(List<String> intropics) {
            this.intropics = intropics;
        }
    }
}
