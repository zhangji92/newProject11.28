package com.yoka.mrskin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yoka.mrskin.R;

public class ShareTestActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_test);

        findViewById(R.id.share_test).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // ShareWxManager.getInstance().init(ShareTestActivity.this,
                // UmSharedAppID.SHARE_WX_APP_ID);
                // ShareWxManager.getInstance().shareWxCircle1("adadsadsadsa",
                // "dsfdsfsdfsdf", "http://www.baidu.com");
            }
        });
    }
}
