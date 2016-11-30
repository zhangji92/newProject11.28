package com.yoka.mrskin.login;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;

public class AdWebViewActivity extends BaseActivity{
	private static final String TAG = AdWebViewActivity.class.getSimpleName().toString();
	public static final String AD_URL = "ad_url";
	private WebView mWebView;
	private String mUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_webview_activity);
		mWebView = (WebView) findViewById(R.id.ad_webview);
		mUrl = getIntent().getStringExtra(AD_URL);
		mWebView.loadUrl(mUrl);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);   //在当前的webview中跳转到新的url

				return true;
			}
		});

	}

	public boolean onKeyDown(int keyCoder,KeyEvent event){  
		if(mWebView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){  
			mWebView.goBack();   //goBack()表示返回webView的上一页面  
			return true;  
		}  else{
			finish();
		}
		return false;  
	}  

}
