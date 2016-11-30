package com.yoka.mrskin.model.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTrialProduct;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.YKFile;

/**
 * 获取试用列表&我的试用列表
 * 
 * @author z l l
 * 
 */
public class YKTrialProductManager extends YKManager
{
    private static final String TAG = YKTrialProductManager.class
            .getSimpleName();
    private static final String NORMAL_PATH = "try/refresh";
    private static final String PERSONAL_PATH = "try/me";
    private static final String PATH_RECEIVE_PRODUCT = getBase() + "try/confirm";
    public static String CACHE_NAME = "TiralProductData";

    private static YKTrialProductManager singleton = null;
    private static Object lock = new Object();

    public static YKTrialProductManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKTrialProductManager();
            }
        }
        return singleton;
    }

    private YKTrialProductManager()
    {
        super();
        Log.d(TAG, "constructor");
    }

    public ArrayList<YKTrialProduct> getTrialProducts() {
        return loadDataFromFile();
    }

    /**
     * 刷新
     * 
     * @param callback
     * @return
     */
    // public YKHttpTask refreshTrialProductsList(int pagerIndex,
    // YKTrialProductCallback callback) {
    // return requestTrialProductsList(false, null, pagerIndex, 20, callback);
    // }

    /**
     * 加载更多
     * 
     * @param pagerIndex
     * @param callback
     * @return
     */
    public YKHttpTask requestTrialProductsList(int pagerIndex, String userId,
            YKTrialProductCallback callback) {
        return requestTrialProductsList(false, userId, pagerIndex, 10, 0,
                callback);
    }

    /**
     * 刷新我的试用列表
     * 
     * @param userId
     * @param callback
     * @return
     */
    // public YKHttpTask refreshMyTrialProductsList(String userId,
    // YKTrialProductCallback callback) {
    // return requestTrialProductsList(true, userId, 0, 20, callback);
    // }

    /**
     * 加载更多我的试用列表
     * 
     * @param pagerIndex
     * @param userId
     * @param callback
     * @return
     */
    public YKHttpTask requestMyTrialProductsList(int pagerIndex, String userId,
            int type, YKTrialProductCallback callback) {
        return requestTrialProductsList(true, userId, pagerIndex, 10, type,
                callback);
    }

    /**
     * 获取试用列表
     * 
     * @param isMine
     * @param userId
     * @param pageIndex
     * @param count
     * @param callback
     * @return
     */
    private YKHttpTask requestTrialProductsList(boolean isMine, String userId,
            int pageIndex, int count, int type,
            final YKTrialProductCallback callback) {
        String url = "";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("page_index", Integer.valueOf(pageIndex));
        params.put("count", Integer.valueOf(count));
        if (isMine) {
            url = getBase() + PERSONAL_PATH;
        } else {
            url = getBase() + NORMAL_PATH;
        }
        if(userId != null) {
            params.put("user_id", userId);
            params.put("type", type);
        }
        return super.postURL(url, params, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                ArrayList<YKTrialProduct> products = new ArrayList<YKTrialProduct>();
                if (result.isSucceeded() && object != null) {
                    JSONArray tmpArray = object.optJSONArray("trial_products");
                    JSONObject tmpObject;
                    for (int i = 0; i < tmpArray.length(); i++) {
                        try {
                            tmpObject = tmpArray.getJSONObject(i);
                            products.add(YKTrialProduct.parse(tmpObject));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                
                //saveDataToFile(products);
                
                if (callback != null) {
                    callback.callback(result, products);
                }
            }
        });
    }

    /**
     * 确认收货
     * 
     * @param productID
     * @param callback
     * @return
     */
    public YKHttpTask receiveProduct(String productID,
            final ReceiveProductCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("trial_product_id", productID);

        if (YKCurrentUserManager.getInstance().isLogin()) {
            params.put("user_id", YKCurrentUserManager.getInstance().getUser()
                    .getId());
        }

        return super.postURL(PATH_RECEIVE_PRODUCT, params, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {

                if (callback != null) {
                    callback.callback(task, result);
                }
            }
        });
    }

    private boolean saveDataToFile(ArrayList<YKTrialProduct> trialProducts) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] data = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(trialProducts);
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

    private ArrayList<YKTrialProduct> loadDataFromFile() {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        byte[] data = YKFile.read(CACHE_NAME);
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            @SuppressWarnings("unchecked")
            ArrayList<YKTrialProduct> objectData = (ArrayList<YKTrialProduct>) ois
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

    public interface ReceiveProductCallback
    {
        public void callback(YKHttpTask task, YKResult result);
    }
}
