package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKObtainManagers extends YKManager
{
    private static final String PATH = getBase()+"passport/sendcode";
    private static YKObtainManagers singleton = null;
    private static Object lock = new Object();

    public static YKObtainManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKObtainManagers();
            }
        }
        return singleton;
    }

    public YKHttpTask postYKobtain(final YKObtainCallback callback,String mobile,String sign) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("mobile", mobile);
        parameters.put("sign", sign);
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result)
            {
                if (callback != null) {
                    callback.callback(result);
                }
            }
        });
    }
}
