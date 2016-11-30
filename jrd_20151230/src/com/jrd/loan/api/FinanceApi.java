package com.jrd.loan.api;

import android.content.Context;
import android.text.TextUtils;

import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.BidRecordBean;
import com.jrd.loan.bean.CouponBean;
import com.jrd.loan.bean.DetailBean;
import com.jrd.loan.bean.InvestRecordBean;
import com.jrd.loan.bean.NotUsedCouponBean;
import com.jrd.loan.bean.ProjectDetailBean;
import com.jrd.loan.net.framework.HttpTask;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;

public class FinanceApi {
    // 项目介绍
    public static void obtainProjectIntroduce(Context context, String mockId, OnHttpTaskListener<ProjectDetailBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.FinanceApi.GETPROJECTINFO, HttpTask.METHOD_POST);
        httpTask.putParam("mockId", mockId);

        if (UserInfoPrefs.isLogin()) {
            httpTask.putParam("userId", UserInfoPrefs.getUserId());
        }

        httpTask.runTask(ProjectDetailBean.class, httpTaskListener);
    }

    // 更多详情
    public static void getDetailInfo(Context context, String mockId, String flag, OnHttpTaskListener<DetailBean> onHttpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.FinanceApi.GETPROJECTDETAILINFO, HttpTask.METHOD_POST);
        httpTask.putParam("mockId", mockId);
        httpTask.putParam("flag", flag);

        httpTask.runTask(DetailBean.class, onHttpTaskListener);
    }

    // 投资记录
    public static void getInvestRecord(Context context, int pageSize, int pageNo, String status, OnHttpTaskListener<InvestRecordBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PayApi.GETUBVESTRECORD, HttpTask.METHOD_POST);
        httpTask.putParam("userId", UserInfoPrefs.getUserId());
        httpTask.putParam("pageSize", String.valueOf(pageSize));
        httpTask.putParam("pageNo", String.valueOf(pageNo));
        httpTask.putParam("status", status);

        httpTask.runTask(InvestRecordBean.class, httpTaskListener);
    }

    // 投标记录
    public static void getRecordInfo(Context context, String mockId, int pageSize, int pageNo, OnHttpTaskListener<BidRecordBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.FinanceApi.GETPROJECTRECORDS, HttpTask.METHOD_POST);
        httpTask.putParam("mockId", mockId);
        httpTask.putParam("pageSize", String.valueOf(pageSize));
        httpTask.putParam("pageNo", String.valueOf(pageNo));

        httpTask.runTask(BidRecordBean.class, httpTaskListener);
    }

    // 确定投标
    public static void confirmBid(Context context, String pojectId, String userId, String investAmount, String hbEntId, OnHttpTaskListener<BaseBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.FinanceApi.INVEST, HttpTask.METHOD_POST);
        httpTask.setUseJson(true);
        httpTask.putParam("projectId", pojectId);
        httpTask.putParam("userId", userId);
        httpTask.putParam("investAmount", investAmount);
        httpTask.putParam("platform", 0);

        if (!TextUtils.isEmpty(hbEntId)) {
            httpTask.putParam("hbEntId", hbEntId);
        }

        httpTask.runTask(BaseBean.class, httpTaskListener);
    }

    // 获取优惠券
    public static void obtainCouponList(Context context, String pojectId, String investAmount, String investmentPeriod, String isNovice, OnHttpTaskListener<CouponBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.FinanceApi.COUPON_LIST, HttpTask.METHOD_POST);
        httpTask.putParam("projectId", pojectId);
        httpTask.putParam("userId", UserInfoPrefs.getUserId());
        httpTask.putParam("investAmount", Double.parseDouble(investAmount));
        httpTask.putParam("investmentPeriod", investmentPeriod);
        httpTask.putParam("isNovice", isNovice);

        httpTask.runTask(CouponBean.class, httpTaskListener);
    }

    // 查询我的优惠券接口
    public static void getCouponInfoList(Context context, String type, int pageSize, int pageNo, OnHttpTaskListener<NotUsedCouponBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.FinanceApi.GETCOUPONINFO, HttpTask.METHOD_POST);
        httpTask.putParam("userId", UserInfoPrefs.getUserId());
        httpTask.putParam("type", type);
        httpTask.putParam("pageSize", String.valueOf(pageSize));
        httpTask.putParam("pageNo", String.valueOf(pageNo));
        httpTask.runTask(NotUsedCouponBean.class, httpTaskListener);
    }

    // 优惠券兑换接口
    public static void exchangeRedPacket(Context context, String hbEntId, OnHttpTaskListener<BaseBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.FinanceApi.EXCHANGEREDPACKET, HttpTask.METHOD_POST);
        httpTask.putParam("userId", UserInfoPrefs.getUserId());
        httpTask.putParam("hbEntId", hbEntId);
        httpTask.runTask(BaseBean.class, httpTaskListener);
    }
}
