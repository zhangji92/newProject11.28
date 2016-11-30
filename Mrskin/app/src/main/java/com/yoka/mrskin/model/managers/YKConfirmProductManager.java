package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import android.text.TextUtils;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKConfirmProductManager extends YKManager
{
    private static final String PATH = "try/confirm";
    private static YKConfirmProductManager singleton = null;
    private static Object lock = new Object();

    public static YKConfirmProductManager getInstnace() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKConfirmProductManager();
            }
        }
        return singleton;
    }

    private YKConfirmProductManager()
    {
        super();
    }

    public YKHttpTask requestConfirmProduct(String productId, String userId,
            final YKGeneralCallBack callback) {
        String url = getBase() + PATH;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(productId)) {
            parameters.put("trial_product_id", productId);
        }
        if (!TextUtils.isEmpty(userId)) {
            parameters.put("user_id", userId);
        }
        return super.postURL(url, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                if (result.isSucceeded()) {
                    // parse data;
                }
                // do callback
                if (callback != null) {
                    callback.callback(result);
                }
            }
        });
    }

    public interface YKGeneralCallBack
    {
        public void callback(YKResult result);
    }

}
