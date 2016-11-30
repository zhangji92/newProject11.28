package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;

/**
 * 上传图片的Manager
 * 
 * @author wangchunhui
 * 
 */
public class YKUploadImageManager extends YKManager
{
    private static final String PATH = getBase() + "my/upload";
    private static YKUploadImageManager instance = null;
    private static Object lock = new Object();
    private uploadImageCompleteListener mCompleteListener;
    private int count = 0;
    private int total;
    private ArrayList<String> mImagePaths = new ArrayList<String>();

    public static YKUploadImageManager getInstance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new YKUploadImageManager();
            }
        }
        return instance;
    }

    public void setCompleteListener(uploadImageCompleteListener listener) {
        mCompleteListener = listener;
    }

    public ArrayList<String> getImagePaths() {
        if (mImagePaths != null) {
            return mImagePaths;
        }
        return null;
    }
    
    public void clearImage() {
        if (mImagePaths != null) {
            mImagePaths.clear();
        }
    }

    public void uoloadImages(ArrayList<String> paths,
            final UploadImageCallback callback) {
        total = paths.size();
        for (int i = 0; i < paths.size(); i++) {
            uploadImages(paths.get(i), callback);
        }
    }

    public YKHttpTask uploadImages(String filePath,
            final UploadImageCallback callback) {
        HashMap<String, String> fileParams = new HashMap<String, String>();
        if (filePath != null) {
            fileParams.put("image", filePath);
        }
        return super.postFiles(PATH, null, fileParams,
                new com.yoka.mrskin.model.managers.base.Callback() {

                    @Override
                    public void doCallback(YKHttpTask task, JSONObject object,
                            YKResult result) {
                        String imageUrl = "";
                        if (result.isSucceeded() && object != null) {
                            count++;
                            imageUrl = object.optString("url");
                            mImagePaths.add(imageUrl);
                            if (count == total) {
                                mCompleteListener.onCompleted();
                                count = 0;
                            }
                        }
                        if (callback != null) {
                            callback.callback(result, imageUrl);
                        }
                    }
                });
    }

    public YKHttpTask uploadImage(String filePath, final Callback callback) {
        HashMap<String, String> fileParams = new HashMap<String, String>();
        if (filePath != null) {
            fileParams.put("image", filePath);
        }
        return super.postFiles(PATH, null, fileParams,
                new com.yoka.mrskin.model.managers.base.Callback() {

                    @Override
                    public void doCallback(YKHttpTask task, JSONObject object,
                            YKResult result) {
                        if (result.isSucceeded()) {

                        }
                        if (callback != null) {
                            callback.callback(result);
                        }
                    }
                });
    }

    public interface Callback
    {
        public void callback(YKResult result);
    }

    public interface UploadImageCallback
    {
        public void callback(YKResult result, String imageUrl);
    }

    public interface uploadImageCompleteListener
    {
        public void onCompleted();
    }
}
