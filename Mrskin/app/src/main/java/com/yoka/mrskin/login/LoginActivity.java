package com.yoka.mrskin.login;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKLoginCallback;
import com.yoka.mrskin.model.managers.YKLoginManager;
import com.yoka.mrskin.model.managers.YKLoginRegistCallback;
import com.yoka.mrskin.model.managers.YKLoginRegisteManager;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers.DownloadTaskCallBack;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.model.managers.task.YKTaskStore;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;

public class LoginActivity extends BaseActivity implements OnClickListener
{
    //private LoginUtils loginUtil;
    private LinearLayout mBack;
    private EditText mLoginName, mLoginPassWord;
    public static final String UESR = "USER";
    private TextView mLoginroll, mSinaLogin, mLoginEnter, mQQLogin,mWeChatLogin;
    private CustomButterfly mCustomButterfly = null;
    private UMShareAPI mShareAPI = null;
    private static String QQToKen = null;
    private static String Uid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login_loginactivity);
	mShareAPI = UMShareAPI.get( this );
	Config.REDIRECT_URL =UmSharedAppID.REDIRECT_URL;
	initViews();
    }
    
    @Override
    public void onBackPressed() {
	setResult(88);
	finish();
        super.onBackPressed();
    }

    private void initViews() {

	mLoginName = (EditText) findViewById(R.id.loginsetting_name);
	mLoginPassWord = (EditText) findViewById(R.id.loginsetting_password);

	mSinaLogin = (TextView) findViewById(R.id.sina);
	mSinaLogin.setOnClickListener(this);
	mQQLogin = (TextView) findViewById(R.id.qq);
	mQQLogin.setOnClickListener(this);
	mWeChatLogin = (TextView) findViewById(R.id.wechat);
	mWeChatLogin.setOnClickListener(this);

	mBack = (LinearLayout) findViewById(R.id.loginactivity_back_layout);
	mBack.setOnClickListener(this);

	mLoginroll = (TextView) findViewById(R.id.quick_loginsetting_enroll);
	mLoginroll.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
	mLoginroll.setTextColor(Color.BLACK);
	mLoginroll.setOnClickListener(this);

	mLoginEnter = (TextView) findViewById(R.id.loginsetting_enter);
	mLoginEnter.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
	SHARE_MEDIA platform = null;
	switch (v.getId()) {
	case R.id.qq:
	    platform = SHARE_MEDIA.QQ;
	    if (AppUtil.isNetworkAvailable(LoginActivity.this)) {
		//                try {
		//                    mCustomButterfly = CustomButterfly.show(this);
		//                } catch (Exception e) {
		//                    mCustomButterfly = null;
		//                }
		mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);

	    }else{
		Toast.makeText(LoginActivity.this, R.string.intent_no, Toast.LENGTH_SHORT).show();
	    }
	    break;
	case R.id.sina:
	    platform = SHARE_MEDIA.SINA;
	    if (AppUtil.isNetworkAvailable(LoginActivity.this)) {
		//                try {
		//                    mCustomButterfly = CustomButterfly.show(this);
		//                } catch (Exception e) {
		//                    mCustomButterfly = null;
		//                }
		mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
	    }else{
		Toast.makeText(LoginActivity.this, R.string.intent_no, Toast.LENGTH_SHORT).show();
	    }
	    break;
	case R.id.wechat:
	    if (AppUtil.isNetworkAvailable(LoginActivity.this)) {
		Boolean iSWeixinInstall = AppUtil.isWeixinAvilible(LoginActivity.this);
		if(iSWeixinInstall){
		    platform = SHARE_MEDIA.WEIXIN;
		    mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
		}else{
		    Toast.makeText(LoginActivity.this, "请安装微信客户端", Toast.LENGTH_SHORT).show();
		}
	    }else{
		Toast.makeText(LoginActivity.this, R.string.intent_no, Toast.LENGTH_SHORT).show();
	    }
	    break;
	case R.id.loginactivity_back_layout:
	    setResult(88);
	    finish();
	    break;
	case R.id.quick_loginsetting_enroll:
	    Intent loginenroll = new Intent(LoginActivity.this,
		    LoginenrollActivity.class);
	    startActivityForResult(loginenroll, 0);
	    break;
	case R.id.loginsetting_enter:
	    try {
		    mCustomButterfly = CustomButterfly.show(this);
		} catch (Exception e) {
		    mCustomButterfly = null;
		}
	    String name = mLoginName.getText().toString().trim();
	    String password = mLoginPassWord.getText().toString().trim();
	    if (TextUtils.isEmpty(name) && TextUtils.isEmpty(password)) {
		AppUtil.dismissDialogSafe(mCustomButterfly);
		Toast.makeText(LoginActivity.this, R.string.setting_input_name,
			Toast.LENGTH_SHORT).show();
	    } else {
		if (TextUtils.isEmpty(name)) {
		    AppUtil.dismissDialogSafe(mCustomButterfly);
		    Toast.makeText(this, R.string.setting_input_name,
			    Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(password)) {
		    AppUtil.dismissDialogSafe(mCustomButterfly);
		    Toast.makeText(this, R.string.setting_input_pass,
			    Toast.LENGTH_SHORT).show();
		}
		else if (!TextUtils.isEmpty(name)
			&& !TextUtils.isEmpty(password)) {
		    if (AppUtil.isNetworkAvailable(this)) {
			localLogin(name, password);
		    } else {
			Toast.makeText(this, R.string.intent_no,
				Toast.LENGTH_SHORT).show();
		    }
		}
	    }
	    break;
	default:
	    break;
	}
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	mShareAPI.onActivityResult(requestCode, resultCode, data);
    }
    private void requstLoging(AuthorUser user) {
	if (user == null || TextUtils.isEmpty(user.getNickname())
		|| TextUtils.isEmpty(user.getAvatar_url())) {
	    Toast.makeText(LoginActivity.this, R.string.auth_error_again,
		    Toast.LENGTH_LONG).show();
	    AppUtil.dismissDialogSafe(mCustomButterfly);
	    return;
	}
	if(user.getType().equals("qq")){
	    YKLoginManager.getInstance().requestLoginInfoQQ(this, user,new YKLoginCallback() {

		@Override
		public void callback(YKResult result, YKUser user) {
		    AppUtil.dismissDialogSafe(mCustomButterfly);
		    if (result.isSucceeded()) {
			sendLoginData(user);
			setResult(111);
			setResult(77);
			finish();
		    } else {
			AppUtil.dismissDialogSafe(mCustomButterfly);
			Toast.makeText(LoginActivity.this,getString(R.string.intent_error),Toast.LENGTH_LONG).show();
		    }
		}

		@Override
		public void callbackFaile(String message) {
		    AppUtil.dismissDialogSafe(mCustomButterfly);
		    Toast.makeText(LoginActivity.this, message,
			    Toast.LENGTH_LONG).show();
		}
	    });
	}else if(user.getType().equals("sina")){
	    YKLoginManager.getInstance().requestLoginInfoSina(this, user,new YKLoginCallback() {

		@Override
		public void callback(YKResult result, YKUser user) {
		    AppUtil.dismissDialogSafe(mCustomButterfly);
		    if (result.isSucceeded()) {
			sendLoginData(user);
			setResult(111);
			setResult(77);
			finish();
		    } else {
			AppUtil.dismissDialogSafe(mCustomButterfly);
			Toast.makeText(LoginActivity.this,getString(R.string.intent_error),Toast.LENGTH_LONG).show();
		    }
		}

		@Override
		public void callbackFaile(String message) {
		    AppUtil.dismissDialogSafe(mCustomButterfly);
		    Toast.makeText(LoginActivity.this, message,
			    Toast.LENGTH_LONG).show();
		}
	    });
	}else if(user.getType().equals("wechat")){
	    YKLoginManager.getInstance().requestLoginInfoWeChat(this, user,new YKLoginCallback() {

		@Override
		public void callback(YKResult result, YKUser user) {
		    AppUtil.dismissDialogSafe(mCustomButterfly);
		    if (result.isSucceeded()) {
			sendLoginData(user);
			setResult(111);
			setResult(77);
			finish();
		    } else {
			AppUtil.dismissDialogSafe(mCustomButterfly);
			Toast.makeText(LoginActivity.this,getString(R.string.intent_error),Toast.LENGTH_LONG).show();
		    }
		}

		@Override
		public void callbackFaile(String message) {
		    AppUtil.dismissDialogSafe(mCustomButterfly);
		    Toast.makeText(LoginActivity.this, message,
			    Toast.LENGTH_LONG).show();
		}
	    });
	}else{
	    AppUtil.dismissDialogSafe(mCustomButterfly);
	    Toast.makeText(LoginActivity.this, "没有该登录方式",
		    Toast.LENGTH_LONG).show();
	}
    }

    /** 计划同步
     * @param user
     */
    private void sendLoginData(YKUser user) {

	YKSyncTaskManagers.getInstance().downLoadTask(
		new DownloadTaskCallBack() {

		    @Override
		    public void callback(ArrayList<YKTask> taskList,
			    YKResult result) {
			AppUtil.dismissDialogSafe(mCustomButterfly);
			if (result.isSucceeded()) {
			    if (taskList != null && taskList.size() > 0) {
				YKTaskStore.getInstnace()
				.saveTaskList(taskList);
				YKTaskManager.getInstnace()
				.notifyTaskDataChanged();
			    }
			    Toast.makeText(LoginActivity.this,
				    getString(R.string.login_sucess),
				    Toast.LENGTH_LONG).show();

			    Intent login = new Intent();
			    login.putExtra(UESR, true);
			    setResult(RESULT_OK, login);

			    finish();
			} else {
			    YKCurrentUserManager.getInstance().clearLoginUser();
			    Toast.makeText(LoginActivity.this,
				    getString(R.string.task_synchroniznonono),
				    Toast.LENGTH_SHORT).show();
			}

		    }
		});
    }
    /**
     * 账户 密码登录
     * @param name
     * @param password
     */
    public void localLogin(final String name, final String password) {
	YKLoginRegisteManager.getInstance().postLoginRegiste(name, password,
		new YKLoginRegistCallback() {

	    @Override
	    public void callback(YKResult result, YKUser user) {
//		AppUtil.dismissDialogSafe(mCustomButterfly);
		if (result.isSucceeded()) {
		    LoginTaskData(user);
		    LoginActivity.this.setResult(33);
		    setResult(111);
		    setResult(77);
		    finish();
		} else {
		    Toast.makeText(LoginActivity.this,
			    R.string.setting_input_pass_name,
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }

    private void LoginTaskData(YKUser user) {
//	try {
//	    mCustomButterfly = CustomButterfly.show(this);
//	} catch (Exception e) {
//	    mCustomButterfly = null;
//	}
	YKSyncTaskManagers.getInstance().downLoadTask(
		new DownloadTaskCallBack() {

		    @Override
		    public void callback(ArrayList<YKTask> taskList,
			    YKResult result) {
			AppUtil.dismissDialogSafe(mCustomButterfly);
			if (result.isSucceeded()) {
			    if (taskList != null && taskList.size() > 0) {
				YKTaskStore.getInstnace()
				.saveTaskList(taskList);
				YKTaskManager.getInstnace()
				.notifyTaskDataChanged();
			    }
			    Toast.makeText(LoginActivity.this,
				    getString(R.string.login_sucess),
				    Toast.LENGTH_LONG).show();
			} else {
			    YKCurrentUserManager.getInstance().clearLoginUser();
			    Toast.makeText(LoginActivity.this,
				    getString(R.string.task_synchroniznonono),
				    Toast.LENGTH_SHORT).show();
			}
		    }
		});
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
	// TrackManager.getInstance().addTrack(
	// TrackUrl.PAGE_CLOSE + "LoginActivity");
    }


    private UMAuthListener umAuthListener = new UMAuthListener() {
	@Override
	public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
	    try {
		mCustomButterfly = CustomButterfly.show(LoginActivity.this);
	    } catch (Exception e) {
		mCustomButterfly = null;
	    }
	    Toast.makeText(getApplicationContext(), "授权成功正在登录", Toast.LENGTH_SHORT).show();
	    for (String key : data.keySet()) {  
		if(key.equals("access_token")){
		    QQToKen = data.get(key);
		}
		if(key.equals("uid")){
		    Uid = data.get(key);
		}

	    }  

	    mShareAPI.getPlatformInfo(LoginActivity.this, platform, umAutInfohListener);
	}

	@Override
	public void onError(SHARE_MEDIA platform, int action, Throwable t) {
	    AppUtil.dismissDialogSafe(mCustomButterfly);
	    Toast.makeText( getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCancel(SHARE_MEDIA platform, int action) {
	    AppUtil.dismissDialogSafe(mCustomButterfly);
	    Toast.makeText( getApplicationContext(), "取消登录", Toast.LENGTH_SHORT).show();
	}
    };

    private UMAuthListener umAutInfohListener = new UMAuthListener() {
	@Override
	public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

	    if (data!=null){
		AuthorUser AuthUserinfo = new AuthorUser();
		String iSQQandSina = String.valueOf(platform);
		Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();  
		if(iSQQandSina.equals("QQ")){
		    while (it.hasNext()) {  
			Map.Entry<String, String> entry = it.next();  
			if(entry.getKey().equals("screen_name")){
			    AuthUserinfo.setNickname(entry.getValue());
			}
			if(entry.getKey().equals("profile_image_url")){
			    AuthUserinfo.setAvatar_url(entry.getValue());
			}
			if(entry.getKey().equals("gender")){
			    AuthUserinfo.setGender(entry.getValue());
			}
			if(!TextUtils.isEmpty(Uid)){
			    AuthUserinfo.setUser_id(Uid);
			}

		    }
		    AuthUserinfo.setType("qq");
		}else if(iSQQandSina.equals("SINA")){
		    String screen_name = null;
		    String profile_image_url = null;
		    String gender = null;
		    String id = null;
		    Map.Entry<String, String> entry = it.next();
		    try {
			JSONObject obj  = new JSONObject(entry.getValue());
			screen_name = obj.getString("screen_name");
			profile_image_url = obj.getString("profile_image_url");
			gender = obj.getString("gender");
			id = obj.getString("id");
		    } catch (JSONException e) {
			e.printStackTrace();
		    }
		    if(!TextUtils.isEmpty(screen_name)){
			AuthUserinfo.setNickname(screen_name);
		    }
		    if(!TextUtils.isEmpty(profile_image_url)){
			AuthUserinfo.setAvatar_url(profile_image_url);
		    }
		    if(!TextUtils.isEmpty(id)){
			AuthUserinfo.setUser_id(id);
		    }
		    if(!TextUtils.isEmpty(gender)){
			String newGender = null;
			if(gender.equals("f")){
			    newGender = "女";
			}else if(gender.equals("m")){
			    newGender = "男";
			}
			AuthUserinfo.setGender(newGender);
		    }
		    AuthUserinfo.setType("sina");
		}else if(iSQQandSina.equals("WEIXIN")){
		    while (it.hasNext()) {  
			Map.Entry<String, String> entry = it.next();  
			if(entry.getKey().equals("nickname")){
			    AuthUserinfo.setNickname(entry.getValue());
			}
			if(entry.getKey().equals("headimgurl")){
			    AuthUserinfo.setAvatar_url(entry.getValue());
			}
			if(entry.getKey().equals("sex")){
			    String sex = entry.getValue();
			    String newGender = null;
			    if(sex.equals("1")){
				newGender = "男";
			    }else{
				newGender = "女";
			    }
			    AuthUserinfo.setGender(newGender);
			}
			if(entry.getKey().equals("openid")){
			    AuthUserinfo.setUser_id(entry.getValue());
			}
		    }

		    AuthUserinfo.setType("wechat");
		}

		if(!TextUtils.isEmpty(QQToKen)){
		    AuthUserinfo.setAccess_token(QQToKen);
		}
		requstLoging(AuthUserinfo);
	    }
	}

	@Override
	public void onError(SHARE_MEDIA platform, int action, Throwable t) {
	    AppUtil.dismissDialogSafe(mCustomButterfly);
	    Toast.makeText( getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCancel(SHARE_MEDIA platform, int action) {
	    AppUtil.dismissDialogSafe(mCustomButterfly);
	    Toast.makeText( getApplicationContext(), "取消登录", Toast.LENGTH_SHORT).show();
	}
    };
}
