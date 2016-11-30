package com.yoka.mrskin.login;

import java.io.Serializable;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

/**
 * 头像地址对象
 * 
 * @author Jack
 * 
 */
@SuppressWarnings("serial")
public class YKAvatar extends YKData implements Serializable
{
    private String mHeadUrl;

    public String getmHeadUrl() {
        return mHeadUrl;
    }

    public void setmHeadUrl(String mHeadUrl) {
        this.mHeadUrl = mHeadUrl;
    }

    public YKAvatar()
    {
        mHeadUrl = "";
    }

    public static YKAvatar parse(JSONObject object) {
        YKAvatar avater = new YKAvatar();
        if (avater != null) {
            avater.parseData(object);
        }
        return avater;
    }

    @Override
    protected void parseData(JSONObject object) {
        super.parseData(object);

        try {
            mHeadUrl = object.optString("url");
        } catch (Exception e) {
        }
    }

    @Override
    public String toString() {
        return "YKAvatar [mHeadUrl=" + mHeadUrl + "]";
    }
}
