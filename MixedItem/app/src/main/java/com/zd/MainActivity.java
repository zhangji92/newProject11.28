package com.zd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.igexin.sdk.PushManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //应用程序启动初始化阶段，初始化SDK：
        PushManager.getInstance().initialize(this.getApplicationContext());
    }
}
