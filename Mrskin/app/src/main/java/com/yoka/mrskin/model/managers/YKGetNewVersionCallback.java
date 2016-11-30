package com.yoka.mrskin.model.managers;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKVersionInfo;


public interface YKGetNewVersionCallback
{
    public void callback(YKResult result, YKVersionInfo versionInfo);
}
