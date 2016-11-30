package com.yoka.mrskin.model.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKHomeAdContent extends YKData
{
    private int mShowTimeFirst;
    private int mShowTimeOther;
    private int mTotalCount;
    private ArrayList<YKHomeAd> mHomeAds = new ArrayList<YKHomeAd>();

    public int getmShowTimeFirst() {
        return mShowTimeFirst;
    }

    public void setmShowTimeFirst(int mShowTimeFirst) {
        this.mShowTimeFirst = mShowTimeFirst;
    }

    public int getmShowTimeOther() {
        return mShowTimeOther;
    }

    public void setmShowTimeOther(int mShowTimeOther) {
        this.mShowTimeOther = mShowTimeOther;
    }

    public int getmTotalCount() {
        return mTotalCount;
    }

    public void setmTotalCount(int mTotalCount) {
        this.mTotalCount = mTotalCount;
    }

    public ArrayList<YKHomeAd> getmHomeAds() {
        return mHomeAds;
    }

    public void setmHomeAds(ArrayList<YKHomeAd> mHomeAds) {
        this.mHomeAds = mHomeAds;
    }

    public YKHomeAdContent(int mShowTimeFirst, int mShowTimeOther,
            int mTotalCount, ArrayList<YKHomeAd> mHomeAds)
    {
        super();
        this.mShowTimeFirst = mShowTimeFirst;
        this.mShowTimeOther = mShowTimeOther;
        this.mTotalCount = mTotalCount;
        this.mHomeAds = mHomeAds;
    }

    public YKHomeAdContent()
    {
        super();
    }
    
    public static YKHomeAdContent parse(JSONObject object) {
        YKHomeAdContent homeAdContent = new YKHomeAdContent();
        if (object != null) {
            homeAdContent.parseData(object);
        }
        return homeAdContent;
    }

    @Override
    protected void parseData(JSONObject object) {
        super.parseData(object);
        try {
            mShowTimeFirst = object.optInt("ShowTimeFirst");
        } catch (Exception e) {
        }
        try {
            mShowTimeOther = object.optInt("ShowTimeOther");
        } catch (Exception e) {
        }
        try {
            mTotalCount = object.optInt("TotalCount");
        } catch (Exception e) {
        }
        try {
            JSONArray tmpArray = object.optJSONArray("Data");
            if (tmpArray != null) {
                for (int i = 0; i < tmpArray.length(); ++i) {
                    mHomeAds.add(YKHomeAd.parse(tmpArray.getJSONObject(i)));
                }
            }
        } catch (Exception e) {
        }

    }

    @Override
    public String toString() {
        return "YKHomeAdContent [mShowTimeFirst=" + mShowTimeFirst
                + ", mShowTimeOther=" + mShowTimeOther + ", mTotalCount="
                + mTotalCount + ", mHomeAds=" + mHomeAds + "]";
    }

}
