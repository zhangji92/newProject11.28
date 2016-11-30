package com.jrd.loan.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Coupon implements Serializable {
  // 优惠券的类型
  public final static String COUPON_TYPE_CASH = "1"; // 现金券
  public final static String COUPON_TYPE_ADD_PROFIT = "2"; // 加息券

  private String hbType; // 优惠券的类型
  private int hbEntId;
  private String hbResume; // 优惠券内容
  private double bagVal;
  private String hbTitle;
  private long expireTime; // 有效期
  private String hbDesc;
  private long prjTimeLimit;
  private String source;
  private boolean available; // 优惠券是否有效
  private int state;

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public String getHbType() {
    return hbType;
  }

  public void setHbType(String hbType) {
    this.hbType = hbType;
  }

  public int getHbEntId() {
    return hbEntId;
  }

  public void setHbEntId(int hbEntId) {
    this.hbEntId = hbEntId;
  }

  public String getHbResume() {
    return hbResume;
  }

  public void setHbResume(String hbResume) {
    this.hbResume = hbResume;
  }

  public double getBagVal() {
    return bagVal;
  }

  public void setBagVal(double bagVal) {
    this.bagVal = bagVal;
  }

  public String getHbTitle() {
    return hbTitle;
  }

  public void setHbTitle(String hbTitle) {
    this.hbTitle = hbTitle;
  }

  public String getExpireTime() {
    return new SimpleDateFormat("yyyy年MM月dd日").format(new Date(this.expireTime));
  }

  public void setExpireTime(long expireTime) {
    this.expireTime = expireTime;
  }

  public String getHbDesc() {
    return hbDesc;
  }

  public void setHbDesc(String hbDesc) {
    this.hbDesc = hbDesc;
  }

  public long getPrjTimeLimit() {
    return prjTimeLimit;
  }

  public void setPrjTimeLimit(long prjTimeLimit) {
    this.prjTimeLimit = prjTimeLimit;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  @Override
  public String toString() {
    return "Coupon [hbType=" + hbType + ", hbEntId=" + hbEntId + ", hbResume=" + hbResume + ", bagVal=" + bagVal + ", hbTitle=" + hbTitle + ", expireTime=" + expireTime + ", hbDesc=" + hbDesc + ", prjTimeLimit=" + prjTimeLimit + ", source=" + source + ", available=" + available + ", state=" + state
        + "]";
  }
}
