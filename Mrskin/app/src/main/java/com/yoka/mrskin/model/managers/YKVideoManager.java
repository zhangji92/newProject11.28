package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTopicData;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKVideoManager extends YKManager
{
    private static final String VIDEO_PATH = getBase() + "video/information";

    private static YKVideoManager singleton = null;
    private static Object lock = new Object();

    public static YKVideoManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKVideoManager();
            }
        }
        return singleton;
    }

    public YKHttpTask requestVideo(String pageIndex, int count,
            final Callback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("page_index", pageIndex);
        parameters.put("count", Integer.valueOf(count));

        return super.postURL(VIDEO_PATH, parameters,
                new com.yoka.mrskin.model.managers.base.Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                ArrayList<YKTopicData> topicData = new ArrayList<YKTopicData>();
                if (result.isSucceeded()) {
                    try {
                        JSONArray jsonArray = object
                                .optJSONArray("topics");
                        YKTopicData topic;
                        JSONObject tmpJsonObject;
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            tmpJsonObject = (JSONObject) jsonArray
                                    .get(i);
                            topic = YKTopicData.parse(tmpJsonObject);
                            topicData.add(topic);
                        }
                    } catch (Exception e) {
                    }
                }
                if (callback != null) {
                    callback.callback(topicData, result);
                }
            }
        });
    }

    public interface Callback
    {
        public void callback(ArrayList<YKTopicData> topic,
                YKResult result);
    }
}
