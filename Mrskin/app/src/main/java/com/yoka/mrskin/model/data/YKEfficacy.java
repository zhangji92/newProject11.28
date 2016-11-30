package com.yoka.mrskin.model.data;

import java.util.ArrayList;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

/**
 * 功效对象
 * @author wangchunhui
 *
 */
public class YKEfficacy extends YKData
{
    /**
     * 
     */
    private static final long serialVersionUID = -9217898643624392776L;
    private String mTitle;
    private ArrayList<YKEfficacy> mSubEfficacies;
    private String mParentID;
    private YKImage mThumbnail;

    public YKImage getmThumbnail()
    {
        return mThumbnail;
    }

    public void setmThumbnail(YKImage mThumbnail)
    {
        this.mThumbnail = mThumbnail;
    }

    public YKEfficacy() {
        mSubEfficacies = new ArrayList<YKEfficacy>();
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

    public ArrayList<YKEfficacy> getSubEfficacies() {
        return mSubEfficacies;
    }

    public void addSubEfficacy(YKEfficacy efficacy) {
        if (efficacy != null) {
            mSubEfficacies.add(efficacy);
        }
    }

    public static YKEfficacy parse(JSONObject object) {
        YKEfficacy efficacy = new YKEfficacy();
        if (object != null) {
            efficacy.parseData(object);
        }
        return efficacy;
    }

    protected void parseData(JSONObject object) {

        super.parseData(object);
        JSONObject tmpObject = null;
        try {
            mTitle = object.optString("name");
        } catch (Exception e) {
        }
        try {
            mParentID = object.optString("parent_id");
        } catch (Exception e) {
        }
        try {
            tmpObject = object.optJSONObject("thumbnail");
            mThumbnail = YKImage.parse(tmpObject);
        } catch (Exception e) {
        }
    }

    public JSONObject toJson(){
        JSONObject object = new JSONObject();
        try {
            object.put("name", mTitle);
        } catch (Exception e) {}
        try {
            object.put("parent_id", mParentID);
        } catch (Exception e) {}
        try {
            object.put("thumbnail", mThumbnail.toJson());
        } catch (Exception e) {}
        return object;
    }
}
