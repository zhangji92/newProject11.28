package com.yoka.mrskin.main;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.YKWebViewActivity2;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.fragment.ExperienceFragment;
import com.yoka.mrskin.fragment.HomeFragment;
import com.yoka.mrskin.fragment.MeFragment;
import com.yoka.mrskin.fragment.PlanFragment;
import com.yoka.mrskin.fragment.ProbationCenterFragment;
import com.yoka.mrskin.fragment.ProductFragment;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.data.YKVersionInfo;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKGetNewVersionCallback;
import com.yoka.mrskin.model.managers.YKGetNewVersionManager;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.model.managers.task.YKTaskManager.HomeCardData;
import com.yoka.mrskin.receiver.ExampleUtil;
import com.yoka.mrskin.util.AlarmHelper;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.NetWorkUtil;

public class MainActivity extends FragmentActivity implements OnClickListener,
LoginStateChangedListener
{
	public final static int REQUEST_CODE_CITY = 1;
	private long mbackPressedTime = -1;
	public static final int REQUEST_CODE_LOGIN_BEAUTIFUL = 102;

	private static final int FRAGMENT_INDEX_HOME = 1;
	public static final int FRAGMENT_INDEX_PRODUCT = 2;
	private static final int FRAGMENT_INDEX_MORE = 3;
	public static final int FRAGMENT_INDEX_TASK = 4;
	// private static final int FRAGMENT_INDEX_READ = 5;
	public static final int FRAGMENT_INDEX_EXPER = 6;
	public static boolean mIsHomeShow;
	public static boolean isHome;

	private Fragment mFragmentX;
	//    private HomeFragment mHomeFragment;
	private HomeFragment mHomeFragment;
	// private PlanFragment mPlanFragment;
	private ProductFragment mProductFragment;
	private ExperienceFragment mExperienceFragment;
	private MeFragment mEFragment;

	private ProbationCenterFragment mProbationCenterFragment;
	private int mPosition = 0;
	// private ViewPager mPager;
	// private List<Fragment> mFragments;

	private LinearLayout mHome, mProduct, mTask, mMore,/* , mRead */
	mExperience;
	public static boolean isForeground = false;
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	private TextView mTaskNumber;
	private View mBottomLayout;
	private ImageView mMe_task;
	private CustomButterfly mCustomButterfly = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d("blackaa", "MainActivity onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		YKActivityManager.getInstance().registerRootActivity(this);
		YKActivityManager.getInstance().addActivity(this);

		// 这是为了应用程序安装完后直接打开，按home键退出后，再次打开程序出现的BUG
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			// 结束你的activity
			finish();
			return;
		}
		// 开机动画
		// startSplashActivity();
		setContentView(R.layout.activity_main);
		String pushInfo = getIntent().getStringExtra("push_info");
		if (!TextUtils.isEmpty(pushInfo)) {
			Intent intent = new Intent(this, YKWebViewActivity2.class);
			intent.putExtra("push_info", pushInfo);
			startActivity(intent);
		}

		//推送跳转本地标识
		if(null != getIntent().getStringExtra("tab")){
			String tabFlag = getIntent().getStringExtra("tab");
			init(Integer.parseInt(tabFlag));

		}else{
			//默认展示Home页
			init(0);
		}
		checkTaskStatus();
		checkVersionFromServer(this);
		uploadTaskData();
		YKActivityManager.getInstance().addActivity(this);
		registerMessageReceiver(); // used for receive msg
		// updateTasknumber();

		if (savedInstanceState != null) {
			int tmpInt = savedInstanceState.getInt("position", -1);
			if (tmpInt != -1) {
				mPosition = tmpInt;
				setSelected(mPosition);
			}
		}
		//        TrackManager.getInstance()
		//                .addTrack(TrackUrl.PAGE_OPEN + "MainActivity");
		checkMeState();
	}

	public boolean isFirstShow() {
		return AppUtil.getSplashState(this);
		// if (!isFirstShow) {
		// AppUtil.saveSplashState(this, true);
		// startActivity(new Intent(this, GuideActivity.class));
		// }
	}

	private void handlerIntent(Intent intent) {
		if (intent == null) {
			return;
		}
		HomeCardData data = (HomeCardData) intent
				.getSerializableExtra(AlarmHelper.EXTRA_DATA_CONTNET);
		if (data == null) {
			return;
		}
		// 拿到子任务
		// YKTaskManager.getInstnace().findSubtaskById(data.getmParent_id(),
		// data.getmId())

		YKTask task = YKTaskManager.getInstnace().findSubtaskById(
				data.getmParent_id(), data.getmId());
		if (task == null) {
			return;
		}
		if (task.isFailed()) {
			Toast.makeText(this, R.string.home_losttask, Toast.LENGTH_LONG)
			.show();
		} else if (task.isFinished()) {
			Toast.makeText(this, R.string.home_wintask, Toast.LENGTH_LONG)
			.show();
		} else {

		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		String pushInfo = getIntent().getStringExtra("push_info");
		if (!TextUtils.isEmpty(pushInfo)) {
			Intent intent2 = new Intent(this, YKWebViewActivity2.class);
			intent2.putExtra("push_info", pushInfo);
			startActivity(intent2);
		}
		if(null != getIntent().getStringExtra("tab")){
			String tabFlag = getIntent().getStringExtra("tab");
			init(Integer.parseInt(tabFlag));

		}else{
			//默认展示Home页
			init(0);
		}
		handlerIntent(getIntent());
	}

	private void checkTaskStatus() {
		YKTaskManager.getInstnace().resetTaskStatusIfNeed();
	}

	private void uploadTaskData() {
		AppContext.postRunnableDelay(new Runnable() {

			@Override
			public void run() {
				if (!YKCurrentUserManager.getInstance().isLogin()) {
					return;
				}
				ArrayList<YKTask> nativeTask = YKTaskManager.getInstnace()
						.getTaskList();
				if (nativeTask == null || nativeTask.size() == 0) {
					return;
				}
				YKSyncTaskManagers.getInstance().uploadTask(nativeTask, null);
			}
		}, 60 * 1000);
	}

	private void init(int tabFlag) {
		mBottomLayout = findViewById(R.id.main_bottom_layout);
		mHome = (LinearLayout) findViewById(R.id.bottom_home_layout);
		mHome.setOnClickListener(this);
		mMore = (LinearLayout) findViewById(R.id.bottom_me_layout);
		mMore.setOnClickListener(this);
		mTask = (LinearLayout) findViewById(R.id.bottom_task_layout);
		mTask.setOnClickListener(this);
		mProduct = (LinearLayout) findViewById(R.id.bottom_productlibrary_layout);
		mProduct.setOnClickListener(this);
		mExperience = (LinearLayout) findViewById(R.id.bottom_experience_layout);
		mExperience.setOnClickListener(this);
		// mRead = (RelativeLayout) findViewById(R.id.bottom_read_layout);
		// mRead.setOnClickListener(this);

		// mTaskNumber = (TextView) findViewById(R.id.bottom_tasknumber);
		mMe_task = (ImageView) findViewById(R.id.me_task);

		//判断初始化Tab---zlz
		switch (tabFlag) {
		case 0://首页
			setSelected(FRAGMENT_INDEX_HOME);
			break;
		case 2://心得
			setSelected(FRAGMENT_INDEX_EXPER);
			break;
		case 3://妆品库
			setSelected(FRAGMENT_INDEX_PRODUCT);
			break;
		case 4://抢试用
			setSelected(FRAGMENT_INDEX_TASK);
			break;
		case 1://我的
			setSelected(FRAGMENT_INDEX_MORE);
			break;
		default:
			setSelected(FRAGMENT_INDEX_HOME);
			break;
		}

		// boolean isLogin = getIntent()
		// .getBooleanExtra("main_login_state", false);
		// if (isLogin) {
		// setSelected(FRAGMENT_INDEX_MORE);
		// } else {
		// setSelected(FRAGMENT_INDEX_HOME);
		// }
		// initData();

	}

	public void showProbationFragment() {
		setSelected(FRAGMENT_INDEX_TASK);
	}

	public void showProductFragment() {
		setSelected(FRAGMENT_INDEX_PRODUCT);
	}
	public void showExperienceFragment() {
		setSelected(FRAGMENT_INDEX_EXPER);
	}
	public void showMeSelectFragment() {
		setSelected(FRAGMENT_INDEX_MORE);
	}


	public void showBottomView() {
		mBottomLayout.setVisibility(View.VISIBLE);
	}

	public void hideBottomView() {
		mBottomLayout.setVisibility(View.GONE);
	}

	public void setSelected(int position) {
		mPosition = position;
		switch (position) {
		case FRAGMENT_INDEX_HOME:
			mHome.setSelected(true);
			mProduct.setSelected(false);
			// mRead.setSelected(false);
			mMore.setSelected(false);
			mTask.setSelected(false);
			mExperience.setSelected(false);
			break;
		case FRAGMENT_INDEX_EXPER:
			mHome.setSelected(false);
			mExperience.setSelected(true);
			mProduct.setSelected(false);
			// mRead.setSelected(false);
			mMore.setSelected(false);
			mTask.setSelected(false);
			break;
		case FRAGMENT_INDEX_PRODUCT:
			mHome.setSelected(false);
			mProduct.setSelected(true);
			// mRead.setSelected(false);
			mMore.setSelected(false);
			mTask.setSelected(false);
			mExperience.setSelected(false);
			break;
		case FRAGMENT_INDEX_TASK:
			mHome.setSelected(false);
			mProduct.setSelected(false);
			// mRead.setSelected(false);
			mMore.setSelected(false);
			mTask.setSelected(true);
			mExperience.setSelected(false);
			break;
		case FRAGMENT_INDEX_MORE:
			mHome.setSelected(false);
			mProduct.setSelected(false);
			// mRead.setSelected(false);
			mMore.setSelected(true);
			mTask.setSelected(false);
			mExperience.setSelected(false);
			break;
		}
		initFragment(position);
	}

	private void initFragment(int index) {

		switch (index) {
		case FRAGMENT_INDEX_HOME:
			showHomeFragment();
			mFragmentX = mHomeFragment;
			break;
		case FRAGMENT_INDEX_PRODUCT:
			showCosmesticFragment();
			mFragmentX = mProductFragment;
			break;
		case FRAGMENT_INDEX_TASK:
			showProbationCenterFragment();
			mFragmentX = mProbationCenterFragment;
			break;
		case FRAGMENT_INDEX_MORE:
			showMeFragment();
			mFragmentX = mEFragment;
			break;
		case FRAGMENT_INDEX_EXPER:
			showExperienceFragmentL();
			mFragmentX = mExperienceFragment;
			break;
		default:
			break;
		}
		
		//其他页面 停止轮播图自动滚动，---v2.7.0 zlz
		if(mIsHomeShow){
			if(null != mHomeFragment && null != mHomeFragment.mPager)
				mHomeFragment.mPager.startAutoPage();
		}else{
			if(null != mHomeFragment && null != mHomeFragment.mPager)
				mHomeFragment.mPager.stopAutoPage();
		}

	}

	// 点击底部按钮页面刷新
	private void replaceFragment(Fragment mFragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (mFragmentX != null) {
			transaction.hide(mFragmentX);
		}
		transaction.show(mFragment);
		if (mHomeFragment != null) {
			//mHomeFragment.onRefresh();
		}
		if (mProductFragment != null) {
			//mProductFragment.onRefresh();
		}
		if (mProbationCenterFragment != null) {
			//mProbationCenterFragment.onRefresh();
		}
		if (mExperienceFragment != null) {
			//mExperienceFragment.onRefresh();
		}
		transaction.commit();
		mFragmentX = mFragment;
	}

	private void showHomeFragment() {
		if (mHomeFragment == null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			mHomeFragment = new HomeFragment();
			transaction.add(R.id.main_top_layout, mHomeFragment);
			transaction.commit();
		} else {
			replaceFragment(mHomeFragment);
		}
		mIsHomeShow = true;
		isHome = true;
		//        TrackManager.getInstance()
		//                .addTrack(TrackUrl.PAGE_OPEN + "HomeFragment");
	}

	public void showCosmesticFragment() {
		if (mProductFragment == null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			if (mProductFragment == null) {
				mProductFragment = new ProductFragment();
				transaction.add(R.id.main_top_layout, mProductFragment);
				if (mFragmentX != null) {
					transaction.hide(mFragmentX);
				}
			}
			transaction.commit();
		} else {
			replaceFragment(mProductFragment);
		}
		mIsHomeShow = false;
		isHome = false;
		//        TrackManager.getInstance().addTrack(
		//                TrackUrl.PAGE_OPEN + "ProductFragment");
	}

	public void showMeFragment() {
		if (mEFragment == null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			if (mEFragment == null) {
				mEFragment = new MeFragment();
				transaction.add(R.id.main_top_layout, mEFragment);
				if (mFragmentX != null) {
					transaction.hide(mFragmentX);
				}
			}
			transaction.commit();
		} else {
			replaceFragment(mEFragment);
		}
		mIsHomeShow = false;
		isHome = false;
		//TrackManager.getInstance().addTrack(TrackUrl.PAGE_OPEN + "MeFragment");
	}

	public void showProbationCenterFragment() {
		if (mProbationCenterFragment == null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			if (mProbationCenterFragment == null) {
				mProbationCenterFragment = new ProbationCenterFragment();
				// mHomeFragment.setSwitchTabHandler(this);
				transaction.add(R.id.main_top_layout, mProbationCenterFragment);
				if (mFragmentX != null) {
					transaction.hide(mFragmentX);
				}
			}
			transaction.commit();
		} else {
			replaceFragment(mProbationCenterFragment);
		}
		mIsHomeShow = false;
		isHome = false;
		//        TrackManager.getInstance().addTrack(
		//                TrackUrl.PAGE_OPEN + "ProbationCenterFragment");
	}

	public void showExperienceFragmentL() {
		if (mExperienceFragment == null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			mExperienceFragment = new ExperienceFragment();
			transaction.add(R.id.main_top_layout, mExperienceFragment);
			if (mFragmentX != null) {
				transaction.hide(mFragmentX);
			}
			mFragmentX = mExperienceFragment;
			transaction.commit();
		} else {
			replaceFragment(mExperienceFragment);
		}
		mIsHomeShow = false;
		isHome = false;
		//        TrackManager.getInstance().addTrack(
		//                TrackUrl.PAGE_OPEN + "ProbationCenterFragment");
	}

	// public void showPlamFragment() {
	// if (mPlanFragment == null) {
	// FragmentManager fragmentManager = getSupportFragmentManager();
	// FragmentTransaction transaction = fragmentManager
	// .beginTransaction();
	// if (mPlanFragment == null) {
	// mPlanFragment = new PlanFragment();
	// // mHomeFragment.setSwitchTabHandler(this);
	// transaction.add(R.id.main_top_layout, mPlanFragment);
	// if (mFragmentX != null) {
	// transaction.hide(mFragmentX);
	// }
	// }
	// transaction.commit();
	// } else {
	// replaceFragment(mPlanFragment);
	// }
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bottom_home_layout:
			setSelected(FRAGMENT_INDEX_HOME);
			break;
		case R.id.bottom_productlibrary_layout:
			setSelected(FRAGMENT_INDEX_PRODUCT);
			break;
			// case R.id.bottom_read_layout:
			// setSelected(FRAGMENT_INDEX_READ);
			// break;
		case R.id.bottom_me_layout:
			setSelected(FRAGMENT_INDEX_MORE);
			break;
		case R.id.bottom_task_layout:
			setSelected(FRAGMENT_INDEX_TASK);
			break;
		case R.id.bottom_experience_layout:
			setSelected(FRAGMENT_INDEX_EXPER);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == REQUEST_CODE_CITY) {
			} else if (requestCode == REQUEST_CODE_LOGIN_BEAUTIFUL) {
				if (YKCurrentUserManager.getInstance().isLogin()) {
					Intent intent = new Intent(this, PlanFragment.class);
					startActivity(intent);
				}
			}

			break;
		case REQUEST_CODE_LOGIN_BEAUTIFUL:
			break;

		case 111://登录成功返回刷新首页---zlz

			if(null != mHomeFragment){
				mHomeFragment.onRefresh();
			}
		default:
			break;
		}
	}
	/**
	 * 刷新首页
	 */
	public void refreshHome(){
		if(null != mHomeFragment){
			mHomeFragment.onRefresh();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
			return true;
		} else {

		}
		return super.onKeyDown(keyCode, event);
	}

	// 按2次退出程序
	@Override
	public void onBackPressed() {
		if (mbackPressedTime == -1
				|| (mbackPressedTime != -1 && (System.currentTimeMillis()
						- mbackPressedTime > 3 * 1000))) {
			Toast.makeText(this, R.string.exit_hint, Toast.LENGTH_SHORT).show();
			mbackPressedTime = System.currentTimeMillis();
		} else if (System.currentTimeMillis() - mbackPressedTime < 3 * 1000) {
			//            TrackManager.getInstance().addTrack(TrackUrl.APP_CLOSE);
			//            TrackManager.getInstance().addTrack(
			//                    TrackUrl.PAGE_CLOSE + "MainActivity");
			releaseData();
			finish();
			mbackPressedTime = -1;
			// 强制关闭，主要为了imageloader能清理缓存
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}

	private void releaseData() {
		if (mHomeFragment != null) {

		}
		System.gc();
	}

	// 退出程序
	public void showBackDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_title)
		.setMessage(R.string.dialog_titletwo)
		.setPositiveButton(R.string.dialog_confirm,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int which) {
				MainActivity.this.finish();
			}
		})
		.setNegativeButton(R.string.dialog_cancle,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int which) {
				dialog.dismiss();
			}
		});
		AlertDialog ad = builder.create();
		ad.show();
	}

	private void checkVersionFromServer(final Context context) {
		try {
			mCustomButterfly = CustomButterfly.show(this);
		} catch (Exception e) {
			mCustomButterfly = null;
		}
		YKGetNewVersionManager.getInstance().postNewVersion(
				new YKGetNewVersionCallback() {

					@Override
					public void callback(YKResult result,
							YKVersionInfo versionInfo) {
						AppUtil.dismissDialogSafe(mCustomButterfly);
						if (result.isSucceeded()) {
							if (versionInfo != null) {
								int newVersion = versionInfo.getmVersionCode();
								int localVersion = AppUtil
										.getAppVersionCode(context);
								if (newVersion > localVersion) {
									AppUtil.hasUpdate(context, versionInfo,
											true);
								}
							}
						}
					}
				});
	}

	@Override
	protected void onResume() {
		// updateTasknumber();
		isForeground = true;
		boolean isPush = AppUtil.getPushState(this);
		if (isHome && !isPush) {
			mIsHomeShow = true;
			//会导致首页重复曝光
			// setSelected(mPosition);
			AppUtil.savePushState(this, false);
		}
		JPushInterface.onResume(this);
		YKActivityManager.getInstance().registerRootActivity(this);
		super.onResume();
		// YKActivityManager.getInstance().activityHidden(this);

		Log.d("MainActivity", "onResume()");

	}



	@Override
	public void onStop() {
		super.onStop();
		YKActivityManager.getInstance().activityHidden(this);
	}

	@Override
	protected void onPause() {
		isForeground = false;
		mIsHomeShow = false;
		JPushInterface.onPause(this);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		try {
			YKActivityManager.getInstance().removeActivity(this);
			unregisterReceiver(mMessageReceiver);
		} catch (Exception e) {
		}
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("position", mPosition);
	}

	public class MessageReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!ExampleUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
			}
		}
	}

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	// public void updateTasknumber() {
	// int taskNumber = YKTaskManager.getInstnace().getUnfinishedTaskNumber();
	// if (YKCurrentUserManager.getInstance().isLogin() && taskNumber != 0) {
	//
	// mTaskNumber.setText(taskNumber + "");
	// mTaskNumber.setVisibility(View.VISIBLE);
	// } else {
	// mTaskNumber.setVisibility(View.GONE);
	// }
	// }

	@Override
	public void onLoginStateChanged(boolean islogin) {
		if (islogin) {
			int taskNumber = YKTaskManager.getInstnace()
					.getUnfinishedTaskNumber();
			if (taskNumber != 0) {
				mTaskNumber.setText(taskNumber + "");
				mTaskNumber.setVisibility(View.VISIBLE);
			} else {
				mTaskNumber.setVisibility(View.GONE);
			}
		} else {
			mTaskNumber.setVisibility(View.GONE);
		}
	}

	public void setMeTaskImageview(boolean bool) {
		if (bool) {
			mMe_task.setBackgroundResource(R.drawable.tab_me_new_selector);
		} else {
			mMe_task.setBackgroundResource(R.drawable.tab_me_selector);
		}
	}

	/**
	 * 检查会员试用等状态
	 */
	private void checkMeState() {
		if (YKCurrentUserManager.getInstance().isLogin()) {
			int taskNumber = YKTaskManager.getInstnace()
					.getUnfinishedTaskNumber();
			int count = YKCurrentUserManager.getInstance().getTrialEventCount();
			if (taskNumber != 0 || count != 0) {
				setMeTaskImageview(true);
			}
		}
	}
}
