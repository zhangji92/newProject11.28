package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKUserinfo extends YKData {

    /**
     * 
     */
    private static final long serialVersionUID = -5209031428834419508L;
    private String mUsername;
    private String mUserid;
    
    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmUserid() {
        return mUserid;
    }

    public void setmUserid(String mUserid) {
        this.mUserid = mUserid;
    }

    public YKUserinfo(String mUsername, String mUserid) {
	super();
	this.mUsername = mUsername;
	this.mUserid = mUserid;
    }

    public YKUserinfo() {
	super();
    }

    public static YKUserinfo parse(JSONObject object) {
	YKUserinfo userinfo = new YKUserinfo();
	if (object != null) {
	    userinfo.parseData(object);
	}
	return userinfo;
    }

    protected void parseData(JSONObject object) {

	super.parseData(object);

	try {
	    mUsername = object.optString("username");
	} catch (Exception e) {
	}

	try {
	    mUserid = object.optString("userid");
	} catch (Exception e) {
	}
    }
}
