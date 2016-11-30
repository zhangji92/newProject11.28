package com.jrd.loan.current.pocket.rehcarge;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.jrd.loan.R;
import com.jrd.loan.api.StatisticsApi;
import com.jrd.loan.api.UserApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.PersonalInfo;
import com.jrd.loan.constant.Const;
import com.jrd.loan.finance.ProjectIntroduceActivity;
import com.jrd.loan.gesture.lock.GestureEditActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.DialogUtils.OnButtonEventListener;
import com.jrd.loan.util.FormatUtils;
import com.jrd.loan.util.IDCardVerify;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class IdCheckActivity extends BaseActivity implements OnClickListener {

  private EditText accountEdi;
  private EditText idnumEdi;
  private Intent mIntent;
  private Context context;
  private String userId;
  private String userName;
  private String idnum;
  private PersonalInfo info;

  @Override
  protected int loadWindowLayout() {
    return R.layout.loan_activity_personal_identityverfiy;
  }

  @Override
  protected void initViews() {
    context = IdCheckActivity.this;
    info = UserInfoPrefs.loadLastLoginUserProfile();
    accountEdi = (EditText) findViewById(R.id.loan_personal_identityverfiy_name_edi);
    idnumEdi = (EditText) findViewById(R.id.loan_personal_identityverfiy_idnum_edi);
    Button enterBtn = (Button) findViewById(R.id.loan_personal_identityverfiy_enterbtn);
    userId = UserInfoPrefs.getUserId();
    enterBtn.setOnClickListener(this);

  }

  @Override
  protected void initTitleBar() {
    WindowView windowView = (WindowView) findViewById(R.id.windowView);
    windowView.setTitle(R.string.loan_account_regist_identityverfiy);
    windowView.setLeftButtonClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.loan_personal_identityverfiy_enterbtn:
        Commit();
        break;
      case R.id.loan_personal_cu_loinpsw_callandtips_tv:// 拨打电话
        UserApi.ServiceCall(context);
        break;
    }
  }

  /**
   * 提交
   */
  private void Commit() {
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
    if (!IDCardVerify.IDCardValidate(idnumEdi.getText().toString())) {
      ToastUtil.showShort(context, R.string.loan_warning_wrong_idnumt);
      return;
    }

    idnum = idnumEdi.getText().toString();
    userName = accountEdi.getText().toString();

    // 确认实名信息dialog
    DialogUtils.showIdCardInfoDialog(context, userName, idnum, new OnButtonEventListener() {

      @Override
      public void onConfirm() {
        CommitToSevice();
      }

      @Override
      public void onCancel() {

      }
    });


  }

  /**
   * 提交信息到服务器
   */
  private void CommitToSevice() {
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

        if (bean.getResultCode() == 0) {// 身份验证成功

          // 用户行为统计接口
          StatisticsApi.getUserBehavior(IdCheckActivity.this, Const.EventName.REALNAME, Const.EventId.REALNAME, null, null, null);

          PersonalInfo user = new PersonalInfo();
          user.idNumberInfo = idnum;
          user.idNumberFlag = "1";
          user.userName = userName;
          user.boundCardFlag = info.getBoundCardFlag();
          user.quickCardFlag = info.getQuickCardFlag();
          user.mobileNumber = info.getMobileNumber();
          user.transPwdInfo = info.getTransPwdInfo();
          user.transPwdFlag = info.getTransPwdFlag();
          UserInfoPrefs.saveUserProfileInfo(user);

          String comeFrom = getIntent().getStringExtra("come_from");
          if (comeFrom != null && comeFrom.equals("projectIntroduce")) {// 如果从标的详情进入身份验证界面
            Intent intent = new Intent(IdCheckActivity.this, ProjectIntroduceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("mockId", getIntent().getStringExtra("mockId"));
            startActivity(intent);
            finish();
          } else if (comeFrom != null && comeFrom.equals("register")) { // 注册界面跳转进来的
            Intent mIntent = new Intent(IdCheckActivity.this, GestureEditActivity.class);
            mIntent.putExtra(Const.Extra.Select_ID, 1);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mIntent);
          } else {// 进入个人中心
            finish();
          }
        } else {
          ToastUtil.showShort(context, bean.getResultMsg());
        }
      }
    });
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }
}
