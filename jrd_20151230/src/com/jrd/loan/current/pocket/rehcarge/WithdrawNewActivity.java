package com.jrd.loan.current.pocket.rehcarge;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.api.StatisticsApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.AccountBean;
import com.jrd.loan.bean.BankCardBean;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.BindCardBean;
import com.jrd.loan.bean.CostBean;
import com.jrd.loan.bean.OpenBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.myaccount.CompleteActivity;
import com.jrd.loan.myaccount.OpeningBankAct;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.umeng.UMengEvent;
import com.jrd.loan.util.BankLogoUtil;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.DialogUtils.OnTransPasswdEventListener;
import com.jrd.loan.util.DoubleUtil;
import com.jrd.loan.util.FormatUtils;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.NetUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

import java.util.HashMap;

/**
 * 新版提现界面
 */
public class WithdrawNewActivity extends BaseActivity implements View.OnClickListener {
    private TextView amountTV; // 可提现金额
    private TextView costTV; // 手续费
    private EditText amountEdit;
    private LinearLayout addressLayout;
    private TextView addressTV;
    private LinearLayout openLayout;
    private TextView openTV;
    private TextView hintTV;
    private ImageView cardIconImg;
    private TextView cardNameTV;
    private TextView cardNumTV;
    private TextView updateTV;
    private ScrollView layoutId;

    private Dialog cityDialog; // 市dialog
    private Dialog provinceDialog; // 省dialog

    private String provinceName = "";
    private String provinceCode;

    private String cityName;
    private String cityCode; // 北京

    private String cardNum;
    private String bankCode;
    private String bankSecondName;// 银行分行名称
    private String bankName;

    private String moneyCost;
    private String moneyReailty = "0";

    private String totalAmount;

    private WindowView windowView;
    private String bankPhoneString; //银行电话

    @Override
    protected int loadWindowLayout() {
        return R.layout.laon_activity_withdraw_new;
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setVisibility(View.INVISIBLE);
        windowView.setTitle("提现");
        windowView.setLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        this.layoutId = (ScrollView) findViewById(R.id.layoutId);

        amountTV = (TextView) findViewById(R.id.loan_new_withdraw_amount_text);
        costTV = (TextView) findViewById(R.id.loan_withdraw_cost_tv);
        costTV.setText(getString(R.string.loan_withdrawnew_cost_text, "0", "0"));

        amountEdit = (EditText) findViewById(R.id.loan_new_withdraw_amount_Edit);
        amountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().equals(".")) {// 如果提现费用第一位为., 清空
                    amountEdit.setText("");
                    return;
                }

                if (s.toString().length() >= 2 && s.toString().charAt(0) == '0' && (s.toString().charAt(1) >= '0' && s.toString().charAt(1) <= '9')) {
                    amountEdit.setText(String.format("%s", s.toString().charAt(1)));
                    amountEdit.setSelection(amountEdit.getText().length());
                    return;
                }

                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        amountEdit.setText(s);
                        amountEdit.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;
                    amountEdit.setText(s);
                    amountEdit.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        amountEdit.setText(s.subSequence(0, 1));
                        amountEdit.setSelection(1);
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
                    costTV.setText(getString(R.string.loan_withdrawnew_cost_text, "0", "0"));
                }

                LogUtil.d("<><><><><>", str.toString());
            }
        });

        // 开户行
        addressLayout = (LinearLayout) findViewById(R.id.loan_new_withdraw_address_layout);
        addressLayout.setOnClickListener(this);

        // 所在地
        addressTV = (TextView) findViewById(R.id.loan_new_withdraw_address_text);

        // 支行
        openLayout = (LinearLayout) findViewById(R.id.loan_new_withdraw_open_bank_layout);
        openLayout.setOnClickListener(this);
        openTV = (TextView) findViewById(R.id.loan_new_withdraw_open_bank_text);

        // 银行温馨提示
        hintTV = (TextView) findViewById(R.id.loan_new_withdraw_hint_text);

        // 确认提现
        Button enterBtn = (Button) findViewById(R.id.loan_new_withdraw_enter_btn);
        enterBtn.setOnClickListener(this);

        // 银行icon
        cardIconImg = (ImageView) findViewById(R.id.loan_recharge_bankiconImg);

        // 银行名称
        cardNameTV = (TextView) findViewById(R.id.loan_recharge_bankNameTV);

        // 银行卡号
        cardNumTV = (TextView) findViewById(R.id.loan_recharge_bankTailNumTV);

        // 修改开户行信息
        updateTV = (TextView) findViewById(R.id.loan_new_withdraw_update_text);
        updateTV.setOnClickListener(this);

        this.obtainBindBank();

        setNoNetworkClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                obtainBindBank();
            }
        });
    }

    private void obtainBindBank() {
        BankApi.MyBindCard(this, new OnHttpTaskListener<BindCardBean>() {
            private View noNetworkLayoutView;

            @Override
            public void onTaskError(int resposeCode) {
                ShowNoNetWorkView();
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowDrawDialog(WithdrawNewActivity.this);

                this.noNetworkLayoutView = findViewById(R.id.noNetworkLayout);
                this.noNetworkLayoutView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                this.noNetworkLayoutView.setVisibility(View.GONE);

                if (!NetUtil.hasMobileNet()) {
                    ShowNoNetWorkView();
                }
            }

            @Override
            public void onFinishTask(BindCardBean bean) {
                if (bean != null) {
                    if (bean.getResultCode() == 0) {

                        this.noNetworkLayoutView.setVisibility(View.GONE);
                        layoutId.setVisibility(View.VISIBLE);

                        if (!bean.getUsrCardInfoList().isEmpty()) {
                            BankCardBean cardBean = bean.getUsrCardInfoList().get(0);

                            initBankInfo(cardBean);
                        }
                        GetAccountValues();
                    } else {
                        ToastUtil.showShort(WithdrawNewActivity.this, bean.getResultMsg());
                    }
                }

                DismissDialog();
            }

            private void ShowNoNetWorkView() {
                this.noNetworkLayoutView.setVisibility(View.VISIBLE);
                this.noNetworkLayoutView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        obtainBindBank();
                    }
                });

                layoutId.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 获取账户信息数据
     */
    private void GetAccountValues() {
        BankApi.GetMyAccount(WithdrawNewActivity.this, new OnHttpTaskListener<AccountBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowDrawDialog(WithdrawNewActivity.this);
            }

            @Override
            public void onFinishTask(AccountBean bean) {
                if (bean.getResultCode() == 0) {
                    windowView.setVisibility(View.VISIBLE);
                    totalAmount = DoubleUtil.getMoney(FormatUtils.ReplaceString(bean.getUserInfo().getAccountBalance()));
                    amountTV.setText(String.format("可提现金额:%s元", DoubleUtil.getMoney(totalAmount)));
                } else {
                    ToastUtil.showShort(WithdrawNewActivity.this, bean.getResultMsg());
                }
                DismissDialog();
            }
        });
    }

    private void initBankInfo(BankCardBean cardBean) {
        this.bankCode = cardBean.getBankCode();
        this.cardNum = cardBean.getCardNumber();

        this.cityCode = cardBean.getCityCode();
        this.bankCode = cardBean.getBankCode();

        this.bankName = cardBean.getBankName();
        this.provinceName = cardBean.getProvinceName();
        this.cityName = cardBean.getCityName();
        this.bankSecondName = cardBean.getBankBranchName();

        // 银行名称
        this.cardNameTV.setText(cardBean.getBankName());

        // 银行卡号card
        int length = cardBean.getCardNumber().length();
        this.cardNumTV.setText("**** **** **** " + cardBean.getCardNumber().substring(length - 4, length));

        // 银行温馨提示
        bankPhoneString = cardBean.getServicePhone();

        String hintString = getString(R.string.loan_bank_kindly_remind, cardBean.getBankName(), bankPhoneString);
        LogUtil.d("------", hintString);
        //给银行电话加蓝颜色并且可以点击
        SpannableString spPhone = new SpannableString(hintString);
        spPhone.setSpan(new ForegroundColorSpan(Color.BLUE), hintString.length() - bankPhoneString.length(), hintString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spPhone.setSpan(new RelativeSizeSpan(1.2f), hintString.length() - bankPhoneString.length(), hintString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spPhone.setSpan(new CustomClickSpan(), hintString.length() - bankPhoneString.length(), hintString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        hintTV.setText(spPhone);
        hintTV.setMovementMethod(LinkMovementMethod.getInstance());

        // 银行icon
        BankLogoUtil.setBankLog(cardIconImg, Integer.parseInt(cardBean.getBankCode()));

        // 显示/隐藏所在地和开户行
        if (!TextUtils.isEmpty(cardBean.getCityName())) {
            addressLayout.setVisibility(View.GONE);
            openLayout.setVisibility(View.GONE);

            LogUtil.d("--------------", provinceName);
            if (provinceName.equals("")) {
                addressTV.setText(cityName);
            } else {
                addressTV.setText(provinceName + "-" + cityName);
            }
            openTV.setText(cardBean.getBankBranchName());

            updateTV.setVisibility(View.VISIBLE);
        } else {
            addressLayout.setVisibility(View.VISIBLE);
            openLayout.setVisibility(View.VISIBLE);
            updateTV.setVisibility(View.GONE);
        }
    }

    private String getBankNum(String bankNum) {
        int len = bankNum.length();

        StringBuffer buffer = new StringBuffer();

        int bankNumLen = (len % 4 == 0) ? (len - 4) : (len / 4 * 4);

        for (int i = 0; i < bankNumLen; i++) {
            buffer.append("*");

            if (i != 0 && (i + 1) % 4 == 0) {
                buffer.append(" ");
            }
        }

        buffer.append(bankNum.substring(bankNumLen + 1));

        return buffer.toString();
    }

    private void SelectCity() {
        BankApi.GetBankAreaList(this, "", "", false, new OnHttpTaskListener<OpenBean>() {
            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowProDialog(WithdrawNewActivity.this);
            }

            @Override
            public void onFinishTask(final OpenBean bean) {
                DismissDialog();
                if (bean.getResultCode() == 0) {
                    if (bean.getCityInfoList() != null && bean.getCityInfoList().size() > 0) {
                        provinceDialog = DialogUtils.ShowCityDialog(WithdrawNewActivity.this, bean.getCityInfoList(), new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                provinceCode = bean.getCityInfoList().get(arg2).getCityCode();
                                provinceName = bean.getCityInfoList().get(arg2).getCityName();

                                BankApi.GetBankAreaList(WithdrawNewActivity.this, provinceCode, "", false, new OnHttpTaskListener<OpenBean>() {
                                    @Override
                                    public void onTaskError(int resposeCode) {
                                        DismissDialog();
                                    }

                                    @Override
                                    public void onStart() {
                                        ShowProDialog(WithdrawNewActivity.this);
                                    }

                                    @Override
                                    public void onFinishTask(final OpenBean bean) {
                                        DismissDialog();

                                        if (bean.getResultCode() == 0) {
                                            if (bean.getCityInfoList() != null && bean.getCityInfoList().size() > 0) {
                                                cityDialog = DialogUtils.ShowCityDialog(WithdrawNewActivity.this, bean.getCityInfoList(), new OnItemClickListener() {

                                                    @Override
                                                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                                        cityCode = bean.getCityInfoList().get(arg2).getCityCode();
                                                        cityName = bean.getCityInfoList().get(arg2).getCityName();

                                                        StringBuilder sb = new StringBuilder();
                                                        sb.append(provinceName);
                                                        sb.append("-");
                                                        sb.append(cityName);

                                                        addressTV.setText(sb.toString());

                                                        openTV.setText("");

                                                        cityDialog.dismiss();
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });

                                provinceDialog.dismiss();
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 选择开户行
     */
    private void SelectOpening() {
        if (this.addressTV.getText().toString().length() == 0) {
            ToastUtil.showShort(this, R.string.loan_please_select_card_address);
            return;
        }

        Intent openIntent = new Intent(this, OpeningBankAct.class);
        openIntent.putExtra("cityCode", cityCode);
        openIntent.putExtra("bankCode", bankCode);
        startActivityForResult(openIntent, Const.IntentCode.ADDCORD_TO_OPENING_INTENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.IntentCode.ADDCORD_TO_OPENING_INTENT_CODE) {
            if (data != null) {
                String bankSecondCode = data.getStringExtra("bankCode");
                bankSecondName = data.getStringExtra("bankName");
                openTV.setText(bankSecondName);

                BankApi.setBankInfo(WithdrawNewActivity.this, bankCode, cardNum, cityCode, cityName, bankSecondCode, bankSecondName, provinceCode, provinceName, new OnHttpTaskListener<BaseBean>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onTaskError(int resposeCode) {
                    }

                    @Override
                    public void onFinishTask(BaseBean bean) {
                        System.out.println("-------------- rs code = " + bean.getResultCode());
                    }
                });
            }
        }
    }

    private void commitWithdraw() {
        if (amountEdit.getText().toString().isEmpty()) {
            ToastUtil.showShort(this, R.string.loan_withdraw_money_tips);
            return;
        } else if (Double.valueOf(amountEdit.getText().toString()) <= 0) {
            ToastUtil.showLong(WithdrawNewActivity.this, "提现金额不能为0");
            return;
        }else if (Double.valueOf(amountEdit.getText().toString()) > Double.valueOf(totalAmount)) {
            ToastUtil.showLong(WithdrawNewActivity.this, "可提现额度不足");
            return;
        } else if (Double.valueOf(moneyReailty) > Double.valueOf(totalAmount)) {
            ToastUtil.showShort(this, "可提现金额不足，请重新输入");
            return;
        } else if (this.addressTV.getText().toString().length() == 0) {
            ToastUtil.showShort(this, R.string.loan_please_select_card_address);
            return;
        } else if (this.openTV.getText().toString().isEmpty()) {
            ToastUtil.showShort(this, R.string.loan_please_select_opening);
            return;
        }

        DialogUtils.showWithdrawInfoDialog(this, true, amountEdit.getText().toString(), moneyCost, moneyReailty, this.bankName, this.cardNum, provinceName + (TextUtils.isEmpty(cityName) ? "" : "," + cityName), bankSecondName, new OnTransPasswdEventListener() {

            @Override
            public void onConfirm(String transPasswd, Dialog dialog) {
                if (transPasswd.equals("")) {
                    ToastUtil.showShort(WithdrawNewActivity.this, "请输入交易密码");
                    return;
                }
                dialog.dismiss();
                withdraw(transPasswd);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    // 提现费用结算接口
    private void RequestCost(Editable str) {
        BankApi.WithdrawCost(WithdrawNewActivity.this, UserInfoPrefs.getUserId(), str.toString(), new OnHttpTaskListener<CostBean>() {

            @Override
            public void onStart() {
                ShowProDialog(WithdrawNewActivity.this);
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
                        moneyCost = DoubleUtil.getMoney(bean.getMoneyCost());
                        moneyReailty = DoubleUtil.getMoney(bean.getActuallyCharge());
                        costTV.setText(getString(R.string.loan_withdrawnew_cost_text, moneyCost, moneyReailty));
                    } else if (bean.getResultCode() == 9999) {
                        ToastUtil.showShort(WithdrawNewActivity.this, bean.getResultMsg());
                    }
                }
            }
        });
    }

    private void withdraw(String transPasswd) {
        BankApi.WithdrawCash(this, getValues(transPasswd), new OnHttpTaskListener<BaseBean>() {
            @Override
            public void onStart() {
                ShowProDialog(WithdrawNewActivity.this);
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onFinishTask(BaseBean bean) {
                DismissDialog();

                if (bean.getResultCode() == 313) {
                    UMengEvent.onEvent(WithdrawNewActivity.this, UMengEvent.EVENT_ID_WITHDRAW);

                    // 用户行为统计接口
                    StatisticsApi.getUserBehavior(WithdrawNewActivity.this, Const.EventName.WITHDRAW, Const.EventId.WITHDRAW, null, null, amountEdit.getText().toString());

                    ToastUtil.showShort(WithdrawNewActivity.this, bean.getResultMsg());
                    startActivity(new Intent(WithdrawNewActivity.this, CompleteActivity.class));
                    AppInfoPrefs.setWithDraw(true);
                    finish();
                } else {
                    ToastUtil.showShort(WithdrawNewActivity.this, bean.getResultMsg());
                }
                

            }
        });
    }

    /**
     * 请求数据传送的值
     */
    private HashMap<String, String> getValues(String transPasswd) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", UserInfoPrefs.getUserId());
        map.put("money", amountEdit.getText().toString());
        map.put("password", transPasswd);
        map.put("cardNumber", cardNum);
        map.put("servFee", this.moneyCost);
        return map;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loan_new_withdraw_address_layout: // 所在地
                SelectCity();
                break;
            case R.id.loan_new_withdraw_open_bank_layout: // 开户行
                SelectOpening();
                break;
            case R.id.loan_new_withdraw_update_text: // 修改开户行信息
                addressLayout.setVisibility(View.VISIBLE);
                openLayout.setVisibility(View.VISIBLE);
                updateTV.setVisibility(View.GONE);
                break;
            case R.id.loan_new_withdraw_enter_btn: // 确认提现
                commitWithdraw();
                break;
        }
    }

    class CustomClickSpan extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            // 用intent启动拨打电话
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + bankPhoneString));
            startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }
}
