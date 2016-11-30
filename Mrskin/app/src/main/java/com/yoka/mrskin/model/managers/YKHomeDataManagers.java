package com.yoka.mrskin.model.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKHomeData;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.YKFile;

public class YKHomeDataManagers extends YKManager
{
    private static final String PATH = getBase() + "index/home";
    public static String CACHE_NAME = "homeData";
    private static YKHomeDataManagers singleton = null;
    private static Object lock = new Object();

    public static YKHomeDataManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKHomeDataManagers();
            }
        }
        return singleton;
    }

    public YKHomeData getCacheData() {
        return loadDataFromFile();
    }

    public YKHttpTask postYKHomeData(final YKHomeDataCallback callback,
            String code) {
        // 后期增加的字段
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("city_code", code);

        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {

                printRequestResult("postYKTopicData", object, result);
                YKHomeData homeData = null;
                if (result.isSucceeded()) {
                    JSONObject objtopic = null;
                    try {
                        objtopic = object.getJSONObject("homeData");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (objtopic != null) {
                        homeData = YKHomeData.parse(objtopic);
                        saveDataToFile(homeData);
                    }

                }
                // do callback
                if (callback != null) {
                    callback.callback(result, homeData);
                }
            }
        });
    }

    private boolean saveDataToFile(YKHomeData objectData) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] data = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(objectData);
            data = baos.toByteArray();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
        }

        YKFile.save(CACHE_NAME, data);
        return true;
    }

    private YKHomeData loadDataFromFile() {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        byte[] data = YKFile.read(CACHE_NAME);
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            YKHomeData objectData = (YKHomeData) ois.readObject();
            return objectData;
        } catch (Exception e) {
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                bais.close();
            } catch (Exception e) {
            }
        }
        return null;
    }
}
