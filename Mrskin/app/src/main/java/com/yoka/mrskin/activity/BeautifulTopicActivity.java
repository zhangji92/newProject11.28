package com.yoka.mrskin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;

public class BeautifulTopicActivity extends BaseActivity implements OnClickListener

{
    private LinearLayout mBackText;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beautifultopicul);
        YKActivityManager.getInstance().addActivity(this);
        init();
        Intent getUrl = getIntent();
        String url = getUrl.getStringExtra("urlt");
        mWebView.loadUrl(url);
    }

    private void init() {
        
        mBackText = (LinearLayout) findViewById(R.id.ervrv_layoutback);
        mBackText.setOnClickListener(this);

        mWebView = (WebView) findViewById(R.id.webView);
        MyWebViewClient myWebViewClient = new MyWebViewClient();
        mWebView.setWebViewClient(myWebViewClient);
    }

    class MyWebViewClient extends WebViewClient
    {

        // 重写父类方法，让新打开的网页在当前的WebView中显示
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.ervrv_layoutback:
            finish();
            break;

        default:
            break;
        }
    }
    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("BeautifulTopicActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
        JPushInterface.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BeautifulTopicActivity"); // 保证 onPageEnd 在onPause
                                                        // 之前调用,因为 onPause
                                                        // 中会保存信息
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        YKActivityManager.getInstance().removeActivity(this);
    }
}
