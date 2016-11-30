package com.yoka.mrskin.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.managers.YKAdviceManager;
import com.yoka.mrskin.model.managers.YKAdviceManager.AdviceCallback;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.NetWorkUtil;
import com.yoka.mrskin.util.YKDeviceInfo;
import com.yoka.mrskin.util.YKUtil;
/**
 * 我的->设置->意见反馈
 * @author zlz
 * @Data 2016年7月6日
 */
public class SettingAdviceActivity extends BaseActivity implements OnClickListener{
	private static final String TAG = SettingAdviceActivity.class.getSimpleName();
	private EditText et_advice,et_contact_information;
	private TextView adviceCommit;
	private LinearLayout back;

	private String temp;//未限制

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_advice);
		initView();

	}
	/**
	 * 初始化组件 & 设置监听
	 */
	private void initView() {
		back = (LinearLayout) findViewById(R.id.settingadvice_back_layout);
		adviceCommit = (TextView) findViewById(R.id.setting_advice_commit);
		et_advice = (EditText) findViewById(R.id.ed_advice);
		et_contact_information = (EditText) findViewById(R.id.ed_contact_information);

		back.setOnClickListener(this);
		adviceCommit.setOnClickListener(this);
		adviceCommit.setEnabled(false);

		et_advice.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s.toString();

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				/*发送按钮是否可点击*/
				if(et_advice.getText().length() > 0){
					adviceCommit.setTextColor(getResources().getColor(R.color.red));
					adviceCommit.setEnabled(true);
				}else{
					adviceCommit.setTextColor(getResources().getColor(R.color.location_city_gps));
					adviceCommit.setEnabled(false);
				}

				/*是否超出255字符*/

				if(!TextUtils.isEmpty(temp)){
					String limitSubstring = YKUtil.getLimitSubstring(temp);
					if (!TextUtils.isEmpty(limitSubstring)) {
						if (!limitSubstring.equals(temp)) {
							Toast.makeText(SettingAdviceActivity.this, "字数已超过限制",
									Toast.LENGTH_SHORT).show();
							et_advice.setText(limitSubstring);
							et_advice.setSelection(limitSubstring.length());
						}

					}

				}
			}
		});


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settingadvice_back_layout://返回
			finish();

			break;
		case R.id.setting_advice_commit://提交反馈

			if(!NetWorkUtil.isNetworkAvailable(SettingAdviceActivity.this)){
				Toast.makeText(SettingAdviceActivity.this, R.string.intent_no, Toast.LENGTH_SHORT).show();
				return;
			}

			String advice = et_advice.getText().toString();
			String contact = et_contact_information.getText().toString();
			if(contact.equals("")||AppUtil.isEmail(contact) || AppUtil.isPhone(contact)){

				commitAdvice(advice,contact);
			}else{
				Toast.makeText(SettingAdviceActivity.this, "邮箱或手机号不正确~", Toast.LENGTH_SHORT).show();
			}




			break;

		default:
			break;
		}

	}
	/**
	 * 请求网络 提交反馈
	 * @param content 反馈内容
	 * @param contact 联系方式
	 */
	private void commitAdvice(String content,String contact) {
		/*用户token*/
		String authToken = "";
		if(null != YKCurrentUserManager.getInstance().getUser()){

			authToken = YKCurrentUserManager.getInstance().getUser().getToken();
		}
		/*设备ID*/
		String phoneToken = YKDeviceInfo.getClientID();


		YKAdviceManager.getInstance().commitAdvice(authToken, content, contact,phoneToken, new AdviceCallback() {

			@Override
			public void adviceCallback(YKResult result) {
				if(result.isSucceeded()){

					Toast.makeText(SettingAdviceActivity.this, "反馈成功", Toast.LENGTH_SHORT).show();
					finish();
				}else{
					Toast.makeText(SettingAdviceActivity.this, result.getMessage().toString(), Toast.LENGTH_SHORT).show();
				}

			}
		});

	}
}
