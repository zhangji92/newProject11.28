package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.UHCity;
import com.yoka.mrskin.model.data.YKUpdateEntity;
import com.yoka.mrskin.model.data.YKUpdateUserInfo;
import com.yoka.mrskin.model.data.YKUserAddress;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

/**
 * 请求用户的所有信息
 * 
 * @author z l l
 * 
 */
public class YKUpdateUserInfoManager extends YKManager
{
    private static final int LIKE = 11;
    private static final int MONEY = 22;
    private static final int AGE = 33;
    private static final String PATH = getBase() + "user/profile";
    private static final String PATH_LIKE = getBase() + "user/brandlike";
    private static final String PATH_MONEY = getBase() + "user/money";
    private static final String PATH_AGE = getBase() + "user/age";
    private static final String PATH_IMG = getBase() + "user/updateavatar";
    private static final String PATH_UPDATE = getBase() + "user/updateprofile";
    private static final String PATH_PHONE = getBase() + "send/mobilecode";
    private static final String PATH_ADDRESS = getBase() + "user/address";
    private static final String PATH__UPDATE_ADDRESS = getBase() + "user/updateaddress";
    private static YKUpdateUserInfoManager singleton = null;
    private static Object lock = new Object();

    public static YKUpdateUserInfoManager getInstance() {
        synchronized (lock) {
            if (singleton == null) {
                singleton = new YKUpdateUserInfoManager();
            }
        }
        return singleton;
    }

    /**
     * 获取用户信息
     * 
     * @param callback
     * @return
     */
    public YKHttpTask requestUserInfo(final UpdateUserInfoCallback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("authtoken", YKCurrentUserManager.getInstance().getUser().getAuth());
        return super.postURL(PATH, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                YKUpdateUserInfo userInfo = null;
                if (result.isSucceeded()) {
                    JSONObject objInfo = null;
                    if (object != null) {
                        try {
                            objInfo = object.getJSONObject("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (objInfo != null) {
                            userInfo = YKUpdateUserInfo.parse(objInfo);
                        }
                    }
                }
                if (callback != null) {
                    callback.callback(result, userInfo);
                }
            }
        });
    }

    /**
     * 获取品牌偏好、美妆月消费、年龄信息
     * 
     * @param type
     * @param callback
     * @return
     */
    public YKHttpTask requestUserEntities(int type,
            final UpdateUserEntitiesCallback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("authtoken", YKCurrentUserManager.getInstance()
                .getUser().getAuth());
        String url = "";
        switch (type) {
        case LIKE:
            url = PATH_LIKE;
            break;
        case MONEY:
            url = PATH_MONEY;
            break;
        case AGE:
            url = PATH_AGE;
            break;
        }
        return super.postURL(url, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                ArrayList<YKUpdateEntity> userEntities = null;
                if (result.isSucceeded()) {
                    JSONArray tmpArray = null;
                    tmpArray = object.optJSONArray("result");
                    if (tmpArray != null) {
                        JSONObject tmpObject;
                        userEntities = new ArrayList<YKUpdateEntity>();
                        for (int i = 0; i < tmpArray.length(); ++i) {
                            try {
                                tmpObject = tmpArray.getJSONObject(i);
                                userEntities.add(YKUpdateEntity
                                        .parse(tmpObject));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (callback != null) {
                        callback.callback(result, userEntities);
                    }
                }
            }
        });
    }

    /**
     * 更换头像
     * 
     * @param userToken
     * @param filePath
     * @param callback
     * @return
     */
    public YKHttpTask requestUpdateUserHead(String userToken, String filePath,final YKGeneralCallBack callback) {
        // handle parameters
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(userToken)) {
            params.put("authtoken", userToken);
        }
        HashMap<String, String> file = new HashMap<String, String>();
        if (filePath != null) {
            file.put("face_img", filePath);
        }
        String url = PATH_IMG;
        return super.postFiles(url, params, file, new Callback() {
            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                JSONObject obj = null;
                String image = null;
                if (result.isSucceeded() && object != null) {

                    try {
                        obj = object.getJSONObject("result");
                        image = obj.getString("avatar");
                        
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                //                Toast.makeText(AppContext.getInstance(),
                //                        object.toString() + "", Toast.LENGTH_SHORT).show();
                if (callback != null) {
                    callback.callback(result,image);
                }
            }
        });
    }

    /**
     * 修改资料
     * 
     * @param key
     * @param value
     * @param callback
     * @return
     */
    public YKHttpTask requestUpdateUSerInfo(String key, String value,final YKGeneralCallBack callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        if (YKCurrentUserManager.getInstance().isLogin()) {
            parameters.put("authtoken", YKCurrentUserManager.getInstance()
                    .getUser().getAuth());
        }
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            parameters.put(key, value + "");
        }
        return super.postURL(PATH_UPDATE, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                if (result.isSucceeded()) {

                }

                if (callback != null) {
                    callback.callback(result,null);
                }
            }
        });

    }

    /**
     * 修改地区
     * 
     * @param city
     * @param callback
     * @return
     */
    public YKHttpTask requestUpdateAddress(UHCity city,final YKGeneralCallBack callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        if (city != null) {
            parameters.put("authtoken", YKCurrentUserManager.getInstance()
                    .getUser().getAuth());
            parameters.put("city_id", city.getID());
            parameters.put("city", city.getCityName());
            parameters.put("province", city.getProvinceName());
        }
        return super.postURL(PATH_UPDATE, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                if (result.isSucceeded()) {

                }
                if (callback != null) {
                    callback.callback(result,null);
                }
            }
        });
    }

    /**
     * 发送验证码
     * 
     * @param key
     * @param value
     * @param callback
     * @return
     */
    public YKHttpTask requestUpdatePhone(String key1,
            String value1, String key2, String value2,final YKGeneralCallBack callback) {
        String url = "";
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("authtoken", YKCurrentUserManager.getInstance()
                .getUser().getAuth());
        parameters.put(key1, value1);
        parameters.put(key2, value2);
        url = PATH_PHONE;
        return super.postURL(url, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                if (result.isSucceeded()) {

                }
                if (callback != null) {
                    callback.callback(result,null);
                }
            }
        });
    }
    
    /**
     * 确定修改手机号
     * 
     * @param key
     * @param value
     * @param callback
     * @return
     */
    public YKHttpTask requestSureUpdatePhone(String key1,
            String value1, String key2, String value2,final YKGeneralCallBack callback) {
        String url = "";
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("authtoken", YKCurrentUserManager.getInstance()
                .getUser().getAuth());
        parameters.put(key1, value1);
        parameters.put(key2, value2);
        url = PATH_UPDATE;
        return super.postURL(url, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                if (result.isSucceeded()) {

                }
                if (callback != null) {
                    callback.callback(result,null);
                }
            }
        });
    }

    /**
     * 获取地址
     * 
     * @return
     */
    public YKHttpTask requestAddress(final UpdateAddressCallback callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("authtoken", YKCurrentUserManager.getInstance()
                .getUser().getAuth());
        return super.postURL(PATH_ADDRESS, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                YKUserAddress address = null;
                if (result.isSucceeded() && object != null) {
                    JSONObject addressObject = null;
                    try {
                        addressObject = object.optJSONObject("result");
                    } catch (Exception e) {
                    }
                    if (addressObject != null) {
                        address = YKUserAddress.parse(addressObject);
                    }
                    if (callback != null) {
                        callback.callback(result, address);
                    }
                }
            }
        });
    }

    /**
     * 修改地址
     * 
     * @param address
     * @param callback
     * @return
     */
    public YKHttpTask requestUpdateAddress(YKUserAddress address,final YKGeneralCallBack callback) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("authtoken", YKCurrentUserManager.getInstance()
                .getUser().getAuth());
        if (!TextUtils.isEmpty(address.getmName())) {
            parameters.put("name", address.getmName());
        }
        if (!TextUtils.isEmpty(address.getmName())) {
            parameters.put("telephone", address.getmTtelephone());
        }
        if (!TextUtils.isEmpty(address.getmName())) {
            parameters.put("address", address.getmAddress());
        }
        if (!TextUtils.isEmpty(address.getmName())) {
            parameters.put("zipcode", address.getmZipcode());
        }
        if (!TextUtils.isEmpty(address.getmName())) {
            parameters.put("city_id", address.getmCityId());
        }
        if (!TextUtils.isEmpty(address.getmName())) {
            parameters.put("city", address.getmCity());
        }
        if (!TextUtils.isEmpty(address.getmName())) {
            parameters.put("province", address.getmProvince());
        }
        return super.postURL(PATH__UPDATE_ADDRESS, parameters, new Callback() {

            @Override
            public void doCallback(YKHttpTask task, JSONObject object,
                    YKResult result) {
                if (result.isSucceeded()) {

                }

                if (callback != null) {
                    callback.callback(result,null);
                }
            }
        });
    }

    public interface UpdateAddressCallback
    {
        public void callback(YKResult result, YKUserAddress address);
    }

    public interface UpdateUserEntitiesCallback
    {
        public void callback(YKResult result,
                ArrayList<YKUpdateEntity> userEneities);
    }

    public interface UpdateUserInfoCallback
    {
        public void callback(YKResult result, YKUpdateUserInfo userInfo);
    }
    
    public interface YKGeneralCallBack
    {
        public void callback(YKResult result,String imageUrl);
    }
}
