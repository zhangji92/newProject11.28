package com.example.lenovo.amuse.mode;

import java.util.List;

/**
 * Created by 张继 on 2016/10/26.
 * 拼桌列表接口
 */

public class TableListMode {

    /**
     * code : 10000
     * message : 成功
     * resultCode : [{"id":"127","name":"哈哈","title":"哗哗","userid":"86","bid":"18","shopid":"27","persons":"-1","away":"12752.657","addtime":"2016-10-25 16:23:58","shopname":"COES酒吧","pic":"/data/upfiles/201511/1502482827.jpg","content":"文明交友","address":"安徽省安庆市宿松县孚玉东路33号","nickname":"我是赵佳佳","upic":"/data/upfiles/201610/2613402343.jpg","lat":"30.151062","lng":"116.144928","isjoin":"2","starlevel":"1"},{"id":"128","name":"啦啊","title":"就看了","userid":"90","bid":"18","shopid":"27","persons":"1","away":"12752.657","addtime":"2016-10-26 10:28:48","shopname":"COES酒吧","pic":"/data/upfiles/201511/1502482827.jpg","content":"文明交友","address":"安徽省安庆市宿松县孚玉东路33号","nickname":"小小心","upic":"/data/upfiles/201610/1714223330.jpg","lat":"30.151062","lng":"116.144928","isjoin":"2","starlevel":"1"},{"id":"129","name":"粑粑","title":"粑粑","userid":"90","bid":"18","shopid":"27","persons":"1","away":"12752.657","addtime":"2016-10-26 10:29:33","shopname":"COES酒吧","pic":"/data/upfiles/201511/1502482827.jpg","content":"文明交友","address":"安徽省安庆市宿松县孚玉东路33号","nickname":"小小心","upic":"/data/upfiles/201610/1714223330.jpg","lat":"30.151062","lng":"116.144928","isjoin":"2","starlevel":"1"},{"id":"134","name":"就看了","title":"监控","userid":"90","bid":"19","shopid":"28","persons":"2","away":"13015.478","addtime":"2016-10-26 11:13:06","shopname":"弘扬酒吧","pic":"/data/upfiles/201511/1800155847.jpg","content":"好喝的洋酒，各位快快来吧。本酒吧热烈欢迎各位的光临","address":"江苏省南京市浦口区大桥北路127号","nickname":"小小心","upic":"/data/upfiles/201610/1714223330.jpg","lat":"32.144794","lng":"118.717056","isjoin":"2","starlevel":"5"},{"id":"135","name":"供养你莫","title":"绿巨人","userid":"104","bid":"19","shopid":"28","persons":"1","away":"13015.478","addtime":"2016-10-26 15:03:10","shopname":"弘扬酒吧","pic":"/data/upfiles/201511/1800155847.jpg","content":"好喝的洋酒，各位快快来吧。本酒吧热烈欢迎各位的光临","address":"江苏省南京市浦口区大桥北路127号","nickname":"servlet","upic":"/data/upfiles/201610/1718194854.jpg","lat":"32.144794","lng":"118.717056","isjoin":"2","starlevel":"5"},{"id":"137","name":"就看了","title":"啦啦啦啦","userid":"90","bid":"19","shopid":"28","persons":"1","away":"13015.478","addtime":"2016-10-26 15:07:34","shopname":"弘扬酒吧","pic":"/data/upfiles/201511/1800155847.jpg","content":"好喝的洋酒，各位快快来吧。本酒吧热烈欢迎各位的光临","address":"江苏省南京市浦口区大桥北路127号","nickname":"小小心","upic":"/data/upfiles/201610/1714223330.jpg","lat":"32.144794","lng":"118.717056","isjoin":"2","starlevel":"5"},{"id":"126","name":"我在","title":"来吧哈哈","userid":"86","bid":"16","shopid":"25","persons":"1","away":"13016.175","addtime":"2016-10-25 15:43:40","shopname":"七楼足疗城","pic":"/data/upfiles/201511/1300460412.jpg","content":"中医手法，弘扬民族文化，造福人类健康。","address":"江苏省南京市浦口区大桥北路弘阳广场88号","nickname":"我是赵佳佳","upic":"/data/upfiles/201610/2613402343.jpg","lat":"32.140335","lng":"118.723763","isjoin":"2","starlevel":"1"},{"id":"132","name":"经历了","title":"啦啦啦啦","userid":"90","bid":"16","shopid":"25","persons":"1","away":"13016.175","addtime":"2016-10-26 11:07:27","shopname":"七楼足疗城","pic":"/data/upfiles/201511/1300460412.jpg","content":"中医手法，弘扬民族文化，造福人类健康。","address":"江苏省南京市浦口区大桥北路弘阳广场88号","nickname":"小小心","upic":"/data/upfiles/201610/1714223330.jpg","lat":"32.140335","lng":"118.723763","isjoin":"2","starlevel":"1"}]
     */

    private String code;
    private String message;
    /**
     * id : 127
     * name : 哈哈
     * title : 哗哗
     * userid : 86
     * bid : 18
     * shopid : 27
     * persons : -1
     * away : 12752.657
     * addtime : 2016-10-25 16:23:58
     * shopname : COES酒吧
     * pic : /data/upfiles/201511/1502482827.jpg
     * content : 文明交友
     * address : 安徽省安庆市宿松县孚玉东路33号
     * nickname : 我是赵佳佳
     * upic : /data/upfiles/201610/2613402343.jpg
     * lat : 30.151062
     * lng : 116.144928
     * isjoin : 2
     * starlevel : 1
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
        private String name;
        private String title;
        private String userid;
        private String bid;
        private String shopid;
        private String persons;
        private String away;
        private String addtime;
        private String shopname;
        private String pic;
        private String content;
        private String address;
        private String nickname;
        private String upic;
        private String lat;
        private String lng;
        private String isjoin;
        private String starlevel;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public String getPersons() {
            return persons;
        }

        public void setPersons(String persons) {
            this.persons = persons;
        }

        public String getAway() {
            return away;
        }

        public void setAway(String away) {
            this.away = away;
        }



        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUpic() {
            return upic;
        }

        public void setUpic(String upic) {
            this.upic = upic;
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

        public String getIsjoin() {
            return isjoin;
        }

        public void setIsjoin(String isjoin) {
            this.isjoin = isjoin;
        }

        public String getStarlevel() {
            return starlevel;
        }

        public void setStarlevel(String starlevel) {
            this.starlevel = starlevel;
        }
    }
}
