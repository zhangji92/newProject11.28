package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKReplytouserinfo extends YKData {

    /**
     * 
     */
    private static final long serialVersionUID = -3826670955191126680L;

    private String mReplytousername;

    private String mReplytouserid;

    public String getmReplytousername() {
	return mReplytousername;
    }

    public void setmReplytousername(String mReplytousername) {
	this.mReplytousername = mReplytousername;
    }

    public String getmReplytouserid() {
	return mReplytouserid;
    }

    public void setmReplytouserid(String mReplytouserid) {
	this.mReplytouserid = mReplytouserid;
    }

    public YKReplytouserinfo(String mReplytousername, String mReplytouserid) {
	super();
	this.mReplytousername = mReplytousername;
	this.mReplytouserid = mReplytouserid;
    }

    public YKReplytouserinfo() {
	super();
    }

    public static YKReplytouserinfo parse(JSONObject object) {
	YKReplytouserinfo replytouserinfo = new YKReplytouserinfo();
	if (object != null) {
	    replytouserinfo.parseData(object);
	}
	return replytouserinfo;
    }

    protected void parseData(JSONObject object) {

	super.parseData(object);

	try {
	    mReplytousername = object.optString("replytousername");
	} catch (Exception e) {
	}

	try {
	    mReplytouserid = object.optString("replytouserid");
	} catch (Exception e) {
	}
    }


}
