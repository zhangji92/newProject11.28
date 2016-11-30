package com.jrd.loan.bean;

import java.util.List;

public class TenderRecordBean extends BaseBean {

    List<TenderList> invests;

    public List<TenderList> getResList() {
        return invests;
    }

    public void setResList(List<TenderList> resList) {
        this.invests = resList;
    }

    public class TenderList {

        String createTime; //时间
        String platMode; //投资渠道
        String account; //账户名
        String useRedPacketFlag; // 使用优惠券标识
        String eventStr; //活动标识
        String investAmount; //投资金额

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPlatMode() {
            return platMode;
        }

        public void setPlatMode(String platMode) {
            this.platMode = platMode;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getUseRedPacketFlag() {
            return useRedPacketFlag;
        }

        public void setUseRedPacketFlag(String useRedPacketFlag) {
            this.useRedPacketFlag = useRedPacketFlag;
        }

        public String getEventStr() {
            return eventStr;
        }

        public void setEventStr(String eventStr) {
            this.eventStr = eventStr;
        }

        public String getInvestAmount() {
            return investAmount;
        }

        public void setInvestAmount(String investAmount) {
            this.investAmount = investAmount;
        }
    }
}
