package com.yoka.mrskin.model.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yoka.mrskin.model.base.YKData;

public class YKRecommendation extends YKData
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6196908463127022066L;
	/**
	 * flag
	 */
	private YKFlag mFlag;
	/**
	 * Content
	 */
	private ArrayList<YKContent> mContent;
	/**
	 * 记录返回天数
	 */
	private String mPage;

	private YKVote vote;

	public YKFlag getmFlag()
	{
		return mFlag;
	}

	public void setmFlag(YKFlag mFlag)
	{
		this.mFlag = mFlag;
	}

	public ArrayList<YKContent> getmContent()
	{
		return mContent;
	}

	public void setmContent(ArrayList<YKContent> mContent)
	{
		this.mContent = mContent;
	}

	public String getmPage()
	{
		return mPage;
	}

	public void setmPage(String mPage)
	{
		this.mPage = mPage;
	}



	public YKVote getVote() {
		return vote;
	}

	public void setVote(YKVote vote) {
		this.vote = vote;
	}

	public YKRecommendation(YKFlag mFlag, ArrayList<YKContent> mContent,
			String mPage)
	{
		super();
		this.mFlag = mFlag;
		this.mContent = mContent;
		this.mPage = mPage;
	}

	public YKRecommendation()
	{
		super();
	}

	public static YKRecommendation parse(JSONObject object) {
		YKRecommendation experience = new YKRecommendation();
		if (object != null) {
			experience.parseData(object);
		}
		return experience;
	}

	protected void parseData(JSONObject object) {

		super.parseData(object);
		try {
			JSONObject tmpObject = object.getJSONObject("flag");
			mFlag = YKFlag.parse(tmpObject);
		} catch (JSONException e) {
		}
		//解析投票
		try {
			JSONObject tmpObject = object.getJSONObject("vote");
			Log.d("voteJson", tmpObject.toString());
			vote = YKVote.parse(tmpObject);
		} catch (JSONException e) {
		}

		try {
			mPage = object.getString("page");
		} catch (JSONException e) {
		}

		try {
			JSONArray data = object.getJSONArray("data");
			if (data == null) {
				return;
			}
			for (int i = 0; i < data.length(); i++) {
				if (mContent == null) {
					mContent = new ArrayList<YKContent>();

				}
				mContent.add(YKContent.parse(data.getJSONObject(i)));
			}
		} catch (JSONException e) {
		}
	}

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("id", mID);
		} catch (Exception e){}
		try {
			object.put("flag", mFlag);
		} catch (Exception e){}
		try {
			object.put("vote", vote);
		} catch (Exception e){}
		try {
			object.put("content", mContent);
		} catch (Exception e){}
		try {
			object.put("page", mPage);
		} catch (Exception e){}
		return object;
	}
}
