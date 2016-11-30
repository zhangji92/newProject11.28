package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKHomeData extends YKData
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Image 类型图片数据
     */
    private YKImage mCoverimage;
    /**
     * 文章对象
     */
    private YKTopicData mTopic;
    
    private String mAdage;
    

    public String getmAdage() {
        return mAdage;
    }

    public void setmAdage(String mAdage) {
        this.mAdage = mAdage;
    }

    public YKImage getmCoverimage() {
        return mCoverimage;
    }

    public void setmCoverimage(YKImage mCoverimage) {
        this.mCoverimage = mCoverimage;
    }

    public YKTopicData getmTopic() {
        return mTopic;
    }

    public void setmTopic(YKTopicData mTopic) {
        this.mTopic = mTopic;
    }
    
    public YKHomeData(YKImage mCoverimage, YKTopicData mTopic, String mAdage)
    {
        super();
        this.mCoverimage = mCoverimage;
        this.mTopic = mTopic;
        this.mAdage = mAdage;
    }

    public YKHomeData()
    {
        super();
    }

    public static YKHomeData parse(JSONObject object) {
        YKHomeData homeData = new YKHomeData();
        if (object != null) {
            homeData.parseData(object);
        }
        return homeData;
    }

    protected void parseData(JSONObject object) {

        super.parseData(object);
        try {
            JSONObject tmpObject = object.getJSONObject("cover_image");
            mCoverimage = YKImage.parse(tmpObject);
        } catch (JSONException e) {
        }

        try {
            JSONObject tmpObject = object.getJSONObject("topic");
            mTopic = YKTopicData.parse(tmpObject);
        } catch (JSONException e) {
        }
        
        try {
            mAdage = object.getString("adage");
        } catch (JSONException e) {
        }
    }
}
