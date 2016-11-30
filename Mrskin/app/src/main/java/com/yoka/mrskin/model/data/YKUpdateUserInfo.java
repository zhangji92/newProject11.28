package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import com.yoka.mrskin.login.YKAvatar;
import com.yoka.mrskin.model.base.YKData;

public class YKUpdateUserInfo extends YKData
{
    private String mId;
    private String mName;
    private String mSkin;
    private String mBrandLike;
    private String mUserMoney;
    private String mUserAge;
    private String mSex;
    private String mAddress;
    private String mTelephone;
    private String mEmail;
    private String mUserLevel;
    private String mHLevel;
    private YKAvatar mAvatar;
    private String mCityId;
    private String mCity;
    private String mProvince;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmSkin() {
        return mSkin;
    }

    public void setmSkin(String mSkin) {
        this.mSkin = mSkin;
    }

    public String getmBrandLike() {
        return mBrandLike;
    }

    public void setmBrandLike(String mBrandLike) {
        this.mBrandLike = mBrandLike;
    }

    public String getmUserMoney() {
        return mUserMoney;
    }

    public void setmUserMoney(String mUserMoney) {
        this.mUserMoney = mUserMoney;
    }

    public String getmUserAge() {
        return mUserAge;
    }

    public void setmUserAge(String mUserAge) {
        this.mUserAge = mUserAge;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmTelephone() {
        return mTelephone;
    }

    public void setmTelephone(String mTelephone) {
        this.mTelephone = mTelephone;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmUserLevel() {
        return mUserLevel;
    }

    public void setmUserLevel(String mUserLevel) {
        this.mUserLevel = mUserLevel;
    }

    public YKAvatar getmAvatar() {
        return mAvatar;
    }

    public void setmAvatar(YKAvatar mAvatar) {
        this.mAvatar = mAvatar;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
    

    public String getmSex() {
		return mSex;
	}

	public void setmSex(String mSex) {
		this.mSex = mSex;
	}

	public String getmHLevel() {
        return mHLevel;
    }

    public void setmHLevel(String mHLevel) {
        this.mHLevel = mHLevel;
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

    public YKUpdateUserInfo(String mId, String mName, String mSkin,
            String mBrandLike, String mUserMoney, String mUserAge,
            String mAddress, String mTelephone, String mEmail,
            String mUserLevel, String mHLevel, YKAvatar mAvatar,
            String mCityId, String mCity, String mProvince,String mSex)
    {
        super();
        this.mId = mId;
        this.mName = mName;
        this.mSkin = mSkin;
        this.mBrandLike = mBrandLike;
        this.mUserMoney = mUserMoney;
        this.mUserAge = mUserAge;
        this.mAddress = mAddress;
        this.mTelephone = mTelephone;
        this.mEmail = mEmail;
        this.mUserLevel = mUserLevel;
        this.mHLevel = mHLevel;
        this.mAvatar = mAvatar;
        this.mCityId = mCityId;
        this.mCity = mCity;
        this.mProvince = mProvince;
        this.mSex = mSex;
    }

    public YKUpdateUserInfo()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public static YKUpdateUserInfo parse(JSONObject object) {
        YKUpdateUserInfo userInfo = new YKUpdateUserInfo();
        if (object != null) {
            userInfo.parseData(object);
        }
        return userInfo;
    }

    protected void parseData(JSONObject object) {
        try {
            mId = object.optString("uid");
        } catch (Exception e) {
        }
        try {
            mName = object.optString("username");
        } catch (Exception e) {
        }
        try {
            mHLevel = object.optString("levelname");
        } catch (Exception e) {
        }
        try {
            mSkin = object.optString("skin");
        } catch (Exception e) {
        }
        try {
            mBrandLike = object.optString("brandlike");
        } catch (Exception e) {
        }
        try {
            mUserMoney = object.optString("usermoney");
        } catch (Exception e) {
        }
        try {
            mUserAge = object.optString("userage");
        } catch (Exception e) {
        }
        try {
            mAddress = object.optString("address");
        } catch (Exception e) {
        }
        try {
            mTelephone = object.optString("telephone");
        } catch (Exception e) {
        }
        try {
            mEmail = object.optString("email");
        } catch (Exception e) {
        }
        try {
            mUserLevel = object.optString("user_level");
        } catch (Exception e) {
        }
        try {
            JSONObject tmpObject = object.getJSONObject("avatar");
            mAvatar = YKAvatar.parse(tmpObject);
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
        try {
            mSex = object.optString("sex");
        } catch (Exception e) {
        }
    }

   
}
