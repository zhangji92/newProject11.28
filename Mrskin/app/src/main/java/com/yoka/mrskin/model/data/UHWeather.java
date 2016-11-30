package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import android.util.Log;

import com.yoka.mrskin.model.base.YKData;

public class UHWeather extends YKData {

    private static final String TAG = "UHWeather";

    private static final String TAG_WEATHER = "weather";
    private static final String TAG_DAY_WEATHER = "day_weather";

    private static final String TAG_HUMIDITY = "humidy";
    private static final String TAG_TIME = "time";
    private static final String TAG_DATE = "date";
    private static final String TAG_WIND = "wind";
    private static final String TAG_WIND_DIRECTION = "wind_direction";
    

    private static final String TAG_HIGH_TEMP = "day_temp";
    private static final String TAG_LOW_TEMP = "night_temp";
    private static final String TAG_TEMPERATURE = "temperature";
    private static final String TAG_FEEL_TEMPERATURE = "feel_temp";
    private static final String TAG_PM25STRING = "pm25";

    private static final String TAG_DAY_WIND = "day_wind";
    private static final String TAG_DAY_WIND_DIRECTION = "day_wind_direction";

    private String mWeatherCode;
    private String mWeatherName;

    private String mHighestTemperature;
    private String mLowestTemperature;
    private String mCurrentTemperature;
    private String mFeelTemperature;

    private String mTime;

    private String mPM25;
    private String mPM25String;
    private String mHumidity;
    
    private String mWind;
    private String mWindDirection;

    private String mDayWind;
    private String mDayWindDirection;
    
    public String getWind() {
        return mWind;
    }

    public void setWind(String wind) {
        mWind = wind;
    }

    public String getWindDirection() {
        return mWindDirection;
    }

    public void setWindDirection(String windDirection) {
        mWindDirection = windDirection;
    }

    public UHWeather() {
        Log.d(TAG, "constructor");
    }

    /**
     * @return the weatherCode
     */
    public String getWeatherCode() {
        return mWeatherCode;
    }

    /**
     * @param weatherCode
     *            the weatherCode to set
     */
    public void setWeatherCode(String weatherCode) {
        mWeatherCode = weatherCode;
    }

    /**
     * @return the highestTemperature
     */
    public String getHighestTemperature() {
        return mHighestTemperature;
    }

    /**
     * @param highestTemperature
     *            the highestTemperature to set
     */
    public void setHighestTemperature(String highestTemperature) {
        mHighestTemperature = highestTemperature;
    }

    /**
     * @return the mWeatherName
     */
    public String getWeatherName() {
        return mWeatherName;
    }

    /**
     * @param mWeatherName
     *            the mWeatherName to set
     */
    public void setWeatherName(String weatherName) {
        this.mWeatherName = weatherName;
    }

    /**
     * @return the lowestTemperature
     */
    public String getLowestTemperature() {
        return mLowestTemperature;
    }

    /**
     * @param lowestTemperature
     *            the lowestTemperature to set
     */
    public void setLowestTemperature(String lowestTemperature) {
        mLowestTemperature = lowestTemperature;
    }

    /**
     * @return the mPM25
     */
    public String getPM25() {
        if (mPM25String == null)
            return "";
        return mPM25String;
    }
    
    public int getPM25Value() {
        int count = 0;
        try {
            count = Integer.parseInt(mPM25);
        } catch (Exception e) {
        }
        return count;
    }

    /**
     * @param mPM25
     *            the mPM25 to set
     */
    public void setPM25(String pm25) {
        int count = 0;
        try {
            count = Integer.parseInt(pm25);
        } catch (Exception e) {
        }

        String description = "";
        if (count < 50) {
            description = "优良";
        } else if (count < 100 && count >= 50) {
            description = "良好";
        }  else if (count < 150 && count >= 100) {
            description = "轻度污染";
        }  else if (count < 200 && count >= 150) {
            description = "中度污染";
        }  else if (count < 300 && count >= 200) {
            description = "重度污染";
        }  else {
            description = "严重污染";
        }

        this.mPM25 = pm25;
        if (mPM25 == null) {
            mPM25 = "";
        }
        mPM25String = description+" "+mPM25;
    }

    /**
     * @return the mHumidity
     */
    public String getHumidity() {
        return mHumidity;
    }

    /**
     * @param mHumidity
     *            the mHumidity to set
     */
    public void setHumidity(String humidity) {
        this.mHumidity = humidity;
    }

    /**
     * @return the mCurrentTemperature
     */
    public String getCurrentTemperature() {
        return mCurrentTemperature;
    }

    /**
     * @param mCurrentTemperature
     *            the mCurrentTemperature to set
     */
    public void setCurrentTemperature(String mCurrentTemperature) {
        this.mCurrentTemperature = mCurrentTemperature;
    }

    /**
     * @return the mTime
     */
    public String getTime() {
        if (mTime == null)
            return "";
        return mTime;
    }

    /**
     * @param mTime
     *            the mTime to set
     */
    public void setTime(String time) {
        mTime = time;
    }
    
    public String getFeelTemperature() {
        return mFeelTemperature;
    }

    public void setFeelTemperature(String feelTemperature) {
        this.mFeelTemperature = feelTemperature;
    }

    @Override
    public String toString() {
        return "weather Code = " + mWeatherCode + ",  day_temp = " + mHighestTemperature + ", night_temp = "
                + mLowestTemperature + ", current_temp = " + mCurrentTemperature + ", PM2.5 = " + mPM25 + "   "
                + ", humidity = " + mHumidity + ", time = " + mTime + ", daywind = " + mDayWind + ", mdaywinddirection = " + mDayWindDirection;
    }

    public void parseData(JSONObject object) {
        if (object == null) {
            return;
        }
        super.parseData(object);
        String tmpString;
        try {
            tmpString = object.getString(TAG_WEATHER);
            if (tmpString != null) {
                mWeatherCode = tmpString;
            }
        } catch (Exception e) {
        }

        try {
            tmpString = object.getString(TAG_WEATHER);
            if (tmpString != null) {
                mWeatherName = tmpString;
            }
        } catch (Exception e) {
        }

        try {
            tmpString = object.getString(TAG_DAY_WEATHER);
            if (tmpString != null) {
                mWeatherCode = tmpString;
            }
        } catch (Exception e) {
        }

        try {
            tmpString = object.getString(TAG_DAY_WEATHER);
            if (tmpString != null) {
                mWeatherName = tmpString;
            }
        } catch (Exception e) {
        }

        try {
            tmpString = object.getString(TAG_TIME);
            if (tmpString != null) {
                mTime = tmpString;
            }
        } catch (Exception e) {
        }
        try {
            tmpString = object.getString(TAG_DATE);
            if (tmpString != null) {
                mTime = tmpString;
            }
        } catch (Exception e) {
        }

        try {
            tmpString = object.getString(TAG_HUMIDITY);
            if (tmpString != null) {
                mHumidity = tmpString;
            }
        } catch (Exception e) {

        }
        try {
            tmpString = object.getString(TAG_HIGH_TEMP);
            if (tmpString != null && tmpString.length() > 0) {
                if (tmpString.equals("null")) {
                    tmpString = "";
                }
                mHighestTemperature = tmpString;
            }
        } catch (Exception e) {
        }
        try {
            tmpString = object.getString(TAG_LOW_TEMP);
            if (tmpString != null) {
                if (tmpString.equals("null")) {
                    tmpString = "";
                }
                mLowestTemperature = tmpString;
            }
        } catch (Exception e) {

        }
        try {
            tmpString = object.getString(TAG_TEMPERATURE);
            if (tmpString != null) {
                mCurrentTemperature = tmpString;
            }
        } catch (Exception e) {
        }
        try {
            tmpString = object.getString(TAG_PM25STRING);
            if (tmpString != null) {
               setPM25(tmpString);
            }
        } catch (Exception e) {
        }
        
        try {
            tmpString = object.getString(TAG_WIND);
            if (tmpString != null) {
               setWind(tmpString);
            }
        } catch (Exception e) {
        }

        try {
            tmpString = object.getString(TAG_WIND_DIRECTION);
            if (tmpString != null) {
               setWindDirection(tmpString);
            }
        } catch (Exception e) {
        }
        try {
            tmpString = object.getString(TAG_FEEL_TEMPERATURE);
            if (tmpString != null) {
                setFeelTemperature(tmpString);
            }
        } catch (Exception e) {
        }
        try {
            tmpString = object.getString(TAG_DAY_WIND);
            if (tmpString != null) {
                setDayWind(tmpString);
            }
        } catch (Exception e) {
        }
        try {
            tmpString = object.getString(TAG_DAY_WIND_DIRECTION);
            if (tmpString != null) {
                setDayWindDirection(tmpString);
            }
        } catch (Exception e) {
        }
    }

    public String toJsonString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        if (mWeatherCode != null) {
            buffer.append("\"" + TAG_WEATHER + "\":");
            buffer.append("\"");
            buffer.append(mWeatherCode);
            buffer.append("\"");
        }
        if (mPM25String != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_PM25STRING + "\":");
            buffer.append("\"");
            buffer.append(mPM25);
            buffer.append("\"");
        }
        if (mHighestTemperature != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_HIGH_TEMP + "\":");
            buffer.append("\"");
            buffer.append(mHighestTemperature);
            buffer.append("\"");
        }
        if (mLowestTemperature != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_LOW_TEMP + "\":");
            buffer.append("\"");
            buffer.append(mLowestTemperature);
            buffer.append("\"");
        }
        if (mTime != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_TIME + "\":");
            buffer.append("\"");
            buffer.append(mTime);
            buffer.append("\"");
        }
        if (mHumidity != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_HUMIDITY + "\":");
            buffer.append("\"");
            buffer.append(mHumidity);
            buffer.append("\"");
        }
        if (mCurrentTemperature != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_TEMPERATURE + "\":");
            buffer.append("\"");
            buffer.append(mCurrentTemperature);
            buffer.append("\"");
        }
        if (mWind != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_WIND + "\":");
            buffer.append("\"");
            buffer.append(mWind);
            buffer.append("\"");
        }
        if (mWindDirection != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_WIND_DIRECTION + "\":");
            buffer.append("\"");
            buffer.append(mWindDirection);
            buffer.append("\"");
        }
        if (mFeelTemperature != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_FEEL_TEMPERATURE + "\":");
            buffer.append("\"");
            buffer.append(mFeelTemperature);
            buffer.append("\"");
        }
        if (mDayWind != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_DAY_WIND + "\":");
            buffer.append("\"");
            buffer.append(mDayWind);
            buffer.append("\"");
        }
        if (mDayWindDirection != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_DAY_WIND_DIRECTION + "\":");
            buffer.append("\"");
            buffer.append(mDayWindDirection);
            buffer.append("\"");
        }
        buffer.append("}");
        return buffer.toString();
    }

    public String getDayWind() {
        return mDayWind;
    }

    public void setDayWind(String mDayWind) {
        this.mDayWind = mDayWind;
    }

    public String getDayWindDirection() {
        return mDayWindDirection;
    }

    public void setDayWindDirection(String mDayWindDirection) {
        this.mDayWindDirection = mDayWindDirection;
    }
}
