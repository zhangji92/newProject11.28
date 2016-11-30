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
import com.jrd.loan.fragment.MyAccountFragment;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.CountDownUtil;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class ChangeLoginPswActivity extends BaseActivity implements OnClickListener {
	private WindowView windowView;
	private Button getcodeBtn;
	private Button enterBtn;
	private EditText accountEdi;
	private EditText vcodeEdi;
	private EditText pswEdi;
	private EditText repeatpswEdi;
	private TextView callTv;
	private Intent mIntent;
	private Context context;
	private String telNum;
	private String vCode;
	private String password;
	private String userId;
	private boolean checkUser = true;
	private Handler mHandler = new Handler();

	@Override
	protected int loadWindowLayout() {
		context = ChangeLoginPswActivity.this;
		return R.layout.loan_activity_personal_cu_loginpsw;
	}

	@Override
	protected void initViews() {
		getcodeBtn = (Button) findViewById(R.id.loan_personal_cu_loinpsw_sendvcode_btn);
		enterBtn = (Button) findViewById(R.id.loan_personal_cu_loinpsw_enterbtn);
		accountEdi = (EditText) findViewById(R.id.loan_personal_cu_loinpsw_accountedi);
		vcodeEdi = (EditText) findViewById(R.id.loan_personal_cu_loinpsw_vcodeedi);

		// 输入登录密码
		pswEdi = (EditText) findViewById(R.id.loan_personal_cu_loinpsw_edi);

		// 再次输入登录密码
		repeatpswEdi = (EditText) findViewById(R.id.loan_personal_cu_loinpsw_repeatedi);
		callTv = (TextView) findViewById(R.id.loan_personal_cu_loinpsw_callandtips_tv);
		getcodeBtn.setOnClickListener(this);
		enterBtn.setOnClickListener(this);
		callTv.setOnClickListener(this);
		enterBtn.setText(R.string.loan_account_personal_change_psw);
		telNum = UserInfoPrefs.getTelNum();
		LogUtil.d("HttpRequest", "changpsw-->" + telNum.toString());
	}

	@Override
	protected void initTitleBar() {
		windowView = (WindowView) findViewById(R.id.windowView);
		windowView.setTitle(R.string.loan_account_personal_change_loginpsw);
		windowView.setLeftButtonClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		accountEdi.setText(MyAccountFragment.getProtectedMobile(UserInfoPrefs.getTelNum()));
		accountEdi.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.loan_personal_cu_loinpsw_sendvcode_btn:
				getCode();
				break;
			case R.id.loan_personal_cu_loinpsw_enterbtn:
				// 验证码判空
				if (TextUtils.isEmpty(vcodeEdi.getText().toString())) {
					ToastUtil.showShort(context, R.string.loan_warning_empty_vcode);
					return;
				}

				// 校验验证码长度
				if (vcodeEdi.getText().toString().length() < 6) {
					ToastUtil.showShort(context, R.string.loan_warning_short_vcode);
					return;
				}

				// 密码判空
				if (TextUtils.isEmpty(pswEdi.getText().toString())) {
					ToastUtil.showShort(context, R.string.loan_warning_empty_newpsw);
					return;
				}

				// 校验密码长度
				if (pswEdi.getText().toString().length() < 6 || pswEdi.getText().toString().length() > 32) {
					ToastUtil.showShort(context, R.string.loan_warning_wrong_loginpswformat);
					return;
				}

				// 重复密码判空
				if (TextUtils.isEmpty(repeatpswEdi.getText().toString())) {
					ToastUtil.showShort(context, R.string.loan_warning_empty_newagain);
					return;
				}

				// 校验密码长度
				if (repeatpswEdi.getText().toString().length() < 6 || repeatpswEdi.getText().toString().length() > 32) {
					ToastUtil.showShort(context, R.string.loan_warning_wrong_loginpswformat);
					return;
				}

				// 校验密码是否一致
				if (!(pswEdi.getText().toString().trim()).equals(repeatpswEdi.getText().toString().trim())) {
					ToastUtil.showShort(context, R.string.loan_warning_different_num);
					return;
				}

				userId = UserInfoPrefs.getUserId();
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

	/**
	 * 请求验证码
	 * */
	private void getCode() {
		UserApi.GetVerifyCode(context, telNum, checkUser, new OnHttpTaskListener<BaseBean>() {

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