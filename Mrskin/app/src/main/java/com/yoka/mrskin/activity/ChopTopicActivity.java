package com.yoka.mrskin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.util.ProgressWebView;

public class ChopTopicActivity extends BaseActivity implements OnClickListener

{
    private LinearLayout mBackText;
    private ProgressWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chop_topic);
        YKActivityManager.getInstance().addActivity(this);
        init();
        Intent getUrl = getIntent();
        String url = getUrl.getStringExtra("urlt");
        mWebView.loadUrl(this, url);
    }

    private void init()
    {

        mBackText = (LinearLayout) findViewById(R.id.chop_back_layout);
        mBackText.setOnClickListener(this);

        mWebView = (ProgressWebView) findViewById(R.id.webView);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
        case R.id.chop_back_layout:
            finish();
            break;

        default:
            break;
        }
    }

    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume()
    {
        super.onResume();
        MobclickAgent.onPageStart("ChopTopicActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
        JPushInterface.onResume(this);
    }

    public void onPause()
    {
        super.onPause();
        MobclickAgent.onPageEnd("ChopTopicActivity"); // 保证 onPageEnd 在onPause
                                                      // 之前调用,因为 onPause
                                                      // 中会保存信息
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        YKActivityManager.getInstance().removeActivity(this);
    }
}
