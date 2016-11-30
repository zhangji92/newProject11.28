package com.yoka.mrskin.model.managers;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKHomeAdContent;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKHomeAdsManager extends YKManager
{
    public static final String TAG = YKHomeAdsManager.class.getSimpleName();
    private static YKHomeAdsManager singleton = null;
    private static Object lock = new Object();

    public static YKHomeAdsManager getInstnace() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKHomeAdsManager();
            }
        }
        return singleton;
    }

    private YKHomeAdsManager()
    {
        super();
        Log.d(TAG, "constructor");
    }

    public YKHttpTask requestHomeAd(Context context,
            final HomeAdsCallback callback) {
        return super.getURL(getAdUrl(), YKManager.getAddHeaderMap("153", context),
                new Callback() {

                    @Override
                    public void doCallback(YKHttpTask task, JSONObject object,
                            YKResult result) {
                        YKHomeAdContent content = null;
                        if (result.isSucceeded()) {
                            JSONObject tmpObject = object.optJSONObject("Contents");
                            if (tmpObject != null) {
                                content = YKHomeAdContent.parse(tmpObject);
                            }
                        }

                        if (callback != null) {
                            callback.callback(result, content);
                        }
                    }
                });
    }

    public interface HomeAdsCallback
    {
        public void callback(YKResult result, YKHomeAdContent content);
    }
}
