package com.yoka.mrskin.model.data;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * 时间对象
 * @author Y H L
 */
public class YKDate implements Serializable
{
    private static final long serialVersionUID = -3785693206227396910L;
    /**
     * 年
     */
    private int mYear;
    /**
     * 月
     */
    private int mMonth;
    /**
     * 日
     */
    private int mDay;
    /**
     * 时
     */
    private int mHour;
    /**
     * 分
     */
    private int mMin;
    /**
     * 秒
     */
    private int mSec;
    /**
     * 1970时间值
     */
    private long mLongtime;

    public int getmYear() {
        return mYear;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public int getmMonth() {
        return mMonth;
    }

    public void setmMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public int getmDay() {
        return mDay;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    public int getmHour() {
        return mHour;
    }

    public void setmHour(int mHour) {
        this.mHour = mHour;
    }

    public int getmMin() {
        return mMin;
    }

    public void setmMin(int mMin) {
        this.mMin = mMin;
    }

    public int getmSec() {
        return mSec;
    }

    public void setmSec(int mSec) {
        this.mSec = mSec;
    }

    public long getmMills() {
        return mLongtime;
    }

    public void setmMills(long mMills) {
        this.mLongtime = mMills;
    }

    public static YKDate parse(JSONObject object) {
        YKDate date = new YKDate();
        if (object != null) {
            date.parseData(object);
        }
        return date;
    }

    protected void parseData(JSONObject object) {

        try {
            mYear = object.optInt("year");
        } catch (Exception e) {
        }
        try {
            mMonth = object.optInt("month");
        } catch (Exception e) {
        }
        try {
            mDay = object.optInt("day");
        } catch (Exception e) {
        }
        try {
            mHour = object.optInt("hour");
        } catch (Exception e) {
        }
        try {
            mMin = object.optInt("min");
        } catch (Exception e) {
        }
        try {
            mSec = object.optInt("sec");
        } catch (Exception e) {
        }
        try {
            mLongtime = object.optLong("long_time");
        } catch (Exception e) {
        }
    }

    public YKDate()
    {
        mYear = Integer.valueOf(0);
        mMonth = Integer.valueOf(0);
        mDay = Integer.valueOf(0);
        mHour = Integer.valueOf(0);
        mMin = Integer.valueOf(0);
        mSec = Integer.valueOf(0);
        mLongtime = Integer.valueOf(0);
    }

    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("year", mYear);
        } catch (Exception e) {}
        try {
            object.put("month", mMonth);
        } catch (Exception e) {}
        try {
            object.put("day", mDay);
        } catch (Exception e) {}
        try {
            object.put("hour", mHour);
        } catch (Exception e) {}
        try {
            object.put("min", mMin);
        } catch (Exception e) {}
        try {
            object.put("sec", mSec);
        } catch (Exception e) {}
        try {
            object.put("long_time", mLongtime);
        } catch (Exception e) {}
        return object;
    }
}
