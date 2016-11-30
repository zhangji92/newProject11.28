package com.jrd.loan.bean;

import java.io.Serializable;

public class SevenInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String updateDesc;// 7付你更新描述
    private String annualRate;// 年化利率如：7.0%
    private String investmentPeriod;// 7付你期限
    private String investmentAmount;// 可投金额
    private String projectId;// 标的ID
    private String minInvestAmount;//
    private String maxinvestAmount;//
    private String inverstRecordCount;// 加入记录
    private Boolean isInvestment;// 是否已满标

    public SevenInfo() {
        super();
    }

    public SevenInfo(String updateDesc, String annualRate, String investmentPeriod, String investmentAmount, String projectId, String minInvestAmount, String maxinvestAmount,
                     String inverstRecordCount, Boolean isInvestment) {
        super();
        this.updateDesc = updateDesc;
        this.annualRate = annualRate;
        this.investmentPeriod = investmentPeriod;
        this.investmentAmount = investmentAmount;
        this.projectId = projectId;
        this.minInvestAmount = minInvestAmount;
        this.maxinvestAmount = maxinvestAmount;
        this.inverstRecordCount = inverstRecordCount;
        this.isInvestment = isInvestment;
    }

    public String getUpdateDesc() {
        return updateDesc;
    }

    public void setUpdateDesc(String updateDesc) {
        this.updateDesc = updateDesc;
    }

    public String getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(String annualRate) {
        this.annualRate = annualRate;
    }

    public String getInvestmentPeriod() {
        return investmentPeriod;
    }

    public void setInvestmentPeriod(String investmentPeriod) {
        this.investmentPeriod = investmentPeriod;
    }

    public String getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(String investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(String minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public String getMaxinvestAmount() {
        return maxinvestAmount;
    }

    public void setMaxinvestAmount(String maxinvestAmount) {
        this.maxinvestAmount = maxinvestAmount;
    }

    public String getInverstRecordCount() {
        return inverstRecordCount;
    }

    public void setInverstRecordCount(String inverstRecordCount) {
        this.inverstRecordCount = inverstRecordCount;
    }

    public Boolean getIsInvestment() {
        return isInvestment;
    }

    public void setIsInvestment(Boolean isInvestment) {
        this.isInvestment = isInvestment;
    }

    @Override
    public String toString() {
        return "SevenInfo [updateDesc=" + updateDesc + ", annualRate=" + annualRate + ", investmentPeriod=" + investmentPeriod + ", investmentAmount=" + investmentAmount + ", projectId=" + projectId
                + ", minInvestAmount=" + minInvestAmount + ", maxinvestAmount=" + maxinvestAmount + ", inverstRecordCount=" + inverstRecordCount + ", isInvestment=" + isInvestment + "]";
    }

}
