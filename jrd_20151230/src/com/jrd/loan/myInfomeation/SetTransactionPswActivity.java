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
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class SetTransactionPswActivity extends BaseActivity implements OnClickListener {
    private WindowView windowView;
    private Button enterBtn;
    private EditText newPswEdi;
    private EditText repeatpswEdi;
    private TextView callTv;
    private Intent mIntent;
    private Context context;
    private String userId;
    private String transPwd;
    private PersonalInfo info;

    @Override
    protected int loadWindowLayout() {
        context = SetTransactionPswActivity.this;
        return R.layout.loan_activity_personal_set_transactionpsw;
    }

    @Override
    protected void initViews() {
        info = UserInfoPrefs.loadLastLoginUserProfile();
        enterBtn = (Button) findViewById(R.id.loan_personal_set_transactionpsw_senterbtn);
        newPswEdi = (EditText) findViewById(R.id.loan_personal_set_transactionpsw_inputedi);
        repeatpswEdi = (EditText) findViewById(R.id.loan_personal_set_transactionpsw_repeatedi);
        callTv = (TextView) findViewById(R.id.loan_personal_cu_loinpsw_callandtips_tv);
        callTv.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(R.string.loan_account_personal_set_transactionpsw);
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
            case R.id.loan_personal_set_transactionpsw_senterbtn:
                // 密码判空
                if (TextUtils.isEmpty(newPswEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_empty_transpsw);
                    return;
                }

                // 重复密码判空
                if (TextUtils.isEmpty(repeatpswEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_empty_transagain);
                    return;
                }

                // 校验密码是否一致
                if (!newPswEdi.getText().toString().trim().contains(repeatpswEdi.getText().toString().trim())) {
                    ToastUtil.showShort(context, R.string.loan_warning_different_num);
                    return;
                }
                // 校验密码长度
                if (newPswEdi.getText().toString().length() < 6 || newPswEdi.getText().toString().length() > 32) {
                    ToastUtil.showShort(context, R.string.loan_warning_wrong_loginpswformat);
                    return;
                }
                // 校验重复密码长度
                if (repeatpswEdi.getText().toString().length() < 6 || repeatpswEdi.getText().toString().length() > 32) {
                    ToastUtil.showShort(context, R.string.loan_warning_wrong_loginpswformat);
                    return;
                }
                userId = UserInfoPrefs.getUserId();
                transPwd = newPswEdi.getText().toString();
                setTransPsw();
                break;
            case R.id.loan_personal_cu_loinpsw_callandtips_tv:
                UserApi.ServiceCall(context);
                break;
            default:
                break;
        }
    }

    private void setTransPsw() {
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
                ToastUtil.showShort(context, bean.toString());
                DismissDialog();
                if (bean.getResultCode() == 0) {
                    ToastUtil.showShort(context, R.string.loan_complete_set);
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
                    finish();
                } else {
                    ToastUtil.showShort(context, bean.getResultMsg());
                }
            }
        });

    }

}
