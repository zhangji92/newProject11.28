package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKLocationCity;
import com.yoka.mrskin.model.data.YKVersionInfo;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

/**
 * 获取城市列表
 * 
 */
public class YKLocationCityManager extends YKManager
{

    private static final String PATH = getBase() + "baidu/geocoder";

    private static YKLocationCityManager singleton = null;
    private static Object lock = new Object();
    private YKVersionInfo mInfo;

    public YKVersionInfo getmInfo() {
        return mInfo;
    }

    public static YKLocationCityManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKLocationCityManager();
            }
        }
        return singleton;
    }

    /**
     * 获取服务器城市
     * 
     * @param callback
     * @return
     */
    public YKHttpTask postLocationCity(final YKLocationCityCallback callback,
            String longitude,String latitude) {
        // 后期增加的字段
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("longitude", longitude);
        parameters.put("latitude", latitude);


        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {

                YKLocationCity location = null;
                if (result.isSucceeded()) {
                    JSONObject obj = null;
                    try {
                        obj = object.getJSONObject("city");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (obj != null) {
                        location = YKLocationCity.parse(obj);
                    }

                }
                // do callback
                if (callback != null) {
                    callback.callback(result, location);
                }
            }
        });
    }  

}
