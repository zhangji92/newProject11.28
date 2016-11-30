package com.yoka.mrskin.util;

import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.HomeFourTopic;

@SuppressLint("NewApi")
public class ProgressWebView extends LinearLayout
{
    private static final String TAG = ProgressWebView.class.getSimpleName();
    private YKWebView mWebView = null;
    private View mErrorView;
    // private ProgressDialog mProgressDialog;
    private CustomProgress mProgressDialog;
    private YKURIHandler mURIHandler = null;
    private YKUrlTitle mTitleHandler = null;
    private String mURL;
    private Context mContext;
    private boolean isNeedCache;
    private onLoadStateListener mLoadStateListener;

    public ProgressWebView(Context context)
    {
        super(context);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init((Activity) context);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    // @Override
    // protected void onDetachedFromWindow() {
    // super.onDetachedFromWindow();
    // mContext = null;
    // mURIHandler = null;
    // mProgressDialog = null;
    // mErrorView = null;
    // mWebView.stopLoading();
    // mWebView = null;
    //
    // }

    public void setIsCache(boolean Iscache) {
        isNeedCache = Iscache;
    }

    public void setLoadStateListener(onLoadStateListener listener) {
        mLoadStateListener = listener;
    }

    private void init(Context context) {
        mContext = context;
        setupErrorPage(context);

        mWebView = new YKWebView(context);
        mWebView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        addView(mWebView);
        // mWebView.addJavascriptInterface(new HomeTopShare.getIntentInfo(),
        // "fujun");
        mWebView.addJavascriptInterface(new HomeFourTopic.getShareInfo(),
                "fujun2");
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "url = " + url);
                boolean result = false;

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
                    //                    view.loadUrl(url);
                    return false;
                } else {
                    //                    view.loadUrl(url);
                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, "onPagestart " + mProgressDialog);
                super.onPageStarted(view, url, favicon);
                if (mProgressDialog == null) {
                    if (mContext instanceof Activity) {
                        Activity activity = (Activity) mContext;
                        while (activity != null && activity.getParent() != null) {
                            activity = activity.getParent();
                        }
                        if (activity == null)
                            return;
                    }
                    try {
                        mProgressDialog = CustomProgress.show(mContext);
                    } catch (Exception e) {
                        mProgressDialog = null;
                    }
                    // mProgressDialog = new ProgressDialog(mContext);
                    // mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    // mProgressDialog.setCancelable(true);
                }
                if (mProgressDialog == null) {
                    return;
                }
                if (mProgressDialog.isShowing()) {
                    return;
                }
                Log.d(TAG, "on pagestarted mProgressDialog show"
                        + mProgressDialog);
                AppUtil.showDialogSafe(mProgressDialog);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "onPageFinished progress dialog dismiss "
                        + mProgressDialog);
                
                if(mLoadStateListener != null){
                    mLoadStateListener.onPageFinished(view,url);
                }
                AppUtil.dismissDialogSafe(mProgressDialog);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                    String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                AppUtil.dismissDialogSafe(mProgressDialog);
                // 隐藏系统出现的页面
                String data = "";
                view.loadUrl("javascript:document.body.innerHTML=\"" + data
                        + "\"");
                onLoadError();
                if(mLoadStateListener != null){
                    mLoadStateListener.onRecevicedError(view, failingUrl);
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                Log.d(TAG, "onLoadResource url =" + url + "   "
                        + mProgressDialog);
                super.onLoadResource(view, url);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                        }
                        AppUtil.dismissDialogSafe(mProgressDialog);
                    }
                });
            }
        });

        WebChromeClient webClie = new WebChromeClient() {

            public void onReceivedTitle(WebView view, String title) {
                String newTitle = null;
                if (mTitleHandler != null) {

                    newTitle = mTitleHandler.getTitle(title);
                }
                super.onReceivedTitle(view, newTitle);
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

    public void onLoadError() {
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

            if (isNeedCache) {
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
            } else {
                settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            }

            settings.setRenderPriority(RenderPriority.HIGH);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);

            settings.setLoadsImagesAutomatically(true);
            settings.setSupportMultipleWindows(true);
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
    public WebView getWebview(){
        return mWebView;
    }

    public interface onLoadStateListener
    {
        public void onRecevicedError(WebView view, String url);

        public void onPageFinished(WebView view, String url);

    }
}