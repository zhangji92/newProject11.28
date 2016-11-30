package com.yoka.mrskin.model.managers;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.yoka.mrskin.model.data.UHCity;
import com.yoka.mrskin.model.data.UHCurrentCity;

@SuppressLint("CommitPrefEdits")
public class UHCityManager {
	private static final String TAG = "UHCityManager";
	private static final String CITY_LIST = "citylist.txt";
	private static final String RECOMMENDATION_CITY_LIST = "recommendationcitylist.txt";

	private Context mContext;
	private ArrayList<UHCity> mCityList;
	private ArrayList<UHCity> mRecommendationCityList;
	private ArrayList<UHCurrentCity> mCurrentCityList;
	private UHCurrentCity mCurrentCity;
	

	/**
	 * @param mContext
	 */
	public UHCityManager(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * @return the currentCity
	 */
	public UHCurrentCity getCurrentCity() {
		return mCurrentCity;
	}

	/**
	 * @param currentCity
	 *            the currentCity to set
	 */
	public void setCurrentCity(UHCurrentCity currentCity) {
		UHCurrentCity tmpcity = getCityByID(currentCity.getCity().getID());
		if (tmpcity != null) {
			mCurrentCityList.remove(tmpcity);
		}
		insertCurrentCity(currentCity);

		mCurrentCity = currentCity;
	}

	public void setup() {
		Log.d(TAG, "setup");
		mCurrentCityList = new ArrayList<UHCurrentCity>();
		loadAllCityList();
		
		//		new LoadTask().execute();
		//		loadRecommendationCityList();
		//		loadCurrentCities();
		Log.d("abcdef", "setup finish");
	}

	private ArrayList<UHCity> loadCityList(String filename) {
		ArrayList<UHCity> citylist = new ArrayList<UHCity>();
		try {
			InputStream in = mContext.getResources().getAssets().open(filename);
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
			in.read(buffer);

			return parseCityList(new String(buffer, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return citylist;
	}

	private ArrayList<UHCity> parseCityList(String jsonData) {
		Log.d(TAG, "parsecitylist");
		JSONObject object = null;
		ArrayList<UHCity> citylist = new ArrayList<UHCity>();
		try {
			object = new JSONObject(jsonData);
			JSONArray cities = object.getJSONArray("citylist");
			JSONObject cityJsonObject;
			for (int i = 0; i < cities.length(); ++i) {
				cityJsonObject = (JSONObject) cities.get(i);
				UHCity city = new UHCity();
				city.parseData(cityJsonObject);
				citylist.add(city);
				// Log.d(TAG, city.toString());

			}
		} catch (Exception e) {

		} finally {
		}
		return citylist;
	}

	private void loadAllCityList() {
		mCityList = loadCityList(CITY_LIST);
	}

	private void loadRecommendationCityList() {
		mRecommendationCityList = loadCityList(RECOMMENDATION_CITY_LIST);
		UHCity city;
		city = new UHCity();
		city.setCityName("定位");
		city.setID("-1");
		mRecommendationCityList.add(0, city);

		return;
	}

	private void loadCurrentCities() {
		Log.d(TAG, "loadCurrentCities");
		SharedPreferences userInfo = mContext.getSharedPreferences("uhweather", 0);
		String cityString = userInfo.getString("citylist", "");
		Log.d(TAG, "loadCurrentCities = " + cityString);
		try {
			JSONObject object = new JSONObject(cityString);
			JSONArray weatherArray = object.getJSONArray("citylist");
			UHCurrentCity currentCity;
			for (int i = 0; i < weatherArray.length(); ++i) {
				currentCity = new UHCurrentCity();
				currentCity.parseData(weatherArray.getJSONObject(i));
				addCurrentCity(currentCity, true);
				Log.d(TAG, currentCity.toString());
			}
		} catch (JSONException e) {
			Log.e(TAG, "loadCurrentCities " + e.toString());
		}

		if (mCurrentCityList.size() > 0) {
			mCurrentCity = mCurrentCityList.get(0);
		}
	}

	public void saveCurrentCities() {
		Log.d(TAG, "saveCurrentCities");
		SharedPreferences userInfo = mContext.getSharedPreferences("uhweather", 0);
		SharedPreferences.Editor editor = userInfo.edit();
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"citylist\":[");
		UHCurrentCity city;
		for (int i = 0; i < mCurrentCityList.size(); ++i) {
			if (i > 0) {
				buffer.append(",");
			}
			city = mCurrentCityList.get(i);
			buffer.append(city.toJsonString());
		}
		buffer.append("]}");
		Log.d(TAG, "savecurrent cities =" + buffer.toString());
		editor.putString("citylist", buffer.toString());
		editor.commit();
	}

	public void addCurrentCity(UHCurrentCity city) {
		insertCurrentCity(city);
		saveCurrentCities();
	}

	public void removeCurrentCity(UHCurrentCity city) {
		mCurrentCityList.remove(city);
		saveCurrentCities();
	}
	public ArrayList<UHCity> searchCities(String keyword) {
		if (keyword == null)
			return new ArrayList<UHCity>();
		if (keyword.length() <= 0)
			return new ArrayList<UHCity>();

		UHCity city;
		ArrayList<UHCity> mTargetList = new ArrayList<UHCity>();
		if(null != mCityList){
			for (int i = 0; i < mCityList.size(); ++i) {
				city = mCityList.get(i);
				if (city.contains(keyword)) {
					mTargetList.add(city);
				}
			}
		}
		return mTargetList;
	}

	public ArrayList<UHCity> getAllCityList() {
		return mCityList;
	}

	public ArrayList<UHCity> getRecommendationCityList() {
		return mRecommendationCityList;
	}

	public ArrayList<UHCurrentCity> getCurrentCityList() {
		return mCurrentCityList;
	}

	public boolean hasCity(UHCity city) {
		UHCurrentCity currentCity;
		for (int i = 0; i < mCurrentCityList.size(); ++i) {
			currentCity = mCurrentCityList.get(i);
			if (city.getID().equals(currentCity.getCity().getID())) {
				return true;
			}
		}
		return false;
	}

	public boolean hasCityCapacity() {
		return mCurrentCityList.size() < 9;
	}

	public boolean hasNoCity() {
		return mCurrentCityList.size() <= 0;
	}

	public UHCurrentCity getCityByID(String ID) {
		if (ID == null)
			return null;
		UHCurrentCity currentCity;
		for (int i = 0; i < mCurrentCityList.size(); ++i) {
			currentCity = mCurrentCityList.get(i);
			if (ID.equals(currentCity.getCity().getID())) {
				return currentCity;
			}
		}
		return null;
	}

	private void insertCurrentCity(UHCurrentCity currentCity) {
		addCurrentCity(currentCity, false);
	}

	private void addCurrentCity(UHCurrentCity currentCity, boolean toTail) {
		if (currentCity == null)
			return;

		if (toTail) {
			mCurrentCityList.add(currentCity);
		} else {
			UHCurrentCity tmpCurrentCity = getCityByID(currentCity.getCity().getID());
			if (tmpCurrentCity != null) {
				mCurrentCityList.remove(tmpCurrentCity);
			}
			mCurrentCityList.add(0, currentCity);
		}
	}
	/**
	 * 异步任务读取城市列表
	 */
	/*
	 class LoadTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			loadAllCityList();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.i(TAG, "load all citys finish!");
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.i(TAG, "start load all citys");
		}
	}*/
}
