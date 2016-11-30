package com.jrd.loan.bean;

public class MyPocketBean extends BaseBean {

  private String profitToday; // 昨日收益
  private String backedInterest; // 累计收益
  private String profitCanTakeOut; // 可提取收益
  private String remainderAmt; // 加入本金
  private String redeemAmtSum; // 处理中的赎回金额总和
  private String count; // 加入活期宝的次数
  private Double annualRate; // 年化利率
  private String leftInvestAmount; // 剩余额度
  private String sysRemainderAmtCanTakeOut; // 平台当前可提取金额

  public String getProfitToday() {
    return profitToday;
  }

  public void setProfitToday(String profitToday) {
    this.profitToday = profitToday;
  }

  public String getBackedInterest() {
    return backedInterest;
  }

  public void setBackedInterest(String backedInterest) {
    this.backedInterest = backedInterest;
  }

  public String getProfitCanTakeOut() {
    return profitCanTakeOut;
  }

  public void setProfitCanTakeOut(String profitCanTakeOut) {
    this.profitCanTakeOut = profitCanTakeOut;
  }

  public String getRemainderAmt() {
    return remainderAmt;
  }

  public void setRemainderAmt(String remainderAmt) {
    this.remainderAmt = remainderAmt;
  }

  public String getRedeemAmtSum() {
    return redeemAmtSum;
  }

  public void setRedeemAmtSum(String redeemAmtSum) {
    this.redeemAmtSum = redeemAmtSum;
  }

  public String getCount() {
    return count;
  }

  public void setCount(String count) {
    this.count = count;
  }

  public Double getAnnualRate() {
    return annualRate;
  }

  public void setAnnualRate(Double annualRate) {
    this.annualRate = annualRate;
  }

  public String getLeftInvestAmount() {
    return leftInvestAmount;
  }

  public void setLeftInvestAmount(String leftInvestAmount) {
    this.leftInvestAmount = leftInvestAmount;
  }

  public String getSysRemainderAmtCanTakeOut() {
    return sysRemainderAmtCanTakeOut;
  }

  public void setSysRemainderAmtCanTakeOut(String sysRemainderAmtCanTakeOut) {
    this.sysRemainderAmtCanTakeOut = sysRemainderAmtCanTakeOut;
  }

}
