package com.yoka.mrskin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.track.manager.TrackManager;
import com.yoka.mrskin.track.manager.TrackUrl;
import com.yoka.mrskin.util.ProgressWebView;
import com.yoka.mrskin.util.ProgressWebView.YKURIHandler;
import com.yoka.mrskin.util.WebViewObserver;

public class BeautyRankingActivity extends BaseActivity {

	private LinearLayout mBackLinearLayout;
	private ProgressWebView mWebView;
	private WebViewObserver mWebViewObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beauty_ranking_layout);
		init();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("BeautyRankingActivity");
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("BeautyRankingActivity");
		MobclickAgent.onPause(this);
	}

	private void init() {
		mBackLinearLayout = (LinearLayout) findViewById(R.id.read_back_layout);
		mWebView = (ProgressWebView) findViewById(R.id.beautyRankingWebView);
		mBackLinearLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		mWebView.loadUrl(this, YKManager.getFour() + "rank/page1v5");

		mWebViewObserver = new WebViewObserver(this);
		mWebView.setURIHandler(new YKURIHandler() {

			@Override
			public boolean handleURI(String url) {
				return mWebViewObserver.shouldOverrideUrlLoading(mWebView.getWebview(), url);
			}
		});

	}
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		TrackManager.getInstance().addTrack1(YKManager.getFour() + "rank/page1v5", TrackUrl.REFER_START, "cosmetics");
	}
	@Override
	public void onStop() {
		super.onStart();
		TrackManager.getInstance().addTrack1(YKManager.getFour() + "rank/page1v5", TrackUrl.REFER_CLOSE, "cosmetics");
	}

}
