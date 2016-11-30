package com.yoka.mrskin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.managers.task.TimeUtil;
import com.yoka.mrskin.util.CharacterFormat;
/**
 * 计划详情页(时间表)
 */
public class TaskBriefActivity extends BaseActivity
{
	private LinearLayout mBack;
	private YKTask mTask;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_brief);
		YKActivityManager.getInstance().addActivity(this);
		initData();
		init();
	}

	private void init() {
		mBack = (LinearLayout) findViewById(R.id.task_brief_back_layout);
		mListView = (ListView) findViewById(R.id.task_brief_lv);

		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mListView.setAdapter(new TaskAdapter());
	}

	private void initData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		YKTask task = (YKTask) intent.getSerializableExtra("task");
		if (task == null) {
			return;
		}
		mTask = task;
		Log.d("taskdeta", mTask.toString());
	}

	private class TaskAdapter extends BaseAdapter
	{
		private ViewHolder viewHolder = null;

		@Override
		public int getCount() {
			if (mTask == null) {
				return 0;
			}
			if (mTask.getmSubtask() == null) {
				return 0;
			}
			return mTask.getmSubtask().size();
		}

		@Override
		public Object getItem(int position) {
			if (mTask == null) {
				return null;
			}
			if (mTask.getmSubtask() == null) {
				return null;
			}
			return mTask.getmSubtask().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (mTask == null) {
				return null;
			}

			if (convertView == null) {
				convertView = LayoutInflater.from(TaskBriefActivity.this)
						.inflate(R.layout.task_brief_lv_test, null);

				viewHolder = new ViewHolder();
				viewHolder.mHeader = (RelativeLayout) convertView
						.findViewById(R.id.task_brief_header_layout);
				viewHolder.mTaskDay = (TextView) convertView
						.findViewById(R.id.task_brief_daytext);
				viewHolder.mTaskTime = (TextView) convertView
						.findViewById(R.id.task_brief_time);
				viewHolder.mTaskTitle = (TextView) convertView
						.findViewById(R.id.task_brief_timebrief);
				viewHolder.mView = (View) convertView
						.findViewById(R.id.task_brief_view);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			YKTask subTask = mTask.getmSubtask().get(position);
			boolean isTheDayFirst = subTask.ismTheDayFirst();// 是否是当天第一条，

			if(mTask.getmCycleTime() == -1){
				viewHolder.mHeader.setVisibility(View.VISIBLE);
				viewHolder.mTaskDay.setText("每日计划");
				viewHolder.mView.setVisibility(View.GONE);//？？？？？
			}else {
				//不再合并同一天的任务---zlz(v2.5.1)
				//				if (isTheDayFirst) {
				viewHolder.mHeader.setVisibility(View.VISIBLE);
				int timeindex = subTask.getmIndexTime();// 第几天
				viewHolder.mTaskDay.setText(getString(R.string.task_d)
						+ timeindex + getString(R.string.task_day));
				viewHolder.mView.setVisibility(View.GONE);
			}
			//			} else {
			//				viewHolder.mHeader.setVisibility(View.GONE);
			//				viewHolder.mView.setVisibility(View.GONE);
			//			}
			long time = 0;
			if (mTask.getmCycleTime() == -1) {//每日计划
				time = TimeUtil.getTodayZero()
						+ subTask.getmRemindTime().getmMills();
			} else {
				time = subTask.getmRemindTime().getmMills();
			}
			viewHolder.mTaskTime.setText(TimeUtil
					.forTimeForHourAndSeconed(time));
			viewHolder.mTaskTitle.setText(subTask.getmTitle());
			return convertView;
		}

	}

	class ViewHolder
	{
		private TextView mTaskDay, mTaskTime, mTaskTitle;
		private RelativeLayout mHeader;
		private View mView;
	}

	/**
	 * 友盟统计需要的俩个方法
	 */
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("TaskBriefActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		JPushInterface.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("TaskBriefActivity"); // 保证 onPageEnd
		// 在onPause
		// 之前调用,因为 onPause
		// 中会保存信息
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		YKActivityManager.getInstance().removeActivity(this);
	}
}
