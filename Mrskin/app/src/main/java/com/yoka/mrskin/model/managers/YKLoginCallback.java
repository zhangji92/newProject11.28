package com.yoka.mrskin.model.managers;

import com.yoka.mrskin.login.YKUser;
import com.yoka.mrskin.model.base.YKResult;

public interface YKLoginCallback
{
    public void callback(YKResult result, YKUser user);

    public void callbackFaile(String message);
}
