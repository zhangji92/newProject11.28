package com.yoka.mrskin.model.managers;

import java.util.ArrayList;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTask;

public interface YKLoadMoreCallback
{
    public void callback(YKResult result, ArrayList<YKTask> task);

}
