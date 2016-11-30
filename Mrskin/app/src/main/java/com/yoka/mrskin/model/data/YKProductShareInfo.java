package com.yoka.mrskin.model.data;

import com.yoka.mrskin.model.base.YKData;

/**
 * js分享
 * @author z l l
 *
 */
@SuppressWarnings("serial")
public class YKProductShareInfo extends YKData
{
    private String mImgUrl = "";
    private String mTitle = "";
    private String mDesc = "";
    private String mLink = "";

    public String getmImgUrl() {
        return mImgUrl;
    }

    public void setmImgUrl(String mImgUrl) {
        this.mImgUrl = mImgUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public YKProductShareInfo(String mImgUrl, String mTitle, String mDesc,
            String mLink)
    {
        super();
        this.mImgUrl = mImgUrl;
        this.mTitle = mTitle;
        this.mDesc = mDesc;
        this.mLink = mLink;
    }

    public YKProductShareInfo()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "YKProductShareInfo [mImgUrl=" + mImgUrl + ", mTitle=" + mTitle
                + ", mDesc=" + mDesc + ", mLink=" + mLink + "]";
    }

}
