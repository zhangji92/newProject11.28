package com.yoka.mrskin.model.data;

import java.io.Serializable;
/**
 * 美丽计划本地存储
 * @author zlz
 * @Data 2016年8月8日
 */
public class PlanTask implements Serializable{
	private int uid;
	private int taskId;
	private int indexTime;


	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getIndexTime() {
		return indexTime;
	}
	public void setIndexTime(int indexTime) {
		this.indexTime = indexTime;
	}
	@Override
	public String toString() {
		return "PlanTask [uid=" + uid + ", taskId=" + taskId + ", indexTime="
				+ indexTime + "]";
	}




}
