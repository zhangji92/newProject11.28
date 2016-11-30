package com.yoka.mrskin.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKData;
import com.yoka.mrskin.model.managers.task.TimeUtil;
import com.yoka.mrskin.model.managers.task.YKTaskManager;

@SuppressWarnings("serial")
public class YKTask extends YKData implements Serializable
{
	private static int STATUS_NOTSTART = 10;// 未开始
	@SuppressWarnings("unused")
	private static int STATUS_ONGOING = 20;// 进行中/待完成
	private static int STATUS_WIN = 30;// 已完成
	private static int STATUS_LOST = 40;// 未完成
	/**
	 * 任务标题
	 */
	private String mTitle;
	/**
	 * 任务简介
	 */
	private String mDesc;
	/**
	 * 提醒文案
	 */
	private String mRemindContent;
	/**
	 * 开始时间
	 */
	private YKDate mRemindTime;
	/**
	 * 任务周期
	 */
	private int mCycleTime;
	/**
	 * 任务分数
	 */
	private int mScore;
	/**
	 * 任务状态
	 */
	private int mStatus;
	/**
	 * 参与人数
	 */
	private int mUsercount;
	/**
	 * 是否提醒
	 */
	private boolean mCanreMind;
	/**
	 * 图片简介
	 */
	private int[] mCoverDesc;
	/**
	 * 图片数据
	 */
	private YKImage mCoverImage;
	/**
	 * 是否已添加
	 */
	private boolean mIsAdd;
	/**
	 * 作者姓名
	 */
	private String authorName;
	/**
	 * 父任务id
	 */
	private String mParentId;
	/**
	 * 天(返回属于第几天)
	 */
	private int mIndexTime;
	/**
	 * Task类型的数据对象
	 */
	private ArrayList<YKTask> mSubtask;

	/**
	 * 子任务是否是当天第一个任务，客户端自用字段，不上传给服务器
	 */
	private boolean mTheDayFirst;

	/**
	 * 相关文章
	 */
	private ArrayList<YKTopicData> related_topics;

	/**
	 * 相关商品
	 */
	private ArrayList<YKProduct> related_products;

	public ArrayList<YKTopicData> getRelated_topics() {
		return related_topics;
	}

	public void setRelated_topics(ArrayList<YKTopicData> related_topics) {
		this.related_topics = related_topics;
	}

	public ArrayList<YKProduct> getRelated_products() {
		return related_products;
	}

	public void setRelated_products(ArrayList<YKProduct> related_products) {
		this.related_products = related_products;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmDesc() {
		return mDesc;
	}

	public void setmDesc(String mDesc) {
		this.mDesc = mDesc;
	}

	public String getmRemindContent() {
		return mRemindContent;
	}

	public void setmRemindContent(String mRemindContent) {
		this.mRemindContent = mRemindContent;
	}

	public YKDate getmRemindTime() {
		return mRemindTime;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public void setmRemindTime(YKDate mRemindTime) {
		this.mRemindTime = mRemindTime;
	}

	public int getmCycleTime() {
		return mCycleTime;
	}

	public void setmCycleTime(int mCycleTime) {
		this.mCycleTime = mCycleTime;
	}

	public int getmScore() {
		return mScore;
	}

	public void setmScore(int mScore) {
		this.mScore = mScore;
	}

	public int getmStatus() {
		return mStatus;
	}

	public void setmStatus(int mStatus) {
		this.mStatus = mStatus;
	}

	public int getmUsercount() {
		return mUsercount;
	}

	public void setmUsercount(int mUsercount) {
		this.mUsercount = mUsercount;
	}

	public boolean ismCanreMind() {
		return mCanreMind;
	}

	public void setmCanreMind(boolean mCanreMind) {
		this.mCanreMind = mCanreMind;
	}

	public int[] getmCoverDesc() {
		return mCoverDesc;
	}

	public void setmCoverDesc(int[] mCoverDesc) {
		this.mCoverDesc = mCoverDesc;
	}

	public YKImage getmCoverImage() {
		return mCoverImage;
	}

	public void setmCoverImage(YKImage mCoverImage) {
		this.mCoverImage = mCoverImage;
	}

	public boolean ismIsAdd() {
		return mIsAdd;
	}

	public void setmIsAdd(boolean mIsAdd) {
		this.mIsAdd = mIsAdd;
	}

	public String getmParentId() {
		return mParentId;
	}

	public void setmParentId(String mParentId) {
		this.mParentId = mParentId;
	}

	public int getmIndexTime() {
		return mIndexTime;
	}

	public void setmIndexTime(int mIndexTime) {
		this.mIndexTime = mIndexTime;
	}

	public ArrayList<YKTask> getmSubtask() {
		return mSubtask;
	}

	public void setmSubtask(ArrayList<YKTask> mSubtask) {
		this.mSubtask = mSubtask;
	}

	public YKTask()
	{
		super();
		mCanreMind = true;
		mRemindTime = new YKDate();
	}

	public static YKTask parse(JSONObject object) {
		YKTask task = new YKTask();
		if (object != null) {
			task.parseData(object);
		}
		return task;
	}

	public YKTask(String mTitle, String mDesc, String mRemindContent,
			YKDate mRemindTime, int mCycleTime, int mScore, int mStatus,
			int mUsercount, boolean mCanreMind, int[] mCoverDesc,
			YKImage mCoverImage, boolean mIsAdd, String authorName,
			String mParentId, int mIndexTime, ArrayList<YKTask> mSubtask,
			boolean mTheDayFirst, ArrayList<YKTopicData> related_topics,
			ArrayList<YKProduct> related_products)
	{
		super();
		this.mTitle = mTitle;
		this.mDesc = mDesc;
		this.mRemindContent = mRemindContent;
		this.mRemindTime = mRemindTime;
		this.mCycleTime = mCycleTime;
		this.mScore = mScore;
		this.mStatus = mStatus;
		this.mUsercount = mUsercount;
		this.mCanreMind = mCanreMind;
		this.mCoverDesc = mCoverDesc;
		this.mCoverImage = mCoverImage;
		this.mIsAdd = mIsAdd;
		this.authorName = authorName;
		this.mParentId = mParentId;
		this.mIndexTime = mIndexTime;
		this.mSubtask = mSubtask;
		this.mTheDayFirst = mTheDayFirst;
		this.related_topics = related_topics;
		this.related_products = related_products;
	}

	private ArrayList<YKTask> sortHomeCardData(ArrayList<YKTask> sourceListData) {
		if (sourceListData == null) {
			return null;
		}
		ArrayList<YKTask> taskList = new ArrayList<YKTask>();
		for (int i = 0; i < sourceListData.size(); i++) {
			YKTask sourceData = sourceListData.get(i);
			if (sourceData == null) {
				continue;
			}
			boolean isAdd = false;
			for (int j = 0; j < taskList.size(); j++) {
				if (taskList.get(j).getmRemindTime().getmMills() > sourceData
						.getmRemindTime().getmMills()) {
					taskList.add(j, sourceData);
					isAdd = true;
					break;
				}
			}
			if (!isAdd) {
				taskList.add(sourceData);
			}

		}
		return taskList;
	}

	protected void parseData(JSONObject object) {
		try {
			JSONArray subTask = object.getJSONArray("subtask");
			if (subTask == null) {
				return;
			}
			for (int i = 0; i < subTask.length(); i++) {
				JSONObject object2 = subTask.getJSONObject(i);
				if (object2 == null) {
					continue;
				}
				if (mSubtask == null) {
					mSubtask = new ArrayList<YKTask>();
				}
				YKTask task = YKTask.parse(object2);
				mSubtask.add(task);
			}
			if (mSubtask != null) {
				int currentIndex = -1;
				mSubtask = sortHomeCardData(mSubtask);
				for (int i = 0; i < mSubtask.size(); i++) {
					YKTask task = mSubtask.get(i);
					if (task.getmIndexTime() != currentIndex) {
						task.setmTheDayFirst(true);
						currentIndex = task.getmIndexTime();
					} else {
						task.setmTheDayFirst(false);
					}

				}
			}
		} catch (JSONException e) {
		}
		try {
			JSONArray data = object.getJSONArray("related_products");
			if (data == null) {
				return;
			}
			for (int i = 0; i < data.length(); i++) {
				JSONObject objectp = data.getJSONObject(i);
				if (objectp == null) {
					continue;
				}
				if (related_products == null) {
					related_products = new ArrayList<YKProduct>();
				}
				related_products.add(YKProduct.parse(objectp));
			}
		} catch (JSONException e) {
		}

		try {
			JSONArray data = object.getJSONArray("related_topics");
			if (data == null) {
				return;
			}
			for (int i = 0; i < data.length(); i++) {
				JSONObject objectt = data.getJSONObject(i);
				if (objectt == null) {
					continue;
				}
				if (related_topics == null) {
					related_topics = new ArrayList<YKTopicData>();
				}
				related_topics.add(YKTopicData.parse(objectt));
			}
		} catch (JSONException e) {
		}
		super.parseData(object);

		try {
			mTitle = object.getString("title");
		} catch (JSONException e) {
		}
		try {
			mDesc = object.getString("desc");
		} catch (JSONException e) {
		}
		try {
			authorName = object.getString("author_name");
		} catch (JSONException e) {
		}
		try {
			mRemindContent = object.getString("remind_content");
		} catch (JSONException e) {
		}
		try {
			JSONObject tmpObject = object.getJSONObject("remind_time");
			mRemindTime = YKDate.parse(tmpObject);
		} catch (JSONException e) {
		}
		try {
			mCycleTime = object.optInt("cycle_time");
		} catch (Exception e) {
		}
		try {
			mScore = object.optInt("score");
		} catch (Exception e) {
		}
		try {
			mStatus = object.optInt("status");
		} catch (Exception e) {
		}
		try {
			mUsercount = object.optInt("usercount");
		} catch (Exception e) {
		}
		try {
			mCanreMind = object.optBoolean("can_remind", true);
		} catch (Exception e) {
		}
		/**
		 * 有疑惑。。。
		 */
		try {
			JSONArray data = object.getJSONArray("cover_desc");
			if (data == null) {
				return;
			}
			mCoverDesc = new int[data.length()];
			for (int i = 0; i < data.length(); i++) {
				mCoverDesc[i] = data.getInt(i);
			}
		} catch (JSONException e) {
		}
		try {
			JSONObject tmpObject = object.getJSONObject("cover_image");
			mCoverImage = YKImage.parse(tmpObject);
		} catch (JSONException e) {
		}
		try {
			mIsAdd = object.optBoolean("is_add");
		} catch (Exception e) {
		}
		try {
			mParentId = object.optString("parent_id");
		} catch (Exception e) {
		}
		try {
			mIndexTime = object.optInt("index_time");
		} catch (Exception e) {
		}
	}

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("id", mID);
		} catch (Exception e){}
		try {
			object.put("title", mTitle);
		} catch (Exception e){}
		try {
			object.put("desc", mDesc);
		} catch (Exception e) {}
		try {
			object.put("remind_content", mRemindContent);
		} catch (Exception e) {}
		try {
			object.put("cycle_time", mCycleTime);
		} catch (Exception e) {}
		try {
			object.put("score", mScore);
		} catch (Exception e) {}
		try {
			object.put("status", mStatus);
		} catch (Exception e) {}
		try {
			object.put("usercount", mUsercount);
		} catch (Exception e) {}
		try {
			object.put("can_remind", mCanreMind);
		} catch (Exception e) {}
		try {
			object.put("is_add", mIsAdd);
		} catch (Exception e) {}
		try {
			object.put("parent_id", mParentId);
		} catch (Exception e) {}
		try {
			object.put("index_time", mIndexTime);
		} catch (Exception e) {}
		// object.put("remind_time", mRemindTime);
		// object.put("cover_desc", mCoverDesc);
		// object.put("cover_image", mCoverImage);
		// object.put("subtask", mSubtask);
		if (mSubtask != null) {
			JSONArray array = new JSONArray();
			for (int i = 0; i < mSubtask.size(); i++) {
				YKTask tmpTask = mSubtask.get(i);
				array.put(tmpTask.toJson());
			}
			try {
				object.put("subtask", array);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (mCoverImage != null) {
			try {
				object.put("cover_image", mCoverImage.toJson());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (mRemindTime != null) {
			try {
				object.put("remind_time", mRemindTime.toJson());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (mCoverDesc != null) {
			JSONArray array = new JSONArray();
			for (int i = 0; i < mCoverDesc.length; i++) {
				array.put(mCoverDesc[i]);
			}
			try {
				object.put("cover_desc", array);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	public boolean ismTheDayFirst() {
		return mTheDayFirst;
	}

	public void setmTheDayFirst(boolean mTheDayFirst) {
		this.mTheDayFirst = mTheDayFirst;
	}

	public boolean isFinished() {
		if (mSubtask != null) {
			if (mSubtask.size() > 0) {
				YKTask task = mSubtask.get(mSubtask.size() - 1);
				return (task.isFinished()); 
			} else {
				return true;
			}
		}
		return mStatus == STATUS_WIN;
	}

	public void finish() {
		if (mSubtask != null && mSubtask.size() > 0) {

		} else {
			YKTaskManager.getInstnace().updateSubTaskStatu(mParentId, getID(),
					YKTask.STATUS_WIN);
		}
	}

	public boolean isFailed() {
		return mStatus == STATUS_LOST;
	}

	public void resetTaskStatus() {
		mStatus = STATUS_NOTSTART;
	}

	public boolean isOverdued(boolean isCycleTask) {
		long now = System.currentTimeMillis();
		String currentHour = TimeUtil.forTimeForHour(now);
		long mTaskTime = mRemindTime.getmMills();
		if (!isCycleTask) {
			mTaskTime += TimeUtil.getNextDayZero();
		}
		String taskRemindTime = TimeUtil.forTimeForHour(mTaskTime);
		int currentHourInt = Integer.parseInt(currentHour);
		int taskRemindTimeInt = Integer.parseInt(taskRemindTime);
		if (currentHourInt > taskRemindTimeInt) {
			return true;
		} else if (currentHourInt < taskRemindTimeInt) {
			return false;
		} else {
			String currentMinute = TimeUtil.forTimeForMinute(now);
			String taskRemindMinute = TimeUtil.forTimeForMinute(mTaskTime);
			int currentMinuteInt = Integer.parseInt(currentMinute);
			int taskRemindMinuteInt = Integer.parseInt(taskRemindMinute);
			if (currentMinuteInt > taskRemindMinuteInt) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void setStatus(int status) {
		mStatus = status;
	}

	public int getStatus() {
		return mStatus;
	}

	@Override
	public String toString() {
		return "YKTask [mTitle=" + mTitle + ", mDesc=" + mDesc
				+ ", mRemindContent=" + mRemindContent + ", mRemindTime="
						+ mRemindTime + ", mCycleTime=" + mCycleTime + ", mScore="
								+ mScore + ", mStatus=" + mStatus + ", mUsercount="
										+ mUsercount + ", mCanreMind=" + mCanreMind + ", mCoverDesc="
												+ Arrays.toString(mCoverDesc) + ", mCoverImage=" + mCoverImage
												+ ", mIsAdd=" + mIsAdd + ", authorName=" + authorName
												+ ", mParentId=" + mParentId + ", mIndexTime=" + mIndexTime
												+ ", mSubtask=" + mSubtask + ", mTheDayFirst=" + mTheDayFirst
												+ ", related_topics=" + related_topics + ", related_products="
												+ related_products + "]";
	}



}
