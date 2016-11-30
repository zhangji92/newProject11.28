package com.jrd.loan.bean;

import java.io.Serializable;

/**
 * 用户信息
 */
public class PersonalInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    public String userId; // 用户唯一标识
    public String mobileNumber; // 注册手机号
    public String securityLevel; // 安全等级
    public String idNumberInfo; // 身份验证（身份证号）
    public String idNumberFlag;// 是否身份认证
    public String passwordInfo; // 登录密码数据
    public String passwordFlag; // 登录密码是否设置 0：未设置 1：已设置
    public String transPwdInfo; // 交易密码数据
    public String transPwdFlag; // 交易密码是否设置 0：未设置 1：已设置
    public String inviteCode; // 邀请码
    public String userName; // 用户名
    public String boundCardFlag;// 是否绑卡0:是没有绑卡,1已绑定
    public String quickCardFlag;// 是否重新绑定快捷卡0:是没有绑卡,1已绑定

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @return the mobileNumber
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * @return the securityLevel
     */
    public String getSecurityLevel() {
        return securityLevel;
    }

    /**
     * @return the idNumberInfo
     */
    public String getIdNumberInfo() {
        return idNumberInfo;
    }

    /**
     * @return the passwordInfo
     */
    public String getPasswordInfo() {
        return passwordInfo;
    }

    /**
     * @return the transPwdInfo
     */
    public String getTransPwdInfo() {
        return transPwdInfo;
    }

    /**
     * @return the inviteCode
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @param mobileNumber the mobileNumber to set
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * @param securityLevel the securityLevel to set
     */
    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    /**
     * @param idNumberInfo the idNumberInfo to set
     */
    public void setIdNumberInfo(String idNumberInfo) {
        this.idNumberInfo = idNumberInfo;
    }

    /**
     * @param passwordInfo the passwordInfo to set
     */
    public void setPasswordInfo(String passwordInfo) {
        this.passwordInfo = passwordInfo;
    }

    /**
     * @param transPwdInfo the transPwdInfo to set
     */
    public void setTransPwdInfo(String transPwdInfo) {
        this.transPwdInfo = transPwdInfo;
    }

    /**
     * @param inviteCode the inviteCode to set
     */
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    
    
    public String getQuickCardFlag() {
      return quickCardFlag;
    }

    public void setQuickCardFlag(String quickCardFlag) {
      this.quickCardFlag = quickCardFlag;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdNumberFlag() {
        return idNumberFlag;
    }

    public void setIdNumberFlag(String idNumberFlag) {
        this.idNumberFlag = idNumberFlag;
    }

    public String getBoundCardFlag() {
        return boundCardFlag;
    }

    public void setBoundCardFlag(String boundCardFlag) {
        this.boundCardFlag = boundCardFlag;
    }


    public String getTransPwdFlag() {
        return transPwdFlag;
    }

    public void setTransPwdFlag(String transPwdFlag) {
        this.transPwdFlag = transPwdFlag;
    }


    public String getPasswordFlag() {
        return passwordFlag;
    }

    public void setPasswordFlag(String passwordFlag) {
        this.passwordFlag = passwordFlag;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "PersonalInfo [userId=" + userId + ", mobileNumber=" + mobileNumber + ", securityLevel=" + securityLevel + ", idNumberInfo=" + idNumberInfo + ", passwordInfo=" + passwordInfo + ", transPwdInfo=" + transPwdInfo + ", inviteCode=" + inviteCode + ", userName=" + userName + "]";
    }
}
