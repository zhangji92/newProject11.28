package com.yoka.mrskin.model.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKWeatherData;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.YKFile;

public class YKWeatherManagers extends YKManager
{
    private static final String PATH = getBase() + "weather/code";
    public static String CACHE_NAME = "weatherData";
    private static YKWeatherManagers singleton = null;
    private static Object lock = new Object();

    public static YKWeatherManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKWeatherManagers();
            }
        }
        return singleton;
    }

    public YKWeatherData getCacheData() {
        return loadDataFromFile();
    }

    public YKHttpTask postYKWeather(final YKWeatherCallback callback,
            String code) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("city_code", code);
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {

                printRequestResult("postWeather", object, result);

                YKWeatherData weather = null;
                if (result.isSucceeded()) {
                    // parse data;
                    JSONObject obj = null;
                    try {
                        obj = object.getJSONObject("weatherData");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (obj != null) {
                        weather = YKWeatherData.parse(obj);
                        saveDataToFile(weather);
                    }
                }
                // do callback
                if (callback != null) {
                    callback.callback(result, weather);
                }
            }
        });

    }

    private boolean saveDataToFile(YKWeatherData weather) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] data = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(weather);
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

    private YKWeatherData loadDataFromFile() {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        byte[] data = YKFile.read(CACHE_NAME);
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            YKWeatherData weather = (YKWeatherData) ois.readObject();
            return weather;
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
