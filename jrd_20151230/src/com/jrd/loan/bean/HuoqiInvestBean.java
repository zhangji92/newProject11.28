package com.jrd.loan.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 活期转入(充值)
 *
 * @author Aaron
 */
public class HuoqiInvestBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String type;
    private String detail;
    private String amount;
    private String oprateTime;
    private ArrayList<HuoqiList> records;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOprateTime() {
        return oprateTime;
    }

    public void setOprateTime(String oprateTime) {
        this.oprateTime = oprateTime;
    }

    public ArrayList<HuoqiList> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<HuoqiList> records) {
        this.records = records;
    }

}
