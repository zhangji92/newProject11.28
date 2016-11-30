package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTopicData;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKBeautyInformationManager extends YKManager {
    private static final String PATH = getBase() + "hot/information";

    private static YKBeautyInformationManager singleton = null;
    private static Object lock = new Object();

    public static YKBeautyInformationManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKBeautyInformationManager();
            }
        }
        return singleton;
    }

    public YKHttpTask postYKTopicData(String tpye, String ID, final Callback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(tpye, ID);
        return super.postURL(PATH, parameters, new com.yoka.mrskin.model.managers.base.Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

                ArrayList<YKTopicData> topicData = null;
                if (result.isSucceeded()) {
                    JSONArray tmpArray = object.optJSONArray("topics");
                    if (tmpArray != null) {
                        topicData = new ArrayList<YKTopicData>();
                        for (int i = 0; i < tmpArray.length(); ++i) {
                            try {
                                topicData.add(YKTopicData.parse(tmpArray.getJSONObject(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                if (callback != null) {
                    callback.callback(result, topicData);
                }
            }
        });
    }

    public interface Callback {
        public void callback(YKResult result, ArrayList<YKTopicData> topicData);
    }
}
