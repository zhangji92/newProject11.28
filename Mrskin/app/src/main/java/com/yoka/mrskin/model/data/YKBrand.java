package com.yoka.mrskin.model.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

public class YKBrand extends YKData
{
	/**
	 * 品牌对像
	 */
	private static final long serialVersionUID = 2042642730004150417L;
	private String mName;
	private String mSortLetters;
	private YKImage mThumbnail;
	private YKImage mImg;
	private YKImage mImgBig;
	private ArrayList<YKProduct> mProducts = new ArrayList<YKProduct>();
	public boolean mFlag;
	private String brandprofiles;

	public String getmTitle()
	{
		return mName;
	}

	public void setmTitle(String mTitle)
	{
		this.mName = mTitle;
	}

	public String getmSortLetters()
	{
		return mSortLetters;
	}

	public void setmSortLetters(String mSortLetters)
	{
		this.mSortLetters = mSortLetters;
	}

	public YKImage getmThumbnail()
	{
		return mThumbnail;
	}

	public void setmThumbnail(YKImage mThumbnail)
	{
		this.mThumbnail = mThumbnail;
	}

	public YKImage getmImg() {
		return mImg;
	}

	public void setmImg(YKImage mImg) {
		this.mImg = mImg;
	}

	public YKImage getmImgBig() {
		return mImgBig;
	}

	public void setmImgBig(YKImage mImgBig) {
		this.mImgBig = mImgBig;
	}

	public ArrayList<YKProduct> getmProducts() {
		return mProducts;
	}

	public void setmProducts(ArrayList<YKProduct> mProducts) {
		this.mProducts = mProducts;
	}

	public boolean ismFlag() {
		return mFlag;
	}

	public void setmFlag(boolean mFlag) {
		this.mFlag = mFlag;
	}


	public String getBrandprofiles() {
		return brandprofiles;
	}

	public void setBrandprofiles(String brandprofiles) {
		this.brandprofiles = brandprofiles;
	}

	public YKBrand(String mTitle, String mSortLetters, YKImage mThumbnail,
			YKImage mImg, YKImage mImgBig, ArrayList<YKProduct> mProducts,
			boolean mFlag)
	{
		super();
		this.mName = mTitle;
		this.mSortLetters = mSortLetters;
		this.mThumbnail = mThumbnail;
		this.mImg = mImg;
		this.mImgBig = mImgBig;
		this.mProducts = mProducts;
		this.mFlag = mFlag;
	}

	public YKBrand()
	{
		super();
	}

	public static YKBrand parse(JSONObject object) {
		YKBrand brand = new YKBrand();
		if (object != null) {
			brand.parseData(object);
		}
		return brand;
	}

	protected void parseData(JSONObject object) {
		super.parseData(object);

		try {
			mName = object.optString("name");
		} catch (Exception e) {
		}
		try {
			mSortLetters = object.optString("sortletters");
		} catch (Exception e) {
		}
		try {
			brandprofiles = object.optString("brandprofiles");
		} catch (Exception e) {
		}


		try {
			JSONObject tmpObject  = object.optJSONObject("thumbnail");
			mThumbnail = YKImage.parse(tmpObject);
		} catch (Exception e) {
		}

		try {
			JSONObject tmpObject = object.optJSONObject("img");
			mImg = YKImage.parse(tmpObject);
		} catch (Exception e) {
		}

		try {
			JSONObject tmpObject = object.optJSONObject("img_big");
			mImgBig = YKImage.parse(tmpObject);
		} catch (Exception e) {
		}

		try {
			JSONArray tmpArray = object.optJSONArray("product");
			if (tmpArray != null) {
				for (int i = 0; i < tmpArray.length(); ++i) {
					mProducts.add(YKProduct.parse(tmpArray.getJSONObject(i)));
				}
			}
		} catch (Exception e) {
		}
	}
	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("name", mName);
		} catch (Exception e){}
		try {
			object.put("sortletters", mSortLetters);
		} catch (Exception e){}
		try {
			object.put("brandprofiles", brandprofiles);
		} catch (Exception e){}
		try {
			object.put("thumbnail", mThumbnail.toJson());
		} catch (Exception e){}
		try {
			object.put("img", mImg.toJson());
		} catch (Exception e){}
		try {
			object.put("img_big", mImgBig.toJson());
		} catch (Exception e){}
		try {
			object.put("product",toArrayJson(mProducts));
		} catch (Exception e){}

		return object;
	}
	/**
	 * list 集合转换 JSONArray（用于缓存）--zlz
	 * @param list
	 * @return
	 */
	private JSONArray toArrayJson(ArrayList<YKProduct> list){
		JSONArray arrayJson = new JSONArray();
		for (YKProduct product : list) {
			arrayJson.put(product.toJson());
		}
		return arrayJson;
	}

	@Override
	public String toString() {
		return "YKBrand [mName=" + mName + ", mSortLetters=" + mSortLetters
				+ ", mThumbnail=" + mThumbnail + ", mImg=" + mImg
				+ ", mImgBig=" + mImgBig + ", mProducts=" + mProducts
				+ ", mFlag=" + mFlag + "]";
	}


}
