package com.jrd.loan.myaccount;

import java.math.BigDecimal;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.api.WebApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.bean.AccountBean;
import com.jrd.loan.constant.Const.IntentCode;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.DoubleUtil;
import com.jrd.loan.util.FormatUtils;
import com.jrd.loan.util.KeyBoardUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

/**
 * 充值界面
 *
 * @author Luke
 */
public class RechargeActivity extends BaseActivity {

    private Context mContext;
    private WindowView windowView;
    private EditText moneyEdit;
    private Button enterBtn;
    private TextView balanceTV;
    private int amount;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_recharge;
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setVisibility(View.INVISIBLE);
        windowView.setTitle(getResources().getString(R.string.loan_recharge));
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
        mContext = RechargeActivity.this;
        moneyEdit = (EditText) findViewById(R.id.loan_recharge_Edit);
        FormatUtils.setPricePointTwo(moneyEdit);
        enterBtn = (Button) findViewById(R.id.loan_recharge_Btn);
        balanceTV = (TextView) findViewById(R.id.loan_recharge_balance_tv);
        enterBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ToCharge();
            }
        });
        RequestValues();
    }

    /**
     * 获取接口数据
     */
    private void RequestValues() {
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
                DismissDialog();
                if (bean.getResultCode() == 0) {
                    windowView.setVisibility(View.VISIBLE);
                    balanceTV.setText(DoubleUtil.getMoney(bean.getUserInfo().getAccountBalance()));
                } else {
                    ToastUtil.showShort(mContext, bean.getResultMsg());
                }
            }
        });
    }

    /**
     * 充值
     */
    private void ToCharge() {
        KeyBoardUtil.closeKeybord(balanceTV, mContext);
        if (moneyEdit.getText().toString().isEmpty()) {
            ToastUtil.showShort(mContext, R.string.loan_please_input_money);
            return;
        } else if (Float.valueOf(moneyEdit.getText().toString()) < 1) {
            ToastUtil.showShort(mContext, R.string.loan_recharge_hint);
            return;
        }
        BigDecimal bd = new BigDecimal(moneyEdit.getText().toString());
        // amount 是以分为单位的
        amount = (int) (bd.doubleValue() * 100);
        String url = JrdConfig.getBaseUrl() + WebApi.PayApi.TORECHARGE + "?platform=0&userId=" + UserInfoPrefs.getUserId() + "&money=" + amount + "&authorization=" + UserInfoPrefs.getSessionId();
        Intent reIntent = new Intent(mContext, WebViewActivity.class);
        reIntent.putExtra("htmlUrl", url);
        reIntent.putExtra("htmlTitle", windowView.getTitle());
        startActivityForResult(reIntent, IntentCode.ADDCORD_TO_WEBVIEW_INTENT_CODE);
        // 调用浏览器
        // Intent intent = new Intent(Intent.ACTION_VIEW);
        // intent.setData(Uri.parse(url));
        // startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }
}
