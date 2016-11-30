package com.jrd.loan.shared.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.jrd.loan.base.BaseApplication;

public final class AppInfoPrefs {
    private final static String FILE_NAME = "app_info";
    public final static String APP_INFO_KEY_CURR_VERSION = "curr_version";
    public final static String APP_INFO_KEY_LAST_VERSION = "last_version";
    public final static String WITHDRAW_IS_SUCCESS = "withdraw";
    public final static String RECHARGE_IS_SUCCESS = "recharge";
    public final static String REGIST_TO_COUPON = "coupon";
    public final static String RECHARGE_IS_FRIST = "recharge_frist";

    public static void setFirstStartApp(boolean isFirstStartApp) {
        SharedPreferences prefs = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean("is_first_start_app", isFirstStartApp).apply();
    }

    public static boolean isFirstStartApp(Context mContext) {
        return mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getBoolean("is_first_start_app", true);
    }

    public static void saveAppInfo(String key, String value) {
        SharedPreferences sharedPrefs = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getAppInfo(String key, String defVal) {
        SharedPreferences sharedPrefs = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        return sharedPrefs.getString(key, defVal);
    }

    // 保存手势密码锁密码
    public static void saveGestureLockPasswd(String passwd) {
        SharedPreferences sharedPrefs = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(UserInfoPrefs.getUserId(), passwd);
        editor.apply();
    }

    // 获得手势密码锁密码
    public static String getGestureLockPasswd() {
        SharedPreferences sharedPrefs = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        return sharedPrefs.getString(UserInfoPrefs.getUserId(), "");
    }

    // 判断手势密码锁是否设置
    public static boolean isSetGestureLock(Context mContext) {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String lockPasswd = sharedPrefs.getString(UserInfoPrefs.getUserId(), "");

        return !TextUtils.isEmpty(lockPasswd);
    }

    public static boolean isFirstStartMainScreen() {
        SharedPreferences sharedPrefs = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean("is_first_start_main", true);
    }

    public static void setFirstStartMainScreen(boolean isSetGestureLock) {
        SharedPreferences sharedPrefs = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().putBoolean("is_first_start_main", isSetGestureLock).apply();
    }

    /**
     * 用户是否提现成功
     *
     */
    public static void setWithDraw(boolean isSuccess) {
        SharedPreferences sharedPrefs = BaseApplication.getContext().getSharedPreferences(WITHDRAW_IS_SUCCESS, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putBoolean(WITHDRAW_IS_SUCCESS, isSuccess);
        editor.apply();
    }

    public static boolean getWithDraw() {
        SharedPreferences sharedPrefs = BaseApplication.getContext().getSharedPreferences(WITHDRAW_IS_SUCCESS, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(WITHDRAW_IS_SUCCESS, false);
    }

    /**
     * 用户是否充值成功
     *
     */
    public static void setRecharge(boolean isSuccess) {
        SharedPreferences sharedPrefs = BaseApplication.getContext().getSharedPreferences(RECHARGE_IS_SUCCESS, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putBoolean(RECHARGE_IS_SUCCESS, isSuccess);
        editor.apply();
    }

    public static boolean getRecharge() {
        SharedPreferences sharedPrefs = BaseApplication.getContext().getSharedPreferences(RECHARGE_IS_SUCCESS, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(RECHARGE_IS_SUCCESS, false);
    }

    /**
     * 是否是从注册成功跳到优惠券界面
     *
     */
    public static void setIntentToCoupon(boolean isTo) {
        SharedPreferences sharedPrefs = BaseApplication.getContext().getSharedPreferences(REGIST_TO_COUPON, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putBoolean(REGIST_TO_COUPON, isTo);
        editor.apply();
    }

    public static boolean getToCoupon() {
        SharedPreferences sharedPrefs = BaseApplication.getContext().getSharedPreferences(REGIST_TO_COUPON, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(REGIST_TO_COUPON, false);
    }
    /**
     * 是否是第一次充值
     *
     */
    public static void setFristRecharge(Context context,boolean isFrist) {
      SharedPreferences sharedPrefs = context.getSharedPreferences(RECHARGE_IS_FRIST, Context.MODE_PRIVATE);
      Editor editor = sharedPrefs.edit();
      editor.putBoolean(RECHARGE_IS_FRIST, isFrist);
      editor.apply();
    }
    
    public static boolean getFristRecharge(Context context) {
      SharedPreferences sharedPrefs = context.getSharedPreferences(REGIST_TO_COUPON, Context.MODE_PRIVATE);
      return sharedPrefs.getBoolean(RECHARGE_IS_FRIST, false);
    }
}
