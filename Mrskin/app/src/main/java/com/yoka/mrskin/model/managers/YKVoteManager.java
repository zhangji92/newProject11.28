package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.socialize.utils.Log;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKVoteResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 首页投票
 * @author zlz
 * @Data 2016年8月16日
 */
public class YKVoteManager extends YKManager{
	public static final String TAG = YKVoteManager.class
			.getSimpleName();
	private static final String VOTEPATH = getBase() + "user/vote";
	private static Object lock = new Object();
	private static YKVoteManager singleton = null;


	public static YKVoteManager getInstnace() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new YKVoteManager();
			}
		}
		return singleton;
	}
	/**
	 * 投票请求网络
	 * @param authtoken
	 * @param voteid
	 * @param user_choice
	 * @param callback
	 */
	public void postVote(String authtoken,int voteid,int user_choice,final VoteCallback callback){

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("authtoken", authtoken);
		parameters.put("voteid", voteid);
		parameters.put("user_choice", user_choice);

		super.postURL(VOTEPATH, parameters, new Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {
				YKVoteResult vote = null;

				if(null != object){
					try {
						Log.d("vote",object.getJSONObject("result").toString());
						vote = YKVoteResult.parse(object.getJSONObject("result"));
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				if(null != callback){
					callback.callback(result,vote);

				}


			}


		});

	}

	public interface VoteCallback{
		public void callback(YKResult result,YKVoteResult voteResult); 
	}

}
