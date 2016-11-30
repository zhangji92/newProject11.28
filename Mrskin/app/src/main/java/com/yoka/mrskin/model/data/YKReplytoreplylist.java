package com.yoka.mrskin.model.data;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKReplytoreplylist extends YKData{
    
    /**
     * 
     */
    private static final long serialVersionUID = 2154883556614532744L;
    private YKUserinfo mUserinfo;
    private YKReplytouserinfo mReplytouserinfo;
    private String mContent;
    private String mYcommentflag;
    
    public YKUserinfo getmUserinfo() {
        return mUserinfo;
    }
    public void setmUserinfo(YKUserinfo mUserinfo) {
        this.mUserinfo = mUserinfo;
    }
    public YKReplytouserinfo getmReplytouserinfo() {
        return mReplytouserinfo;
    }
    public void setmReplytouserinfo(YKReplytouserinfo mReplytouserinfo) {
        this.mReplytouserinfo = mReplytouserinfo;
    }
    public String getmContent() {
        return mContent;
    }
    public void setmContent(String mContent) {
        this.mContent = mContent;
    }
    public String getmYcommentflag() {
        return mYcommentflag;
    }
    public void setmYcommentflag(String mYcommentflag) {
        this.mYcommentflag = mYcommentflag;
    }
    
    public YKReplytoreplylist(YKUserinfo mUserinfo,
	    YKReplytouserinfo mReplytouserinfo, String mContent,
	    String mYcommentflag) {
	super();
	this.mUserinfo = mUserinfo;
	this.mReplytouserinfo = mReplytouserinfo;
	this.mContent = mContent;
	this.mYcommentflag = mYcommentflag;
    }
    
    public YKReplytoreplylist() {
	super();
    }
    
    public static YKReplytoreplylist parse(JSONObject object) {
	YKReplytoreplylist replytoreplylist = new YKReplytoreplylist();
        if (object != null) {
            replytoreplylist.parseData(object);
        }
        return replytoreplylist;
    }

    protected void parseData(JSONObject object) {

        super.parseData(object);
        
        JSONObject tmpObject = null;
        try {
            tmpObject = object.optJSONObject("userinfo");
            mUserinfo = YKUserinfo.parse(tmpObject);
        } catch (Exception e) {
        }
        try {
            tmpObject = object.optJSONObject("replytouserinfo");
            mReplytouserinfo = YKReplytouserinfo.parse(tmpObject);
        } catch (Exception e) {
        }
        try {
            mContent = object.optString("content");
        } catch (Exception e) {
        }
        try {
            mYcommentflag = object.optString("mycommentflag");
        } catch (Exception e) {
        }
    }

}
