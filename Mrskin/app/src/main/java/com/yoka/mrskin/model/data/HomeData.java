/**
 * 
 */
package com.yoka.mrskin.model.data;

import java.io.Serializable;

import com.adsame.main.AdsameBannerAd;

/**
 * @author zlz
 * @date 2016年6月15日
 */
public class HomeData implements Serializable{
	private YKNewExperience expert;
	private YKTopicData topic;
	private YKFlag ykFlag;
	private String title;
	private YKVote vote;//投票
	private String bannerAdId;

	private int type; // 1:ykFlag 2:YKTopicData 3:YKNewExperience 4:title 5:vote 6:Ad


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public YKNewExperience getExpert() {
		return expert;
	}
	public void setExpert(YKNewExperience expert) {
		this.expert = expert;
	}
	public YKTopicData getTopic() {
		return topic;
	}
	public void setTopic(YKTopicData topic) {
		this.topic = topic;
	}
	public YKFlag getYkFlag() {
		return ykFlag;
	}
	public void setYkFlag(YKFlag ykFlag) {
		this.ykFlag = ykFlag;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}


	public YKVote getVote() {
		return vote;
	}
	public void setVote(YKVote vote) {
		this.vote = vote;
	}



	public String getBannerAdId() {
		return bannerAdId;
	}
	public void setBannerAdId(String bannerAdId) {
		this.bannerAdId = bannerAdId;
	}
	@Override
	public String toString() {
		return "HomeData [expert=" + expert + ", topic=" + topic + ", ykFlag="
				+ ykFlag + ", title=" + title + ", vote=" + vote + ", type="
				+ type + "]";
	}






}
