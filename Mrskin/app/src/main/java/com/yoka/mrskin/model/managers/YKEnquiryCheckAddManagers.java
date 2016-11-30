package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 添加咨询点赞接口
 *
 */
public class YKEnquiryCheckAddManagers extends YKManager
{
    private static final String PATH = getBase() + "add/topiclike";

    private static YKEnquiryCheckAddManagers singleton = null;
    private static Object lock = new Object();


    public static YKEnquiryCheckAddManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKEnquiryCheckAddManagers();
            }
        }
        return singleton;
    }


    public YKHttpTask postYKEnquiryPointAdd(final EnquiryPointAddCallback callback, String topicId,String uid) {

        // 后期增加的字段
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("topicID", topicId);
        parameters.put("uid", uid);

        // do request
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {

                printRequestResult("YKEnquiryCheckAddManagers", object, result);
                
                if (result.isSucceeded()) {

                }
                
                // do callback
                if (callback != null) {
                    callback.callback(result);
                }
            }
        });
    }

    public interface EnquiryPointAddCallback{
        public void callback(YKResult result); 
    }
}
