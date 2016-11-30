package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

@SuppressWarnings("serial")
public class YKSearchList extends YKData
{
    /**
     * 搜索列表
     * 标题
     */
    private String mTitle;
    /**
     * 图片对象
     */
    private YKImage mImage;
    /**
     * 价格
     */
    private String mPrice;
    /**
     * 点评
     */
    private String mRemark;
    /**
     * 喜欢
     */
    private String mLike;
    public String getmTitle() {
        return mTitle;
    }
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
    public YKImage getmImage() {
        return mImage;
    }
    public void setmImage(YKImage mImage) {
        this.mImage = mImage;
    }
    public String getmPrice() {
        return mPrice;
    }
    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }
    public String getmRemark() {
        return mRemark;
    }
    public void setmRemark(String mRemark) {
        this.mRemark = mRemark;
    }
    public String getmLike() {
        return mLike;
    }
    public void setmLike(String mLike) {
        this.mLike = mLike;
    }
    public YKSearchList(String mTitle, YKImage mImage, String mPrice,
            String mRemark, String mLike)
    {
        super();
        this.mTitle = mTitle;
        this.mImage = mImage;
        this.mPrice = mPrice;
        this.mRemark = mRemark;
        this.mLike = mLike;
    }
    public YKSearchList()
    {
        super();
    }
    public static YKSearchList parse(JSONObject object) {
        YKSearchList searchlist = new YKSearchList();
        if (object != null) {
            searchlist.parseData(object);
        }
        return searchlist;
    }

    protected void parseData(JSONObject object) {
        
        super.parseData(object);

        try {
            mTitle = object.getString("title");
        } catch (JSONException e) {
        }
        try {
            JSONObject tmpObject = object.getJSONObject("startTime");
            mImage = YKImage.parse(tmpObject);
        } catch (JSONException e) {
        }
        try {
            mPrice = object.getString("price");
        } catch (JSONException e) {
        }
        try {
            mRemark = object.getString("remark");
        } catch (JSONException e) {
        }
        try {
            mLike = object.getString("like");
        } catch (JSONException e) {
        }
    }
}
