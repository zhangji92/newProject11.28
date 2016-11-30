package com.jrd.loan.bean;

import java.io.Serializable;

public class BannerList implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String imgSrc;// 图片
    private String imgHref;// 链接地址
    private String imgTitlle;// 图片标题
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getImgHref() {
        return imgHref;
    }

    public void setImgHref(String imgHref) {
        this.imgHref = imgHref;
    }

    public String getImgTitlle() {
        return imgTitlle;
    }

    public void setImgTitlle(String imgTitlle) {
        this.imgTitlle = imgTitlle;
    }

    @Override
    public String toString() {
        return "BannerList [imgSrc=" + imgSrc + ", imgHref=" + imgHref + ", imgTitlle=" + imgTitlle + "]";
    }

}
