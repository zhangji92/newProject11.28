package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKApplyTime extends YKData
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int second;
    private int minute;
    private long time;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public YKApplyTime(int year, int month, int day, int hour, int second,
            int minute, long time)
    {
        super();
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.second = second;
        this.minute = minute;
        this.time = time;
    }

    public YKApplyTime()
    {
        super();
    }

    public static YKApplyTime parse(JSONObject object) {
        YKApplyTime applyTime = new YKApplyTime();
        if (object != null) {
            applyTime.parseData(object);
        }
        return applyTime;
    }

    @Override
    protected void parseData(JSONObject object) {
        try {
            year = object.optInt("year");
        } catch (Exception e) {
        }
        try {
            month = object.optInt("month");
        } catch (Exception e) {
        }
        try {
            day = object.optInt("day");
        } catch (Exception e) {
        }
        try {
            hour = object.optInt("hour");
        } catch (Exception e) {
        }
        try {
            minute = object.optInt("min");
        } catch (Exception e) {
        }
        try {
            second = object.optInt("sec");
        } catch (Exception e) {
        }
        try {
            time = object.getLong("long_time");
        } catch (Exception e) {
        }
    }
}
