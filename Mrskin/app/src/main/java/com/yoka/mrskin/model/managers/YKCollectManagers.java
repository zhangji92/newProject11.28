package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 收藏
 * 
 */
public class YKCollectManagers extends YKManager
{
    private static final String PATH = getBase() + "comment/collect";

    private static YKCollectManagers singleton = null;
    private static Object lock = new Object();


    public static YKCollectManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKCollectManagers();
            }
        }
        return singleton;
    }


    public YKHttpTask postYKCollect(final ColectCallback callback,String commentID,String uid,String type) {

        // 后期增加的字段
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("commentID", commentID);
        parameters.put("uid", uid);
        parameters.put("type", type);

        // do request
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                printRequestResult("YKCollectManagers", object, result);
                
                
                if (result.isSucceeded()) {
                    
                }
                // do callback
                if (callback != null) {
                    callback.callback(result);
                }
            }
        });
    }

    public interface ColectCallback{
        public void callback(YKResult result); 
    }
}
