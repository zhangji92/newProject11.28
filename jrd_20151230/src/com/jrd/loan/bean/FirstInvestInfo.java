package com.jrd.loan.bean;

import java.io.Serializable;

public class FirstInvestInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userOneAccount;
    private String userTwoAccount;
    private int userTwoAmount;
    private String userThreeAccount;
    private String firstInvestUrl;

    public String getFirstInvestUrl() {
        return firstInvestUrl;
    }

    public void setFirstInvestUrl(String firstInvestUrl) {
        this.firstInvestUrl = firstInvestUrl;
    }

    public String getUserOneAccount() {
        return userOneAccount;
    }

    public void setUserOneAccount(String userOneAccount) {
        this.userOneAccount = userOneAccount;
    }

    public String getUserTwoAccount() {
        return userTwoAccount;
    }

    public void setUserTwoAccount(String userTwoAccount) {
        this.userTwoAccount = userTwoAccount;
    }

    public String getUserTwoAmount() {
        return String.valueOf(userTwoAmount);
    }

    public void setUserTwoAmount(int userTwoAmount) {
        this.userTwoAmount = userTwoAmount;
    }

    public String getUserThreeAccount() {
        return userThreeAccount;
    }

    public void setUserThreeAccount(String userThreeAccount) {
        this.userThreeAccount = userThreeAccount;
    }
}