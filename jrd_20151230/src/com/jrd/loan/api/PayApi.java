package com.jrd.loan.api;

import android.content.Context;

import com.jrd.loan.bean.AssetsBean;
import com.jrd.loan.bean.HuoqiInvestBean;
import com.jrd.loan.bean.HuoqiTakeoutBean;
import com.jrd.loan.bean.PaymentAccountBean;
import com.jrd.loan.bean.ReturnBean;
import com.jrd.loan.bean.TenderRecordBean;
import com.jrd.loan.bean.TradeBean;
import com.jrd.loan.net.framework.HttpTask;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;

public final class PayApi {

  // 交易记录
  public static void queryBusinessRecords(Context context, String pageSize, String pageNo, OnHttpTaskListener<TradeBean> httpTaskListener) {
    HttpTask httpTask = HttpTask.getHttpTask(context, "appapi/getTransRecord", HttpTask.METHOD_POST);
    httpTask.putParam("userId", UserInfoPrefs.getUserId());
    httpTask.putParam("pageSize", pageSize);
    httpTask.putParam("pageNo", pageNo);
    httpTask.runTask(TradeBean.class, httpTaskListener);
  }

  // 我的资产
  public static void GetAssets(Context context, OnHttpTaskListener<AssetsBean> httpTaskListener) {
    HttpTask httpTask = HttpTask.getHttpTask(context, "appapi/getTotalMoney", HttpTask.METHOD_POST);
    httpTask.putParam("userId", UserInfoPrefs.getUserId());
    httpTask.runTask(AssetsBean.class, httpTaskListener);
  }

  // 回款计划
  public static void GetPlan(Context context, String pageNo, String pageSize, String year, String month, OnHttpTaskListener<ReturnBean> httpTaskListener) {
    HttpTask httpTask = HttpTask.getHttpTask(context, "appapi/getInvestRepaymentPlan", HttpTask.METHOD_POST);
    httpTask.putParam("userId", UserInfoPrefs.getUserId());
    httpTask.putParam("status", "");
    httpTask.putParam("year", year);
    httpTask.putParam("moth", month);
    httpTask.putParam("pageSize", pageSize);
    httpTask.putParam("pageNo", pageNo);
    httpTask.runTask(ReturnBean.class, httpTaskListener);
  }

  // 投标记录
  public static void GetTenderRecord(Context context, String mockId, String pageSize, String pageNo, OnHttpTaskListener<TenderRecordBean> httpTaskListener) {
    HttpTask httpTask = HttpTask.getHttpTask(context, "appapi/getProjectRecordInfo", HttpTask.METHOD_POST);
    httpTask.putParam("pageSize", pageSize);
    httpTask.putParam("pageNo", pageNo);
    httpTask.putParam("mockId", mockId);
    httpTask.runTask(TenderRecordBean.class, httpTaskListener);
  }

  // 付款账号查询2
  public static void paymentAccount(Context context, OnHttpTaskListener<PaymentAccountBean> httpTaskListener) {
    HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PayApi.PAYMENTACCOUNT, HttpTask.METHOD_POST);
    httpTask.putParam("userId", UserInfoPrefs.getUserId());
    httpTask.putParam("platform", 0);
    httpTask.runTask(PaymentAccountBean.class, httpTaskListener);
  }

  // 活期转入(充值)2
  public static void userHuoqiInvest(Context context, String type, Double amount, OnHttpTaskListener<HuoqiInvestBean> httpTaskListener) {
    HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PayApi.USERHUOQIINVEST, HttpTask.METHOD_POST);
    httpTask.setUseJson(true);
    httpTask.putParam("userId", UserInfoPrefs.getUserId());
    httpTask.putParam("type", type);
    httpTask.putParam("amount", amount);
    httpTask.putParam("platform", 0);
    httpTask.runTask(HuoqiInvestBean.class, httpTaskListener);
  }

  // 活期转出(提取)2
  public static void userHuoqiTakeout(Context context, String flag, String type, String cardNo, Double amount, String password, OnHttpTaskListener<HuoqiTakeoutBean> httpTaskListener) {
    HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PayApi.USERHUOQITAKEOUT, HttpTask.METHOD_POST);
    httpTask.setUseJson(true);
    httpTask.putParam("userId", UserInfoPrefs.getUserId());
    httpTask.putParam("flag", flag);
    httpTask.putParam("type", type);
    httpTask.putParam("cardNo", cardNo);
    httpTask.putParam("amount", amount);
    httpTask.putParam("password", password);
    httpTask.runTask(HuoqiTakeoutBean.class, httpTaskListener);
  }

  // 活期转出(提取)2
  public static void userHuoqiInvestResult(Context context, OnHttpTaskListener<HuoqiTakeoutBean> httpTaskListener) {
    HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PayApi.USERHUOQIINVESTRESULT, HttpTask.METHOD_POST);
    httpTask.putParam("userId", UserInfoPrefs.getUserId());
    httpTask.runTask(HuoqiTakeoutBean.class, httpTaskListener);
  }
}
