package com.example.lenovo.amuse.mode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 张继 on 2016/10/27.
 * 场所
 */

public class PlaceMode implements Serializable{

    /**
     * code : 10000
     * message : 成功
     * resultCode : [{"id":"24","bid":"15","shopname":"欧曼世KTV","pic":"/data/upfiles/201511/1800001172.jpg","ordernum":"181","count1":"0","count2":"0","onestar":"0","address":"江苏省南京市浦口区大桥北路2号","lat":"32.128155","lng":"118.728294","content":"欧曼世量贩式KTV，是全国连锁KTV品牌，一直以来凭借与其它量贩KTV不同的经营风格，为消费者营造了一种时尚，健康，快乐，舒适的全新消费理念。欧曼世量贩式KTV在全国拥有3000多家连锁分店，各色玻璃镜面的巧妙搭配，别致的LED变换灯光和光纤灯，构成了欧曼世量贩式KTV时尚前卫的装修风格，场内各种风格的包厢，从精致的迷你小房到场面宏大可容纳60多人的总统包厢，使您充分感受到PARTY的魅力！先进的专业进口音响设备，让您的嗓音立刻具有歌星的潜质！独特的一键式17寸液晶触摸屏点歌系统，操作便捷，超过四万首的超大歌库，另有每月新增百余首最新的单曲,在此您将紧跟流行的前沿。 贴心的服务品质，平价齐全的购物超市，现代时尚的装修环境，经济实惠的价格，欧曼世量贩式KTV与您一起尽情唱享音乐的快乐！ 有音乐就快乐 有快乐就OK ！","juli":"3468.442"},{"id":"25","bid":"16","shopname":"七楼足疗城","pic":"/data/upfiles/201511/1300460412.jpg","ordernum":"85","count1":"0","count2":"0","onestar":"0","address":"江苏省南京市浦口区大桥北路弘阳广场88号","lat":"32.140335","lng":"118.723763","content":"中医手法，弘扬民族文化，造福人类健康。","juli":"3469.763"},{"id":"26","bid":"17","shopname":"1916酒吧","pic":"/data/upfiles/201511/1502335330.jpg","ordernum":"82","count1":"0","count2":"1","onestar":"0","address":"江苏省南京市玄武区中山南路217号","lat":"32.04203","lng":"118.796371","content":"开心消费，文明交友。","juli":"3459.358"},{"id":"27","bid":"18","shopname":"COES酒吧","pic":"/data/upfiles/201511/1502482827.jpg","ordernum":"66","count1":"0","count2":"0","onestar":"0","address":"安徽省安庆市宿松县孚玉东路33号","lat":"30.151062","lng":"116.144928","content":"文明交友","juli":"3242.033"},{"id":"28","bid":"19","shopname":"弘扬酒吧","pic":"/data/upfiles/201511/1800155847.jpg","ordernum":"163","count1":"5","count2":"0","onestar":"0","address":"江苏省南京市浦口区大桥北路127号","lat":"32.144794","lng":"118.717056","content":"好喝的洋酒，各位快快来吧。本酒吧热烈欢迎各位的光临","juli":"3470.212"}]
     */

    private String code;
    private String message;
    /**
     * id : 24
     * bid : 15
     * shopname : 欧曼世KTV
     * pic : /data/upfiles/201511/1800001172.jpg
     * ordernum : 181
     * count1 : 0
     * count2 : 0
     * onestar : 0
     * address : 江苏省南京市浦口区大桥北路2号
     * lat : 32.128155
     * lng : 118.728294
     * content : 欧曼世量贩式KTV，是全国连锁KTV品牌，一直以来凭借与其它量贩KTV不同的经营风格，为消费者营造了一种时尚，健康，快乐，舒适的全新消费理念。欧曼世量贩式KTV在全国拥有3000多家连锁分店，各色玻璃镜面的巧妙搭配，别致的LED变换灯光和光纤灯，构成了欧曼世量贩式KTV时尚前卫的装修风格，场内各种风格的包厢，从精致的迷你小房到场面宏大可容纳60多人的总统包厢，使您充分感受到PARTY的魅力！先进的专业进口音响设备，让您的嗓音立刻具有歌星的潜质！独特的一键式17寸液晶触摸屏点歌系统，操作便捷，超过四万首的超大歌库，另有每月新增百余首最新的单曲,在此您将紧跟流行的前沿。 贴心的服务品质，平价齐全的购物超市，现代时尚的装修环境，经济实惠的价格，欧曼世量贩式KTV与您一起尽情唱享音乐的快乐！ 有音乐就快乐 有快乐就OK ！
     * juli : 3468.442
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

    public static class ResultCodeBean implements Serializable{
        private String id;
        private String bid;
        private String shopname;
        private String pic;
        private String ordernum;
        private String count1;
        private String count2;
        private String onestar;
        private String address;
        private String lat;
        private String lng;
        private String content;
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

        public String getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(String ordernum) {
            this.ordernum = ordernum;
        }

        public String getCount1() {
            return count1;
        }

        public void setCount1(String count1) {
            this.count1 = count1;
        }

        public String getCount2() {
            return count2;
        }

        public void setCount2(String count2) {
            this.count2 = count2;
        }

        public String getOnestar() {
            return onestar;
        }

        public void setOnestar(String onestar) {
            this.onestar = onestar;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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
