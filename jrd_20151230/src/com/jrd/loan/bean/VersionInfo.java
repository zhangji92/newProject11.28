package com.jrd.loan.bean;

import android.text.TextUtils;

public class VersionInfo {
	private int versionCode;
	private String versionName;
	private String mustUpdate;
	private String appUrl;
	private String isShowUpdateMsg;
	private String updateMsg;

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getMustUpdate() {
		return mustUpdate;
	}

	public void setMustUpdate(String mustUpdate) {
		this.mustUpdate = mustUpdate;
	}

	public String getIsShowUpdateMsg() {
		return TextUtils.isEmpty(isShowUpdateMsg) ? "" : isShowUpdateMsg;
	}

	public void setIsShowUpdateMsg(String isShowUpdateMsg) {
		this.isShowUpdateMsg = isShowUpdateMsg;
	}

	public String getUpdateMsg() {
		return TextUtils.isEmpty(updateMsg) ? "" : updateMsg;
	}

	public void setUpdateMsg(String updateMsg) {
		this.updateMsg = updateMsg;
	}

}
