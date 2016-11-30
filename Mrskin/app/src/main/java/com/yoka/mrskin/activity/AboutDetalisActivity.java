package com.yoka.mrskin.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.util.AppUtil;

public class AboutDetalisActivity extends BaseActivity{

    private LinearLayout mBack;
    private TextView mVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_detalis);
        init();
    }
    private void init(){
        mBack = (LinearLayout) findViewById(R.id.about_detalis_layout);
        mVersion = (TextView) findViewById(R.id.about_details_version);
        mVersion.setText(AppUtil.getAppVersionName(this));
        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AboutDetalisActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
        JPushInterface.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AboutDetalisActivity"); // 保证 onPageEnd 在onPause
        // 之前调用,因为 onPause
        // 中会保存信息
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }
}
