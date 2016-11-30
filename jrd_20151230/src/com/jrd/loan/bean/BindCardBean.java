package com.jrd.loan.bean;

import java.util.List;

public class BindCardBean extends BaseBean {
	private List<BankCardBean> usrCardInfoList;

	public List<BankCardBean> getUsrCardInfoList() {
		return usrCardInfoList;
	}

	public void setUsrCardInfoList(List<BankCardBean> usrCardInfoList) {
		this.usrCardInfoList = usrCardInfoList;
	}

	@Override
	public String toString() {
		return "BindCardBean [usrCardInfoList=" + usrCardInfoList + "]";
	}
}