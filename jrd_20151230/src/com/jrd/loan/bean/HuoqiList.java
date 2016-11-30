package com.jrd.loan.bean;

import java.io.Serializable;

public class HuoqiList implements Serializable {
    private static final long serialVersionUID = 1L;
    private String datetime;// 时间
    private String detail;// 过程
    private String title;// 标题
    private String status;// 激活状态0未激活；1已激活


    public HuoqiList() {
        super();
    }


    public HuoqiList(String datetime, String detail, String title, String status) {
        super();
        this.datetime = datetime;
        this.detail = detail;
        this.title = title;
        this.status = status;
    }


    public String getDatetime() {
        return datetime;
    }


    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }


    public String getDetail() {
        return detail;
    }


    public void setDetail(String detail) {
        this.detail = detail;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "HuoqiList [datetime=" + datetime + ", detail=" + detail + ", title=" + title + ", status=" + status + "]";
    }

}
