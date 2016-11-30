package com.yoka.mrskin.model.data;

import java.util.ArrayList;

import com.yoka.mrskin.model.base.YKData;

/**
 * 提交试用报告的对象
 * 
 * @author z l l
 * 
 */
@SuppressWarnings("serial")
public class YKTrialProductInfo extends YKData
{
    private String mTrialId;
    private String mRating;
    private String mExterior;
    private String mEffect;
    private String mFeel;
    private String mTitle;
    private String mDesc;

    // private ArrayList<String> mImagePaths;

    public String getmTrialId() {
        return mTrialId;
    }

    public void setmTrialId(String mTrialId) {
        this.mTrialId = mTrialId;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public String getmExterior() {
        return mExterior;
    }

    public void setmExterior(String mExterior) {
        this.mExterior = mExterior;
    }

    public String getmEffect() {
        return mEffect;
    }

    public void setmEffect(String mEffect) {
        this.mEffect = mEffect;
    }

    public String getmFeel() {
        return mFeel;
    }

    public void setmFeel(String mFeel) {
        this.mFeel = mFeel;
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

    // public ArrayList<String> getmImagePaths() {
    // return mImagePaths;
    // }
    //
    // public void setmImagePaths(ArrayList<String> mImagePaths) {
    // this.mImagePaths = mImagePaths;
    // }

    public YKTrialProductInfo(String mTrialId, String mRating,
            String mExterior, String mEffect, String mFeel, String mTitle,
            String mDesc, ArrayList<String> mImagePaths)
    {
        super();
        this.mTrialId = mTrialId;
        this.mRating = mRating;
        this.mExterior = mExterior;
        this.mEffect = mEffect;
        this.mFeel = mFeel;
        this.mTitle = mTitle;
        this.mDesc = mDesc;
        // this.mImagePaths = mImagePaths;
    }

    public YKTrialProductInfo()
    {
        super();
    }

    // @Override
    // public String toString() {
    // return "YKTrialProductInfo [mTrialId=" + mTrialId + ", mRating="
    // + mRating + ", mExterior=" + mExterior + ", mEffect=" + mEffect
    // + ", mFeel=" + mFeel + ", mTitle=" + mTitle + ", mDesc="
    // + mDesc + ", mImagePaths=" + mImagePaths + "]";
    // }
}
