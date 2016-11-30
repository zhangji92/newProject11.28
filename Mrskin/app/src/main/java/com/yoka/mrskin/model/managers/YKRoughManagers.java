package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

/**
 * 我长草的
 * 
 */
public class YKRoughManagers extends YKManager
{
    private static final String PATH = getBase() + "grass/list";

    private static YKRoughManagers singleton = null;
    private static Object lock = new Object();

    public static YKRoughManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKRoughManagers();
            }
        }
        return singleton;
    }

    public YKHttpTask postYKcommentShop(final CommentShopCallbackZ callback,
            int page_index, int count, String user_id) {

        // 后期增加的字段
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("page_index", page_index);
        parameters.put("count", count);
        parameters.put("user_id", user_id);

        // do request
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                printRequestResult("postYKcommentShop", object, result);

                ArrayList<YKProduct> product = null;

                if (result.isSucceeded()) {
                    JSONArray tmpArray = object.optJSONArray("products");
                    if (tmpArray != null) {
                        product = new ArrayList<YKProduct>();
                        for (int i = 0; i < tmpArray.length(); ++i) {
                            try {
                                product.add(YKProduct.parse(tmpArray
                                        .getJSONObject(i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                // do callback
                if (callback != null) {
                    callback.callback(result, product);
                }

            }
        });
    }

    public interface CommentShopCallbackZ
    {
        public void callback(YKResult result, ArrayList<YKProduct> product);
    }
}
