package com.jrd.loan.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 付款账号查询实体类
 *
 * @author Aaron
 */
public class PaymentAccountBean extends BaseBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<RecordsList> records;

    public ArrayList<RecordsList> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<RecordsList> records) {
        this.records = records;
    }

}
