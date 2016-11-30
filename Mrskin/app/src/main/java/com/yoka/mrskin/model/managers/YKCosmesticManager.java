package com.yoka.mrskin.model.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKKeyword;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.YKFile;

public class YKCosmesticManager extends YKManager
{

    private static final String PATH_RECOMMENDATION = getBase()
            + "cosmetics/recommend";
    private static final String PATH_COMMENT_PRODUCT = getBase()
            + "cosmetics/recommend";
    private static YKCosmesticManager instance = null;
    private static Object lock = new Object();
    private int mUploadedImageCount;
    public static String CACHE_PRODUCT = "ProductData";
    public static String CACHE_LEYWORD = "KeyWordData";

    public static YKCosmesticManager getInstance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new YKCosmesticManager();
            }
        }
        return instance;
    }

    private YKCosmesticManager()
    {

    }
    public ArrayList<YKProduct> getProductData() {
        return loadDataProductFile();
    }
    
    public ArrayList<YKKeyword> getKeyWordData() {
        return loadDatakeywordFile();
    }

    public YKHttpTask requestRecommendations(final Callback callback) {
        return super.postURL(PATH_RECOMMENDATION, null,
                new com.yoka.mrskin.model.managers.base.Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {

                ArrayList<YKKeyword> keywords = new ArrayList<YKKeyword>();
                ArrayList<YKProduct> products = new ArrayList<YKProduct>();
                if (result.isSucceeded()) {
                    // parse keywords
                    JSONArray tmpArray = object
                            .optJSONArray("keywords");
                    if (tmpArray != null) {
                        for (int i = 0; i < tmpArray.length(); ++i) {
                            try {
                                keywords.add(YKKeyword.parse(tmpArray
                                        .getJSONObject(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // parse keywords
                    tmpArray = object.optJSONArray("products");
                    if (tmpArray != null) {
                        for (int i = 0; i < tmpArray.length(); ++i) {
                            try {
                                products.add(YKProduct.parse(tmpArray
                                        .getJSONObject(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                saveDataProductFile(products);
                saveDataKeywordFile(keywords);
                // do callback
                if (callback != null) {
                    callback.callback(result, keywords, products);
                }
            }
        });
    }

    public YKHttpTask commentProduct(String productID, String title,
            String description, int rating, final ArrayList<String> images,
            final CommentCallback callback) {
        mUploadedImageCount = 0;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        if (productID != null) {
            parameters.put("product_id", productID);
        }
        if (title != null) {
            parameters.put("title", title);
        }
        if (description != null) {
            parameters.put("description", description);
        }
        if (images != null) {
            parameters.put("images", images);
        }
        parameters.put("rating", rating + "");
        if (images != null && images.size() > 0) {
            final YKHttpTask task = new YKHttpTask();
            for (String imagePath : images) {
                YKHttpTask imageTask = YKUploadImageManager.getInstance().uploadImage(imagePath,
                        new YKUploadImageManager.Callback() {
                    @Override
                    public void callback(YKResult result) {
                        ++mUploadedImageCount;
                        if (mUploadedImageCount >= images.size()) {
                            YKHttpTask commentTask = sendCommentRequest(callback);
                            task.addSubTask(commentTask);
                        }
                    }
                });
                task.addSubTask(imageTask);
            }

            return task;
        } else {

            return sendCommentRequest(callback);
        }
    }

    private YKHttpTask  sendCommentRequest(final CommentCallback callback) {
        return super.postURL(PATH_COMMENT_PRODUCT, null,
                new com.yoka.mrskin.model.managers.base.Callback() {

            @Override
            public void doCallback(YKHttpTask task,
                    JSONObject object, YKResult result) {

                // do callback
                if (callback != null) {
                    callback.callback(task, result);
                }
            }
        });
    }

    public interface Callback
    {
        public void callback(YKResult result, ArrayList<YKKeyword> keywords,
                ArrayList<YKProduct> products);
    }

    public interface CommentCallback
    {
        public void callback(YKHttpTask task, YKResult result);
    }
    //saveYKProduct
    private boolean saveDataProductFile(ArrayList<YKProduct> topicData) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] data = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(topicData);
            data = baos.toByteArray();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
        }

        YKFile.save(CACHE_PRODUCT, data);
        return true;
    }
    //loadYKProduct
    private ArrayList<YKProduct> loadDataProductFile() {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        byte[] data = YKFile.read(CACHE_PRODUCT);
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            @SuppressWarnings("unchecked")
            ArrayList<YKProduct> objectData = (ArrayList<YKProduct>) ois
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

    //saveYKKeyword
    private boolean saveDataKeywordFile(ArrayList<YKKeyword> topicData) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] data = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(topicData);
            data = baos.toByteArray();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
        }

        YKFile.save(CACHE_LEYWORD, data);
        return true;
    }
    //loadYKkeyword
    private ArrayList<YKKeyword> loadDatakeywordFile() {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        byte[] data = YKFile.read(CACHE_LEYWORD);
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            @SuppressWarnings("unchecked")
            ArrayList<YKKeyword> objectData = (ArrayList<YKKeyword>) ois
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
