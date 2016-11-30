package com.allactivity.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import io.rong.imkit.RongIM;

//import com.android.volley.RequestQueue;

/**
 * Created by 张继 on 2016/10/28.
 *
 */

public class MyApplication extends Application {

    public static RequestQueue queue;

    public static RequestQueue getQueue() {
        return queue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 将“12345678”替换成您申请的APPID，申请地址：http://open.voicecloud.cn
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=583d3209");


        queue= Volley.newRequestQueue(getApplicationContext());


        /** 
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。 
         * io.rong.push 为融云 push 进程名称，不可修改。 
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))){
            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);



            super.onCreate();

        }


    }

    /**
     * 获取当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess :
                activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}
