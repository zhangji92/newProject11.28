package com.yoka.mrskin.model.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKNewStatistics;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.YKFile;

public class YKNewStatisticsManager extends YKManager {

    private static final String PATH = getBase() + "dot/statis";
    public static String CACHE_NAME = "newStatistics";
    private static YKNewStatisticsManager singleton = null;
    private static Object lock = new Object();

    public static YKNewStatisticsManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKNewStatisticsManager();
            }
        }
        return singleton;
    }

    public YKHttpTask postStatistics(ArrayList<YKNewStatistics> statistics,final NewStatisticsCallback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("data", statistics);
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

                if (result.isSucceeded()) {

                }

                if (callback != null) {
                    callback.callback(result);
                }
            }
        });
    }

    public interface NewStatisticsCallback {
        public void callback(YKResult result);
    }

    public ArrayList<YKNewStatistics> getStatistic() {
        return loadDataStatisticFile();
    }

    public boolean saveStatisticFile(ArrayList<YKNewStatistics> statistic) {
        return saveDataStatisticFile(statistic);
    } 

    private boolean saveDataStatisticFile(ArrayList<YKNewStatistics> statistics) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] data = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(statistics);
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

    private ArrayList<YKNewStatistics> loadDataStatisticFile() {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        byte[] data = YKFile.read(CACHE_NAME);
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            @SuppressWarnings("unchecked")
            ArrayList<YKNewStatistics> objectData = (ArrayList<YKNewStatistics>) ois
            .readObject();
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
