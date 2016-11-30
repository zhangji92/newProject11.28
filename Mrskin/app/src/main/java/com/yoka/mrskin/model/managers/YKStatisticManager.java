package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.track.manager.TrackModel;

public class YKStatisticManager extends YKManager
{
    private static final String TAG = YKStatisticManager.class.getSimpleName();
    //private static final String PATH = "http://192.168.57.86:9054/fujun/client";
    //private static final String PATH = "http://192.168.57.91:8031/fujun/api/dot/statis";
    private static final String PATH = "http://hzp.yoka.com/fujun/api/dot/statis";
    

    private static YKStatisticManager singleton = null;
    private static Object lock = new Object();

    public static YKStatisticManager getInstnace() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKStatisticManager();
            }
        }
        return singleton;
    }

    private YKStatisticManager()
    {
        super();
        Log.d(TAG, "constructor");
    }

    public YKHttpTask requestRecommendation(ArrayList<TrackModel> tracks,
            final YKStatisticCallback callback) {
        // handle parameters
        if (tracks == null)
            return null;
        JSONArray trackArray = new JSONArray();
        for (TrackModel track : tracks) {
            JSONObject trackObject = null;
            trackObject = new JSONObject();
            try {
                trackObject.put("url", track.url);
            } catch (JSONException e) {
            }
            try {
                trackObject.put("time", track.time);
            } catch (JSONException e) {
            }
            trackArray.put(trackObject);
        }
        HashMap<String, JSONArray> params = new HashMap<String, JSONArray>();
        params.put("tracks", trackArray);
        //params.put("data", trackArray);
        String url = PATH;
        // do request
        return super.postURL4JsonArray(url, params, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                if (result.isSucceeded()) {

                }
                if (callback != null) {
                    callback.callback(result);
                }
            }
        });
    }
}
