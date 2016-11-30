package com.yoka.mrskin.model.managers.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class TimeUtil
{
    // today zero
    public static long getTodayZero() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.set(Calendar.DAY_OF_YEAR, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (calendar.getTimeInMillis());
    }

    // next day zero
    public static long getNextDayZero() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);//一天后的日期
        return (cal.getTimeInMillis());
    }

    public static boolean isToday(long time) {
        long todayZero = TimeUtil.getTodayZero();
        long nextDayZero = TimeUtil.getNextDayZero();
        if (time > todayZero && time < nextDayZero) {
            return true;
        }
        return false;
    }

    public static boolean isBefor(long time) {
        // 允许前一小时的任务出现
        long now = System.currentTimeMillis();
        // return time < now - 30 * 60 * 1000;
        return time < now;
    }

    public static int timeForHour(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        return Integer.parseInt(sdf.format((time)));
    }

    public static int timeForMinute(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        return Integer.parseInt(sdf.format((time)));
    }

    public static String forTimeForHour(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        return sdf.format((time));
    }

    public static String forTimeForMinute(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        return sdf.format((time));
    }

    public static String forTimeForHourAndSeconed(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format((time));
    }

    public static String forTimeForYearMonthDay(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format((time));
    }
    //发布心得转换
    public static String forTimeForYearMonthDays(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format((time));
    }
    public static String forTimeForYearMonthDayHour(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return sdf.format((time));
    }

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new java.util.Date());
    }
    public static String forTimeForYearMonthDayShorthand(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format((time*1000));
    }
    public static String forTimeDay(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format((time*1000));
    }
    public static String forTimeForYearMonthDayShorthandNew(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format((time*1000));
    }
    public static String forTimeForYearMonthDayShortDayHour(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format((time*1000));
    }
}
