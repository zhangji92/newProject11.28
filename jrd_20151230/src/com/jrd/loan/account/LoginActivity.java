package com.jrd.loan.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jrd.loan.MainActivity;
import com.jrd.loan.R;
import com.jrd.loan.SplashActivity;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.api.StatisticsApi;
import com.jrd.loan.api.UserApi;
import com.jrd.loan.api.WebApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.PersonalInfo;
import com.jrd.loan.bean.PersonalInfoBean;
import com.jrd.loan.bean.RegisterBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.constant.Const.Extra;
import com.jrd.loan.finance.ProjectIntroduceActivity;
import com.jrd.loan.gesture.lock.GestureEditActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.net.imageloader.ImageLoader;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.AppInfoUtil;
import com.jrd.loan.util.CountDownUtil;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.DialogUtils.OnButtonVerfifyListener;
import com.jrd.loan.util.FormatUtils;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.util.UuidUtil;
import com.jrd.loan.widget.ResizeLayout;

@SuppressLint("HandlerLeak")
public class LoginActivity extends BaseActivity implements OnClickListener {
  private EditText accountEdi;
  private EditText pswEdi;
  private EditText vcodeEdi;
  private ImageView vcodeImg;// 验证码
  private Context context;
  private String account;
  private String vCode;
  private String password;
  private static final int BIGGER = 1;
  private static final int SMALLER = 2;
  private static final int MSG_RESIZE = 1;

  private InputMethodManager inputMethodManager;

  private InputHandler mHandler = new InputHandler();

  class InputHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MSG_RESIZE: {
          if (msg.arg1 == BIGGER) {
            findViewById(R.id.loan_account_login_logo_img).setVisibility(View.VISIBLE);
            findViewById(R.id.loan_account_login_title_img).setVisibility(View.GONE);
          } else {
            findViewById(R.id.loan_account_login_logo_img).setVisibility(View.GONE);
            findViewById(R.id.loan_account_login_title_img).setVisibility(View.VISIBLE);
          }
        }
          break;
        default:
          break;
      }
      super.handleMessage(msg);
    }
  }

  @Override
  protected int loadWindowLayout() {
    context = LoginActivity.this;
    return R.layout.loan_activity_login_update;
  }

  @SuppressLint("NewApi")
  @Override
  protected void initViews() {
    this.inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    ResizeLayout layout = (ResizeLayout) findViewById(R.id.rootView);
    View enterBtn = findViewById(R.id.loan_account_login_enterbtn);
    accountEdi = (EditText) findViewById(R.id.loan_account_login_account_edi);
    pswEdi = (EditText) findViewById(R.id.loan_account_login_psw_edi);
    vcodeEdi = (EditText) findViewById(R.id.loan_account_regist_vcode_edi);
    vcodeImg = (ImageView) findViewById(R.id.loan_account_login_sendvcode_img);
    TextView forgetTv = (TextView) findViewById(R.id.loan_account_login_forgetpsw_tv);
    TextView registTv = (TextView) findViewById(R.id.loan_account_login_regist_immediately_tv);
    View closeView = findViewById(R.id.loan_account_login_close_lin);
    closeView.setOnClickListener(this);
    enterBtn.setOnClickListener(this);
    vcodeImg.setOnClickListener(this);
    forgetTv.setOnClickListener(this);
    registTv.setOnClickListener(this);
    layout.setOnResizeListener(new ResizeLayout.OnResizeListener() {
      @Override
      public void OnResize(int w, int h, int oldw, int oldh) {
        int change = BIGGER;
        if (h < oldh) {
          change = SMALLER;
        }
        Message msg = new Message();
        msg.what = 1;
        msg.arg1 = change;
        mHandler.sendMessage(msg);
      }
    });

    // 加载验证码图片
    getCodeImage();

    FormatUtils.inputFilterSpace(accountEdi);

  }

  @Override
  protected void initTitleBar() {

  }

  @SuppressLint("NewApi")
  @Override
  public void onClick(View v) {
    Intent mIntent;
    switch (v.getId()) {
      case R.id.loan_account_login_close_lin:
        finish();
        overridePendingTransition(0, R.anim.loan_top_to_buttom);
        break;
      case R.id.loan_account_login_enterbtn:// 登录成功
        login();
        break;
      case R.id.loan_account_login_sendvcode_img:
        vcodeImg.setBackground(null);
        getCodeImage();
        break;
      case R.id.loan_account_login_forgetpsw_tv:
        mIntent = new Intent(context, FindPswActivity.class);
        startActivity(mIntent);
        break;
      case R.id.loan_account_login_regist_immediately_tv:
        mIntent = new Intent(context, RegistActivity.class);
        startActivity(mIntent);
        break;
      default:
        break;
    }
  }

  /**
   * 登录
   */
  private void login() {
    this.hideKeyboard();

    // 手机号码判空
    if (TextUtils.isEmpty(accountEdi.getText().toString())) {
      ToastUtil.showShort(context, R.string.loan_warning_empty_account);
      return;
    }

    // 密码判空
    if (TextUtils.isEmpty(pswEdi.getText().toString())) {
      ToastUtil.showShort(context, R.string.loan_warning_empty_psw);
      return;
    }

    // 验证码判空
    if (TextUtils.isEmpty(vcodeEdi.getText().toString())) {
      ToastUtil.showShort(context, R.string.loan_warning_empty_vcode);
      return;
    }

    // 验证码长度
    if (vcodeEdi.getText().toString().length() < 4) {
      ToastUtil.showShort(context, R.string.loan_warning_short_vcode);
      return;
    }

    account = accountEdi.getText().toString();
    password = pswEdi.getText().toString();
    vCode = vcodeEdi.getText().toString();
    ToLoginRequest();
  }

  /**
   * 请求登录接口
   */
  private void ToLoginRequest() {
    UserApi.Login(context, account, password, vCode, new OnHttpTaskListener<RegisterBean>() {
      @Override
      public void onStart() {
        ShowProDialog(context);
      }

      @Override
      public void onTaskError(int resposeCode) {
        if (resposeCode == 0) {
          getCodeImage();
        }

        DismissDialog();
      }

      @Override
      public void onFinishTask(RegisterBean bean) {
        DismissDialog();
        final Handler mHandler = new Handler();
        if (bean.getResultCode() == 0) {
          final String userid = bean.getUserId();
          if (bean.getIsMobile().equals("0")) {// 未绑定手机
            BindMobileDialog(mHandler, userid);
          } else {
            UserInfoPrefs.setUserId(bean.getUserId());
            UserInfoPrefs.setSessionId(bean.getSessionid());
            UserInfoPrefs.setTelNum(bean.getMobileNumber());
            PersonalInfo info = new PersonalInfo();
            info.boundCardFlag = bean.getBoundCardFlag();
            info.transPwdInfo = bean.getTransPwdFlag();
            info.passwordInfo = bean.getPasswordInfo();
            if (bean.getIdNumberInfo().equals("")) {
              info.idNumberFlag = "0";
            } else {
              info.idNumberFlag = "1";
            }
            UserInfoPrefs.saveUserProfileInfo(info);

            LoginComplete();

          }
          // 用户行为统计接口
          StatisticsApi.getUserBehavior(LoginActivity.this, Const.EventName.LOGIN, Const.EventId.LOGIN, null, null, null);
        } else {
          vcodeEdi.setText("");
          ToastUtil.showShort(context, bean.getResultMsg());
          getCodeImage();
        }


      }
    });
  }

  /**
   * 获取验证码图片
   */
  private void getCodeImage() {
    StringBuilder builder = new StringBuilder(JrdConfig.getBaseUrl());
    builder.append(WebApi.Account.GETLOGINVCODE).append("?deviceid=").append(UuidUtil.getUUID());
    ImageLoader.loadGraphicCodeImage(vcodeImg, builder.toString());
  }

  /**
   * 登录成功以后的界面跳转
   */
  private void LoginComplete() {
    String comeFrom = getIntent().getStringExtra("come_from");

    if (comeFrom != null) {
      if (comeFrom.equals("projectIntroduce")) {// 如果从标的详情进入登录界面
        if (AppInfoPrefs.isSetGestureLock(LoginActivity.this)) {
          Intent intent = new Intent(LoginActivity.this, ProjectIntroduceActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("mockId", getIntent().getStringExtra("mockId"));
          startActivity(intent);
        } else {
          Intent intent = new Intent(LoginActivity.this, GestureEditActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("mockId", getIntent().getStringExtra("mockId"));
          intent.putExtra("come_from", comeFrom);
          startActivity(intent);
        }
      } else if (comeFrom.equals("h5_screen")) {// 从h5界面点击登录
        if (AppInfoPrefs.isSetGestureLock(LoginActivity.this)) {
          StringBuffer buffer = new StringBuffer();

          String url = getIntent().getStringExtra("htmlUrl");

          buffer.append(url);

          if (url.contains("?")) {// url中包含了?
            buffer.append("&channel=").append(AppInfoUtil.getChannel());
          } else {
            buffer.append("?channel=").append(AppInfoUtil.getChannel());
          }

          buffer.append("&userId=").append(UserInfoPrefs.getUserId());

          Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("htmlUrl", buffer.toString());
          intent.putExtra("htmlTitle", getIntent().getStringExtra("htmlTitle"));
          startActivity(intent);
        } else {
          Intent intent = new Intent(LoginActivity.this, GestureEditActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("come_from", comeFrom);
          intent.putExtra("htmlUrl", getIntent().getStringExtra("htmlUrl"));
          intent.putExtra("htmlTitle", getIntent().getStringExtra("htmlTitle"));
          startActivity(intent);
        }
      }

      finish();
    } else {
      if (AppInfoPrefs.isSetGestureLock(LoginActivity.this)) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(Extra.Select_Info, 2);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      } else {
        Intent intent = new Intent(LoginActivity.this, GestureEditActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      }

      finish();
    }
  }

  /**
   * 绑定手机的dialog
   */
  private void BindMobileDialog(final Handler mHandler, final String userid) {
    DialogUtils.ShowVerifyDialog(context, new OnButtonVerfifyListener() {
      @Override
      public void onConfirm(String phone, String verify) {
        BindMobileRequest(userid, phone, verify);
      }

      @Override
      public void onCancel() {
        finish();
      }

      @Override
      public void onSendVerify(String phone, final Button sendBtn) {
        RequestCode(mHandler, phone, sendBtn);
      }

    });
  }

  /**
   * 获取验证码接口
   */
  private void RequestCode(final Handler mHandler, String phone, final Button sendBtn) {
    UserApi.GetVerifyCode(context, phone, false, new OnHttpTaskListener<BaseBean>() {
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
          new CountDownUtil(context, mHandler, sendBtn);
        } else {
          ToastUtil.showShort(context, bean.getResultMsg());
        }
      }
    });
  }

  /**
   * 请求绑定手机接口
   */
  private void BindMobileRequest(final String userId, String phone, String verify) {
    UserApi.BindMobile(context, userId, phone, verify, new OnHttpTaskListener<BaseBean>() {

      @Override
      public void onStart() {
        ShowProDialog(context);
      }

      @Override
      public void onTaskError(int resposeCode) {
        DismissDialog();
      }

      @Override
      public void onFinishTask(BaseBean bean) {
        DismissDialog();
        if (bean.getResultCode() == 0) {
          getUserInfo();
        } else {
          ToastUtil.showShort(context, bean.getResultMsg());
        }
      }
    });
  }

  private void getUserInfo() {
    UserApi.GetUserInfo(context, UserInfoPrefs.getUserId(), new OnHttpTaskListener<PersonalInfoBean>() {

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
          LoginComplete();
        } else {
          ToastUtil.showShort(context, bean.getResultMsg());
        }
      }
    });
  }

  /**
   * 隐藏软键盘
   */
  private void hideKeyboard() {
    if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
      if (getCurrentFocus() != null) {
        this.inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
      }
    }
  }

  @Override
  public void onBackPressed() {
    finish();
    overridePendingTransition(0, R.anim.loan_top_to_buttom);
  }
}
