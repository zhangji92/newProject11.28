package com.yoka.mrskin.model.managers;

import org.json.JSONObject;

import android.content.Context;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKAdBootManager extends YKManager
{

    private static YKAdBootManager singleton = null;
    private static Object lock = new Object();

    public static YKAdBootManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKAdBootManager();
            }
        }
        return singleton;
    }

    public YKHttpTask requestAdBootData(final Context context,
            final Callback callback) {

        return super.getURL(getAdUrl(), YKManager.getAddHeaderMap("115", context),
                new com.yoka.mrskin.model.managers.base.Callback() {

                    @Override
                    public void doCallback(YKHttpTask task, JSONObject object,
                            YKResult result) {
                        if (callback != null)
                            callback.doCallback(task, object, result);
                    }
                }

        );
    }

}
