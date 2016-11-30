package com.example.lenovo.amuse.mode;

import java.util.List;
import java.util.Objects;

/**
 * Created by lenovo on 2016/9/26.
 * 首页实体类
 */

public class FirstPageMode{


    /**
     * code : 10000
     * message : 成功
     * resultCode : {"ad":[{"id":"1","title":"tete","pic":"/data/upfiles/201607/2910145822.jpg","link":"http://www.baidu.com"},{"id":"2","title":"test","pic":"/data/upfiles/201608/3016545060.png","link":"http://www.baidu.com"}],"recommend":{"heng":[{"id":"4","bid":"18","sid":"27","shopname":"COES酒吧","pic":"/data/upfiles/201511/1502482827.jpg","address":"安徽省安庆市宿松县孚玉东路33号","businessname":"COES酒吧","lat":"30.151062","lng":"116.144928","juli":"12752.657"},{"id":"13","bid":"18","sid":"27","shopname":"COES酒吧","pic":"/data/upfiles/201511/1502482827.jpg","address":"安徽省安庆市宿松县孚玉东路33号","businessname":"COES酒吧","lat":"30.151062","lng":"116.144928","juli":"12752.657"},{"id":"5","bid":"19","sid":"28","shopname":"弘扬酒吧","pic":"/data/upfiles/201511/1800155847.jpg","address":"江苏省南京市浦口区大桥北路127号","businessname":"弘扬酒吧","lat":"32.144794","lng":"118.717056","juli":"13015.478"},{"id":"14","bid":"19","sid":"28","shopname":"弘扬酒吧","pic":"/data/upfiles/201511/1800155847.jpg","address":"江苏省南京市浦口区大桥北路127号","businessname":"弘扬酒吧","lat":"32.144794","lng":"118.717056","juli":"13015.478"},{"id":"2","bid":"16","sid":"25","shopname":"七楼足疗城","pic":"/data/upfiles/201511/1300460412.jpg","address":"江苏省南京市浦口区大桥北路弘阳广场88号","businessname":"七楼足疗城","lat":"32.140335","lng":"118.723763","juli":"13016.175"},{"id":"11","bid":"16","sid":"25","shopname":"七楼足疗城","pic":"/data/upfiles/201511/1300460412.jpg","address":"江苏省南京市浦口区大桥北路弘阳广场88号","businessname":"七楼足疗城","lat":"32.140335","lng":"118.723763","juli":"13016.175"},{"id":"1","bid":"15","sid":"24","shopname":"欧曼世KTV","pic":"/data/upfiles/201511/1800001172.jpg","address":"江苏省南京市浦口区大桥北路2号","businessname":"欧曼世量贩式KTV","lat":"32.128155","lng":"118.728294","juli":"13016.663"},{"id":"10","bid":"15","sid":"24","shopname":"欧曼世KTV","pic":"/data/upfiles/201511/1800001172.jpg","address":"江苏省南京市浦口区大桥北路2号","businessname":"欧曼世量贩式KTV","lat":"32.128155","lng":"118.728294","juli":"13016.663"}],"shu":[{"id":"3","bid":"17","sid":"26","shopname":"1916酒吧","pic":"/data/upfiles/201512/3118411816.png","address":"江苏省南京市玄武区中山南路217号","businessname":"1916酒吧","lat":"32.04203","lng":"118.796371","juli":"13023.818"},{"id":"12","bid":"17","sid":"26","shopname":"1916酒吧","pic":"/data/upfiles/201512/3118411816.png","address":"江苏省南京市玄武区中山南路217号","businessname":"1916酒吧","lat":"32.04203","lng":"118.796371","juli":"13023.818"}]}}
     */

    private String code;
    private String message;
    /**
     * ad : [{"id":"1","title":"tete","pic":"/data/upfiles/201607/2910145822.jpg","link":"http://www.baidu.com"},{"id":"2","title":"test","pic":"/data/upfiles/201608/3016545060.png","link":"http://www.baidu.com"}]
     * recommend : {"heng":[{"id":"4","bid":"18","sid":"27","shopname":"COES酒吧","pic":"/data/upfiles/201511/1502482827.jpg","address":"安徽省安庆市宿松县孚玉东路33号","businessname":"COES酒吧","lat":"30.151062","lng":"116.144928","juli":"12752.657"},{"id":"13","bid":"18","sid":"27","shopname":"COES酒吧","pic":"/data/upfiles/201511/1502482827.jpg","address":"安徽省安庆市宿松县孚玉东路33号","businessname":"COES酒吧","lat":"30.151062","lng":"116.144928","juli":"12752.657"},{"id":"5","bid":"19","sid":"28","shopname":"弘扬酒吧","pic":"/data/upfiles/201511/1800155847.jpg","address":"江苏省南京市浦口区大桥北路127号","businessname":"弘扬酒吧","lat":"32.144794","lng":"118.717056","juli":"13015.478"},{"id":"14","bid":"19","sid":"28","shopname":"弘扬酒吧","pic":"/data/upfiles/201511/1800155847.jpg","address":"江苏省南京市浦口区大桥北路127号","businessname":"弘扬酒吧","lat":"32.144794","lng":"118.717056","juli":"13015.478"},{"id":"2","bid":"16","sid":"25","shopname":"七楼足疗城","pic":"/data/upfiles/201511/1300460412.jpg","address":"江苏省南京市浦口区大桥北路弘阳广场88号","businessname":"七楼足疗城","lat":"32.140335","lng":"118.723763","juli":"13016.175"},{"id":"11","bid":"16","sid":"25","shopname":"七楼足疗城","pic":"/data/upfiles/201511/1300460412.jpg","address":"江苏省南京市浦口区大桥北路弘阳广场88号","businessname":"七楼足疗城","lat":"32.140335","lng":"118.723763","juli":"13016.175"},{"id":"1","bid":"15","sid":"24","shopname":"欧曼世KTV","pic":"/data/upfiles/201511/1800001172.jpg","address":"江苏省南京市浦口区大桥北路2号","businessname":"欧曼世量贩式KTV","lat":"32.128155","lng":"118.728294","juli":"13016.663"},{"id":"10","bid":"15","sid":"24","shopname":"欧曼世KTV","pic":"/data/upfiles/201511/1800001172.jpg","address":"江苏省南京市浦口区大桥北路2号","businessname":"欧曼世量贩式KTV","lat":"32.128155","lng":"118.728294","juli":"13016.663"}],"shu":[{"id":"3","bid":"17","sid":"26","shopname":"1916酒吧","pic":"/data/upfiles/201512/3118411816.png","address":"江苏省南京市玄武区中山南路217号","businessname":"1916酒吧","lat":"32.04203","lng":"118.796371","juli":"13023.818"},{"id":"12","bid":"17","sid":"26","shopname":"1916酒吧","pic":"/data/upfiles/201512/3118411816.png","address":"江苏省南京市玄武区中山南路217号","businessname":"1916酒吧","lat":"32.04203","lng":"118.796371","juli":"13023.818"}]}
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
        private RecommendBean recommend;
        /**
         * id : 1
         * title : tete
         * pic : /data/upfiles/201607/2910145822.jpg
         * link : http://www.baidu.com
         */

        private List<AdBean> ad;

        public RecommendBean getRecommend() {
            return recommend;
        }

        public void setRecommend(RecommendBean recommend) {
            this.recommend = recommend;
        }

        public List<AdBean> getAd() {
            return ad;
        }

        public void setAd(List<AdBean> ad) {
            this.ad = ad;
        }

        public static class RecommendBean {
            /**
             * id : 4
             * bid : 18
             * sid : 27
             * shopname : COES酒吧
             * pic : /data/upfiles/201511/1502482827.jpg
             * address : 安徽省安庆市宿松县孚玉东路33号
             * businessname : COES酒吧
             * lat : 30.151062
             * lng : 116.144928
             * juli : 12752.657
             */

            private List<HengBean> heng;
            /**
             * id : 3
             * bid : 17
             * sid : 26
             * shopname : 1916酒吧
             * pic : /data/upfiles/201512/3118411816.png
             * address : 江苏省南京市玄武区中山南路217号
             * businessname : 1916酒吧
             * lat : 32.04203
             * lng : 118.796371
             * juli : 13023.818
             */

            private List<ShuBean> shu;

            public List<HengBean> getHeng() {
                return heng;
            }

            public void setHeng(List<HengBean> heng) {
                this.heng = heng;
            }

            public List<ShuBean> getShu() {
                return shu;
            }

            public void setShu(List<ShuBean> shu) {
                this.shu = shu;
            }

            public static class HengBean {
                private String id;
                private String bid;
                private String sid;
                private String shopname;
                private String pic;
                private String address;
                private String businessname;
                private String lat;
                private String lng;
                private String juli;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getBid() {
                    return bid;
                }

                public void setBid(String bid) {
                    this.bid = bid;
                }

                public String getSid() {
                    return sid;
                }

                public void setSid(String sid) {
                    this.sid = sid;
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

                public String getBusinessname() {
                    return businessname;
                }

                public void setBusinessname(String businessname) {
                    this.businessname = businessname;
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

                public String getJuli() {
                    return juli;
                }

                public void setJuli(String juli) {
                    this.juli = juli;
                }
            }

            public static class ShuBean {
                private String id;
                private String bid;
                private String sid;
                private String shopname;
                private String pic;
                private String address;
                private String businessname;
                private String lat;
                private String lng;
                private String juli;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getBid() {
                    return bid;
                }

                public void setBid(String bid) {
                    this.bid = bid;
                }

                public String getSid() {
                    return sid;
                }

                public void setSid(String sid) {
                    this.sid = sid;
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

                public String getBusinessname() {
                    return businessname;
                }

                public void setBusinessname(String businessname) {
                    this.businessname = businessname;
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

                public String getJuli() {
                    return juli;
                }

                public void setJuli(String juli) {
                    this.juli = juli;
                }
            }
        }

        public static class AdBean {
            private String id;
            private String title;
            private String pic;
            private String link;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }
        }
    }
}
