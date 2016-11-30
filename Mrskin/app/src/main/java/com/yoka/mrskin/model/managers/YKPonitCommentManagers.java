package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 点赞收藏状态接口
 * collect=1 表示收藏 avail=1 表示点赞 =0表示未收藏和点赞

 */
public class YKPonitCommentManagers extends YKManager
{
	private static final String PATH = getBase() + "user/commentstatus";

	private static YKPonitCommentManagers singleton = null;
	private static Object lock = new Object();


	public static YKPonitCommentManagers getInstance() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new YKPonitCommentManagers();
			}
		}
		return singleton;
	}


	public YKHttpTask postYKPonitComment(final PonitCommentCallback callback,String commentID,String uid) {

		// 后期增加的字段
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("commentID", commentID);
		parameters.put("uid", uid);

		// do request
		return super.postURL(PATH, parameters, new Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {
				printRequestResult("YKPonitCommentManagers", object, result);
				JSONObject obj = null;
				String collect = null;
				String avail = null;
				if (result.isSucceeded()) {
					try {
						obj = object.getJSONObject("result");
						collect = obj.getString("collect");
						avail = obj.getString("avail");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{

				}
				// do callback
				if (callback != null) {
					callback.callback(result,collect,avail);
				}
			}
		});
	}

	public interface PonitCommentCallback{
		public void callback(YKResult result,String collect,String avail); 
	}
}
