package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 删除我长草的
 * 
 */
public class YKDeleteRoughManagers extends YKManager
{
    private static final String PATH = getBase() + "my/seeding";

    private static YKDeleteRoughManagers singleton = null;
    private static Object lock = new Object();


    public static YKDeleteRoughManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKDeleteRoughManagers();
            }
        }
        return singleton;
    }


    public YKHttpTask postYKcommentShop(final CommentShopCallback callback,String product_id,String user_id) {

        // 后期增加的字段
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("product_id", product_id);
        parameters.put("user_id", user_id);

        // do request
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                printRequestResult("YKDeleteRoughManagers", object, result);
                
                
                if (result.isSucceeded()) {
                    
                }
                // do callback
                if (callback != null) {
                    callback.callback(result);
                }



            }
        });
    }

    public interface CommentShopCallback{
        public void callback(YKResult result); 
    }
}
