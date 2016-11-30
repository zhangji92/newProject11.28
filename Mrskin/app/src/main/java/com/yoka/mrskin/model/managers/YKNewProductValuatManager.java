package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKWebentry;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKNewProductValuatManager extends YKManager
{
    private static final String VIDEO_PATH = getBase() + "xinpin/information";

    private static YKNewProductValuatManager singleton = null;
    private static Object lock = new Object();

    public static YKNewProductValuatManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKNewProductValuatManager();
            }
        }
        return singleton;
    }

    public YKHttpTask requestNewProductValuat(String pageIndex, int count,
            final Callback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("page_index", pageIndex);
        parameters.put("count", Integer.valueOf(count));

        return super.postURL(VIDEO_PATH, parameters,
                new com.yoka.mrskin.model.managers.base.Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                ArrayList<YKWebentry> tListwebentry = new ArrayList<YKWebentry>();
                if (result.isSucceeded()) {
                    try {
                        JSONArray jsonArray = object
                                .optJSONArray("web_entry_list");
                        YKWebentry webentry;
                        JSONObject tmpJsonObject;
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            tmpJsonObject = (JSONObject) jsonArray
                                    .get(i);
                            webentry = YKWebentry.parse(tmpJsonObject);
                            tListwebentry.add(webentry);
                        }
                    } catch (Exception e) {
                    }
                }
                if (callback != null) {
                    callback.callback(tListwebentry, result);
                }
            }
        });
    }

    public interface Callback
    {
        public void callback(ArrayList<YKWebentry> topic,
                YKResult result);
    }
}
