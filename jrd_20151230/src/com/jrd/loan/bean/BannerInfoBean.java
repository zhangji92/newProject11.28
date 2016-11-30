package com.jrd.loan.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * banner信息实体类
 *
 * @author Aaron
 */
public class BannerInfoBean extends BaseBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<BannerList> bannerList;

    public ArrayList<BannerList> getBannerList() {
        return bannerList;
    }

    public void setBannerList(ArrayList<BannerList> bannerList) {
        this.bannerList = bannerList;
    }

}
