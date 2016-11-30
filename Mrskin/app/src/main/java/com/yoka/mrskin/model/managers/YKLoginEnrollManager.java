package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.login.YKUser;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKLoginEnrollManager extends YKManager
{
    private static final String PATH = "passport/register";
    private static YKLoginEnrollManager singleton = null;
    private static Object lock = new Object();

    public static YKLoginEnrollManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKLoginEnrollManager();
            }
        }
        return singleton;
    }

    public YKHttpTask postLoginEnroll(final String username,
            final String password, final String mobile,final String code,final YKLoginEnrollCallback callback) {

        HashMap<String, Object> params = new HashMap<String, Object>();
        if (username != null && password != null && mobile !=null && code !=null) {
            params.put("username", username);
            params.put("password", password);
            params.put("mobile", mobile);
            params.put("code", code);
        } else {
            YKResult result = new YKResult();
            result.fail();
            callback.callback(result);
        }
        String url = getBase() + PATH;
        return super.postURL(url, params, new Callback() {
            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result)
            {
                YKUser loginUser = null;

                if(result.isSucceeded()){
                    JSONObject objtopic = null;
                    try {
                        objtopic = object.getJSONObject("user");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (objtopic != null) {
                        loginUser = new YKUser();
                        loginUser.paseUser(objtopic);
                    }
                    // 保存到本地 user
                    YKCurrentUserManager.getInstance().setUser(loginUser);

                }
                if (callback != null) {
                    callback.callback(result);
                }
            }
        });
    }
}
