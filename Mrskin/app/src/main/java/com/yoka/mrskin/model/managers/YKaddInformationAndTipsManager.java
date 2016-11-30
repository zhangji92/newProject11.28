package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKComment;
import com.yoka.mrskin.model.data.YKReplytoreplylist;
import com.yoka.mrskin.model.data.YKReplytouserinfo;
import com.yoka.mrskin.model.data.YKUserinfo;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 添加回复再回复接口
 * @author Y H Ｌ
 *
 */
public class YKaddInformationAndTipsManager extends YKManager{
    private static final String PATHINFORMATION = getBase() + "add/topicreplytoreply";
    private static final String PATHTIPS        = getBase() + "add/experiencereplytoreply";

    private static YKaddInformationAndTipsManager singleton = null;
    private static Object lock = new Object();


    public static YKaddInformationAndTipsManager getInstance() {
	synchronized (lock) {
	    if (singleton == null) {
		singleton = new YKaddInformationAndTipsManager();
	    }
	}
	return singleton;
    }

    //资讯 心得
    public YKHttpTask postYKInformationAndTips(final AddYKInformationAndTipsCallback callback,int iSCommentOrTips,String authtoken,int replytoID,String replyID,String content,String replytouserid,String replytousername) {
	// 后期增加的字段
	HashMap<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("authtoken", authtoken);
	parameters.put("replytoID", replytoID);
	parameters.put("replyID", replyID);
	parameters.put("content", content);
	parameters.put("replytouserid", replytouserid);
	parameters.put("replytousername", replytousername);
	String url = "";

	switch (iSCommentOrTips) {
	//心得
	case 1:
	    url  = PATHTIPS;
	    break;
	    //资讯
	case 2:
	    url = PATHINFORMATION;
	    break;
	}
	// do request
	return super.postURL(url, parameters, new Callback() {

	    @Override
	    public void doCallback(YKHttpTask task, JSONObject object,YKResult result) {
		printRequestResult("YKaddInformationAndTipsManager", object, result);

		YKComment comment = new  YKComment();
		if (result.isSucceeded()) {
		    ArrayList<YKReplytoreplylist> replyList = new ArrayList<YKReplytoreplylist>();
		    JSONObject tmpJSONObject = null;
		    JSONObject userInfo = null;
		    JSONObject replyUserJson = null;
		    JSONObject tmpObject = object.optJSONObject("result");
		    if (tmpObject != null) {
		    	 YKReplytoreplylist reply = new YKReplytoreplylist();
			try {
			    tmpJSONObject = tmpObject.getJSONObject("replytoreplylist");
			    comment.setID(tmpJSONObject.getString("id"));
			    comment.setmContext(tmpJSONObject.getString("content"));
			    reply.setmContent(tmpJSONObject.getString("content"));
			    reply.setID(tmpJSONObject.getString("id"));
			   
			    YKUserinfo  user = new YKUserinfo();
			    userInfo = tmpJSONObject.getJSONObject("userinfo");
			    user.setmUserid(userInfo.getString("userid"));
			    user.setmUsername(userInfo.getString("username"));
			    reply.setmUserinfo(user);
			    if(tmpJSONObject.has("replytouserinfo")){
				replyUserJson = tmpJSONObject.getJSONObject("replytouserinfo");//这里会有异常
				
				if(null != replyUserJson){
				    YKReplytouserinfo replyUser = new YKReplytouserinfo();
				    replyUser.setmReplytouserid(replyUserJson.getString("replytouserid"));
				    replyUser.setmReplytousername(replyUserJson.getString("replytousername"));
				    reply.setmReplytouserinfo(replyUser);
				}
			    }
			    replyList.add(reply);
			    comment.setmReplytoreplylist(replyList);
			} catch (JSONException e) {//replyUserJson,这个没有的时候服务器会返回空数组
				replyList.add(reply);
				comment.setmReplytoreplylist(replyList);
		    	e.printStackTrace();
		    }
		    }
		}
		// do callback
		if (callback != null) {
		    callback.callback(result,comment);
		}
	    }
	});
    }

    public interface AddYKInformationAndTipsCallback{
	public void callback(YKResult result,YKComment comment); 
    }
}
