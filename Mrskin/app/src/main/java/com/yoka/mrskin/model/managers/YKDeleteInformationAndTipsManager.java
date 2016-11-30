package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 删除回复再回复接口
 * @author Y H Ｌ
 *
 */
public class YKDeleteInformationAndTipsManager extends YKManager{
    private static final String PATHINFORMATION = getBase() + "delect/mytopicreply";
    private static final String PATHTIPS        = getBase() + "delect/myexperiencereply";

    private static YKDeleteInformationAndTipsManager singleton = null;
    private static Object lock = new Object();


    public static YKDeleteInformationAndTipsManager getInstance() {
	synchronized (lock) {
	    if (singleton == null) {
		singleton = new YKDeleteInformationAndTipsManager();
	    }
	}
	return singleton;
    }

    //资讯 心得
    public YKHttpTask postDeleteYKInformationAndTips(final deleteYKInformationAndTipsCallback callback,int iSCommentOrTips,String authtoken,int replytoID,String replytouserid) {
	// 后期增加的字段
	HashMap<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("authtoken", authtoken);
	parameters.put("replyid", replytoID);
	parameters.put("replytoreplyid", replytouserid);
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
		printRequestResult("YKDeleteInformationAndTipsManager", object, result);
		
		if (result.isSucceeded()) {

		}
		// do callback
		if (callback != null) {
		    callback.callback(result);
		}
	    }
	});
    }

    public interface deleteYKInformationAndTipsCallback{
	public void callback(YKResult result); 
    }
}
