package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

/**
 * 版本信息更新
 * 
 * @author Y H L
 * 
 */
@SuppressWarnings("serial")
public class YKVersionInfo extends YKData
{

    /**
     * 版本名称
     */
    private String mVersionName;

    /**
     * 新版本下载地址
     */
    private String mURL;

    /**
     * 版本信息
     */
    private String mVersionInfo;
    /**
     * 内容
     */
    private String mMessage;

    /**
     * 版本号
     */
    private int mVersionCode;

    public String getmCtime() {
        return mMessage;
    }

    public void setmCtime(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmVersion() {
        return mVersionName;
    }

    public void setmVersion(String mVersion) {
        this.mVersionName = mVersion;
    }

    public String getmURL() {
        return mURL;
    }

    public void setmURL(String mURL) {
        this.mURL = mURL;
    }

    public String getmVersionInfo() {
        return mVersionInfo;
    }

    public void setmVersionInfo(String mVersionInfo) {
        this.mVersionInfo = mVersionInfo;
    }

    public int getmVersionCode() {
        return mVersionCode;
    }

    public void setmVersionCode(int mVersionCode) {
        this.mVersionCode = mVersionCode;
    }

    public static YKVersionInfo parse(JSONObject object) {
        YKVersionInfo topic = new YKVersionInfo();
        if (object != null) {
            topic.parseData(object);
        }
        return topic;
    }

    protected void parseData(JSONObject object) {

        super.parseData(object);
        
        try {
            mVersionName = object.getString("version_name");
        } catch (JSONException e) {
        }

        try {
            mURL = object.getString("url");
        } catch (JSONException e) {
        }
        try {
            mVersionInfo = object.getString("version_info");
        } catch (JSONException e) {
        }
        try {
            mVersionCode = object.getInt("version_code");
        } catch (JSONException e) {
        }
        try {
            mMessage = object.getString("version_info_array");
        } catch (JSONException e) {
        }
    }
}
