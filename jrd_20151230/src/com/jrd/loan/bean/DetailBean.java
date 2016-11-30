package com.jrd.loan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 更多详情实体类
 *
 * @author Aaron
 */
public class DetailBean extends BaseBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private List<RepaymentPlanList> repaymentPlan;// 还款计划列表
    String infoDetail;

    public String getInfoDetail() {
        return infoDetail;
    }

    public void setInfoDetail(String infoDetail) {
        this.infoDetail = infoDetail;
    }

    public List<RepaymentPlanList> getRepaymentPlan() {
        return repaymentPlan;
    }

    public void setRepaymentPlan(List<RepaymentPlanList> repaymentPlan) {
        this.repaymentPlan = repaymentPlan;
    }
}
