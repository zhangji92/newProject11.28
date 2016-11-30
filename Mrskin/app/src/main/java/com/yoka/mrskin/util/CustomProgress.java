package com.yoka.mrskin.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;

import com.yoka.mrskin.R;

public class CustomProgress extends Dialog
{

    public CustomProgress(Context context)
    {
        super(context);
    }

    public CustomProgress(Context context, int theme)
    {
        super(context, theme);
    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        // 获取ImageView上的动画背景
        AnimationDrawable spinner = (AnimationDrawable) imageView
                .getBackground();
        // 开始动画
        spinner.start();
    }

    /**
     * 弹出自定义ProgressDialog
     * 
     * @param context
     *            上下文
     * @param message
     *            提示
     * @param cancelable
     *            是否按返回键取消
     * @param cancelListener
     *            按下返回键监听
     * @return
     */
    public static CustomProgress show(Context context) {
         CustomProgress mDialog = new CustomProgress(context,
         R.style.Custom_Progress);
//        CustomProgress mDialog = new CustomProgress(context);
        mDialog.setContentView(R.layout.progress_custom);
        // 按返回键是否取消
        mDialog.setCancelable(true);
        // 设置居中
        Window window = mDialog.getWindow();
        window.getAttributes().gravity = Gravity.CENTER;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        mDialog.show();
        return mDialog;
    }

    // @Override
    // public boolean dispatchTouchEvent(MotionEvent ev) {
    // if (isShowing()) {
    // AppUtil.dismissDialogSafe(this);
    // }
    // super.dispatchTouchEvent(ev);
    // return false;
    // }
}
