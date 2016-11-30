package com.example.lenovo.amuse.mode;

import java.util.List;

/**
 * Created by lenovo on 2016/10/20.
 * 商家详情
 */

public class ShopMode {

    /**
     * code : 10000
     * message : 成功
     * resultCode : {"id":"27","shopname":"COES酒吧","introduce":"决绝毒赌黄具体消费按照套餐选用价格为准，如有其它消费，到店现付。","pic":"/data/upfiles/201511/1502482827.jpg","content":"文明交友","minconsume":"108.00","bid":"18","businessname":"COES酒吧","address":"安徽省安庆市宿松县孚玉东路33号","tel":"02585914911","phone":"15251701931","bpic":"/data/upfiles/201511/1502465336.jpg","bintro":"好酒吧，好味道。","intropics":["/data/upfiles/201511/17221503721.jpg","/data/upfiles/201511/17221503859.jpg","/data/upfiles/201511/17221503636.jpg","/data/upfiles/201511/17221503913.jpg","/data/upfiles/201511/17221503698.jpg","/data/upfiles/201511/17221503829.jpg"],"lat":"30.151062","lng":"116.144928","price":[{"id":"76","name":"小卡","content":"2瓶洋酒+2瓶红酒","oldprice":"228.00","nowprice":"108.00"},{"id":"77","name":"大卡","content":"6瓶洋酒+6瓶红酒+大果盘","oldprice":"1088.00","nowprice":"888.00"},{"id":"86","name":"test","content":"test","oldprice":"10000.00","nowprice":"1.00"}]}
     */

    private String code;
    private String message;
    /**
     * id : 27
     * shopname : COES酒吧
     * introduce : 决绝毒赌黄具体消费按照套餐选用价格为准，如有其它消费，到店现付。
     * pic : /data/upfiles/201511/1502482827.jpg
     * content : 文明交友
     * minconsume : 108.00
     * bid : 18
     * businessname : COES酒吧
     * address : 安徽省安庆市宿松县孚玉东路33号
     * tel : 02585914911
     * phone : 15251701931
     * bpic : /data/upfiles/201511/1502465336.jpg
     * bintro : 好酒吧，好味道。
     * intropics : ["/data/upfiles/201511/17221503721.jpg","/data/upfiles/201511/17221503859.jpg","/data/upfiles/201511/17221503636.jpg","/data/upfiles/201511/17221503913.jpg","/data/upfiles/201511/17221503698.jpg","/data/upfiles/201511/17221503829.jpg"]
     * lat : 30.151062
     * lng : 116.144928
     * price : [{"id":"76","name":"小卡","content":"2瓶洋酒+2瓶红酒","oldprice":"228.00","nowprice":"108.00"},{"id":"77","name":"大卡","content":"6瓶洋酒+6瓶红酒+大果盘","oldprice":"1088.00","nowprice":"888.00"},{"id":"86","name":"test","content":"test","oldprice":"10000.00","nowprice":"1.00"}]
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
        private String shopname;
        private String introduce;
        private String pic;
        private String content;
        private String minconsume;
        private String bid;
        private String businessname;
        private String address;
        private String tel;
        private String phone;
        private String bpic;
        private String bintro;
        private String lat;
        private String lng;
        private List<String> intropics;
        /**
         * id : 76
         * name : 小卡
         * content : 2瓶洋酒+2瓶红酒
         * oldprice : 228.00
         * nowprice : 108.00
         */

        private List<PriceBean> price;

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

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMinconsume() {
            return minconsume;
        }

        public void setMinconsume(String minconsume) {
            this.minconsume = minconsume;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getBusinessname() {
            return businessname;
        }

        public void setBusinessname(String businessname) {
            this.businessname = businessname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getBpic() {
            return bpic;
        }

        public void setBpic(String bpic) {
            this.bpic = bpic;
        }

        public String getBintro() {
            return bintro;
        }

        public void setBintro(String bintro) {
            this.bintro = bintro;
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

        public List<PriceBean> getPrice() {
            return price;
        }

        public void setPrice(List<PriceBean> price) {
            this.price = price;
        }

        public static class PriceBean {
            private String id;
            private String name;
            private String content;
            private String oldprice;
            private String nowprice;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getOldprice() {
                return oldprice;
            }

            public void setOldprice(String oldprice) {
                this.oldprice = oldprice;
            }

            public String getNowprice() {
                return nowprice;
            }

            public void setNowprice(String nowprice) {
                this.nowprice = nowprice;
            }
        }
    }
}
