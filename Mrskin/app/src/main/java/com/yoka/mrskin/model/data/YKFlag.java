package com.yoka.mrskin.model.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;
/**
 * falag
 */
public class YKFlag extends YKData
{
	private YKDate date;
	private String brief;

	public YKDate getDate()
	{
		return date;
	}

	public void setDate(YKDate date)
	{
		this.date = date;
	}

	public String getBrief()
	{
		return brief;
	}

	public void setBrief(String brief)
	{
		this.brief = brief;
	}

	public YKFlag(YKDate date, String brief)
	{
		super();
		this.date = date;
		this.brief = brief;
	}

	public YKFlag()
	{
		super();
	}

	public static YKFlag parse(JSONObject object) {
		YKFlag flag = new YKFlag();
		if (object != null) {
			flag.parseData(object);
		}
		return flag;
	}

	protected void parseData(JSONObject object) {

		super.parseData(object);
		JSONObject tmpObject = null;

		try {
			tmpObject = object.optJSONObject("date");
			date = YKDate.parse(tmpObject);
		} catch (Exception e) {
		}

		try {
			brief = object.getString("brief");
		} catch (JSONException e) {
		}
	}
}
