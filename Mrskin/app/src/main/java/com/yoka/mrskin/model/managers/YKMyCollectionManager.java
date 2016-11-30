package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKExperience;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKMyCollectionManager extends YKManager {
	private static final String PATH = getBase() + "my/commentcollect";
	private static YKMyCollectionManager singleton = null;
	private static Object lock = new Object();

	public static YKMyCollectionManager getInstance() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new YKMyCollectionManager();
			}
		}
		return singleton;
	}
	public YKHttpTask postYKMyCollectionData(String page,String userId,final Callback callback) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("page_index", page);
		parameters.put("user_id", userId);
		//parameters.put("user_id", "6705753");
		return super.postURL(PATH, parameters, new com.yoka.mrskin.model.managers.base.Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

				ArrayList<YKExperience> topicData = null;
				if (result.isSucceeded()&&object!=null) {
					JSONArray tmpArray = object.optJSONArray("collect_list");
					if (tmpArray != null) {
						topicData = new ArrayList<YKExperience>();
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
							topicData.add(YKExperience.parse(tmpArray.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}

				if (callback != null) {
					callback.callback(result, topicData);
				}
			}
		});
	}
	public interface Callback {
		public void callback(YKResult result, ArrayList<YKExperience> topicData);
	}
}
