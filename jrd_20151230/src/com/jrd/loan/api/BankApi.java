package com.jrd.loan.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;

import com.Rky.rongshu.entity.back.InsertOrderAndBindCardBean;
import com.Rky.rongshu.entity.back.PayResultBean;
import com.Rky.rongshu.entity.send.InsertOrderAndBindCardRequestBean;
import com.Rky.rongshu.entity.send.InsertPayOrderRequestBean;
import com.Rky.rongshu.net.HttpsApi;
import com.jrd.loan.api.WebApi.PayApi;
import com.jrd.loan.bean.AccountBean;
import com.jrd.loan.bean.BankListBean;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.BindCardBean;
import com.jrd.loan.bean.CostBean;
import com.jrd.loan.bean.OpenBean;
import com.jrd.loan.bean.RechargeOrderBean;
import com.jrd.loan.net.framework.HttpTask;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;

public class BankApi {

	/**
	 * 已绑定的银行卡列表
	 * 
	 * @param context
	 * @param httpTaskListener
	 */
	public static void MyBindCard(Context context, OnHttpTaskListener<BindCardBean> httpTaskListener) {
		HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.BankApi.GETOWNBANKLIST, HttpTask.METHOD_POST);
		httpTask.putParam("userId", UserInfoPrefs.getUserId());
		httpTask.runTask(BindCardBean.class, httpTaskListener);
	}

	/**
	 * 绑定银行卡接口
	 * 
	 * @param context
	 * @param param
	 * @param httpTaskListener
	 */
	public static void BindCard(Context context, HashMap<String, String> param, OnHttpTaskListener<BaseBean> httpTaskListener) {
		HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.BankApi.ADDBANKCARD, HttpTask.METHOD_POST);
		httpTask.setUseJson(true);
		httpTask.putParam(param);
		httpTask.runTask(BaseBean.class, httpTaskListener);
	}

	/**
	 * 未绑卡情况下提现 绑卡界面
	 */
	public static void WithDrawCard(Context context, String bankCode, String cardNum, OnHttpTaskListener<BaseBean> httpTaskListener) {
		HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.BankApi.WITHDRAW_ADDBANGCARD, HttpTask.METHOD_POST);
		httpTask.setUseJson(true);
		httpTask.putParam("userId", UserInfoPrefs.getUserId());
		httpTask.putParam("bankCode", bankCode);
		httpTask.putParam("cardNumber", cardNum);
		httpTask.runTask(BaseBean.class, httpTaskListener);
	}

	/**
	 * 回款计划
	 */
	public static void GetinvestRepaymentPlan(Context context, String userId, OnHttpTaskListener<BaseBean> httpTaskListener) {
		HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PayApi.GETINVESTREPAYMENTPLAN, HttpTask.METHOD_POST);
		httpTask.putParam("userId", userId);
		httpTask.runTask(BaseBean.class, httpTaskListener);
	}

	/**
	 * 充值接口
	 */
	public static void ToRecharge(Context context, String userId, String money, OnHttpTaskListener<BaseBean> httpTaskListener) {
		HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PayApi.TORECHARGE, HttpTask.METHOD_POST);
		httpTask.setUseJson(true);
		httpTask.putParam("userId", userId);
		httpTask.putParam("money", money);
		httpTask.putParam("platform", 0);
		httpTask.runTask(BaseBean.class, httpTaskListener);
	}

	/**
	 * 提现接口
	 */
	public static void WithdrawCash(Context context, HashMap<String, String> param, OnHttpTaskListener<BaseBean> httpTaskListener) {
		HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PayApi.WITHDRAWCASH, HttpTask.METHOD_POST);
		httpTask.setUseJson(true);
		httpTask.putParam("platform", 0);
		httpTask.putParam(param);

		httpTask.runTask(BaseBean.class, httpTaskListener);
	}

	/**
	 * 提现费用计算接口
	 */
	public static void WithdrawCost(Context context, String userId, String money, OnHttpTaskListener<CostBean> httpTaskListener) {
		HttpTask httpTask = HttpTask.getHttpTask(context, PayApi.PRESENTCOST, HttpTask.METHOD_POST);
		httpTask.putParam("userId", userId);
		httpTask.putParam("money", money);
		httpTask.runTask(CostBean.class, httpTaskListener);
	}

	/**
	 * 充值获取订单信息3
	 */
	public static void obtainRechargeOrderInfo(Context context, double amount, String bankCode, String cardNumber, OnHttpTaskListener<RechargeOrderBean> httpTaskListener) {
		HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.PayApi.OBTAIN_RECHARGE_ORDER, HttpTask.METHOD_POST);
		httpTask.putParam("userId", UserInfoPrefs.getUserId());
		httpTask.putParam("amount", amount);
		httpTask.putParam("bankCode", bankCode);
		httpTask.putParam("cardNumber", cardNumber);

		httpTask.runTask(RechargeOrderBean.class, httpTaskListener);
	}

	/**
	 * 获取支付订单号
	 * 
	 * @param context
	 * @param userOrderId
	 * @param constid
	 * @param productId
	 * @param userorderid
	 * @param orderMoney
	 * @param cardNum
	 * @param bankName
	 *            银行总行名称 (如:中国银行)
	 * @param idCardNum
	 *            身份证号
	 * @param bankUserName
	 *            开户名
	 * @param mobileNum
	 *            手机号
	 * @param bankCode
	 *            银行code
	 * @param registerTime
	 *            注册时间
	 * @param httpTaskListener
	 */
	public static void obtainBusinessOrderInfo1(Context context, String userOrderId, String constid, String productId, String userorderid, String orderMoney, String cardNum, String bankName, String idCardNum, String bankUserName, String mobileNum, String bankCode, String registerTime, com.Rky.rongshu.net.OnHttpTaskListener<InsertOrderAndBindCardBean> httpTaskListener) {
		InsertOrderAndBindCardRequestBean entity = new InsertOrderAndBindCardRequestBean();

		entity.userid = userOrderId;
		entity.constid = constid;
		entity.productid = productId;
		entity.ordertypeid = "BX1";
		entity.ordertime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		entity.userorderid = userorderid;
		entity.amount = orderMoney;
		entity.accountnumber = cardNum;
		entity.accounttypeid = "00";
		entity.bankheadname = bankName;
		entity.currency = "CNY";
		entity.accountpurpose = "2";
		entity.accountproperty = "2";
		entity.certificatetype = "0";
		entity.certificatenumber = idCardNum;
		entity.accountname = bankUserName;
		entity.bankhead = bankCode;
		entity.registerTime = registerTime;
		entity.mobile = mobileNum;

		HttpsApi.InsertOrderAndbindCardAPI(context, entity, httpTaskListener);
	}

	public static void obtainBusinessOrderInfo1(Context context, InsertOrderAndBindCardRequestBean entity, com.Rky.rongshu.net.OnHttpTaskListener<InsertOrderAndBindCardBean> httpTaskListener) {
		HttpsApi.InsertOrderAndbindCardAPI(context, entity, httpTaskListener);
	}

	/**
	 * 融数充值sdk
	 * 
	 * @param context
	 * @param merchantNo
	 * @param payerAccountName
	 * @param userNo
	 * @param orderNo
	 * @param orderAmount
	 * @param bankCode
	 * @param expend
	 * @param payerAccountNo
	 * @param lianLianPayTaskListener
	 */
	public static void rechargeMoney(Context context, String merchantNo, String userOrderId, String payerAccountName, String userNo, String orderNo, String orderAmount, String bankCode, String expend, String payerAccountNo, String registerTime, String mobileNum, com.Rky.rongshu.net.OnLianLianPayTaskListener<PayResultBean> lianLianPayTaskListener) {
		InsertPayOrderRequestBean entity = new InsertPayOrderRequestBean();

		entity.merchantNo = merchantNo;// 商户号（M000005）
		entity.userId = userOrderId;
		entity.payerAccountName = payerAccountName;// 绑卡人名称
		entity.userNo = userNo;// 身份证号

		entity.orderNo = orderNo;// 业务订单号
		entity.orderAmount = orderAmount; // 支付金额（分）
		entity.amtType = "CNY"; // 币种 CNY
		entity.payType = "24"; // 24是连连支付
		entity.bankCode = bankCode; // 银行code
		entity.orderTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		entity.expend = expend; // 第三方业务订单号
		entity.payerAccountNo = payerAccountNo; // 付款账户
		entity.registerTime = registerTime;
		entity.mobile = mobileNum;

		System.out.println("=================entity==================" + entity);

		HttpsApi.LianLianPayOrderAPI((Activity) context, entity, lianLianPayTaskListener);
	}

	/**
	 * 交易记录
	 * 
	 * @param context
	 */
	public static void GetTransRecord(Context context) {
	}

	/**
	 * 获取银行卡所在地
	 * 
	 * @param mContext
	 * @param httpTaskListener
	 */
	public static void GetBankAreaList(Context mContext, String cityCode, String bankCode, boolean flag, OnHttpTaskListener<OpenBean> httpTaskListener) {
		HttpTask httpTask = HttpTask.getHttpTask(mContext, WebApi.BankApi.GETBANKAREAINFO, HttpTask.METHOD_POST);
		httpTask.putParam("bankCode", bankCode);
		httpTask.putParam("cityCode", cityCode);
		httpTask.putParam("queryBank", flag);
		httpTask.runTask(OpenBean.class, httpTaskListener);
	}

	/**
	 * 我的账户信息接口
	 */
	public static void GetMyAccount(Context context, OnHttpTaskListener<AccountBean> httpTaskListener) {
		HttpTask httpTask = HttpTask.getHttpTask(context, WebApi.MyAccountInfoApi.GETACCOUNTINFO, HttpTask.METHOD_POST);
		httpTask.putParam("userId", UserInfoPrefs.getUserId());
		httpTask.runTask(AccountBean.class, httpTaskListener);
	}

	/**
	 * 获取银行卡列表
	 * 
	 * @param context
	 * @param onHttpTaskListener
	 */
	public static void getBankList(Context context, OnHttpTaskListener<BankListBean> onHttpTaskListener) {
		HttpTask httpTask = HttpTask.getHttpTask(context, "appapi/bankQuery");
		httpTask.runTask(BankListBean.class, onHttpTaskListener);
	}

	/**
	 * 设置绑定银行卡的所在地、支行信息
	 * 
	 * @param context
	 * @param onHttpTaskListener
	 */
	public static void setBankInfo(Context context, String bankCode, String cardNumber, String cityCode, String cityName, String bankBranchCode, String bankBranchName, String provinceCode, String provinceName, OnHttpTaskListener<BaseBean> onHttpTaskListener) {
		HttpTask httpTask = HttpTask.getHttpTask(context, "appapi/setBankInfo");
		httpTask.putParam("userId", UserInfoPrefs.getUserId());
		httpTask.putParam("bankCode", bankCode);
		httpTask.putParam("cardNumber", cardNumber);
		httpTask.putParam("cityCode", cityCode);
		httpTask.putParam("cityName", cityName);
		httpTask.putParam("bankBranchCode", bankBranchCode);
		httpTask.putParam("bankBranchName", bankBranchName);
		httpTask.putParam("provinceCode", provinceCode);
		httpTask.putParam("provinceName", provinceName);
		httpTask.runTask(BaseBean.class, onHttpTaskListener);
	}
}
