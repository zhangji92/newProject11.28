package com.jrd.loan.bean;

import java.util.List;

public class SevenBean extends BaseBean {

    List<Records> records;


    public List<Records> getRecords() {
        return records;
    }


    public void setRecords(List<Records> records) {
        this.records = records;
    }


    public class Records {
        String date;
        double profit;

        public String getDate() {
            return this.date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }
    }
}
