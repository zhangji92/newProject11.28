package com.yoka.mrskin.model.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yoka.mrskin.model.base.YKData;

public class UHCurrentCity extends YKData {
    private static final String TAG = "UHCurrentCity";

    private static final String TAG_CITY = "city";
    private static final String TAG_WEATHER = "weather";
    private static final String TAG_TODAY = "today";
    private static final String TAG_7_DAYS = "days";
    private static final String TAG_INDEX = "indexs";

    private UHCity mCity;
    private UHWeather mWeather;
    private ArrayList<UHWeather> m7DaysWeahter;
    private ArrayList<UHWeather> mTodayWeahter;
    private String mUpdateTime;
    private ArrayList<UHWeatherIndex> mWeatherIndexs;

    public UHCurrentCity() {
        Log.d(TAG, "constructor");
        mWeather = new UHWeather();
    }

    /**
     * @return the mWeather
     */
    public UHWeather getWeather() {
        return mWeather;
    }

    /**
     * @param mWeather
     *            the mWeather to set
     */
    public void setWeather(UHWeather mWeather) {
        this.mWeather = mWeather;

//        mTodayWeahter = new ArrayList<UHWeather>();
//        UHWeather weather;
//        int high = 0, low = 0;
//        try {
//            high = Integer.parseInt(mWeather.getHighestTemperature());
//        } catch (Exception e) {
//
//        }
//        try {
//            low = Integer.parseInt(mWeather.getLowestTemperature());
//        } catch (Exception e) {
//        }
//        int temperature = 0;
//        for (int i = 0; i < 24; ++i) {
//            temperature = low + (i * (high - low) / 24);
//            weather = new UHWeather();
//            weather.setWeatherCode(mWeather.getWeatherCode());
//            weather.setCurrentTemperature(temperature + "");
//            mTodayWeahter.add(weather);
//        }
    }

    /**
     * @return the mTodayWeahter
     */
    public ArrayList<UHWeather> getTodayWeahter() {
        return mTodayWeahter;
    }

    /**
     * @param mTodayWeahter
     *            the mTodayWeahter to set
     */
    public void setTodayWeahter(ArrayList<UHWeather> mTodayWeahter) {
         this.mTodayWeahter = mTodayWeahter;
    }

    /**
     * @return the m7DaysWeahter
     */
    public ArrayList<UHWeather> get7DaysWeahter() {
        return m7DaysWeahter;
    }

    /**
     * @param m7DaysWeahter
     *            the m7DaysWeahter to set
     */
    public void set7DaysWeahter(ArrayList<UHWeather> m7DaysWeahter) {
        this.m7DaysWeahter = m7DaysWeahter;
    }

    /**
     * @return the city
     */
    public UHCity getCity() {
        return mCity;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(UHCity city) {
        this.mCity = city;
    }

    /**
     * @return the updateTime
     */
    public String getUpdateTime() {
        return mUpdateTime;
    }

    /**
     * @param updateTime
     *            the updateTime to set
     */
    public void setUpdateTime(String updateTime) {
        mUpdateTime = updateTime;
    }

    public ArrayList<UHWeatherIndex> getWeatherIndexs() {
        return mWeatherIndexs;
    }

    public void setWeatherIndexs(ArrayList<UHWeatherIndex> weatherIndexs) {
        this.mWeatherIndexs = weatherIndexs;
    }

    public void parseData(JSONObject object) {
        JSONObject cityObject;
        try {
            cityObject = object.getJSONObject(TAG_CITY);
            mCity = new UHCity();
            mCity.parseData(cityObject);
        } catch (JSONException e) {
        }

        JSONObject weatherObject;
        try {
            weatherObject = object.getJSONObject(TAG_WEATHER);
            mWeather = new UHWeather();
            mWeather.parseData(weatherObject);
        } catch (JSONException e) {
        }

        JSONArray daysArray;
        try {
            m7DaysWeahter = new ArrayList<UHWeather>();
            daysArray = object.getJSONArray(TAG_7_DAYS);
            UHWeather weather;
            for (int i = 0; i < daysArray.length(); ++i) {
                weather = new UHWeather();
                weather.parseData((JSONObject) (daysArray.get(i)));
                m7DaysWeahter.add(weather);
            }
        } catch (JSONException e) {
        }

        try {
            mWeatherIndexs = new ArrayList<UHWeatherIndex>();
            daysArray = object.getJSONArray(TAG_INDEX);
            UHWeatherIndex weatherIndex;
            for (int i = 0; i < daysArray.length(); ++i) {
                weatherIndex = new UHWeatherIndex();
                weatherIndex.parseData((JSONObject) (daysArray.get(i)));
                mWeatherIndexs.add(weatherIndex);
            }
        } catch (JSONException e) {
        }
        
        try {
            mTodayWeahter = new ArrayList<UHWeather>();
            daysArray = object.getJSONArray(TAG_TODAY);
            UHWeather weather;
            for (int i = 0; i < daysArray.length(); ++i) {
                weather = new UHWeather();
                weather.parseData( (JSONObject) (daysArray.get(i)));
                mTodayWeahter.add(weather);
            }
        } catch (JSONException e) {
        }
    }

    public String toJsonString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        if (mCity != null) {
            buffer.append("\"" + TAG_CITY + "\":");
            buffer.append(mCity.toJsonString());
        }

        if (mWeather != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_WEATHER + "\":");
            buffer.append(mWeather.toJsonString());
        }

        UHWeather weather;
        if (mTodayWeahter != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_TODAY + "\":[");

            for (int i = 0; i < mTodayWeahter.size(); ++i) {
                if (i > 0) {
                    buffer.append(",");
                }
                weather = mTodayWeahter.get(i);
                buffer.append(weather.toJsonString());
            }
            buffer.append("]");
        }

        if (m7DaysWeahter != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            buffer.append("\"" + TAG_7_DAYS + "\":[");
            for (int i = 0; i < m7DaysWeahter.size(); ++i) {
                if (i > 0) {
                    buffer.append(",");
                }
                weather = m7DaysWeahter.get(i);
                buffer.append(weather.toJsonString());

            }
            buffer.append("]");
        }

        if (mWeatherIndexs != null) {
            if (buffer.length() > 1) {
                buffer.append(",");
            }
            UHWeatherIndex index;
            buffer.append("\"" + TAG_INDEX + "\":[");
            for (int i = 0; i < mWeatherIndexs.size(); ++i) {
                if (i > 0) {
                    buffer.append(",");
                }
                index = mWeatherIndexs.get(i);
                buffer.append(index.toJsonString());
            }
            buffer.append("]");
        }

        buffer.append("}");
        Log.d(TAG, "save result " + buffer.toString());
        return buffer.toString();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (mCity != null) {
            buffer.append("city =" + mCity.toString());
        }
        if (mWeather != null) {
            buffer.append("\nweather =" + mWeather.toString());
        }

        if (m7DaysWeahter != null) {
            for (int i = 0; i < m7DaysWeahter.size(); ++i) {
                buffer.append("\nm7DaysWeahter[" + i + "]=" + m7DaysWeahter.get(i).toString());
            }
        }
        if (mUpdateTime != null) {
            buffer.append("\n update time = " + mUpdateTime);
        }

        return buffer.toString();
    }
}
