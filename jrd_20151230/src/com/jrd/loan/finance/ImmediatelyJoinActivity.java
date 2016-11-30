package com.jrd.loan.finance;

import java.math.BigDecimal;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.api.ProjectInfoApi;
import com.jrd.loan.api.WebApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.bean.AccountBean;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.SevenInfo;
import com.jrd.loan.myaccount.RechargeActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.DigitUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

/**
 * 立即加入
 *
 * @author Aaron
 */
public class ImmediatelyJoinActivity extends BaseActivity {
    private LinearLayout ontouch;
    private Context mContext;
    private SevenInfo mSevenInfo;
    private String userId;
    private TextView mUpdateDesc;
    private TextView mAnnualRate;
    private TextView mInvestmentPeriod;
    private TextView mSurplusAmount;
    private TextView mJoinAmount;
    private TextView mAvailableBalance;
    private EditText mInvestmentEdt;
    private TextView mExpectedReturn;

    private Boolean mReturn = true;
    // 投资协议
    private CheckBox mImmedAgreeProtocol;
    private TextView mImmedProtocol;
    private Button Recharge;
    private Button joiningBtn;

    private String Balance;// 余额

    private String investAmount;

    @Override
    protected int loadWindowLayout() {
        mContext = ImmediatelyJoinActivity.this;
        return R.layout.loan_immediately_join_layout;
    }

    @Override
    protected void initViews() {
        ontouch = (LinearLayout) findViewById(R.id.loan_ontouch);
        ontouch.setOnClickListener(this.btnClickListener);
        mUpdateDesc = (TextView) findViewById(R.id.loan_updateDesc);
        mAnnualRate = (TextView) findViewById(R.id.loan_annualRate);
        mInvestmentPeriod = (TextView) findViewById(R.id.loan_investmentPeriod);
        mSurplusAmount = (TextView) findViewById(R.id.loan_surplusAmount);
        mJoinAmount = (TextView) findViewById(R.id.loan_join_amount);

        mAvailableBalance = (TextView) findViewById(R.id.loan_available_balance);
        mInvestmentEdt = (EditText) findViewById(R.id.loan_investment_edt);
        mExpectedReturn = (TextView) findViewById(R.id.loan_expected_return);
        // 投资协议
        mImmedAgreeProtocol = (CheckBox) findViewById(R.id.loan_ImmedAgreeProtocol);
        mImmedProtocol = (TextView) findViewById(R.id.loan_ImmedProtocol);
        mImmedProtocol.setOnClickListener(this.btnClickListener);
        // 充值
        Recharge = (Button) findViewById(R.id.loan_recharge);
        Recharge.setOnClickListener(this.btnClickListener);
        // 确认加入
        joiningBtn = (Button) findViewById(R.id.loan_confirm_joiningBtn);
        joiningBtn.setOnClickListener(this.btnClickListener);

        initData();
    }

    protected void initData() {
        Intent intent = getIntent();
        mSevenInfo = (SevenInfo) intent.getSerializableExtra("mSevenInfo");

        userId = UserInfoPrefs.getUserId();

        // 7付你赋值
        set7fnInfo(mSevenInfo);

        // 我的账户信息接口获取可用余额
        getMyAccount();

        // 投资金额光标定位在最后边
        mInvestmentEdt.setSelection(mInvestmentEdt.getText().length());
    }

    private final OnClickListener btnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loan_recharge: // 充值
                    showRecharge();
                    break;
                case R.id.loan_ImmedProtocol: // 投资协议
                    showImmedProtocol();
                    break;
                case R.id.loan_confirm_joiningBtn: // 确认加入
                    showConfirmJjoining();
                case R.id.loan_ontouch: // 关闭软键盘
                    CloseOntouch();
                    break;
            }
        }
    };

    // 充值
    private void showRecharge() {
        Intent rechargeIntent = new Intent(mContext, RechargeActivity.class);
        startActivity(rechargeIntent);
    }

    // 投资协议
    private void showImmedProtocol() {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("htmlUrl", JrdConfig.getBaseUrl() + WebApi.FinanceApi.GETAGREEMENTINFO);
        intent.putExtra("htmlTitle", getString(R.string.loan_bid_protocol_title));
        startActivity(intent);
    }

    // 确认加入
    private void showConfirmJjoining() {
        investAmount = mInvestmentEdt.getText().toString();

        if (TextUtils.isEmpty(investAmount)) {// 投标金额不能为空
            ToastUtil.showShort(this, R.string.loan_add_money);
            return;
        }

        int bidMoney = Integer.parseInt(investAmount);
        if (bidMoney % 100 != 0) {// 投标金额必须100整数倍
            ToastUtil.showShort(this, R.string.loan_bid_money_must_100_times);
            return;
        }

        double mAmount = Double.parseDouble(DigitUtil.getMoneys(mSevenInfo.getInvestmentAmount()));

        if (bidMoney > mAmount) {// 输入金额大于可投金额
            ToastUtil.showShort(this, new StringBuffer(getResources().getString(R.string.loan_amount_investment_money)).append(DigitUtil.getMoneys(mSevenInfo.getInvestmentAmount())).append("元").toString());
            return;
        }

        double balance = Double.parseDouble(Balance);
        if (bidMoney > balance) {// 输入金额大于可用余额
            ToastUtil.showShort(this, R.string.loan_available_bal_money);
            return;
        }

        if (!this.mImmedAgreeProtocol.isChecked()) {// 没有选中投资协议
            ToastUtil.showShort(this, R.string.loan_choose_bid_protocol);
            return;
        }

        String projectId = mSevenInfo.getProjectId();
        ProjectInfoApi.get7fnTransaction(mContext, userId, projectId, investAmount, new OnHttpTaskListener<BaseBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowProDialog(mContext);
            }

            @Override
            public void onFinishTask(BaseBean bean) {
                DismissDialog();
                if (bean.getResultCode() == 0) {
                    showJoinSuccess();
                } else {
                    ToastUtil.showShort(mContext, bean.getResultMsg());
                }
            }
        });

    }

    private void showJoinSuccess() {
        Intent intent = new Intent(mContext, JoinSuccessActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // 关闭软键盘
    private void CloseOntouch() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // 7付你赋值
    private void set7fnInfo(SevenInfo mSevenInfo) {
        mUpdateDesc.setText(mSevenInfo.getUpdateDesc());
        mAnnualRate.setText(mSevenInfo.getAnnualRate());

        mInvestmentPeriod.setText(DigitUtil.getMonth(mSevenInfo.getInvestmentPeriod()));

        // 可投金额
        mSurplusAmount.setText(DigitUtil.getMoneys(mSevenInfo.getInvestmentAmount()));

        if (Double.parseDouble(mSevenInfo.getInvestmentAmount()) < 10000) {
            mJoinAmount.setText("元");
        } else {
            mJoinAmount.setText("万");
        }

    }

    // 7付你预计收入
    private void ExpectedReturn() {
        if (mReturn) {
            ExpectedReturnCalculation(this.mInvestmentEdt.getText().toString());
        }
        mInvestmentEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence txt, int arg1, int arg2, int arg3) {
                if (!TextUtils.isEmpty(txt)) {
                    ExpectedReturnCalculation(txt.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    // 7付你预计收入
    private void ExpectedReturnCalculation(String text) {
        String bidMoneyStr = text;
        int bidMoney = Integer.parseInt(bidMoneyStr);

        double investEdt = Double.parseDouble(bidMoneyStr);
        double updateDesc = 7;
        double mYear = 365;
        double OneHundred = 100;
        double ExpcReturn = (updateDesc / mYear) * (updateDesc / OneHundred) * investEdt;
        BigDecimal mBigDecimal = new BigDecimal(ExpcReturn);
        double expeReturn = mBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        mExpectedReturn.setText("" + expeReturn);

        if (!mReturn) {
            if (TextUtils.isEmpty(bidMoneyStr)) {// 投标金额不能为空
                ToastUtil.showShort(this, R.string.loan_add_money);
                return;
            }

            if (bidMoney % 100 != 0) {// 投标金额必须100整数倍
                ToastUtil.showShort(this, R.string.loan_bid_money_must_100_times);
                return;
            }

            double mAmount = Double.parseDouble(DigitUtil.getMoneys(mSevenInfo.getInvestmentAmount()));

            if (bidMoney > mAmount) {// 输入金额大于可投金额
                ToastUtil.showShort(this, new StringBuffer(getResources().getString(R.string.loan_amount_investment_money)).append(DigitUtil.getMoneys(mSevenInfo.getInvestmentAmount())).append("元").toString());
                return;
            }

            double balance = Double.parseDouble(Balance);
            if (bidMoney > balance) {// 输入金额大于可用余额
                ToastUtil.showShort(this, R.string.loan_available_bal_money);
                return;
            }
        }

        mReturn = false;
    }

    // 我的账户信息接口获取可用余额
    private void getMyAccount() {
        BankApi.GetMyAccount(mContext, new OnHttpTaskListener<AccountBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowProDialog(mContext);
            }

            @Override
            public void onFinishTask(AccountBean bean) {
                DismissDialog();
                if (bean.getResultCode() == 0 && bean.getUserInfo() != null) {
                    Balance = bean.getUserInfo().getAccountBalance();
                    mAvailableBalance.setText(DigitUtil.getMoneys(Balance));
                    ExpectedReturn();
                } else {
                    ToastUtil.showShort(mContext, bean.getResultMsg());
                }
            }
        });
    }

    @Override
    protected void initTitleBar() {
        WindowView windowView = (WindowView) findViewById(R.id.windowView);

        // 项目介绍
        windowView.setTitle(R.string.loan_finance_immediately_join);

        // 返回按钮单击事件
        windowView.setLeftButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
