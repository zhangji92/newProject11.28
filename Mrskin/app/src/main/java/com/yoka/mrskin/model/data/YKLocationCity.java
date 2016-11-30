package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKLocationCity extends YKData
{
    /**
     * 定位城市
     */
    private static final long serialVersionUID = 1L;
    private String mName;
    private String mProvince;
    public String getmName()
    {
        return mName;
    }
    public void setmName(String mName)
    {
        this.mName = mName;
    }
    public String getmProvince()
    {
        return mProvince;
    }
    public void setmProvince(String mProvince)
    {
        this.mProvince = mProvince;
    }
    public YKLocationCity(String mName, String mProvince)
        {
            super();
            this.mName = mName;
            this.mProvince = mProvince;
        }
    public YKLocationCity()
        {
            super();
        }
    public static YKLocationCity parse(JSONObject object) {
        YKLocationCity locationCity = new YKLocationCity();
        if (object != null) {
            locationCity.parseData(object);
        }
        return locationCity;
    }
    protected void parseData(JSONObject object) {

        super.parseData(object);
        
        try {
            mName = object.getString("name");
        } catch (JSONException e) {
        }
        try {
            mProvince = object.getString("province");
        } catch (JSONException e) {
        }
    }
}
