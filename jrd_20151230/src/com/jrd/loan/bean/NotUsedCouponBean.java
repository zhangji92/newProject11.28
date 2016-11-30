package com.jrd.loan.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 我的优惠券实体类
 *
 * @author Aaron
 */
public class NotUsedCouponBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String listSize;// 查询数据list长短，方便页面操作
    private ArrayList<NotUsedCoupon> couponsList;

    public String getListSize() {
        return listSize;
    }

    public void setListSize(String listSize) {
        this.listSize = listSize;
    }

    public ArrayList<NotUsedCoupon> getCouponsList() {
        return couponsList;
    }

    public void setCouponsList(ArrayList<NotUsedCoupon> couponsList) {
        this.couponsList = couponsList;
    }


}
