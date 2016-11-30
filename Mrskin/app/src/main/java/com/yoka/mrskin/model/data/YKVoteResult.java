package com.yoka.mrskin.model.data;

import java.io.Serializable;

import org.json.JSONObject;

public class YKVoteResult implements Serializable{

	private static final long serialVersionUID = 1L;
	private String choice_one_percent;
	private String choice_two_percent;
	private int totalcount;
	private int yobicount;
	public String getChoice_one_percent() {
		return choice_one_percent;
	}
	public void setChoice_one_percent(String choice_one_percent) {
		this.choice_one_percent = choice_one_percent;
	}
	public String getChoice_two_percent() {
		return choice_two_percent;
	}
	public void setChoice_two_percent(String choice_two_percent) {
		this.choice_two_percent = choice_two_percent;
	}
	public int getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}


	public int getYobicount() {
		return yobicount;
	}
	public void setYobicount(int yobicount) {
		this.yobicount = yobicount;
	}
	@Override
	public String toString() {
		return "YKVoteResult [choice_one_percent=" + choice_one_percent
				+ ", choice_two_percent=" + choice_two_percent
				+ ", totalcount=" + totalcount + "]";
	}

	public static YKVoteResult parse(JSONObject object) {
		YKVoteResult vote = new YKVoteResult();
		if (object != null) {
			vote.parseData(object);
		}
		return vote;
	}


	private void parseData(JSONObject object) {

		try {
			choice_one_percent = object.optString("choice_one_percent");
		} catch (Exception e) {
		}

		try {
			choice_two_percent = object.optString("choice_two_percent");
		} catch (Exception e) {
		}

		try {
			totalcount = object.optInt("totalcount");
		} catch (Exception e) {
		}

		try {
			yobicount = object.optInt("yobicount");
		} catch (Exception e) {
		}


	}

}
