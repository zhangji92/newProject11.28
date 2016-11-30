package com.yoka.mrskin.model.managers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKFocusImage;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.YKFile;

public class YKNewFocusImageManagers extends YKManager
{
    private static final String PATH = getBase() + "index/banner";
    private static YKNewFocusImageManagers singleton = null;
    public static String CACHE_NAME = "FocusImage";
    private static Object lock = new Object();

    public static YKNewFocusImageManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKNewFocusImageManagers();
            }
        }
        return singleton;
    }

    public ArrayList<YKFocusImage> getFocusImageData() {
        return loadDataFromFile();
    }

    public YKHttpTask postYKFocusImage(final YKNewFocusImageCallback callback) {
        return super.postURL(PATH, null, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                printRequestResult("postYKFocusImage", object, result);

                ArrayList<YKFocusImage> topicData = null;
                if (result.isSucceeded()) {
                    JSONArray tmpArray = object.optJSONArray("focusimages");
                    if (tmpArray != null) {
                        topicData = new ArrayList<YKFocusImage>();
                        for (int i = 0; i < tmpArray.length(); ++i) {
                            try {
                                topicData.add(YKFocusImage.parse(tmpArray
                                        .getJSONObject(i)));
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

    public static boolean saveDataToFile(ArrayList<YKFocusImage> topicData) {
        JSONObject object;
        String str;
        byte[] data = null;
        try {
            JSONArray imageArray = new JSONArray();
            for (YKFocusImage image : topicData) {
                imageArray.put(image.toJson());
            }
            object = new JSONObject();
            object.put("focusImages", imageArray);
            str = object.toString();
            data = str.getBytes("utf-8");
        } catch (Exception e) {
            return false;
        }
        YKFile.save(CACHE_NAME, data);
        return true;
    }

    private ArrayList<YKFocusImage> loadDataFromFile() {
        byte[] data = YKFile.read(CACHE_NAME);
        String str;
        ArrayList<YKFocusImage> images = new ArrayList<YKFocusImage>();
        try {
            str = new String(data, "utf-8");
            JSONObject object = new JSONObject(str);
            JSONArray array = object.getJSONArray("focusImages");
            YKFocusImage image;
            for (int i = 0; i < array.length(); ++i) {
                image = YKFocusImage.parse(array.getJSONObject(i));
                images.add(image);
            }
            ;
        } catch (Exception e) {
        }
        return images;
    }
}
