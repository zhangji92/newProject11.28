package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKShareRewardManager extends YKManager {
    private static final String PATH = getBase() + "share/reward";

    private static YKShareRewardManager singleton = null;
    private static Object lock = new Object(); 

    public static YKShareRewardManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKShareRewardManager();
            }
        }
        return singleton;
    }

    public YKHttpTask postShareReward(String authtoken, final ShareRewardCallback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("authtoken", authtoken);
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {
                
                
                if (result.isSucceeded()) {}

                if (callback != null) {
                    callback.callback(result);
                }
            }
        });
    }

    public interface ShareRewardCallback {
        public void callback(YKResult result);
    }
}
