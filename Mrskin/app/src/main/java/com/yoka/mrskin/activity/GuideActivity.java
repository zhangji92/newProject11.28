package com.yoka.mrskin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.main.MainActivity;
import com.yoka.mrskin.util.AppUtil;

/**
 * 首次启动的引导页
 * 
 * @author z l l
 * 
 */
public class GuideActivity extends BaseActivity implements OnClickListener
{

    private View mJumpLayout, mScanLayout, mTeachLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);

        initView();
    }

    private void initView()
    {
        mJumpLayout = findViewById(R.id.guide_jump_text_layout);
        mTeachLayout = findViewById(R.id.guide_teach_layout);
        mScanLayout = findViewById(R.id.guide_scan_layout);
        mJumpLayout.setOnClickListener(this);
        mTeachLayout.setOnClickListener(this);
        mScanLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
        case R.id.guide_jump_text_layout:
            // startAnotherActivity(MainActivity.class);
            finish();
            break;
        case R.id.guide_teach_layout:
            if(AppUtil.isNetworkAvailable(GuideActivity.this)){
                startAnotherActivity(TestCourseActivity.class);
            }else{
                Toast.makeText(GuideActivity.this, R.string.intent_no, Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.guide_scan_layout:
            startAnotherActivity(SkinTestActivity.class);
            finish();
            break;
        }
    }

    @Override
    public void onBackPressed()
    {
        this.finish();
        startAnotherActivity(MainActivity.class);
        super.onBackPressed();
    }

    @SuppressWarnings("rawtypes")
    private void startAnotherActivity(Class cla)
    {
        Intent intent = new Intent(this, cla);
        startActivity(intent);
    }
}
