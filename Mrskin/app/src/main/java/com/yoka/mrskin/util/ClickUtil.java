package com.yoka.mrskin.util;

/**
 * Created by treason on 15/9/18.
 */
public class ClickUtil {

    /**
     * 按钮连续点击
     */
    private static long lastClickTime;

    /**
     * 按钮连续点击
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 测试
     */
    public void test() {
        if (ClickUtil.isFastClick()) {
            return;
        }
        android.util.Log.i("ClickUtil-test", "点击一下");
    }

}
