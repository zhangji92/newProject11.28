package com.jrd.loan.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import android.text.TextUtils;
import android.util.Log;

import com.jrd.loan.base.BaseApplication;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.umeng.analytics.MobclickAgent;

/**
 * log输出, 开发和测试阶段isDebug = true, 产品上线后改成false
 *
 * @author Luke
 */
public final class LogUtil {
    // 是否需要打印bug
    private final static boolean isDebug = true;

    private LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public final static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public final static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public final static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public final static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public final static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    public final static void reportError(String tag, Throwable exp) {
        MobclickAgent.reportError(BaseApplication.getContext(), obtainErrorInfo(tag, ExceptionPrinter.throwableToString(exp)));
    }

    public final static void reportError(String tag, String msg) {
        MobclickAgent.reportError(BaseApplication.getContext(), obtainErrorInfo(tag, msg));
    }

    private final static String obtainErrorInfo(String tag, String msg) {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("tag: ").append(tag).append("   \n");

        //渠道
        strBuffer.append("channel: ").append(AppInfoUtil.getChannel()).append("   \n");

        //app当前版本
        strBuffer.append("curr version: ").append(AppInfoPrefs.getAppInfo(AppInfoPrefs.APP_INFO_KEY_CURR_VERSION, "")).append("   \n");

        //app上一个版本
        strBuffer.append("last version: ").append(AppInfoPrefs.getAppInfo(AppInfoPrefs.APP_INFO_KEY_LAST_VERSION, "")).append("   \n");

        //异常信息
        strBuffer.append("exp info: ").append(msg).append("   \n");

        return strBuffer.toString();
    }

    private static class ExceptionPrinter {
        public static String throwableToString(Throwable throwable) {
            ByteArrayOutputStream b = null;
            PrintStream ps = null;
            String msg = null;

            try {
                b = new ByteArrayOutputStream();
                ps = new PrintStream(b, true);
                throwable.printStackTrace(ps);

                msg = ps.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (b != null) {
                        b.close();
                        b = null;
                    }

                    if (ps != null) {
                        ps.close();
                        ps = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return TextUtils.isEmpty(msg) ? "" : msg;
        }
    }
}