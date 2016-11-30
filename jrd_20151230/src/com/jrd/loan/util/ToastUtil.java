package com.jrd.loan.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jrd.loan.R;

/**
 * 显示toast信息
 *
 * @author Luke
 */
public final class ToastUtil {
    private static TextView tvView;
    private static Toast toast = null;

    private ToastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public final static void showShort(Context context, CharSequence message) {
        if (toast == null) {
            toast = new Toast(context);
            tvView = (TextView) View.inflate(context, R.layout.loan_toast_layout, null);
            tvView.setText(message);

            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(tvView);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            tvView.setText(message);
        }

        toast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public final static void showShort(Context context, int message) {

        if (toast == null) {
            toast = new Toast(context);
            tvView = (TextView) View.inflate(context, R.layout.loan_toast_layout, null);
            tvView.setText(message);

            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(tvView);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            tvView.setText(message);
        }

        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public final static void showLong(Context context, CharSequence message) {
        if (toast == null) {
            toast = new Toast(context);
            tvView = (TextView) View.inflate(context, R.layout.loan_toast_layout, null);
            tvView.setText(message);

            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(tvView);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            tvView.setText(message);
        }

        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public final static void showLong(Context context, int message) {
        if (toast == null) {
            toast = new Toast(context);
            tvView = (TextView) View.inflate(context, R.layout.loan_toast_layout, null);
            tvView.setText(message);

            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(tvView);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            tvView.setText(message);
        }

        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public final static void show(Context context, CharSequence message, int duration) {
        if (toast == null) {
            toast = new Toast(context);
            tvView = (TextView) View.inflate(context, R.layout.loan_toast_layout, null);
            tvView.setText(message);

            toast.setDuration(duration);
            toast.setView(tvView);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            tvView.setText(message);
        }

        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public final static void show(Context context, int message, int duration) {
        if (toast == null) {
            toast = new Toast(context);
            tvView = (TextView) View.inflate(context, R.layout.loan_toast_layout, null);
            tvView.setText(message);

            toast.setDuration(duration);
            toast.setView(tvView);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            tvView.setText(message);
        }

        toast.show();
    }
}