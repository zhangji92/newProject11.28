package com.jrd.loan.bean;

public class AssetsBean extends BaseBean {

    String totalMoney; //总资产
    String accountBalance;//可用余额
    String backingInterest;//待收收益
    String backingPrincipal;//待收本金
    String frozenMoney;//冻结金额
    String sumInvestAmount;//累计投资
    String backedInterest;//累计收益

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getBackingInterest() {
        return backingInterest;
    }

    public void setBackingInterest(String backingInterest) {
        this.backingInterest = backingInterest;
    }

    public String getBackingPrincipal() {
        return backingPrincipal;
    }

    public void setBackingPrincipal(String backingPrincipal) {
        this.backingPrincipal = backingPrincipal;
    }

    public String getFrozenMoney() {
        return frozenMoney;
    }

    public void setFrozenMoney(String frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    public String getSumInvestAmount() {
        return sumInvestAmount;
    }

    public void setSumInvestAmount(String sumInvestAmount) {
        this.sumInvestAmount = sumInvestAmount;
    }

    public String getBackedInterest() {
        return backedInterest;
    }

    public void setBackedInterest(String backedInterest) {
        this.backedInterest = backedInterest;
    }
}
