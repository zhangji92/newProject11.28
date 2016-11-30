package com.jrd.loan.util;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

import com.jrd.loan.R;

/**
 * @author Iris 倒计时相关
 */
public class CountDownUtil {

    private int countDownTime = 60000; // 开始倒计时的时间 以毫秒为单位
    private Button btnSendCode; // 倒计时按钮

    private String getAgainCode; // 获取多少秒以后重新获取验证码的String字符串
    private String getStringCode;// 获取点击获取验证码的String字符串

    private Context context;

    /**
     * 启动倒计时
     *
     * @param context     当前activity
     * @param mHandler    用handler启动线程
     * @param btnSendCode 倒计时按钮
     */
    public CountDownUtil(Context context, Handler mHandler, Button btnSendCode) {
        this.btnSendCode = btnSendCode;
        this.context = context;

        getAgainCode = context.getResources().getString(R.string.loan_personal_againgCode);
        getStringCode = context.getResources().getString(R.string.loan_personal_getCode);

        mHandler.post(attemptLockout);
    }

    /**
     * 启动倒计时的线程
     */
    Runnable attemptLockout = new Runnable() {
        @Override
        public void run() {
            new CountDownTimer(countDownTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int secondsRemaining = (int) (millisUntilFinished / 1000);
                    btnSendCode.setEnabled(false);
                    String codeText = String.format(getAgainCode, secondsRemaining);
                    SpannableStringBuilder style = new SpannableStringBuilder(codeText);
                    style.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    btnSendCode.setText(style);
                }

                @Override
                public void onFinish() {
                    btnSendCode.setText(getStringCode);
                    btnSendCode.setEnabled(true);
                }

            }.start();
        }
    };

}
