package com.jrd.loan.gesture.lock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jrd.loan.MainActivity;
import com.jrd.loan.R;
import com.jrd.loan.account.RegistCompleteActivity;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.constant.Const.Extra;
import com.jrd.loan.constant.Const.Type;
import com.jrd.loan.finance.ProjectIntroduceActivity;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.AppInfoUtil;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.widget.GestureContentView;
import com.jrd.loan.widget.GestureContentView.GestureCallBack;
import com.umeng.analytics.MobclickAgent;

/**
 * 手势密码设置界面
 */
public class GestureEditActivity extends Activity implements OnClickListener {
	/** 手机号码 */
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";

	/** 意图 */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";

	/** 首次提示绘制手势密码，可以选择跳过 */
	public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";

	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextReset;
	private boolean mIsFirstInput = true;
	private String mFirstPassword = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loan_gesture_edit_layout);

		setUpViews();
		setUpListeners();
	}

	private void setUpViews() {
		this.mTextReset = (TextView) findViewById(R.id.text_reset);
		this.mTextReset.setClickable(false);
		this.mTextTip = (TextView) findViewById(R.id.text_tip);
		this.mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);

		// 初始化一个显示各个点的viewGroup
		this.mGestureContentView = new GestureContentView(this, false, "", new GestureCallBack() {
			@Override
			public void onGestureCodeInput(String inputCode) {
				if (!isInputPassValidate(inputCode)) {// 连接点少于4个
					mTextTip.setText(Html.fromHtml("<font color='#E2304E'>最少链接4个点, 请重新输入</font>"));
					mGestureContentView.clearDrawlineState(0L);
					return;
				}

				if (mIsFirstInput) {// 第一次输入
					mFirstPassword = inputCode;
					mGestureContentView.clearDrawlineState(0L);

					mTextReset.setVisibility(View.VISIBLE);
					mTextReset.setClickable(true);
					mTextReset.setText(getString(R.string.loan_reset_gesture_code));

					mTextTip.setText(R.string.loan_setup_gesture_pattern_again);
				} else {
					if (inputCode.equals(mFirstPassword)) {// 设置成功
						Toast.makeText(GestureEditActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
						mGestureContentView.clearDrawlineState(0L);

						AppInfoPrefs.saveGestureLockPasswd(inputCode);

						LogUtil.d("HttpRequeset", "Extra.Select_ID-->" + getIntent().getIntExtra(Extra.Select_ID, 0));

						String comeFrom = getIntent().getStringExtra("come_from");

						if (comeFrom != null) {
							if (comeFrom.equals("projectIntroduce")) {// 如果从标的详情进入
								Intent intent = new Intent(GestureEditActivity.this, ProjectIntroduceActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("mockId", getIntent().getStringExtra("mockId"));
								startActivity(intent);
							} else if (comeFrom.equals("come_from")) {// 从h5界面点击进入
								StringBuffer buffer = new StringBuffer();

								String url = getIntent().getStringExtra("htmlUrl");

								buffer.append(url);

								if (url.contains("?")) {// url中包含了?
									buffer.append("&channel=").append(AppInfoUtil.getChannel());
								} else {
									buffer.append("?channel=").append(AppInfoUtil.getChannel());
								}

								buffer.append("&userId=").append(UserInfoPrefs.getUserId());

								Intent intent = new Intent(GestureEditActivity.this, WebViewActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("htmlUrl", buffer.toString());
								intent.putExtra("htmlTitle", getIntent().getStringExtra("htmlTitle"));
								startActivity(intent);
							}

							finish();
						} else if (getIntent().getIntExtra(Extra.Select_ID, 0) == Type.SET_GESTURE_REGISTER) {// 从注册进入该界面
							Intent intent = new Intent(GestureEditActivity.this, RegistCompleteActivity.class);
							startActivity(intent);
						} else {
							Intent intent = new Intent(GestureEditActivity.this, MainActivity.class);
							intent.putExtra(Extra.Select_Info, 2);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}

						GestureEditActivity.this.finish();
					} else {// 2此绘制不一致
						mTextTip.setText(Html.fromHtml("<font color='#E2304E'>与上一次绘制不一致，请重新输入</font>"));

						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEditActivity.this, R.anim.shake);

						mTextTip.startAnimation(shakeAnimation);

						// 保持绘制的线，1.5秒后清除
						mGestureContentView.clearDrawlineState(300L);
					}
				}

				mIsFirstInput = false;
			}

			@Override
			public void checkedSuccess() {

			}

			@Override
			public void checkedFail() {

			}
		});

		// 设置手势解锁显示到哪个布局里面
		this.mGestureContentView.setParentView(this.mGestureContainer);
	}

	private void setUpListeners() {
		this.mTextReset.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.text_reset:
				mIsFirstInput = true;
				mTextTip.setText(getString(R.string.loan_setup_gesture_pattern));
				mTextReset.setVisibility(View.GONE);
				break;
			default:
				break;
		}
	}

	private boolean isInputPassValidate(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
			return false;
		}

		return true;
	}

	@Override
	public void onResume() {
		super.onResume();

		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		MobclickAgent.onPause(this);
	}
}