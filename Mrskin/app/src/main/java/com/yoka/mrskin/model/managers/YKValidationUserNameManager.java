package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKValidationUserNameManager extends YKManager {
    
    private static final String PATH = getBase() + "check/username";
    private static YKValidationUserNameManager singleton = null;
    private static Object lock = new Object();

    public static YKValidationUserNameManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKValidationUserNameManager();
            }
        }
        return singleton;
    }

    public YKHttpTask postYKValidationUserName(String userName,final UserNameCallback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("username", userName);
        return super.postURL(PATH, parameters, new com.yoka.mrskin.model.managers.base.Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

                if (result.isSucceeded()) {
                    
                }

                if (callback != null) {
                    callback.callback(result);
                }
            }
        });
    }




    public interface UserNameCallback {
        public void callback(YKResult result);
    }


}
