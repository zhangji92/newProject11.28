package com.jrd.loan.bean;

import java.io.Serializable;

/**
 * 投标记录List
 *
 * @author Aaron
 */
public class InvestsList implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String account;// 用户账号(手机)
    private String createTime;// 投资时间
    private String platMode;// 投资渠道1 手机
    private int investAmount;// 投资金额
    private String useRedPacketFlag;// 使用优惠券标识1 移动端劵
    private String eventStr;// 活动标识：mobile701运动季

    public InvestsList() {
        super();
    }

    public InvestsList(String account, String createTime, String platMode, int investAmount, String useRedPacketFlag, String eventStr) {
        super();
        this.account = account;
        this.createTime = createTime;
        this.platMode = platMode;
        this.investAmount = investAmount;
        this.useRedPacketFlag = useRedPacketFlag;
        this.eventStr = eventStr;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

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

    public int getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(int investAmount) {
        this.investAmount = investAmount;
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

    @Override
    public String toString() {
        return "ResList [account=" + account + ", createTime=" + createTime + ", platMode=" + platMode + ", investAmount=" + investAmount + ", useRedPacketFlag=" + useRedPacketFlag + ", eventStr=" + eventStr + "]";
    }

}
