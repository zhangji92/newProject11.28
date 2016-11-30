package com.yoka.mrskin.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.util.ProgressWebView;
import com.yoka.mrskin.util.ProgressWebView.onLoadStateListener;

public class TestCourseActivity extends BaseActivity implements onLoadStateListener
{
    private ProgressWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_course_activity);

        initView();
//        TrackManager.getInstance().addTrack(
//                TrackUrl.PAGE_OPEN + "TestCourseActivity");
    }

    @Override
    protected void onDestroy()
    {
//        TrackManager.getInstance().addTrack(
//                TrackUrl.PAGE_CLOSE + "TestCourseActivity");
        super.onDestroy();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView()
    {
        mWebView = (ProgressWebView) findViewById(R.id.test_course_webview);
        mWebView.setLoadStateListener(this);
        mWebView.loadUrl(this,"http://hzp.yoka.com/fujun/web/tutorial/skin");

        /**
         * 返回
         */
        findViewById(R.id.test_course_back_layout).setOnClickListener(
                new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        if (mWebView.canGoBack()) {
                            mWebView.goBack();
                        } else {
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onPageFinished(WebView view, String url)
    {
        //
        mWebView.loadUrl(this,"javascript:(function() { var videos = document.getElementsByTagName('video'); for(var i=0;i<videos.length;i++){videos[i].play();}})()");
        String js = "javascript: var v=document.getElementsByTagName('video')[0]; "
                + "v.play(); ";
        mWebView.loadUrl(this,js);

    }

    @Override
    public void onRecevicedError(WebView view, String url)
    {
        mWebView.onLoadError();
    }
}
