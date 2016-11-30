package com.example.lenovo.amuse.mode;

import java.util.List;

/**
 * Created by lenovo on 2016/9/27.
 * 同城爱玩
 */

public class LovePlayMode {

    /**
     * code : 10000
     * message : 成功
     * resultCode : [{"id":"27","shopname":"COES酒吧","pic":"/data/upfiles/201511/1502482827.jpg","address":"安徽省安庆市宿松县孚玉东路33号","ordernum":"63","content":"文明交友","juli":"12752.657"},{"id":"28","shopname":"弘扬酒吧","pic":"/data/upfiles/201511/1800155847.jpg","address":"江苏省南京市浦口区大桥北路127号","ordernum":"160","content":"好喝的洋酒，各位快快来吧。本酒吧热烈欢迎各位的光临","juli":"13015.478"},{"id":"25","shopname":"七楼足疗城","pic":"/data/upfiles/201511/1300460412.jpg","address":"江苏省南京市浦口区大桥北路弘阳广场88号","ordernum":"84","content":"中医手法，弘扬民族文化，造福人类健康。","juli":"13016.175"},{"id":"24","shopname":"欧曼世KTV","pic":"/data/upfiles/201511/1800001172.jpg","address":"江苏省南京市浦口区大桥北路2号","ordernum":"180","content":"欧曼世量贩式KTV，是全国连锁KTV品牌，一直以来凭借与其它量贩KTV不同的经营风格，为消费者营造了一种时尚，健康，快乐，舒适的全新消费理念。欧曼世量贩式KTV在全国拥有3000多家连锁分店，各色玻璃镜面的巧妙搭配，别致的LED变换灯光和光纤灯，构成了欧曼世量贩式KTV时尚前卫的装修风格，场内各种风格的包厢，从精致的迷你小房到场面宏大可容纳60多人的总统包厢，使您充分感受到PARTY的魅力！先进的专业进口音响设备，让您的嗓音立刻具有歌星的潜质！独特的一键式17寸液晶触摸屏点歌系统，操作便捷，超过四万首的超大歌库，另有每月新增百余首最新的单曲,在此您将紧跟流行的前沿。 贴心的服务品质，平价齐全的购物超市，现代时尚的装修环境，经济实惠的价格，欧曼世量贩式KTV与您一起尽情唱享音乐的快乐！ 有音乐就快乐 有快乐就OK ！","juli":"13016.663"},{"id":"26","shopname":"1916酒吧","pic":"/data/upfiles/201511/1502335330.jpg","address":"江苏省南京市玄武区中山南路217号","ordernum":"78","content":"开心消费，文明交友。","juli":"13023.818"}]
     */

    private String code;
    private String message;
    /**
     * id : 27
     * shopname : COES酒吧
     * pic : /data/upfiles/201511/1502482827.jpg
     * address : 安徽省安庆市宿松县孚玉东路33号
     * ordernum : 63
     * content : 文明交友
     * juli : 12752.657
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
