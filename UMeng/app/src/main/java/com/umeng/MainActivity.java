package com.umeng;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        UMShareAPI mShareAPI = UMShareAPI.get( MainActivity.this );
//        mShareAPI.doOauthVerify(MainActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);


        //点击按钮
        Button bnt_weChat= (Button) findViewById(R.id.bnt_weChat);
        bnt_weChat.setOnClickListener(this);
    }

//    private UMAuthListener umAuthListener = new UMAuthListener() {
//        @Override
//        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
//
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
//        }
//        @Override
//        public void onCancel(SHARE_MEDIA platform, int action) {
//            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
//        }
//    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bnt_weChat:
                new ShareAction(MainActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withText("hello")
                        .setCallback(umShareListener)
                        .share();
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
}
