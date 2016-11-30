package com.yoka.mrskin.model.managers;

import java.util.ArrayList;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKFocusImage;


public interface YKNewFocusImageCallback
{
    public void callback(YKResult result,ArrayList<YKFocusImage> focusImage);

}
