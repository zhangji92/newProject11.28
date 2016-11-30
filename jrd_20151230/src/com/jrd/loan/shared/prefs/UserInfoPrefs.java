package com.jrd.loan.shared.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.jrd.loan.base.BaseApplication;
import com.jrd.loan.bean.PersonalInfo;
import com.jrd.loan.constant.Const.Extra;

public final class UserInfoPrefs {
    private final static String FILE_NAME = "loan_user_info";
    private static final String PREF_USERID = "userId";
    private static final String PREF_TELNUM = "telNum";
    private static final String PREF_SESSIONID = "sessionid";
    
    private static final String PREF_PROVINCE = "province";
    private static final String PREF_CITY = "city";
    private static final String PREF_LONGITUDE = "longitude";
    private static final String PREF_LATITUDE = "latitude";
    

    /**
     * 判断是否是登录状态
     */
    public static boolean isLogin() {
        SharedPreferences preferences = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        // 如果本地userid不为空, 用户处于登录状态
        return !TextUtils.isEmpty(preferences.getString(PREF_USERID, ""));

    }

    /**
     * 获取当前登陆用户id
     */
    public static String getUserId() {
        SharedPreferences preferences = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        return preferences.getString(PREF_USERID, null);
        // return "c9521a95bcf84648ae957bbd49803b47";
    }

    /**
     * 保存用户id
     */
    public static void setUserId(String userId) {
        SharedPreferences preferences = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(PREF_USERID, userId);
        editor.apply();
    }

    /**
     * 获取当前登陆用户手机号码
     */
    public static String getTelNum() {
        SharedPreferences preferences = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PREF_TELNUM, null);
    }

    /**
     * 保存手机号码
     */
    public static void setTelNum(String telNum) {
        SharedPreferences preferences = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(PREF_TELNUM, telNum);
        editor.apply();
    }

    /**
     * 获取当前sessionid
     */
    public static String getSessionId() {
        SharedPreferences preferences = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PREF_SESSIONID, null);
    }

    /**
     * 保存sessionid
     */
    public static void setSessionId(String sessionid) {
        SharedPreferences preferences = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(PREF_SESSIONID, sessionid);
        editor.apply();
    }

    /**
     * 加载本地用户最新信息
     */
    public static PersonalInfo loadLastLoginUserProfile() {
        SharedPreferences preferences = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        PersonalInfo user = new PersonalInfo();
        user.userId = getUserId();
        user.userName = preferences.getString(Extra.USER_NAME, null);
        user.mobileNumber = preferences.getString(Extra.MobileNumber, null);
        user.idNumberInfo = preferences.getString(Extra.IdNumberInfo, null);
        user.idNumberFlag = preferences.getString(Extra.IdNumberFlag, null);
        user.passwordInfo = preferences.getString(Extra.PswInfo, null);
        user.securityLevel = preferences.getString(Extra.SecurityLevel, null);
        user.transPwdInfo = preferences.getString(Extra.TransInfo, null);
        user.transPwdFlag = preferences.getString(Extra.TransFlag, null);
        user.boundCardFlag = preferences.getString(Extra.BindCardInfo, null);
        user.quickCardFlag = preferences.getString(Extra.QuickCardInfo, null);
        return user;
    }

    /**
     * 保存用户信息
     */
    public static void saveUserProfileInfo(PersonalInfo user) {
        if (user == null) {
            clearUserInfo();
            return;
        }

        SharedPreferences preferences = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // 用户ID
        if (!TextUtils.isEmpty(user.userId)) {
            editor.putString(Extra.USER_ID, user.userId);
        }

        // 用户姓名
        if (!TextUtils.isEmpty(user.userName)) {
            editor.putString(Extra.USER_NAME, user.userName);
        }

        // 手机号码
        if (!TextUtils.isEmpty(user.mobileNumber)) {
            editor.putString(Extra.MobileNumber, user.mobileNumber);
        }

        // 安全等级
        if (!TextUtils.isEmpty(user.securityLevel)) {
            editor.putString(Extra.SecurityLevel, user.securityLevel);
        }

        // 身份证号
        if (!TextUtils.isEmpty(user.idNumberInfo)) {
            editor.putString(Extra.IdNumberInfo, user.idNumberInfo);
        } else {
            editor.putString(Extra.IdNumberInfo, "");
        }

        // 身份证号flag
        if (!TextUtils.isEmpty(user.idNumberFlag)) {
            editor.putString(Extra.IdNumberFlag, user.idNumberFlag);
        } else {
            editor.putString(Extra.IdNumberFlag, "");
        }

        // 交易密码
        if (!TextUtils.isEmpty(user.transPwdInfo)) {
            editor.putString(Extra.TransInfo, user.transPwdInfo);
        } else {
            editor.putString(Extra.TransInfo, "");
        }
        // 交易密码flag
        if (!TextUtils.isEmpty(user.transPwdFlag)) {
            editor.putString(Extra.TransFlag, user.transPwdFlag);
        } else {
            editor.putString(Extra.TransFlag, "");
        }
        // 是否绑卡
        if (!TextUtils.isEmpty(user.boundCardFlag)) {
            editor.putString(Extra.BindCardInfo, user.boundCardFlag);
        } else {
            editor.putString(Extra.BindCardInfo, "");
        }
        // 是否绑快捷卡
        if (!TextUtils.isEmpty(user.quickCardFlag)) {
            editor.putString(Extra.QuickCardInfo, user.quickCardFlag);
        } else {
            editor.putString(Extra.QuickCardInfo, "");
        }

        editor.apply();
    }

    public static void clearUserInfo() {
        SharedPreferences preferences = BaseApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 保存身份证号和用户名称
     */
    public static void setUserIdAndName(Context mContext, String userId, String name) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userid", userId);
        editor.putString("name", name);
        editor.apply();
    }

    /**
     * 获取身份证号
     */
    public static String getUserCardId(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString("userid", "");
    }

    /**
     * 获取用户真实姓名
     */
    public static String getCardName(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString("name", "");

    }
    
    /**
     * 保存定位信息   省市经纬度
     */
    public static void setLocationInfo(Context mContext, String province, String city,String longitude,String latitude) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREF_PROVINCE, province);
        editor.putString(PREF_CITY, city);
        editor.putString(PREF_LONGITUDE, longitude);
        editor.putString(PREF_LATITUDE, latitude);
        editor.apply();
    }
    /**
     * 获取省份
     */
    public static String getProvince(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(PREF_PROVINCE, "");

    }
    /**
     * 获取市
     */
    public static String getCity(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(PREF_CITY, "");

    }
    /**
     * 获取经度
     */
    public static String getLongitude(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(PREF_LONGITUDE, "");

    }
    /**
     * 获取纬度
     */
    public static String getLatitude(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(PREF_LATITUDE, "");

    }

}
