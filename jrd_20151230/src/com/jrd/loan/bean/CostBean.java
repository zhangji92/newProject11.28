package com.jrd.loan.bean;

public class CostBean extends BaseBean {

    String moneyCost; // 提现费用
    String actuallyCharge;// 实际扣除金额

    public String getMoneyCost() {
        return moneyCost;
    }

    public void setMoneyCost(String moneyCost) {
        this.moneyCost = moneyCost;
    }

    public String getActuallyCharge() {
        return actuallyCharge;
    }

    public void setActuallyCharge(String actuallyCharge) {
        this.actuallyCharge = actuallyCharge;
    }

}
