package com.allactivity.imkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.allactivity.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by 张继 on 2016/10/28.
 *
 */

public class IMKit extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_kit);
        String token="AHIuSPB0Upbiwyr3vQH4LeAqJDmyD2FQZAAbKy2F8MdKyt5yb/+8wRhOJ8gua3a9hZwK5e7SNI4=";
        connect(token);
    }


    /**
     * 建立与融云服务器的连接
     * @param token
     */
    private void connect(String token){

//        if (getApplicationInfo().packageName.equals(  ((MyApplication) getApplication()).getCurProcessName(getApplicationContext()))){
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token 
                 */
                @Override
                public void onTokenIncorrect() {

                }

                /**
                 * 连接融云成功 
                 * @param s userId当前的token
                 */
                @Override
                public void onSuccess(String s) {
                    Log.e("www","-------onSuccess--------"+s);
                    startActivity(new Intent(IMKit.this,HuiHuaListActivity.class));
                    finish();
                }

                /**
                 * 连接融云失败 
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("www","-------onSuccess--------"+errorCode);
                }
            });
        }
//    }
}
