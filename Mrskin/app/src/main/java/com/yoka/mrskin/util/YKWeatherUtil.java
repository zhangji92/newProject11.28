package com.yoka.mrskin.util;

import com.yoka.mrskin.R;

public class YKWeatherUtil
{
    // 类型ID 类型名
    // -1 未知
    // 0 晴
    // 1 多云
    // 2 阴
    // 3 阵雨
    // 4 雷阵雨
    // 5 雷阵雨伴有冰雹
    // 6 雨夹雪
    // 7 小雨
    // 8 中雨
    // 9 大雨
    // 10 暴雨
    // 11 大暴雨
    // 12 特大暴雨
    // 13 阵雪
    // 14 小雪
    // 15 中雪
    // 16 大雪
    // 17 暴雪
    // 18 雾
    // 19 冻雨
    // 20 沙尘暴
    // 21 小到中雨
    // 22 中到大雨
    // 23 大到暴雨
    // 24 暴雨到大暴雨
    // 25 大暴雨到特大暴雨
    // 26 小到中雪
    // 27 中到大雪
    // 28 大到暴雪
    // 29 浮尘
    // 30 扬沙
    // 31 强沙尘暴
    // 32 霾
    public static String getWeatherTextByType(int type) {
        switch (type) {
        case 0:
            return "晴";
        case 1:
            return "多云";
        case 2:
            return "阴";
        case 3:
            return "阵雨";
        case 4:
            return "雷阵雨";
        case 5:
            return "雷阵雨伴有冰雹";
        case 6:
            return "雨夹雪";
        case 7:
            return "小雨";
        case 8:
            return "中雨";
        case 9:
            return "大雨";
        case 10:
            return "暴雨";
        case 11:
            return "大暴雨";
        case 12:
            return "特大暴雨";
        case 13:
            return "阵雪";
        case 14:
            return "小雪";
        case 15:
            return "中雪";
        case 16:
            return "大雪";
        case 17:
            return "暴雪";
        case 18:
            return "雾";
        case 19:
            return "冻雨";
        case 20:
            return "沙尘暴";
        case 21:
            return "小到中雨";
        case 22:
            return "中到大雨";
        case 23:
            return "大到暴雨";
        case 24:
            return "暴雨到大暴雨";
        case 25:
            return "大暴雨到特大暴雨";
        case 26:
            return "小到中雪";
        case 27:
            return "中到大雪";
        case 28:
            return "大到暴雪";
        case 29:
            return "浮尘";
        case 30:
            return "扬沙";
        case 31:
            return "强沙尘暴";
        case 32:
            return "霾";
        case -1:
            default:
            return "未知";
        }
    }

    public static int getWeatherPicResByType(int type) {
        switch (type) {
        case 0:
            return R.drawable.weather_34;
        case 1:
            return R.drawable.weather_24;
        case 2:
            return R.drawable.weather_23;
        case 3:
            return R.drawable.weather_33;
        case 4:
            return R.drawable.weather_22;
        case 5:
            return R.drawable.weather_21;
        case 6:
            return R.drawable.weather_20;
        case 7:
            return R.drawable.weather_10;
        case 8:
            return R.drawable.weather_27;
        case 9:
            return R.drawable.weather_09;
        case 10:
            return R.drawable.weather_16;
        case 11:
            return R.drawable.weather_17;
        case 12:
            return R.drawable.weather_18;
        case 13:
            return R.drawable.weather_30;
        case 14:
            return R.drawable.weather_07;
        case 15:
            return R.drawable.weather_06;
        case 16:
            return R.drawable.weather_05;
        case 17:
            return R.drawable.weather_29;
        case 18:
            return R.drawable.weather_12;
        case 19:
            return R.drawable.weather_28;
        case 20:
            return R.drawable.weather_11;
        case 21:
            return R.drawable.weather_32;
        case 22:
            return R.drawable.weather_31;
        case 23:
            return R.drawable.weather_19;
        case 24:
            return R.drawable.weather_26;
        case 25:
            return R.drawable.weather_08;
        case 26:
            return R.drawable.weather_15;
        case 27:
            return R.drawable.weather_14;
        case 28:
            return R.drawable.weather_13;
        case 29:
            return R.drawable.weather_04;
        case 30:
            return R.drawable.weather_03;
        case 31:
            return R.drawable.weather_02;
        case 32:
            return R.drawable.weather_25;
        case -1:
        default:
            return R.drawable.weather_01;
        }
    }

}
