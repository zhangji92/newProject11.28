package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

@SuppressWarnings("serial")
public class YKUserAddress extends YKData
{
    private String mUId;
    private String mName;
    private String mTtelephone;
    private String mAddress;
    private String mZipcode;
    private String mCityId;
    private String mCity;
    private String mProvince;

    public String getmId() {
        return mUId;
    }

    public void setmId(String mUId) {
        this.mUId = mUId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmTtelephone() {
        return mTtelephone;
    }

    public void setmTtelephone(String mTtelephone) {
        this.mTtelephone = mTtelephone;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmZipcode() {
        return mZipcode;
    }

    public void setmZipcode(String mZipcode) {
        this.mZipcode = mZipcode;
    }

    public String getmCityId() {
        return mCityId;
    }

    public void setmCityId(String mCityId) {
        this.mCityId = mCityId;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmProvince() {
        return mProvince;
    }

    public void setmProvince(String mProvince) {
        this.mProvince = mProvince;
    }

    public YKUserAddress(String mId, String mName, String mTtelephone,
            String mAddress, String mZipcode, String mCityId, String mCity,
            String mProvince)
    {
        super();
        this.mUId = mId;
        this.mName = mName;
        this.mTtelephone = mTtelephone;
        this.mAddress = mAddress;
        this.mZipcode = mZipcode;
        this.mCityId = mCityId;
        this.mCity = mCity;
        this.mProvince = mProvince;
    }

    public YKUserAddress()
    {
        super();
    }
    
    public static YKUserAddress parse(JSONObject object) {
        YKUserAddress address = new YKUserAddress();
        if (object != null) {
            address.parseData(object);
        }
        return address;
    }

    protected void parseData(JSONObject object) {
        try {
            mUId = object.optString("uid");
        } catch (Exception e) {
        }
        try {
            mName = object.optString("name");
        } catch (Exception e) {
        }
        try {
            mTtelephone = object.optString("telephone");
        } catch (Exception e) {
        }
        try {
            mAddress = object.optString("address");
        } catch (Exception e) {
        }
        try {
            mZipcode = object.optString("zipcode");
        } catch (Exception e) {
        }
        try {
            mCityId = object.optString("city_id");
        } catch (Exception e) {
        }
        try {
            mCity = object.optString("city");
        } catch (Exception e) {
        }
        try {
            mProvince = object.optString("province");
        } catch (Exception e) {
        }
    }
}
