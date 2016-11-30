package com.jrd.loan.activity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jrd.loan.MainActivity;
import com.jrd.loan.R;
import com.jrd.loan.account.LoginActivity;
import com.jrd.loan.api.PayApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.bean.HuoqiTakeoutBean;
import com.jrd.loan.encrypt.MD5Util;
import com.jrd.loan.finance.ProjectIntroduceActivity;
import com.jrd.loan.myaccount.InvestmentSuccessActivity;
import com.jrd.loan.net.framework.HttpRequest;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.umeng.UMengEvent;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.UuidUtil;
import com.jrd.loan.widget.ProgressWebView;
import com.jrd.loan.widget.WindowView;

public class WebViewActivity extends BaseActivity {
	// h5界面进入Android本地的标的详情
	public final static String PROJECT_DETAIL_INFO_URL = "startProDetailInfo?mockId=";

	// h5界面返回Android本地的首页
	private final static String RETURN_HOMEPAGE_INFO_URL = "returnHomePage";

	// 从h5界面点击登录, 调用手机本地的登录
	private final static String LOGIN_FROM_H5_TO_APP = "loginFromH5ToApp";

	// 连连支付充值成功界面url
	private final static String LIANLIAN_PAY_SUCCESS = "https://yintong.com.cn/llpayh5/http/success.html";

	private WindowView windowView;
	private ProgressWebView webView;
	private String getTitle = "君融贷";
	private boolean isRecharge = false;// 是否充值成功

	@Override
	protected int loadWindowLayout() {
		return R.layout.loan_activity_webview;
	}

	@Override
	protected void initTitleBar() {
		if (getIntent().getStringExtra("htmlTitle") != null && !getIntent().getStringExtra("htmlTitle").equals("")) {
			getTitle = getIntent().getStringExtra("htmlTitle");

			if (getTitle.equals("优惠券")) {
				AppInfoPrefs.setWithDraw(true);
			}
		}

		windowView = (WindowView) findViewById(R.id.windowView);
		windowView.setTitle(getTitle);
		windowView.ShowCloseImage();
		windowView.setLeftButtonClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isRecharge) {
					setResult(RESULT_OK);
				}
				finish();
			}
		});

	}

	@Override
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	protected void initViews() {
		/**
		 * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
		 * LOAD_DEFAULT:根据cache-control决定是否从网络上取数据。
		 * LOAD_NO_CACHE:不使用缓存，只从网络获取数据.
		 * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
		 */
		webView = (ProgressWebView) findViewById(R.id.loan_webview);
		WebSettings settings = webView.getSettings();
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		settings.setDefaultTextEncodingName("UTF-8");
		settings.setJavaScriptEnabled(true);

		// android5.0开始使用
		if (Build.VERSION.SDK_INT >= 21) {
			settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				LogUtil.d("----------- h5 url = ", url);

				if (url.contains(PROJECT_DETAIL_INFO_URL)) {// 从H5进入标的详情界面
					showBidScreen(url.substring(url.lastIndexOf("=") + 1));
				} else if (url.contains(RETURN_HOMEPAGE_INFO_URL)) {// 从h5返回app的首页
					showHomePageScreen();
				} else if (url.contains(LOGIN_FROM_H5_TO_APP)) {
					// 从h5界面点击登录, 调用手机本地的登录
					showLoginScreen();
				} else if (url.startsWith(LIANLIAN_PAY_SUCCESS)) {// 连连支付充值成功界面url
					UMengEvent.onEvent(WebViewActivity.this, UMengEvent.EVENT_ID_TRANSFERIN, UMengEvent.EVENT_MODULE_TRANSFERIN_BANKCARD);
					getInvestResult();
				} else {
					view.loadUrl(url);
				}

				return true;
			}

		});

		LoadUrl();
	}

	private void getInvestResult() {
		PayApi.userHuoqiInvestResult(WebViewActivity.this, new OnHttpTaskListener<HuoqiTakeoutBean>() {

			@Override
			public void onTaskError(int resposeCode) {
				DismissDialog();
			}

			@Override
			public void onStart() {
				ShowProDialog(WebViewActivity.this);
			}

			@Override
			public void onFinishTask(HuoqiTakeoutBean bean) {
				DismissDialog();
				if (bean != null && bean.getResultCode() == 0) {
					if (bean.getRecords().isEmpty()) {
						return;
					}

					Intent intent = new Intent(WebViewActivity.this, InvestmentSuccessActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					intent.putExtra("detail", getIntent().getExtras().getString("detail", ""));
					intent.putExtra("amount", getIntent().getExtras().getString("amount", ""));
					intent.putExtra("oprateTime", getIntent().getExtras().getString("oprateTime", ""));
					intent.putExtra("records", bean.getRecords());

					startActivity(intent);
					finish();
				}
			}
		});
	}

	private void showBidScreen(String mockId) {
		Intent intent = new Intent(this, ProjectIntroduceActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("mockId", mockId);
		startActivity(intent);
	}

	private void showHomePageScreen() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private void showLoginScreen() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("come_from", "h5_screen");
		intent.putExtra("htmlUrl", getIntent().getStringExtra("htmlUrl"));
		intent.putExtra("htmlTitle", getIntent().getStringExtra("htmlTitle"));
		startActivity(intent);
	}

	private void LoadUrl() {
		if (UserInfoPrefs.isLogin()) {// 登录
			String timestamp = String.valueOf(System.currentTimeMillis());
			String sessionId = UserInfoPrefs.getSessionId();

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", UserInfoPrefs.getSessionId());
			headerMap.put("sign", MD5Util.getMD5Str(sessionId + timestamp + JrdConfig.getAppKey()));
			headerMap.put("timestamp", timestamp);
			headerMap.put("ver", HttpRequest.VERSION);
			headerMap.put("deviceid", UuidUtil.getUUID());

			webView.loadUrl(getIntent().getStringExtra("htmlUrl"), headerMap);
		} else {// 未登录
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
			headerMap.put("ver", HttpRequest.VERSION);
			headerMap.put("deviceid", UuidUtil.getUUID());

			webView.loadUrl(getIntent().getStringExtra("htmlUrl"), headerMap);
		}

		LogUtil.d("HttpRequest", getIntent().getStringExtra("htmlUrl"));
	}

	// 改写物理按键——返回的逻辑
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack();// 返回上一页面
				return true;
			} else {
				finish();
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.removeAllViews();
		webView.destroy();
	}
}
