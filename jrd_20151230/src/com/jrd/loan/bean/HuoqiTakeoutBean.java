package com.jrd.loan.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class HuoqiTakeoutBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String type;// 转入类型0余额、1 银行卡
    private String detail;// 描述
    private String amount;// 金额
    private String oprateTime;// 操作时间
    private ArrayList<HuoqiList> records;// 记录集合

    public HuoqiTakeoutBean(String type, String detail, String amount, String oprateTime, ArrayList<HuoqiList> records) {
        super();
        this.type = type;
        this.detail = detail;
        this.amount = amount;
        this.oprateTime = oprateTime;
        this.records = records;
    }

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
