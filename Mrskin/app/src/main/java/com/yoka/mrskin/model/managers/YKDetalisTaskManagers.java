package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 *获取单个任务
 */
public class YKDetalisTaskManagers extends YKManager
{
    private static final String PATH = getBase()+"task/id";
    private static YKDetalisTaskManagers singleton = null;
    private static Object lock = new Object();

    public static YKDetalisTaskManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKDetalisTaskManagers();
            }
        }
        return singleton;
    }

    public YKHttpTask postYKDetalisTask(final YKDetalisTaskCallback callback,String tid) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("tid", tid);
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result)
            {
                YKTask taska = null;
                if(result.isSucceeded()){
                    JSONObject objtopic = null;
                    try {
                        objtopic = object.getJSONObject("task");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (objtopic != null) {
                        taska = YKTask.parse(objtopic);
                    }

                }
                if (callback != null) {
                    callback.callback(result, taska);
                }
            }
        });
    }
}
