package com.yoka.mrskin.fragment;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.MeFragmentSettingActivity;
import com.yoka.mrskin.activity.MePlanActivity;
import com.yoka.mrskin.activity.MeProbationActivity;
import com.yoka.mrskin.activity.MyExperienceListActivity;
import com.yoka.mrskin.activity.MySkinManagerActivity;
import com.yoka.mrskin.activity.SildingMenuGrassActivity;
import com.yoka.mrskin.activity.SildingMenuMyBagActivity;
import com.yoka.mrskin.activity.UpdateUserInfoActivity;
import com.yoka.mrskin.activity.YKWebViewActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.login.YKUser;
import com.yoka.mrskin.login.YKUserInfo;
import com.yoka.mrskin.main.MainActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKCurrentUserManager.ILoginCallback;
import com.yoka.mrskin.model.managers.YKGetUserInfoManager;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers.SyncTaskCallBack;
import com.yoka.mrskin.model.managers.YKTrialEventCountManager;
import com.yoka.mrskin.model.managers.YKTrialEventCountManager.TrialEventCountCallback;
import com.yoka.mrskin.model.managers.YKUserSignUpManager;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.AvatarUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.RoundImage;

public class MeFragment extends Fragment implements OnClickListener, Observer
{
	private static final int REQUEST_CODE = 6;
	private static final int REQUSET_BAT = 14;

	private RoundImage mSildMenuLoginImage;
	private TextView mTaskNumber, mRightOut, mRightName;
	private LinearLayout mLoginLayout, mMoneyLinearLayout;
	private RelativeLayout mRightTry, mRightTask, mRightZhang, mRightHua,
	mSettting, mRightMe, mRightComment;
	private TextView mTrialCount, mLevelNameTextView, mMoneyTextView,
	mSigned_dayTextView, mIsSignTextView, mRuleTextView,
	mSignNumTextView;
	private ImageView mEditData;//可编辑个人信息图标
	private View mNoLoginLine;
	private Button loginButton, mSignButton;
	private String mUserId;
	private int mUserDay;
	private String mUserMoney;
	private Animation mNumAnimation;
	private ImageView mSettingImageView;
	private CustomButterfly mCustomButterfly = null;

	private static Timer mTimer;

	private  DisplayImageOptions     options1 = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)  
			.showImageForEmptyUri(R.drawable.right_nologin).showImageOnFail(R.drawable.right_nologin).cacheInMemory(true)  
			.cacheOnDisc(true).build();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = View.inflate(getActivity(), R.layout.residemenu_info,
				null);
		return rootView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
		checkTaskNumber();
		checkLoginorNoLogin();
	}

	private void requestEventCount(String userId) {
		YKTrialEventCountManager.getInstance().requestEventCount(userId,
				new TrialEventCountCallback() {

			@Override
			public void callback(YKResult result, int count) {
				if (result.isSucceeded()) {
					setCountTag();
				}
			}
		});
	}

	private void checkTaskNumber() {
		int taskNumber = YKTaskManager.getInstnace().getUnfinishedTaskNumber();
		if (YKCurrentUserManager.getInstance().isLogin() && taskNumber != 0) {

			mTaskNumber.setText(taskNumber + "");
			mTaskNumber.setVisibility(View.VISIBLE);
		} else {
			mTaskNumber.setVisibility(View.GONE);
		}
	}

	private void checkLoginorNoLogin() {
		if (YKCurrentUserManager.getInstance(getActivity()).isLogin()) {
			mRightOut.setVisibility(View.GONE);
		} else {
			mRightOut.setVisibility(View.GONE);
		}
	}

	private void init() {
		mSildMenuLoginImage = (RoundImage) this.getView().findViewById(
				R.id.image_icon);
		mRightName = (TextView) this.getView().findViewById(R.id.tv_username);
		mTaskNumber = (TextView) this.getView().findViewById(
				R.id.right_task_number);
		mSettting = (RelativeLayout) this.getView().findViewById(
				R.id.right_login_setting);
		mNoLoginLine = this.getView().findViewById(R.id.rightsildmenu_line);

		mSettting.setOnClickListener(this);
		mLoginLayout = (LinearLayout) this.getView().findViewById(
				R.id.residmenu_info_loginlayout);

		mRightMe = (RelativeLayout) this.getView().findViewById(R.id.right_me);
		mRightMe.setOnClickListener(this);
		mRightTry = (RelativeLayout) this.getView()
				.findViewById(R.id.right_try);
		mRightTry.setOnClickListener(this);
		mRightTask = (RelativeLayout) this.getView().findViewById(
				R.id.right_task);
		mRightTask.setOnClickListener(this);
		mRightZhang = (RelativeLayout) this.getView().findViewById(
				R.id.right_zhang);
		mRightZhang.setOnClickListener(this);
		mRightHua = (RelativeLayout) this.getView()
				.findViewById(R.id.right_hua);
		mRightHua.setOnClickListener(this);
		mRightOut = (TextView) this.getView()
				.findViewById(R.id.right_login_out);
		mRightOut.setOnClickListener(this);
		loginButton = (Button) this.getView().findViewById(R.id.loginButton);
		loginButton.setOnClickListener(this);
		mTrialCount = (TextView) this.getView().findViewById(
				R.id.right_event_count);
		mRightComment = (RelativeLayout) this.getView().findViewById(
				R.id.right_comments);
		mRightComment.setOnClickListener(this);
		mLevelNameTextView = (TextView) this.getView().findViewById(
				R.id.levelNameTextView);
		mLevelNameTextView.setOnClickListener(this);
		mMoneyLinearLayout = (LinearLayout) this.getView().findViewById(
				R.id.moneyLinearLayout);
		mMoneyLinearLayout.setOnClickListener(this);
		((TextView) this.getView().findViewById(R.id.moneyShopTextView))
		.setOnClickListener(this);
		mMoneyTextView = (TextView) this.getView().findViewById(
				R.id.moneyTextView);
		mSignButton = (Button) this.getView().findViewById(R.id.signButton);
		mSignButton.setOnClickListener(this);
		mSigned_dayTextView = (TextView) this.getView().findViewById(
				R.id.signed_dayTextView);
		mIsSignTextView = (TextView) this.getView().findViewById(
				R.id.isSignTextView);
		mRuleTextView = (TextView) this.getView().findViewById(
				R.id.ruleTextView);
		mRuleTextView.setOnClickListener(this);
		mNumAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.sign_num);
		mSignNumTextView = (TextView) this.getView().findViewById(
				R.id.signNumTextView);
		mSettingImageView = (ImageView) this.getView().findViewById(
				R.id.settingImageView);
		mSettingImageView.setOnClickListener(this);
		mEditData = (ImageView) this.getView().findViewById(R.id.tv_editdata);
		mEditData.setOnClickListener(this);

		YKTaskManager.getInstnace().addObserver(this);
		setCountTag();

		mTimer = new Timer();
		setTimerTask();
		int width = (int) ((getActivity()).getWindowManager()
				.getDefaultDisplay().getWidth());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, width / 4, 1);
		mRightMe.setLayoutParams(lp);
		mRightHua.setLayoutParams(lp);
	}

	public void setTimerTask() {
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				doActionHandler.sendMessage(message);
			}
		}, 1000, 1000/* 表示1000毫秒之後，每隔10分钟(600000)執行一次 */);
	}

	private void setCountTag() {
		if (YKCurrentUserManager.getInstance().isLogin()) {
			int count = YKCurrentUserManager.getInstance().getTrialEventCount();
			MainActivity main = (MainActivity) getActivity();
			if (count > 0) {
				mTrialCount.setVisibility(View.VISIBLE);
				if (main != null)
					main.setMeTaskImageview(true);
				mTrialCount.setText(count + "");
			} else {
				mTrialCount.setVisibility(View.GONE);
				if (main != null)
					main.setMeTaskImageview(false);
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler doActionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int msgId = msg.what;
			switch (msgId) {
			case 1:
				// do some action
				isShowNumber();

				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE
				&& resultCode == FragmentActivity.RESULT_OK) {
			MainActivity main = (MainActivity) getActivity();
			main.setSelected(MainActivity.FRAGMENT_INDEX_PRODUCT);

		}

		if (requestCode == REQUSET_BAT
				&& resultCode == FragmentActivity.RESULT_OK) {
			MainActivity main = (MainActivity) getActivity();
			main.setSelected(MainActivity.FRAGMENT_INDEX_TASK);
		}
		Log.d("refresh", resultCode+"--");
		switch (resultCode) {
		case 441://退出登录
			//刷新首页(投票)---zlz
			((MainActivity)getActivity()).refreshHome();
			break;

		default:
			break;
		}

		// if (requestCode == RESULT_PRODUCT) {
		// if (resultCode == RESULT_PRODUCT) {
		// MainActivity main = (MainActivity) getActivity();
		// main.closeMenu();
		// main.showProductFragment();
		// }
		// }
		// if (requestCode == RESULT_KEYWORD) {
		// if (resultCode == RESULT_KEYWORD) {
		// MainActivity main = (MainActivity) getActivity();
		// main.closeMenu();
		// main.showProductFragment();
		// }
		// }

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_login_setting:
			Intent rightSetting = new Intent(getActivity(),
					MeFragmentSettingActivity.class);
			startActivityForResult(rightSetting, 44);

			break;
		case R.id.loginButton:
			if (!YKCurrentUserManager.getInstance().isLogin()) {
				YKActivityManager.getInstance().showLogin(getActivity(),
						new ILoginCallback() {

					@Override
					public void loginCallback(YKResult result,
							YKUser user) {
						if (result.isSucceeded()) {
							setCountTag();
							//登录成功刷新首页(投票)---zlz
							((MainActivity)getActivity()).refreshHome();
						}
					}
				});
			}
			break;
		case R.id.right_me://
			checkLogin(MySkinManagerActivity.class);
			break;
		case R.id.right_try:
			MainActivity main = (MainActivity) getActivity();
			main.setMeTaskImageview(false);
			checkLogin(MeProbationActivity.class);
			break;
		case R.id.right_zhang:
			checkLogin(SildingMenuGrassActivity.class);
			break;
		case R.id.right_hua:
			checkLogin(SildingMenuMyBagActivity.class);
			break;
		case R.id.right_task:
			MainActivity main1 = (MainActivity) getActivity();
			main1.setMeTaskImageview(false);
			checkLogin(MePlanActivity.class);
			break;
		case R.id.right_login_out:
			logout();
			break;
		case R.id.right_comments:
			// TODO
			checkLogin(MyExperienceListActivity.class);
			break;
			// 当天签到
		case R.id.signButton:
			YKUserSignUpManager.getInstance().postUserSignUpManager(mUserId,
					mUserMoney, new YKUserSignUpManager.Callback() {

				@Override
				public void callback(YKResult result, String code,
						String day, String money) {
					if ("1".equals(code)) {
						if (!TextUtils.isEmpty(money))
							mMoneyTextView.setText(money);
						mSignButton.setVisibility(View.GONE);
						mIsSignTextView.setVisibility(View.VISIBLE);
						mSigned_dayTextView.setVisibility(View.VISIBLE);
						mSigned_dayTextView
						.setText("已连续签到" + day + "天");

						startAnimation();
					} else {
						Toast.makeText(getActivity(),
								R.string.intent_error,
								Toast.LENGTH_SHORT).show();
					}

				}
			});

			break;
			// 签到规则页面
		case R.id.ruleTextView:
			if (AppUtil.isNetworkAvailable(getActivity())) {
				Intent shareTop = new Intent(getActivity(),
						YKWebViewActivity.class);
				String url = YKManager.getFour() + "user/signrules?1=1";
				Uri uri = Uri.parse(url);
				shareTop.putExtra("probation_detail_url", url);
				shareTop.putExtra(YKWebViewActivity.URL_INTENT_TILTE, "签到规则");

				startActivity(shareTop);

			} else {
				Toast.makeText(getActivity(), getString(R.string.intent_no),
						Toast.LENGTH_SHORT).show();
			}
			break;
			// 积分商城页面
		case R.id.moneyShopTextView:
			if (AppUtil.isNetworkAvailable(getActivity())) {
				Intent shareTop = new Intent(getActivity(),
						YKWebViewActivity.class);
				YKWebViewActivity.SHOP_CHANGE = false;
				String url = YKManager.getFour() + "mall/index?1=1";
				Uri uri = Uri.parse(url);
				shareTop.putExtra("probation_detail_url", url);
				shareTop.putExtra(YKWebViewActivity.URL_INTENT_TILTE, "优币商城");
				shareTop.putExtra(YKWebViewActivity.U_NUM_INTENT,mMoneyTextView.getText().toString());
				startActivity(shareTop);
			} else {
				Toast.makeText(getActivity(), getString(R.string.intent_no),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.settingImageView:
			Intent setting = new Intent(getActivity(),
					MeFragmentSettingActivity.class);
			startActivity(setting);
			break;
		case R.id.tv_editdata:
			Intent intenttt = new Intent(getActivity(),
					UpdateUserInfoActivity.class);
			startActivity(intenttt);
			break;

		default:
			break;
		}
	}

	/**
	 * 按照之前方式进行登录后页面操作，建议未来统一放到onActivityResult去处理。
	 * 
	 * @param c
	 */
	@SuppressWarnings("rawtypes")
	private void checkLogin(final Class c) {
		if (YKCurrentUserManager.getInstance().isLogin()) {
			Intent rightLogin = new Intent(getActivity(), c);
			if (c.getName().equals(
					"com.yoka.mrskin.activity.MyExperienceListActivity")) {
				rightLogin.putExtra(
						MyExperienceListActivity.EXPERIENCE_INTENT_USERID,
						YKCurrentUserManager.getInstance(getActivity())
						.getUser().getId());
			}
			if (c.getName().equals(
					"com.yoka.mrskin.activity.SildingMenuGrassActivity")
					|| c.getName()
					.equals("com.yoka.mrskin.activity.SildingMenuMyBagActivity")) {
				startActivityForResult(rightLogin, REQUEST_CODE);
			} else if (c.getName().equals(
					"com.yoka.mrskin.activity.ProbationActivity")) {
				startActivityForResult(rightLogin, REQUEST_CODE);
			} else if (c.getName().equals(
					"com.yoka.mrskin.activity.MeProbationActivity")) {
				startActivityForResult(rightLogin, REQUSET_BAT);
			} else {
				startActivity(rightLogin);
			}

		} else {
			YKActivityManager.getInstance().showLogin(getActivity(),
					new ILoginCallback() {

				@Override
				public void loginCallback(YKResult result, YKUser user) {
					if (result.isSucceeded()) {
						Intent rightLogin = new Intent(getActivity(), c);
						if (c.getName()
								.equals("com.yoka.mrskin.activity.MyExperienceListActivity")) {
							rightLogin
							.putExtra(
									MyExperienceListActivity.EXPERIENCE_INTENT_USERID,
									YKCurrentUserManager
									.getInstance(
											getActivity())
											.getUser().getId());
						}
						if (c.getName()
								.equals("com.yoka.mrskin.activity.SildingMenuGrassActivity")
								|| c.getName()
								.equals("com.yoka.mrskin.activity.SildingMenuMyBagActivity")) {
							startActivityForResult(rightLogin,
									REQUEST_CODE);
						} else if (c
								.getName()
								.equals("com.yoka.mrskin.activity.ProbationActivity")) {
							startActivityForResult(rightLogin,
									REQUEST_CODE);
						} else if (c
								.getName()
								.equals("com.yoka.mrskin.activity.MeProbationActivity")) {
							startActivityForResult(rightLogin,
									REQUSET_BAT);
						} else {
							startActivity(rightLogin);
						}
					}
				}
			});
		}
	}

	private void startAnimation() {
		mSignNumTextView.setVisibility(View.VISIBLE);
		mSignNumTextView.startAnimation(mNumAnimation);
		new Handler().postDelayed(new Runnable() {
			public void run() {

				mSignNumTextView.setVisibility(View.GONE);

			}
		}, 1000);
	}

	private void logout() {
		ArrayList<YKTask> nativeTask = YKTaskManager.getInstnace()
				.getTaskList();
		if (nativeTask == null || nativeTask.size() == 0) {
			YKCurrentUserManager.getInstance().clearLoginUser();
			// imageView.setImageResource(R.drawable.default_user_bg);
			mRightName.setText(getString(R.string.login_please));
			mSildMenuLoginImage.setImageResource(R.drawable.right_nologin);
			mNoLoginLine.setVisibility(View.GONE);
			mRightOut.setVisibility(View.GONE);
			mTrialCount.setVisibility(View.GONE);
			Toast.makeText(getActivity(),
					getString(R.string.quit_login_success), Toast.LENGTH_SHORT)
					.show();
			return;
		}

		try {
			mCustomButterfly = CustomButterfly.show(getActivity());
		} catch (Exception e) {
			mCustomButterfly = null;
		}
		YKSyncTaskManagers.getInstance().uploadTask(nativeTask,
				new SyncTaskCallBack() {

			@Override
			public void callback(YKResult result) {
				AppUtil.dismissDialogSafe(mCustomButterfly);
				if (result.isSucceeded()) {
					YKCurrentUserManager.getInstance().clearLoginUser();
					YKTaskManager.getInstnace().clearAllTask();
					YKTaskManager.getInstnace().notifyTaskDataChanged();
					// imageView
					// .setImageResource(R.drawable.loading_icon);
					mRightName
					.setText(getString(R.string.login_please));
					mSildMenuLoginImage
					.setImageResource(R.drawable.right_nologin);
					mTrialCount.setVisibility(View.GONE);
					mRightOut.setVisibility(View.GONE);
					mNoLoginLine.setVisibility(View.GONE);
					Toast.makeText(getActivity(),
							getString(R.string.quit_login_success),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(),
							getString(R.string.more_logout_intent),
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public boolean isShowNumber() {
		int count = YKCurrentUserManager.getInstance().getTrialEventCount();
		int taskNumber = YKTaskManager.getInstnace().getUnfinishedTaskNumber();
		int newShowNymber = count + taskNumber;
		MainActivity main = (MainActivity) getActivity();
		checkTaskNumber();
		if (count > 0) {
			mTrialCount.setVisibility(View.VISIBLE);

			mTrialCount.setText(count + "");
		} else {
			mTrialCount.setVisibility(View.GONE);

		}
		if (newShowNymber > 0) {
			if (main != null)
				main.setMeTaskImageview(true);
			return true;
		} else {
			if (main != null)
				main.setMeTaskImageview(false);
			return false;
		}
	}
	//切换fragment 刷新优币
	@Override
	public void onHiddenChanged(boolean hidden) {
		if(!hidden){
			//显示缓存的 签到等用户信息
			showUserInfo();
		}
	}
	/**
	 * 友盟统计需要的俩个方法
	 */
	public void onResume() {
		super.onResume();
		Log.d("money", "onResume()");
		checkLoginorNoLogin();
		// 展示图片
		if (YKCurrentUserManager.getInstance().isLogin()) {
			YKUser user = YKCurrentUserManager.getInstance().getUser();
			requestEventCount(user.getId());
			mRightName.setText(user.getName());
			loginButton.setVisibility(View.GONE);
			mRightName.setVisibility(View.VISIBLE);
			mEditData.setVisibility(View.VISIBLE);
			mUserId = user.getId();
			//显示缓存的 签到等用户信息
			showUserInfo();

			getUseInfo();
			try {
				String imageUrl = AvatarUtil.getInstance(getActivity()).getAvatarPath();
				ImageLoader.getInstance().displayImage("file://"+imageUrl, mSildMenuLoginImage, options1);
				//     Glide.with(MrSkinApplication.getApplication()).load(imageUrl).into(mSildMenuLoginImage);
			} catch (Exception e) {
				mSildMenuLoginImage.setImageResource(R.drawable.right_nologin);
			}
			mNoLoginLine.setVisibility(View.GONE);
			mLoginLayout.setVisibility(View.VISIBLE);



		} else {
			mLoginLayout.setVisibility(View.INVISIBLE);
			mLevelNameTextView.setVisibility(View.INVISIBLE);
			MainActivity main = (MainActivity) getActivity();
			if (main != null)
				main.setMeTaskImageview(false);
			mRightName.setText(getString(R.string.login_please));
			loginButton.setVisibility(View.VISIBLE);
			mRightName.setVisibility(View.GONE);
			mEditData.setVisibility(View.GONE);
			mNoLoginLine.setVisibility(View.GONE);
			mTrialCount.setVisibility(View.INVISIBLE);
			mSildMenuLoginImage.setImageResource(R.drawable.right_nologin);

			/*
			 * mLevelNameTextView.setVisibility(View.VISIBLE);
			 * loginButton.setVisibility(View.GONE);
			 * mRightName.setVisibility(View.VISIBLE);
			 */
		}
		MobclickAgent.onPageStart("MeFragment"); // 统计页面
		MobclickAgent.onResume(getActivity()); // 统计时长
	}

	private void showUserInfo() {
		//没网情况下展示缓存用户信息---zlz
		YKUserInfo info = YKCurrentUserManager.getInstance().getYkUserInfo(getActivity());
		if(null != info){


			mUserMoney = info.getMoney();
			mMoneyTextView.setText(mUserMoney);
			mUserDay = info.getSigned_day();
			mLevelNameTextView.setText(info
					.getLevelname()
					+ ","
					+ info.getUser_level());
			if (info.getIs_signed()!= null && info.getIs_signed().equals("0")) {
				mIsSignTextView.setVisibility(View.GONE);
				mSigned_dayTextView.setVisibility(View.GONE);
				mSignButton.setVisibility(View.VISIBLE);
			} else {
				mSignButton.setVisibility(View.GONE);
				mIsSignTextView.setVisibility(View.VISIBLE);
				mSigned_dayTextView.setVisibility(View.VISIBLE);
				mSigned_dayTextView.setText("已连续签到"
						+ info.getSigned_day() + "天");
			}
		}

	}

	private void getUseInfo() {
		YKGetUserInfoManager.getInstance().postGetUserInfoManager(mUserId,
				new YKGetUserInfoManager.Callback() {

			@Override
			public void callback(YKResult result, YKUserInfo mYkUserInfo) {
				if(result.isSucceeded()){
					if (mYkUserInfo != null) {
						YKCurrentUserManager.getInstance().saveYkUserInfo(mYkUserInfo, getActivity());
						AvatarUtil.getInstance(getActivity()).AsyncHttpFileDown(mYkUserInfo.getAvatar().getmHeadUrl(),handler);
						mLoginLayout.setVisibility(View.VISIBLE);
						mLevelNameTextView.setVisibility(View.VISIBLE);
						mUserMoney = mYkUserInfo.getMoney();
						mMoneyTextView.setText(mUserMoney);
						Log.d("money", mUserMoney+"net");
						mUserDay = mYkUserInfo.getSigned_day();
						mLevelNameTextView.setText(mYkUserInfo
								.getLevelname()
								+ ","
								+ mYkUserInfo.getUser_level());
						if (mYkUserInfo.getIs_signed().equals("0")) {
							mIsSignTextView.setVisibility(View.GONE);
							mSigned_dayTextView.setVisibility(View.GONE);
							mSignButton.setVisibility(View.VISIBLE);
						} else {
							mSignButton.setVisibility(View.GONE);
							mIsSignTextView.setVisibility(View.VISIBLE);
							mSigned_dayTextView.setVisibility(View.VISIBLE);
							mSigned_dayTextView.setText("已连续签到"
									+ mYkUserInfo.getSigned_day() + "天");
						}
					}
				}else{
					if (AppUtil.isNetworkAvailable(getActivity())) {
						Toast.makeText(getActivity(), R.string.intent_error,
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(), R.string.intent_no, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MeFragment"); // 保证 onPageEnd 在onPause
		// 之前调用,因为 onPause
		// 中会保存信息
		MobclickAgent.onPause(getActivity());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		YKTaskManager.getInstnace().deleteObserver(this);
		mTimer.cancel();
	}

	@Override
	public void update(Observable observable, Object data) {
		checkTaskNumber();
	}
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 201:
				String path = (String) msg.obj;

				// Glide.with(MrSkinApplication.getApplication()).load(path).into(mSildMenuLoginImage);
				ImageLoader.getInstance().displayImage("file://"+path, mSildMenuLoginImage, options1);

				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};
}
