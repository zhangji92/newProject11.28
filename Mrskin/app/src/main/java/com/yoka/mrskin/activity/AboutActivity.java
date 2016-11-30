package com.yoka.mrskin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.util.AppUtil;

public class AboutActivity extends BaseActivity
{
    private LinearLayout mBack;
    private TextView mTopVersion;
    private RelativeLayout mLaw,mDetalis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YKActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.setting_about);
        init();
    }

    public void init() {
        mBack = (LinearLayout) findViewById(R.id.about_about_layout);
        // 返回按钮
        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLaw = (RelativeLayout) findViewById(R.id.setting_law_layout);
        mLaw.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent goLaw = new Intent(AboutActivity.this,AboutLawActivity.class);
                startActivity(goLaw);
            }
        });
        mDetalis = (RelativeLayout) findViewById(R.id.setting_detalis_layout);
        mDetalis.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent goDetalis = new Intent(AboutActivity.this,AboutDetalisActivity.class);
                startActivity(goDetalis);
            }
        });
        mTopVersion = (TextView) findViewById(R.id.about_setting_version);
        mTopVersion.setText(AppUtil.getAppVersionName(this));
    }
    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AboutActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
        JPushInterface.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AboutActivity"); // 保证 onPageEnd 在onPause
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
