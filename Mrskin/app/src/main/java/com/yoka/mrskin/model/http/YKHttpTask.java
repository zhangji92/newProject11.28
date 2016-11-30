package com.yoka.mrskin.model.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.loopj.android.http.RequestHandle;

public class YKHttpTask
{
    private static final String TAG = "YKHttpTask";

    private final Map<String, String> mMap;
    private final CallBack mCallback;
    private String mURL;
    private String mMethod;
    private boolean mIsCancelled;
    private ArrayList<YKHttpTask> mSubTasks;
    
    private RequestHandle mHandler;

    public YKHttpTask() 
    {
        mMap = new HashMap<String, String>();
        mCallback = null;
        mIsCancelled = false;
        mSubTasks = new ArrayList<YKHttpTask>();
    }
    public YKHttpTask(String method, final Map<String, String> map,
            final CallBack callBack, final String url)
    {
        Log.d(TAG, "constructor");
        mMap = map;
        mCallback = callBack;
        mURL = url;
        mMethod = method;
        mIsCancelled = false;
        mSubTasks = new ArrayList<YKHttpTask>();
    }

    public Map<String, String> getMap() {
        return mMap;
    }

    public CallBack getCallBack() {
        return mCallback;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        this.mMethod = method;
    }

    public String getURL() {
        return mURL;
    }
    
    public void cancel() {
        mIsCancelled = true;
        mHandler.cancel(true);
        for (YKHttpTask task : mSubTasks) {
            task.cancel();
        }
    }
    
    public boolean isCancelled() {
        return mIsCancelled;
    }
    
    public void addSubTask(YKHttpTask task) {
        if (task != null) {
            mSubTasks.add(task);
        }
    }
    
    public void setRequestHandler(RequestHandle handler) {
        mHandler = handler;
    }
}
