package com.jrd.loan.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jrd.loan.gesture.lock.GestureLockDialog;
import com.jrd.loan.net.framework.HttpRequest;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.ActivityCollector;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.DialogUtils.OnButtonCancelListener;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.NetUtil;
import com.jrd.loan.widget.WindowView;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends Activity {

    private final static String TAG = "BaseActivity";
    private final static int LAST_TIME = 30000;
    private HomeWatcherReceiver homeKeyReceiver;
    private static boolean isPresseHomeKey = false;
    private static boolean gestureLockShowing = false;
    private static long lastTime = System.currentTimeMillis();
    private Dialog dialog;
    private Dialog dialog2;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.rootView = View.inflate(this, this.loadWindowLayout(), null);
        setContentView(this.rootView);
        ActivityCollector.addActivity(this);
        this.initTitleBar();
        this.initViews();
    }

    protected abstract int loadWindowLayout();

    protected abstract void initTitleBar();

    protected abstract void initViews();

    @Override
    public void onResume() {
        super.onResume();
        registerHomeKeyReceiver();
        MobclickAgent.onResume(this);

        // 如果isPresseHomeKey=true, 说明用户按了home键退出了
        if (isPresseHomeKey) {
            isPresseHomeKey = false;
            // 设置了密码锁(只有登录后的用户, 才显示)
            if (UserInfoPrefs.isLogin() && AppInfoPrefs.isSetGestureLock(BaseActivity.this) && !AppInfoPrefs.isFirstStartMainScreen() && System.currentTimeMillis() - lastTime > LAST_TIME) {
                gestureLockShowing = true;
                this.startVerifyLockPatternDialog();
            }
        }
    }

    // 显示手势密码锁界面
    private void startVerifyLockPatternDialog() {
        GestureLockDialog dialog = new GestureLockDialog();
        dialog.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterHomeKeyReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    public static void setIsPresseHomeKey(boolean isPressedHomeKey) {
        isPresseHomeKey = isPressedHomeKey;
    }

    public static void setGestureLock(boolean gestureLockShowing) {
        BaseActivity.gestureLockShowing = gestureLockShowing;
    }

    public static void setLastTimes(long times) {
        lastTime = times;
    }

    /**
     * 显示dialog进度条
     *
     * @param context
     */
    public void ShowDrawDialog(final Context context) {
        if (this.rootView.getClass() == WindowView.class) {
            ((WindowView) this.rootView).hideContentLayout();
        }

        if (dialog == null && !gestureLockShowing) {
            dialog = DialogUtils.ShowDefaultProDialog(context, new OnButtonCancelListener() {

                @Override
                public void onCancel() {
                    finish();
                    HttpRequest.CancelHttp(context);
                }
            });
        } else {
            dialog.show();
        }
    }

    /**
     * 显示默认dialog进度条
     *
     * @param context
     */
    public void ShowProDialog(final Context context) {
        // if (this.rootView.getClass() == WindowView.class) {
        // ((WindowView) this.rootView).hideContentLayout();
        // }

        if (dialog == null && !gestureLockShowing) {
            dialog = DialogUtils.ShowDefaultProDialog(context, new OnButtonCancelListener() {

                @Override
                public void onCancel() {
                    finish();
                    HttpRequest.CancelHttp(context);
                }
            });
        } else {
            dialog.show();
        }
    }

    /**
     * 关闭dialog进度条
     */
    public void DismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        if (NetUtil.hasMobileNet()) {// 有网络
            if (this.rootView.getClass() == WindowView.class) {
                ((WindowView) this.rootView).hideNoNetworkLayout();
            }
        } else {// 无网络
            if (this.rootView.getClass() == WindowView.class) {
                ((WindowView) this.rootView).showNoNetworkLayout();
            }
        }
    }

    protected void setNoNetworkClickListener(OnClickListener clickListener) {
        if (this.rootView.getClass() == WindowView.class) {
            ((WindowView) this.rootView).setNoNetworkClick(clickListener);
        }
    }

    private final void registerHomeKeyReceiver() {
        LogUtil.i(TAG, "--------- registerHomeKeyReceiver");
        homeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter();
        homeFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeKeyReceiver, homeFilter);
    }

    private final void unregisterHomeKeyReceiver() {
        LogUtil.i(TAG, "--------- unregisterHomeKeyReceiver");
        if (null != homeKeyReceiver) {
            unregisterReceiver(homeKeyReceiver);
            homeKeyReceiver = null;
        }
    }

    private static class HomeWatcherReceiver extends BroadcastReceiver {

        private static final String LOG_TAG = "HomeReceiver";
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
        private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                LogUtil.i(LOG_TAG, "----- reason: " + reason);
                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason) // 短按Home键
                        || SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason) // 长按Home键或者activity切换键
                        || SYSTEM_DIALOG_REASON_LOCK.equals(reason) // 锁屏
                        || SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {// samsung长按Home键用户按了home键
                    isPresseHomeKey = true;
                    lastTime = System.currentTimeMillis();
                }
            }
        }
    }
}
