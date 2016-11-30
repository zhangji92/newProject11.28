package com.example.lenovo.amuse;

import android.app.Application;

import com.example.lenovo.amuse.mode.SuccessMode;
import com.example.lenovo.amuse.mode.TableListMode;
import com.example.lenovo.amuse.util.MyFinalDB;

import net.tsz.afinal.FinalDb;

import io.rong.imkit.RongIM;

/**
 * Created by lenovo on 2016/9/27.
 *
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
    }

    private TableListMode mTableListMode;

    public TableListMode getmTableListMode() {
        return mTableListMode;
    }

    public void setmTableListMode(TableListMode mTableListMode) {
        this.mTableListMode = mTableListMode;
    }

    //退出后的标识符
    private boolean flag = false;
    //账号密码
    private String user;
    private String pad;
    //代理商id
    private String agentId;
    //商品id
    private String goodsId;

    private String shopId;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    private SuccessMode successMode;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPad() {
        return pad;
    }

    public void setPad(String pad) {
        this.pad = pad;
    }

    public SuccessMode getSuccessMode() {
        return successMode;
    }


    public void setSuccessMode(SuccessMode successMode) {
        this.successMode = successMode;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
