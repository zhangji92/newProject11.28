package com.yoka.mrskin.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.YKWebViewActivity;
import com.yoka.mrskin.activity.YKWebViewActivity2;

@SuppressLint("NewApi")
public class PersonalWebView extends LinearLayout
{
	private static final String TAG = PersonalWebView.class.getSimpleName();
	private YKWebView mWebView = null;
	private View mErrorView;
	// private ProgressDialog mProgressDialog;
	private YKURIHandler mURIHandler = null;
	private YKUrlTitle mTitleHandler = null;
	private String mURL;
	private Context mContext;
	private ValueCallback<Uri> mUploadMessage = null;
	private onLoadStateListener mLoadStateListener;
	private onWebViewInputListener mWebViewInputListener;
	private HashMap<String, String> titleList = new HashMap<>();

	public PersonalWebView(Context context)
	{
		super(context);
		init(context);
	}

	public PersonalWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init((Activity) context);
	}

	public PersonalWebView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public void onDestory() {
		if (mWebView != null) {
			mWebView.removeAllViews();
			mWebView.destroy();
			mWebView.stopLoading();
			mWebView = null;
			System.gc();
		}
	}

	public void setLoadStateListener(onLoadStateListener listener) {
		mLoadStateListener = listener;
	}

	public void setWebViewInputListener(onWebViewInputListener listener) {
		mWebViewInputListener = listener;
	}

	public void addJavaScriptInterface(Activity activity,String name){
		mWebView.addJavascriptInterface(activity,"fujunComment");
	}
	
	private void init(Context context) {
		mContext = context;
		setupErrorPage(context);
		mWebView = new YKWebView(context);
		mWebView.addJavascriptInterface(new YKWebViewActivity.getShareInfo(),"fujun2");
		mWebView.addJavascriptInterface(new YKWebViewActivity.getTiralInfo(),"fujun3");
		mWebView.addJavascriptInterface(new YKWebViewActivity2.getShareInfo(),"fujun4");
//		mWebView.addJavascriptInterface(new YKWebViewActivity.getCommentlikeReplyDeleteReport(),"fujunComment");
		mWebView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, 1));
		setOrientation(LinearLayout.VERTICAL);
		addView(mWebView);

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.d(TAG, "url = " + url);
				boolean result = false;
				System.out.println("personalwebview load url = " + url);
				if (mURIHandler != null) {
					result = mURIHandler.handleURI(url);
				}
				if (!result) {
					try {
						URL tmpURL = new URL(url);
						String domain = tmpURL.getHost();
						if (domain.indexOf("yoka.com") != -1) {
							if (!TextUtils.isEmpty(tmpURL.getQuery())) {
								url += "clientID=";
							} else {
								url += "&clientID=";
							}
							Log.d("YKDeviceInfo",
									"///////////" + YKDeviceInfo.getClientID());
							url += YKDeviceInfo.getClientID();
						}
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// view.loadUrl(url);
					return false;
				} else {
					return true;
				}
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (mContext instanceof Activity) {
					Activity activity = (Activity) mContext;
					while (activity != null && activity.getParent() != null) {
						activity = activity.getParent();
					}
					if (activity == null)
						return;
				}
				if (mLoadStateListener != null) {
					mLoadStateListener.onPageStart();
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (mLoadStateListener != null) {
					mLoadStateListener.onPageFinished();
				}
				YKWebViewActivity.reSetTitle(view.getTitle());
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				if (mLoadStateListener != null) {
					mLoadStateListener.onRecevicedError();
				}
				// 隐藏系统出现的页面
				String data = "";
				view.loadUrl("javascript:document.body.innerHTML=\"" + data
						+ "\"");
				onLoadError();
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
						}
						if (mLoadStateListener != null) {
							mLoadStateListener.onLoadResource();
						}
					}
				});
			}
		});

		WebChromeClient webClie = new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				String newTitle = null;
				if (mTitleHandler != null) {

					newTitle = mTitleHandler.getTitle(title);

					Log.d("newTitle", "in--"+newTitle);
				}
				Log.d("newTitle", "out--"+newTitle);
				super.onReceivedTitle(view, newTitle);
			}



			//			public void onReceivedTitle(WebView view, String title) {
			//                String newTitle = null;
			//                if (mTitleHandler != null) {
			//
			//                    newTitle = mTitleHandler.getTitle(title);
			//                    
			//                    Log.d("newTitle", "in--"+newTitle);
			//                }
			//                Log.d("newTitle", "out--"+newTitle);
			//                super.onReceivedTitle(view, newTitle);
			//            }

			// For Android 3.0+
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				if (mWebViewInputListener != null) {
					mWebViewInputListener.openFileChooser(uploadMsg);
				}
			}

			// For Android 3.0+
			public void openFileChooser(ValueCallback uploadMsg,
					String acceptType) {
				if (mWebViewInputListener != null) {
					mWebViewInputListener
					.openFileChooser(uploadMsg, acceptType);
				}
			}

			// For Android 4.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				if (mWebViewInputListener != null) {
					mWebViewInputListener.openFileChooser(uploadMsg,
							acceptType, capture);
				}
			}
		};
		mWebView.setWebChromeClient(webClie);
	}

	public void loadUrl(final Context context, String url) {
		mURL = url;
		mWebView.loadUrl(context, mWebView, url);
		mWebView.setVisibility(View.VISIBLE);
		mErrorView.setVisibility(View.GONE);
	}

	public void clearHistory() {
		mWebView.clearHistory();
	}

	public void setURIHandler(YKURIHandler handler) {
		mURIHandler = handler;
	}

	public void setTitleHandler(YKUrlTitle title) {
		mTitleHandler = title;
	}

	// public void setProgressDialog(ProgressDialog dialog) {
	// if (dialog != null) {
	// mProgressDialog = dialog;
	// Log.d(TAG, "setProgressDialog " + dialog);
	// }
	// }

	public void onResume() {
		mWebView.onResume();
	}

	public void onPause() {
		mWebView.onPause();
	}

	public boolean canGoBack() {
		return mWebView.canGoBack();
	}

	public void goBack() {
		mWebView.goBack();
	}

	public void reFresh() {
		mWebView.reload();
	}

	private void onLoadError() {
		mWebView.setVisibility(View.GONE);
		mErrorView.setVisibility(View.VISIBLE);
	}

	private void reloadCurrentURL() {
		loadUrl(mContext, mURL);
	}

	private void setupErrorPage(Context context) {
		RelativeLayout tmpRelativeLayout = new RelativeLayout(context);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		tmpRelativeLayout.setLayoutParams(param);
		tmpRelativeLayout.setGravity(Gravity.CENTER);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.intent_error, null);

		// TextView textView = new TextView(context);
		// textView.setGravity(Gravity.CENTER);
		param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// param.addRule(RelativeLayout.CENTER_VERTICAL);
		param.addRule(RelativeLayout.CENTER_IN_PARENT);
		// param.setMargins(170, 0, 170, 0);
		// textView.setLayoutParams(param);
		// textView.setBackgroundResource(R.drawable.intent_error);
		// textView.setText(R.string.detail_error);
		tmpRelativeLayout.addView(layout);
		mErrorView = tmpRelativeLayout;
		mErrorView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				reloadCurrentURL();
			}
		});
		mErrorView.setVisibility(View.GONE);
		addView(mErrorView);
	}

	public interface YKURIHandler
	{
		public boolean handleURI(String url);
	}

	public interface YKUrlTitle
	{
		public String getTitle(String title);
	}

	private class YKWebView extends WebView
	{

		public YKWebView(Context context)
		{
			super(context);
			init(context);
		}

		public YKWebView(Context context, AttributeSet attrs)
		{
			super(context, attrs);
			init(context);
		}

		public YKWebView(Context context, AttributeSet attrs, int defStyle)
		{
			super(context, attrs, defStyle);
			init(context);
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("SetJavaScriptEnabled")
		private void init(Context context) {
			WebSettings settings = getSettings();
			settings.setJavaScriptEnabled(true);
			settings.setDomStorageEnabled(true);
			settings.setLoadWithOverviewMode(true);

			settings.setUseWideViewPort(true);
			settings.setDatabaseEnabled(true);
			settings.setGeolocationEnabled(true);
			settings.setPluginState(PluginState.ON);
			String dir = context.getDir("database", Context.MODE_PRIVATE)
					.getPath();
			settings.setDatabasePath(dir);
			settings.setGeolocationDatabasePath(dir);
			settings.setLoadWithOverviewMode(true);
			settings.setAppCacheEnabled(true);
			String cacheDir = context.getDir("cache", Context.MODE_PRIVATE)
					.getPath();
			settings.setAppCachePath(cacheDir);
			settings.setAppCacheMaxSize(1024 * 1024 * 10);// 设置缓冲大小，我设的是8M
			settings.setCacheMode(WebSettings.LOAD_DEFAULT);
			settings.setAllowFileAccess(true);

			settings.setRenderPriority(RenderPriority.HIGH);
			settings.setJavaScriptCanOpenWindowsAutomatically(true);

			settings.setLoadsImagesAutomatically(true);
			//settings.setSupportMultipleWindows(true);
			String oldUserAgentString = settings.getUserAgentString();
			String newUserAgentString = oldUserAgentString + " "
					+ YKDeviceInfo.getUserAgent();
			settings.setUserAgentString(newUserAgentString);
		}

		public void loadUrl(final Context context, WebView webView, String url) {
			Log.d(TAG, "progresswebview load url =" + url);
			webView.loadUrl(url);
		}
	}

	public String getCurrentURL() {
		return mWebView.getUrl();
	}

	public interface onLoadStateListener
	{
		public void onPageStart();

		public void onPageFinished();

		public void onRecevicedError();

		public void onLoadResource();
	}

	public interface onWebViewInputListener
	{

		public void openFileChooser(ValueCallback<Uri> uploadMsg);

		public void openFileChooser(ValueCallback uploadMsg, String acceptType);

		public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType, String capture);
	}
}