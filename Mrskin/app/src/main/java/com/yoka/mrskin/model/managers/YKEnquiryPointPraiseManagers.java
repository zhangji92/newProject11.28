package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 咨询点赞状态接口
 *
 */
public class YKEnquiryPointPraiseManagers extends YKManager
{
    private static final String PATH = getBase() + "user/topiclikestatus";

    private static YKEnquiryPointPraiseManagers singleton = null;
    private static Object lock = new Object();


    public static YKEnquiryPointPraiseManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKEnquiryPointPraiseManagers();
            }
        }
        return singleton;
    }


    public YKHttpTask postYKEnquiryPointPraise(final EnquiryPointPraiseCallback callback,String uid,String topicId) {

        // 后期增加的字段
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uid", uid);
        parameters.put("topicID", topicId);

        // do request
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {

                printRequestResult("YKEnquiryPointPraiseManagers", object, result);
                JSONObject obj = null;
                String avail = null;
                if (result.isSucceeded()) {
                    try {
                        obj = object.getJSONObject("result");
                        avail = obj.getString("avail");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // do callback
                if (callback != null) {
                    callback.callback(result,avail);
                }
            }
        });
    }

    public interface EnquiryPointPraiseCallback{
        public void callback(YKResult result,String avail); 
    }
}
