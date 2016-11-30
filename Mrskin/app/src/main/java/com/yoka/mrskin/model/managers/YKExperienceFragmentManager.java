package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKRecommendation;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKExperienceFragmentManager extends YKManager {
    private static final String PATH = getBase()+"";

    private static YKExperienceFragmentManager singleton = null;
    private static Object lock = new Object();

    public static YKExperienceFragmentManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKExperienceFragmentManager();
            }
        }
        return singleton;
    }

    public YKHttpTask postYKExperienceFragment(String page, final ExperCallback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("page",page);
        return super.postURL(PATH, parameters, new com.yoka.mrskin.model.managers.base.Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

                if (result.isSucceeded()) {
                    
                }

//                if (callback != null) {
//                    callback.callback(result, experience);
//                }
            }
        });
    }

    public interface ExperCallback {
        public void callback(YKResult result, YKRecommendation experience);
    }
}
