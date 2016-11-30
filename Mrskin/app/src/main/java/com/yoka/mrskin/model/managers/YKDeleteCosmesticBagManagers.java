package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 删除我的化妆包
 * 
 */
public class YKDeleteCosmesticBagManagers extends YKManager
{
    private static final String PATH = getBase() + "my/used";

    private static YKDeleteCosmesticBagManagers singleton = null;
    private static Object lock = new Object();


    public static YKDeleteCosmesticBagManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKDeleteCosmesticBagManagers();
            }
        }
        return singleton;
    }


    public YKHttpTask postYKCosmesticShop(final CosmesticBagCallback callback,String product_id,String user_id) {

        // 后期增加的字段
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("product_id", product_id);
        parameters.put("user_id", user_id);

        // do request
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                printRequestResult("YKDeleteCosmesticBagManagers", object, result);
                
                
                if (result.isSucceeded()) {
                    
                }
                // do callback
                if (callback != null) {
                    callback.callback(result);
                }
            }
        });
    }

    public interface CosmesticBagCallback{
        public void callback(YKResult result); 
    }
}
