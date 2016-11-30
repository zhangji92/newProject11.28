package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.yoka.mrskin.R;
import com.yoka.mrskin.login.AuthorUser;
import com.yoka.mrskin.login.YKUser;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.MD5;

/**
 * 登录
 * 
 * @author Y H L
 * 
 */
public class YKLoginManager extends YKManager
{
    private static YKLoginManager singleton = null;
    private static Object lock = new Object();
    private static final String PATH = getBase() + "passport/other";

    public static YKLoginManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKLoginManager();
            }
        }
        return singleton;
    }

    public YKHttpTask requestLoginInfoSina(final Context context, AuthorUser user,
            final YKLoginCallback callback) {

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("gender", user.getGender());
        parameters.put("id", user.getUser_id());
        parameters.put("platform", user.getType());
        parameters.put("name", user.getNickname());
        parameters.put("access_token", user.getAccess_token());
        parameters.put("avatar", user.getAvatar_url());
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                YKUser user = null;
                printRequestResult("requestLoginInfo", object, result);
                if (result.isSucceeded() && object != null) {
                    System.out.println("ykloginmanager login object = "
                            + object.toString());
                    JSONObject userObj = null;
                    try {
                        userObj = (JSONObject) object.get("user");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    user = new YKUser();
                    user.paseUser(userObj);
                    // 保存到本地 user
                    YKCurrentUserManager.getInstance().setUser(user);
                    if (callback != null) {
                        callback.callback(result, user);
                    }

                } else {
                    if (callback != null && object != null) {
                        try {
                            callback.callbackFaile(object.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context, R.string.intent_error,
                                Toast.LENGTH_LONG).show();
                    }
                }

            }

        });

    }

    public YKHttpTask requestLoginInfoQQ(final Context context,AuthorUser user, final YKLoginCallback callback) {
        String sex;
        if ("男".equals(user.getGender())) {
            sex = "1";
        } else if ("女".equals(user.getGender())) {
            sex = "2";
        } else {
            sex = "0";
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("qq_id", user.getUser_id());
            obj.put("qq_name", user.getNickname());
            obj.put("qq_gender", sex);
            obj.put("qq_image_url", user.getAvatar_url());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("qq_id", user.getUser_id());
        parameters.put("qq_name", user.getNickname());
        parameters.put("qq_gender", sex);
        parameters.put("qq_image_url", user.getAvatar_url());
        parameters.put("qq_sign",MD5.Md5(user.getUser_id() + user.getNickname()+ "yokaapp"));
        parameters.put("more_info", obj.toString());
        parameters.put("platform", user.getType());
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                YKUser user = null;
                printRequestResult("requestLoginInfo", object, result);
                if (result.isSucceeded() && object != null) {
                    JSONObject userObj = null;
                    try {
                        userObj = (JSONObject) object.get("user");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    user = new YKUser();
                    user.paseUser(userObj);
                    // 保存到本地 user
                    YKCurrentUserManager.getInstance().setUser(user);
                    if (callback != null) {
                        callback.callback(result, user);
                    }

                } else {
                    if (callback != null && object != null) {
                        try {
                            callback.callbackFaile(object.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context, R.string.intent_error,
                                Toast.LENGTH_LONG).show();
                    }
                }

            }

        });

    }
    
    public YKHttpTask requestLoginInfoWeChat(final Context context,AuthorUser user, final YKLoginCallback callback) {
        String sex;
        if ("男".equals(user.getGender())) {
            sex = "1";
        } else if ("女".equals(user.getGender())) {
            sex = "2";
        } else {
            sex = "0";
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("weixin_id", user.getUser_id());
            obj.put("weixin_name", user.getNickname());
            obj.put("platform", user.getType());
            obj.put("weixin_image_url", user.getAvatar_url());
            obj.put("weixin_sign",MD5.Md5(user.getUser_id() + user.getNickname()+ "_weixin_yokaapp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("weixin_id", user.getUser_id());
        parameters.put("weixin_name", user.getNickname());
//        parameters.put("qq_gender", sex);
        parameters.put("weixin_image_url", user.getAvatar_url());
        parameters.put("weixin_sign",MD5.Md5(user.getUser_id() + user.getNickname()+ "_weixin_yokaapp"));
        parameters.put("more_info", obj.toString());
        parameters.put("platform", user.getType());
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                YKUser user = null;
                printRequestResult("requestLoginInfo", object, result);
                if (result.isSucceeded() && object != null) {
                    JSONObject userObj = null;
                    try {
                        userObj = (JSONObject) object.get("user");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    user = new YKUser();
                    user.paseUser(userObj);
                    // 保存到本地 user
                    YKCurrentUserManager.getInstance().setUser(user);
                    if (callback != null) {
                        callback.callback(result, user);
                    }

                } else {
                    if (callback != null && object != null) {
                        try {
                            callback.callbackFaile(object.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context, R.string.intent_error,
                                Toast.LENGTH_LONG).show();
                    }
                }

            }

        });

    }

    public YKHttpTask requestLoginInfo4Obj(Context context, JSONObject obj,
            final YKLoginCallback callback) {
        HashMap<String, JSONObject> params = new HashMap<String, JSONObject>();
        params.put("sns_platform", obj);
        return super.postURL4JsonObj(PATH, params, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                YKUser user = null;
                if (result.isSucceeded()) {
                    Log.e("-------login", object.toString());
                    user = new YKUser();
                    user.paseUser(object);
                    // 保存全局变量user；
                }
                if (callback != null) {
                    callback.callback(result, user);
                }
            }
        });
    }
}
