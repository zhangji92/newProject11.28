package com.yoka.mrskin.model.managers;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKVersionInfo;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

/**
 * 获取最新版本
 * 
 */
public class YKGetNewVersionManager extends YKManager
{

    private static final String PATH = getBase() + "version/upgrade";

    private static YKGetNewVersionManager singleton = null;
    private static Object lock = new Object();
    private YKVersionInfo mInfo;

    public YKVersionInfo getmInfo() {
        return mInfo;
    }

    public static YKGetNewVersionManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKGetNewVersionManager();
            }
        }
        return singleton;
    }

    /**
     * 获取服务器最新版本
     * 
     * @param callback
     * @return
     */
    public YKHttpTask postNewVersion(final YKGetNewVersionCallback callback) {

        // do request
        return super.postURL(PATH, null, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                printRequestResult("YKGetNewVersionManager", object, result);

                YKVersionInfo version = null;
                if (result.isSucceeded()) {
                    // parse data;
                    JSONObject obj = null;
                    try {
                        obj = object.getJSONObject("version");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (obj != null) {
                        version = YKVersionInfo.parse(obj);
                    }
                }
                // do callback
                if (callback != null) {
                    callback.callback(result, version);
                }
            }
        });
    }
}