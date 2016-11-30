package com.yoka.mrskin.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.ClickUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.NetWorkUtil;
import com.yoka.mrskin.util.PersonalWebView;
import com.yoka.mrskin.util.PersonalWebView.YKURIHandler;
import com.yoka.mrskin.util.PersonalWebView.onLoadStateListener;
import com.yoka.mrskin.util.YKDeviceInfo;
import com.yoka.mrskin.util.YKSharelUtil;

public class MySkinManagerActivity extends BaseActivity implements
onLoadStateListener
{
	private static final int REQUEST_CODE = 8;
	private PersonalWebView mWebView;
	private View mBackLayout, mRetestLayout;
	private String normalUrl, mToken, mClienId;
	private View mNoDataLayout, mGoTestLayout;
	private String mTrackTypeId;
	private CustomButterfly mCustomButterfly = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_skin_manager_activity);

		getUrl();
		initView();
		//        TrackManager.getInstance().addTrack(
		//                TrackUrl.PAGE_OPEN + "MySkinManagerActivity");
	}

	@Override
	protected void onDestroy()
	{
		//        TrackManager.getInstance().addTrack(
		//                TrackUrl.PAGE_CLOSE + "MySkinManagerActivity");
		super.onDestroy();
	}

	private void getUrl()
	{
		mClienId = YKDeviceInfo.getClientID();
		if (YKCurrentUserManager.getInstance().isLogin()) {
			mToken = YKCurrentUserManager.getInstance().getUser().getToken();
			normalUrl = YKManager.getUrl() + mClienId + "&auth=" + mToken;
		}
	}

	private void initView()
	{
		mNoDataLayout = findViewById(R.id.myskin_manager_no_data_layout);
		mGoTestLayout = findViewById(R.id.myskin_manager_go_test_layout);
		mWebView = (PersonalWebView) findViewById(R.id.myskin_manager_webview);
		mWebView.setLoadStateListener(this);
		mBackLayout = findViewById(R.id.myskin_manager_back_layout);
		mRetestLayout = findViewById(R.id.myskin_manager_retest_layout);
		if (YKCurrentUserManager.getInstance().getSkinTestState()) {
			mNoDataLayout.setVisibility(View.GONE);
			mWebView.setVisibility(View.VISIBLE);
			mRetestLayout.setVisibility(View.VISIBLE);
			mWebView.loadUrl(this, normalUrl);
		} else {
			mNoDataLayout.setVisibility(View.VISIBLE);
			mRetestLayout.setVisibility(View.GONE);
			mWebView.setVisibility(View.GONE);
		}
		mWebView.setURIHandler(new YKURIHandler()
		{

			@Override
			public boolean handleURI(String url)
			{
				System.out.println("MySkinManagerActivity url = " + url);
				String webpage = YKSharelUtil.tryToGetWebPagemUrl(
						MySkinManagerActivity.this, url);
				if (!TextUtils.isEmpty(webpage)) {
					Uri uri = Uri.parse(url);
					if (!TextUtils.isEmpty(uri.getQueryParameter("url"))) {
						String mWebPager = uri.getQueryParameter("url");
						setTrackId(mWebPager);
						Intent intent = new Intent(MySkinManagerActivity.this,
								YKWebViewActivity.class);
						intent.putExtra("probation_detail_url", mWebPager);
						intent.putExtra("track_type_id", mTrackTypeId);
						if (mWebPager
								.contains("http://hzp.yoka.com/fujun/web/cosmetics/show")) {
							intent.putExtra("track_type", "product");
						} else if (mWebPager
								.contains("http://hzp.yoka.com/fujun/web/news/document")) {
							intent.putExtra("track_type", "information");
						}
						startActivity(intent);
						return true;
					}
				}
				return false;
			}
		});

		mRetestLayout.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (ClickUtil.isFastClick()) {
					return;
				}
				startActivityForResult(new Intent(MySkinManagerActivity.this,
						SkinTestActivity.class), REQUEST_CODE);
				//                TrackManager.getInstance().addTrack(
				//                        TrackUrl.SKIN_TEST_ENTRY + "re_test");
			}
		});

		mBackLayout.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (mWebView != null && mWebView.canGoBack()) {
					mWebView.goBack();
				} else {
					finish();
				}
			}
		});

		mGoTestLayout.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (ClickUtil.isFastClick()) {
					return;
				}
				if(!NetWorkUtil.isNetworkAvailable(MySkinManagerActivity.this)){
					Toast.makeText(MySkinManagerActivity.this, "连接网络再来测试吧~", Toast.LENGTH_SHORT).show();
				}else{
					startActivityForResult(new Intent(MySkinManagerActivity.this,
							SkinTestActivity.class), REQUEST_CODE);
				}
				//                TrackManager.getInstance().addTrack(
				//                        TrackUrl.SKIN_TEST_ENTRY + "user_profile");
			}
		});
	}

	private void setTrackId(String url)
	{
		if (url != null) {
			Uri uri = Uri.parse(url);
			if (!TextUtils.isEmpty(uri.getQueryParameter("id"))) {
				mTrackTypeId = uri.getQueryParameter("id");
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_CODE) {
			if (YKCurrentUserManager.getInstance().getSkinTestState()) {
				if (mWebView != null) {
					mNoDataLayout.setVisibility(View.GONE);
					mWebView.setVisibility(View.VISIBLE);
					mWebView.loadUrl(this, normalUrl);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed()
	{
		if (mWebView != null && mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			finish();
		}
	}

	@Override
	public void onPageStart()
	{
		try {
			mCustomButterfly = CustomButterfly.show(this);
		} catch (Exception e) {
			mCustomButterfly = null;
		}
	}

	@Override
	public void onPageFinished()
	{
		AppUtil.dismissDialogSafe(mCustomButterfly );
	}

	@Override
	public void onRecevicedError()
	{
		AppUtil.dismissDialogSafe(mCustomButterfly );
	}

	@Override
	public void onLoadResource()
	{
		AppUtil.dismissDialogSafe(mCustomButterfly );
	}
}
