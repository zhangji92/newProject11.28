package com.yoka.mrskin.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.adsame.main.AdsameFullAd;
import com.adsame.main.AdsameManager;
import com.adsame.main.FullAdListener;
import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.AdBootActivity;
import com.yoka.mrskin.activity.YKWebViewActivity;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.main.MainActivity;
import com.yoka.mrskin.util.AdBootUtil;

/**
 * 开机动画
 * 
 * @author Y H L
 * 
 */
public class SplashActivity extends BaseActivity implements FullAdListener {
	private Context mContext;
	private ImageView mContentImg;
	private String mExtras;
	public static final int VERSION = 1;
	public static SharedPreferences sp;
	private Bundle bundle;

	private String cID = "107";
	private AdsameFullAd mFullAd;
	/****
	 * PublishID 注意点 : (1)设置一次就可以了 (2)写在创建广告之前
	 ****/
	public String PublishID = "34";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		super.onCreate(savedInstanceState);
		AdsameManager.setPublishID(PublishID);
		setContentView(R.layout.start_splash);
		mContext = SplashActivity.this;
		//请求开机屏广告
		AdBootUtil.getInstance(mContext).getAdd();
		init();
		// 发送策略
		MobclickAgent.updateOnlineConfig(mContext);
		// 禁止默认的页面统计方式
		MobclickAgent.openActivityDurationTrack(false);

		mFullAd = new AdsameFullAd(this, cID);
		mFullAd.setFullAdListener(this);
		mFullAd.setCloseButton(R.drawable.ad_close);
	}

	@SuppressLint("InflateParams")
	public void init() {
		// final boolean isFirstShow = AppUtil.getSplashState(mContext);
		bundle = getIntent().getExtras();

		mContentImg = (ImageView) findViewById(R.id.content_image);
		// try {
		// mContentImg.setBackgroundResource(R.drawable.login_anim);
		// } catch (Exception e) {
		// }

		/* DisplayImageOptions     options = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
    			.resetViewBeforeLoading(true).considerExifParams(true)
    			.build();
         ImageLoader.getInstance().displayImage(mCoverImage.get(0).getmCover().getmURL(), imageOne, options);*/
		/* Glide.with(SplashActivity.this).load(R.drawable.login_360)
		.into(mContentImg);*/
		mContentImg.setBackgroundResource(R.drawable.login_anim);


	}

	private void togo(){
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent;
				sp = getSharedPreferences("frist_welcome", Context.MODE_PRIVATE);
				/**
				 * 如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
				 */
				if (sp.getInt("VERSION", 0) != VERSION) {
					intent = new Intent(mContext, WelcomeActivity.class);
				} 
				else if (AdBootUtil.getInstance(mContext).isAdd()) {
					intent = new Intent(mContext, AdBootActivity.class);
				}
				else {

					intent = new Intent();


				}
				if (bundle != null) {
					mExtras = bundle.getString(JPushInterface.EXTRA_EXTRA);
					// if (!TextUtils.isEmpty(mExtras)) {
					// AppUtil.savePushInfo(mContext, mExtras);
					// }
					intent.putExtra("push_info", mExtras);
				}
				// if (!isFirstShow) {
				// AppUtil.saveSplashState(mContext, true);
				// startAnotherActivity(GuideActivity.class);
				// } else {
				// Intent intent = new Intent(mContext, MainActivity.class);

				System.out.println("ykwebviewactivity mextras splash = " + mExtras);
				startActivity(intent);
				finish();
				// }
			}
		}, 500);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 12:
			goMain();
			break;

		default:
			break;
		}

	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mContentImg!=null){
			mContentImg.setBackgroundResource(0);
		}
		mFullAd.release();

	}

	/**
	 * 友盟统计需要的俩个方法
	 */
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		JPushInterface.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashActivity"); // 保证 onPageEnd 在onPause
		// 之前调用,因为 onPause
		// 中会保存信息
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}

	@Override
	public boolean onClickFullAd(String url) {
		Log.i("AdsameFullAd", "onClickFullAd");
		Intent intent = new Intent(mContext,YKWebViewActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("identification", "index");
		startActivityForResult(intent, 12);
		mFullAd.release();
		return false;
	}

	private void goMain() {
		// 跳转
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		if (bundle != null) {
			mExtras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			// if (!TextUtils.isEmpty(mExtras)) {
			// AppUtil.savePushInfo(mContext, mExtras);
			// }
			intent.putExtra("push_info", mExtras);
		}
		startActivity(intent);
		finish();

	}
	/***
	 * 有两种情况会调用该接口: 1.展示时间结束 2.数据或网络异常
	 */
	@Override
	public void onDismissFullAd() {
		goMain();
		Log.i("AdsameFullAd", "onDismissFullAd");
	}
	/*****
	 * 同时会调用onDismissFullAd
	 */
	@Override
	public void onFailedFullAd(int arg0) {
		goMain();
		Log.i("AdsameFullAd", "onFailedFullAd");

	}
	@Override
	public void onReadyFullAd(int arg0) {

		RelativeLayout parent = (RelativeLayout) findViewById(R.id.fullad_parent);
		parent.setVisibility(View.VISIBLE);
		mFullAd.show(parent);

		Log.i("AdsameFullAd", "onReadyFullAd");

	}

	@Override
	public void onShowFullAd() {
		Log.i("AdsameFullAd", "onShowFullAd");
		Toast.makeText(this, "onShowFullAd", Toast.LENGTH_SHORT).show();

	}


	/***
	 * 屏蔽返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}






}
