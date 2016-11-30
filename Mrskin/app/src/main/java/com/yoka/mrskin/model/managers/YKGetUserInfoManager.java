package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.login.YKUserInfo;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKGetUserInfoManager extends YKManager {

    private static final String PATH = getBase()+"user/info";
    private static YKGetUserInfoManager singleton = null;
    private static Object lock = new Object();
    private YKUserInfo mUser;

    public static YKGetUserInfoManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKGetUserInfoManager();
            }
        }
        return singleton;
    }

    public YKHttpTask postGetUserInfoManager(final String user_id, final Callback callback) {

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("user_id", user_id);

        // String url = BASE + PATH;
        String url = PATH;
        return super.postURL(url, params, new com.yoka.mrskin.model.managers.base.Callback() {
            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

                if (object != null) {
                    JSONObject tmpArray = null;
                    try {
                        tmpArray = object.getJSONObject("user_info");
                        mUser = YKUserInfo.parse(tmpArray);
                        mUser.setJsonData(tmpArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (callback != null) {
                    callback.callback(result, mUser);
                }
            }
        });
    }

    public interface Callback {
        public void callback(YKResult result, YKUserInfo mYkUserInfo);
    }
}
