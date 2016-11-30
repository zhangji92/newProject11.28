package com.yoka.mrskin.model.managers.base;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;


public interface Callback
{
    public void doCallback(YKHttpTask task, JSONObject object, YKResult result);
}
