package com.jrd.loan.fragment;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.account.LoginActivity;
import com.jrd.loan.api.PocketApi;
import com.jrd.loan.base.BaseFragment;
import com.jrd.loan.bean.PocketBean;
import com.jrd.loan.current.pocket.PocketDetailsActivity;
import com.jrd.loan.current.pocket.rehcarge.IdCheckActivity;
import com.jrd.loan.myaccount.AccountSwitchToActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.KeyBoardUtil;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.LoanRefreshScrollView;
import com.jrd.loan.widget.LoanRefreshScrollView.OverScrollListener;
import com.jrd.loan.widget.RulerWheel;
import com.jrd.loan.widget.RulerWheel.OnWheelScrollListener;

import java.math.BigDecimal;

public class CurrentPocketFragment extends BaseFragment implements OnClickListener, OverScrollListener {

    private final static int MAX_MONEY = 200000;
    private final static int DEFAULT_MONEY = 1000;
    private View noNetworkLayoutView;
    private LoanRefreshScrollView mScrollView;
    // private RoundProgressBar mProgressBar;
    private TextView yearRateTV;
    private TextView castAmountTV; // 可投金额
    private EditText shiftToEdit; // 本次转入金额
    private TextView dayGoinTV; // 每日可赚
    private TextView bankGoinTV; // 银行同期可赚
    private Button enterBtn;
    private RulerWheel rulerWheel;
    private String projectId; // 活期口袋ID
    private double yearCost;// 年化利率

    private String leftInvestAmount;// 可投金额
    private OnWheelScrollListener wheelScrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(RulerWheel wheel) {
        }

        @Override
        public void onScrollingFinished(RulerWheel wheel) {
        }

        @Override
        public void onChanged(RulerWheel wheel, int oldValue, int newValue) {
            LogUtil.d("---------", newValue + "");
            if (newValue == 0) {
                shiftToEdit.setText("1");
            } else {
                shiftToEdit.setText(String.valueOf(newValue));
            }
            shiftToEdit.setSelection(String.valueOf(newValue).length());
        }
    };

    @Override
    protected int getLoadViewId() {
        return R.layout.loan_current_pocket_layout;
    }

    @Override
    protected void initView(View view) {
        this.noNetworkLayoutView = view.findViewById(R.id.noNetworkLayout);
        this.noNetworkLayoutView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // 尺子
        this.rulerWheel = (RulerWheel) view.findViewById(R.id.rulerWheel);
        this.rulerWheel.setValue(DEFAULT_MONEY, MAX_MONEY);
        this.rulerWheel.setScrollingListener(this.wheelScrollListener);
        mScrollView = (LoanRefreshScrollView) view.findViewById(R.id.loan_pocket_scrollView);
        mScrollView.setVisibility(View.INVISIBLE);
        mScrollView.setOverScrollListener(this);
        // mProgressBar = (RoundProgressBar) view.findViewById(R.id.loan_pocket_circlePro);
        yearRateTV = (TextView) view.findViewById(R.id.loan_pocket_rateTV);
        castAmountTV = (TextView) view.findViewById(R.id.loan_pocket_callCastTV);
        shiftToEdit = (EditText) view.findViewById(R.id.loan_pocket_shiftToEdit);

        this.shiftToEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int currentCount = Integer.parseInt(s.toString());
                    if (currentCount > MAX_MONEY) {
                        currentCount = MAX_MONEY;
                    }
                    rulerWheel.setValue(currentCount, MAX_MONEY);
                    IncomeMethod(s.toString());

                } else {
                    dayGoinTV.setText(R.string.loan_amount_default_text);
                    bankGoinTV.setText(R.string.loan_amount_default_text);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(s.toString()) && Integer.parseInt(s.toString()) > MAX_MONEY) {

                    shiftToEdit.setText(String.valueOf(MAX_MONEY));
                    shiftToEdit.setSelection(String.valueOf(MAX_MONEY).length());

                    ToastUtil.showShort(getActivity(), "每次最多转入200000元");
                }
            }
        });

        this.shiftToEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mScrollView.scrollTo(0, mScrollView.getHeight());
            }
        });
        dayGoinTV = (TextView) view.findViewById(R.id.loan_pocket_dayGoinTV);
        dayGoinTV.setText(R.string.loan_amount_default_text);
        bankGoinTV = (TextView) view.findViewById(R.id.loan_pocket_bankGoinTV);
        bankGoinTV.setText(R.string.loan_amount_default_text);
        enterBtn = (Button) view.findViewById(R.id.loan_pocket_enterBtn);
        enterBtn.setVisibility(View.INVISIBLE);
        enterBtn.setOnClickListener(this);

        view.findViewById(R.id.loan_pocket_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyBoardUtil.closeKeybord(bankGoinTV, getActivity());
            }
        });

    }

    private void IncomeMethod(String money) {
        /**
         * 预计每日可赚=本金*年利率*天数/365
         */

        double dayGoin = Integer.valueOf(money) * (yearCost / 100) / 365;

        BigDecimal bg = new BigDecimal(dayGoin);

        dayGoinTV.setText(String.valueOf(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));

        /**
         * 银行同期可赚：本金×实际天数×0.35%/360
         */

        double bankGoin = Integer.valueOf(money) * (0.35 / 100) / 360;

        BigDecimal bankBg = new BigDecimal(bankGoin);

        bankGoinTV.setText(String.valueOf(bankBg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
    }

    @Override
    protected void initData() {
        // 获取活期口袋详情
        PocketApi.obtainCurrentPocketFateToday(getActivity(), new OnHttpTaskListener<PocketBean>() {


            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
                noNetworkLayoutView.setVisibility(View.VISIBLE);
                noNetworkLayoutView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }

            @Override
            public void onStart() {
                ShowDrawDialog(getActivity());


                noNetworkLayoutView.setVisibility(View.GONE);
            }

            @Override
            public void onFinishTask(PocketBean bean) {
                DismissDialog();
                if (bean != null) {
                    if (bean.getResultCode() == 0) {
                        leftInvestAmount = bean.getLeftInvestAmount();

                        mScrollView.setVisibility(View.VISIBLE);
                        enterBtn.setVisibility(View.VISIBLE);
                        projectId = bean.getProjectId();
                        yearCost = Double.valueOf(bean.getAnnualRate());
                        yearRateTV.setText(bean.getAnnualRate());// 年化利率
                        castAmountTV.setText(String.format("可投金额:%s", bean.getLeftInvestAmount()));// 剩余额度


                        shiftToEdit.setText(String.valueOf(DEFAULT_MONEY));
                        shiftToEdit.setSelection(String.valueOf(DEFAULT_MONEY).length());
                        IncomeMethod(String.valueOf(DEFAULT_MONEY));
                    } else {
                        ToastUtil.showShort(getActivity(), bean.getResultMsg());
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loan_pocket_enterBtn:
                if (!UserInfoPrefs.isLogin()) {// 判断用户是否登录
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(R.anim.loan_bottom_to_top, R.anim.loan_default);
                } else if (UserInfoPrefs.loadLastLoginUserProfile().idNumberFlag.equals("0")) {// 用户是否身份认证
                    startActivity(new Intent(getActivity(), IdCheckActivity.class));
                } else{

                    String amount = shiftToEdit.getText().toString();
                    String annualRate = yearRateTV.getText().toString();

                    if (TextUtils.isEmpty(amount)) {
                        ToastUtil.showShort(getActivity(), R.string.loan_add_money);
                        return;
                    }

                    if (Double.parseDouble(amount) < 1) {
                        ToastUtil.showShort(getActivity(), R.string.loan_MinimumAmount);
                        shiftToEdit.setText("1");
                        shiftToEdit.setSelection(1);
                        return;
                    }

                    Intent intent = new Intent(getActivity(), AccountSwitchToActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("amount", amount);
                    intent.putExtra("leftInvestAmount", leftInvestAmount);
                    intent.putExtra("annualRate", annualRate);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void headerScroll() {
        // 向下滑动的监听事件
    }

    @Override
    public void footerScroll() {
        // 向上滑动的监听事件
        Intent intent = new Intent(getActivity(), PocketDetailsActivity.class);
        intent.putExtra("mockId", projectId);
        intent.putExtra("amount", shiftToEdit.getText().toString());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.loan_bottom_to_top, R.anim.loan_default);
    }
}
