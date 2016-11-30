    package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

public class YKPushArticle
{
    /**
     * 文章标题
     */
    private String mTitle;
    /**
     * 文章副标题
     */
    private String mSubTitle;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSubTitle() {
        return mSubTitle;
    }

    public void setmSubTitle(String mSubTitle) {
        this.mSubTitle = mSubTitle;
    }

    public YKPushArticle(String mTitle, String mSubTitle)
    {
        super();
        this.mTitle = mTitle;
        this.mSubTitle = mSubTitle;
    }

    public YKPushArticle()
    {
        super();
    }

    public static YKPushArticle parse(JSONObject object) {
        YKPushArticle article = new YKPushArticle();
        if (object != null) {
            article.parseData(object);
        }
        return article;
    }

    protected void parseData(JSONObject object) {

        try {
            mTitle = object.getString("title");
        } catch (JSONException e) {
        }
        try {
            mSubTitle = object.getString("sub_title");
        } catch (JSONException e) {
        }
    }
}
