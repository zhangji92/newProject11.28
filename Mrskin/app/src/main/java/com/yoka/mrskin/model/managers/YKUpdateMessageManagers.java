package com.yoka.mrskin.model.managers;

import java.io.File;
import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKUpdateMessageManagers extends YKManager
{
    private static final String PATH = getBase() + "passport/edit_userinfo";
    public static String CACHE_NAME = "passWord";
    private static YKUpdateMessageManagers singleton = null;
    private static Object lock = new Object();

    public static YKUpdateMessageManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKUpdateMessageManagers();
            }
        }
        return singleton;
    }

    public YKHttpTask postYKUpdateMessage(final YKUpdateMessageCallback callback,
            String user_id,String nickname,String headimage) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("user_id",user_id);
        parameters.put("nickname",nickname);
        try {
            File file = new File(headimage);
            parameters.put("headimage", file);
        } catch (Exception e) {

        }

        return super.postFile(PATH, parameters, headimage, new Callback()
        {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result)
            {
                
            }
        });
    }     
}
