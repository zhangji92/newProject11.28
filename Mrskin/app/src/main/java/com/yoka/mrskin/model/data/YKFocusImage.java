package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.adsame.main.AdsameBannerAd;
import com.yoka.mrskin.model.base.YKData;

public class YKFocusImage extends YKData
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 标题
	 */
	private String mTitle;
	/**
	 * 图片对象
	 */
	private YKImage mImage;
	/**
	 * 跳转地址
	 */
	private String mUrl;

	private boolean mIsPro = false;

	private String mShowUrl;
	private String mClickUrl;
	private int mPosition;
	private String mId;
	private int mBgCount=0;
	private int type;//1:广告  2:焦点图 
	private AdsameBannerAd banner;


	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public int getmPosition() {
		return mPosition;
	}

	public void setmPosition(int mPosition) {
		this.mPosition = mPosition;
	}

	public String getmShowUrl() {
		return mShowUrl;
	}

	public void setmShowUrl(String mShowUrl) {
		this.mShowUrl = mShowUrl;
	}

	public String getmClickUrl() {
		return mClickUrl;
	}

	public void setmClickUrl(String mClickUrl) {
		this.mClickUrl = mClickUrl;
	}

	public boolean ismIsPro() {
		return mIsPro;
	}

	public void setmIsPro(boolean mIsPro) {
		this.mIsPro = mIsPro;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public YKImage getmImage() {
		return mImage;
	}

	public void setmImage(YKImage mImage) {
		this.mImage = mImage;
	}

	public String getmUrl() {
		return mUrl;
	}

	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}



	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public AdsameBannerAd getBanner() {
		return banner;
	}

	public void setBanner(AdsameBannerAd banner) {
		this.banner = banner;
	}

	public YKFocusImage(String mId, String mTitle, YKImage mImage, String mUrl,
			boolean mIsPro, String mShowUrl, String mClickUrl)
	{
		super();
		this.mId = mId;
		this.mTitle = mTitle;
		this.mImage = mImage;
		this.mUrl = mUrl;
		this.mIsPro = mIsPro;
		this.mShowUrl = mShowUrl;
		this.mClickUrl = mClickUrl;
	}

	public YKFocusImage()
	{
		mId = "";
		mTitle = "";
		mImage = new YKImage();
		mUrl = "";
		mIsPro = false;
		mShowUrl = "";
		mClickUrl = "";
		mPosition = Integer.valueOf(0);
		mID = "";

	}

	public int getmBgCount() {
		return mBgCount;
	}

	public void setmBgCount(int mBgCount) {
		this.mBgCount = mBgCount;
	}

	public static YKFocusImage parse(JSONObject object) {
		YKFocusImage focus = new YKFocusImage();
		if (object != null) {
			focus.parseData(object);
		}
		return focus;
	}

	protected void parseData(JSONObject object) {

		super.parseData(object);

		try {
			mTitle = object.getString("title");
		} catch (JSONException e) {
		}

		try {
			mUrl = object.getString("url");
		} catch (JSONException e) {
		}
		try {
			type = object.getInt("type");
		} catch (JSONException e) {
		}
		try {
			JSONObject tmpObject = object.getJSONObject("image");
			mImage = YKImage.parse(tmpObject);
		} catch (JSONException e) {
		}
	}

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("id", mID);
		} catch (Exception e) {
		}
		try {
			object.put("title", mTitle);
		} catch (Exception e) {
		}
		try {
			object.put("url", mUrl);
		} catch (Exception e) {
		}
		try {
			object.put("image", mImage.toJson());
		} catch (Exception e) {
		}

		try {
			object.put("type", type);
		} catch (Exception e) {
		}
		return object;
	}

	@Override
	public String toString() {
		return "YKFocusImage [mTitle=" + mTitle + ", mImage=" + mImage
				+ ", mUrl=" + mUrl + ", mIsPro=" + mIsPro + ", mShowUrl="
				+ mShowUrl + ", mClickUrl=" + mClickUrl + ", mPosition="
				+ mPosition + ", mId=" + mId + ", mBgCount=" + mBgCount + "]";
	}

}
