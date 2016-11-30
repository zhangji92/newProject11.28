package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKTopicData extends YKData
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    private String mTopicTitle;

    /**
     * 文章簡介
     */
    private String mTopicDesc;
    /**
     * 文章URL
     */
    private String mTopicUrl;
    /**
     * 心灵鸡汤
     */
    private String mAdage;
    /**
     * 文章封面图
     */
    private YKImage mCover;
    /**
     * 文章的类别
     */
    private String mType;
    /**
     * 作者对象
     */
    private String  mUser;
    /**
     * 文章创建时间
     */
    private Long create_time;
    /**
     *topic 
     */
    private YKTopicData mTopic;
    /**
     * subTitle
     */
    private String mSubTitle;

    public String getmSubTitle()
    {
        return mSubTitle;
    }

    public void setmSubTitle(String mSubTitle)
    {
        this.mSubTitle = mSubTitle;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

    public YKImage getmCover()
    {
        return mCover;
    }

    public void setmCover(YKImage mCover)
    {
        this.mCover = mCover;
    }

    public YKTopicData getmTopic()
    {
        return mTopic;
    }

    public void setmTopic(YKTopicData mTopic)
    {
        this.mTopic = mTopic;
    }

    public String getmTopicTitle()
    {
        return mTopicTitle;
    }

    public void setmTopicTitle(String mTopicTitle)
    {
        this.mTopicTitle = mTopicTitle;
    }

    public String getmTopicDesc()
    {
        return mTopicDesc;
    }

    public void setmTopicDesc(String mTopicDesc)
    {
        this.mTopicDesc = mTopicDesc;
    }

    public String getmTopicUrl()
    {
        return mTopicUrl;
    }

    public void setmTopicUrl(String mTopicUrl)
    {
        this.mTopicUrl = mTopicUrl;
    }

    public String getmAdage()
    {
        return mAdage;
    }

    public void setmAdage(String mAdage)
    {
        this.mAdage = mAdage;
    }

    public YKImage getImage()
    {
        return mCover;
    }

    public void setImage(YKImage image)
    {
        this.mCover = image;
    }

    public String getmType()
    {
        return mType;
    }

    public void setmType(String mType)
    {
        this.mType = mType;
    }

    public String getmUser()
    {
        return mUser;
    }

    public void setmUser(String mUser)
    {
        this.mUser = mUser;
    }

    public Long getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(Long create_time)
    {
        this.create_time = create_time;
    }

    public YKTopicData(String mTopicTitle, String mTopicDesc, String mTopicUrl,
            String mAdage, YKImage mCover, String mType, String mUser,
            Long create_time, YKTopicData mTopic,String mSubTitle)
        {
            super();
            this.mTopicTitle = mTopicTitle;
            this.mTopicDesc = mTopicDesc;
            this.mTopicUrl = mTopicUrl;
            this.mAdage = mAdage;
            this.mCover = mCover;
            this.mType = mType;
            this.mUser = mUser;
            this.create_time = create_time;
            this.mTopic = mTopic;
            this.mSubTitle = mSubTitle;
        }

    public YKTopicData()
    {
        super();
    }

    public static YKTopicData parse(JSONObject object) {
        YKTopicData topic = new YKTopicData();
        if (object != null) {
            topic.parseData(object);
        }
        return topic;
    }

    protected void parseData(JSONObject object) {
        
        super.parseData(object);
        
        try {
            mTopicTitle = object.getString("topic_title");
        } catch (JSONException e) {
        }

        try {
            mTopicDesc = object.getString("topic_desc");
        } catch (JSONException e) {
        }
        try {
            mSubTitle = object.getString("topic_sub_title");
        } catch (JSONException e) {
        }

        try {
            mTopicUrl = object.getString("topic_url");
        } catch (JSONException e) {
        }
        try {
            mType = object.getString("type");
        } catch (JSONException e) {
        }
        try {
            mUser = object.getString("author");
        } catch (JSONException e) {
        }
        try {
            create_time = object.optLong("create_time");
        } catch (Exception e) {
        }
        try {
            JSONObject tmpObject = object.getJSONObject("image");
            mCover = YKImage.parse(tmpObject);
        } catch (JSONException e) {
        }
        try {
            JSONObject tmpObject = object.getJSONObject("topic");
            mTopic = YKTopicData.parse(tmpObject);
        } catch (JSONException e) {
        }
    }
    
    public JSONObject toJson(){
        JSONObject object = new JSONObject();
        
        try {
            object.put("topic_title", mTopicTitle);
        } catch (Exception e){}
        try {
            object.put("topic_desc", mTopicDesc);
        } catch (Exception e){}
        try {
            object.put("topic_sub_title", mSubTitle);
        } catch (Exception e){}
        try {
            object.put("topic_url", mTopicUrl);
        } catch (Exception e){}
        try {
            object.put("type", mType);
        } catch (Exception e){}
        try {
            object.put("author", mUser);
        } catch (Exception e){}
        try {
            object.put("create_time", create_time);
        } catch (Exception e){}
        try {
            object.put("image", mCover.toJson());
        } catch (Exception e){}
        try {
            object.put("topic", mTopic.toJson());
        } catch (Exception e){}
        return object;
    }
}
