package com.jrd.loan.myInfomeation;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.api.UserApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.fragment.MyAccountFragment;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.CountDownUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class ResetTransactionPswActivity extends BaseActivity implements OnClickListener {
    private Button getcodeBtn;
    private EditText vcodeEdi;
    private Context context;
    private String telNum;
    private String userId;
    private Handler mHandler = new Handler();

    private int getIntentId;

    @Override
    protected int loadWindowLayout() {
        context = ResetTransactionPswActivity.this;
        return R.layout.loan_activity_personal_reset_transactionpsw;
    }

    @Override
    protected void initViews() {
        getIntentId = getIntent().getIntExtra(Const.Type.TRANS, 0);
        getcodeBtn = (Button) findViewById(R.id.loan_personal_reset_transactionpsw_sendvcode_btn);
        Button enterBtn = (Button) findViewById(R.id.loan_personal_reset_transactionpsw_enterbtn);
        TextView telnumTv = (TextView) findViewById(R.id.loan_personal_reset_transactionpsw_tv);
        vcodeEdi = (EditText) findViewById(R.id.loan_personal_reset_transactionpsw_vcodeedi);
        TextView callTv = (TextView) findViewById(R.id.loan_personal_cu_loinpsw_callandtips_tv);
        getcodeBtn.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        callTv.setOnClickListener(this);
        telNum = UserInfoPrefs.getTelNum();
        telnumTv.setText(MyAccountFragment.getProtectedMobile(telNum));
        userId = UserInfoPrefs.getUserId();
    }

    @Override
    protected void initTitleBar() {
        WindowView windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(R.string.loan_account_personal_reset_transactionpsw);
        windowView.setLeftButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loan_personal_reset_transactionpsw_enterbtn:
                // 验证码判空
                if (TextUtils.isEmpty(vcodeEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_empty_vcode);
                    return;
                }
                // 验证码位数
                if (vcodeEdi.getText().toString().length() < 6) {
                    ToastUtil.showShort(context, R.string.loan_warning_short_vcode);
                    return;
                }
                CheckVCode();
                break;
            case R.id.loan_personal_reset_transactionpsw_sendvcode_btn:
                getVcode();
                break;
            case R.id.loan_personal_cu_loinpsw_callandtips_tv:
                UserApi.ServiceCall(context);
                break;
            default:
                break;
        }
    }

    private void CheckVCode() {
        String vCode = vcodeEdi.getText().toString();
        UserApi.CheckVCode(context, userId, telNum, vCode, new OnHttpTaskListener<BaseBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowProDialog(context);
            }

            @Override
            public void onFinishTask(BaseBean bean) {
                DismissDialog();
                if (bean.getResultCode() == 0) {
                    Intent mIntent = new Intent(context, ResetTransactionPswSecondActivity.class);
                    mIntent.putExtra(Const.Type.TRANS, getIntentId);
                    startActivity(mIntent);
                } else {
                    ToastUtil.showShort(context, bean.getResultMsg());
                }
            }
        });
    }

    /**
     * 请求验证码
     */
    private void getVcode() {
        UserApi.GetVerifyCode(context, telNum, true, new OnHttpTaskListener<BaseBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowProDialog(context);
            }

            @Override
            public void onFinishTask(BaseBean bean) {
                DismissDialog();
                if (bean.getResultCode() == 0) {
                    new CountDownUtil(context, mHandler, getcodeBtn);
                } else {
                    ToastUtil.showShort(context, bean.getResultMsg());
                }
            }
        });
    }

}
