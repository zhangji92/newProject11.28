package com.yoka.mrskin.track.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.text.TextUtils;

import com.yoka.mrskin.main.AppContext;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKStatisticCallback;
import com.yoka.mrskin.model.managers.YKStatisticManager;
import com.yoka.mrskin.track.YokaTrackProvider;
import com.yoka.mrskin.util.YKDeviceInfo;

public class TrackManager
{
    private static final boolean is_Debug = false;
    private static TrackManager mTrackManager;
    private Object lock = new Object();
    private static final int MAX_TRACK_NUM = 10;
    private Context mContext;
    private TrackTask mTask;
    private static Handler mHandler;

    public static void init(Context context) {
        if (mTrackManager == null) {
            mTrackManager = new TrackManager(context);
        }
    }

    public static void setHandler(Handler handler) {
        mHandler = handler;
    }

    public static void destroy() {
        if (mTrackManager == null) {
            return;
        }
        mTrackManager.onDestroy();
        mTrackManager = null;
    }

    private void onDestroy() {
        mTask.exit();
        mTask = null;
        mContext = null;
    }

    public static TrackManager getInstance() {
        return mTrackManager;
    }

    private TrackManager(Context context)
    {
        super();
        mContext = AppContext.getInstance();
        mTask = new TrackTask();
        mTask.start();
    }

    /**
     * 对外接口，添加一条纪录
     * 
     * @param context
     *            :上下文
     * @param url
     *            ：url或者加工过的url
     * @param time
     *            :当前时间
     */
    public void addTrack(String url) {
        if (mTask == null || TextUtils.isEmpty(url)) {
            if (is_Debug) {
                System.out.println("insert error mTask == null  url = " + url
                        + addDefaultParamsDefault());
            }
            return;
        }
        synchronized (lock) {
            System.out.println("insert error mTask == null  url = " + url
                    + addDefaultParamsDefault());
            String newUrl = url + addDefaultParamsDefault();
            System.out.println("trackmanager newurl = " + newUrl);
//            mTask.addData(new TrackModel(newUrl, System.currentTimeMillis()));
        }
    }
    
    
    public void addTrack1(String url, String action, String refer) {
        if (mTask == null || TextUtils.isEmpty(url)) {
            if (is_Debug) {
                System.out.println("insert error mTask == null  url = " + url
                        + addDefaultParamsDefault());
            }
            return;
        }
        synchronized (lock) {
            System.out.println("insert error mTask == null  url = " + url
                    + addDefaultParamsDefault());
            String newUrl = "http://www.fujun.com?";
            if (!TextUtils.isEmpty(action)) {
                newUrl = newUrl+ ("action="+action);
                newUrl +="&";
            }
            if (!TextUtils.isEmpty(url)) {
                newUrl += "url="+URLEncoder.encode(url);
                newUrl +="&";
            }
            if (!TextUtils.isEmpty(refer)) {
                newUrl += "refer="+refer;
            }
            newUrl += addDefaultParamsDefault();
            System.out.println("trackmanager newurl = " + newUrl);
            mTask.addData(new TrackModel(newUrl, System.currentTimeMillis()/1000));
        }
    }

    /**
     * 向数据库中插入数据
     * 
     * @param context
     *            :上下文
     * @param url
     *            ：url或者加工过的url
     */
    private void insertTrack(Context context, String url, long time) {
        if (is_Debug) {
            System.out.println("insert start url = " + url);
        }
        String encodeUrl = getEncodeUrl(url);
        ContentValues valus = new ContentValues();
        valus.put(YokaTrackProvider.TrackColumns.SOURCE_URL, encodeUrl);
        valus.put(YokaTrackProvider.TrackColumns.SOURCE_TIME, time);
        context.getContentResolver().insert(
                YokaTrackProvider.TrackColumns.CONTENT_URI, valus);
        if (is_Debug) {
            System.out.println("insert end url = " + url);
        }
    }

    /**
     * 查询数据库中的所有数据
     * 
     * @param context
     *            ：上下文
     * @return
     */
    public ArrayList<TrackModel> queryTrack(Context context) {
        if (is_Debug) {
            System.out.println("queryTrack start");
        }
        Cursor cursor = context.getContentResolver().query(
                YokaTrackProvider.TrackColumns.CONTENT_URI, null, null, null,
                null);
        if (cursor == null || !cursor.moveToFirst()) {
            if (is_Debug) {
                System.out.println("queryTrack cursor error " + cursor);
            }
            return null;
        }
        if (is_Debug) {
            System.out.println("queryTrack cursor count " + cursor.getCount());
        }
        int count = cursor.getCount();
        if (count == 0) {
            cursor.close();
            return null;
        }
        ArrayList<TrackModel> result = new ArrayList<TrackModel>(count);
        for (int i = 0; i < count; i++) {
            String url = cursor.getString(cursor
                    .getColumnIndex(YokaTrackProvider.TrackColumns.SOURCE_URL));
            long time = cursor
                    .getLong(cursor
                            .getColumnIndex(YokaTrackProvider.TrackColumns.SOURCE_TIME));
            // long id = cursor.getInt(cursor
            // .getColumnIndex(YokaTrackProvider.TrackColumns._ID));
            url = getDecodeUrl(url);
            result.add(new TrackModel(url, time));
            cursor.moveToNext();
        }
        cursor.close();
        if (is_Debug) {
            System.out.println("queryTrack   end");
        }
        return result;
    }

    /**
     * 获取数据库中数据条数
     * 
     * @param context
     * @return
     */
    private int getDBTrackCount(Context context) {
        Cursor cursor = context.getContentResolver().query(
                YokaTrackProvider.TrackColumns.CONTENT_URI, null, null, null,
                null);
        if (cursor == null || !cursor.moveToFirst()) {
            if (is_Debug) {
                System.out.println("getDBTrackCount cursor error " + cursor);
            }
            return -1;
        }
        if (is_Debug) {
            System.out.println("getDBTrackCount cursor count "
                    + cursor.getCount());
        }
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * 
     * @param context
     *            上下文，用于获取ContentResolver
     */
    private void clearAllTrack(Context context) {
        try {
            context.getContentResolver().delete(
                    YokaTrackProvider.TrackColumns.CONTENT_URI, null, null);
        } catch (Exception e) {
        }
    }

    /**
     * 转码
     * 
     * @param url
     * @return
     */
    private String getEncodeUrl(String url) {
        try {
            url = URLEncoder.encode(url, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 逆转码
     * 
     * @param url
     * @return
     */
    private String getDecodeUrl(String url) {
        try {
            return URLDecoder.decode(url, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    private void postTrackData(final Context context) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<TrackModel> result = queryTrack(context);
                YKStatisticManager.getInstnace().requestRecommendation(result,
                        new YKStatisticCallback() {

                            @Override
                            public void callback(YKResult result) {
                                if (result.isSucceeded()) {
                                    if (mTask != null) {
                                        mTask.netCallBackOk();
                                    }
                                } else {
                                    if (mTask != null) {
                                        mTask.netCallBackError();
                                    }
                                }
                            }
                        });
            }
        });
        // // =====测试代码====
        // // new Thread() {
        // // public void run() {
        // // try {
        // // Thread.sleep(2000);
        // // } catch (InterruptedException e) {
        // // }
        // // if (mTask != null) {
        // // mTask.netCallBackOk();
        // // }
        // // };
        // // }.start();

    }

    private class TrackTask extends Thread
    {
        private ArrayList<TrackModel> mData = new ArrayList<TrackModel>();
        private boolean isExit = false;
        private boolean isReady = true;
        private int mErrorCount;

        public void addData(TrackModel model) {
            mData.add(model);
            if (isReady) {
                notifyData();
            }
        }

        public void exit() {
            isExit = true;
            notifyData();
        }

        private void notifyData() {
            synchronized (mData) {
                mData.notify();
            }
        }

        /**
         * 如果上传成功，会回调此接口，让db继续work
         */
        public void netCallBackOk() {
            clearAllTrack(mContext);
            mErrorCount = 0;
            isReady = true;
            notifyData();
        }

        /**
         * 如果上传失败，会回调此接口，让db继续work
         */
        public void netCallBackError() {
            isReady = true;
            mErrorCount++;
            notifyData();
        }

        @Override
        public void run() {
            // 每次启动如果有数据先上传
            int dbcount0 = getDBTrackCount(mContext);
            if (dbcount0 > 0) {
                isReady = false;
                postTrackData(mContext);
            }

            while (true) {
                // 没有需要插入数据库的数据，并且退出状态为true,退出
                if (mData.isEmpty() && isExit) {
                    return;
                }
                // 没有需要插入的数据，或者此时因为正在上传，不允许插入数据，需要继续wait
                if (mData.isEmpty() || !isReady) {
                    synchronized (mData) {
                        try {
                            mData.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                }
                if (mData.isEmpty()) {
                    continue;
                }
                // 取第0个数据插入数据库，并从列表中将其删除之
                TrackModel model = null;
                synchronized (lock) {
                    model = mData.remove(0);
                }
                insertTrack(mContext, model.url, model.time);
                // 如果已通知退出，就不要再联网了，先插入到数据库，等下次启动再处理
                if (isExit || mErrorCount > 3) {
                    continue;
                }
                // 查询数据库存储个数，满足条件就上传
                int dbcount = getDBTrackCount(mContext);
                if (dbcount >= MAX_TRACK_NUM) {
                    isReady = false;
                    postTrackData(mContext);
                }
            }

        }
    }

    @SuppressWarnings("deprecation")
    private String addDefaultParamsDefault() {
        String AppVersion = null;
        String os = null;
        String os_version = null;
        String device_model = null;
        String clientid = null;
        String user = null;
        String screenwidth = null;
        String screenheight = null;
        String api_version = null;
        String uid = null;
        try {
            AppVersion = "&version="
                    + URLEncoder.encode(YKDeviceInfo.getAppVersion(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            os = "&device=" + URLEncoder.encode("Android", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            os_version = "&os="
                    + URLEncoder.encode(android.os.Build.VERSION.RELEASE,
                            "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            device_model = "&vendor="
                    + URLEncoder.encode(android.os.Build.MODEL, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            clientid = "&client_id="
                    + URLEncoder.encode(YKDeviceInfo.getClientID(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            user = "&user-agent="
                    + URLEncoder.encode(YKDeviceInfo.getUserAgent(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            screenwidth = "&screenwidth="
                    + URLEncoder.encode(YKDeviceInfo.getScreenWidth());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            screenheight = "&screenheight="
                    + URLEncoder.encode(YKDeviceInfo.getScreenHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            api_version = "&api_version=" + URLEncoder.encode("v5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (YKCurrentUserManager.getInstance().isLogin()) {
            uid = "&user_id="
                    + URLEncoder.encode(YKCurrentUserManager.getInstance()
                            .getUser().getId());
            return AppVersion + os + os_version + device_model + clientid
                    + user + screenwidth + screenheight + api_version + uid;
        }
        return AppVersion + os + os_version + device_model + clientid + user
                + screenwidth + screenheight + api_version;
    }
}
