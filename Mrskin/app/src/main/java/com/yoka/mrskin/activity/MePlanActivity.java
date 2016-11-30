package com.yoka.mrskin.activity;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers.DownloadTaskCallBack;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.model.managers.task.YKTaskStore;
/**
 * 我的计划
 * @author yuhailong
 * 
 */
public class MePlanActivity extends BaseActivity implements Observer
{
	private LinearLayout mBack,mAddTask;
	private ListView mListView;
	private MyAdapter myAdapter;
	private ArrayList<YKTask> mList;
	private ImageView mAddImageView;
	private TextView mSettingAddTaskTextView;
	private DisplayImageOptions options;
	private WindowManager wm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_meplan);

		wm = (WindowManager) this.getSystemService(
				Context.WINDOW_SERVICE);

		YKActivityManager.getInstance().addActivity(this);
		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true).considerExifParams(true)
				.build();
		getTask();
		init();
		getData();
		//TrackManager.getInstance().addTrack(TrackUrl.PAGE_OPEN + "MePlanActivity");
	}

	public void init() {
		mListView = (ListView) findViewById(R.id.setting_metask_lv);
		mListView.setDivider(null);
		mBack = (LinearLayout) findViewById(R.id.setting_metask_layoutback);
		mAddTask = (LinearLayout) findViewById(R.id.task_no_task_layout);
		mSettingAddTaskTextView = (TextView) findViewById(R.id.setting_add_task);
		mSettingAddTaskTextView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent task = new Intent(MePlanActivity.this,PlanFragmentActivity.class);
				startActivity(task);
			}
		});
		myAdapter = new MyAdapter();
		mListView.setAdapter(myAdapter);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//添加计划
		mAddImageView=(ImageView)findViewById(R.id.setting_metask_add_imageView);
		mAddImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent task = new Intent(MePlanActivity.this,PlanFragmentActivity.class);
				startActivity(task);
			}
		});
		YKTaskManager.getInstnace().addObserver(this);
	}

	/**
	 * 请求数据
	 */
	private void getTask(){
		mList =  YKTaskManager.getInstnace().getTaskList();
		for (int i = 0; i < mList.size(); i++) {
			if(mList.get(i) == null){
				YKTaskManager.getInstnace().clearAllTask();
				YKSyncTaskManagers.getInstance().downLoadTask(
						new DownloadTaskCallBack() {

							@Override
							public void callback(ArrayList<YKTask> taskList,
									YKResult result) {
								if (result.isSucceeded()) {
									if (taskList != null && taskList.size() > 0) {
										YKTaskStore.getInstnace()
										.saveTaskList(taskList);
										YKTaskManager.getInstnace()
										.notifyTaskDataChanged();
									}
								} else {
									YKCurrentUserManager.getInstance().clearLoginUser();
								}
							}
						});
				break;
			}
		}
	}


	private class MyAdapter extends BaseAdapter
	{

		private ViewHolder viewHolder = null;

		@Override
		public int getCount() {
			if (mList == null) {
				return 0;
			}
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			if (mList == null) {
				return null;
			}
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final YKTask task = mList.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(MePlanActivity.this).inflate(
						R.layout.setting_meplan_text, null);
				viewHolder = new ViewHolder();
				viewHolder.sTitle = (TextView) convertView
						.findViewById(R.id.setting_plan_title);
				viewHolder.sBigImage = (ImageView) convertView
						.findViewById(R.id.setting_plan_image);
				viewHolder.sAuthor = (TextView) convertView
						.findViewById(R.id.setting_plan_author);
				viewHolder.sOrSuccess = (ImageView) convertView
						.findViewById(R.id.me_plan_success);
				viewHolder.sDesc = (TextView) convertView
						.findViewById(R.id.setting_plan_desc);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if (task.getmCoverImage() != null) {
				try {
					/*  Glide.with(MePlanActivity.this).load(task.getmCoverImage().getmURL())
                		.into(viewHolder.sBigImage);*/
                		// calculate image height

                		int screenWidth = wm.getDefaultDisplay().getWidth();

                		int imageWidth = task.getmCoverImage().getMwidth();
                		int imageHeight = task.getmCoverImage().getMheight();
                		int tmpHeight = 0;
                		tmpHeight = screenWidth * imageHeight / imageWidth;
                		viewHolder.sBigImage
                		.setLayoutParams(new RelativeLayout.LayoutParams(
                				screenWidth, tmpHeight));

                		ImageLoader.getInstance().displayImage(task.getmCoverImage().getmURL(), viewHolder.sBigImage, options);

				} catch (Exception e) {
					viewHolder.sBigImage.setBackgroundResource(R.drawable.list_default_bg);
				}
			} else {
				viewHolder.sBigImage
				.setBackgroundResource(R.drawable.list_default_bg);
			}

			if (task.ismIsAdd()) {
				viewHolder.sOrSuccess.setVisibility(View.VISIBLE);
				viewHolder.sOrSuccess
				.setBackgroundResource(R.drawable.plan_imgtag_undone);

				if (task.getmCycleTime() != -1) {
					// 过期的任务置成未完成
					long systemTime = System.currentTimeMillis();
					long taskTime = task.getmSubtask()
							.get(task.getmSubtask().size() - 1)
							.getmRemindTime().getmMills();

					if (systemTime > taskTime || task.isFinished()) {
						viewHolder.sOrSuccess
						.setBackgroundResource(R.drawable.plan_imgtag_done);
					}
				}

			} else {
				viewHolder.sOrSuccess.setVisibility(View.GONE);
			}

			// boolean isFinished = true;
			// if (task.getmCycleTime() == -1) {
			// isFinished = false;
			// } else {
			// for (int j = 0; j < task.getmSubtask().size(); j++) {
			// if (!task.getmSubtask().get(j).isFinished()) {
			// isFinished = false;
			// break;
			// }
			// }
			// }
			// if (isFinished) {
			// viewHolder.sOrSuccess
			// .setBackgroundResource(R.drawable.plan_imgtag_done);
			// } else {
			// viewHolder.sOrSuccess
			// .setBackgroundResource(R.drawable.plan_imgtag_undone);
			// }
			//
			//
			// //过期的任务置成未完成
			// long systemTime = System.currentTimeMillis();
			// String newSystemTime =
			// TimeUtil.forTimeForYearMonthDay(systemTime);
			// long a =
			// task.getmSubtask().get(task.getmSubtask().size()-1).getmRemindTime().getmMills();
			// String newTaskmTime = TimeUtil.forTimeForYearMonthDay(a);
			//
			// if(newSystemTime.equals(newTaskmTime)){
			// viewHolder.sOrSuccess
			// .setBackgroundResource(R.drawable.plan_imgtag_done);
			// }
			viewHolder.sDesc.setText(task.getmDesc());
			viewHolder.sAuthor.setText(R.string.home_title);
			viewHolder.sTitle.setText(task.getmTitle());

			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent taskDetalis = new Intent(MePlanActivity.this,
							TaskFinishActivity.class);
					if (mList != null) {
						taskDetalis.putExtra("task", task);
						taskDetalis.putExtra("isNative", true);
						startActivity(taskDetalis);
					}
				}
			});
			return convertView;
		}
	}

	private class ViewHolder
	{
		private TextView sTitle, sAuthor, sDesc;
		private ImageView sBigImage, sOrSuccess;
	}

	private void getData() {
		mList = YKTaskManager.getInstnace().getTaskList();
		if(mList != null && mList.size() > 0){
			mListView.setVisibility(View.VISIBLE);
			mAddTask.setVisibility(View.GONE);
			myAdapter.notifyDataSetChanged();
		}else{
			mListView.setVisibility(View.GONE);
			mAddTask.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		getData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		YKTaskManager.getInstnace().deleteObserver(this);
		YKActivityManager.getInstance().removeActivity(this);
		//TrackManager.getInstance().addTrack(TrackUrl.PAGE_CLOSE + "MePlanActivity");
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mList != null && mList.size() > -1){
			mListView.setVisibility(View.VISIBLE);
			mAddTask.setVisibility(View.GONE);
			myAdapter.notifyDataSetChanged();
		}else{
			mListView.setVisibility(View.GONE);
			mAddTask.setVisibility(View.VISIBLE);
		}
		MobclickAgent.onPageStart("MePlanActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		getData();
		JPushInterface.onResume(this);
	}

	/**
	 * 有Fragment
	 */
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MePlanActivity"); // 保证 onPageEnd
		// 在onPause
		// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}
}
