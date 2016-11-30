package com.jrd.loan.bean;

import java.io.Serializable;

/**
 * 更多详情list
 *
 * @author Aaron
 */
public class RepaymentPlanList implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String repaymentDate;// List中的还款日期
    private Double money;// List还款金额
    private String status;// List还款状态
    private String type;// List还款类型

    public RepaymentPlanList() {
        super();
    }

    public RepaymentPlanList(String repaymentDate, Double money, String status, String type) {
        super();
        this.repaymentDate = repaymentDate;
        this.money = money;
        this.status = status;
        this.type = type;
    }

    public String getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(String repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RepaymentPlanList [repaymentDate=" + repaymentDate + ", money=" + money + ", status=" + status + ", type=" + type + "]";
    }


}
