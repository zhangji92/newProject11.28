package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.login.YKUser;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKLoginRegisteManager extends YKManager
{
    private static final String PATH = "passport/login";
    private static YKLoginRegisteManager singleton = null;
    private static Object lock = new Object();
    private YKUser mUser;

    public static YKLoginRegisteManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKLoginRegisteManager();
            }
        }
        return singleton;
    }

    public YKHttpTask postLoginRegiste(final String username,
            final String password, final YKLoginRegistCallback callback) {

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", password);

        String url = getBase() + PATH;
        return super.postURL(url, params, new Callback() {
            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                mUser = new YKUser();
                if (result.isSucceeded() && object != null) {
                    JSONObject objtopic = null;
                    try {
                        objtopic = object.getJSONObject("user");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (objtopic != null) {
                        mUser.paseUser(objtopic);
                    }
                    // 保存到本地 user
                    YKCurrentUserManager.getInstance().setUser(mUser);
                }
                if (callback != null) {
                    callback.callback(result, mUser);
                }
            }
        });
    }
}
