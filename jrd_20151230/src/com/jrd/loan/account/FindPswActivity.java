package com.jrd.loan.account;

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
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.CountDownUtil;
import com.jrd.loan.util.FormatUtils;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class FindPswActivity extends BaseActivity implements OnClickListener {
    private Button getcodeBtn;
    private EditText accountEdi;
    private EditText vcodeEdi;
    private EditText pswEdi;
    private EditText repeatpswEdi;
    private Intent mIntent;
    private Context context;
    private Handler mHandler = new Handler();

    @Override
    protected int loadWindowLayout() {
        context = FindPswActivity.this;
        return R.layout.loan_activity_personal_cu_loginpsw;
    }

    @Override
    protected void initViews() {
        getcodeBtn = (Button) findViewById(R.id.loan_personal_cu_loinpsw_sendvcode_btn);
        Button enterBtn = (Button) findViewById(R.id.loan_personal_cu_loinpsw_enterbtn);
        accountEdi = (EditText) findViewById(R.id.loan_personal_cu_loinpsw_accountedi);
        vcodeEdi = (EditText) findViewById(R.id.loan_personal_cu_loinpsw_vcodeedi);
        pswEdi = (EditText) findViewById(R.id.loan_personal_cu_loinpsw_edi);
        repeatpswEdi = (EditText) findViewById(R.id.loan_personal_cu_loinpsw_repeatedi);
        TextView callTv = (TextView) findViewById(R.id.loan_personal_cu_loinpsw_callandtips_tv);
        getcodeBtn.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        callTv.setOnClickListener(this);
    }

    @Override
    protected void initTitleBar() {
        WindowView windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(R.string.loan_account_login_resetpsw);
        windowView.setLeftButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        String telNum;
        String vCode;
        String password;
        String userId = "";
        switch (v.getId()) {
            case R.id.loan_personal_cu_loinpsw_sendvcode_btn:
                // 手机号码判空
                if (TextUtils.isEmpty(accountEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_empty_telnum);
                    return;
                }
                // 校验手机号码格式
                if (!FormatUtils.isPhoneNumber(accountEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_wrong_numformat);
                    return;
                }
                getCode(accountEdi.getText().toString());
                break;
            case R.id.loan_personal_cu_loinpsw_enterbtn:
                // 手机号码判空
                if (TextUtils.isEmpty(accountEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_empty_telnum);
                    return;
                }

                // 验证码判空
                if (TextUtils.isEmpty(vcodeEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_empty_vcode);
                    return;
                }

                // 密码判空
                if (TextUtils.isEmpty(pswEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_empty_newpsw);
                    return;
                }

                // 重复密码判空
                if (TextUtils.isEmpty(repeatpswEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_empty_newagain);
                    return;
                }

                // 校验手机号码格式
                if (!FormatUtils.isPhoneNumber(accountEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_wrong_numformat);
                    return;
                }

                // 校验密码长度
                if (pswEdi.getText().toString().length() < 6 || pswEdi.getText().toString().length() > 32) {
                    ToastUtil.showShort(context, R.string.loan_warning_wrong_loginpswformat);
                    return;
                }
                // 校验密码是否一致
                if (!pswEdi.getText().toString().trim().contains(repeatpswEdi.getText().toString().trim())) {
                    ToastUtil.showShort(context, R.string.loan_warning_different_num);
                    return;
                }

                telNum = accountEdi.getText().toString();
                password = pswEdi.getText().toString();
                vCode = vcodeEdi.getText().toString();
                UserApi.UpdateLoginPsw(context, userId, vCode, password, telNum, new OnHttpTaskListener<BaseBean>() {

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
                            ToastUtil.showShort(context, R.string.loan_complete_update);
                            mIntent = new Intent(context, LoginActivity.class);
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mIntent);
                            finish();
                        } else {
                            ToastUtil.showShort(context, bean.getResultMsg());
                        }
                    }
                });
                break;
            case R.id.loan_personal_cu_loinpsw_callandtips_tv:
                UserApi.ServiceCall(context);
                break;
            default:
                break;
        }
    }

    private void getCode(String telnum) {
        UserApi.GetVerifyCode(context, telnum, true, new OnHttpTaskListener<BaseBean>() {

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
