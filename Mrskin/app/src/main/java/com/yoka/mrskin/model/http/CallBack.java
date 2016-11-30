package com.yoka.mrskin.model.http;

import java.util.HashMap;

import com.yoka.mrskin.model.base.YKResult;

/**
 * 回调接口
 * 
 * @author Y H L
 */
public interface CallBack
{
    /**
     * @param bt
     *            返回的内容
     * @param result
     *            code,message
     */
    public void success(YKHttpTask task, byte[] bt,
            HashMap<String, String> headers, YKResult result);
}