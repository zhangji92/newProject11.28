package com.jrd.loan.current.pocket.rehcarge;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jrd.loan.MainActivity;
import com.jrd.loan.R;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.AccountBean;
import com.jrd.loan.myaccount.MyBankCardActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.DoubleUtil;
import com.jrd.loan.widget.WindowView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 充值成功
 *
 * @author Aaron
 */
public class RechargeSuccessActivity extends BaseActivity {
    OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loan_AccountDetailsBtn: // 账户明细
                    showAccountDetailsBtn();
                    break;
                case R.id.loan_ImmediateInvestmentBtn: // 立即投资
                    showImmediateInvestmentBtn();
                    break;
            }
        }
    };
    private Context context;
    private TextView TransactionNumberTV;// 交易单号
    private TextView ExchangeHourTV;// 交易时间
    private TextView CurrentBalanceTV;// 当前余额
    private Button AccountDetailsBtn;// 账户明细
    private Button ImmediateInvestmentBtn;// 立即投资
    private int getIntentCode; //从我的银行卡界面跳转过来的

    @Override
    protected int loadWindowLayout() {
        context = RechargeSuccessActivity.this;
        return R.layout.loan_recharge_success_layout;
    }

    @Override
    protected void initViews() {

        TransactionNumberTV = (TextView) findViewById(R.id.loan_TransactionNumberTV);
        ExchangeHourTV = (TextView) findViewById(R.id.loan_ExchangeHourTV);
        CurrentBalanceTV = (TextView) findViewById(R.id.loan_CurrentBalanceTV);

        AccountDetailsBtn = (Button) findViewById(R.id.loan_AccountDetailsBtn);
        AccountDetailsBtn.setOnClickListener(this.clickListener);
        ImmediateInvestmentBtn = (Button) findViewById(R.id.loan_ImmediateInvestmentBtn);
        ImmediateInvestmentBtn.setOnClickListener(this.clickListener);

        initData();

        RequestData();
    }

    /*
     * 初始化数据
     */
    private void initData() {
        TransactionNumberTV.setText(getIntent().getExtras().getString("userorderid", "").toString());
        ExchangeHourTV.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString());
    }

    /*
     * 网络请求
     */
    private void RequestData() {
        BankApi.GetMyAccount(context, new OnHttpTaskListener<AccountBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowDrawDialog(context);
            }

            @Override
            public void onFinishTask(AccountBean bean) {
                DismissDialog();
                if (bean != null && bean.getResultCode() == 0) {
                    CurrentBalanceTV.setText(DoubleUtil.getMoney(bean.getUserInfo().getAccountBalance()) + "元");
                }
            }
        });
    }

    /*
     * 账户明细
     */
    private void showAccountDetailsBtn() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("currMenu", 2);
        startActivity(intent);
    }

    /*
     * 立即投资
     */
    private void showImmediateInvestmentBtn() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("currMenu", 0);
        startActivity(intent);
    }

    @Override
    protected void initTitleBar() {
        getIntentCode = getIntent().getIntExtra("recharge", 0);
        WindowView windowView = (WindowView) findViewById(R.id.windowView);
        if (getIntentCode != 0) {
            windowView.setTitle("绑卡成功");
        } else {
            windowView.setTitle("充值成功");
        }
        windowView.setLeftButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FinishAct();
            }

        });
    }

    @Override
    public void onBackPressed() {

        FinishAct();
    }

    private void FinishAct() {
        if (getIntentCode != 0) {
            Intent intent = new Intent(RechargeSuccessActivity.this, MyBankCardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            finish();
        }
    }
}
