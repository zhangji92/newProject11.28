package com.yoka.mrskin.model.data;

import java.util.ArrayList;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

/**
 * 分类
 * @author wangchunhui
 */

public class YKCategory extends YKData
{
    private static final long serialVersionUID = 7636679573753043273L;
    private String mTitle;
    private YKImage mThumbnail;
    private String mParentID;
    private ArrayList<YKCategory> mSubCategories;


    public YKCategory() {
        mSubCategories = new ArrayList<YKCategory>();
    }

    public void setParentID(String ID) {
        mParentID = ID;
    }

    public String getParentID() {
        return mParentID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public YKImage getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(YKImage thumbnail) {
        mThumbnail = thumbnail;
    }

    public ArrayList<YKCategory> getSubCategories() {
        return mSubCategories;
    }

    public void addSubCategory(YKCategory category) {
        if (category != null) {
            mSubCategories.add(category);
        }
    }

    public static YKCategory parse(JSONObject object) {
        YKCategory category = new YKCategory();
        if (object != null) {
            category.parseData(object);
        }
        return category;
    }

    protected void parseData(JSONObject object) {
        super.parseData(object);
        JSONObject tmpObject = null;
        try {
            mTitle = object.optString("name");
        } catch (Exception e) {
        }

        try {
            tmpObject = object.optJSONObject("thumbnail");
            mThumbnail = YKImage.parse(tmpObject);
        } catch (Exception e) {
        }

        try {
            mParentID = object.optString("parent_id");
        } catch (Exception e) {
        }

        //        JSONArray tmpArray = object.optJSONArray("topics");
        //        if (tmpArray != null) {
        //            for (int i = 0; i < tmpArray.length(); ++i) {
        //                try {
        //                    topics.add(YKTopicData.parse(tmpArray.getJSONObject(i)));
        //                } catch (Exception e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //        }
    }

    public JSONObject toJson(){
        JSONObject object = new JSONObject();

        try {
            object.put("name", mTitle);
        } catch (Exception e){}
        try {
            object.put("thumbnail", mThumbnail.toJson());
        } catch (Exception e){}
        try {
            object.put("parent_id", mParentID);
        } catch (Exception e){}
        return object;
    }
}
