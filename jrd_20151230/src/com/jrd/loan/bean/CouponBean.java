package com.jrd.loan.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 优惠券列表
 *
 * @author Jacky
 */
public class CouponBean extends BaseBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Coupon> packets;

    public ArrayList<Coupon> getPackets() {
        return packets;
    }

    public void setPackets(ArrayList<Coupon> redPackInfoList) {
        this.packets = redPackInfoList;
    }
}
