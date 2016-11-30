package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import android.widget.Toast;

import com.yoka.mrskin.main.AppContext;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKUpdatePasswordManagers extends YKManager
{
    private static final String PATH = getBase() + "passport/edit_pass";
    public static String CACHE_NAME = "passWord";
    private static YKUpdatePasswordManagers singleton = null;
    private static Object lock = new Object();

    public static YKUpdatePasswordManagers getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKUpdatePasswordManagers();
            }
        }
        return singleton;
    }

    public YKHttpTask postYKUpdatePassword(final YKUpdatePasswordCallback callback,
            String user_id,String password,String newpassword,String newpswdagain) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("user_id",user_id);
        parameters.put("password",password);
        parameters.put("newpassword",newpassword);
        parameters.put("newpswdagain",newpswdagain);

        return super.postURL(PATH, parameters, new Callback()
        {
            
            @Override
            public void doCallback(YKHttpTask task, JSONObject object, YKResult result)
            {
               if(result.isSucceeded()){
                   Toast.makeText(AppContext.getInstance(), "密码修改成功", Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(AppContext.getInstance(), "密码修改失败", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
}
