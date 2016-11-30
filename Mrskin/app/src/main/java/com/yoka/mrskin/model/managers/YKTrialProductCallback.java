package com.yoka.mrskin.model.managers;

import java.util.ArrayList;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTrialProduct;

public interface YKTrialProductCallback
{
    public void callback(YKResult result, ArrayList<YKTrialProduct> products);
}
