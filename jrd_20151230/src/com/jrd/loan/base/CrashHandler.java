package com.jrd.loan.base;

import android.os.Process;

import com.jrd.loan.util.LogUtil;

public final class CrashHandler implements Thread.UncaughtExceptionHandler {
    private final static String TAG = "CrashHandler";
    private static CrashHandler INSTANCE = new CrashHandler();

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtil.reportError(TAG, ex);

        //show关闭应用
        closeApp();
    }

    private void closeApp() {
        Process.killProcess(Process.myPid());
    }
}