package com.yoka.mrskin.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.umeng.analytics.AnalyticsConfig;
import com.yoka.mrskin.R;
import com.yoka.mrskin.login.AuthorUser;
import com.yoka.mrskin.model.data.YKCity;
import com.yoka.mrskin.model.data.YKLocationCity;
import com.yoka.mrskin.model.data.YKVersionInfo;

public class AppUtil
{
    /**
     * 判断网络是否连接
     * 
     * @param activity
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
	if (context == null)
	    return false;
	// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
	try {
	    ConnectivityManager connectivityManager = (ConnectivityManager) context
		    .getSystemService(Context.CONNECTIVITY_SERVICE);

	    if (connectivityManager == null) {
		return false;
	    } else {
		// 获取NetworkInfo对象
		NetworkInfo[] networkInfo = connectivityManager
			.getAllNetworkInfo();
		if (networkInfo != null && networkInfo.length > 0) {
		    for (int i = 0; i < networkInfo.length; i++) {
			// 判断当前网络状态是否为连接状态
			if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
			    return true;
			}
		    }
		}
	    }
	} catch (Exception e) {
	}
	return false;
    }

    /**
     * 获取版本名
     * 
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
	String versionName = "";
	try {
	    PackageManager packageManager = context.getPackageManager();
	    PackageInfo packageInfo = packageManager.getPackageInfo(
		    context.getPackageName(), 0);
	    versionName = packageInfo.versionName;
	    if (TextUtils.isEmpty(versionName)) {
		return "";
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return versionName;
    }

    /**
     * 获取版本号
     * 
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
	int versionCode = -1;
	try {
	    PackageManager packageManager = context.getPackageManager();
	    PackageInfo packageInfo = packageManager.getPackageInfo(
		    context.getPackageName(), 0);
	    versionCode = packageInfo.versionCode;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return versionCode;
    }

    public static String getPackageName(Context context) {
	return context.getPackageName();
    }

    /**
     * 升级提示
     * 
     * @param context
     * @param versionInfo
     */
    public static void hasUpdate(final Context context,
	    final YKVersionInfo versionInfo, boolean isAuto) {
	if (!canShowUpdateDialog(context, isAuto)) {
	    return;
	}
	AlertDialog.Builder alert = new AlertDialog.Builder(context);
	alert.setTitle(R.string.more_updata)
	.setMessage(
		context.getString(R.string.more_version_info)
		+ versionInfo.getmVersionInfo())
		.setPositiveButton(R.string.more_updata_load,
			new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,
			    int which) {
			String url = versionInfo.getmURL();
			try {
			    Intent viewIntent = new Intent(
				    "android.intent.action.VIEW", Uri
				    .parse(url));
			    context.startActivity(viewIntent);
			} catch (Exception e) {
			    Toast.makeText(context,
				    R.string.more_updata_error,
				    Toast.LENGTH_SHORT).show();
			}
		    }
		})
		.setNegativeButton(R.string.more_updata_exit,
			new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,
			    int which) {
			saveShowUpdateDialogTime(context);
			dialog.dismiss();
		    }
		});
	alert.create().show();
    }

    public static void showDialogSafe(Dialog dialog) {
	if (dialog == null) {
	    return;
	}
	try {
	    dialog.show();
	} catch (Exception e) {
	}
    }

    public static void dismissDialogSafe(Dialog dialog) {
	if (dialog == null) {
	    return;
	}
	try {
	    dialog.dismiss();
	} catch (Exception e) {
	}
    }

    private static boolean canShowUpdateDialog(Context context, boolean isAuto) {
	if (!isAuto) {
	    // 手动更新默认显示
	    return true;
	}
	long nowTime = System.currentTimeMillis();
	// 如果用户点击取消3天之内不需要再次提示
	long noToastSpace = 3 * 24 * 60 * 60 * 1000;
	if (nowTime - getShowUpdateDialogTime(context) > noToastSpace) {
	    return true;
	}
	return false;
    }

    // /**
    // * 判断是否是当天第一次启动
    // *
    // * @param context
    // * @param OneStart
    // * @return
    // */
    // @SuppressLint("SimpleDateFormat")
    // public static boolean isOneStart(Context context, boolean OneStart) {
    // if (!OneStart) {
    // return true;
    // }
    // // 获取系统当前时间
    // SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
    // Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
    // String str = formatter.format(curDate);
    //
    // String localDate = getOneStart(context);
    //
    // if (TextUtils.isEmpty(localDate)) {
    // return true;
    // } else {
    // if (localDate.equals(str)) {
    // return true;
    // }
    // }
    // return false;
    // }

    // ============升级提示忽略时间=================
    private static void saveShowUpdateDialogTime(Context context) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putLong(STORE_KEY_SHOWUPDATETIME, System.currentTimeMillis());
	edit.commit();
    }

    private static long getShowUpdateDialogTime(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getLong(STORE_KEY_SHOWUPDATETIME, 0);
    }

    // ============新手引导状态位=================
    public static void saveSplashState(Context context, boolean isShow) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putBoolean(STORE_KEY_ISSHOWSPLASH, isShow);
	edit.commit();
    }

    // ============保存时间=================
    // @SuppressLint("SimpleDateFormat")
    // public static void saveOneShortDate(Context context) {
    // // 获取系统当前时间
    // SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
    // Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
    // String str = formatter.format(curDate);
    // Editor edit = getSharedPreferencesEditor(context);
    // edit.putString(STORE_KEY_ONESTART, str);
    // edit.commit();
    // }

    public static boolean getSplashState(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getBoolean(STORE_KEY_ISSHOWSPLASH, false);
    }

    // ============获取时间=================
    // public static String getOneStart(Context context) {
    // SharedPreferences content = getSharedPreferences(context);
    // return content.getString(STORE_KEY_ONESTART, null);
    // }

    // ============clientid=================
    public static void saveClientId(Context context, String clientId) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putString(STORE_KEY_CLIENTID, clientId);
	edit.commit();
    }

    public static String getClientId(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getString(STORE_KEY_CLIENTID, null);
    }

    public static YKCity getCity(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	String id = content.getString(STORE_KEY_CITY_ID, null);
	String city = content.getString(STORE_KEY_CITY_NAME, null);
	if (TextUtils.isEmpty(city) || TextUtils.isEmpty(id)) {
	    return null;
	}
	YKCity ykCity = new YKCity();
	ykCity.setID(id);
	ykCity.setmCity(city);
	return ykCity;
    }

    public static void saveCity(Context context, YKCity ykCity) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putString(STORE_KEY_CITY_ID, ykCity.getID());
	edit.putString(STORE_KEY_CITY_NAME, ykCity.getmCity());
	edit.commit();
    }

    // 定位信息城市
    public static void saveLocationCity(Context context,
	    YKLocationCity locationCity) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putString(LOCATION_NAME, locationCity.getmName());
	edit.commit();
    }

    public static YKLocationCity getLocationCity(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	String city = content.getString(LOCATION_NAME, null);
	if (TextUtils.isEmpty(city)) {
	    return null;
	}
	YKLocationCity ykLocationCity = new YKLocationCity();
	ykLocationCity.setmName(city);
	return ykLocationCity;
    }

    public static boolean getCanRemindAll(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getBoolean(STORE_KEY_REMIND_ALL, true);
    }

    public static boolean getCanRemindOFF(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getBoolean(STORE_KEY_REMIND_OFF, true);
    }

    public static boolean getCanRemindRING(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getBoolean(STORE_KEY_REMIND_RING, true);
    }

    public static boolean getCanRemindSHAKE(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getBoolean(STORE_KEY_REMIND_SHAKE, true);
    }

    public static void saveCanRemindAll(Context context, boolean canRemind) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putBoolean(STORE_KEY_REMIND_ALL, canRemind);
	edit.commit();
    }

    public static void saveCanRemindOFF(Context context, boolean canRemind) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putBoolean(STORE_KEY_REMIND_OFF, canRemind);
	edit.commit();
    }

    public static void saveCanRemindRing(Context context, boolean canRemind) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putBoolean(STORE_KEY_REMIND_RING, canRemind);
	edit.commit();
    }

    // 保存新版本状态
    public static void saveCheckVerSionRing(Context context, boolean canRemind) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putBoolean(CHECK_SAVE, canRemind);
	edit.commit();
    }

    public static boolean getCheckVerSionRing(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getBoolean(CHECK_SAVE, true);
    }

    public static void saveCanRemindShake(Context context, boolean canRemind) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putBoolean(STORE_KEY_REMIND_SHAKE, canRemind);
	edit.commit();
    }

    // 分辨是第三方登录还是注册登录
    public static boolean getLoginInfoMessage(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getBoolean(LOGIN_MESSAGE, true);
    }

    public static void saveLoginInfoMessage(Context context, boolean canRemind) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putBoolean(LOGIN_MESSAGE, canRemind);
	edit.commit();
    }

    public static final String IS_PUSH = "is_push";

    public static void savePushState(Context context, boolean isPush) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putBoolean(IS_PUSH, isPush);
	edit.commit();
    }

    public static boolean getPushState(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getBoolean(IS_PUSH, false);
    }

    public static final String UPLOAD_TIME = "upload_time";

    public static void saveUploadTime(Context context, long time) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putLong(UPLOAD_TIME, time);
	edit.commit();
    }

    public static long getUploadTime(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getLong(UPLOAD_TIME, 0);
    }

    public static final String PUSH_INFO = "push_info";

    public static void savePushInfo(Context context, String info) {
	Editor editor = getSharedPreferencesEditor(context);
	editor.putString(PUSH_INFO, info);
	editor.commit();
    }

    public static String getPushInfo(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getString(PUSH_INFO, "");
    }

    private final static String STORE_KEY_TOPIC_READDATA = "trial_data";

    public static void saveTopicReadData(Context context, String name,
	    JSONArray array) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putString(STORE_KEY_TOPIC_READDATA + name, array.toString());
	edit.commit();
    }

    private final static String AD_BOOT_DATA = "ad_boot_data";

    public static void saveAdBootData(Context context, String data) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putString(AD_BOOT_DATA, data);
	edit.commit();
    }

    public static String getAdBootData(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getString(AD_BOOT_DATA, "");
    }

    private final static String AD_BOOT_ERROR = "ad_boot_error";

    public static void saveAdBootErrorData(Context context, String data) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putString(AD_BOOT_ERROR, data);
	edit.commit();
    }

    public static String getAdBootErrorData(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getString(AD_BOOT_ERROR, "");
    }

    private final static String AD_BOOT_CONTENT_TYPE = "ad_content_type";

    public static void saveAdBootContentTypeData(Context context, String data) {
	Editor edit = getSharedPreferencesEditor(context);
	edit.putString(AD_BOOT_CONTENT_TYPE, data);
	edit.commit();
    }

    public static String getAdBootContentTypeData(Context context) {
	SharedPreferences content = getSharedPreferences(context);
	return content.getString(AD_BOOT_CONTENT_TYPE, "");
    }

    public static JSONArray getTopicReadData(Context context, String name) {
	SharedPreferences preferences = getSharedPreferences(context);
	String content = preferences.getString(STORE_KEY_TOPIC_READDATA + name,
		null);
	JSONArray array = null;
	try {
	    array = new JSONArray(content);
	} catch (Exception e) {
	}
	return array;
    }

    // =======================================
    private final static String STORE_NAME = "YOKA_DATA";
    private final static String STORE_KEY_SHOWUPDATETIME = "showUpdateTime";
    private final static String STORE_KEY_ISSHOWSPLASH = "isShowSplash";
    private final static String STORE_KEY_CLIENTID = "clientid";
    private final static String STORE_KEY_CITY_ID = "city_id";
    private final static String STORE_KEY_CITY_NAME = "city_name";
    private final static String LOCATION_NAME = "location_city_name";
    private final static String STORE_KEY_REMIND_ALL = "remind_all";
    private final static String STORE_KEY_REMIND_RING = "remind_ring";
    private final static String STORE_KEY_REMIND_SHAKE = "remind_shake";
    public static final String LOGIN_DATA = "login_data";
    private static final String LOGIN_MESSAGE = "login_Message";
    private static final String CHECK_SAVE = "login_version_sava";
    private final static String STORE_KEY_REMIND_OFF = "remind_off";
    public static final String USER_INFO = "user_info";

    public static SharedPreferences getSharedPreferences(Context context) {
	return context.getSharedPreferences(STORE_NAME, 0);
    }

    public static Editor getSharedPreferencesEditor(Context context) {
	return getSharedPreferences(context).edit();
    }

    public static void showToast(Context context) {
	if (context != null) {
	    Toast toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
	    toast.setGravity(Gravity.CENTER, 0, 0);
	    View view = LayoutInflater.from(context).inflate(R.layout.toast_bg,
		    null);
	    toast.setView(view);
	    toast.show();
	}
    }

    public static String getAppChannel(Context context) {
	return AnalyticsConfig.getChannel(context);
    }

    private static final String TYPE_NAME = "mobile";

    /**
     * @Description 得到当前网络连接类型
     * @author huangke@yoka.com
     * @param context
     *            环境
     * @return String 当前网络连接类型名称
     * @date 2011-12-5 上午11:02:07
     */
    public static String getCurrentNetworkTypeName(Context context) {
	if (context == null)
	    return "";
	ConnectivityManager connectivityManager = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

	if (networkInfo == null) {
	    return "";
	}
	// 当前存在网络接入方式，获取具体的网络接入类型值
	else {
	    String typeName = networkInfo.getTypeName();
	    if (StringUtils.containsIgnoreCase(typeName, TYPE_NAME)) {
		String extraInfo = networkInfo.getExtraInfo();
		if (extraInfo == null) {
		    extraInfo = "ChinaTelecom";
		}
		return extraInfo;
	    }
	    return null == typeName ? "" : typeName;
	}
    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationRunInBackground(final Context context) {
	ActivityManager am = (ActivityManager) context
		.getSystemService(Context.ACTIVITY_SERVICE);
	List<RunningTaskInfo> tasks = am.getRunningTasks(1);
	if (!tasks.isEmpty()) {
	    ComponentName topActivity = tasks.get(0).topActivity;
	    if (!topActivity.getPackageName().equals(context.getPackageName())) {
		return true;
	    }
	}
	return false;
    }

    /**
     * 判断邮箱格式
     */
    public static boolean isEmail(String email) {
	if (TextUtils.isEmpty(email)) {
	    return false;
	}
	//String str = "\\w+@(\\w+.)+[a-z]{2,3}";//简单匹配
	String str = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";//复杂匹配
	//String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
	Pattern p = Pattern.compile(str);
	Matcher m = p.matcher(email);
	return m.matches();

    }
    public static boolean isPhone(String phone){

	String str = "[1][3578]\\d{9}";
	Pattern p = Pattern.compile(str);
	Matcher m = p.matcher(phone);
	return m.matches();
    }

    public static final String QQ_USER = "qq_user";

    public static boolean saveDataToFile(AuthorUser objectData) {
	ByteArrayOutputStream baos = null;
	ObjectOutputStream oos = null;
	byte[] data = null;
	try {
	    baos = new ByteArrayOutputStream();
	    oos = new ObjectOutputStream(baos);
	    oos.writeObject(objectData);
	    data = baos.toByteArray();
	} catch (Exception e) {
	    return false;
	} finally {
	    try {
		oos.close();
	    } catch (Exception e) {
	    }
	}

	YKFile.save(QQ_USER, data);
	return true;
    }

    public static AuthorUser loadDataFromFile() {
	ByteArrayInputStream bais = null;
	ObjectInputStream ois = null;
	byte[] data = YKFile.read(QQ_USER);
	try {
	    bais = new ByteArrayInputStream(data);
	    ois = new ObjectInputStream(bais);
	    AuthorUser objectData = (AuthorUser) ois.readObject();
	    return objectData;
	} catch (Exception e) {
	} finally {
	    try {
		ois.close();
	    } catch (Exception e) {
	    }
	    try {
		bais.close();
	    } catch (Exception e) {
	    }
	}
	return null;
    }
    /**
     * 判断是否安装微信
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
	final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
	List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
	if (pinfo != null) {
	    for (int i = 0; i < pinfo.size(); i++) {
		String pn = pinfo.get(i).packageName;
		if (pn.equals("com.tencent.mm")) {
		    return true;
		}

	    }

	}
	return false;
    }
    
    /**
     * 判断是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){   

	Pattern pattern = Pattern.compile("[0-9]*");   

	Matcher isNum = pattern.matcher(str);  

	if( !isNum.matches() ){  

	    return false;   

	}   

	return true;   

    }   

}
