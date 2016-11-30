package com.yoka.mrskin.model.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKWebComment extends YKData
{
    /**
     * 评论列表页
     */
    private static final long serialVersionUID = 6475124395472208448L;
    private String mReplytoID;//评论id
    private String mReplyID;//回复id
    private String mReplytouserid;//被回复者id
    private String mReplytousername;//被回复者昵称
    private String mContent;//回复内容
    private String mReplyuserid;//回复者id
    private String mReplyusername;//回复者昵称
    public String getmReplytoID() {
        return mReplytoID;
    }
    public void setmReplytoID(String mReplytoID) {
        this.mReplytoID = mReplytoID;
    }
    public String getmReplyID() {
        return mReplyID;
    }
    public void setmReplyID(String mReplyID) {
        this.mReplyID = mReplyID;
    }
    public String getmReplytouserid() {
        return mReplytouserid;
    }
    public void setmReplytouserid(String mReplytouserid) {
        this.mReplytouserid = mReplytouserid;
    }
    public String getmReplytousername() {
        return mReplytousername;
    }
    public void setmReplytousername(String mReplytousername) {
        this.mReplytousername = mReplytousername;
    }
    public String getmContent() {
        return mContent;
    }
    public void setmContent(String mContent) {
        this.mContent = mContent;
    }
    public String getmReplyuserid() {
        return mReplyuserid;
    }
    public void setmReplyuserid(String mReplyuserid) {
        this.mReplyuserid = mReplyuserid;
    }
    public String getmReplyusername() {
        return mReplyusername;
    }
    public void setmReplyusername(String mReplyusername) {
        this.mReplyusername = mReplyusername;
    }
    public YKWebComment(String mReplytoID, String mReplyID,
	    String mReplytouserid, String mReplytousername, String mContent,
	    String mReplyuserid, String mReplyusername) {
	super();
	this.mReplytoID = mReplytoID;
	this.mReplyID = mReplyID;
	this.mReplytouserid = mReplytouserid;
	this.mReplytousername = mReplytousername;
	this.mContent = mContent;
	this.mReplyuserid = mReplyuserid;
	this.mReplyusername = mReplyusername;
    }
    public YKWebComment() {
	super();
	// TODO Auto-generated constructor stub
    }
}
