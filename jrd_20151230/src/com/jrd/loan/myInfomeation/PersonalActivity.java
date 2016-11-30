package com.jrd.loan.myInfomeation;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrd.loan.MainActivity;
import com.jrd.loan.R;
import com.jrd.loan.api.UserApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.PersonalInfoBean;
import com.jrd.loan.current.pocket.rehcarge.IdCheckActivity;
import com.jrd.loan.fragment.MyAccountFragment;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.DialogUtils.OnButtonEventListener;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class PersonalActivity extends BaseActivity implements OnClickListener {
    public static int TRANSPSW_STATE = 0;
    private WindowView windowView;
    private Intent mIntent;
    private Context context;
    private TextView telnumTv;
    private TextView invitecodeTv;// 邀请码
    private TextView securitylevelTv;// 安全等级
    private TextView securityTipsTv;// 安全提示
    private TextView idcheckedTv;// 身份验证
    private TextView idnotTv;// 身份未验证
    private TextView loginpswTv;
    private TextView transpswTv;// 交易密码
    private TextView notranspswTv;// 未设置交易密码
    private View vcodeView;// 邀请码布局
    private View idcheckView;// 身份验证布局
    private String userId;

    public PersonalActivity() {
    }

    @Override
    protected int loadWindowLayout() {
        context = PersonalActivity.this;
        return R.layout.loan_activity_personal;
    }

    @Override
    protected void initViews() {
        telnumTv = (TextView) findViewById(R.id.loan_personal_tel_tv);
        invitecodeTv = (TextView) findViewById(R.id.loan_personal_invitecode_tv);
        securitylevelTv = (TextView) findViewById(R.id.loan_personal_securitylevel_tv);
        securityTipsTv = (TextView) findViewById(R.id.loan_personal_securitylevetv);
        idcheckedTv = (TextView) findViewById(R.id.loan_personal_idcheack_tv);
        idnotTv = (TextView) findViewById(R.id.loan_personal_idcheack_notchecktv);
        loginpswTv = (TextView) findViewById(R.id.loan_personal_loginpsw_tv);
        transpswTv = (TextView) findViewById(R.id.loan_personal_transactionpsw_tv);
        notranspswTv = (TextView) findViewById(R.id.loan_personal_transactionpsw_notsettv);
        idcheckView = findViewById(R.id.loan_personal_checklin);
        View lodgingsView = findViewById(R.id.loan_personal_loginpswlin);
        View transposeView = findViewById(R.id.loan_personal_transactionpswlin);
        vcodeView = findViewById(R.id.loan_personal_vcode_rel);
        Button quiteBtn = (Button) findViewById(R.id.loan_personal_quitebtn);
        idcheckView.setOnClickListener(this);
        transposeView.setOnClickListener(this);
        lodgingsView.setOnClickListener(this);
        quiteBtn.setOnClickListener(this);

    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setVisibility(View.INVISIBLE);
        windowView.setTitle(R.string.loan_account_personal_title);
        windowView.setLeftButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userId = UserInfoPrefs.loadLastLoginUserProfile().getUserId();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo(userId);
    }

    private void getUserInfo(String userId) {
        UserApi.GetUserInfo(context, userId, new OnHttpTaskListener<PersonalInfoBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowDrawDialog(context);
            }

            @Override
            public void onFinishTask(PersonalInfoBean bean) {
                DismissDialog();
                if (bean.getResultCode() == 0) {
                    windowView.setVisibility(View.VISIBLE);
                    telnumTv.setText(MyAccountFragment.getProtectedMobile(bean.getPersonalInfo().getMobileNumber()));// 手机号码

//                    PersonalInfo info = new PersonalInfo();
//                    info.mobileNumber = bean.getPersonalInfo().getMobileNumber();
//                    info.idNumberInfo = bean.getPersonalInfo().getIdNumberInfo();
//                    info.transPwdFlag = bean.getPersonalInfo().getTransPwdFlag();
//                    info.passwordFlag = bean.getPersonalInfo().getPasswordFlag();
//                    if (bean.getPersonalInfo().idNumberInfo.equals("")) {
//                        info.idNumberFlag = "0";
//                    } else {
//                        info.idNumberFlag = "1";
//                    }
//                    UserInfoPrefs.saveUserProfileInfo(info);

                    if (bean.getPersonalInfo().getInviteCode().equals("") || bean.getPersonalInfo().getInviteCode() == null) {
                        vcodeView.setVisibility(View.GONE);// 邀请码提示
                        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) vcodeView.getLayoutParams(); // 取控件textView当前的布局参数
                        linearParams.height = 75;
                        vcodeView.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件
                    } else {
                        vcodeView.setVisibility(View.VISIBLE);// 邀请码提示
                        invitecodeTv.setText(" " + bean.getPersonalInfo().getInviteCode());
                    }

                    securitylevelTv.setText(bean.getPersonalInfo().getSecurityLevel());// 安全等级

                    if (bean.getPersonalInfo().getIdNumberFlag().equals("0")) {// 身份未认证
                        idcheckView.setBackgroundResource(R.drawable.loan_white_to_gray_selector);
                        idcheckedTv.setVisibility(View.GONE);
                        idnotTv.setVisibility(View.VISIBLE);
                        idnotTv.setText(R.string.loan_setting_state_notset);// 身份未验证
                    } else {// 身份已验证
                        idcheckedTv.setText(bean.getPersonalInfo().getIdNumberInfo());// 身份验证
                        idcheckView.setBackgroundColor(getResources().getColor(R.color.loan_color_white));
						idcheckView.setOnClickListener(null);
                        idcheckedTv.setVisibility(View.VISIBLE);
                        idnotTv.setVisibility(View.GONE);
                        securityTipsTv.setVisibility(View.GONE);

                        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) securityTipsTv.getLayoutParams(); // 取控件textView当前的布局参数
                        linearParams.height = 75;
                        securityTipsTv.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件
                    }

                    if (bean.getPersonalInfo().getPasswordFlag().equals("1")) {
                        loginpswTv.setText(R.string.loan_setting_state_setted);// 登录密码
                    } else {
                        loginpswTv.setText(R.string.loan_setting_state_notset);// 登录密码
                    }

                    if (bean.getPersonalInfo().transPwdFlag.equals("1")) {
                        TRANSPSW_STATE = 1;
                        transpswTv.setText(R.string.loan_setting_state_setted);// 支付密码
                        transpswTv.setVisibility(View.VISIBLE);
                        notranspswTv.setVisibility(View.GONE);
                    } else {
                        TRANSPSW_STATE = 0;
                        transpswTv.setVisibility(View.GONE);
                        notranspswTv.setVisibility(View.VISIBLE);
                        notranspswTv.setText(R.string.loan_setting_state_notset);// 支付密码未设置
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loan_personal_quitebtn:// 退出登录
                DialogUtils.showTextDialog(context, R.string.loan_base_cancel, R.string.loan_base_quit, R.string.loan_personal_logout, new OnButtonEventListener() {

                    @Override
                    public void onConfirm() {

                    }

                    @Override
                    public void onCancel() {
                        logout();
                    }
                });
                break;
            case R.id.loan_personal_checklin:// 身份验证
//				mIntent = new Intent(context, BindBankCardActivity.class);
                mIntent = new Intent(context, IdCheckActivity.class);
                startActivity(mIntent);
                break;
            case R.id.loan_personal_loginpswlin:// 修改登录密码
                mIntent = new Intent(context, ChangeLoginPswActivity.class);
                startActivity(mIntent);
                break;
            case R.id.loan_personal_transactionpswlin:// 修改/设置交易密码
                if (TRANSPSW_STATE == 1) {
                    transpswTv.setText(R.string.loan_setting_state_setted);// 交易密码已设置
                    mIntent = new Intent(context, UpdateTransactionPswActivity.class);
                    startActivity(mIntent);
                } else {
                    TRANSPSW_STATE = 0;
                    notranspswTv.setText(R.string.loan_setting_state_notset);// 交易密码未设置
                    mIntent = new Intent(context, SetTransactionPswActivity.class);
                    startActivity(mIntent);
                }
                break;
            default:
                break;
        }
    }

    private void logout() {
        UserApi.LogOut(context, new OnHttpTaskListener<BaseBean>() {

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
                    UserInfoPrefs.clearUserInfo();
                    ToastUtil.showShort(context, "退出成功");

                    mIntent = new Intent(context, MainActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);
                    finish();
                } else {
                    ToastUtil.showShort(context, bean.getResultMsg());
                }
            }
        });
    }
}
