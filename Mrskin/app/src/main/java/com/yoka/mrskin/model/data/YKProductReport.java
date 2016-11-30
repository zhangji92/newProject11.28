package com.yoka.mrskin.model.data;

import com.yoka.mrskin.model.base.YKData;

@SuppressWarnings("serial")
public class YKProductReport extends YKData
{
    private String mId;
    private String mTitle;
    private String mImageUrl;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getImage() {
        return mImageUrl;
    }

    public void setImage(String image) {
        this.mImageUrl = image;
    }

    public YKProductReport(String mId, String title, String image)
    {
        super();
        this.mId = mId;
        this.mTitle = title;
        this.mImageUrl = image;
    }

    public YKProductReport()
    {
        super();
    }
}
