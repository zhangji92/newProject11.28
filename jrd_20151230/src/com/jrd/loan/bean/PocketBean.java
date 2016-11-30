package com.jrd.loan.bean;

import android.text.TextUtils;

public final class PocketBean extends BaseBean {
    private String projectId; // 标的ID
    private String projectName; // 标的名称
    private String annualRate; // 年化利率
    private String leftInvestAmount; // 剩余额度
    private String accountBalance; // 可用余额
    private int investsCount; // 加入人数

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAnnualRate() {
        return TextUtils.isEmpty(annualRate) ? "" : annualRate;
    }

    public void setAnnualRate(String annualRate) {
        this.annualRate = annualRate;
    }

    public String getLeftInvestAmount() {
        return leftInvestAmount;
    }

    public void setLeftInvestAmount(String leftInvestAmount) {
        this.leftInvestAmount = leftInvestAmount;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public int getInvestsCount() {
        return investsCount;
    }

    public void setInvestsCount(int investsCount) {
        this.investsCount = investsCount;
    }

    @Override
    public String toString() {
        return "PocketBean [projectId=" + projectId + ", projectName=" + projectName + ", annualRate=" + annualRate + ", leftInvestAmount=" + leftInvestAmount + ", accountBalance=" + accountBalance + ", investsCount=" + investsCount + "]";
    }
}