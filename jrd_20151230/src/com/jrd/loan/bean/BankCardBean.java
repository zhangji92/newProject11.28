package com.jrd.loan.bean;

public class BankCardBean {
	private String bankName;
	private String orderName;
	private String bankCode;
	private String bankImg;
	private String cardNumber;
	private String servicePhone;
	private String cityName;
	private String cityCode;
	private String bankBranchName;
	private boolean isShowDel;
	private String oneTime;
	private String oneMonth;
	private String oneDay;
	private String provinceCode;
	private String provinceName;


	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getOneTime() {
		return oneTime;
	}

	public void setOneTime(String oneTime) {
		this.oneTime = oneTime;
	}

	public String getOneMonth() {
		return oneMonth;
	}

	public void setOneMonth(String oneMonth) {
		this.oneMonth = oneMonth;
	}

	public String getOneDay() {
		return oneDay;
	}

	public void setOneDay(String oneDay) {
		this.oneDay = oneDay;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}

	public boolean isShowDel() {
		return isShowDel;
	}

	public void setShowDel(boolean isShowDel) {
		this.isShowDel = isShowDel;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getBankImg() {
		return bankImg;
	}

	public void setBankImg(String bankImg) {
		this.bankImg = bankImg;
	}

	public String getServicePhone() {
		return servicePhone;
	}

	public void setServicePhone(String servicePhone) {
		this.servicePhone = servicePhone;
	}

	@Override
	public String toString() {
		return "BankCardBean [bankName=" + bankName + ", bankCode=" + bankCode + ", bankImg=" + bankImg + ", cardNumber=" + cardNumber + ", isShowDel=" + isShowDel + "]";
	}
}
