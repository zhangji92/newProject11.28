package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 点赞
 * @author Y H Ｌ
 *
 */
public class YKThumbupManager extends YKManager{
    private static final String PATHINFORMATION = getBase() + "topic/commentlike";
    private static final String PATHTIPS        = getBase() + "experience/commentlike";

    private static YKThumbupManager singleton = null;
    private static Object lock = new Object();


    public static YKThumbupManager getInstance() {
	synchronized (lock) {
	    if (singleton == null) {
		singleton = new YKThumbupManager();
	    }
	}
	return singleton;
    }

    //资讯 心得
    public YKHttpTask postYKThumbup(final ThumbupCallback callback,int iSCommentOrTips,String authtoken,int replytoID) {
	// 后期增加的字段
	HashMap<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("authtoken", authtoken);
	parameters.put("replyid", replytoID);
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
		printRequestResult("YKThumbupManager", object, result);
		
		if (result.isSucceeded()) {

		}
		// do callback
		if (callback != null) {
		    callback.callback(result);
		}
	    }
	});
    }

    public interface ThumbupCallback{
	public void callback(YKResult result); 
    }
}
