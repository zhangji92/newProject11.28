package com.jrd.loan.bean;

import java.io.Serializable;

public class ProjectInvestsList implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String interest;// 已收利息
    private String name;// "医药经销企业融资项目"
    private String code;// "J15032701"
    private String moneyRate;// 年化利率/收益率,如12
    private String interestStartTime;// 计息日
    private String investAmount;// 投资金额
    private String collectAmount;// 待收金额
    private String bidSchedule;// 投标进度

    public ProjectInvestsList() {
        super();
    }

    public ProjectInvestsList(String interest, String name, String code, String moneyRate, String interestStartTime, String investAmount, String collectAmount, String bidSchedule) {
        super();
        this.interest = interest;
        this.name = name;
        this.code = code;
        this.moneyRate = moneyRate;
        this.interestStartTime = interestStartTime;
        this.investAmount = investAmount;
        this.collectAmount = collectAmount;
        this.bidSchedule = bidSchedule;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMoneyRate() {
        return moneyRate;
    }

    public void setMoneyRate(String moneyRate) {
        this.moneyRate = moneyRate;
    }

    public String getInterestStartTime() {
        return interestStartTime;
    }

    public void setInterestStartTime(String interestStartTime) {
        this.interestStartTime = interestStartTime;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getCollectAmount() {
        return collectAmount;
    }

    public void setCollectAmount(String collectAmount) {
        this.collectAmount = collectAmount;
    }

    public String getBidSchedule() {
        return bidSchedule;
    }

    public void setBidSchedule(String bidSchedule) {
        this.bidSchedule = bidSchedule;
    }

    @Override
    public String toString() {
        return "ProjectInvestsList [interest=" + interest + ", name=" + name + ", code=" + code + ", moneyRate=" + moneyRate + ", interestStartTime=" + interestStartTime + ", investAmount=" + investAmount + ", collectAmount=" + collectAmount + ", bidSchedule=" + bidSchedule + "]";
    }


}
