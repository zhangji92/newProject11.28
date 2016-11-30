package com.yoka.mrskin.model.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKComment extends YKData
{
    /**
     * 评论列表页
     */
    private static final long serialVersionUID = 5787054151166959653L;
    /**
     * 添加时间
     */
    private  YKDate mAddDate;
    /**
     * 内容
     */
    private String mContext;
    /**
     * 用户信息
     */
    private YKExperienceUser mAuthor;
    
    private ArrayList<String> mImage;
    
    private String mYflag;
    private String mLikenum;
    private String mReplynum;
    private String mCommentlikeflag;
    private ArrayList<YKReplytoreplylist> mReplytoreplylist;
    
    public YKDate getmAddDate() {
        return mAddDate;
    }

    public void setmAddDate(YKDate mAddDate) {
        this.mAddDate = mAddDate;
    }

    public String getmContext() {
        return mContext;
    }

    public void setmContext(String mContext) {
        this.mContext = mContext;
    }

    public YKExperienceUser getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(YKExperienceUser mAuthor) {
        this.mAuthor = mAuthor;
    }

    public ArrayList<String> getmImage() {
        return mImage;
    }

    public void setmImage(ArrayList<String> mImage) {
        this.mImage = mImage;
    }

    public String getmYflag() {
        return mYflag;
    }

    public void setmYflag(String mYflag) {
        this.mYflag = mYflag;
    }

    public String getmLikenum() {
        return mLikenum;
    }

    public void setmLikenum(String mLikenum) {
        this.mLikenum = mLikenum;
    }

    public String getmReplynum() {
        return mReplynum;
    }

    public void setmReplynum(String mReplynum) {
        this.mReplynum = mReplynum;
    }

    public String getmCommentlikeflag() {
        return mCommentlikeflag;
    }

    public void setmCommentlikeflag(String mCommentlikeflag) {
        this.mCommentlikeflag = mCommentlikeflag;
    }

    public ArrayList<YKReplytoreplylist> getmReplytoreplylist() {
        return mReplytoreplylist;
    }

    public void setmReplytoreplylist(ArrayList<YKReplytoreplylist> mReplytoreplylist) {
        this.mReplytoreplylist = mReplytoreplylist;
    }

    public YKComment(YKDate mAddDate, String mContext,
	    YKExperienceUser mAuthor, ArrayList<String> mImage, String mYflag,
	    String mLikenum, String mReplynum, String mCommentlikeflag,
	    ArrayList<YKReplytoreplylist> mReplytoreplylist) {
	super();
	this.mAddDate = mAddDate;
	this.mContext = mContext;
	this.mAuthor = mAuthor;
	this.mImage = mImage;
	this.mYflag = mYflag;
	this.mLikenum = mLikenum;
	this.mReplynum = mReplynum;
	this.mCommentlikeflag = mCommentlikeflag;
	this.mReplytoreplylist = mReplytoreplylist;
    }

    public YKComment()
    {
        super();
    }
    
    public static YKComment parse(JSONObject object) {
        YKComment comment = new YKComment();
        if (object != null) {
            comment.parseData(object);
        }
        return comment;
    }

    protected void parseData(JSONObject object) {

        super.parseData(object);
        
        JSONObject tmpObject = null;
        try {
            tmpObject = object.optJSONObject("addDate");
            mAddDate = YKDate.parse(tmpObject);
        } catch (Exception e) {
        }
        try {
            mContext = object.optString("context");
        } catch (Exception e) {
        }
        try {
            mYflag = object.optString("myflag");
        } catch (Exception e) {
        }
        try {
            mLikenum = object.optString("likenum");
        } catch (Exception e) {
        }
        try {
            mReplynum = object.optString("replynum");
        } catch (Exception e) {
        }
        try {
            mCommentlikeflag = object.optString("commentlikeflag");
        } catch (Exception e) {
        }
        try {
            tmpObject = object.optJSONObject("author");
            mAuthor = YKExperienceUser.parse(tmpObject);
        } catch (Exception e) {
        }
        try {
            JSONArray tmpArray = object.optJSONArray("image");
            if (tmpArray != null) {
        	mImage = new ArrayList<String>();
                for (int i = 0; i < tmpArray.length(); ++i) {
                    mImage.add(tmpArray.getString(i));
                }
            }
        } catch (Exception e) {
        }
        try {
            JSONArray tmpArray = object.optJSONArray("replytoreplylist");
            if (tmpArray != null) {
        	mReplytoreplylist = new ArrayList<YKReplytoreplylist>();
                for (int i = 0; i < tmpArray.length(); ++i) {
                    mReplytoreplylist.add(YKReplytoreplylist.parse(tmpArray.getJSONObject(i)));
                }
            }
        } catch (Exception e) {
        }
    }

}
