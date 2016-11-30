package com.jrd.loan.bean;

import java.util.List;

public class ReturnBean extends BaseBean {

    String backedTotalMoney; // 未还款总额
    String unBackTotalMoney; // 还款总额
    List<BackPlans> backPlans;


    public List<BackPlans> getBackPlans() {
        return backPlans;
    }

    public void setBackPlans(List<BackPlans> backPlans) {
        this.backPlans = backPlans;
    }

    public String getBackedTotalMoney() {
        return backedTotalMoney;
    }

    public void setBackedTotalMoney(String backedTotalMoney) {
        this.backedTotalMoney = backedTotalMoney;
    }

    public String getUnBackTotalMoney() {
        return unBackTotalMoney;
    }

    public void setUnBackTotalMoney(String unBackTotalMoney) {
        this.unBackTotalMoney = unBackTotalMoney;
    }

    public class BackPlans {

        String type;
        String repaymentDate;
        String money;
        String projectName;


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRepaymentDate() {
            return repaymentDate;
        }

        public void setRepaymentDate(String repaymentDate) {
            this.repaymentDate = repaymentDate;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getProjectName() {
          return projectName;
        }

        public void setProjectName(String projectName) {
          this.projectName = projectName;
        }
        
    }
}
