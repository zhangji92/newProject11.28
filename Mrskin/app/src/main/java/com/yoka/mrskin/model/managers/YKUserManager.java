package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKUserManager extends YKManager
{
    private static final String FAVORITE_PATH = getBase() + "grass/list";
    private static final String MYCOSMESTIC_PATH = getBase() + "used/list";
    private static final String REMOVE_FAVORITE_PATH = getBase() + "grass/list";
    private static final String REMOVE_MYCOSMESTIC_PATH = getBase() + "used/list";
    private static final String UPDATE_USER_INFO_PATH = getBase() + "try/message";
    
    private static YKUserManager singleton = null;
    private static Object lock = new Object();

    public static YKUserManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKUserManager();
            }
        }
        return singleton;
    }

    public YKHttpTask requestFavoriteProducts(int pageIndex, int count,
            final Callback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("page_index", Integer.valueOf(pageIndex));
        parameters.put("count", Integer.valueOf(count));
        if (YKCurrentUserManager.getInstance().isLogin()) {
            parameters.put("user_id", YKCurrentUserManager.getInstance()
                    .getUser().getId());
        }
        return super.postURL(FAVORITE_PATH, parameters,
                new com.yoka.mrskin.model.managers.base.Callback() {

                    @Override
                    public void doCallback(YKHttpTask task, JSONObject object,
                            YKResult result) {
                        ArrayList<YKProduct> products = new ArrayList<YKProduct>();
                        if (result.isSucceeded()) {
                            try {
                                JSONArray jsonArray = object
                                        .optJSONArray("products");
                                YKProduct product;
                                JSONObject tmpJsonObject;
                                for (int i = 0; i < jsonArray.length(); ++i) {
                                    tmpJsonObject = (JSONObject) jsonArray
                                            .get(i);
                                    product = YKProduct.parse(tmpJsonObject);
                                    products.add(product);
                                }
                            } catch (Exception e) {
                            }
                        }
                        if (callback != null) {
                            callback.callback(task, products, result);
                        }
                    }
                });
    }

    public YKHttpTask requestMyCosmesticProducts(int pageIndex, int count,
            final Callback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("page_index", Integer.valueOf(pageIndex));
        parameters.put("count", Integer.valueOf(count));
        if (YKCurrentUserManager.getInstance().isLogin()) {
            parameters.put("user_id", YKCurrentUserManager.getInstance()
                    .getUser().getId());
        }
        return super.postURL(MYCOSMESTIC_PATH, parameters,
                new com.yoka.mrskin.model.managers.base.Callback() {

                    @Override
                    public void doCallback(YKHttpTask task, JSONObject object,
                            YKResult result) {
                        ArrayList<YKProduct> products = new ArrayList<YKProduct>();
                        if (result.isSucceeded()) {
                            try {
                                JSONArray jsonArray = object
                                        .optJSONArray("products");
                                YKProduct product;
                                JSONObject tmpJsonObject;
                                for (int i = 0; i < jsonArray.length(); ++i) {
                                    tmpJsonObject = (JSONObject) jsonArray
                                            .get(i);
                                    product = YKProduct.parse(tmpJsonObject);
                                    products.add(product);
                                }
                            } catch (Exception e) {
                            }
                        }
                        if (callback != null) {
                            callback.callback(task, products, result);
                        }
                    }
                });
    }
    
    public YKHttpTask removeMyCosmesticProduct(String productID,
            final RemoveCallback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("product_id", productID);
        if (YKCurrentUserManager.getInstance().isLogin()) {
            parameters.put("user_id", YKCurrentUserManager.getInstance()
                    .getUser().getId());
        }
        return super.postURL(REMOVE_MYCOSMESTIC_PATH, parameters,
                new com.yoka.mrskin.model.managers.base.Callback() {

                    @Override
                    public void doCallback(YKHttpTask task, JSONObject object,
                            YKResult result) {
                        
                        if (callback != null) {
                            callback.callback(task, result);
                        }
                    }
                });
    }
    
    public YKHttpTask removeFavoriteProduct(String productID,
            final RemoveCallback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("product_id", productID);
        if (YKCurrentUserManager.getInstance().isLogin()) {
            parameters.put("user_id", YKCurrentUserManager.getInstance()
                    .getUser().getId());
        }
        return super.postURL(REMOVE_FAVORITE_PATH, parameters,
                new com.yoka.mrskin.model.managers.base.Callback() {

                    @Override
                    public void doCallback(YKHttpTask task, JSONObject object,
                            YKResult result) {
                        
                        if (callback != null) {
                            callback.callback(task, result);
                        }
                    }
                });
    }
    
    public YKHttpTask updateUserInfo(String productID,
            final UpdateUserInfoCallback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        if (YKCurrentUserManager.getInstance().isLogin()) {
            parameters.put("user_id", YKCurrentUserManager.getInstance()
                    .getUser().getId());
        }
        return super.postURL(UPDATE_USER_INFO_PATH, parameters,
                new com.yoka.mrskin.model.managers.base.Callback() {

                    @Override
                    public void doCallback(YKHttpTask task, JSONObject object,
                            YKResult result) {
                        int trialEventCount = 0;
                        if (result.isSucceeded()) {
                            trialEventCount = object.optInt("trial_event_count");
                        }
                        if (callback != null) {
                            callback.callback(task,trialEventCount, result);
                        }
                    }
                });
    }
    
    public interface RemoveCallback
    {
        public void callback(YKHttpTask task, YKResult result);
    }
    
    public interface UpdateUserInfoCallback
    {
        public void callback(YKHttpTask task, int trialEventCount, YKResult result);
    }
    
    public interface Callback
    {
        public void callback(YKHttpTask task, ArrayList<YKProduct> products,
                YKResult result);
    }
}
