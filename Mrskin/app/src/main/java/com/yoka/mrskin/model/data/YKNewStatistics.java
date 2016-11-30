package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKNewStatistics extends YKData {


    /**
     * 打点统计
     */
    private static final long serialVersionUID = -6101215936862184936L;

    private String mClient_id;

    private String mUser_id;

    private String mDevice;

    private String mOs;

    private String mVendor;

    private String mVersion;

    private String mUrl;

    private String mWebUrl;

    public String getmClient_id()
    {
        return mClient_id;
    }

    public void setmClient_id(String mClient_id)
    {
        this.mClient_id = mClient_id;
    }

    public String getmUser_id()
    {
        return mUser_id;
    }

    public void setmUser_id(String mUser_id)
    {
        this.mUser_id = mUser_id;
    }

    public String getmDevice()
    {
        return mDevice;
    }

    public void setmDevice(String mDevice)
    {
        this.mDevice = mDevice;
    }

    public String getmOs()
    {
        return mOs;
    }

    public void setmOs(String mOs)
    {
        this.mOs = mOs;
    }

    public String getmVendor()
    {
        return mVendor;
    }

    public void setmVendor(String mVendor)
    {
        this.mVendor = mVendor;
    }

    public String getmVersion()
    {
        return mVersion;
    }

    public void setmVersion(String mVersion)
    {
        this.mVersion = mVersion;
    }

    public String getmUrl()
    {
        return mUrl;
    }

    public void setmUrl(String mUrl)
    {
        this.mUrl = mUrl;
    }

    public String getmWebUrl()
    {
        return mWebUrl;
    }

    public void setmWebUrl(String mWebUrl)
    {
        this.mWebUrl = mWebUrl;
    }

    public YKNewStatistics(String mClient_id, String mUser_id, String mDevice,
            String mOs, String mVendor, String mVersion, String mUrl
             , String mWebUrl)
    {
        super();
        this.mClient_id = mClient_id;
        this.mUser_id = mUser_id;
        this.mDevice = mDevice;
        this.mOs = mOs;
        this.mVendor = mVendor;
        this.mVersion = mVersion;
        this.mUrl = mUrl;
        this.mWebUrl = mWebUrl;
    }

    public YKNewStatistics()
    {
        super();
    }

    public static YKNewStatistics parse(JSONObject object) {
        YKNewStatistics topic = new YKNewStatistics();
        if (object != null) {
            topic.parseData(object);
        }
        return topic;
    }

    protected void parseData(JSONObject object) {

        super.parseData(object);

        try {
            mClient_id = object.getString("client_id");
        } catch (JSONException e) {
        }

        try {
            mDevice = object.getString("device");
        } catch (JSONException e) {
        }

        try {
            mOs = object.getString("os");
        } catch (JSONException e) {
        }
        
        try {
            mVendor = object.getString("vendor");
        } catch (JSONException e) {
        }
        
        try {
            mVersion = object.getString("version");
        } catch (JSONException e) {
        }
        
        try {
            mUrl = object.getString("url");
        } catch (JSONException e) {
        }
        
        try {
           mWebUrl = object.getString("webUrl");
        } catch (JSONException e) {
        }
        
    }

    public JSONObject toJsonObject() {
        JSONObject object =new JSONObject();
        if (mClient_id != null) {
            try {
                object.put("client_id", mClient_id);
            } catch (Exception e ){}
        }
        
        if (mUser_id != null) {
            try {
                object.put("user_id", mUser_id);
            } catch (Exception e ){}
        }
        
        if (mDevice != null) {
            try {
                object.put("device", mDevice);
            } catch (Exception e ){}
        }
        
        if (mOs != null) {
            try {
                object.put("os", mOs);
            } catch (Exception e ){}
        }
        
        if (mVendor != null) {
            try {
                object.put("vendor", mVendor);
            } catch (Exception e ){}
        }
        
        if (mVersion != null) {
            try {
                object.put("version", mVersion);
            } catch (Exception e ){}
        }
        
        if (mUrl != null) {
            try {
                object.put("url", mUrl);
            } catch (Exception e ){}
        }
        
        
        if (mWebUrl != null) {
            try {
                object.put("webUrl", mWebUrl);
            } catch (Exception e ){}
        }
        
        return object;
    }
}
