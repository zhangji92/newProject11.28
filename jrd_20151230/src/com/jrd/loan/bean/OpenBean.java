package com.jrd.loan.bean;

import java.util.List;

public class OpenBean extends BaseBean {

    List<BankCardBean> bankInfoList;

    List<CityBean> cityInfoList;

    public List<CityBean> getCityInfoList() {
        return cityInfoList;
    }

    public void setCityInfoList(List<CityBean> cityInfoList) {
        this.cityInfoList = cityInfoList;
    }

    public List<BankCardBean> getBankInfoList() {
        return bankInfoList;
    }

    public void setBankInfoList(List<BankCardBean> bankInfoList) {
        this.bankInfoList = bankInfoList;
    }

}
