package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKKeyword extends YKData
{

    /**
     * 
     */
    private static final long serialVersionUID = -7652138795176331453L;
    
    private String mTitle;
    private YKImage mThumbnail;
    
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public YKImage getThumbnail() {
        return mThumbnail;
    }
    public void setThumbnail(YKImage thumbnail) {
        mThumbnail = thumbnail;
    }
    
    public static YKKeyword parse(JSONObject object) {
        YKKeyword keyword = new YKKeyword();
        if (object != null) {
            keyword.parseData(object);
        }
        return keyword;
    }

    protected void parseData(JSONObject object) {
        super.parseData(object);

        JSONObject tmpObject = null;
        try {
            mTitle = object.optString("name");
        } catch (Exception e) {
        }

        try {
            tmpObject = object.optJSONObject("thumbnail");
            mThumbnail = YKImage.parse(tmpObject);
        } catch (Exception e) {
        }
    }

}
