package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.yoka.mrskin.model.base.YKData;

public class YKCity extends YKData
{
    /**
     * 城市名称
     */
    private static final long serialVersionUID = -5804268011049970071L;
    
    private String mCityName;
    private String mCityDesc;

    public String getmCity()
    {
        return mCityName;
    }

    public void setmCity(String mCity)
    {
        this.mCityName = mCity;
    }

    public YKCity(String mCity)
        {
            super();
            this.mCityName = mCity;
        }

    public String getmCityDesc()
    {
        return mCityDesc;
    }

    public void setmCityDesc(String mCityDesc)
    {
        this.mCityDesc = mCityDesc;
    }

    public YKCity()
        {
            super();
        }

    public static YKCity parse(JSONObject object)
    {
        YKCity city = new YKCity();
        if (object != null) {
            city.parseData(object);
        }
        return city;
    }

    public static YKCity parse(JSONObject object, String parentDesc)
    {
        YKCity city = parse(object);
        if (!TextUtils.isEmpty(parentDesc)) {
            city.mCityDesc = parentDesc;
        }
        return city;
    }

    protected void parseData(JSONObject object)
    {
        super.parseData(object);
        try {
            mCityName = object.getString("name");
        } catch (JSONException e) {
        }
    }

    @Override
    public String toString()
    {
        return mCityName;
    }

}
