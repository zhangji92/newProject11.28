package com.yoka.mrskin.login;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class YKUserInfo implements Serializable {

    /**
     * 新的会员信息类
     */
    private static final long serialVersionUID = -4122985124913567603L;

    private String nickname;
    private String user_level;
    private String levelvalue;
    private YKAvatar avatar;
    private int signed_day;
    private String money;
    private String is_signed;
    private String levelname;
    
    private JSONObject jsonData;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getLevelvalue() {
        return levelvalue;
    }

    public void setLevelvalue(String levelvalue) {
        this.levelvalue = levelvalue;
    }

    public YKAvatar getAvatar() {
        return avatar;
    }

    public void setAvatar(YKAvatar avatar) {
        this.avatar = avatar;
    }

    public int getSigned_day() {
        return signed_day;
    }

    public void setSigned_day(int signed_day) {
        this.signed_day = signed_day;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIs_signed() {
        return is_signed;
    }

    public void setIs_signed(String is_signed) {
        this.is_signed = is_signed;
    }

    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public static YKUserInfo parse(JSONObject object) {
        YKUserInfo user = new YKUserInfo();
        if (object != null) {
            user.parseData(object);
        }
        return user;
    }
    protected void parseData(JSONObject object) {

        try {
            nickname = object.getString("nickname");
        } catch (JSONException e) {
        }

        try {
            user_level = object.getString("user_level");
        } catch (JSONException e) {
        }

        try {
            levelvalue = object.getString("levelvalue");
        } catch (JSONException e) {
        }
        try {
            signed_day = object.getInt("signed_day");
        } catch (JSONException e) {
        }
        try {
            money = object.getString("money");
        } catch (JSONException e) {
        }
        try {
            JSONObject tmpObject = object.getJSONObject("avatar");
            avatar = YKAvatar.parse(tmpObject);
        } catch (JSONException e) {
        }
        try {
            is_signed = object.getString("is_signed");
        } catch (JSONException e) {
        }
        try {
            levelname = object.getString("levelname");
        } catch (JSONException e) {
        }

    }

	public JSONObject getJsonData() {
		return jsonData;
	}

	public void setJsonData(JSONObject jsonData) {
		this.jsonData = jsonData;
	}
    
    

}
