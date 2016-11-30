package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKAdRecordManager extends YKManager {

    public static final String ListAdShow="26";
    public static final String ListAdClick="27";
    public static final String ListInterFaceId="121";

    public static final String BootAdShow="28";
    public static final String BootAdClick="29";
    public static final String BootInterFaceId="123";

    private static YKAdRecordManager singleton = null;
    private static Object lock = new Object();

    public static YKAdRecordManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKAdRecordManager();
            }
        }
        return singleton;
    }

    public YKHttpTask requestAdUrl(String url) {

        return super.getURL(url, null, new com.yoka.mrskin.model.managers.base.Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

            }
        }

                );
    }

    public YKHttpTask requestAdRecord(Context context,String interfaceId, String articleId, String url, String pointId) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("articleId", articleId);
        parameters.put("url", url);
        parameters.put("pointId", pointId);

        return super.getURL(getAdUrl(), YKManager.getAddHeaderMap(interfaceId, context), parameters,
                new com.yoka.mrskin.model.managers.base.Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

            }
        }

                );
    }
    public YKHttpTask requestAdBootRecord(Context context,String interfaceId, String advid, String url, String pointId) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("advid", advid);
        parameters.put("url", url);
        parameters.put("pointId", pointId);

        return super.getURL(getAdUrl(), YKManager.getAddHeaderMap(interfaceId, context), parameters,
                new com.yoka.mrskin.model.managers.base.Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

            }
        }

                );
    }
}
