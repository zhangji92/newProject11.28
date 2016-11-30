package com.yoka.mrskin.model.managers;

/**
 * 获取首页下午茶数据
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKWebentry;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.util.YKFile;

public class YKAfternoonTeaManager extends YKManager
{
	public static final String TAG = YKAfternoonTeaManager.class
			.getSimpleName();
	private static final String PATH = getBase() + "index/subbanner";
	public static String CACHE_NAME = "AfternoonData";
	private static YKAfternoonTeaManager singleton = null;
	private static Object lock = new Object();

	public static YKAfternoonTeaManager getInstnace() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new YKAfternoonTeaManager();
			}
		}
		return singleton;
	}

	private YKAfternoonTeaManager()
	{
		super();
		Log.d(TAG, "constructor");
	}

	public ArrayList<YKWebentry> getAfternoonData() {
		return loadDataAfternoonFile();
	}

	public YKHttpTask requestData(final YKAfternoonTeaCallback callback) {
		// do request
		return super.postURL(PATH, null, new Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {
				{
					ArrayList<YKWebentry> webentries = null;
					if (result.isSucceeded()) {
						JSONArray tmpArray = null;
						tmpArray = object.optJSONArray("web_entry_list");
						if (tmpArray != null) {
							JSONObject tmpObject;
							webentries = new ArrayList<YKWebentry>();
							for (int i = 0; i < tmpArray.length(); ++i) {
								try {
									tmpObject = tmpArray.getJSONObject(i);
									webentries.add(YKWebentry.parse(tmpObject));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}

					if(webentries != null && webentries.size() > 0){
						saveDataToFile(webentries);
					}

					if (callback != null) {
						callback.callback(result, webentries);
					}
				}
			}
		});
	}

	private boolean saveDataToFile(ArrayList<YKWebentry> afternoon) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		byte[] data = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(afternoon);
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

	private ArrayList<YKWebentry> loadDataAfternoonFile() {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		byte[] data = YKFile.read(CACHE_NAME);
		try {
			bais = new ByteArrayInputStream(data);
			ois = new ObjectInputStream(bais);
			ArrayList<YKWebentry> objectData = (ArrayList<YKWebentry>) ois.readObject();
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

	public interface YKAfternoonTeaCallback
	{
		public void callback(YKResult result, ArrayList<YKWebentry> webentries);
	}
}
