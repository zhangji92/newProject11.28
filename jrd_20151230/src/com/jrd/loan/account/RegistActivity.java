package com.jrd.loan.account;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jrd.loan.MainActivity;
import com.jrd.loan.R;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.api.StatisticsApi;
import com.jrd.loan.api.UserApi;
import com.jrd.loan.api.WebApi.Account;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.RegisterBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.current.pocket.rehcarge.RechargeSuccessActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.umeng.UMengEvent;
import com.jrd.loan.util.CountDownUtil;
import com.jrd.loan.util.FormatUtils;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class RegistActivity extends BaseActivity implements OnClickListener {
  private Intent mIntent;
  private Context context;
  private Button getCodeBtn;
  private EditText numEdi;
  private EditText vcodeEdi;
  private EditText inCodeEdi;// 邀请码
  private TextView arrowTv;// 选填上下箭头
  private CheckBox checkCb;
  private View invitecodeView;// 邀请码布局

  private boolean isLogin = false;
  private final Handler mHandler = new Handler();

  @Override
  protected int loadWindowLayout() {
    context = RegistActivity.this;
    return R.layout.loan_activity_regist;
  }

  @Override
  protected void initViews() {
    numEdi = (EditText) findViewById(R.id.loan_account_regist_num_edi);
    vcodeEdi = (EditText) findViewById(R.id.loan_account_regist_vcode_edi);
    inCodeEdi = (EditText) findViewById(R.id.loan_account_regist_invitecode_edi);
    TextView loginTv = (TextView) findViewById(R.id.loan_account_regist_login_immediately_tv);
    checkCb = (CheckBox) findViewById(R.id.loan_account_regist_cb);
    TextView agreementTv = (TextView) findViewById(R.id.loan_account_regist_agreement_tv);
    arrowTv = (TextView) findViewById(R.id.loan_account_regist_invitecode_select_tv);
    invitecodeView = findViewById(R.id.loan_account_regist_invitecode_lin);
    Button enterBtn = (Button) findViewById(R.id.loan_account_regist_enterbtn);
    getCodeBtn = (Button) findViewById(R.id.loan_account_regist_sendvcode_btn);
    loginTv.setOnClickListener(this);
    agreementTv.setOnClickListener(this);
    arrowTv.setOnClickListener(this);
    getCodeBtn.setOnClickListener(this);
    enterBtn.setOnClickListener(this);
    SpannableStringBuilder builder = new SpannableStringBuilder(loginTv.getText().toString());
    ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.loan_color_btn_normal));
    builder.setSpan(span, 4, loginTv.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    loginTv.setText(builder);
  }

  @Override
  protected void initTitleBar() {
    WindowView windowView = (WindowView) findViewById(R.id.windowView);
    windowView.setTitle(R.string.loan_account_regist_title);
    windowView.setLeftButtonClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isLogin) {
          mIntent = new Intent(context, MainActivity.class);
          mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(mIntent);
          finish();
        } else {
          finish();
        }

      }
    });
  }

  @SuppressWarnings("static-access")
  @Override
  public void onClick(View v) {
    String telNum;
    String vCode;
    switch (v.getId()) {
      case R.id.loan_account_regist_enterbtn:
        // 手机号码判空
        if (TextUtils.isEmpty(numEdi.getText().toString())) {
          ToastUtil.showShort(context, R.string.loan_warning_empty_telnum);
          return;
        }
        // 校验手机号码格式
        if (!FormatUtils.isPhoneNumber(numEdi.getText().toString())) {
          ToastUtil.showShort(context, R.string.loan_warning_wrong_numformat);
          return;
        }
        // 验证码判空
        if (TextUtils.isEmpty(vcodeEdi.getText().toString())) {
          ToastUtil.showShort(context, R.string.loan_warning_empty_vcode);
          return;
        }

        // 验证码长度
        if (vcodeEdi.getText().toString().length() < 6) {
          ToastUtil.showShort(context, R.string.loan_warning_short_vcode);
          return;
        }

        if (!checkCb.isChecked()) {
          ToastUtil.showShort(context, R.string.loan_warning_same_notcheck);
          return;
        }
        telNum = numEdi.getText().toString();
        vCode = vcodeEdi.getText().toString();
        register(telNum, vCode);
        break;
      case R.id.loan_account_regist_login_immediately_tv:
        finish();
        break;
      case R.id.loan_account_regist_agreement_tv:// 服务协议
        mIntent = new Intent(context, WebViewActivity.class);
        mIntent.putExtra("htmlUrl", JrdConfig.getBaseUrl() + Account.GETREGAGREEMENTINFO);
        mIntent.putExtra("htmlTitle", getResources().getText(R.string.loan_account_regist_splice));
        startActivity(mIntent);
        break;
      case R.id.loan_account_regist_sendvcode_btn:// 获取验证码
        // 手机号码判空
        if (TextUtils.isEmpty(numEdi.getText().toString())) {
          ToastUtil.showShort(context, R.string.loan_warning_empty_telnum);
          return;
        }
        // 校验手机号码格式
        if (!FormatUtils.isPhoneNumber(numEdi.getText().toString())) {
          ToastUtil.showShort(context, R.string.loan_warning_wrong_numformat);
          return;
        }
        getCode();
        break;
      case R.id.loan_account_regist_invitecode_select_tv:
        if (invitecodeView.GONE == invitecodeView.getVisibility()) {
          arrowTv.setSelected(true);
          invitecodeView.setVisibility(View.VISIBLE);
        } else {
          invitecodeView.setVisibility(View.GONE);
          arrowTv.setSelected(false);
        }
        break;
      default:
        break;
    }
  }

  private void getCode() {
    UserApi.GetVerifyCode(context, numEdi.getText().toString(), false, new OnHttpTaskListener<BaseBean>() {

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
          new CountDownUtil(context, mHandler, getCodeBtn);
        } else {
          ToastUtil.showShort(context, bean.getResultMsg());
        }
      }
    });
  }

  private void register(String telNum, String vCode) {
    UserApi.RegistMobile(context, telNum, vCode, inCodeEdi.getText().toString(), new OnHttpTaskListener<RegisterBean>() {

      @Override
      public void onTaskError(int resposeCode) {
        DismissDialog();
      }

      @Override
      public void onStart() {
        ShowProDialog(context);
      }

      @Override
      public void onFinishTask(RegisterBean bean) {
        DismissDialog();

        if (bean.getResultCode() == 0) {
          UMengEvent.onEvent(RegistActivity.this, UMengEvent.EVENT_ID_REGISTER);

          isLogin = true;

          mIntent = new Intent(context, RegistSetLoginPswActivity.class);

          UserInfoPrefs.setTelNum(numEdi.getText().toString());
          UserInfoPrefs.setUserId(bean.getUserId());
          UserInfoPrefs.setSessionId(bean.getSessionid());
          startActivity(mIntent);

          // 用户行为统计接口
          StatisticsApi.getUserBehavior(RegistActivity.this, Const.EventName.REGISTER, Const.EventId.REGISTER, null, null, null);

        } else {
          ToastUtil.showShort(context, bean.getResultMsg());
        }
      }
    });
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    if (isLogin) {
      mIntent = new Intent(context, MainActivity.class);
      mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(mIntent);
      finish();
    } else {
      finish();
    }
  }
}
