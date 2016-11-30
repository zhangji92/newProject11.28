package com.jrd.loan.current.pocket.rehcarge;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.Rky.rongshu.entity.back.InsertOrderAndBindCardBean;
import com.Rky.rongshu.entity.back.PayResultBean;
import com.Rky.rongshu.entity.send.InsertOrderAndBindCardRequestBean;
import com.jrd.loan.R;
import com.jrd.loan.adapter.SelectCardAdapter;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.api.StatisticsApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BankCardBean;
import com.jrd.loan.bean.BindCardBean;
import com.jrd.loan.bean.PersonalInfo;
import com.jrd.loan.bean.RechargeOrderBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.BankLogoUtil;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.MoneyFormatUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

/**
 * 再次充值界面
 */
public class RechargeAgainActivity extends BaseActivity {

	private Context mContext;
	private EditText amountEdit;
	private ImageView bankIconImg;
	private ImageView rechargeArrowImg;
	private TextView bankNameTV;
	private TextView bankNumTV;
	private TextView hintTV;
	private Button rechargeBtn;

	private List<BankCardBean> cardList;
	private SelectCardAdapter cardAdapter;

	private String cardNum;
	private String cardName;
	private String cardCode;
	private String oneTime;
	private String oneDay;
	private String oneMonth;

	private String userCustId;// 用户订单号
	private String constid;// 机构码
	private String productId;// 产品号
	private String userorderid;// 用户订单号
	private String orderMoney;// 订单金额
	private String cardNumber;// 银行卡号

	private String orderTime;// 订单提交时间
	private String registerTime;//
	private String mobileNo;//
	private WindowView windowView;

	@Override
	protected int loadWindowLayout() {
		return R.layout.loan_activity_current_recharge_again;
	}

	@Override
	protected void initTitleBar() {
		windowView = (WindowView) findViewById(R.id.windowView);
		windowView.setVisibility(View.INVISIBLE);
		windowView.setTitle("充值");
		windowView.setLeftButtonClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void initViews() {

		mContext = RechargeAgainActivity.this;
		amountEdit = (EditText) findViewById(R.id.loan_recharge_amountEdit);
		bankIconImg = (ImageView) findViewById(R.id.loan_recharge_bankiconImg);
		rechargeArrowImg = (ImageView) findViewById(R.id.loan_recharge_arrowImg);
		rechargeArrowImg.setVisibility(View.GONE);
		bankNameTV = (TextView) findViewById(R.id.loan_recharge_bankNameTV);
		bankNumTV = (TextView) findViewById(R.id.loan_recharge_bankTailNumTV);
		hintTV = (TextView) findViewById(R.id.loan_recharge_hintTV);

		// 充值
		rechargeBtn = (Button) findViewById(R.id.loan_recharge_nextBtn);
		rechargeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(amountEdit.getText().toString())) {
					ToastUtil.showShort(RechargeAgainActivity.this, "请输入充值金额");

					return;
				} else if (amountEdit.getText().toString().equals("0")) {
					ToastUtil.showShort(RechargeAgainActivity.this, "充值金额不能为0");

					return;
				}

				obtainRechargeOrderInfo();
			}
		});

		RequestData();

		setNoNetworkClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RequestData();
			}
		});
	}

	/**
	 * 获取银行卡数据
	 */
	private void RequestData() {
		BankApi.MyBindCard(mContext, new OnHttpTaskListener<BindCardBean>() {

			@Override
			public void onTaskError(int resposeCode) {
				DismissDialog();
			}

			@Override
			public void onStart() {
				ShowDrawDialog(mContext);
			}

			@Override
			public void onFinishTask(BindCardBean bean) {
				DismissDialog();

				if (bean.getResultCode() == 0) {
					windowView.setVisibility(View.VISIBLE);
					if (bean.getUsrCardInfoList() != null) {
						if (bean.getUsrCardInfoList().size() > 0) {
							cardList = bean.getUsrCardInfoList();

							cardName = cardList.get(0).getBankName();
							cardNum = cardList.get(0).getCardNumber();
							cardCode = cardList.get(0).getBankCode();

							oneTime = cardList.get(0).getOneTime();
							oneDay = cardList.get(0).getOneDay();
							oneMonth = cardList.get(0).getOneMonth();

							bankNameTV.setText(cardName);
							String cardNumber = cardNum.substring(cardNum.length() - 4, cardNum.length());
							bankNumTV.setText("**** **** **** " + cardNumber);

							// 温馨提示
							hintTV.setText(getString(R.string.loan_recharge_again_kindly_remind, MoneyFormatUtil.getMoneyTwo(oneTime), oneDay.equals("0.00") ? getString(R.string.loan_no_district) : "限额" + MoneyFormatUtil.getMoneyTwo(oneDay) + "元", oneMonth.equals("0.00") ? getString(R.string.loan_no_district) : "限额" + MoneyFormatUtil.getMoneyTwo(oneMonth) + "元"));

							// 设置银行logo
							BankLogoUtil.setBankLog(bankIconImg, cardList.get(0).getBankCode());
						} else {
							startActivity(new Intent(mContext, RechargeActivity.class));
							finish();
						}
					}
				} else {
					ToastUtil.showShort(mContext, bean.getResultMsg());
				}
			}
		});
	}

	/**
	 * 充值获取订单信息3
	 */
	private void obtainRechargeOrderInfo() {
		Double amount = Double.parseDouble(amountEdit.getText().toString());

		BankApi.obtainRechargeOrderInfo(mContext, amount, cardCode, cardNum, new OnHttpTaskListener<RechargeOrderBean>() {

			@Override
			public void onTaskError(int resposeCode) {
				DismissDialog();
			}

			@Override
			public void onStart() {
				ShowProDialog(mContext);

			}

			@Override
			public void onFinishTask(RechargeOrderBean bean) {
				DismissDialog();
				if (bean.getResultCode() == 0) {
					userCustId = bean.getUserCustId();
					constid = bean.getMerchantNo();// 机构码
					productId = bean.getProductNo();// 产品号
					userorderid = bean.getOrderNo();// "20151230113623123"; //
													// bean.getOrderNo();//
													// 订单号
					orderMoney = bean.getOrderAmount();// 订单金额
					cardNum = bean.getCardNumber();// 银行卡号
					orderTime = bean.getOrderTime();// 注册时间
					registerTime = bean.getRegisterTime();// 注册时间
					mobileNo = bean.getMobileNo();// 手机号

					obtainBusinessOrderInfo();
				} else {
					ToastUtil.showShort(mContext, bean.getResultMsg());
				}
			}
		});
	}

	/**
	 * 获取支付订单号
	 */
	private void obtainBusinessOrderInfo() {
		InsertOrderAndBindCardRequestBean entity = new InsertOrderAndBindCardRequestBean();

		entity.userid = userCustId;
		entity.constid = constid;
		entity.productid = productId;
		entity.ordertypeid = "BX1";
		entity.ordertime = orderTime;
		entity.userorderid = userorderid;
		entity.amount = orderMoney;
		entity.accountnumber = cardNum;
		entity.accounttypeid = "00";
		entity.bankheadname = bankNameTV.getText().toString();
		entity.currency = "CNY";
		entity.accountpurpose = "2";
		entity.accountproperty = "2";
		entity.certificatetype = "0";
		entity.certificatenumber = UserInfoPrefs.loadLastLoginUserProfile().idNumberInfo;
		entity.accountname = UserInfoPrefs.loadLastLoginUserProfile().userName;
		entity.bankhead = cardCode;
		entity.registerTime = registerTime;
		entity.mobile = mobileNo;

		LogUtil.d("HttpRequest", "--------------- runTask--> " + entity);

		BankApi.obtainBusinessOrderInfo1(mContext, entity, new com.Rky.rongshu.net.OnHttpTaskListener<InsertOrderAndBindCardBean>() {

			@Override
			public void onTaskError(Throwable bean) {
				DismissDialog();
			}

			@Override
			public void onPreTask() {
				ShowProDialog(mContext);
			}

			@Override
			public void onFinishTask(InsertOrderAndBindCardBean bean) {

				LogUtil.d("HttpRequest", "--------------- onFinishTask--> " + bean);

				DismissDialog();
				if (bean.getData().wheatfield_order_save_withcard_response.getIs_success().equals("true")) {
					rechargeMoney(bean);
				} else if (bean.getData().wheatfield_order_save_withcard_response.getIs_success().equals("false")) {
					ToastUtil.showShort(mContext, bean.getData().wheatfield_order_save_withcard_response.getMsg());
				}
			}
		});
	}

	/**
	 * 融数充值sdk
	 */
	private void rechargeMoney(InsertOrderAndBindCardBean bean) {
		BankApi.rechargeMoney(mContext, constid, userCustId, UserInfoPrefs.loadLastLoginUserProfile().userName, UserInfoPrefs.loadLastLoginUserProfile().idNumberInfo, bean.getData().wheatfield_order_save_withcard_response.getOrderid(), orderMoney, cardCode, userorderid, cardNum, registerTime, mobileNo, new com.Rky.rongshu.net.OnLianLianPayTaskListener<PayResultBean>() {

			@Override
			public void onTaskError(Throwable bean) {
			}

			@Override
			public void onPreTask() {
			}

			@Override
			public void onPayFinishTask(PayResultBean bean) {
				LogUtil.d("HttpRequest", "--------------- onFinishTask--> " + bean);
				if (bean != null && bean.ret_code.equals("0000")) {
					PersonalInfo user = new PersonalInfo();
					PersonalInfo info = UserInfoPrefs.loadLastLoginUserProfile();
					user.idNumberInfo = info.getIdNumberInfo();

					if (info.getIdNumberInfo().equals("")) {
						user.idNumberFlag = "0";
					} else {
						user.idNumberFlag = "1";
					}
					user.userName = info.getUserName();
					user.boundCardFlag = info.getBoundCardFlag();
					user.quickCardFlag = "1";
					user.mobileNumber = info.getMobileNumber();
					user.transPwdInfo = info.getTransPwdInfo();
					user.transPwdFlag = info.getTransPwdFlag();
					UserInfoPrefs.saveUserProfileInfo(user);
					AppInfoPrefs.setRecharge(true);
					
					// 用户行为统计接口
					StatisticsApi.getUserBehavior(RechargeAgainActivity.this, Const.EventName.RECHARGE, Const.EventId.RECHARGE, null, null, amountEdit.getText().toString());
					
					Intent intent = new Intent(mContext, RechargeSuccessActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("userorderid", userorderid);
					startActivity(intent);
					finish();
					

				} else {
					Intent intent = new Intent(mContext, RechargeFailureActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("ret_code", bean.getRet_msg());
					startActivity(intent);
					finish();
				}
			}

			@Override
			public void onDismissPreTask() {
			}
		});
	}
}
