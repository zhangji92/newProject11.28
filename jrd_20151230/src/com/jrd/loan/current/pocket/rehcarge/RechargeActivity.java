package com.jrd.loan.current.pocket.rehcarge;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.Rky.rongshu.entity.back.InsertOrderAndBindCardBean;
import com.Rky.rongshu.entity.back.PayResultBean;
import com.Rky.rongshu.entity.send.InsertOrderAndBindCardRequestBean;
import com.jrd.loan.R;
import com.jrd.loan.adapter.BankCardItemAdapter;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.api.StatisticsApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BankListBean;
import com.jrd.loan.bean.PersonalInfo;
import com.jrd.loan.bean.RechargeOrderBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.BankLogoUtil;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.DialogUtils.OnButtonEventListener;
import com.jrd.loan.util.FormatUtils;
import com.jrd.loan.util.KeyBoardUtil;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.MoneyFormatUtil;
import com.jrd.loan.util.NetUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

/**
 * 新用户充值绑卡界面
 * 
 * @author Javen
 */
public class RechargeActivity extends BaseActivity implements OnClickListener {

	private Context mContext;
	private EditText amountEdit;
	private EditText cardNumEdit;
	private TextView hintTV;
	private PopupWindow paymentPopWin;
	private LinearLayout selectLayout;
	private LinearLayout bankLayout;
	private TextView bankNameTV;
	private TextView selectTV;
	private ImageView bankIcon;
	private List<BankListBean.RecordsEntity> mList;
	private boolean isBindCard = false;// 是否绑卡
	private String bankCode;
	// private String cardNumber; // 银行卡号

	private String userCustId;// 用户订单号
	private String constid;// 机构码
	private String productId;// 产品号
	private String userorderid;// 用户订单号
	private String orderMoney;// 订单金额
	private String cardNum;// 银行卡号
	private String orderTime;// 订单提交时间
	private String registerTime;//
	private String mobileNo;//

	private String cardName;

	private int getIntentCode; // 从我的银行卡界面跳转过来的

	@Override
	protected int loadWindowLayout() {
		return R.layout.loan_activity_current_recharge;
	}

	@Override
	protected void initTitleBar() {
		getIntentCode = getIntent().getIntExtra("recharge", 0);

		WindowView windowView = (WindowView) findViewById(R.id.windowView);
		if (getIntentCode != 0) {
			windowView.setTitle("绑卡");
		} else {
			windowView.setTitle("充值");
		}
		windowView.setLeftButtonClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void initViews() {

		mContext = RechargeActivity.this;

		String[] getBankInfo = getIntent().getStringArrayExtra("cardInfo");

		selectLayout = (LinearLayout) findViewById(R.id.loan_bindcard_selectLayout);
		selectLayout.setOnClickListener(this);

		selectTV = (TextView) findViewById(R.id.loan_bindcard_selectTV);
		selectTV.setVisibility(View.VISIBLE);

		bankLayout = (LinearLayout) findViewById(R.id.loan_bindcard_bankLayout);
		bankLayout.setVisibility(View.GONE);

		cardNumEdit = (EditText) findViewById(R.id.loan_recharge_cardnumEdit);
		FormatUtils.CardEdit(cardNumEdit);

		bankNameTV = (TextView) findViewById(R.id.loan_bindcard_bankName);

		bankIcon = (ImageView) findViewById(R.id.loan_bindcard_bankImage);
		TextView nameTV = (TextView) findViewById(R.id.loan_recharge_nameTV);
		nameTV.setText(UserInfoPrefs.loadLastLoginUserProfile().userName);

		// 温馨提示
		hintTV = (TextView) findViewById(R.id.loan_recharge_hintTV);
		hintTV.setText(R.string.loan_add_fast_card_kindly_remind);

		amountEdit = (EditText) findViewById(R.id.loan_recharge_amountEdit);
		FormatUtils.setPricePointTwo(amountEdit);

		Button nextBtn = (Button) findViewById(R.id.loan_recharge_nextBtn);
		nextBtn.setOnClickListener(this);

		if (getBankInfo != null) {// set银行卡信息

			cardNumEdit.setEnabled(false);
			selectLayout.setClickable(false);

			selectTV.setVisibility(View.GONE);
			bankLayout.setVisibility(View.VISIBLE);
			bankNameTV.setText(getBankInfo[0]);
			cardNumEdit.setText(getBankInfo[1]);
			bankCode = getBankInfo[3];
			hintTV.setText(getBankInfo[4]);
			cardName = getBankInfo[5];

			BankLogoUtil.setBankLog(bankIcon, bankCode);
			isBindCard = true;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.loan_recharge_nextBtn:
				if (NetUtil.hasMobileNet()) {
					ToRecharge();
				} else {
					ToastUtil.showShort(RechargeActivity.this, "网络异常，请检查网络");
				}
				break;
			case R.id.loan_bindcard_selectLayout:
				KeyBoardUtil.closeKeybord(selectTV, mContext);
				getBankListData();
				break;
		}
	}

	// 充值
	private void ToRecharge() {
		if (amountEdit.getText().toString().isEmpty()) {
			ToastUtil.showShort(RechargeActivity.this, R.string.loan_please_input_money);
			return;
		} else if (Double.valueOf(amountEdit.getText().toString()) <= 0) {
			ToastUtil.showShort(RechargeActivity.this, R.string.loan_recharge_hint);
			return;
		} else if (!isBindCard) {
			ToastUtil.showShort(mContext, R.string.loan_warning_empty_select_bankcard);
			return;
		} else if (cardNumEdit.getText().toString().isEmpty()) {
			ToastUtil.showShort(mContext, R.string.loan_warning_input_card_number_text);
			return;
		}

		DialogUtils.showBankCardInfoDialog(mContext, cardName, cardNumEdit.getText().toString(), new OnButtonEventListener() {

			@Override
			public void onConfirm() {
				// 充值
				obtainRechargeOrderInfo();
			}

			@Override
			public void onCancel() {

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			finish();
		}
	}

	private void getBankListData() {

		BankApi.getBankList(mContext, new OnHttpTaskListener<BankListBean>() {

			@Override
			public void onStart() {
				ShowProDialog(mContext);
			}

			@Override
			public void onTaskError(int resposeCode) {
				DismissDialog();
			}

			@Override
			public void onFinishTask(BankListBean bean) {
				DismissDialog();
				if (bean != null && bean.getResultCode() == 0) {
					if (bean.getRecords() != null && bean.getRecords().size() > 0) {

						mList = bean.getRecords();

						initPopupWindow();

					}
				}

			}
		});
	}

	/**
	 * 充值获取订单信息3
	 */
	private void obtainRechargeOrderInfo() {
		Double amount = Double.parseDouble(amountEdit.getText().toString());

		String cardNumberT = cardNumEdit.getText().toString();

		BankApi.obtainRechargeOrderInfo(mContext, amount, bankCode, cardNumberT.replace(" ", ""), new OnHttpTaskListener<RechargeOrderBean>() {

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
					userorderid = bean.getOrderNo();// 订单号
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
	@SuppressLint("SimpleDateFormat")
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
		entity.bankheadname = cardName;
		entity.currency = "CNY";
		entity.accountpurpose = "2";
		entity.accountproperty = "2";
		entity.certificatetype = "0";
		entity.certificatenumber = UserInfoPrefs.loadLastLoginUserProfile().idNumberInfo;
		entity.accountname = UserInfoPrefs.loadLastLoginUserProfile().userName;
		entity.bankhead = bankCode;
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

				if (bean.getData().wheatfield_order_save_withcard_response != null && bean.getData().wheatfield_order_save_withcard_response.getIs_success().equals("true")) {
					rechargeMoney(bean);
				} else {
					ToastUtil.showShort(mContext, bean.getData().msg);
				}
				DismissDialog();
			}
		});
	}

	/**
	 * 融数充值sdk
	 */
	private void rechargeMoney(InsertOrderAndBindCardBean bean) {
		BankApi.rechargeMoney(mContext, constid, userCustId, UserInfoPrefs.loadLastLoginUserProfile().userName, UserInfoPrefs.loadLastLoginUserProfile().idNumberInfo, bean.getData().wheatfield_order_save_withcard_response.getOrderid(), orderMoney, bankCode, userorderid, cardNum, registerTime, mobileNo, new com.Rky.rongshu.net.OnLianLianPayTaskListener<PayResultBean>() {

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
			        StatisticsApi.getUserBehavior(RechargeActivity.this, Const.EventName.RECHARGE, Const.EventId.RECHARGE, null, null, amountEdit.getText().toString());

					Intent intent = new Intent(mContext, RechargeSuccessActivity.class);
					if (getIntentCode != 0) {
						intent.putExtra("recharge", getIntentCode);
						// 用户行为统计接口
						StatisticsApi.getUserBehavior(RechargeActivity.this, Const.EventName.TIECARD, Const.EventId.TIECARD, null, null, amountEdit.getText().toString());
					}
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("userorderid", userorderid);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(mContext, RechargeFailureActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("ret_code", bean.getRet_msg());
					if (getIntentCode != 0) {
						intent.putExtra("recharge", getIntentCode);
					}
					startActivity(intent);
					finish();
				}
			}

			@Override
			public void onDismissPreTask() {
			}
		});
	}

	/**
	 * 初始化选择银行卡页面
	 */
	@SuppressLint("InflateParams")
	private void initPopupWindow() {
		View PopupView = this.getLayoutInflater().inflate(R.layout.loan_payment_dialog, null);

		TextView title = (TextView) PopupView.findViewById(R.id.loan_payment_title);
		title.setText("请选择银行卡（仅限储蓄卡）");

		ListView mListView = (ListView) PopupView.findViewById(R.id.loan_payment_ListView);

		paymentPopWin = new PopupWindow(PopupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);

		// 设置SelectPicPopupWindow弹出窗体的背景
		paymentPopWin.setBackgroundDrawable(dw);

		// 设置SelectPicPopupWindow弹出窗体的宽
		paymentPopWin.setWidth(LayoutParams.MATCH_PARENT);

		// 设置SelectPicPopupWindow弹出窗体的高
		paymentPopWin.setHeight(LayoutParams.WRAP_CONTENT);

		// 设置SelectPicPopupWindow弹出窗体动画效果
		paymentPopWin.setAnimationStyle(R.style.AnimBottom);

		// 设置点击窗口外边窗口消失
		paymentPopWin.setOutsideTouchable(true);

		// 设置此参数活的焦点，否在无法点击
		paymentPopWin.setFocusable(true);

		// mListView.setOnItemClickListener(mListViewItem);

		paymentPopWin.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			@Override
			public void onDismiss() {
				backgroundAlpha(1f);
			}
		});

		BankCardItemAdapter adapter = new BankCardItemAdapter(mList, mContext);

		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				selectTV.setVisibility(View.GONE);
				bankLayout.setVisibility(View.VISIBLE);

				bankCode = mList.get(position).getBankCode();
				cardName = mList.get(position).getBankName();
				bankNameTV.setText(mList.get(position).getOrderName());

				BankLogoUtil.setBankLog(bankIcon, mList.get(position).getBankCode());

				String oneTime = mList.get(position).getOneTime();
				String oneDay = mList.get(position).getOneDay();
				String oneMonth = mList.get(position).getOneMonth();

				// 温馨提示
				hintTV.setText(getString(R.string.loan_recharge_again_kindly_remind, MoneyFormatUtil.getMoneyTwo(oneTime), oneDay.equals("0.00") ? getString(R.string.loan_no_district) : "限额" + MoneyFormatUtil.getMoneyTwo(oneDay) + "元", oneMonth.equals("0.00") ? getString(R.string.loan_no_district) : "限额" + MoneyFormatUtil.getMoneyTwo(oneMonth) + "元"));

				backgroundAlpha(1f);
				isBindCard = true;
				paymentPopWin.dismiss();

			}
		});

		if (!paymentPopWin.isShowing()) {
			paymentPopWin.showAsDropDown(selectLayout);
		}
		backgroundAlpha(1.4f);
	}

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 *            透明的 0-1
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

}
