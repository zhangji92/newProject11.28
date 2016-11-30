package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKWeatherData extends YKData
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 紫外线
     */
    private String ultraviolet;
    /**
     * 紫外线点击跳转地址
     */
    private String mVioletlineUrl;
    /**
     * 美丽指数
     */
    private String mScore;
    /**
     * 湿度
     */
    private String mHumidity;
    /**
     * 湿度跳转地址
     */
    private String mHumidityUrl;
    /**
     * 温度
     */
    private String mTemperature;
    /**
     * 温度跳转地址
     */
    private String mTemperatureUrl;
    /**
     * PM2.5
     */
    private String pm;
    /**
     * PM2.5跳转地址
     */
    private String pm25Url;
    /**
     * 天气类型
     */
    private int mWeathertype;

    public String getultraviolet() {
        return ultraviolet;
    }

    public void setUltraviolet(String ultraviolet) {
        this.ultraviolet = ultraviolet;
    }

    public String getmVioletlineUrl() {
        return mVioletlineUrl;
    }

    public void setmVioletlineUrl(String mVioletlineUrl) {
        this.mVioletlineUrl = mVioletlineUrl;
    }

    public String getmScore() {
        return mScore;
    }

    public void setmScore(String mScore) {
        this.mScore = mScore;
    }

    public String getmHumidity() {
        return mHumidity;
    }

    public void setmHumidity(String mHumidity) {
        this.mHumidity = mHumidity;
    }

    public String getmHumidityUrl() {
        return mHumidityUrl;
    }

    public void setmHumidityUrl(String mHumidityUrl) {
        this.mHumidityUrl = mHumidityUrl;
    }

    public String getmTemperature() {
        return mTemperature;
    }

    public void setmTemperature(String mTemperature) {
        this.mTemperature = mTemperature;
    }

    public String getmTemperatureUrl() {
        return mTemperatureUrl;
    }

    public void setmTemperatureUrl(String mTemperatureUrl) {
        this.mTemperatureUrl = mTemperatureUrl;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getPm25Url() {
        return pm25Url;
    }

    public void setPm25Url(String pm25Url) {
        this.pm25Url = pm25Url;
    }

    public int getmWeathertype() {
        return mWeathertype;
    }

    public void setmWeathertype(int mWeathertype) {
        this.mWeathertype = mWeathertype;
    }

    public YKWeatherData(String ultraviolet, String mVioletlineUrl,
            String mScore, String mHumidity, String mHumidityUrl,
            String mTemperature, String mTemperatureUrl, String pm,
            String pm25Url, int mWeathertype)
    {
        super();
        this.ultraviolet = ultraviolet;
        this.mVioletlineUrl = mVioletlineUrl;
        this.mScore = mScore;
        this.mHumidity = mHumidity;
        this.mHumidityUrl = mHumidityUrl;
        this.mTemperature = mTemperature;
        this.mTemperatureUrl = mTemperatureUrl;
        this.pm = pm;
        this.pm25Url = pm25Url;
        this.mWeathertype = mWeathertype;
    }

    public YKWeatherData()
    {
        super();
    }

    public static YKWeatherData parse(JSONObject object) {
        YKWeatherData weather = new YKWeatherData();
        if (object != null) {
            weather.parseData(object);
        }
        return weather;
    }

    protected void parseData(JSONObject object) {
        
        super.parseData(object);

        try {
            ultraviolet = object.getString("violetline");
        } catch (JSONException e) {
        }
        try {
            mVioletlineUrl = object.getString("violetline_url");
        } catch (JSONException e) {
        }
        try {
            mScore = object.getString("score");
        } catch (JSONException e) {
        }
        try {
            mHumidity = object.getString("humidity");
        } catch (JSONException e) {
        }
        try {
            mHumidityUrl = object.getString("humidity_url");
        } catch (JSONException e) {
        }
        try {
            mTemperature = object.getString("temperature");
        } catch (JSONException e) {
        }
        try {
            mTemperatureUrl = object.getString("temperature_url");
        } catch (JSONException e) {
        }
        try {
            pm = object.getString("pm25");
        } catch (JSONException e) {
        }
        try {
            pm25Url = object.getString("pm25_url");
        } catch (JSONException e) {
        }
        try {
            mWeathertype = object.optInt("weather_type");
        } catch (Exception e) {
        }
    }

}
