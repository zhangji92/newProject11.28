package com.yoka.mrskin.model.data;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;

/**
 * 产品名称
 * 
 * @author Y H L
 * 
 */
@SuppressWarnings("serial")
public class YKProduct extends YKData implements Serializable
{

	/**
	 * 商品名称
	 */
	private String mTitle;

	/**
	 * 商品图片
	 */
	private YKImage product_image;

	/**
	 * 商品图片
	 */
	private YKImage mCover;

	/**
	 * 商品描述
	 */
	private String mDescription;

	/**
	 * 商品评级
	 */
	private YKSpecification mSpecification;
	/**
	 * 商品标签数组
	 */
	private ArrayList<String> mTags;

	/**
	 *商品介绍 
	 */
	private String mDescription_url;

	/**
	 * 商品评级
	 */
	private Float rating;

	public String getTitle()
	{
		return mTitle;
	}

	public void setTitle(String title)
	{
		this.mTitle = title;
	}

	public YKImage getCover() {
		return mCover;
	}

	public Float getRating()
	{
		return rating;
	}

	public void setRating(Float rating)
	{
		this.rating = rating;
	}

	public YKImage getProduct_image()
	{
		return product_image;
	}

	public void setProduct_image(YKImage product_image)
	{
		this.product_image = product_image;
	}

	public String getDescription()
	{
		return mDescription;
	}

	public void setDescription(String description)
	{
		this.mDescription = description;
	}

	public YKSpecification getSpecification()
	{
		return mSpecification;
	}

	public void setSpecification(YKSpecification specification)
	{
		this.mSpecification = specification;
	}

	public ArrayList<String> getTags()
	{
		return mTags;
	}

	public void setTags(ArrayList<String> tags)
	{
		this.mTags = tags;
	}

	public String getDescription_url()
	{
		return mDescription_url;
	}

	public void setDescription_url(String description_url)
	{
		this.mDescription_url = description_url;
	}

	public YKProduct(String title, YKImage product_image, YKImage mCover,
			String description, YKSpecification specification,
			ArrayList<String> tags, String description_url, Float rating)
	{
		super();
		this.mTitle = title;
		this.product_image = product_image;
		this.mCover = mCover;
		this.mDescription = description;
		this.mSpecification = specification;
		this.mTags = tags;
		this.mDescription_url = description_url;
		this.rating = rating;
	}

	public YKProduct()
	{
		super();
	}

	public static YKProduct parse(JSONObject object) {
		YKProduct product = new YKProduct();
		if (object != null) {
			product.parseData(object);
		}
		return product;
	}

	protected void parseData(JSONObject object) {

		super.parseData(object);

		try {
			mTitle = object.getString("title");
		} catch (JSONException e) {
		}
		try {
			JSONObject tmpObject = object.getJSONObject("product_image");
			product_image = YKImage.parse(tmpObject);
		} catch (JSONException e) {
		}

		try {
			JSONObject tmpObject = object.getJSONObject("cover");
			mCover = YKImage.parse(tmpObject);
		} catch (JSONException e) {
		}

		try {
			JSONObject tmpObject = object.getJSONObject("specification");
			mSpecification = YKSpecification.parse(tmpObject);
		} catch (JSONException e) {
		}
		try {
			mDescription = object.getString("description");
		} catch (JSONException e) {
		}
		try {
			mDescription_url = object.getString("description_url");
		} catch (JSONException e) {
		}
		try {
			rating = (float) object.getDouble("rating");
		} catch (JSONException e) {
		}
		try {
			JSONArray data = object.getJSONArray("tags");
			if (data == null) {
				return;
			}
			for (int i = 0; i < data.length(); i++) {
				//                JSONObject object2 = data.getJSONObject(i);
				String label = data.getString(i);
				//                if (object2 == null) {
				//                    continue;
				//                }
				if (mTags == null) {
					mTags = new ArrayList<String>();
				}
				mTags.add(label);
			}
		} catch (JSONException e) {
		}
	}

	public JSONObject toJson(){
		JSONObject object = new JSONObject();
		try {
			object.put("title", mTitle);
		} catch (Exception e){}
		try {
			object.put("product_image", product_image.toJson());
		} catch (Exception e){}
		try {
			object.put("cover", mCover.toJson());
		} catch (Exception e){}
		try {
			object.put("specification", mSpecification.toJson());
		} catch (Exception e){}
		try {
			object.put("description_url", mDescription_url);
		} catch (Exception e){}
		try {
			object.put("rating", rating);
		} catch (Exception e){}
		try {
			object.put("tags", toArrayJson(mTags));
		} catch (Exception e){}
		return object;
	}

	/**
	 * list 集合转换 JSONArray（用于缓存）--zlz
	 * @param list
	 * @return
	 */
	private JSONArray toArrayJson(ArrayList<String> list){
		JSONArray arrayJson = new JSONArray();
		for (String str : list) {
			arrayJson.put(str);
		}
		return arrayJson;
	}
	@Override
	public String toString() {
		return "YKProduct [mTitle=" + mTitle + ", product_image="
				+ product_image + ", mCover=" + mCover + ", mDescription="
				+ mDescription + ", mSpecification=" + mSpecification
				+ ", mTags=" + mTags + ", mDescription_url=" + mDescription_url
				+ ", rating=" + rating + "]";
	}


}
