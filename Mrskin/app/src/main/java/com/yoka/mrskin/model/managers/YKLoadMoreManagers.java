package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKLoadMoreManagers extends YKManager
{
    private static final String PATH = getBase()+"task/loadmore";
    private static YKLoadMoreManagers singleton = null;
    private static Object lock = new Object();

    public static YKLoadMoreManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKLoadMoreManagers();
            }
        }
        return singleton;
    }

    public YKHttpTask postYKLoadMore(final YKLoadMoreCallback callback,String task_id) {
        
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("task_id", task_id);
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                printRequestResult("postYKTask", object, result);

                ArrayList<YKTask> taska = null;
                if (result.isSucceeded()) {
                    JSONArray tmpArray = object.optJSONArray("tasks");
                    if (tmpArray != null) {
                        taska = new ArrayList<YKTask>();
                        for (int i = 0; i < tmpArray.length(); ++i) {
                            try {
                                taska.add(YKTask.parse(tmpArray.getJSONObject(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                // do callback
                if (callback != null) {
                    callback.callback(result, taska);
                }
            }
        });

    }
}
