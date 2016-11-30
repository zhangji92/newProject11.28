package com.jrd.loan.myInfomeation;

import android.content.Context;
import android.content.Intent;
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
import com.jrd.loan.bean.PersonalInfo;
import com.jrd.loan.constant.Const;
import com.jrd.loan.constant.Const.Type;
import com.jrd.loan.myaccount.WithdrawActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class ResetTransactionPswSecondActivity extends BaseActivity implements OnClickListener {
    private WindowView windowView;
    private Button enterBtn;
    private EditText transpswEdi;
    private EditText repeatpswEdi;
    private TextView callTv;
    private Context context;
    private String userId;
    private String transPwd;
    private String oldTransPwd;
    private int getIntentId;
    private PersonalInfo info;

    @Override
    protected int loadWindowLayout() {
        context = ResetTransactionPswSecondActivity.this;
        return R.layout.loan_activity_personal_reset_transactionpsw_s;
    }

    @Override
    protected void initViews() {
        info = UserInfoPrefs.loadLastLoginUserProfile();
        getIntentId = getIntent().getIntExtra(Const.Type.TRANS, 0);
        enterBtn = (Button) findViewById(R.id.loan_personal_reset_transactionpsw_senterbtn);
        transpswEdi = (EditText) findViewById(R.id.loan_personal_reset_transactionpsw_inputedi);
        repeatpswEdi = (EditText) findViewById(R.id.loan_personal_reset_transactionpsw_srepeatedi);
        callTv = (TextView) findViewById(R.id.loan_personal_cu_loinpsw_callandtips_tv);
        callTv.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
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
            case R.id.loan_personal_cu_loinpsw_callandtips_tv:
                UserApi.ServiceCall(context);
                break;
            case R.id.loan_personal_reset_transactionpsw_senterbtn:
                // 新密码判空
                if (TextUtils.isEmpty(transpswEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_input_transpsw_new);
                    return;
                }

                // 校验新密码长度
                if (transpswEdi.getText().toString().length() < 6 || transpswEdi.getText().toString().length() > 32) {
                    ToastUtil.showShort(context, R.string.loan_warning_wrong_loginpswformat);
                    return;
                }

                // 重复密码判空
                if (TextUtils.isEmpty(repeatpswEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_input_transpsw_again);
                    return;
                }

                // 校验重复密码长度
                if (repeatpswEdi.getText().toString().length() < 6 || repeatpswEdi.getText().toString().length() > 32) {
                    ToastUtil.showShort(context, R.string.loan_warning_wrong_loginpswformat);
                    return;
                }

                // 校验密码是否一致
                if (!transpswEdi.getText().toString().trim().contains(repeatpswEdi.getText().toString().trim())) {
                    ToastUtil.showShort(context, R.string.loan_warning_different_num);
                    return;
                }

                userId = UserInfoPrefs.getUserId();
                oldTransPwd = transpswEdi.getText().toString();
                transPwd = repeatpswEdi.getText().toString();
                updateTransPsw();
                break;
            default:
                break;
        }
    }

    private void updateTransPsw() {
        UserApi.SetTransPsw(context, userId, transPwd, new OnHttpTaskListener<BaseBean>() {

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
                    ToastUtil.showShort(context, R.string.loan_complete_reset);


                    PersonalInfo user = new PersonalInfo();
                    user.idNumberInfo = info.getIdNumberInfo();
                    if (info.getIdNumberInfo().equals("")) {
                        user.idNumberFlag = "0";
                    } else {
                        user.idNumberFlag = "1";
                    }
                    user.userName = info.getUserName();
                    user.boundCardFlag = info.getBoundCardFlag();
                    user.quickCardFlag = info.getQuickCardFlag();
                    user.mobileNumber = info.getMobileNumber();
                    user.transPwdInfo = transPwd;
                    user.transPwdFlag = "1";
                    UserInfoPrefs.saveUserProfileInfo(user);


                    if (getIntentId == Const.Type.WITHDRAW_TO_TRANS) {
                        Intent mIntent = new Intent(context, WithdrawActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mIntent);
                        finish();
                    } else {
                        Intent mIntent = new Intent(context, PersonalActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mIntent);
                        finish();
                    }
                } else {
                    ToastUtil.showShort(context, bean.getResultMsg());
                }
            }
        });
    }
}
