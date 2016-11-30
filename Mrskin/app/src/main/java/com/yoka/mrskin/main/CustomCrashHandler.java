package com.yoka.mrskin.main;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.util.YKUtil;

public class CustomCrashHandler implements UncaughtExceptionHandler
{
    private Context mContext;
    private static CustomCrashHandler mInstance = new CustomCrashHandler();
    @SuppressWarnings("unused")
    private static UncaughtExceptionHandler mDefaultHandler = null;

    private CustomCrashHandler()
    {
    }

    /**
     * 单例模式，保证只有一个CustomCrashHandler实例存在
     * 
     * @return
     */
    public static CustomCrashHandler getInstance() {
        return mInstance;
    }

    /**
     * 异常发生时，系统回调的函数，我们在这里处理一些操作
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 将一些信息保存到SDcard中
        YKUtil.savaExceptionInfoToSD(mContext, ex);
        // 提示用户程序即将退出
     //   showToast(mContext, "程序异常退出！");  
        // mDefaultHandler.uncaughtException(thread, ex);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        YKActivityManager.getInstance().finishAllActivity();
        // 退出程序方法
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }

    private void showToast(final Context context, final String msg) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    /**
     * 为我们的应用程序设置自定义Crash处理
     */
    public void setCustomCrashHanler(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

}
