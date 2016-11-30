package com.allactivity.web.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.allactivity.R;
import com.allactivity.util.HttpThread;

/**
 * Created by 张继 on 2016/11/9.
 * webView
 */

public class WebViewActivity extends Activity implements View.OnClickListener {
    private WebView mWebView;

    private Button mBnt_back;
    private Button mBnt_refresh;
    private TextView mTextView_title;
    private TextView mTextView_error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        mWebView = (WebView) findViewById(R.id.web_view);
//        mWebView.loadUrl("http://192.168.1.10:8080/app02/tt.apk");
        mWebView.loadUrl("http://ucdl.25pp.com/fs08/2016/11/09/1/110_57d8e59f9a25ef6e82e0cb6d6b2a21e6.apk");

        mBnt_back = (Button) findViewById(R.id.back);
        mBnt_refresh = (Button) findViewById(R.id.refresh);
        mTextView_title = (TextView) findViewById(R.id.title);
        mTextView_error = (TextView) findViewById(R.id.error);
        mBnt_back.setOnClickListener(this);
        mBnt_refresh.setOnClickListener(this);


        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                mTextView_title.setText(title);
                super.onReceivedTitle(view, title);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                view.loadUrl("file:///android_asset/index.html");
//                mTextView_error.setText("404 error");
                //隐藏mWebView
                mWebView.setVisibility(View.GONE);
            }
        });
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Log.e("aaa", "url-------->" + url);

//                if (url.endsWith(".apk")) {
                    new HttpThread(url).start();
//                    Uri uri=Uri.parse(url);
//                    Intent intent=new Intent(Intent.ACTION_VIEW,uri);
//                    startActivity(intent);
//                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.refresh:
                //刷新
                mWebView.reload();
                break;
        }
    }
}
