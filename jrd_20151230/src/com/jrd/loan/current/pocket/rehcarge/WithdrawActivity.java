package com.jrd.loan.current.pocket.rehcarge;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.api.PocketApi;
import com.jrd.loan.api.StatisticsApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.bean.AccountBean;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.BindCardBean;
import com.jrd.loan.bean.CostBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.constant.Const.IntentCode;
import com.jrd.loan.myaccount.AccountRollOutActivity;
import com.jrd.loan.myaccount.AddBankCardActivity;
import com.jrd.loan.myaccount.CompleteActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.BankLogoUtil;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.DoubleUtil;
import com.jrd.loan.util.FormatUtils;
import com.jrd.loan.util.KeyBoardUtil;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

import java.util.HashMap;

public class WithdrawActivity extends BaseActivity implements OnClickListener, TextWatcher {

    private WindowView windowView;
    private TextView bankCardNameTV;
    private TextView bankCardTailNumTV;
    private ImageView bankCardImg;
    private EditText withAmountEdit;
    private TextView factorageTV;
    private TextView deductTV;
    private TextView amountTV;
    private String cardNumber; // 银行卡账号
    private String totalAmount;// 可提现金额
    private String moneyCostString; // 提现手续费 string
    private String realityString; // 实际扣除 String

    private InputMethodManager inputMethodManager;

    private LinearLayout addBankLayout;
    private RelativeLayout cardLayout;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_current_withdraw;
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setVisibility(View.GONE);
        windowView.setTitle("提现");
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                KeyBoardUtil.closeKeybord(deductTV, WithdrawActivity.this);
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        this.inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // 添加绑卡的 include layout
        addBankLayout = (LinearLayout) findViewById(R.id.loan_addcard_Layout);
        addBankLayout.setVisibility(View.GONE);
        addBankLayout.setOnClickListener(this);

        // 绑卡信息的 include layout
        cardLayout = (RelativeLayout) findViewById(R.id.loan_recharge_bankinfoLayout);
        cardLayout.setVisibility(View.GONE);
        cardLayout.setOnClickListener(this);

        bankCardNameTV = (TextView) findViewById(R.id.loan_recharge_bankNameTV);
        bankCardTailNumTV = (TextView) findViewById(R.id.loan_recharge_bankTailNumTV);
        bankCardImg = (ImageView) findViewById(R.id.loan_recharge_bankiconImg);
        withAmountEdit = (EditText) findViewById(R.id.loan_withdraw_amountEdit);
        withAmountEdit.addTextChangedListener(this);
        TextView allOutTV = (TextView) findViewById(R.id.loan_withdraw_allOutTV);
        allOutTV.setOnClickListener(this);
        factorageTV = (TextView) findViewById(R.id.loan_withdraw_factorageTV);
        deductTV = (TextView) findViewById(R.id.loan_withdraw_deductTV);
        amountTV = (TextView) findViewById(R.id.loan_withdraw_amountTV);
        TextView detailsTV = (TextView) findViewById(R.id.loan_withdraw_detailsTV);
        detailsTV.setOnClickListener(this);
        Button enterBtn = (Button) findViewById(R.id.loan_withdraw_enterBtn);
        enterBtn.setOnClickListener(this);

        RequestValues();

        setNoNetworkClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestValues();
            }
        });
    }

    /**
     * 获取绑定银行卡数据
     */
    private void RequestValues() {
        BankApi.MyBindCard(WithdrawActivity.this, new OnHttpTaskListener<BindCardBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowDrawDialog(WithdrawActivity.this);
            }

            @Override
            public void onFinishTask(BindCardBean bean) {
                if (bean.getResultCode() == 0) {
                    if (bean.getUsrCardInfoList() != null && bean.getUsrCardInfoList().size() > 0) {
                        cardLayout.setVisibility(View.VISIBLE);
                        addBankLayout.setVisibility(View.GONE);
                        // set银行卡信息
                        SetCardInfo(bean.getUsrCardInfoList().get(0).getBankName(), bean.getUsrCardInfoList().get(0).getBankCode(), bean.getUsrCardInfoList().get(0).getCardNumber());
                    } else {
                        cardLayout.setVisibility(View.GONE);
                        addBankLayout.setVisibility(View.VISIBLE);
                    }
                    GetAccountValues();
                } else {
                    ToastUtil.showShort(WithdrawActivity.this, bean.getResultMsg());
                }

            }
        });
    }

    /**
     * set银行卡信息
     */
    private void SetCardInfo(String cardName, String bankCode, String cardNum) {
        BankLogoUtil.setBankLog(bankCardImg, Integer.parseInt(bankCode));

        bankCardNameTV.setText(cardName);
        cardNumber = cardNum;

        // 银行卡尾号后4位
        String tail = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
        bankCardTailNumTV.setText(String.format("尾号%s", tail));
    }

    /**
     * 获取账户信息数据
     */
    private void GetAccountValues() {
        BankApi.GetMyAccount(WithdrawActivity.this, new OnHttpTaskListener<AccountBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowDrawDialog(WithdrawActivity.this);
            }

            @Override
            public void onFinishTask(AccountBean bean) {
                if (bean.getResultCode() == 0) {
                    windowView.setVisibility(View.VISIBLE);
                    totalAmount = DoubleUtil.getMoney(FormatUtils.ReplaceString(bean.getUserInfo().getAccountBalance()));
                    amountTV.setText(String.format("可提现金额%s元", DoubleUtil.getMoney(totalAmount)));
                } else {
                    ToastUtil.showShort(WithdrawActivity.this, bean.getResultMsg());
                }
                DismissDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loan_withdraw_allOutTV:
                withAmountEdit.setText(totalAmount);
                withAmountEdit.setSelection(totalAmount.length());
                break;

            case R.id.loan_withdraw_enterBtn:// 确认提现
                hideKeyboard();
                ToWithdraw();
                break;

            case R.id.loan_withdraw_detailsTV:
                Intent detailsIntent = new Intent(WithdrawActivity.this, WebViewActivity.class);
                detailsIntent.putExtra("htmlUrl", JrdConfig.getBaseUrl() + PocketApi.WITHFRAW_DETAILS);
                detailsIntent.putExtra("htmlTitle", windowView.getTitle());
                startActivity(detailsIntent);
                break;

            case R.id.loan_recharge_bankinfoLayout:// 银行卡信息
                Intent intent = new Intent(WithdrawActivity.this, SelectCardActivity.class);
                startActivityForResult(intent, IntentCode.INTENT_TO_SELECT_ACTIVITY);
                break;
            case R.id.loan_addcard_Layout:// 添加银行卡信息layout
                Intent addintent = new Intent(WithdrawActivity.this, AddBankCardActivity.class);
                addintent.putExtra("intent", Const.IntentCode.WITHDRAW_TO_ADDCRAD_ITNENT_CODE);
                startActivityForResult(addintent, IntentCode.WITHDRAW_TO_ADDCRAD_ITNENT_CODE);
                break;
        }
    }

    // 提现
    private void ToWithdraw() {
        if (cardLayout.getVisibility() == View.GONE) {
            ToastUtil.showShort(WithdrawActivity.this, "请先添加银行卡");
            return;
        } else if (withAmountEdit.getText().toString().isEmpty()) {
            ToastUtil.showLong(WithdrawActivity.this, R.string.loan_input_withdraw_money);
            return;
        } else if (Double.valueOf(withAmountEdit.getText().toString()) <= 0) {
            ToastUtil.showLong(WithdrawActivity.this, "提现金额不能为0");
            return;
        } else if (Double.valueOf(withAmountEdit.getText().toString()) > Double.valueOf(totalAmount)) {
            ToastUtil.showLong(WithdrawActivity.this, "可提现额度不足");
            return;
        }else if (Double.valueOf(realityString) > Double.valueOf(totalAmount)) {
            ToastUtil.showLong(WithdrawActivity.this, "可提现额度不足");
            return;
        }

        // 交易密码dialog
        DialogUtils.showWithdrawInfoDialog(WithdrawActivity.this, false, withAmountEdit.getText().toString(), moneyCostString, realityString, bankCardNameTV.getText().toString(), cardNumber, "", "", new DialogUtils.OnTransPasswdEventListener() {

            @Override
            public void onConfirm(String transPasswd, Dialog dialog) {
                if (transPasswd.equals("")) {
                    ToastUtil.showShort(WithdrawActivity.this, "请输入交易密码");
                } else {
                    dialog.dismiss();
                    hideKeyboard();
                    WithdrawResult(transPasswd);
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 提现
     */
    private void WithdrawResult(String pwd) {
        BankApi.WithdrawCash(WithdrawActivity.this, getValues(pwd), new OnHttpTaskListener<BaseBean>() {

            @Override
            public void onStart() {
                ShowProDialog(WithdrawActivity.this);
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onFinishTask(BaseBean bean) {
                DismissDialog();
                if (bean.getResultCode() == 313) {
                    ToastUtil.showShort(WithdrawActivity.this, bean.getResultMsg());
                    
                    // 用户行为统计接口
                    StatisticsApi.getUserBehavior(WithdrawActivity.this, Const.EventName.WITHDRAW, Const.EventId.WITHDRAW, null, null, withAmountEdit.getText().toString());
                    
                    startActivity(new Intent(WithdrawActivity.this, CompleteActivity.class));
                    AppInfoPrefs.setWithDraw(true);
                    finish();
                } else {
                    ToastUtil.showShort(WithdrawActivity.this, bean.getResultMsg());
                }
                

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().equals(".")) {// 如果提现费用第一位为., 清空
            withAmountEdit.setText("");
            return;
        }

        if (s.toString().length() >= 2 && s.toString().charAt(0) == '0' && (s.toString().charAt(1) >= '0' && s.toString().charAt(1) <= '9')) {
            withAmountEdit.setText(String.format("%s", s.toString().charAt(1)));
            withAmountEdit.setSelection(withAmountEdit.getText().length());
            return;
        }

        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                withAmountEdit.setText(s);
                withAmountEdit.setSelection(s.length());
            }
        }

        if (s.toString().trim().equals(".")) {
            s = "0" + s;
            withAmountEdit.setText(s);
            withAmountEdit.setSelection(2);
        }

        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                withAmountEdit.setText(s.subSequence(0, 1));
                withAmountEdit.setSelection(1);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable str) {

        if (str.toString().equals(".")) {// 如果提现费用第一位为., 清空
            return;
        }
        if (!str.toString().isEmpty() && Double.valueOf(str.toString()) > 0) {
            RequestCost(str);
        } else {
            factorageTV.setText("0元");
            deductTV.setText(R.string.loan_deduct_0);
        }

        LogUtil.d("<><><><><>", str.toString());
    }

    // 提现费用结算接口
    private void RequestCost(Editable str) {
        BankApi.WithdrawCost(WithdrawActivity.this, UserInfoPrefs.getUserId(), str.toString(), new OnHttpTaskListener<CostBean>() {

            @Override
            public void onStart() {
                ShowProDialog(WithdrawActivity.this);
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onFinishTask(CostBean bean) {
                DismissDialog();
                if (bean != null) {
                    if (bean.getResultCode() == 0) {
                        moneyCostString = DoubleUtil.getMoney(bean.getMoneyCost());
                        factorageTV.setText(String.format("%s元", moneyCostString));
                        realityString = DoubleUtil.getMoney(bean.getActuallyCharge());
                        deductTV.setText(String.format("实际扣除%s元", realityString));
                    } else if (bean.getResultCode() == 9999) {
                        ToastUtil.showShort(WithdrawActivity.this, bean.getResultMsg());
                    }
                }
            }
        });
    }

    /**
     * 请求数据传送的值
     */
    private HashMap<String, String> getValues(String pwd) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", UserInfoPrefs.getUserId());
        map.put("money", withAmountEdit.getText().toString());
        map.put("password", pwd);
        map.put("cardNumber", cardNumber);
        map.put("servFee", moneyCostString);
        return map;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == IntentCode.INTENT_TO_SELECT_ACTIVITY || requestCode == IntentCode.WITHDRAW_TO_ADDCRAD_ITNENT_CODE) {
                cardLayout.setVisibility(View.VISIBLE);
                addBankLayout.setVisibility(View.GONE);

                String cardName = data.getStringExtra("cardName");
                String bankCode = data.getStringExtra("bankCode");
                String cardNum = data.getStringExtra("cardNumber");

                SetCardInfo(cardName, bankCode, cardNum);

            }
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                this.inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
