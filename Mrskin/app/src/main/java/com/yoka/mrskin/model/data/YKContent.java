package com.yoka.mrskin.model.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKContent extends YKData
{
    private ArrayList<YKTopicData> mTopicDatas;
    private ArrayList<YKNewExperience> mExperiences;
    /**
     * 标题
     */
    private String mTitle;

    public ArrayList<YKTopicData> getmTopicDatas()
    {
        return mTopicDatas;
    }

    public void setmTopicDatas(ArrayList<YKTopicData> mTopicDatas)
    {
        this.mTopicDatas = mTopicDatas;
    }

    public ArrayList<YKNewExperience> getmExperiences()
    {
        return mExperiences;
    }

    public void setmExperiences(ArrayList<YKNewExperience> mExperiences)
    {
        this.mExperiences = mExperiences;
    }

    public String getmTitle()
    {
        return mTitle;
    }

    public void setmTitle(String mTitle)
    {
        this.mTitle = mTitle;
    }

    public YKContent(ArrayList<YKTopicData> mTopicDatas,
            ArrayList<YKNewExperience> mExperiences, String mTitle)
        {
            super();
            this.mTopicDatas = mTopicDatas;
            this.mExperiences = mExperiences;
            this.mTitle = mTitle;
        }

    public YKContent()
    {
        super();
    }

    public static YKContent parse(JSONObject object) {
        YKContent content = new YKContent();
        if (object != null) {
            content.parseData(object);
        }
        return content;
    }

    protected void parseData(JSONObject object) {
        JSONObject obj = null;
        
        try {
            mTitle = object.getString("title");
        } catch (JSONException e) {
        }
        
        try {
            obj = object.getJSONObject("content");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        
        super.parseData(object);

        try {
            JSONArray data = obj.getJSONArray("topic");
            if (data == null) {
                return;
            }
            for (int i = 0; i < data.length(); i++) {
                JSONObject objectp = data.getJSONObject(i);
                if (objectp == null) {
                    continue;
                }
                if (mTopicDatas == null) {
                    mTopicDatas = new ArrayList<YKTopicData>();
                }
                mTopicDatas.add(YKTopicData.parse(objectp));
            }
        } catch (JSONException e) {
        }

        try {
            JSONArray data = obj.getJSONArray("experience");
            if (data == null) {
                return;
            }
            for (int i = 0; i < data.length(); i++) {
                JSONObject objectp = data.getJSONObject(i);
                if (objectp == null) {
                    continue;
                }
                if (mExperiences == null) {
                    mExperiences = new ArrayList<YKNewExperience>();
                }
                mExperiences.add(YKNewExperience.parse(objectp));
            }
        } catch (JSONException e) {
        }
    }

}
