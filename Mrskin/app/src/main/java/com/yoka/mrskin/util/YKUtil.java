package com.yoka.mrskin.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class YKUtil
{
    private static final String CACHE_ROOT = "fujun";
    private static final String saveImage = "yokaImage";

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    // px转dp
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mWm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mWm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String getSDCardRootPath() {
        if (!hasSDCard()) {
            return null;
        }
        return Environment.getExternalStorageDirectory().toString();
    }

    public static String getDownloadImagePath(Context context) {
        boolean hasSDCard = hasSDCard();
        if (hasSDCard) {
            return getSDCardRootPath() + "/" + saveImage;
        } else
            return context.getCacheDir().getAbsoluteFile() + "/" + saveImage;

    }

    public static String getCacheRootDirPath(Context context) {
        boolean hasSDCard = hasSDCard();
        String root = null;
        if (hasSDCard) {
            root = getSDCardRootPath();
        } else {
            root = context.getCacheDir().getAbsoluteFile().toString();
        }
        String path = root + File.separator + CACHE_ROOT + File.separator;
        return path;

    }

    public static String getDiskCachePath(Context context) {
        String path = getCacheRootDirPath(context) + "cacheDir";
        return path;
    }

    /**
     * 保存获取的 软件信息，设备信息和出错信息保存在SDcard中
     * 
     * @param context
     * @param ex
     * @return
     */
    public static String savaExceptionInfoToSD(Context context, Throwable ex) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();

        for (Map.Entry<String, String> entry : obtainSimpleInfo(context)
                .entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }

        sb.append(obtainExceptionInfo(ex));

        File dir = new File(YKUtil.getCacheRootDirPath(context) + "crash"
                + File.separator);
        if (dir.exists()) {
            deleteFile(dir);
        }
        dir.mkdir();

        try {
            fileName = dir.toString() + File.separator
                    + paserTime(System.currentTimeMillis()) + ".log";
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(sb.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileName;
    }

    /**
     * 获取系统未捕捉的错误信息
     * 
     * @param throwable
     * @return
     */
    public static String obtainExceptionInfo(Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();

        Log.e("YKUtil", mStringWriter.toString());
        return mStringWriter.toString();
    }

    /**
     * 将毫秒数转换成yyyy-MM-dd-HH-mm-ss的格式
     * 
     * @param milliseconds
     * @return
     */
    private static String paserTime(long milliseconds) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String times = format.format(new Date(milliseconds));
        return times;
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     * 
     * @param context
     * @return
     */
    private static HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);

        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);

        return map;
    }

    /**
     * 是否点击到View
     * 
     * @param v
     * @param event
     * @return
     */
    public static boolean isHitView(View v, MotionEvent event) {
        if (v == null || event == null) {
            return false;
        }
        int[] leftTop = { 0, 0 };
        v.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + v.getHeight();
        int right = left + v.getWidth();
        if (event.getX() > left && event.getX() < right && event.getY() > top
                && event.getY() < bottom) {
            return false;
        }
        return true;
    }

    public static void showKeyBoard(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.showSoftInput(view, 0);
//        inputManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                deleteFile(childFiles[i]);
            }
            file.delete();
        }
    }
    
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

	/** 
     * 将sp值转换为px值，保证文字大小不变 
     *  
     * @param spValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int sp2px(Context context,float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }  
    
    /**
     * 限制输入字节数
     * @param inputStr
     * @return
     */
    public static  String getLimitSubstring(String inputStr) {
	int orignLen = inputStr.length();
	int resultLen = 0;
	String temp = null;
	for (int i = 0; i < orignLen; i++) {
	    temp = inputStr.substring(i, i + 1);
	    try {// 3 bytes to indicate chinese word,1 byte to indicate english
		// word ,in utf-8 encode
		if (temp.getBytes("utf-8").length == 3) {
		    resultLen += 2;
		} else {
		    resultLen++;
		}
	    } catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	    }
	    /*限制510字节*/
	    if (resultLen > 510) {
		return inputStr.substring(0, i);
	    }
	}
	return inputStr;
    }
}
