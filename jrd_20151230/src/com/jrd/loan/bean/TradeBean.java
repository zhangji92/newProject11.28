package com.jrd.loan.bean;

import java.util.List;

public class TradeBean extends BaseBean {

    List<DetailList> detailList;

    public List<DetailList> getDetailLists() {
        return detailList;
    }

    public void setDetailLists(List<DetailList> detailLists) {
        this.detailList = detailLists;
    }

    public class DetailList {

        String time;
        String money;
        String type;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
