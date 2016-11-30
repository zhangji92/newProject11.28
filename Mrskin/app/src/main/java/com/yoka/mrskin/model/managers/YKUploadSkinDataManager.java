package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.util.Log;

import com.aijifu.skintest.api.SkinData;
import com.aijifu.skintest.api.SkinPart;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.YKDeviceInfo;

/**
 * 上传皮肤测试数据
 * 
 * @author z l l
 * 
 */
public class YKUploadSkinDataManager extends YKManager
{
    private static final String TAG = YKUploadSkinDataManager.class
            .getSimpleName();
    private static final String ITEM_PATH = "skin/item";
    private static final String WHOLE_PATH = "skin/main";

    private static YKUploadSkinDataManager singleton = null;
    private static Object lock = new Object();
    private int count = 0;

    private String type[] = { "rightface", "leftface", "head", "jaw", "partT" };
    private String[] skinName = { "original_image", "color_image",
            "moisture_image", "uniformity_image", "hole_image",
            "microgroove_image", "stain_image", };
    private OnUploadCompleteListener mCompleteListener;

    public static YKUploadSkinDataManager getInstnace() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKUploadSkinDataManager();
            }
        }
        return singleton;
    }

    private YKUploadSkinDataManager()
    {
        super();
        Log.d(TAG, "constructor");
    }

    public void setOnCompletedListener(OnUploadCompleteListener listener) {
        mCompleteListener = listener;
    }

    public void uploadLocalSkinData(ArrayList<SkinPart> skinParts, long time,
            ArrayList<ArrayList<String>> filePath,
            final YKGeneralCallBack callback) {
        for (int i = 0; i < skinParts.size(); i++) {
            uploadData(skinParts.get(i), type[i], time, filePath.get(i),
                    callback);
        }
    }

    private YKHttpTask uploadData(SkinPart skinPart, String type, long time,
            ArrayList<String> filePath, final YKGeneralCallBack callback) {

        return uploadSkinData(skinPart, type, time, filePath, callback);
    }

    /**
     * 上传局部皮肤信息
     * 
     * @param skinPart
     * @param type
     * @param callback
     * @return
     */
    private YKHttpTask uploadSkinData(SkinPart skinPart, String type,
            long time, ArrayList<String> filePath,
            final YKGeneralCallBack callback) {
        String url = getBase() + ITEM_PATH;
        System.out.println("YKUploadSkinDataManager url = " + url);
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("type", type);
        parameters.put("batch_id", YKDeviceInfo.getClientID() + time + "");
        parameters.put("color_score",
                Float.valueOf(skinPart.getColor().getScore()));
        parameters.put("moisture_score",
                Float.valueOf(skinPart.getMoisture().getScore()));
        parameters.put("uniformity_score",
                Float.valueOf(skinPart.getUniformity().getScore()));
        parameters.put("hole_score",
                Float.valueOf(skinPart.getHoles().getScore()));
        parameters.put("microgroove_score",
                Float.valueOf(skinPart.getMicrogroove().getScore()));
        parameters.put("stain_score",
                Float.valueOf(skinPart.getStain().getScore()));

        HashMap<String, String> files = new HashMap<String, String>();
        if (filePath != null) {
            for (int i = 0; i < filePath.size(); i++) {
                files.put(skinName[i], filePath.get(i));
            }
        }
        return super.postFiles(url, parameters, files, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                String webUrl = null;
                if (result.isSucceeded()) {
                    // parse data;
                    webUrl = object.optString("url");
                    count++;
                    if (count == 6) {
                        mCompleteListener.onCompleted();
                        count = 0;
                    }
                    // System.out.println("YKUploadSkinDataManager count = "
                    // + count + object.toString());
                }

                // do callback
                if (callback != null) {
                    callback.callback(result, webUrl);
                }
            }
        });
    }

    /**
     * 上传整体皮肤信息
     * 
     * @param data
     * @param callback
     * @return
     */
    public YKHttpTask uploadWholeSkinData(SkinData data, String filePath,
            long time, final YKGeneralCallBack callback) {
        String url = getBase() + WHOLE_PATH;
        System.out.println("skinresultactivity skindata  age2 = "
                + data.getSkinAge());
        System.out.println("YKUploadSkinDataManager url = " + url);
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("gender", Integer.valueOf(data.getGender()));
        parameters.put("age", Integer.valueOf(data.getSkinAge()));
        parameters.put("batch_id", YKDeviceInfo.getClientID() + time + "");
        if (YKCurrentUserManager.getInstance().getUser() != null) {
            parameters.put("user_id", YKCurrentUserManager.getInstance()
                    .getUser().getId());
        } else {
            parameters.put("user_id", "");
        }

        HashMap<String, String> files = new HashMap<String, String>();
        if (filePath != null) {
            files.put("original_image", filePath);
        }
        return super.postFiles(url, parameters, files, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                String webUrl = null;
                if (result.isSucceeded()) {
                    // parse data;
                    webUrl = object.optString("url");
                    count++;
                    if (count == 6) {
                        mCompleteListener.onCompleted();
                        count = 0;
                    }
                    // System.out.println("YKUploadSkinDataManager count = "
                    // + count + object.toString());
                }
                // do callback
                if (callback != null) {
                    callback.callback(result, webUrl);
                }
            }
        });
    }

    public int getCount() {
        return count;
    }

    public interface YKGeneralCallBack
    {
        public void callback(YKResult result, String webUrl);
    }

    public interface OnUploadCompleteListener
    {
        public void onCompleted();
    }
}
