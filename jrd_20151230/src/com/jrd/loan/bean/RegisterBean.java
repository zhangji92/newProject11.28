package com.jrd.loan.bean;

public class RegisterBean extends BaseBean {
    String userId;
    String sessionid;
    String mobileNumber;
    String isMobile;
    String idNumberInfo; // 是否身份认证
    String passwordInfo; // 是否设置密码
    String transPwdFlag; // 是否设置交易密码
    String boundCardFlag; // 是否绑卡

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @return the sessionid
     */
    public String getSessionid() {
        return sessionid;
    }

    /**
     * @return the mobileNumber
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * @return the isMobile
     */
    public String getIsMobile() {
        return isMobile;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @param sessionid the sessionid to set
     */
    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    /**
     * @param mobileNumber the mobileNumber to set
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * @param isMobile the isMobile to set
     */
    public void setIsMobile(String isMobile) {
        this.isMobile = isMobile;
    }

    public String getIdNumberInfo() {
        return idNumberInfo;
    }

    public void setIdNumberInfo(String idNumberInfo) {
        this.idNumberInfo = idNumberInfo;
    }

    public String getPasswordInfo() {
        return passwordInfo;
    }

    public void setPasswordInfo(String passwordInfo) {
        this.passwordInfo = passwordInfo;
    }

    public String getTransPwdFlag() {
        return transPwdFlag;
    }

    public void setTransPwdFlag(String transPwdFlag) {
        this.transPwdFlag = transPwdFlag;
    }

    public String getBoundCardFlag() {
        return boundCardFlag;
    }

    public void setBoundCardFlag(String boundCardFlag) {
        this.boundCardFlag = boundCardFlag;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RegisterBean [userId=" + userId + ", sessionid=" + sessionid + ", mobileNumber=" + mobileNumber + ", isMobile=" + isMobile + "]";
    }

}
