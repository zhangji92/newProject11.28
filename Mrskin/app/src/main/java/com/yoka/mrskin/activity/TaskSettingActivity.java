package com.yoka.mrskin.activity;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.model.data.YKDate;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.managers.task.TimeUtil;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.model.managers.task.YKTaskManager.HomeCardData;
import com.yoka.mrskin.util.AlarmHelper;
import com.yoka.mrskin.util.YKUtil;

/**
 * 任务设置页
 * 
 * @author yuhailong@yoka.com
 * 
 */
public class TaskSettingActivity extends BaseActivity implements
OnClickListener
{

	private YKTask mTask;
	private CheckBox mAlarm;
	private LinearLayout mBack;
	private ListView mListView;
	private MyAdapter mAdapter;
	private Calendar mCalendar = Calendar.getInstance();
	private String mSubTaskId = null;
	private ArrayList<String> mSubTaskSubTaskId = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_remind_setting);
		YKActivityManager.getInstance().addActivity(this);
		initData();
		init();
	}

	private void init() {
		mAlarm = (CheckBox) findViewById(R.id.task_setting_open);
		if (mTask != null) {
			YKTask newTask = YKTaskManager.getInstnace().getTaskById(mTask.getID());
			mAlarm.setChecked(newTask.ismCanreMind());
		}
		mAlarm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				updateTaskCanRemind(isChecked);
			}
		});
		mListView = (ListView) findViewById(R.id.card_setting_remind);
		mListView.setDivider(new ColorDrawable(0xffFAFAFA));
		mListView.setDividerHeight(YKUtil.dip2px(this, 1));
		mBack = (LinearLayout) findViewById(R.id.setting_remind_back_layout);
		mBack.setOnClickListener(this);

		mAdapter = new MyAdapter();
		mListView.setAdapter(mAdapter);

	}

	private void updateTaskCanRemind(boolean canRemind) {
		if (mTask == null) {
			return;
		}
		YKTaskManager.getInstnace().updateTaskCanRemind(mTask.getID(),
				canRemind);
		YKTaskManager.getInstnace().notifyTaskDataChanged();
	}
	/**
	 * 更新子任务提醒时间
	 * @param subtask
	 * @param time
	 */
	private void updateSubTaskRemindTime(YKTask subtask, long time) {
		if (mTask == null) {
			return;
		}
		// long todayZero = TimeUtil.getTodayZero();
		// if (mTask.getmCycleTime() == -1
		// && subtask.getmRemindTime().getmMills() < todayZero) {
		// time -= todayZero;
		// }

		//
		YKTaskManager.getInstnace().updateSubtaskRemindTime(mTask.getID(),
				subtask.getID(), time);

		ArrayList<HomeCardData> data = YKTaskManager.getInstnace()
				.getHomeCardData();
		AlarmHelper.getInstance().openAlarm(data);

		YKTaskManager.getInstnace().notifyTaskDataChanged();
	}

	private class MyAdapter extends BaseAdapter
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (mTask == null) {
				return null;
			}

			if (convertView == null) {
				convertView = LayoutInflater.from(TaskSettingActivity.this)
						.inflate(R.layout.task_remind_setting_test, null);

				viewHolder = new ViewHolder();
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.task_remind_setting_time);
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.task_remind_setting_title);
				viewHolder.year = (TextView) convertView
						.findViewById(R.id.task_setting_yeartime);
				viewHolder.yearLayout = (RelativeLayout) convertView
						.findViewById(R.id.setting_task_tearlayout);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final YKTask subTask = mTask.getmSubtask().get(position);
			long time = 0;
			viewHolder.time.setText(TimeUtil.forTimeForHourAndSeconed(time));
			viewHolder.title.setText(subTask.getmTitle());
			// if (mTask.getmCycleTime() == -1) {
			time = subTask.getmRemindTime().getmMills();
			System.out.println("tasksettingactivity modify time = " + time);
			// } else {
			// time = TimeUtil.getTodayZero()
			// + subTask.getmRemindTime().getmMills();
			// }
			viewHolder.time.setText(TimeUtil.forTimeForHourAndSeconed(time));

			//不再合并同一天的任务---zlz(v2.5.1)
			//			if (subTask.ismTheDayFirst()) {
			viewHolder.yearLayout.setVisibility(View.VISIBLE);
			viewHolder.year.setText(TimeUtil.forTimeForYearMonthDay(subTask
					.getmRemindTime().getmMills()));
			//			} else {
			//				viewHolder.yearLayout.setVisibility(View.GONE);
			//			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					modeifyTime(subTask, mTask.getmCycleTime() == -1, position);
				}
			});
			return convertView;
		}
	}

	class ViewHolder
	{
		private TextView time, title, year;
		private RelativeLayout yearLayout;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_remind_back_layout:
			Intent toFinish = new Intent(TaskSettingActivity.this,TaskFinishActivity.class);
			setResult(12,toFinish);
			finish();
			break;
		}
	}
	@Override
	public void onBackPressed() {
	    
	super.onBackPressed();
	}
	/**
	 * 修改子任务时间
	 * @param subTask
	 * @param isEverydayTask
	 * @param position
	 */
	public void modeifyTime(final YKTask subTask, boolean isEverydayTask,
			final int position) {
		if (subTask == null) {
			return;
		}
		final YKDate subDate = subTask.getmRemindTime();
		long taskRemindTime = subDate.getmMills();
		// long now = System.currentTimeMillis();
		// String toast = "任务时间已过期，不能编辑";
		// if (isEverydayTask) {
		// if (now > (taskRemindTime + TimeUtil.getTodayZero())) {
		// Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
		// return;
		// }
		// } else if (now > taskRemindTime) {
		// Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
		// return;
		// }
//		if(subTask.getmSubtask() == null){
//		    for (int i = 0; i < subTask.getmSubtask().size(); i++) {
//			if(subTask.getmSubtask().get(i).getmStatus() == 30){
//			    mSubTaskSubTaskId.add(subTask.getmSubtask().get(i).getID());
//			}
//		    }
//		}else{
		    if(subTask.getmStatus() == 30){
			mSubTaskId = subTask.getID();
		    }
//		}
		mCalendar.setTimeInMillis(taskRemindTime);
		int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
		int mMinute = mCalendar.get(Calendar.MINUTE);
		new YKTimePickerDialog(TaskSettingActivity.this,
				new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay,
					int minute) {
				mCalendar.setTimeInMillis(subDate.getmMills());
				mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				mCalendar.set(Calendar.MINUTE, minute);
				mCalendar.set(Calendar.SECOND, 0);
				mCalendar.set(Calendar.MILLISECOND, 0);
				boolean isEveryDay = mTask.getmCycleTime() == -1;
				subDate.setmMills(mCalendar.getTimeInMillis());
				mAdapter.notifyDataSetChanged();
				long newtime = mCalendar.getTimeInMillis() - (isEveryDay ? TimeUtil.getTodayZero() : 0);
				updateSubTaskRemindTime(subTask, newtime);
				System.out.println("tasksettingactivity modify time");
			}
		}, mHour, mMinute, true).show();
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
		if (mTask.getmCycleTime() == -1) {
			ArrayList<YKTask> mSubTasks = new ArrayList<YKTask>();
			mSubTasks = mTask.getmSubtask();
			long tadayZero = TimeUtil.getTodayZero();
			for (int i = 0; i < mSubTasks.size(); i++) {
				YKTask tmptask = mSubTasks.get(i);
				YKDate ykdate = tmptask.getmRemindTime();
				ykdate.setmMills(tadayZero + ykdate.getmMills());
			}
		}
	}

	/**
	 * 友盟统计需要的俩个方法
	 */
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("TaskSettingActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		JPushInterface.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("TaskSettingActivity"); // 保证 onPageEnd 在onPause
		// 之前调用,因为 onPause
		// 中会保存信息
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}

	private class YKTimePickerDialog extends TimePickerDialog
	{

		public YKTimePickerDialog(Context context, int theme,
				OnTimeSetListener callBack, int hourOfDay, int minute,
				boolean is24HourView)
		{
			super(context, theme, callBack, hourOfDay, minute, is24HourView);
		}

		public YKTimePickerDialog(Context context, OnTimeSetListener callBack,
				int hourOfDay, int minute, boolean is24HourView)
		{
			super(context, callBack, hourOfDay, minute, is24HourView);
		}

		@Override
		protected void onStop() {
		}
	}
}
