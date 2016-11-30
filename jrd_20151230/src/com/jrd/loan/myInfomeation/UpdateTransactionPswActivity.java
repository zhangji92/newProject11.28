package com.jrd.loan.myInfomeation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.api.UserApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class UpdateTransactionPswActivity extends BaseActivity implements OnClickListener {
	private WindowView windowView;
	private Button enterBtn;
	private TextView fopswTv;
	private EditText orginalEdi;// 原始密码
	private EditText pswEdi;// 新密码
	private EditText repeatpswEdi;// 确认 新密码
	private Intent mIntent;
	private Context context;
	private TextView callTv;
	private String userId;
	private String transPwd;
	private String oldTransPwd;

	private InputMethodManager inputMethodManager;

	@Override
	protected int loadWindowLayout() {
		context = UpdateTransactionPswActivity.this;
		return R.layout.loan_activity_personal_update_transactionpsw;
	}

	@Override
	protected void initViews() {
		this.inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		enterBtn = (Button) findViewById(R.id.loan_account_update_transactionpsw_enterbtn);
		orginalEdi = (EditText) findViewById(R.id.loan_account_changeloginpsw_orginal_edi);
		pswEdi = (EditText) findViewById(R.id.loan_account_update_transactionpsw_new_edi);
		repeatpswEdi = (EditText) findViewById(R.id.loan_account_changeloginpsw_repeatpsw_edi);
		fopswTv = (TextView) findViewById(R.id.loan_account_update_transactionpsw_forget_tv);
		callTv = (TextView) findViewById(R.id.loan_personal_cu_loinpsw_callandtips_tv);
		callTv.setOnClickListener(this);
		enterBtn.setOnClickListener(this);
		fopswTv.setOnClickListener(this);
		fopswTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线

	}

	@Override
	protected void initTitleBar() {
		windowView = (WindowView) findViewById(R.id.windowView);
		windowView.setTitle(R.string.loan_account_personal_change_transactionpsw);
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
			case R.id.loan_account_update_transactionpsw_enterbtn:
				// 原密码判空
				if (TextUtils.isEmpty(orginalEdi.getText().toString())) {
					ToastUtil.showShort(context, R.string.loan_warning_input_transpsw_orginal);
					return;
				}

				// 校验交易密码长度
				if (orginalEdi.getText().toString().length() < 6 || orginalEdi.getText().toString().length() > 32) {
					ToastUtil.showShort(context, R.string.loan_warning_wrong_transpswformat);
					return;
				}

				// 新密码判空
				if (TextUtils.isEmpty(pswEdi.getText().toString())) {
					ToastUtil.showShort(context, R.string.loan_warning_input_transpsw_new);
					return;
				}

				// 校验交易密码长度
				if (pswEdi.getText().toString().length() < 6 || pswEdi.getText().toString().length() > 32) {
					ToastUtil.showShort(context, R.string.loan_warning_wrong_transpswformat);
					return;
				}

				// 重复密码判空
				if (TextUtils.isEmpty(repeatpswEdi.getText().toString())) {
					ToastUtil.showShort(context, R.string.loan_warning_input_transpsw_again);
					return;
				}

				// 校验交易密码长度
				if (repeatpswEdi.getText().toString().length() < 6 || repeatpswEdi.getText().toString().length() > 32) {
					ToastUtil.showShort(context, R.string.loan_warning_wrong_transpswformat);
					return;
				}

				// 校验密码是否一致
				if (!pswEdi.getText().toString().trim().equals(repeatpswEdi.getText().toString().trim())) {
					ToastUtil.showShort(context, R.string.loan_warning_different_num);
					return;
				}

				userId = UserInfoPrefs.getUserId();
				oldTransPwd = orginalEdi.getText().toString();
				transPwd = pswEdi.getText().toString();
				updateTransPsw();
				break;
			case R.id.loan_account_update_transactionpsw_forget_tv:// 忘记交易密码
				hideKeyboard();
				mIntent = new Intent(context, ResetTransactionPswActivity.class);
				startActivity(mIntent);
				break;
			default:
				break;
		}
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

	private void updateTransPsw() {
		UserApi.UpdateTransPsw(context, userId, oldTransPwd, transPwd, new OnHttpTaskListener<BaseBean>() {

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
				ToastUtil.showShort(context, bean.toString());
				if (bean.getResultCode() == 0) {
					ToastUtil.showShort(context, R.string.loan_complete_update);
					finish();
				} else {
					ToastUtil.showShort(context, bean.getResultMsg());
				}
			}
		});
	}
}
