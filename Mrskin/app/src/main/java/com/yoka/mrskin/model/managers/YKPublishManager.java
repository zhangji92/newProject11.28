package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKUpDataExperience;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKPublishManager extends YKManager {
    private static final String PATH = getBase() + "add/comment";

    private static YKPublishManager singleton = null;
    private static Object lock = new Object();

    public static YKPublishManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKPublishManager();
            }
        }
        return singleton;
    }

    public YKHttpTask postYKPublishData(String user_id,String title, ArrayList<YKUpDataExperience> experience_list ,final Callback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("user_id", user_id);
        parameters.put("title", title);

        ArrayList<String> experienceList = new ArrayList<String>();
        JSONObject experienceItem = null;
        for (YKUpDataExperience experience : experience_list) {

            experienceItem = experience.toJsonObject();
            experienceList.add(experienceItem.toString());

        }

        parameters.put("experience_list", experienceList);

        return super.postURL(PATH, parameters, new com.yoka.mrskin.model.managers.base.Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {
                String url = null;
                if(result.isSucceeded()){
                    try {
                        String newUrl = object.getString("url");
                         url = newUrl;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (callback != null) {
                    callback.callback(result,url);
                }
            }
        });
    }




    public interface Callback {
        public void callback(YKResult result,String url);
    }
}
