package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import android.annotation.SuppressLint;

import com.yoka.mrskin.model.base.YKData;
import com.yoka.mrskin.util.PinyinUtil;

@SuppressWarnings("serial")
public class UHCity extends YKData {

    private static final String TAG_ID = "areaId";
    private static final String TAG_NAME = "name";
    private static final String TAG_CITY_NAME = "city_name";
    private static final String TAG_PROVINCE_NAME = "province_name";
    private String mCityName;
    private String mProvinceName;
    private String mCityNamePinyin;
    private String mCityNameCapitalPinyin;

    /**
     * @return the mCityNamePinyin
     */
    public String getCityNamePinyin() {
        return mCityNamePinyin;
    }

    /**
     * @param mCityNamePinyin
     *            the mCityNamePinyin to set
     */
    public void setCityNamePinyin(String cityNamePinyin) {
        this.mCityNamePinyin = cityNamePinyin;
    }

    /**
     * @return the cityName
     */
    public String getCityName() {
        if (mCityName == null)
            return "";
        return mCityName;
    }

    /**
     * @param cityName
     *            the cityName to set
     */
    public void setCityName(String cityName) {

        this.mCityName = cityName;
        if (mCityName != null) {
            mCityNamePinyin = PinyinUtil.makeStringByStringSet(PinyinUtil.getPinyin(mCityName));
            String strTmp;
            String tmpPinyin;
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < cityName.length(); ++i) {
                strTmp = mCityName.substring(i, i + 1);
                tmpPinyin = PinyinUtil.makeStringByStringSet(PinyinUtil.getPinyin(strTmp));
                if (tmpPinyin != null && tmpPinyin.length() > 0) {
                    buffer.append(tmpPinyin.charAt(0));
                }
            }
            mCityNameCapitalPinyin = buffer.toString();
        } else {
            mCityNamePinyin = "";
            mCityNameCapitalPinyin = "";
        }
    }

    public void setProvinceName(String provinceName) {
        mProvinceName = provinceName;
    }

    public String getProvinceName() {
        return mProvinceName;
    }

    @Override
    public String toString() {
        return mCityName + "," + getID() + "   " + mCityNamePinyin;
    }

    @SuppressLint("DefaultLocale")
    public boolean contains(String str) {
        if (str == null)
            return false;
        if (str.length() <= 0)
            return false;

        Boolean resultname = false;
        Boolean resultpinyin = false;
        Boolean resultCapitalpinyin = false;

        if (mCityName != null) {
            resultname = mCityName.contains(str.toLowerCase());
        }
        if (mCityNamePinyin != null) {
            resultpinyin = mCityNamePinyin.contains(str.toLowerCase());
        }
        if (mCityNameCapitalPinyin != null) {
            resultCapitalpinyin = mCityNameCapitalPinyin.contains(str.toLowerCase());
        }

        return resultpinyin || resultname || resultCapitalpinyin;
    }

    public void parseData(JSONObject object) {
        if (object == null) {
            return;
        }
        super.parseData(object);

        String tmpString;
        try {
            tmpString = object.getString(TAG_ID);
            if (tmpString != null) {
                setID(tmpString);
            }
        } catch (Exception e) {
        }
        try {
            tmpString = object.getString(TAG_CITY_NAME);
            if (tmpString != null) {
                setCityName(tmpString);
            }
        } catch (Exception e) {
        }
        try {
            tmpString = object.getString(TAG_NAME);
            if (tmpString != null) {
                setCityName(tmpString);
            }
        } catch (Exception e) {
        }
        try {
            tmpString = object.getString(TAG_PROVINCE_NAME);
            if (tmpString != null) {
                setProvinceName(tmpString);
            }
        } catch (Exception e) {
        }
    }

    public String toJsonString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        if (mCityName != null) {
            buffer.append("\"" + TAG_CITY_NAME + "\":\"");
            buffer.append(mCityName);
            buffer.append("\"");
            buffer.append(",");
        }

        buffer.append("\"" + TAG_ID + "\":\"");
        buffer.append(getID());
        buffer.append("\"}");
        return buffer.toString();
    }
}
