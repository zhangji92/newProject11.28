package com.yoka.mrskin.activity;

import java.net.URLDecoder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.ProgressWebView;
import com.yoka.mrskin.util.ProgressWebView.YKURIHandler;

public class SearchRackActivity extends AddTaskWebViewActivity implements OnClickListener
{
    private TextView mBack,mSearch;
    private EditText mSearchEdit;
    private String mProductUrl,mSearchUrl,NewUrl;
    private final String URL = YKManager.getFour()+"product/list?keyword=";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_rack);
        YKActivityManager.getInstance().addActivity(this);
        init();
    }
    private void init(){
        mBack = (TextView) findViewById(R.id.search_rack_back);
        mBack.setOnClickListener(this);
        mSearch = (TextView) findViewById(R.id.search_magnifier);
        mSearch.setOnClickListener(this);
        mSearchEdit = (EditText) findViewById(R.id.serach_and_search);
        mSearchEdit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchEdit.setOnEditorActionListener(editListener);
        mWebView = (ProgressWebView) findViewById(R.id.search_progress);
        //mLayoutSearch = (LinearLayout) findViewById(R.id.search_layout_but);

        Intent productUrl = getIntent();
        mProductUrl = productUrl.getStringExtra("productUrl");
        if(mProductUrl != null){
            //ProductFourActivity跳的
            mWebView.loadUrl(SearchRackActivity.this, mProductUrl);
        }
        Intent searchUrl = getIntent();
        //ProductFourActivity跳的
        mSearchUrl = searchUrl.getStringExtra("searNewUrl");
        if(mSearchUrl != null){

            mWebView.loadUrl(SearchRackActivity.this, mSearchUrl);
        }

            mWebView.setURIHandler(new YKURIHandler()
            {

                @SuppressWarnings("deprecation")
                @Override
                public boolean handleURI(String url)
                {
                    if(url != null){
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
                        String mCourseUrl = uri.getQueryParameter("url");

                        if(mCourseUrl != null){
                            Intent course = new Intent(SearchRackActivity.this,HomeTopShare.class);
                            course.putExtra("url", mCourseUrl);
                            course.putExtra("urlall", url);
                            startActivity(course);
                        }
                        return true;
                    }
                    return false;
                }
            });
        }


        private OnEditorActionListener editListener = new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String info = mSearchEdit.getText().toString().trim();

                    StringBuilder builder = new StringBuilder(URL);
                    NewUrl = String.valueOf(builder.append(info));
                    if(info.equals("")){
                        mSearchEdit.setSelection(0);//调整光标到第一行
                    }else{
                        mWebView.loadUrl(SearchRackActivity.this, NewUrl);
                    }
                    return true;
                }
                return false;
            }
        };
        @Override
        public void onClick(View v)
        {
            switch (v.getId()) {
            case R.id.search_rack_back:
                finish();
                break;
            case R.id.search_magnifier:
                String info = mSearchEdit.getText().toString().trim();
                if(info.equals("")){
                    mSearchEdit.setSelection(0);//调整光标到第一行
                }else{
                    StringBuilder builder = new StringBuilder(URL);
                    NewUrl = String.valueOf(builder.append(info));
                    mWebView.loadUrl(SearchRackActivity.this, NewUrl);
                }
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
            MobclickAgent.onPageStart("SearchRackActivity"); // 统计页面
            MobclickAgent.onResume(this); // 统计时长
            JPushInterface.onResume(this);
        }

        public void onPause() {
            super.onPause();
            MobclickAgent.onPageEnd("SearchRackActivity"); // 保证 onPageEnd 在onPause
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