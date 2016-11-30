package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKExperience;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.YKFile;

public class YKExperienceManager extends YKManager {
	private static final String PATH = getBase() + "comment/list";

	private static YKExperienceManager singleton = null;
	public static String CACHE_NAME = "Experience";
	private static Object lock = new Object();

	public static YKExperienceManager getInstance() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new YKExperienceManager();
			}
		}
		return singleton;
	}

	public ArrayList<YKExperience> getExperienceData() {
		return loadDataFromFile();
	}

	public YKHttpTask postYKExperienceData(final String page,final String catalogId, final Callback callback) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("page_index", page);
		parameters.put("catalogId", catalogId);
		return super.postURL(PATH, parameters, new com.yoka.mrskin.model.managers.base.Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {

				ArrayList<YKExperience> experience = null;
				if (result.isSucceeded()) {
					JSONArray tmpArray = object.optJSONArray("experience");
					if (tmpArray != null) {
						experience = new ArrayList<YKExperience>();
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								experience.add(YKExperience.parse(tmpArray.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}

				if(experience != null && experience.size() > 0 
						&& 0 == Integer.parseInt(page)
						&& "" .equals(catalogId)){
					saveDataToFile(experience);
				}

				if (callback != null) {
					callback.callback(result, experience);
				}
			}
		});
	}

	//保存数据
	private boolean saveDataToFile(ArrayList<YKExperience> experience) {
		JSONObject object;
		String str;
		byte[] data = null;
		try {
			JSONArray imageArray = new JSONArray();
			for (YKExperience exper : experience) {
				imageArray.put(exper.toJson());
			}
			object = new JSONObject();
			object.put("experience", imageArray);
			str = object.toString();
			data = str.getBytes("utf-8");
		} catch (Exception e) {
			return false;
		}
		YKFile.save(CACHE_NAME, data);
		return true;
	}

	private ArrayList<YKExperience> loadDataFromFile() {
		byte[] data = YKFile.read(CACHE_NAME);
		String str;
		ArrayList<YKExperience> experience = new ArrayList<YKExperience>();
		try {
			str = new String(data, "utf-8");
			JSONObject object = new JSONObject(str);
			JSONArray array = object.getJSONArray("experience");
			YKExperience image;
			for (int i = 0; i < array.length(); ++i) {
				image = YKExperience.parse(array.getJSONObject(i));

				experience.add(image);
			}
		} catch (Exception e) {
		}
		return experience;
	}



	public interface Callback {
		public void callback(YKResult result, ArrayList<YKExperience> topicData);
	}
}
