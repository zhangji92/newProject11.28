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
public class YKAvailManagers extends YKManager
{
    private static final String PATH = getBase() + "comment/avail";

    private static YKAvailManagers singleton = null;
    private static Object lock = new Object();


    public static YKAvailManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKAvailManagers();
            }
        }
        return singleton;
    }


    public YKHttpTask postYKAvail(final AvailCallback callback,String commentID,String uid) {

        // 后期增加的字段
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("commentID", commentID);
        parameters.put("uid", uid);

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

    public interface AvailCallback{
        public void callback(YKResult result); 
    }
}
