package com.yoka.mrskin.login;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * sso登录数据结构
 * 
 * @author Jack
 * 
 */
public class YKUser implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1293877504402268049L;

    // {
    // "message": "",
    // "id": "54c9967de837a074058b4567",
    // "user_id": "5430931596",
    // "code": 0,
    // "avatar": {
    // "id": "",
    // "url": "http:\/\/tp1.sinaimg.cn\/5430931596\/180\/0\/0",
    // "height": "",
    // "width": ""
    // },
    // "name": "肤君"
    // }
    public void paseUser(JSONObject object) {
        int tag = -1;
        tag = object.optInt("is_skintested");
        if (tag == 0) {
            isSkinTest = false;
        } else {
            isSkinTest = true;
        }
        jsonData = object;
        id = object.optString("id");
        token = object.optString("passport");
        name = object.optString("name");
        mAuth = object.optString("passport");
        JSONObject avatarObj = object.optJSONObject("avatar");
        YKAvatar a = YKAvatar.parse(avatarObj);
        avatar = a;

        mTrialEventCount = object.optInt("trial_event_count");
    }

    public JSONObject getJsonData() {
        return jsonData;
    }

    public void setJsonData(JSONObject jsonData) {
        this.jsonData = jsonData;
    }

    private String id;
    private String name;
    private String token;
    private YKAvatar avatar;
    private String mAuth;

    private JSONObject jsonData;
    private int mTrialEventCount;
    private boolean isSkinTest;

    public boolean isSkinTest() {
        return isSkinTest;
    }

    public void setSkinTest(boolean isSkinTest) {
        this.isSkinTest = isSkinTest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public YKAvatar getAvatar() {
        return avatar;
    }

    public void setAvatar(YKAvatar avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAuth() {
        return mAuth;
    }

    public void setTrialEventCount(int count) {
        mTrialEventCount = count;
    }

    public int getTrialEventCount() {
        return mTrialEventCount;
    }

    @Override
    public String toString() {
        return "YKUser [id=" + id + ", name=" + name + ", token=" + token
                + ", avatar=" + avatar + ", jsonData=" + jsonData + "]";
    }
    public static YKUser parse(JSONObject object) {
    	YKUser user = new YKUser();
        if (object != null) {
        	user.paseUser(object);
        }
        return user;
    }
}
