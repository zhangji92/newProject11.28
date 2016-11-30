package com.yoka.mrskin.model.managers;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yoka.mrskin.login.YKUser;
import com.yoka.mrskin.login.YKUserInfo;
import com.yoka.mrskin.main.AppContext;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.AppUtil;

public class YKCurrentUserManager extends YKManager
{
	private static Object lock = new Object();
	private static YKCurrentUserManager instance = null;
	private YKUser mUser;
	private Context mContext;
	private ILoginCallback mCallback;

	public ILoginCallback getCallback() {
		return mCallback;
	}

	public void setCallback(ILoginCallback callback) {
		this.mCallback = callback;
	}

	public static YKCurrentUserManager getInstance(Context context) {
		synchronized (lock) {
			if (instance == null) {
				instance = new YKCurrentUserManager();
				instance.mContext = context;
				instance.mUser = instance.getLoginInfo(context);
			}
		}
		return instance;

	}

	public static YKCurrentUserManager getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new YKCurrentUserManager();
			}
		}
		return instance;

	}

	private YKCurrentUserManager()
	{

	}

	public void setUser(YKUser user) {
		mUser = user;
		saveLoginInfo(mContext);
		saveIsSkinTest(mContext, isTest());
		saveEventCount(mContext);
		YKResult result;
		result = new YKResult();
		result.succeed();
		ILoginCallback callback = getCallback();
		if (callback != null) {
			callback.loginCallback(result, user);
		}
		setCallback(null);
	}

	public YKUser getUser() {
		if (mUser == null) {
			mUser = getLoginInfo(AppContext.getInstance());
		}
		return mUser;
	}

	public void clearLoginUser() {
		mUser = null;
		deleteLoginInfo(AppContext.getInstance());
		YKTaskManagers.getInstance().clearTaskJoinedStatus();
	}

	public boolean isLogin() {
		return mUser != null;
	}

	public boolean getSkinTestState() {
		return getIsSkinTest(mContext);
	}

	public int getTrialEventCount() {
		return AppUtil.getSharedPreferences(mContext).getInt(
				"trial_event_count", 0);
	}

	private boolean isTest() {
		if (mUser != null) {
			return mUser.isSkinTest();
		}
		return false;
	}

	private void deleteLoginInfo(Context context) {
		AppUtil.getSharedPreferencesEditor(context).remove(AppUtil.LOGIN_DATA)
		.commit();

	}

	public void saveIsSkinTest(Context context, boolean isTest) {
		if (context != null) {
			AppUtil.getSharedPreferencesEditor(context)
			.putBoolean("IS_SKIN_TEST", isTest).commit();
		}
	}

	private boolean getIsSkinTest(Context context) {
		return AppUtil.getSharedPreferences(context).getBoolean("IS_SKIN_TEST",
				false);
	}

	private YKUser getLoginInfo(Context context) {
		String jsonData = AppUtil.getSharedPreferences(context).getString(
				AppUtil.LOGIN_DATA, "");
		YKUser user = null;
		if (!TextUtils.isEmpty(jsonData)) {
			user = new YKUser();
			try {
				user.paseUser(new JSONObject(jsonData));
			} catch (Exception e) {
			}
		}

		return user;

	}

	private void saveLoginInfo(Context context) {
		if (mUser != null && context != null) {
			AppUtil.getSharedPreferencesEditor(context)
			.putString(AppUtil.LOGIN_DATA,
					mUser.getJsonData().toString()).commit();
		}
	}

	private void saveEventCount(Context context) {
		if (context != null) {
			AppUtil.getSharedPreferencesEditor(context)
			.putInt("trial_event_count", mUser.getTrialEventCount())
			.commit();
		}
	}
	/**
	 * 保存登陆后签到、优币等信息
	 * --zlz
	 */
	public void saveYkUserInfo(YKUserInfo userInfo,Context context){
		if(null != userInfo){
			Gson gson = new Gson();  
			String jsonInfo = gson.toJson(userInfo);  
			AppUtil.getSharedPreferencesEditor(context)
			.putString(AppUtil.USER_INFO,
					jsonInfo).commit();
		}

	}

	public YKUserInfo getYkUserInfo(Context context){
		String jsonData = AppUtil.getSharedPreferences(context).getString(
				AppUtil.USER_INFO, "");
		YKUserInfo userInfo = null;
		if (!TextUtils.isEmpty(jsonData)) {
			userInfo = new YKUserInfo();
			try {
				userInfo = YKUserInfo.parse(new JSONObject(jsonData));
			} catch (Exception e) {
			}
		}

		return userInfo;
	}

	public interface ILoginCallback
	{
		public void loginCallback(YKResult result, YKUser user);
	}

}
