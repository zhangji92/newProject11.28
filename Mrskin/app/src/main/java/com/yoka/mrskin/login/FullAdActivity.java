package com.yoka.mrskin.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adsame.main.AdsameFullAd;
import com.adsame.main.AdsameManager;
import com.adsame.main.FullAdListener;
import com.yoka.mrskin.R;
import com.yoka.mrskin.main.MainActivity;
/**
 * 全屏广告(启动图)
 * @author zlz
 * @Data 2016年9月6日
 */
public class FullAdActivity extends Activity implements FullAdListener {
	private String cID = "107";
	private AdsameFullAd mFullAd;
	/****
	 * PublishID 注意点 : (1)设置一次就可以了 (2)写在创建广告之前
	 ****/
	public String PublishID = "34";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AdsameManager.setPublishID(PublishID);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.activity_full_ad);


		mFullAd = new AdsameFullAd(this, cID);
		mFullAd.setFullAdListener(this);
		mFullAd.setCloseButton(R.drawable.ad_close);

	}
	private void go() {
		// 跳转
		Intent intent = new Intent(FullAdActivity.this, MainActivity.class);
		this.startActivity(intent);
		this.finish();

	}
	@Override
	public boolean onClickFullAd(String arg0) {
		Log.i("AdsameFullAd", "onClickFullAd");
		//		Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
		return true;
	}
	/***
	 * 有两种情况会调用该接口: 1.展示时间结束 2.数据或网络异常
	 */
	@Override
	public void onDismissFullAd() {
		go();
		Log.i("AdsameFullAd", "onDismissFullAd");
		Toast.makeText(this, "onDismissFullAd", Toast.LENGTH_SHORT).show();
	}
	/*****
	 * 同时会调用onDismissFullAd
	 */
	@Override
	public void onFailedFullAd(int arg0) {
		go();
		Log.i("AdsameFullAd", "onFailedFullAd");

	}
	@Override
	public void onReadyFullAd(int arg0) {
		RelativeLayout parent = (RelativeLayout) findViewById(R.id.fullad_parent);
		mFullAd.show(parent);
		Toast.makeText(this, "onReadyFullAd", Toast.LENGTH_SHORT).show();

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
	@Override
	protected void onDestroy() {
		mFullAd.release();
		super.onDestroy();
	}


}
