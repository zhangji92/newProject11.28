package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKWebentry extends YKData
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String mUrl = "";
    private YKImage mCover;
    private String mTitle = "";
    private String mSubTitle = "";
    private String mActionType = "";

    public String getmUrl()
    {
        return mUrl;
    }

    public void setmUrl(String mUrl)
    {
        this.mUrl = mUrl;
    }

    public YKImage getmCover()
    {
        return mCover;
    }

    public void setmCover(YKImage mCover)
    {
        this.mCover = mCover;
    }

    public String getmTitle()
    {
        return mTitle;
    }

    public void setmTitle(String mTitle)
    {
        this.mTitle = mTitle;
    }

    public String getmSubTitle()
    {
        return mSubTitle;
    }

    public void setmSubTitle(String mSubTitle)
    {
        this.mSubTitle = mSubTitle;
    }

    public String getmActionType()
    {
        return mActionType;
    }

    public void setmActionType(String mActionType)
    {
        this.mActionType = mActionType;
    }

    public YKWebentry(String mUrl, YKImage mCover, String mTitle,
            String mSubTitle, String mActionType)
        {
            super();
            this.mUrl = mUrl;
            this.mCover = mCover;
            this.mTitle = mTitle;
            this.mSubTitle = mSubTitle;
            this.mActionType = mActionType;
        }

    public YKWebentry()
    {
        super();
    }

    public static YKWebentry parse(JSONObject object) {
        YKWebentry webentry = new YKWebentry();
        if (object != null) {
            webentry.parseData(object);
        }
        return webentry;
    }

    protected void parseData(JSONObject object) {
        
        super.parseData(object);
        JSONObject tmpObject = null;

        try {
            mTitle = object.optString("title");
        } catch (Exception e) {
            
        }
        try {
            mSubTitle = object.optString("sub_title");
        } catch (Exception e) {
            
        }
        
        try {
            mUrl = object.optString("url");
        } catch (Exception e) {
            
        }
        
        try {
            mActionType = object.optString("action_type");
        } catch (Exception e) {
            
        }
        
        try {
            tmpObject = object.optJSONObject("cover");
            mCover = YKImage.parse(tmpObject);
        } catch (Exception e) {
            
        }
    }
    
    public JSONObject toJson(){
        JSONObject object = new JSONObject();
        try {
            object.put("url", mUrl);
        } catch (Exception e){}
        try {
            object.put("cover", mCover.toJson());
        } catch (Exception e){}
        try {
            object.put("title", mTitle);
        } catch (Exception e){}
        try {
            object.put("sub_title", mSubTitle);
        } catch (Exception e){}
        try {
            object.put("action_type", mActionType);
        } catch (Exception e){}
        return object;
    }

}
