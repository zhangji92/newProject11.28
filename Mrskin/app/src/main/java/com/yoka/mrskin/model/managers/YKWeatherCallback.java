package com.yoka.mrskin.model.managers;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKWeatherData;


public interface YKWeatherCallback
{
    public void callback(YKResult result,YKWeatherData weather);

}
