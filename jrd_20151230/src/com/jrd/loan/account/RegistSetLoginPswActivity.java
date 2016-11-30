package com.jrd.loan.account;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.jrd.loan.R;
import com.jrd.loan.api.UserApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.current.pocket.rehcarge.BindBankCardActivity;
import com.jrd.loan.current.pocket.rehcarge.IdCheckActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class RegistSetLoginPswActivity extends BaseActivity implements OnClickListener {
    private WindowView windowView;
    private Button enterBtn;
    private EditText pswEdi;
    private Intent mIntent;
    private Context context;
    private String userId = "";
    private String psw;

    @Override
    protected int loadWindowLayout() {
        context = RegistSetLoginPswActivity.this;
        return R.layout.loan_activity_regist_setloginpsw;
    }

    @Override
    protected void initViews() {
        pswEdi = (EditText) findViewById(R.id.loan_regist_setloginpsw_edi);
        enterBtn = (Button) findViewById(R.id.loan_regist_setloginpsw_enterbtn);
        enterBtn.setOnClickListener(this);
        userId = UserInfoPrefs.getUserId();
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(R.string.loan_account_regist_setloginpsw);
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
            case R.id.loan_regist_setloginpsw_enterbtn:
                // 密码判空
                if (TextUtils.isEmpty(pswEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_empty_loginpsw);
                    return;
                }
                // 校验密码长度
                if (pswEdi.getText().toString().length() < 6 || pswEdi.getText().toString().length() > 32) {
                    ToastUtil.showShort(context, R.string.loan_warning_wrong_loginpswformat);
                    return;
                }
                psw = pswEdi.getText().toString();
                UserApi.RegistSetPsw(context, userId, psw, new OnHttpTaskListener<BaseBean>() {

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
                            mIntent = new Intent(context, IdCheckActivity.class);
                            mIntent.putExtra("come_from", "register");
                            startActivity(mIntent);
                        } else {
                            ToastUtil.showShort(context, bean.getResultMsg());
                        }
                    }
                });
                break;

            default:
                break;
        }
    }
}
