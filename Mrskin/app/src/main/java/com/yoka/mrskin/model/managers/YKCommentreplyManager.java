package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKComment;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKCommentreplyManager extends YKManager {
    private static final String PATHEXPER = getBase() + "add/commentreply";
    private static final String PATHTOPIC = getBase() + "add/topicreply";

    private static YKCommentreplyManager singleton = null;
    private static Object lock = new Object();

    public static YKCommentreplyManager getInstance() {
	synchronized (lock) {
	    if (singleton == null) {
		singleton = new YKCommentreplyManager();
	    }
	}
	return singleton;
    }

    public YKHttpTask postCommentReply(String authtoken,String comment_id,String content,ArrayList<String> imageUrl,final IDReplyCallback callback) {
	HashMap<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("authtoken", authtoken);
	parameters.put("commentID", comment_id);
	parameters.put("content", content);
	parameters.put("image", imageUrl);
	return super.postURL(PATHEXPER, parameters, new Callback() {

	    @Override
	    public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

		String  mCommentID = null;
		JSONObject tmpArray = null;

		if (result.isSucceeded()) {
		    JSONObject tmpObject = object.optJSONObject("result");
		    if (tmpObject != null) {
			try {
			    tmpArray = tmpObject.getJSONObject("replytoreplylist");
			    mCommentID = tmpArray.getString("id");
			} catch (Exception e) {
			    e.printStackTrace();
			}
		    }
		}

		if (callback != null) {
		    callback.callback(result,mCommentID);
		}
	    }
	});
    }

    public YKHttpTask postTopicReply(String authtoken,String comment_id,String content,ArrayList<String> imageUrl, final IDReplyCallback callback) {
	HashMap<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("authtoken", authtoken);
	parameters.put("topicID", comment_id);
	parameters.put("content", content);
	parameters.put("image", imageUrl);
	return super.postURL(PATHTOPIC, parameters, new Callback() {

	    @Override
	    public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

		String  mCommentID = null;
		JSONObject tmpArray = null;

		if (result.isSucceeded()) {
		    JSONObject tmpObject = object.optJSONObject("result");
		    if (tmpObject != null) {
			try {
			    tmpArray = tmpObject.getJSONObject("replytoreplylist");
			    mCommentID = tmpArray.getString("id");
			} catch (Exception e) {
			    e.printStackTrace();
			}
		    }
		}

		if (callback != null) {
		    callback.callback(result,mCommentID);
		}
	    }
	});
    }

    public YKHttpTask postAeniorReply(String authtoken,String comment_id,String content,ArrayList<String> imageUrl,int type, final ReplyCallback callback) {
	HashMap<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("authtoken", authtoken);
	parameters.put("content", content);
	parameters.put("image", imageUrl);

	String url = "";

	switch (type) {
	//心得高级回复
	case 1:
	    parameters.put("commentID", comment_id);
	    url  = PATHEXPER;
	    break;
	    //	资讯高级回复
	case 2:
	    parameters.put("topicID", comment_id);
	    url = PATHTOPIC;
	    break;
	}


	return super.postURL(url, parameters, new Callback() {

	    @Override
	    public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

		ArrayList<YKComment> mComments = null;

		if (result.isSucceeded()) {
		    JSONArray tmpArray = object.optJSONArray("result");
		    if (tmpArray != null) {
			mComments = new ArrayList<YKComment>();
			for (int i = 0; i < tmpArray.length(); ++i) {
			    try {
				mComments.add(YKComment.parse(tmpArray.getJSONObject(i)));
			    } catch (Exception e) {
				e.printStackTrace();
			    }
			}
		    }
		}

		if (callback != null) {
		    callback.callback(result);
		}
	    }
	});
    }



    public interface ReplyCallback {
	public void callback(YKResult result);
    }
    public interface IDReplyCallback {
	public void callback(YKResult result,String replyID);
    }
}
