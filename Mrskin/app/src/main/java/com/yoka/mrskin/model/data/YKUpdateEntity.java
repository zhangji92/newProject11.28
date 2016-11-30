package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

@SuppressWarnings("serial")
public class YKUpdateEntity extends YKData
{
    private String mId;
    private String mName;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public YKUpdateEntity(String mId, String mName)
    {
        super();
        this.mId = mId;
        this.mName = mName;
    }

    public YKUpdateEntity()
    {
        super();
    }

    public static YKUpdateEntity parse(JSONObject object) {
        YKUpdateEntity userInfo = new YKUpdateEntity();
        if (object != null) {
            userInfo.parseData(object);
        }
        return userInfo;
    }

    protected void parseData(JSONObject object) {
        try {
            mId = object.optString("id");
        } catch (Exception e) {
        }
        try {
            mName = object.optString("name");
        } catch (Exception e) {
        }
    }

    @Override
    public String toString() {
        return "YKUpdateEntity [mId=" + mId + ", mName=" + mName + "]";
    }
    
}
