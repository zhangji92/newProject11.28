package com.jrd.loan.bean;

import java.util.List;

public class BankListBean extends BaseBean {

    /**
     * bankName : 中国农业银行
     * orderName : 中国农业银行
     * bankCode : 103
     * oneTime : 50000.00
     * oneDay : 300000.00
     * oneMonth : 9000000.00
     * servicePhone : 95599
     */

    private List<RecordsEntity> records;

    public void setRecords(List<RecordsEntity> records) {
        this.records = records;
    }

    public List<RecordsEntity> getRecords() {
        return records;
    }

    public static class RecordsEntity {
        private String bankName;
        private String orderName;
        private String bankCode;
        private String oneTime;
        private String oneDay;
        private String oneMonth;
        private String servicePhone;

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public void setOrderName(String orderName) {
            this.orderName = orderName;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public void setOneTime(String oneTime) {
            this.oneTime = oneTime;
        }

        public void setOneDay(String oneDay) {
            this.oneDay = oneDay;
        }

        public void setOneMonth(String oneMonth) {
            this.oneMonth = oneMonth;
        }

        public void setServicePhone(String servicePhone) {
            this.servicePhone = servicePhone;
        }

        public String getBankName() {
            return bankName;
        }

        public String getOrderName() {
            return orderName;
        }

        public String getBankCode() {
            return bankCode;
        }

        public String getOneTime() {
            return oneTime;
        }

        public String getOneDay() {
            return oneDay;
        }

        public String getOneMonth() {
            return oneMonth;
        }

        public String getServicePhone() {
            return servicePhone;
        }
    }
}
