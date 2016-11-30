package com.jrd.loan.account;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.jrd.loan.bean.PersonalInfoBean;
import com.jrd.loan.constant.Const.Extra;
import com.jrd.loan.gesture.lock.GestureEditActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.FormatUtils;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class RegistIdCheckActivity extends BaseActivity implements OnClickListener {
    private WindowView windowView;
    private Button enterBtn;
    private EditText accountEdi;
    private EditText idnumEdi;
    private TextView jumpTV;
    private Intent mIntent;
    private Context context;
    private String userId;
    private String userName;
    private String idnum;

    @Override
    protected int loadWindowLayout() {
        context = RegistIdCheckActivity.this;
        return R.layout.loan_activity_regist_identityverfiy;
    }

    @Override
    protected void initViews() {
        accountEdi = (EditText) findViewById(R.id.loan_activity_regist_identityverfiy_name_edi);
        idnumEdi = (EditText) findViewById(R.id.loan_activity_regist_identityverfiy_idnum_edi);
        jumpTV = (TextView) findViewById(R.id.loan_activity_regist_identityverfiy_jump_tv);
        enterBtn = (Button) findViewById(R.id.loan_activity_regist_identityverfiy_enterbtn);
        userId = UserInfoPrefs.getUserId();
        SpannableStringBuilder builder = new SpannableStringBuilder(jumpTV.getText().toString());
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.loan_color_btn_normal));
        builder.setSpan(span, 4, jumpTV.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        jumpTV.setText(builder);
        idnum = idnumEdi.getText().toString();
        userName = accountEdi.getText().toString();
        enterBtn.setOnClickListener(this);
        jumpTV.setOnClickListener(this);
    }

    private void getUserInfo(String userId) {
        userId = UserInfoPrefs.getUserId();
        UserApi.GetUserInfo(context, userId, new OnHttpTaskListener<PersonalInfoBean>() {

            @Override
            public void onStart() {
                ShowProDialog(context);
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
                if (resposeCode == 0) {
                    ToastUtil.showShort(context, "服务器无响应,连接超时");
                }
            }

            @Override
            public void onFinishTask(PersonalInfoBean bean) {
                DismissDialog();
                if (bean.getResultCode() == 0) {
                    UserInfoPrefs.saveUserProfileInfo(bean.getPersonalInfo());
                    mIntent = new Intent(context, GestureEditActivity.class);
                    mIntent.putExtra(Extra.Select_ID, 1);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);
                    finish();
                    LogUtil.d("HttpRequest", "idNumberInfo-->" + UserInfoPrefs.loadLastLoginUserProfile().idNumberInfo);
                } else {
                    ToastUtil.showShort(context, bean.getResultMsg());
                }
            }
        });
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(R.string.loan_account_regist_identityverfiy);
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
            case R.id.loan_activity_regist_identityverfiy_jump_tv:
                getUserInfo(UserInfoPrefs.getUserId());
                break;
            case R.id.loan_activity_regist_identityverfiy_enterbtn:
                // 姓名判空
                if (TextUtils.isEmpty(accountEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_empty_realname);
                    return;
                }

                // 姓名格式判断
                if (!FormatUtils.isPersonName(accountEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_wrong_realname);
                    return;
                }

                // 身份证判空
                if (TextUtils.isEmpty(idnumEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_empty_idnum);
                    return;
                }

                // 身份证格式判断
                if (!FormatUtils.isIDCard(idnumEdi.getText().toString())) {
                    ToastUtil.showShort(context, R.string.loan_warning_wrong_idnumt);
                    return;
                }
                idnum = idnumEdi.getText().toString();
                userName = accountEdi.getText().toString();
                UserApi.Verified(context, userId, userName, idnum, new OnHttpTaskListener<BaseBean>() {

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
                            PersonalInfo personal = new PersonalInfo();
                            personal.setIdNumberInfo(idnum);
                            UserInfoPrefs.saveUserProfileInfo(personal);
                            mIntent = new Intent(context, GestureEditActivity.class);
                            mIntent.putExtra(Extra.Select_ID, 1);
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mIntent);
                            finish();
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
