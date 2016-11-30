package com.yoka.mrskin.model.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yoka.mrskin.model.base.YKData;

public class YKExperience extends YKData {

	/**
	 * 心得数据类
	 */
	private static final long serialVersionUID = 3073143366411362758L;

	private String content;

	private ArrayList<YKImage> images = new ArrayList<YKImage>();

	private String produt_id;

	private YKExperienceUser author;

	private String title;

	private int time;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArrayList<YKImage> getImages() {
		return images;
	}

	public void setImages(ArrayList<YKImage> images) {
		this.images = images;
	}

	public String getProdut_id() {
		return produt_id;
	}

	public void setProdut_id(String produt_id) {
		this.produt_id = produt_id;
	}

	public YKExperienceUser getAuthor() {
		return author;
	}

	public void setAuthor(YKExperienceUser author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public static YKExperience parse(JSONObject object) {
		YKExperience topic = new YKExperience();
		if (object != null) {
			topic.parseData(object);
		}
		return topic;
	}

	protected void parseData(JSONObject object) {

		super.parseData(object);

		try {
			content = object.getString("content");
		} catch (JSONException e) {
		}

		try {
			produt_id = object.getString("produt_id");
		} catch (JSONException e) {
		}

		try {
			title = object.getString("title");
		} catch (JSONException e) {
		}
		try {
			url = object.getString("url");
		} catch (JSONException e) {
		}
		try {
			time = object.getInt("time");
		} catch (JSONException e) {
		}
		try {
			JSONObject tmpObject = object.getJSONObject("author");
			author = YKExperienceUser.parse(tmpObject);
		} catch (JSONException e) {
		}

		try {
			Log.d("mURL","size="+object.getString("images"));
		} catch (JSONException e1) {
			Log.e("mURL", e1.toString());
		}
		try {
			JSONArray tmpArray = object.getJSONArray("images");

			for (int i = 0; i < tmpArray.length(); i++) {

				images.add(YKImage.parse(tmpArray.getJSONObject(i)));

				Log.d("mURL", images.toString());
			}
		} catch (JSONException e) {
		}

	}

	public JSONObject toJson() {
		JSONObject object =new JSONObject();
		if (content != null) {
			try {
				object.put("content", content);
			} catch (Exception e ){}
		}
		if(images != null && images.size() > 0){

			try {
				object.put("images", toArrayJson(images));
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		if (author != null) {
			try {
				object.put("author", author.toJson());
			} catch (Exception e ){}
		}

		if (produt_id != null) {
			try {
				object.put("produt_id", produt_id);
			} catch (Exception e ){}
		}

		if (title != null) {
			try {
				object.put("title", title);
			} catch (Exception e ){}
		}

		if (time != 0) {
			try {
				object.put("time", time);
			} catch (Exception e ){}
		}

		if (url != null) {
			try {
				object.put("url", url);
			} catch (Exception e ){}
		}
		return object;
	}
	/**
	 * list 集合转换 JSONArray（用于缓存）--zlz
	 * @param list
	 * @return
	 */
	private JSONArray toArrayJson(ArrayList<YKImage> list){
		JSONArray arrayJson = new JSONArray();
		for (YKImage image : list) {
			arrayJson.put(image.toJson());

		}
		return arrayJson;
	}


}
