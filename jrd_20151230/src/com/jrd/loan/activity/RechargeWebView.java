package com.jrd.loan.activity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jrd.loan.R;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.encrypt.MD5Util;
import com.jrd.loan.net.framework.HttpRequest;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.umeng.UMengEvent;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.util.UuidUtil;
import com.jrd.loan.widget.ProgressWebView;
import com.jrd.loan.widget.WindowView;

public class RechargeWebView extends BaseActivity {
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
  @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
  protected void initViews() {
    /**
     * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据 LOAD_DEFAULT:根据cache-control决定是否从网络上取数据。
     * LOAD_NO_CACHE:不使用缓存，只从网络获取数据. LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
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

        if (url.startsWith(LIANLIAN_PAY_SUCCESS)) {// 连连支付充值成功界面url
          UMengEvent.onEvent(RechargeWebView.this, UMengEvent.EVENT_ID_TRANSFERIN, UMengEvent.EVENT_MODULE_TRANSFERIN_BANKCARD);
          AppInfoPrefs.setRecharge(true);
          isRecharge = true;
          ToastUtil.showShort(RechargeWebView.this, R.string.loan_recharge_success_hing);
          mHandler.sendEmptyMessageDelayed(1, 2000);
        } else {
          view.loadUrl(url);
        }
        return true;
      }
    });

    LoadUrl();
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

  @SuppressLint("HandlerLeak")
  Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {

      switch (msg.what) {
        case 1:
          if (isRecharge) {
            setResult(RESULT_OK);
          }

          finish();
          break;
      }
    };
  };

  @Override
  protected void onDestroy() {
    super.onDestroy();
    webView.removeAllViews();
    webView.destroy();
  }
}
