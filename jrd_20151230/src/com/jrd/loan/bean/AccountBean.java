package com.jrd.loan.bean;

public class AccountBean extends BaseBean {
	userInfo userInfo;
	String userName;
	String idNumberInfo; // 是否身份认证
	String passwordInfo; // 是否设置密码
	String transPwdFlag; // 是否设置交易密码
	String boundCardFlag; // 是否绑卡
	String quickCardFlag; // 是佛绑快捷卡 (0 未绑卡, 1 已绑卡)

	public String getQuickCardFlag() {
		return quickCardFlag;
	}

	public void setQuickCardFlag(String quickCardFlag) {
		this.quickCardFlag = quickCardFlag;
	}

	public String getIdNumberInfo() {
		return idNumberInfo;
	}

	public void setIdNumberInfo(String idNumberInfo) {
		this.idNumberInfo = idNumberInfo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public userInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(userInfo userInfo) {
		this.userInfo = userInfo;
	}

	public class userInfo {
		String mobileNumber;
		String yesterdayEarnings;
		String totalAssets;
		String accountBalance;
		String availableInvestMoney;
		String safeLevel;

		public String getMobileNumber() {
			return mobileNumber;
		}

		public void setMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
		}

		public String getYesterdayEarnings() {
			return yesterdayEarnings;
		}

		public void setYesterdayEarnings(String yesterdayEarnings) {
			this.yesterdayEarnings = yesterdayEarnings;
		}

		public String getTotalAssets() {
			return totalAssets;
		}

		public void setTotalAssets(String totalAssets) {
			this.totalAssets = totalAssets;
		}

		public String getAccountBalance() {
			return accountBalance;
		}

		public void setAccountBalance(String accountBalance) {
			this.accountBalance = accountBalance;
		}

		public String getSafeLevel() {
			return safeLevel;
		}

		public void setSafeLevel(String safeLevel) {
			this.safeLevel = safeLevel;
		}

		public String getAvailableInvestMoney() {
			return availableInvestMoney;
		}

		public void setAvailableInvestMoney(String availableInvestMoney) {
			this.availableInvestMoney = availableInvestMoney;
		}

	}
}
