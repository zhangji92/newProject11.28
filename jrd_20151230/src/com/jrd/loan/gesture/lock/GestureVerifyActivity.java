package com.jrd.loan.gesture.lock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.account.LoginActivity;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.widget.GestureContentView;
import com.jrd.loan.widget.GestureContentView.GestureCallBack;
import com.umeng.analytics.MobclickAgent;

/**
 * 手势绘制/校验界面
 */
public class GestureVerifyActivity extends Activity implements android.view.View.OnClickListener {
    private final static int INPUT_PASSWD_ERROR_COUNT = 5;

    /**
     * 手机号码
     */
    public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
    /**
     * 意图
     */
    public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";

    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView tvForgetPasswd;

    private int inputPasswdTimes = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_gesture_verify_layout);

        this.setUpViews();
        this.setUpListeners();
    }

    private void setUpViews() {
        // 手机号码
        TextView text_phone_number = (TextView) findViewById(R.id.text_phone_number);
        text_phone_number.setText(this.getProtectedMobile(UserInfoPrefs.getTelNum()));

        this.mTextTip = (TextView) findViewById(R.id.text_tip);
        this.mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        this.tvForgetPasswd = (TextView) findViewById(R.id.tvForgetPasswd);

        // 初始化一个显示各个点的viewGroup
        this.mGestureContentView = new GestureContentView(this, true, AppInfoPrefs.getGestureLockPasswd(), new GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {

            }

            @Override
            public void checkedSuccess() {
                mGestureContentView.clearDrawlineState(0L);
                BaseActivity.setGestureLock(false);
                GestureVerifyActivity.this.finish();
            }

            @Override
            public void checkedFail() {
                inputPasswdTimes++;

                if (inputPasswdTimes < INPUT_PASSWD_ERROR_COUNT) {
                    mGestureContentView.clearDrawlineState(300L);
                    mTextTip.setTextColor(getResources().getColor(R.color.loan_gesture_color_txt_red));

                    if (inputPasswdTimes == 1) {
                        mTextTip.setText(getString(R.string.loan_draw_gesture_passwd_error, getString(R.string.loan_draw_gesture_passwd_error_4)));
                    } else if (inputPasswdTimes == 2) {
                        mTextTip.setText(getString(R.string.loan_draw_gesture_passwd_error, getString(R.string.loan_draw_gesture_passwd_error_3)));
                    } else if (inputPasswdTimes == 3) {
                        mTextTip.setText(getString(R.string.loan_draw_gesture_passwd_error, getString(R.string.loan_draw_gesture_passwd_error_2)));
                    } else if (inputPasswdTimes == 4) {
                        mTextTip.setText(getString(R.string.loan_draw_gesture_passwd_error, getString(R.string.loan_draw_gesture_passwd_error_1)));
                    }

                    // 左右移动动画
                    Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this, R.anim.shake);
                    mTextTip.startAnimation(shakeAnimation);
                } else {// 手势密码5次输入错误
                    BaseActivity.setGestureLock(false);

                    Intent intent = new Intent(GestureVerifyActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
    }

    private void setUpListeners() {
        this.tvForgetPasswd.setOnClickListener(this);
    }

    private String getProtectedMobile(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(phoneNumber.subSequence(0, 3));
        builder.append("****");
        builder.append(phoneNumber.subSequence(7, 11));

        return builder.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvForgetPasswd:
                BaseActivity.setGestureLock(false);

                Intent intent = new Intent(GestureVerifyActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
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
