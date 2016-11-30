package com.yoka.mrskin.model.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 投票实体
 * @author zlz
 * @Data 2016年8月15日
 */
public class YKVote implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String vote_startdate;
	private String vote_enddate;
	private String pushRecommendDate;
	private String vote_title;
	private String choice_one;
	private String choice_two;
	private String article_links;
	private String article_title;
	private String created;
	private String status;

	private int uservoteflag;
	private String choice_one_percent;
	private String choice_two_percent;
	private int totalcount;
	private int voteflag;



	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVote_startdate() {
		return vote_startdate;
	}
	public void setVote_startdate(String vote_startdate) {
		this.vote_startdate = vote_startdate;
	}
	public String getVote_enddate() {
		return vote_enddate;
	}
	public void setVote_enddate(String vote_enddate) {
		this.vote_enddate = vote_enddate;
	}
	public String getPushRecommendDate() {
		return pushRecommendDate;
	}
	public void setPushRecommendDate(String pushRecommendDate) {
		this.pushRecommendDate = pushRecommendDate;
	}
	public String getVote_title() {
		return vote_title;
	}
	public void setVote_title(String vote_title) {
		this.vote_title = vote_title;
	}
	public String getChoice_one() {
		return choice_one;
	}
	public void setChoice_one(String choice_one) {
		this.choice_one = choice_one;
	}
	public String getChoice_two() {
		return choice_two;
	}
	public void setChoice_two(String choice_two) {
		this.choice_two = choice_two;
	}
	public String getArticle_links() {
		return article_links;
	}
	public void setArticle_links(String article_links) {
		this.article_links = article_links;
	}
	
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


	public String getArticle_title() {
		return article_title;
	}
	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}
	public int getUservoteflag() {
		return uservoteflag;
	}
	public void setUservoteflag(int uservoteflag) {
		this.uservoteflag = uservoteflag;
	}
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
	
	
	public int getVoteflag() {
		return voteflag;
	}
	public void setVoteflag(int voteflag) {
		this.voteflag = voteflag;
	}
	public static YKVote parse(JSONObject object) {
		YKVote vote = new YKVote();
		if (object != null) {
			vote.parseData(object);
		}
		return vote;
	}

	private void parseData(JSONObject object) {


		try {
			id = object.optString("id");
		} catch (Exception e) {
		}

		try {
			vote_startdate = object.getString("vote_startdate");
		} catch (JSONException e) {
		}

		try {
			vote_enddate = object.optString("vote_enddate");
		} catch (Exception e) {
		}

		try {
			pushRecommendDate = object.optString("pushRecommendDate");
		} catch (Exception e) {
		}
		try {
			vote_title = object.optString("vote_title");
		} catch (Exception e) {
		}

		try {
			choice_one = object.optString("choice_one");
		} catch (Exception e) {
		}

		try {
			choice_two = object.optString("choice_two");
		} catch (Exception e) {
		}

		try {
			article_links = object.optString("article_links");
		} catch (Exception e) {
		}

		try {
			article_title = object.optString("article_title");
		} catch (Exception e) {
		}

		try {
			created = object.optString("created");
		} catch (Exception e) {
		}

		try {
			status = object.optString("status");
		} catch (Exception e) {
		}

		try {
			uservoteflag = object.optInt("uservoteflag");
		} catch (Exception e) {
		}

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
			voteflag = object.optInt("voteflag");
		} catch (Exception e) {
		}


	}
	@Override
	public String toString() {
		return "YKVote [id=" + id + ", vote_startdate=" + vote_startdate
				+ ", vote_enddate=" + vote_enddate + ", pushRecommendDate="
				+ pushRecommendDate + ", vote_title=" + vote_title
				+ ", choice_one=" + choice_one + ", choice_two=" + choice_two
				+ ", article_links=" + article_links + ", article_title="
				+ article_title + ", created=" + created + ", status=" + status
				+ ", uservoteflag=" + uservoteflag + ", choice_one_percent="
				+ choice_one_percent + ", choice_two_percent="
				+ choice_two_percent + ", totalcount=" + totalcount
				+ ", voteflag=" + voteflag + "]";
	}
		



}
