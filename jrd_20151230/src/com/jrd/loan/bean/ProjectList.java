package com.jrd.loan.bean;

import java.io.Serializable;

/**
 * 首页标的信息集合
 *
 * @author Aaron
 */
public class ProjectList implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mockId;// 标的短码
    private String projectId;// 标的ID
    private String projectCode;// 标的编号
    private String projectName;// 标的名称
    private String annualRate;// 年化利率
    private String rewardRate;// 年化利率复利
    private String investmentPeriod;// 标的期限
    private String investmentAmount;// 可投金额
    private String surplusAmount;// 融资金额
    private String investPercentage;// 已融资金额百分比
    private String type;// 融资产品类型
    private String typeName;// 融资产品名
    private String isVip;// VIP标的 是:1,否0
    private String isNovice;// 新手标的 是:1,否0
    private String brandName;// 品牌名称

    // 以下字段在项目介绍中使用
    private String brandType; // 品牌代码
    private String financeAmt; // 预计融资金额
    private String repaymentType; // 还款方式
    private String greenInvester;
    private int isFirstInvest;
    private String repaymentEndDate;
    private String status;            //标的状态

    private double minInvestmentAmount;        //最小投资额
    private double maxInvestmentAmount;        //最大投资额
    private int inverstRecordCount;                    //已投资人数
    private String projectBeginDate;                    //标的上线开始时间
    private String comingInfo;

    private int firstFlag;        //用户是不是第一次投资(0 不是)

    public int getFirstFlag() {
        return firstFlag;
    }

    public void setFirstFlag(int firstFlag) {
        this.firstFlag = firstFlag;
    }

    public String getComingInfo() {
        return comingInfo;
    }

    public void setComingInfo(String comingInfo) {
        this.comingInfo = comingInfo;
    }

    public ProjectList(String mockId, String projectId, String projectCode, String projectName, String annualRate, String rewardRate, String investmentPeriod, String investmentAmount,
                       String surplusAmount, String investPercentage, String type, String typeName, String isVip, String isNovice, String brandName) {
        super();
        this.mockId = mockId;
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.annualRate = annualRate;
        this.rewardRate = rewardRate;
        this.investmentPeriod = investmentPeriod;
        this.investmentAmount = investmentAmount;
        this.surplusAmount = surplusAmount;
        this.investPercentage = investPercentage;
        this.type = type;
        this.typeName = typeName;
        this.isVip = isVip;
        this.isNovice = isNovice;
        this.brandName = brandName;
    }

    public ProjectList(SevenInfo sevenInfo) {
        this.annualRate = sevenInfo.getAnnualRate(); // 年化利率如：7.0%
        this.investmentPeriod = sevenInfo.getInvestmentPeriod(); // 7付你期限
        this.investmentAmount = sevenInfo.getInvestmentAmount(); // 剩余额度
        this.brandName = sevenInfo.getUpdateDesc(); // 7付你更新描述
        this.projectId = sevenInfo.getProjectId(); // 标的ID
        this.projectName = sevenInfo.getMinInvestAmount(); //
        this.rewardRate = sevenInfo.getMaxinvestAmount(); //
        this.typeName = sevenInfo.getInverstRecordCount(); // 加入记录
        this.type = "-1";
    }

    public double getMinInvestmentAmount() {
        return minInvestmentAmount;
    }

    public void setMinInvestmentAmount(double minInvestmentAmount) {
        this.minInvestmentAmount = minInvestmentAmount;
    }

    public double getMaxInvestmentAmount() {
        return maxInvestmentAmount;
    }

    public void setMaxInvestmentAmount(double maxInvestmentAmount) {
        this.maxInvestmentAmount = maxInvestmentAmount;
    }

    public int getInverstRecordCount() {
        return inverstRecordCount;
    }

    public void setInverstRecordCount(int inverstRecordCount) {
        this.inverstRecordCount = inverstRecordCount;
    }

    public String getProjectBeginDate() {
        return projectBeginDate;
    }

    public void setProjectBeginDate(String projectBeginDate) {
        this.projectBeginDate = projectBeginDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRepaymentEndDate() {
        return repaymentEndDate;
    }

    public void setRepaymentEndDate(String repaymentEndDate) {
        this.repaymentEndDate = repaymentEndDate;
    }

    public int getIsFirstInvest() {
        return isFirstInvest;
    }

    public void setIsFirstInvest(int isFirstInvest) {
        this.isFirstInvest = isFirstInvest;
    }

    public String getGreenInvester() {
        return greenInvester;
    }

    public void setGreenInvester(String greenInvester) {
        this.greenInvester = greenInvester;
    }

    public String getMockId() {
        return mockId;
    }

    public void setMockId(String mockId) {
        this.mockId = mockId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(String annualRate) {
        this.annualRate = annualRate;
    }

    public String getRewardRate() {
        return rewardRate;
    }

    public void setRewardRate(String rewardRate) {
        this.rewardRate = rewardRate;
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

    public String getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(String surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public String getInvestPercentage() {
        return investPercentage;
    }

    public void setInvestPercentage(String investPercentage) {
        this.investPercentage = investPercentage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getIsVip() {
        return isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    public String getIsNovice() {
        return isNovice;
    }

    public void setIsNovice(String isNovice) {
        this.isNovice = isNovice;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandType() {
        return brandType;
    }

    public void setBrandType(String brandType) {
        this.brandType = brandType;
    }

    public String getFinanceAmt() {
        return financeAmt;
    }

    public void setFinanceAmt(String financeAmt) {
        this.financeAmt = financeAmt;
    }

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    @Override
    public String toString() {
        return "ProjectList [mockId=" + mockId + ", projectId=" + projectId + ", projectCode=" + projectCode + ", projectName=" + projectName + ", annualRate=" + annualRate + ", rewardRate="
                + rewardRate + ", investmentPeriod=" + investmentPeriod + ", investmentAmount=" + investmentAmount + ", surplusAmount=" + surplusAmount + ", investPercentage=" + investPercentage
                + ", type=" + type + ", typeName=" + typeName + ", isVip=" + isVip + ", isNovice=" + isNovice + ", brandName=" + brandName + "]";
    }

}
