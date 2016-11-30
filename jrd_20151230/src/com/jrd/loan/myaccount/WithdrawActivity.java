package com.jrd.loan.myaccount;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.AccountBean;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.BindCardBean;
import com.jrd.loan.bean.CostBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.myInfomeation.ResetTransactionPswActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.umeng.UMengEvent;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.LoanRefreshScrollView;
import com.jrd.loan.widget.WindowView;

/**
 * 提现界面
 *
 * @author Luke
 */
public class WithdrawActivity extends BaseActivity implements OnClickListener, TextWatcher {

    private Context mContext;
    private WindowView windowView;
    private RelativeLayout bankCardLayout;
    private ImageView bankIconImage;
    private TextView balanceTV;
    private TextView costTV;
    private TextView realityTV;
    private TextView bankNumTV;
    private TextView addBankCardTV;
    private EditText moneyEdit;
    private EditText pwdEdit;
    private TextView forgetPwdTV;
    private Button withdrawBtn;
    private String cardCode;
    private String cardNumber;
    private LoanRefreshScrollView refreshScrollView;
    private LinearLayout inputLayout;
    private View includeAddLayout; // 添加银行卡的view
    // private double availableInvestMoney; // 投资额
    private double inputBalance;// 提现金额
    private double accountBalance;// 账户余额
    private double moneyCost;// 提现手续费
    private double reality; // 实扣金额
    private String moneyCostString;
    private String realityString;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_withdraw;
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setVisibility(View.INVISIBLE);
        windowView.setTitle(getResources().getString(R.string.loan_withdraw));
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        setNoNetworkClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                RequestValues();
            }
        });
    }

    @Override
    protected void initViews() {
        mContext = WithdrawActivity.this;
        refreshScrollView = (LoanRefreshScrollView) findViewById(R.id.loan_withdraw_refreshScrollView);
        inputLayout = (LinearLayout) findViewById(R.id.loan_withdraw_input_Layout);
        includeAddLayout = findViewById(R.id.loan_withdraw_addcard_layout);
        bankCardLayout = (RelativeLayout) findViewById(R.id.loan_withdraw_card_layout);
        bankCardLayout.setVisibility(View.GONE);
        bankCardLayout.setOnClickListener(this);
        bankIconImage = (ImageView) findViewById(R.id.loan_withdraw_card_icon_image);
        bankNumTV = (TextView) findViewById(R.id.loan_withdraw_card_num_tv);
        addBankCardTV = (TextView) findViewById(R.id.loan_addcard_TV);
        addBankCardTV.setOnClickListener(this);
        moneyEdit = (EditText) findViewById(R.id.loan_withdraw_money_edit);
        moneyEdit.addTextChangedListener(this);
        pwdEdit = (EditText) findViewById(R.id.loan_withdraw_pwd_edit);
        forgetPwdTV = (TextView) findViewById(R.id.loan_withdraw_forget_pwd_tv);
        forgetPwdTV.setOnClickListener(this);
        withdrawBtn = (Button) findViewById(R.id.loan_withdraw_enterBtn);
        withdrawBtn.setOnClickListener(this);
        balanceTV = (TextView) findViewById(R.id.loan_withdraw_balance_tv);
        costTV = (TextView) findViewById(R.id.loan_withdraw_cost_tv);
        realityTV = (TextView) findViewById(R.id.loan_withdraw_reality_tv);
        RequestValues();
    }

    /**
     * 获取绑定银行卡数据
     */
    private void RequestValues() {
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
                if (bean.getResultCode() == 0) {
                    if (bean.getUsrCardInfoList() != null && bean.getUsrCardInfoList().size() > 0) {
                        bankCardLayout.setVisibility(View.VISIBLE);
                        inputLayout.setVisibility(View.VISIBLE);
                        includeAddLayout.setVisibility(View.GONE);
                        cardCode = bean.getUsrCardInfoList().get(0).getBankCode();
                        cardNumber = bean.getUsrCardInfoList().get(0).getCardNumber();
                        SetBankData(cardCode, cardNumber);
                    } else {
                        includeAddLayout.setVisibility(View.VISIBLE);
                        inputLayout.setVisibility(View.GONE);
                    }
                }
                GetAccountValues();
            }
        });
    }

    /**
     * 获取账户信息数据
     */
    private void GetAccountValues() {
        BankApi.GetMyAccount(mContext, new OnHttpTaskListener<AccountBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowDrawDialog(mContext);
            }

            @Override
            public void onFinishTask(AccountBean bean) {
                if (bean.getResultCode() == 0) {
                    windowView.setVisibility(View.VISIBLE);
                    accountBalance = Double.valueOf(bean.getUserInfo().getAccountBalance());
                    NumberFormat format = NumberFormat.getInstance();
                    format.setMaximumFractionDigits(2);
                    // 可用余额
                    balanceTV.setText(format.format(new Double(bean.getUserInfo().getAccountBalance())));
                    // availableInvestMoney =
                    // Double.valueOf(bean.getUserInfo().getAvailableInvestMoney());
                }
                DismissDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loan_withdraw_card_layout:
                Intent selectIntent = new Intent(mContext, SelectCardActivity.class);
                startActivityForResult(selectIntent, Const.IntentCode.WITHDRAW_SELECTOR_CARD_INTENT_RESULT_CODE);
                break;
            case R.id.loan_addcard_TV:
                Intent addIntent = new Intent(mContext, AddBankCardActivity.class);
                addIntent.putExtra("intent", Const.IntentCode.WITHDRAW_TO_ADDCRAD_ITNENT_CODE);
                startActivityForResult(addIntent, Const.IntentCode.WITHDRAW_TO_ADDCRAD_ITNENT_CODE);
                break;
            case R.id.loan_withdraw_forget_pwd_tv:
                Intent transIntent = new Intent(mContext, ResetTransactionPswActivity.class);
                transIntent.putExtra(Const.Type.TRANS, Const.Type.WITHDRAW_TO_TRANS);
                startActivity(transIntent);
                break;
            case R.id.loan_withdraw_enterBtn:
                WithdrawResult();
                break;
        }
    }

    /**
     * 提现
     */
    private void WithdrawResult() {
        if (bankCardLayout.getVisibility() == View.GONE) {
            ToastUtil.showShort(mContext, R.string.loan_please_select_withdraw_card);
            return;
        } else if (moneyEdit.getText().toString().isEmpty()) {
            ToastUtil.showShort(mContext, R.string.loan_please_input_withdraw_money);
            return;
        } else if (pwdEdit.getText().toString().isEmpty()) {
            ToastUtil.showShort(mContext, R.string.loan_please_input_trade_pwd);
            return;
        } else if (reality > accountBalance) {
            ToastUtil.showShort(mContext, R.string.loan_withdraw_hint);
            return;
        } else if (reality < 0) {
            ToastUtil.showShort(mContext, R.string.loan_withdraw_money_small);
            return;
        }

        BankApi.WithdrawCash(mContext, getValues(), new OnHttpTaskListener<BaseBean>() {
            @Override
            public void onStart() {
                ShowProDialog(mContext);
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onFinishTask(BaseBean bean) {
                DismissDialog();

                if (bean.getResultCode() == 313) {
                    UMengEvent.onEvent(WithdrawActivity.this, UMengEvent.EVENT_ID_WITHDRAW);

                    ToastUtil.showShort(mContext, bean.getResultMsg());
                    startActivity(new Intent(mContext, CompleteActivity.class));
                    AppInfoPrefs.setWithDraw(true);
                    finish();
                } else {
                    ToastUtil.showShort(mContext, bean.getResultMsg());
                }
            }
        });
    }

    /**
     * 请求数据传送的值
     *
     * @return
     */
    private HashMap<String, String> getValues() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", UserInfoPrefs.getUserId());
        map.put("money", moneyEdit.getText().toString());
        map.put("password", pwdEdit.getText().toString());
        map.put("cardNumber", cardNumber);
        map.put("servFee", costTV.getText().toString());
        return map;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.IntentCode.WITHDRAW_SELECTOR_CARD_INTENT_RESULT_CODE || requestCode == Const.IntentCode.WITHDRAW_TO_ADDCRAD_ITNENT_CODE) {
            if (data != null) {
                ResultData(data);
            }
        } else if (resultCode != RESULT_OK) {
            finish();
        }
    }

    /**
     * 填充提现银行卡
     *
     * @param data
     */
    private void ResultData(Intent data) {
        cardCode = data.getStringExtra("cardCode");
        cardNumber = data.getStringExtra("cardNumber");
        SetBankData(cardCode, cardNumber);
    }

    /**
     * 设置提现银行卡数据
     *
     * @param cardCode
     * @param cardNum
     */
    private void SetBankData(String cardCode, String cardNum) {
        bankNumTV.setText(getProtectedMobile(cardNum));
        bankCardLayout.setVisibility(View.VISIBLE);
        includeAddLayout.setVisibility(View.GONE);
        inputLayout.setVisibility(View.VISIBLE);
        switch (Integer.valueOf(cardCode)) {
            case 104:
                bankIconImage.setImageResource(R.drawable.loan_zhongguo_head);
                break;
            case 102:
                bankIconImage.setImageResource(R.drawable.loan_gongshang_head);
                break;
            case 103:
                bankIconImage.setImageResource(R.drawable.loan_nongye_head);
                break;
            case 105:
                bankIconImage.setImageResource(R.drawable.loan_jianshe_head);
                break;
            case 308:
                bankIconImage.setImageResource(R.drawable.loan_zhaoshang_head);
                break;
            case 403:
                bankIconImage.setImageResource(R.drawable.loan_youzheng_head);
                break;
            case 302:
                bankIconImage.setImageResource(R.drawable.loan_zhongxin_head);
                break;
            case 303:
                bankIconImage.setImageResource(R.drawable.loan_guangda_head);
                break;
            case 309:
                bankIconImage.setImageResource(R.drawable.loan_xingye_head);
                break;
        }
    }

    /**
     * 拼接银行卡账号字符串
     *
     * @param phoneNumber
     * @return
     */
    private String getProtectedMobile(String phoneNumber) {
        if (phoneNumber.isEmpty() || phoneNumber.length() < 11) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(phoneNumber.subSequence(0, 4));
        builder.append(" **** **** ");
        builder.append(phoneNumber.subSequence(phoneNumber.length() - 3, phoneNumber.length()));
        return builder.toString();
    }

    @Override
    public void afterTextChanged(Editable str) {
        if (str.toString().equals(".")) {// 如果提现费用第一位为., 清空
            return;
        }
        if (!str.toString().isEmpty() && Double.valueOf(str.toString()) > 0) {
            // inputBalance = Double.valueOf(str.toString());
            //
            // if (inputBalance > accountBalance) {
            // ToastUtil.showShort(mContext, R.string.loan_withdraw_hint);
            // }
            //
            // // 提现费用 = 2+提现金额 * 0.25%
            // if (inputBalance <= availableInvestMoney) {
            // moneyCost = 0.0 + 2;
            // } else {
            // moneyCost = (inputBalance - availableInvestMoney) * 0.25 / 100 +
            // 2;
            // }
            //
            // // 四舍五入
            // BigDecimal b = new BigDecimal(moneyCost);
            //
            // // 实扣金额
            // reality = inputBalance + b.setScale(2,
            // BigDecimal.ROUND_HALF_UP).doubleValue();
            //
            // DecimalFormat df = new DecimalFormat("0.00");
            // // 提现费用
            // costTV.setText(df.format(moneyCost));
            //
            // // 实扣金额
            // realityTV.setText(df.format(reality));
            // 提现费用接口请求
            RequestCost(str);
        } else {
            if (str.toString().equals("")) {// 如果提现费用第一位为., 清空
                costTV.setText(" ");
                realityTV.setText(" ");
            } else {
                costTV.setText("0");
                realityTV.setText("0");
            }
        }
    }

    // 提现费用结算接口
    private void RequestCost(Editable str) {
        BankApi.WithdrawCost(mContext, UserInfoPrefs.getUserId(), str.toString(), new OnHttpTaskListener<CostBean>() {

            @Override
            public void onStart() {
            }

            @Override
            public void onTaskError(int resposeCode) {
            }

            @Override
            public void onFinishTask(CostBean bean) {
                if (bean != null && bean.getResultCode() == 0) {
                    moneyCostString = bean.getMoneyCost();
                    BigDecimal bd = new BigDecimal(moneyCostString);
                    moneyCost = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    costTV.setText(String.valueOf(moneyCost));
                    realityString = bean.getActuallyCharge();
                    BigDecimal bd1 = new BigDecimal(realityString);
                    reality = bd1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    realityTV.setText(String.valueOf(reality));
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
        if (s.toString().equals(".")) {// 如果提现费用第一位为., 清空
            moneyEdit.setText("");
            return;
        }
        if (s.toString().length() >= 2 && s.toString().charAt(0) == '0' && (s.toString().charAt(1) >= '0' && s.toString().charAt(1) <= '9')) {
            moneyEdit.setText(s.toString().charAt(1) + "");
            moneyEdit.setSelection(moneyEdit.getText().length());
            return;
        }
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                moneyEdit.setText(s);
                moneyEdit.setSelection(s.length());
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            moneyEdit.setText(s);
            moneyEdit.setSelection(2);
        }
        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                moneyEdit.setText(s.subSequence(0, 1));
                moneyEdit.setSelection(1);
                return;
            }
        }
    }
}
