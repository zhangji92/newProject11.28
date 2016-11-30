package com.jrd.loan.bean;

import java.io.Serializable;

/**
 * 付款账号查询 List
 *
 * @author Aaron
 */
public class RecordsList implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String type;// 类型：0 账号余额、1 银行卡
    private String name;// 名称
    private String cardNumber;// 银行卡号
    private String amount;// 余额/可转入额
    private String bankImg;// 银行logo
    private String bankCode;//
    private String cardNo;// 尾号
    private Double annualRate;// 年华利率

    public RecordsList() {
        super();
    }


    public RecordsList(String type, String name, String amount, Double annualRate) {
        super();
        this.type = type;
        this.name = name;
        this.amount = amount;
        this.annualRate = annualRate;
    }


    public RecordsList(String type, String name, String cardNumber, String amount, String bankImg, String bankCode, String cardNo) {
        super();
        this.type = type;
        this.name = name;
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.bankImg = bankImg;
        this.bankCode = bankCode;
        this.cardNo = cardNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBankImg() {
        return bankImg;
    }

    public void setBankImg(String bankImg) {
        this.bankImg = bankImg;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Double getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(Double annualRate) {
        this.annualRate = annualRate;
    }

    @Override
    public String toString() {
        return "RecordsList [type=" + type + ", name=" + name + ", cardNumber=" + cardNumber + ", amount=" + amount + ", bankImg=" + bankImg + ", bankCode=" + bankCode + ", cardNo=" + cardNo + "]";
    }


}
