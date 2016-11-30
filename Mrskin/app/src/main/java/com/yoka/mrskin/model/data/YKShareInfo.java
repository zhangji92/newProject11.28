package com.yoka.mrskin.model.data;

import com.yoka.mrskin.model.base.YKData;

public class YKShareInfo extends YKData
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String mId;
    private String mTitle;
    private String mPic;
    private String mUrl;
    private String mSummary;
    private String mPlatform;
    private String mTryId;
    private String mType;

    public String getmTryId() {
        return mTryId;
    }

    public void setmTryId(String mTryId) {
        this.mTryId = mTryId;
    }

    public String getmId()
    {
        return mId;
    }

    public void setmId(String mId)
    {
        this.mId = mId;
    }

    public String getmTitle()
    {
        return mTitle;
    }

    public void setmTitle(String mTitle)
    {
        this.mTitle = mTitle;
    }

    public String getmPic()
    {
        return mPic;
    }

    public void setmPic(String mPic)
    {
        this.mPic = mPic;
    }

    public String getmUrl()
    {
        return mUrl;
    }

    public void setmUrl(String mUrl)
    {
        this.mUrl = mUrl;
    }

    public String getmSummary()
    {
        return mSummary;
    }

    public void setmSummary(String mSummary)
    {
        this.mSummary = mSummary;
    }

    public String getmPlatform()
    {
        return mPlatform;
    }

    public void setmPlatform(String mPlatform)
    {
        this.mPlatform = mPlatform;
    }

    public String getmType() {
		return mType;
	}

	public void setmType(String mType) {
		this.mType = mType;
	}

	public YKShareInfo(String mId, String mTitle, String mPic, String mUrl,
            String mSummary, String mPlatform, String mTryId)
        {
            super();
            this.mId = mId;
            this.mTitle = mTitle;
            this.mPic = mPic;
            this.mUrl = mUrl;
            this.mSummary = mSummary;
            this.mPlatform = mPlatform;
            this.mTryId = mTryId;
        }

    public YKShareInfo()
    {
        super();
    }
}
