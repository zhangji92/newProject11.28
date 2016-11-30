package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 删除一级回复接口
 * @author Y H Ｌ
 *
 */
public class YKDeleteInformationAndTipsAllManager extends YKManager{
    private static final String PATHINFORMATION = getBase() + "delete/myinformationreply";
    private static final String PATHTIPS        = getBase() + "delete/mycommentreply";

    private static YKDeleteInformationAndTipsAllManager singleton = null;
    private static Object lock = new Object();


    public static YKDeleteInformationAndTipsAllManager getInstance() {
	synchronized (lock) {
	    if (singleton == null) {
		singleton = new YKDeleteInformationAndTipsAllManager();
	    }
	}
	return singleton;
    }

    //资讯 心得
    public YKHttpTask postDeleteAllYKInformationAndTips(final deleteAllYKInformationAndTipsCallback callback,int iSCommentOrTips,String authtoken,int replytoID) {
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
		printRequestResult("YKDeleteInformationAndTipsAllManager", object, result);
		
		if (result.isSucceeded()) {

		}
		// do callback
		if (callback != null) {
		    callback.callback(result);
		}
	    }
	});
    }

    public interface deleteAllYKInformationAndTipsCallback{
	public void callback(YKResult result); 
    }
}
