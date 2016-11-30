package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 添加咨询回复接口
 *
 */
public class YKEnquiryStataAddManagers extends YKManager
{
    private static final String PATH = getBase() + "add/topicreply";

    private static YKEnquiryStataAddManagers singleton = null;
    private static Object lock = new Object();


    public static YKEnquiryStataAddManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKEnquiryStataAddManagers();
            }
        }
        return singleton;
    }


    public YKHttpTask postYKEnquiryPointAdd(final EnquiryPointPraiseCallback callback,String authtoken,String topicId,String content) {

        // 后期增加的字段
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("authtoken", authtoken);
        parameters.put("topicId", topicId);
        parameters.put("content", content);

        // do request
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {

                printRequestResult("YKEnquiryStataAddManagers", object, result);

                if (result.isSucceeded()) {

                }
                
                // do callback
                if (callback != null) {
                    callback.callback(result);
                }
            }
        });
    }

    public interface EnquiryPointPraiseCallback{
        public void callback(YKResult result); 
    }
}
