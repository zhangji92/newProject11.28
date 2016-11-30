package com.yoka.mrskin.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.db.PlanTaskDao;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.PlanTask;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.data.YKVersionInfo;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKGetNewVersionCallback;
import com.yoka.mrskin.model.managers.YKGetNewVersionManager;
import com.yoka.mrskin.model.managers.YKPlanTaskManager;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers.SyncTaskCallBack;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;

public class MeFragmentSettingActivity extends BaseActivity implements OnClickListener {
	private TextView mVersion, mVersionNewImage, mLoginOutTextView;
	private LinearLayout mBack;
	private RelativeLayout mRemind, mCheckVersion, mAbout,mAdvice;
	private Context mContext;
	private View mVersionView;
	private CustomButterfly mCustomButterfly = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_fragment_activity);
		YKActivityManager.getInstance().addActivity(this);
		mContext = this;
		init();
	}

	private void init() {
		mBack = (LinearLayout) findViewById(R.id.settingactivity_back_layout);
		mBack.setOnClickListener(this);

		mRemind = (RelativeLayout) findViewById(R.id.activity_setting_layout_remind);
		mRemind.setOnClickListener(this);
		mCheckVersion = (RelativeLayout) findViewById(R.id.activity_setting_layout_check);
		mCheckVersion.setOnClickListener(this);
		mAbout = (RelativeLayout) findViewById(R.id.activity_setting_layout_aboutus);
		mAbout.setOnClickListener(this);
		//		意见反馈
		mAdvice = (RelativeLayout) findViewById(R.id.activity_setting_layout_advice);
		mAdvice.setOnClickListener(this);
		mLoginOutTextView = (TextView) findViewById(R.id.right_login_out);
		mLoginOutTextView.setOnClickListener(this);
		mVersionNewImage = (TextView) findViewById(R.id.setting_version_new);

		mVersion = (TextView) findViewById(R.id.setting_current_version);
		mVersion.setText(getString(R.string.setting_version_code) + getVersionText());

//				Version360();
	}

	//打包发布360渠道时用
	private void Version360(){
		mCheckVersion.setVisibility(View.GONE);
		mVersionView = findViewById(R.id.version_view);
		mVersionView.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settingactivity_back_layout:
			finish();
			break;
		case R.id.activity_setting_layout_remind:
			Intent remind = new Intent(this, SettingRemindActivity.class);
			startActivity(remind);
			break;
		case R.id.activity_setting_layout_check:
			if (AppUtil.isNetworkAvailable(this)) {
				checkVersionFromServer();
			} else {
				Toast.makeText(this, getString(R.string.intent_no), Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.activity_setting_layout_aboutus:
			Intent it = new Intent(this, AboutActivity.class);
			startActivity(it);
			break;
		case R.id.right_login_out:
			AlertDialog.Builder builder  = new Builder(MeFragmentSettingActivity.this);
			builder.setMessage("确认退出吗?");
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					logout();
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();


			break;

		case R.id.activity_setting_layout_advice://意见反馈
			Intent intent= new Intent(MeFragmentSettingActivity.this,SettingAdviceActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private String getVersionText() {
		return AppUtil.getAppVersionName(this);
	}

	// 检查版本号
	public void checkLocalVersion() {

		YKVersionInfo version = YKGetNewVersionManager.getInstance().getmInfo();
		if (version == null) {
			mVersionNewImage.setVisibility(View.GONE);
		} else if (AppUtil.getAppVersionCode(this) < version.getmVersionCode()) {
			mVersionNewImage.setVisibility(View.VISIBLE);
		} else {
			mVersionNewImage.setVisibility(View.GONE);
		}
	}

	private void checkVersionFromServer() {
		checkLocalVersion();
		try {
			mCustomButterfly = CustomButterfly.show(MeFragmentSettingActivity.this);
		} catch (Exception e) {
			mCustomButterfly = null;
		}
		YKGetNewVersionManager.getInstance().postNewVersion(new YKGetNewVersionCallback() {

			@Override
			public void callback(YKResult result, YKVersionInfo versionInfo) {
				AppUtil.dismissDialogSafe(mCustomButterfly);
				if (result.isSucceeded()) {
					if (versionInfo != null) {
						int newVersion = versionInfo.getmVersionCode();
						int localVersion = AppUtil.getAppVersionCode(MeFragmentSettingActivity.this);
						if (newVersion > localVersion) {
							AppUtil.hasUpdate(MeFragmentSettingActivity.this, versionInfo, false);
						} else {
							Toast.makeText(MeFragmentSettingActivity.this, getString(R.string.more_more_current),
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(MeFragmentSettingActivity.this, R.string.more_more_current, Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MeFragmentSettingActivity.this, getString(R.string.intent_error), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		YKActivityManager.getInstance().removeActivity(this);
		//TrackManager.getInstance().addTrack(TrackUrl.PAGE_CLOSE + "MeFragmentSettingActivity");
	}

	@Override
	public void onResume() {
		super.onResume();
		if (YKCurrentUserManager.getInstance().isLogin()) {
			mLoginOutTextView.setVisibility(View.VISIBLE);
		} else {
			mLoginOutTextView.setVisibility(View.GONE);
		}
	}
	/**
	 * 退出登录
	 */
	private void logout() {
		//更新本地待统计数据
		updateNativeCollection();

		ArrayList<YKTask> nativeTask = YKTaskManager.getInstnace().getTaskList();
		if (nativeTask == null || nativeTask.size() == 0) {
			YKCurrentUserManager.getInstance().clearLoginUser();
			mLoginOutTextView.setVisibility(View.GONE);
			Toast.makeText(mContext, getString(R.string.quit_login_success), Toast.LENGTH_SHORT).show();
			setResult(441);//退出登录刷新首页---zlz
			finish();
			return;
		}
		try {
			mCustomButterfly = CustomButterfly.show(MeFragmentSettingActivity.this);
		} catch (Exception e) {
			mCustomButterfly = null;
		}
		/**
		 * 同步美丽计划本地数据
		 */
		YKSyncTaskManagers.getInstance().uploadTask(nativeTask, new SyncTaskCallBack() {

			@Override
			public void callback(YKResult result) {
				AppUtil.dismissDialogSafe(mCustomButterfly);
				if (result.isSucceeded()) {
					YKCurrentUserManager.getInstance().clearLoginUser();
					YKTaskManager.getInstnace().clearAllTask();
					YKTaskManager.getInstnace().notifyTaskDataChanged();
					mLoginOutTextView.setVisibility(View.GONE);
					Toast.makeText(mContext, getString(R.string.quit_login_success), Toast.LENGTH_SHORT).show();
					setResult(441);//退出登录刷新首页---zlz
					finish();
				} else {
					Toast.makeText(mContext, getString(R.string.more_logout_intent), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	/**
	 * 更新本地统计（美丽计划本地
	 */
	private void updateNativeCollection() {
		/*用户token*/
		String authToken = "";
		String userId = "-1";
		if(null != YKCurrentUserManager.getInstance().getUser()){

			authToken = YKCurrentUserManager.getInstance().getUser().getToken();
			userId = YKCurrentUserManager.getInstance().getUser().getId();
		}
		//本地数据读取
		final PlanTaskDao dao = new PlanTaskDao(MeFragmentSettingActivity.this);
		ArrayList<JSONObject> list = dao.queryTask(userId);
		if(list.size() <= 0){
			return;
		}

		/*联网进行统计*/
		YKPlanTaskManager.getInstance().requestFinishTask(authToken,list,new YKPlanTaskManager.Callback(){

			@Override
			public void callback(YKResult result) {
				if(result.isSucceeded()){
//					Toast.makeText(MeFragmentSettingActivity.this, "统计成功~", Toast.LENGTH_SHORT).show();
					dao.remove();

				}else{
					Toast.makeText(MeFragmentSettingActivity.this, "统计失败..."+result.getMessage().toString(), Toast.LENGTH_SHORT).show();
				}

			}

		});
	}

}
