package com.example.administrator.beijingplayer.mode;

import java.util.List;

/**
 * 集合和对象的类
 */
public class HomeRes {

    private Pictury[] ad;

    private ShopMessages recommend;

    public HomeRes() {
    }

    public HomeRes(Pictury[] ad, ShopMessages recommend) {
        this.ad = ad;
        this.recommend = recommend;
    }

    public Pictury[] getPicList() {
        return ad;
    }

    public void setPicList(Pictury[] ad) {
        this.ad = ad;
    }

    public ShopMessages getRecommend() {
        return recommend;
    }

    public void setRecommend(ShopMessages recommend) {
        this.recommend = recommend;
    }
}
