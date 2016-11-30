package com.yoka.mrskin.model.managers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKExperienceTag;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.YKFile;

public class YKExperienceTagManager extends YKManager {
    private static final String PATH = getBase() + "comment/catalog";

    private static YKExperienceTagManager singleton = null;
    public static String CACHE_NAME = "ExperienceTag";
    private static Object lock = new Object();

    public static YKExperienceTagManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKExperienceTagManager();
            }
        }
        return singleton;
    }

    public ArrayList<YKExperienceTag> getExperienceData() {
        return loadDataFromFile();
    }

    public YKHttpTask postYKExperienceTagData(final Callback callback) {
        //HashMap<String, Object> parameters = new HashMap<String, Object>();
        //        parameters.put("page_index", page);
        //        parameters.put("catalogId", catalogId);
        return super.postURL(PATH, null, new com.yoka.mrskin.model.managers.base.Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

                ArrayList<YKExperienceTag> experienceTag = null;
                if (result.isSucceeded()) {
                    JSONArray tmpArray = object.optJSONArray("Commentcatalog");
                    if (tmpArray != null) {
                        experienceTag = new ArrayList<YKExperienceTag>();
                        for (int i = 0; i < tmpArray.length(); ++i) {
                            try {
                                experienceTag.add(YKExperienceTag.parse(tmpArray.getJSONObject(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if(experienceTag != null){
                    saveDataToFile(experienceTag);
                }
                if (callback != null) {
                    callback.callback(result, experienceTag);
                }
            }
        });
    }

    //保存数据
    private boolean saveDataToFile(ArrayList<YKExperienceTag> experienceTag) {
        JSONObject object;
        String str;
        byte[] data = null;
        try {
            JSONArray imageArray = new JSONArray();
            for (YKExperienceTag experTag : experienceTag) {
                imageArray.put(experTag.toJson());
            }
            object = new JSONObject();
            object.put("experienceTag", imageArray);
            str = object.toString();
            data = str.getBytes("utf-8");
        } catch (Exception e) {
            return false;
        }
        YKFile.save(CACHE_NAME, data);
        return true;
    }

    private ArrayList<YKExperienceTag> loadDataFromFile() {
        byte[] data = YKFile.read(CACHE_NAME);
        String str;
        ArrayList<YKExperienceTag> experienceTag = new ArrayList<YKExperienceTag>();
        try {
            str = new String(data, "utf-8");
            JSONObject object = new JSONObject(str);
            JSONArray array = object.getJSONArray("experienceTag");
            YKExperienceTag image;
            for (int i = 0; i < array.length(); ++i) {
                image = YKExperienceTag.parse(array.getJSONObject(i));
                experienceTag.add(image);
            }
            ;
        } catch (Exception e) {
        }
        return experienceTag;
    }



    public interface Callback {
        public void callback(YKResult result, ArrayList<YKExperienceTag> experienceTag);
    }
}
