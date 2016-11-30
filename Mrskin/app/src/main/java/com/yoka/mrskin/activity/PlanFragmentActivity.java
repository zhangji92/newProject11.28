package com.yoka.mrskin.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;
import com.xlistview.XListViewFooter;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKLoadMoreCallback;
import com.yoka.mrskin.model.managers.YKTaskCallback;
import com.yoka.mrskin.model.managers.YKTaskManagers;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.model.managers.task.YKTaskStore;
import com.yoka.mrskin.model.managers.task.YKTaskStore.StoreItem;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.DoubleOnClick;
/**
 * 美丽计划
 * @author yuhailong
 *
 */
public class PlanFragmentActivity extends BaseActivity implements IXListViewListener, Observer,OnClickListener
{
	private Handler mHandler;
	private XListView mListView;
	private MyAdapter myAdapter;
	private ArrayList<YKTask> mTaskList;
	private LinearLayout mBack;
	private RelativeLayout mDoubleTop;
	private XListViewFooter mListViewFooter;
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_task);
		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true).considerExifParams(true).showImageOnLoading(R.drawable.list_default_bg)
				.build();
		init();
		initData();
	}
	private void init(){
		mListView = (XListView) findViewById(R.id.add_task_lv);
		mListViewFooter = new XListViewFooter(PlanFragmentActivity.this);
		mListView.setOnScrollListener(new OnScrollListener()
		{

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				if(android.os.Build.MODEL != null && android.os.Build.MODEL.equals("MX5")){
					if (view.getLastVisiblePosition() == (view.getCount() - 1)){
						mListViewFooter.setState(XListViewFooter.STATE_READY);
						mListView.startLoadMore();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount)
			{

			}
		});
		mBack = (LinearLayout) findViewById(R.id.plan_search_back);
		mBack.setOnClickListener(this);
		mDoubleTop = (RelativeLayout) findViewById(R.id.planfragment_title_layout);
		mDoubleTop.setOnClickListener(this);
		YKTaskManager.getInstnace().addObserver(this);
		//默认ListView上拉刷新可用加载更多不可用
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(false);
		// 控制ListView每个Item高度
		mListView.setDividerHeight(15);
		mListView.setCacheColorHint(0xff00ff00);
		myAdapter = new MyAdapter();
		mListView.setAdapter(myAdapter);
		mListView.setXListViewListener(this);
		mHandler = new Handler();
	}
	private void initData(){
		if (AppUtil.isNetworkAvailable(this)) {
			YKTaskManagers.getInstance().refreshTaskList(new YKTaskCallback() {
				@Override
				public void callback(YKResult result, ArrayList<YKTask> tasks) {
					if (result.isSucceeded()) {
						if (tasks != null && tasks.size() > 0) {
							updateTaskStatu(tasks);
							updateTaskStatus(tasks);

							mTaskList = tasks;
							myAdapter.notifyDataSetChanged();
							if(mTaskList != null){
								mListView.setPullLoadEnable(true);
							}
						}
					} else {
						Toast.makeText(PlanFragmentActivity.this,
								getString(R.string.intent_error),
								Toast.LENGTH_LONG).show();
					}
				}
			});
		} else {
			Toast.makeText(PlanFragmentActivity.this, getString(R.string.intent_no),
					Toast.LENGTH_LONG).show();
		}

	}
	private class MyAdapter extends BaseAdapter
	{
		private ViewHolder viewHolder = null;

		@Override
		public int getCount() {
			if (mTaskList == null) {
				return 0;
			}
			return mTaskList.size();
		}

		@Override
		public Object getItem(int position) {
			if (mTaskList == null) {
				return null;
			}
			return mTaskList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final YKTask task = mTaskList.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(PlanFragmentActivity.this).inflate(
						R.layout.add_task_lv_test, null);
				viewHolder = new ViewHolder();
				viewHolder.sTitle = (TextView) convertView
						.findViewById(R.id.plan_text_title);
				viewHolder.sBigImage = (ImageView) convertView
						.findViewById(R.id.plan_image_title);
				viewHolder.mAuthor = (TextView) convertView
						.findViewById(R.id.plan_text_author);
				viewHolder.mDesc = (TextView) convertView
						.findViewById(R.id.plan_text_desc);
				viewHolder.sComm = (TextView) convertView
						.findViewById(R.id.plan_text_common);
				viewHolder.sDayNum = (TextView) convertView
						.findViewById(R.id.plan_text_daynum);
				viewHolder.sPersion = (TextView) convertView
						.findViewById(R.id.plan_beautiful_person);
				viewHolder.sIsNoHave = (ImageView) convertView
						.findViewById(R.id.planfragment_isnohave);
				convertView.setTag(R.id.add_task_lv, viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag(R.id.add_task_lv);
			}

			// calculate image height
			WindowManager wm = (WindowManager) PlanFragmentActivity.this.getSystemService(
					Context.WINDOW_SERVICE);
			@SuppressWarnings("deprecation")
			int screenWidth = wm.getDefaultDisplay().getWidth();
			int imageWidth = task.getmCoverImage().getMwidth();
			int imageHeight = task.getmCoverImage().getMheight();
			int tmpHeight = 0;
			tmpHeight = screenWidth * imageHeight / imageWidth;
			viewHolder.sBigImage
			.setLayoutParams(new RelativeLayout.LayoutParams(
					screenWidth, tmpHeight));

			if (task.getmCoverImage() == null
					&& !TextUtils.isEmpty(task.getmCoverImage().getmURL())) {
				viewHolder.sBigImage
				.setBackgroundResource(R.drawable.list_default_bg);
			} else {
				ImageLoader.getInstance().displayImage(task.getmCoverImage().getmURL(), viewHolder.sBigImage, options);
				//	 Glide.with(PlanFragmentActivity.this).load( task.getmCoverImage().getmURL()).into( viewHolder.sBigImage).onLoadStarted(getResources().getDrawable(R.drawable.list_default_bg));

			}
			viewHolder.sTitle.setText(task.getmTitle());
			viewHolder.mAuthor.setText(task.getAuthorName());
			viewHolder.mDesc.setText(task.getmDesc());

			int[] coverDesc = task.getmCoverDesc();
			if (coverDesc != null && coverDesc.length == 3) {
				viewHolder.sComm.setText(getString(R.string.task_experience)
						+ coverDesc[0] + getString(R.string.task_day));
				viewHolder.sDayNum.setText(getString(R.string.task_everyday)
						+ coverDesc[1] + getString(R.string.task_order_c));
				viewHolder.sPersion
				.setText(getString(R.string.task_have) + coverDesc[2]
						+ getString(R.string.task_beautiful_mon));
			}

			if (task.getmCycleTime() == -1) {
				viewHolder.sComm.setText(getString(R.string.task_everyday)
						+ coverDesc[1] + getString(R.string.task_order_c));
				viewHolder.sDayNum.setVisibility(View.GONE);
			} else {
				viewHolder.sComm.setText(getString(R.string.task_experience)
						+ coverDesc[0] + getString(R.string.task_day));
				viewHolder.sDayNum.setText(getString(R.string.task_everyday)
						+ coverDesc[1] + getString(R.string.task_order_c));
			}

			if (task.ismIsAdd()) {
				viewHolder.sIsNoHave.setVisibility(View.VISIBLE);
				viewHolder.sIsNoHave
				.setBackgroundResource(R.drawable.plan_imgtag_undone);

				if (task.getmCycleTime() != -1) {//是否为周期性任务
					YKTask addtask = YKTaskManager.getInstnace().getTaskById(task.getID());
					long systemTime = System.currentTimeMillis();
					long taskTime = addtask.getmSubtask()
							.get(addtask.getmSubtask().size() - 1)
							.getmRemindTime().getmMills();
					Log.d("time=", taskTime+"--"+ (systemTime > taskTime)+"--"+systemTime);

					if (systemTime > taskTime || task.isFinished()) {
						viewHolder.sIsNoHave
						.setBackgroundResource(R.drawable.plan_imgtag_done);
					}
				}
			} else {
				viewHolder.sIsNoHave.setVisibility(View.GONE);
			}

			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mTaskList != null && mTaskList.size() > 0) {
						if (task.ismIsAdd()) {//已添加为我的计划
							Intent taskFinish = new Intent(PlanFragmentActivity.this,
									TaskFinishActivity.class);
							taskFinish.putExtra("task", mTaskList.get(position));
							taskFinish.putExtra("isNative", false);
							startActivity(taskFinish);
						} else {
							if (AppUtil.isNetworkAvailable(PlanFragmentActivity.this)) {
								Intent taskDetalis = new Intent(PlanFragmentActivity.this,
										PlanDetailsActivity.class);
								taskDetalis.putExtra("taskSub",
										mTaskList.get(position).getID());
								startActivity(taskDetalis);
							} else {
								Toast.makeText(PlanFragmentActivity.this,
										R.string.intent_no, Toast.LENGTH_SHORT)
										.show();
							}
						}
					}
				}
			});
			return convertView;
		}
	}

	private class ViewHolder
	{
		private TextView sTitle, mAuthor, mDesc, sComm, sDayNum, sPersion;
		private ImageView sBigImage, sIsNoHave;
	}

	private void updateTaskStatu(ArrayList<YKTask> task) {
		if (task == null) {
			return;
		}
		ArrayList<StoreItem> mStoreItemList = YKTaskStore.getInstnace()
				.getStoreTable();
		for (int i = 0; i < task.size(); i++) {
			YKTask yktask = task.get(i);
			boolean isFind = false;
			for (int j = 0; j < mStoreItemList.size(); j++) {
				StoreItem storedItem = mStoreItemList.get(j);
				if (storedItem == null)
					continue;
				if (storedItem.getTaskId() == null)
					continue;
				if (storedItem.getTaskId().equals(yktask.getID())) {
					isFind = true;
					break;
				}
			}
			yktask.setmIsAdd(isFind);
		}
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				initData();
				if (mListView != null) {
					mListView.stopRefresh();
				}
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		if (AppUtil.isNetworkAvailable(PlanFragmentActivity.this)) {
			String id = null;
			if (mTaskList != null && mTaskList.size() > 0) {
				int index = mTaskList.size() - 1;
				id = mTaskList.get(index).getID();

			}
			YKTaskManagers.getInstance().loadmoreTaskList(
					new YKLoadMoreCallback() {

						@Override
						public void callback(YKResult result,
								ArrayList<YKTask> task) {
							mListView.stopLoadMore();
							if (result.isSucceeded()) {
								if (task.size() == 0) {
									Toast.makeText(PlanFragmentActivity.this,
											getString(R.string.task_no_task),
											Toast.LENGTH_LONG).show();
								} else {
									if (task != null && task.size() > 0) {
										//更新主任务
										updateTaskStatu(task);
										//更新子任务
										updateTaskStatus(task);
									}
									mTaskList.addAll(task);
								}
								myAdapter.notifyDataSetChanged();
							} else {
								Toast.makeText(PlanFragmentActivity.this,
										getString(R.string.intent_error),
										Toast.LENGTH_LONG).show();
							}
						}
					}, id);
		} else {
			mListView.stopLoadMore();
			Toast.makeText(PlanFragmentActivity.this, getString(R.string.intent_no),
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 友盟统计需要的俩个方法
	 */
	public void onResume() {
		super.onResume();
		updateTaskStatus(mTaskList);
		if (YKCurrentUserManager.getInstance().isLogin()) {
			ArrayList<YKTask> mArrayList = new ArrayList<YKTask>();
			mArrayList = YKTaskManager.getInstnace().getTaskList();
			if (!mArrayList.equals(mTaskList)) {
				myAdapter.notifyDataSetChanged();
			}
		}

		if (myAdapter != null) {
			myAdapter.notifyDataSetChanged();
		}

		MobclickAgent.onPageStart("PlanFragmentActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("PlanFragmentActivity"); // 保证 onPageEnd 在onPause
		// 之前调用,因为 onPause
		// 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		YKTaskManager.getInstnace().deleteObserver(this);
	}

	@Override
	public void update(Observable observable, Object data) {
		if (myAdapter != null) {
			myAdapter.notifyDataSetChanged();
		}
	}

	private void updateTaskStatus(ArrayList<YKTask> taskList) {
		if (taskList == null) return;
		for (YKTask yktask : taskList) {
			YKTask storedTask = YKTaskManager.getInstnace().getTaskById(
					yktask.getID());
			if (storedTask == null)
				continue;

			ArrayList<YKTask> storedSubTasks = storedTask.getmSubtask();
			if (storedSubTasks == null)
				continue;

			HashMap<String, YKTask> storedSubTaskTable = new HashMap<String, YKTask>();
			for (YKTask storedSubTask : storedSubTasks) {
				storedSubTaskTable.put(storedSubTask.getID(), storedSubTask);
			}

			ArrayList<YKTask> subTasks = yktask.getmSubtask();
			if (subTasks == null)
				continue;
			String subTaskID;
			YKTask storedSubTask;
			for (YKTask subTask : subTasks) {
				subTaskID = subTask.getID();
				if (subTaskID == null)
					continue;
				storedSubTask = storedSubTaskTable.get(subTaskID);
				if (storedSubTask == null)
					continue;

				subTask.setStatus(storedSubTask.getStatus());
			}
		}
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
		case R.id.plan_search_back:
			finish();
			break;
		case R.id.planfragment_title_layout:
			DoubleOnClick.doubleClick(mListView,null);
			break;
		default:
			break;
		}

	}
}
