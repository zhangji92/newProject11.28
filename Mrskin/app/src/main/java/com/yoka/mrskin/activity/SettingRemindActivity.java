package com.yoka.mrskin.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.util.AppUtil;

public class SettingRemindActivity extends BaseActivity implements
        OnCheckedChangeListener
{
    private LinearLayout mBack;
    private CheckBox mCheckBoxNew, mCheckBoxSound, mCheckBoxShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_remind);
        YKActivityManager.getInstance().addActivity(this);
        init();
    }

    private void init() {
        mBack = (LinearLayout) findViewById(R.id.setting_remind_layout);
        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCheckBoxNew = (CheckBox) findViewById(R.id.setting_remind_yesimage);
        mCheckBoxSound = (CheckBox) findViewById(R.id.setting_sound_image);
        mCheckBoxShake = (CheckBox) findViewById(R.id.setting_shake_image);
        mCheckBoxNew.setOnCheckedChangeListener(this);
        mCheckBoxSound.setOnCheckedChangeListener(this);
        mCheckBoxShake.setOnCheckedChangeListener(this);
        getSharedPreferences();
    }

    private void getSharedPreferences() {
        mCheckBoxNew.setChecked(AppUtil.getCanRemindAll(this));
        mCheckBoxSound.setChecked(AppUtil.getCanRemindRING(this));
        mCheckBoxShake.setChecked(AppUtil.getCanRemindSHAKE(this));
    }

    private void savePushStatus() {
        AppUtil.saveCanRemindAll(this, mCheckBoxNew.isChecked());
        AppUtil.saveCanRemindRing(this, mCheckBoxSound.isChecked());
        AppUtil.saveCanRemindShake(this, mCheckBoxShake.isChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SettingRemindActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
        JPushInterface.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SettingRemindActivity"); // 保证 onPageEnd
        // 在onPause
        // 之前调用,因为 onPause
        // 中会保存信息
        MobclickAgent.onPause(this);
        savePushStatus();
        JPushInterface.onPause(this);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        YKActivityManager.getInstance().removeActivity(this);
    }
}
