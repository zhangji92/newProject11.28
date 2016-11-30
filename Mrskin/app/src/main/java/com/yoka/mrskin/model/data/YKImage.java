package com.yoka.mrskin.model.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

/**
 * 图片
 * 
 * @author Y H L
 */
@SuppressWarnings("serial")
public class YKImage extends YKData implements Serializable
{

	/**
	 * 图片url
	 */
	private String mURL = "";

	/**
	 * 图片宽
	 */
	private int mwidth;

	/**
	 * 图片高
	 */
	private int mheight;
	/**
	 * 图片类型
	 */
	private String mMimetype;
	/**
	 * 图片对象
	 */
	private YKImage  mCover;

	public String getmURL() {
		return mURL;
	}

	public void setmURL(String mURL) {
		this.mURL = mURL;
	}

	public String getMimetype() {
		return mMimetype;
	}

	public void setMimetype(String mimetype) {
		this.mMimetype = mimetype;
	}

	public int getMwidth() {
		return mwidth;
	}

	public void setMwidth(int mwidth) {
		this.mwidth = mwidth;
	}

	public int getMheight() {
		return mheight;
	}

	public void setMheight(int mheight) {
		this.mheight = mheight;
	}


	public YKImage getmCover()
	{
		return mCover;
	}

	public void setmCover(YKImage mCover)
	{
		this.mCover = mCover;
	}

	public static YKImage parse(JSONObject object) {
		YKImage image = new YKImage();
		if (object != null) {
			image.parseData(object);
		}
		return image;
	}

	protected void parseData(JSONObject object) {
		super.parseData(object);
		try {
			mID = object.optString("id");
		} catch (Exception e) {
		}
		try {
			mURL = object.optString("url");
		} catch (Exception e) {
		}
		try {
			mMimetype = object.optString("mimetype");
		} catch (Exception e) {
		}
		try {
			mwidth = object.optInt("width");
		} catch (Exception e) {
		}
		try {
			mheight = object.optInt("height");
		} catch (Exception e) {
		}
		try {
			JSONObject tmpObject = object.getJSONObject("cover");
			mCover = YKImage.parse(tmpObject);
		} catch (JSONException e) {
		}
	}

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("id", mID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			object.put("width", mwidth);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			object.put("height", mheight);
		} catch (JSONException e) {
			e.printStackTrace();
		}try {
			object.put("url", mURL);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			object.put("mimetype", mMimetype);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			object.put("cover", null == mCover ? mCover : mCover.toJson());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public String toString() {
		return "YKImage [mURL=" + mURL + ", mwidth=" + mwidth + ", mheight="
				+ mheight + ", mMimetype=" + mMimetype + ", mCover=" + mCover
				+ "]";
	}



}