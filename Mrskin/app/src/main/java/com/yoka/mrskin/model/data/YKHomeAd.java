package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKHomeAd extends YKData
{
    private String mId;
    private String mTitle;
    private String mSummary;
    private String mImgUrl;
    private int mImgWidth;
    private int mImgHeight;
    private String mLinkUrl;
    private String mShowUrl;
    private String mClickUrl;
    private int mPositionid;
    private int mBgCount;
    
    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }


    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSummary() {
        return mSummary;
    }

    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public String getmImgUrl() {
        return mImgUrl;
    }

    public void setmImgUrl(String mImgUrl) {
        this.mImgUrl = mImgUrl;
    }

    public int getmImgWidth() {
        return mImgWidth;
    }

    public void setmImgWidth(int mImgWidth) {
        this.mImgWidth = mImgWidth;
    }

    public int getmImgHeight() {
        return mImgHeight;
    }

    public void setmImgHeight(int mImgHeight) {
        this.mImgHeight = mImgHeight;
    }

    public String getmLinkUrl() {
        return mLinkUrl;
    }

    public void setmLinkUrl(String mLinkUrl) {
        this.mLinkUrl = mLinkUrl;
    }

    public String getmShowUrl() {
        return mShowUrl;
    }

    public void setmShowUrl(String mShowUrl) {
        this.mShowUrl = mShowUrl;
    }

    public String getmClickUrl() {
        return mClickUrl;
    }

    public void setmClickUrl(String mClickUrl) {
        this.mClickUrl = mClickUrl;
    }

    public int getmPositionid() {
        return mPositionid;
    }

    public void setmPositionid(int mPositionid) {
        this.mPositionid = mPositionid;
    }

    public int getmBgCount() {
        return mBgCount;
    }

    public void setmBgCount(int mBgCount) {
        this.mBgCount = mBgCount;
    }

    public YKHomeAd(String mId, String mTitle, String mSummary, String mImgUrl,
            int mImgWidth, int mImgHeight, String mLinkUrl, String mShowUrl,
            String mConvesionUrl, int mPositionid, int mBgCount)
    {
        super();
        this.mId = mId;
        this.mTitle = mTitle;
        this.mSummary = mSummary;
        this.mImgUrl = mImgUrl;
        this.mImgWidth = mImgWidth;
        this.mImgHeight = mImgHeight;
        this.mLinkUrl = mLinkUrl;
        this.mShowUrl = mShowUrl;
        this.mClickUrl = mConvesionUrl;
        this.mPositionid = mPositionid;
        this.mBgCount = mBgCount;
    }

    public YKHomeAd()
    {
        mId = "";
        mTitle = "";
        mSummary = "";
        mImgUrl = "";
        mImgWidth = Integer.valueOf(0);
        mImgHeight = Integer.valueOf(0);
        mLinkUrl = "";
        mShowUrl = "";
        mClickUrl = "";
        mPositionid = Integer.valueOf(0);
        mBgCount = Integer.valueOf(0);
    }

    public static YKHomeAd parse(JSONObject object) {
        YKHomeAd homeAd = new YKHomeAd();
        if (object != null) {
            homeAd.parseData(object);
        }
        return homeAd;
    }

    @Override
    protected void parseData(JSONObject object) {
        super.parseData(object);
        try {
            mId = object.optInt("ID") + "";
        }catch(Exception e){
        }
        try {
            mTitle = object.optString("Title");
        } catch (Exception e) {
        }
        try {
            mSummary = object.optString("Summary");
        } catch (Exception e) {
        }
        try {
            mImgUrl = object.optString("FocusImgUrl");
        } catch (Exception e) {
        }
        try {
            mImgWidth = object.optInt("FocusImgWidth");
        } catch (Exception e) {
        }
        try {
            mImgHeight = object.optInt("FocusImgHeight");
        } catch (Exception e) {
        }
        try {
            mLinkUrl = object.optString("SpreadUrl");
            System.out.println("ykhomead linkurl = " + mLinkUrl);
        } catch (Exception e) {
        }
        try {
            mShowUrl = object.optString("CFUrl");
        } catch (Exception e) {
        }
        try {
            mClickUrl = object.optString("ConvesionUrl");
        } catch (Exception e) {
        }
        try {
            mPositionid = object.optInt("PositionId");
        } catch (Exception e) {
        }
        try {
            mBgCount = object.optInt("BgCount");
        } catch (Exception e) {
        }
    }

    @Override
    public String toString() {
        return "YKHomeAd [mTitle=" + mTitle + ", mSummary=" + mSummary
                + ", mImgUrl=" + mImgUrl + ", mImgWidth=" + mImgWidth
                + ", mImgHeight=" + mImgHeight + ", mLinkUrl=" + mLinkUrl
                + ", mShowUrl=" + mShowUrl + ", mConvesionUrl=" + mClickUrl
                + ", mPositionid=" + mPositionid + ", mBgCount=" + mBgCount
                + "]";
    }

}
