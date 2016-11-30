package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKUpDataExperience extends YKData {

    private static final long serialVersionUID = 1L;
    /**
     * 心得上传数据类
     */

    private String content;
    private String produt_id;
    private String url;
    private String mType;
    private String mUploadURL;
    private int mWidth;
    private int mHeight;

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getProdut_id()
    {
        return produt_id;
    }

    public void setProdut_id(String produt_id)
    {
        this.produt_id = produt_id;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getmType()
    {
        return mType;
    }

    public void setmType(String mType)
    {
        this.mType = mType;
    }

    public String getmUploadURL()
    {
        return mUploadURL;
    }

    public void setmUploadURL(String mUploadURL)
    {
        this.mUploadURL = mUploadURL;
    }

    public int getmWidth()
    {
        return mWidth;
    }

    public void setmWidth(int mWidth)
    {
        this.mWidth = mWidth;
    }

    public int getmHeight()
    {
        return mHeight;
    }

    public void setmHeight(int mHeight)
    {
        this.mHeight = mHeight;
    }

    public YKUpDataExperience()
    {
        super();
    }

    public static YKUpDataExperience parse(JSONObject object) {
        YKUpDataExperience topic = new YKUpDataExperience();
        if (object != null) {
            topic.parseData(object);
        }
        return topic;
    }

    protected void parseData(JSONObject object) {

        super.parseData(object);

        try {
            content = object.getString("content");
        } catch (JSONException e) {
        }

        try {
            produt_id = object.getString("product_id");
        } catch (JSONException e) {
        }
    }

    public JSONObject toJsonObject() {
        JSONObject object =new JSONObject();
        if (url != null) {
            try {
                object.put("image", mUploadURL);
            } catch (Exception e ){}
        }

        if (content != null) {
            try {
                object.put("content", content);
            } catch (Exception e ){}
        }

        if (produt_id != null) {
            try {
                object.put("product_id", produt_id);
            } catch (Exception e ){}
        }
        if(mWidth != 0){
            try {
                mWidth = object.getInt("mWidth");
            } catch (JSONException e) {
            }
        }
        if(mHeight != 0){
            try {
                mHeight = object.getInt("mHeight");
            } catch (JSONException e) {
            }
        }
        return object;
    }
}
