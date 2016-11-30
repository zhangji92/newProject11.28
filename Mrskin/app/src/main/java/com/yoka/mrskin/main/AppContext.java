package com.yoka.mrskin.main;

import android.content.Context;
import android.os.Handler;

public class AppContext
{
    private static Context mContext;
    private static Handler mHandler;

    public static Context getInstance() {
        return mContext;
    }

    public static void init(Context context) {
        mContext = context;
        mHandler = new Handler();
    }

    public static void postRunnable(Runnable run) {
        mHandler.post(run);
    }

    public static void postRunnableDelay(Runnable run, long delayMillis) {
        mHandler.postDelayed(run, delayMillis);
    }
}
