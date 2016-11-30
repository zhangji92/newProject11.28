package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKExperienceTag extends YKData {

    /**
     * 心得分类数据
     */
    private static final long serialVersionUID = 3036524765790092650L;

    private String mName;

    public String getmName()
    {
        return mName;
    }

    public void setmName(String mName)
    {
        this.mName = mName;
    }

    public static YKExperienceTag parse(JSONObject object) {
        YKExperienceTag topic = new YKExperienceTag();
        if (object != null) {
            topic.parseData(object);
        }
        return topic;
    }

    public YKExperienceTag()
        {
            super();
        }

    public YKExperienceTag(String mName)
        {
            super();
            this.mName = mName;
        }

    protected void parseData(JSONObject object) {

        super.parseData(object);

        try {
            mName = object.getString("name");
        } catch (JSONException e) {
        }
    }

    public JSONObject toJson() {
        JSONObject object =new JSONObject();
        if (mName != null) {
            try {
                object.put("name", mName);
            } catch (Exception e ){}
        }
        return object;
    }
}
