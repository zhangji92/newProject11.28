package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import com.yoka.mrskin.login.YKAvatar;
import com.yoka.mrskin.model.base.YKData;

public class YKNewExperience extends YKData
{
    private String mTitle;
    private YKAvatar mAvatar;
    private String mBrief;
    private String mUrl;
    private YKImage mImage;
    private String mLikenum;
    private String mReplynum;
    private String mComment_id;
    private String mSection;
    public String getmTitle()
    {
        return mTitle;
    }
    public void setmTitle(String mTitle)
    {
        this.mTitle = mTitle;
    }
    public YKAvatar getmAvatar()
    {
        return mAvatar;
    }
    public void setmAvatar(YKAvatar mAvatar)
    {
        this.mAvatar = mAvatar;
    }
    public String getmBrief()
    {
        return mBrief;
    }
    public void setmBrief(String mBrief)
    {
        this.mBrief = mBrief;
    }
    public String getmUrl()
    {
        return mUrl;
    }
    public void setmUrl(String mUrl)
    {
        this.mUrl = mUrl;
    }
    public YKImage getmImage()
    {
        return mImage;
    }
    public void setmImage(YKImage mImage)
    {
        this.mImage = mImage;
    }
    public String getmLikenum()
    {
        return mLikenum;
    }
    public void setmLikenum(String mLikenum)
    {
        this.mLikenum = mLikenum;
    }
    public String getmReplynum()
    {
        return mReplynum;
    }
    public void setmReplynum(String mReplynum)
    {
        this.mReplynum = mReplynum;
    }
    public String getmComment_id()
    {
        return mComment_id;
    }
    public void setmComment_id(String mComment_id)
    {
        this.mComment_id = mComment_id;
    }
    public String getmSection()
    {
        return mSection;
    }
    public void setmSection(String mSection)
    {
        this.mSection = mSection;
    }
    public YKNewExperience(String mTitle, YKAvatar mAvatar, String mBrief,
            String mUrl, YKImage mImage, String mLikenum, String mReplynum,
            String mComment_id, String mSection)
    {
        super();
        this.mTitle = mTitle;
        this.mAvatar = mAvatar;
        this.mBrief = mBrief;
        this.mUrl = mUrl;
        this.mImage = mImage;
        this.mLikenum = mLikenum;
        this.mReplynum = mReplynum;
        this.mComment_id = mComment_id;
        this.mSection = mSection;
    }

    public YKNewExperience()
    {
        super();
    }
    
    public static YKNewExperience parse(JSONObject object) {
        YKNewExperience newExperience = new YKNewExperience();
        if (object != null) {
            newExperience.parseData(object);
        }
        return newExperience;
    }

    protected void parseData(JSONObject object) {
        super.parseData(object);
        JSONObject tmpObject = null;

        try {
            mTitle = object.optString("title");
        } catch (Exception e) {
        }
        try {
            tmpObject = object.optJSONObject("avatar");
            mAvatar = YKAvatar.parse(tmpObject);
        } catch (Exception e) {
        }
        try {
            mBrief = object.optString("brief");
        } catch (Exception e) {
        }
        try {
            mUrl = object.optString("url");
        } catch (Exception e) {
        }
        try {
            tmpObject = object.optJSONObject("image");
            mImage = YKImage.parse(tmpObject);
        } catch (Exception e) {
        }
        try {
            mLikenum = object.optString("likenum");
        } catch (Exception e) {
        }
        try {
            mReplynum = object.optString("replynum");
        } catch (Exception e) {
        }
        try {
            mComment_id = object.optString("comment_id");
        } catch (Exception e) {
        }
        try {
            mSection = object.optString("section");
        } catch (Exception e) {
        }
    }

}
