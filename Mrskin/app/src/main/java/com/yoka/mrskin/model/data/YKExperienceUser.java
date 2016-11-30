package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKExperienceUser extends YKData {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String trial_event_count;
    private int complexion;
    private int age;
    private String name;
    private int vip;
    private YKImage avatar;

    public String getTrial_event_count() {
        return trial_event_count;
    }

    public void setTrial_event_count(String trial_event_count) {
        this.trial_event_count = trial_event_count;
    }

    public int getComplexion() {
        return complexion;
    }

    public void setComplexion(int complexion) {
        this.complexion = complexion;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public YKImage getAvatar() {
        return avatar;
    }

    public void setAvatar(YKImage avatar) {
        this.avatar = avatar;
    }

    public static YKExperienceUser parse(JSONObject object) {
        YKExperienceUser user = new YKExperienceUser();
        if (object != null) {
            user.parseData(object);
        }
        return user;
    }

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}


    protected void parseData(JSONObject object) {
        super.parseData(object);

        try {
            trial_event_count = object.optString("trial_event_count");
        } catch (Exception e) {
        }
        try {
            age = object.optInt("age");
        } catch (Exception e) {
        }
        try {
            name = object.optString("name");
        } catch (Exception e) {
        }
        try {
            complexion = object.optInt("complexion");
        } catch (Exception e) {
        }
        try {
            avatar = YKImage.parse(object.getJSONObject("avatar"));
        } catch (Exception e) {
        }
        try {
            vip = object.optInt("vip");
        } catch (Exception e) {
        }
    }
    
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("trial_event_count", trial_event_count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            object.put("age", age);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            object.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }try {
            object.put("complexion", complexion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            object.put("avatar", avatar.toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            object.put("vip", vip);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
