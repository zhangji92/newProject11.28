package com.yoka.mrskin.activity;

import java.net.URLDecoder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.util.ProgressWebView;
import com.yoka.mrskin.util.ProgressWebView.YKURIHandler;
import com.yoka.mrskin.util.YKSharelUtil;

public class ProductFourActivity extends AddTaskWebViewActivity
{
    private TextView mTitle;
    private String mNewUrl;
    private LinearLayout mBack;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_four);
        YKActivityManager.getInstance().addActivity(this);
        init();
        cont();
    }
    private void init(){
        mTitle = (TextView) findViewById(R.id.product_four_title);
        mWebView = (ProgressWebView) findViewById(R.id.webView);
        mBack = (LinearLayout) findViewById(R.id.product_four_back_layout);
        mBack.setOnClickListener(this);
        getData();
    }
    
    private void getData(){
        Intent getInfo = getIntent();
        String title = getInfo.getStringExtra("typeTitle");
        String url   = getInfo.getStringExtra("productUrl");

        if(title.equals("brand")){
            mTitle.setText("按品牌查找");
        }else if(title.equals("catalog")){
            mTitle.setText("按分类查找");
        }else if(title.equals("effect")){
            mTitle.setText("按功效查找");
        }else{
            mTitle.setText("按口碑查找");
        }
        mWebView.loadUrl(ProductFourActivity.this, url);
    }
    
    private void cont(){
        mWebView.setURIHandler(new YKURIHandler()
        {

            @SuppressWarnings("deprecation")
            @Override
            public boolean handleURI(String url)
            {
                
                String searchUrl = YKSharelUtil.tryToSearchmUrl(ProductFourActivity.this, url);
                if(searchUrl != null){
                    Uri uri = Uri.parse(url);
                    String query = uri.getQuery();
                    String spStr[] = query.split("&");
                    String param;
                    for (int i = 0; i < spStr.length; ++i) {
                        param = spStr[i];
                        String paramSplit[] = param.split("=");
                        if (paramSplit.length != 2) {
                            continue;
                        }
                        URLDecoder.decode(paramSplit[1]);
                    }
                    //url
                    mNewUrl = uri.getQueryParameter("url");
                    Intent productUrl = new Intent(ProductFourActivity.this,SearchRackActivity.class);
                    productUrl.putExtra("searNewUrl", mNewUrl);
                    startActivity(productUrl);
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
        case R.id.product_four_back_layout:
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
        MobclickAgent.onPageStart("ProductFourActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
        JPushInterface.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ProductFourActivity"); // 保证 onPageEnd 在onPause
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

