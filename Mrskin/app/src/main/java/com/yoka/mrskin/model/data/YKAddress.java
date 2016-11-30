package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKAddress extends YKData
{
    /**
     * 
     */
    private static final long serialVersionUID = -2897136860927039955L;
    /**
     * 经度
     */
    private String mLongitudc;
    /**
     * 纬度
     */
    private String mLatitude;

    public String getLongitudc() {
        return mLongitudc;
    }

    public void setLongitudc(String longitudc) {
        this.mLongitudc = longitudc;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        this.mLatitude = latitude;
    }

    public YKAddress(String longitudc, String latitude)
    {
        super();
        this.mLongitudc = longitudc;
        this.mLatitude = latitude;
    }

    public YKAddress()
    {
        super();
    }

    public static YKAddress parse(JSONObject object) {
        YKAddress address = new YKAddress();
        if (object != null) {
            address.parseData(object);
        }
        return address;
    }

    protected void parseData(JSONObject object) {

        try {
            mLongitudc = object.getString("longitucd");
        } catch (JSONException e) {
        }

        try {
            mLatitude = object.getString("latitude");
        } catch (JSONException e) {
        }
    }
}
