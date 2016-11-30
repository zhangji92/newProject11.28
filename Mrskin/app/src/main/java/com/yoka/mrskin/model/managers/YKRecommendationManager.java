package com.yoka.mrskin.model.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKRecommendation;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.YKFile;

public class YKRecommendationManager extends YKManager
{
	private static final String PATH = getBase() + "index/article";

	public static String CACHE_NAME = "recommendationData";

	private static YKRecommendationManager singleton = null;
	private static Object lock = new Object();

	public static YKRecommendationManager getInstance() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new YKRecommendationManager();
			}
		}
		return singleton;
	}

	public YKRecommendation getTopicData() {
		return loadDataFromFile();
	}

	public YKHttpTask postYKTopicData(String page, final Callback callback) {
		/*用户token*/
		String authToken = "";
		if(null != YKCurrentUserManager.getInstance().getUser()){

			authToken = YKCurrentUserManager.getInstance().getUser().getToken();
		}
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("page",page);
		parameters.put("authtoken", authToken);
		return super.postURL(PATH, parameters, new com.yoka.mrskin.model.managers.base.Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

				YKRecommendation experience = null;

				if (result.isSucceeded()) {
					JSONObject obj = null;
					try {
						obj = object.getJSONObject("homelist");
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (obj != null) {
						experience =YKRecommendation.parse(obj);
					}
				}

				if(experience != null){
					saveDataToFile(experience);
				}

				if (callback != null) {
					callback.callback(result, experience);
				}
			}
		});
	}

	private boolean saveDataToFile(YKRecommendation topicData) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		byte[] data = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(topicData);
			data = baos.toByteArray();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
		}
		YKFile.save(CACHE_NAME, data);
		return true;
	}

	private YKRecommendation loadDataFromFile() {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		byte[] data = YKFile.read(CACHE_NAME);
		try {
			bais = new ByteArrayInputStream(data);
			ois = new ObjectInputStream(bais);
			YKRecommendation objectData = (YKRecommendation) ois.readObject();
			return objectData;
		} catch (Exception e) {
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				bais.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	public YKHttpTask postYKNewLoadMore(final Callback callback, String page) {
		/*用户token*/
		String authToken = "";
		if(null != YKCurrentUserManager.getInstance().getUser()){

			authToken = YKCurrentUserManager.getInstance().getUser().getToken();
		}
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("page", page);
		parameters.put("authtoken", authToken);
		return super.postURL(PATH, parameters,
				new com.yoka.mrskin.model.managers.base.Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {
				printRequestResult("postYKNewLoadMore", object, result);

				YKRecommendation topic = null;
				if (result.isSucceeded()) {
					//                    JSONArray tmpArray = object
					//                            .optJSONArray("homelist");
					//                    if (tmpArray != null) {
					//                        topic = new YKRecommendation();
					//                        for (int i = 0; i < tmpArray.length(); ++i) {
					//                            try {
					//                                topic.add(YKRecommendation
					//                                        .parse(tmpArray
					//                                                .getJSONObject(i)));
					//                            } catch (Exception e) {
					//                                e.printStackTrace();
					//                            }
					//                        }
					//                    }
					JSONObject obj = null;
					try {
						obj = object.getJSONObject("homelist");
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (obj != null) {
						topic = YKRecommendation.parse(obj);

					}
				}
				// do callback
				if (callback != null) {
					callback.callback(result, topic);
				}
			}
		});

	}

	public interface Callback
	{
		public void callback(YKResult result,
				YKRecommendation topicData);
	}
}
