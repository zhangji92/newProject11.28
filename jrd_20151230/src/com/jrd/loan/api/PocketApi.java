package com.jrd.loan.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;

import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.BillInfoBean;
import com.jrd.loan.bean.MyPocketBean;
import com.jrd.loan.bean.PocketBean;
import com.jrd.loan.bean.PocketDetailsBean;
import com.jrd.loan.bean.SevenBean;
import com.jrd.loan.bean.TenderRecordBean;
import com.jrd.loan.net.framework.HttpTask;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;

public class PocketApi {

    private final static String CURRENT_POCKET_FATE_TODAY = "appapi/huoqiProjectInfo";// 精选推荐
    private final static String MY_CURRENT_POCKET_INFO = "appapi/getUserHuoQiInfo";// 我的活期宝信息
    private final static String SEVEN_YEAR_RETE = "appapi/huoqiLast7Profit";// 7日年化利率
    public final static String RECHARGE = "appapi/userRechargeHtml";// 充值

    public final static String POCKET_AGREE = "appapi/huoqiAgreementInfoHtml";// 活期投资协议
    public final static String WITHFRAW_DETAILS = "appapi/withdrawAgreementHtml";// 提现详细介绍

    public final static String BINDCARD_AGREE = "appapi/bankBindAgreementHtml";// 银行卡绑定以及解绑协议

    /**
     * 实名认证以及绑卡
     *
     * @param context
     * @param userName
     * @param idNumber
     * @param cardNumber
     * @param httpTaskListener
     */
    public static void BindCard(Context context, String userName, String idNumber, String bankCode, String cardNumber, OnHttpTaskListener<BaseBean> httpTaskListener) {
        try {
            HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PocketApi.BINDCARDANDREALNAME, HttpTask.METHOD_POST);
            httpTask.setUseJson(true);
            httpTask.putParam("userId", UserInfoPrefs.getUserId());
            httpTask.putParam("userName", URLEncoder.encode(userName, "UTF-8"));
            httpTask.putParam("idNumber", idNumber);
            httpTask.putParam("cardNumber", cardNumber);
            httpTask.putParam("bankCode", bankCode);
            httpTask.runTask(BaseBean.class, httpTaskListener);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 绑定银行卡
     *
     * @param context
     * @param cardNumber
     * @param httpTaskListener
     */
    public static void BindCard(Context context, String bankCode, String cardNumber, OnHttpTaskListener<BaseBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PocketApi.BINDCARD, HttpTask.METHOD_POST);
        httpTask.setUseJson(true);
        httpTask.putParam("userId", UserInfoPrefs.getUserId());
        httpTask.putParam("cardNumber", cardNumber);
        httpTask.putParam("bankCode", bankCode);
        httpTask.runTask(BaseBean.class, httpTaskListener);
    }

    /**
     * 转入记录
     *
     * @param context
     * @param flag
     * @param yearmonth
     * @param pageNo
     * @param pageSize
     * @param httpTaskListener
     */
    public static void IntoRecord(Context context, String flag, String yearmonth, int pageNo, int pageSize, OnHttpTaskListener<BillInfoBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PocketApi.INTORECORD, HttpTask.METHOD_POST);
        httpTask.putParam("userId", UserInfoPrefs.getUserId());
        httpTask.putParam("flag", flag);
        if (flag.equals("1")) {
            httpTask.putParam("yearmonth", yearmonth);
            httpTask.putParam("pageNo", pageNo);
            httpTask.putParam("pageSize", pageSize);
        }
        httpTask.runTask(BillInfoBean.class, httpTaskListener);
    }

    /**
     * 转出记录
     *
     * @param context
     * @param flag
     * @param yearmonth
     * @param pageNo
     * @param pageSize
     * @param httpTaskListener
     */
    public static void OutoRecord(Context context, String flag, String yearmonth, int pageNo, int pageSize, OnHttpTaskListener<BillInfoBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PocketApi.OUTORECORD, HttpTask.METHOD_POST);
        httpTask.putParam("userId", UserInfoPrefs.getUserId());
        httpTask.putParam("flag", flag);
        if (flag.equals("1")) {
            httpTask.putParam("yearmonth", yearmonth);
            httpTask.putParam("pageNo", pageNo);
            httpTask.putParam("pageSize", pageSize);
        }
        httpTask.runTask(BillInfoBean.class, httpTaskListener);
    }

    /**
     * 收益明细
     *
     * @param context
     * @param flag
     * @param yearmonth
     * @param pageNo
     * @param pageSize
     * @param httpTaskListener
     */
    public static void IncomeRecord(Context context, String flag, String yearmonth, int pageNo, int pageSize, OnHttpTaskListener<BillInfoBean> httpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PocketApi.INCOMEDETAILS, HttpTask.METHOD_POST);
        httpTask.putParam("userId", UserInfoPrefs.getUserId());
        httpTask.putParam("flag", flag);
        if (flag.equals("1")) {
            httpTask.putParam("yearmonth", yearmonth);
            httpTask.putParam("pageNo", pageNo);
            httpTask.putParam("pageSize", pageSize);
        }
        httpTask.runTask(BillInfoBean.class, httpTaskListener);
    }

    /**
     * 转入记录详情
     *
     * @param context
     * @param id
     * @param onHttpTaskListener
     */
    public static void IntoDetails(Context context, String id, OnHttpTaskListener<BillInfoBean> onHttpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PocketApi.INTODETAILS, HttpTask.METHOD_POST);
        httpTask.putParam("userId", UserInfoPrefs.getUserId());
        httpTask.putParam("id", id);
        httpTask.runTask(BillInfoBean.class, onHttpTaskListener);
    }

    /**
     * 转出记录详情
     *
     * @param context
     * @param id
     * @param onHttpTaskListener
     */
    public static void OutoDetails(Context context, String id, String kind, OnHttpTaskListener<BillInfoBean> onHttpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PocketApi.OUTODETAILS, HttpTask.METHOD_POST);
        httpTask.putParam("userId", UserInfoPrefs.getUserId());
        httpTask.putParam("id", id);
        httpTask.putParam("kind", kind);
        httpTask.runTask(BillInfoBean.class, onHttpTaskListener);
    }

    // 获取活期口袋今日利率
    public static void obtainCurrentPocketFateToday(Context context, OnHttpTaskListener<PocketBean> onHttpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, CURRENT_POCKET_FATE_TODAY, HttpTask.METHOD_POST);
        httpTask.runTask(PocketBean.class, onHttpTaskListener);
    }

    /**
     * 活期标的详情接口
     *
     * @param context
     * @param flag               0是产品介绍 1是风控信息
     * @param onHttpTaskListener
     */
    public static void PocketDetails(Context context, String flag, OnHttpTaskListener<PocketDetailsBean> onHttpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PocketApi.POCIKETDETAILS, HttpTask.METHOD_POST);
        httpTask.putParam("flag", flag);
        httpTask.runTask(PocketDetailsBean.class, onHttpTaskListener);
    }

    /**
     * 投资记录
     *
     * @param context
     * @param projectId
     * @param pageNo
     * @param pageSize
     * @param url
     * @param onHttpTaskListener
     */
    public static void PocketInvestRecord(Context context, String mockId, String pageNo, String pageSize, String url, OnHttpTaskListener<TenderRecordBean> onHttpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, url, HttpTask.METHOD_POST);
        httpTask.putParam("mockId", mockId);
        httpTask.putParam("pageSize", pageSize);
        httpTask.putParam("pageNo", pageNo);
        httpTask.runTask(TenderRecordBean.class, onHttpTaskListener);
    }

    /**
     * 我的活期宝信息
     *
     * @param context
     * @param onHttpTaskListener
     */
    public static void MyPocketInfo(Context context, OnHttpTaskListener<MyPocketBean> onHttpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, MY_CURRENT_POCKET_INFO, HttpTask.METHOD_POST);
        httpTask.putParam("userId", UserInfoPrefs.getUserId());
        httpTask.runTask(MyPocketBean.class, onHttpTaskListener);
        ;
    }

    /**
     * 获取七日年化利率
     *
     * @param context
     * @param onHttpTaskListener
     */
    public static void SevenYearIncome(Context context, OnHttpTaskListener<SevenBean> onHttpTaskListener) {
        HttpTask httpTask = HttpTask.getHttpTask(context, SEVEN_YEAR_RETE, HttpTask.METHOD_POST);
        httpTask.putParam("userId", UserInfoPrefs.getUserId());
        httpTask.runTask(SevenBean.class, onHttpTaskListener);
    }
}
