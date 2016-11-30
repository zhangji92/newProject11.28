package com.yoka.mrskin.activity;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.data.YKTopicData;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKDetalisTaskCallback;
import com.yoka.mrskin.model.managers.YKDetalisTaskManagers;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers;
import com.yoka.mrskin.model.managers.YKTaskManagers;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.NetWorkUtil;

/**
 * 计划详情页
 * 
 * @author yuhailong@yoka.com
 * 
 */
public class PlanDetailsActivity extends BaseActivity implements OnClickListener, Observer

{
	private YKTask mTask;
	private String taskid = null;
	private ImageView sBigImage;

	private boolean mIsNative = false;

	private LinearLayout mProductImage,topicTop,productTop;
	private ArrayList<YKProduct> mProduct;
	private ArrayList<YKTopicData> mTopic;
	private LinearLayout mBack, mTopicLayout;
	private CustomButterfly mCustomButterfly = null;
	private TextView mAddTask, mDetalisTitle, mDetalisAuthor, mDetalisDesc, mDetalisTask;
	private DisplayImageOptions options;
	private WindowManager wm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plan_details_activity);

		wm = (WindowManager) this.getSystemService(
				Context.WINDOW_SERVICE);

		YKActivityManager.getInstance().addActivity(this);
		Intent initNative = getIntent();
		mIsNative = initNative.getBooleanExtra("isNative", false);
		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true).considerExifParams(true).build();
		initTask();
		initData();
		init();
		getData();
		//        TrackManager.getInstance().addTrack(
		//                TrackUrl.PAGE_OPEN + "PlanDetailsAvtivity");
	}
	//由我的计划页面跳转
	private void initData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		String taskId = intent.getStringExtra("taskSub");

		if (taskId == null) {
			return;
		}
		taskid = taskId;
	}
	//由美丽计划页面跳转
	private void initTask() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		YKTask task = (YKTask) intent.getSerializableExtra("task");

		if (task == null) {
			return;
		}
		mTask = task;
		if(NetWorkUtil.isNetworkAvailable(PlanDetailsActivity.this)){
			taskid = mTask.getID();
		}

	}

	public void getData() {
		if (taskid != null) {
			loadingView();
			YKDetalisTaskManagers.getInstance().postYKDetalisTask(new YKDetalisTaskCallback() {
				@SuppressWarnings("deprecation")
				@Override
				public void callback(YKResult result, YKTask detalisTask) {
					AppUtil.dismissDialogSafe(mCustomButterfly);
					if (result.isSucceeded()) {
						// ListView Heard
						mTask = detalisTask;
						mDetalisTitle.setText(mTask.getmTitle());
						mDetalisAuthor.setText(mTask.getAuthorName());
						mDetalisDesc.setText(mTask.getmDesc());
						try {
							/* Glide.with(PlanDetailsActivity.this).load( mTask.getmCoverImage().getmURL())
							.into( sBigImage);*/

							int screenWidth = wm.getDefaultDisplay().getWidth();

							int imageWidth = mTask.getmCoverImage().getMwidth();
							int imageHeight = mTask.getmCoverImage().getMheight();
							int tmpHeight = 0;
							tmpHeight = screenWidth * imageHeight / imageWidth;
							sBigImage.setLayoutParams(new LinearLayout.LayoutParams(
									screenWidth, tmpHeight));

							ImageLoader.getInstance().displayImage(mTask.getmCoverImage().getmURL(), sBigImage, options);

						} catch (Exception e) {
							sBigImage.setBackgroundResource(R.drawable.list_default_bg);
						}
						//连网条件下添加相关关联(因为本地没有存储)
						addRelated();
					} else {
						if (!mIsNative) {
							Toast.makeText(PlanDetailsActivity.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
						}
					}
				}
			}, taskid);


		} else {
			if (mTask != null && mTask.ismIsAdd()) {
				mDetalisTitle.setText(mTask.getmTitle());
				mDetalisAuthor.setText(mTask.getAuthorName());
				mDetalisDesc.setText(mTask.getmDesc());

				try {
					ImageLoader.getInstance().displayImage(mTask.getmCoverImage().getmURL(), sBigImage, options);
					//	Glide.with(PlanDetailsActivity.this).load(mTask.getmCoverImage().getmURL()).into(sBigImage);

				} catch (Exception e) {
					sBigImage.setBackgroundResource(R.drawable.list_default_bg);
				}
			} else {
				if (!mIsNative) {
					Toast.makeText(PlanDetailsActivity.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
				}
			}
		}

	}

	private void addRelated(){
		//======================================================================
		// 正文ListView
		mTopic = mTask.getRelated_topics();
		if (mTopic != null) {
			topicTop.setVisibility(View.VISIBLE);
			for (int i = 0; i < mTopic.size(); i++) {
				final int positon = i;
				TextView mTopicTitle = new TextView(PlanDetailsActivity.this);
				TextView mTopicDesc = new TextView(PlanDetailsActivity.this);
				mTopicTitle.setTextColor(Color.BLACK);
				mTopicTitle.setText(mTopic.get(i).getmTopicTitle());
				mTopicDesc.setText(mTopic.get(i).getmTopicDesc());
				mTopicDesc.setTextColor(getResources().getColor(R.color.text_my_experience_tab_default));
				View vLine = new View(PlanDetailsActivity.this);
				LayoutParams paramsLine = new LayoutParams(LayoutParams.MATCH_PARENT,1);
				paramsLine.bottomMargin = 10;
				vLine.setLayoutParams(paramsLine);
				vLine.setBackgroundColor(getResources().getColor(R.color.gray_e4));

				mTopicLayout.addView(mTopicTitle);
				mTopicLayout.addView(mTopicDesc);
				mTopicLayout.addView(vLine);
				mTopicLayout.setTag(i);
				//点击跳转对应Topic----zlz(待改善,唉……)
				mTopicTitle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						//
						Intent intent = new Intent(PlanDetailsActivity.this,
								YKWebViewActivity.class);
						intent.putExtra("url", mTopic.get(positon).getmTopicUrl());
						intent.putExtra("identification", "index");
						startActivity(intent);

					}
				});

				mTopicDesc.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						//
						Intent intent = new Intent(PlanDetailsActivity.this,
								YKWebViewActivity.class);
						intent.putExtra("url", mTopic.get(positon).getmTopicUrl());
						intent.putExtra("identification", "index");
						startActivity(intent);

					}
				});
			}
		}
		// 底部Foot
		mProduct = mTask.getRelated_products();
		if (mProduct != null) {
			productTop.setVisibility(View.VISIBLE);
			for (int i = 0; i < mTask.getRelated_products().size(); i++) {
				//(item)商品图片+名称  垂直分布
				LinearLayout layoutItem = new LinearLayout(PlanDetailsActivity.this);
				layoutItem.setOrientation(LinearLayout.VERTICAL);
				LinearLayout.LayoutParams paramsitem = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
				paramsitem.leftMargin = 10;
				paramsitem.topMargin = 10;
				paramsitem.bottomMargin = 10;
				layoutItem.setLayoutParams(paramsitem);
				/*图片*/
				ImageView image = new ImageView(PlanDetailsActivity.this);
				WindowManager wm = (WindowManager) PlanDetailsActivity.this
						.getSystemService(Context.WINDOW_SERVICE);
				final int position = i;
				int width = wm.getDefaultDisplay().getWidth();

				LayoutParams params = new LayoutParams(width / 4, width / 4);
				params.leftMargin = 10;
				image.setLayoutParams(params);

				/*    Glide.with(PlanDetailsActivity.this).load( mProduct.get(i)
						            .getProduct_image()
						            .getmURL())
							.into( image);*/
				ImageLoader.getInstance().displayImage(mProduct.get(i).getProduct_image().getmURL(), image,
						options);
				/*文字*/
				TextView pName = new TextView(PlanDetailsActivity.this);
				LayoutParams paramsText = new LayoutParams(width / 4, LayoutParams.WRAP_CONTENT);
				paramsText.topMargin = 5;
				pName.setLayoutParams(paramsText);
				pName.setText(mProduct.get(i).getTitle());
				pName.setTextColor(getResources().getColor(R.color.text_my_experience_tab_default));

				layoutItem.addView(image);
				layoutItem.addView(pName);
				mProductImage.addView(layoutItem);

				//添加关联商品点击----zlz
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(PlanDetailsActivity.this,YKWebViewActivity.class);
						intent.putExtra("productdetalis",mProduct.get(position).getDescription_url());
						intent.putExtra("identification", "cosmetics");
						startActivity(intent);


					}
				});


			}
		}
	}

	private void loadingView() {
		try {
			mCustomButterfly = CustomButterfly.show(this);
		} catch (Exception e) {
			mCustomButterfly = null;
		}
	}

	private void init() {
		mBack = (LinearLayout) findViewById(R.id.plan_detalis_black_layout);
		mBack.setOnClickListener(this);

		// 计划
		mDetalisTitle = (TextView) findViewById(R.id.plan_detalis_title);
		mDetalisAuthor = (TextView) findViewById(R.id.plan_detalis_author);
		mDetalisDesc = (TextView) findViewById(R.id.plan_detalis_desc);
		sBigImage = (ImageView) findViewById(R.id.plan_big_image);

		mDetalisTask = (TextView) findViewById(R.id.plan_detalis_taskdetalis);
		mDetalisTask.setOnClickListener(this);
		mAddTask = (TextView) findViewById(R.id.plan_detalis_add);
		mAddTask.setOnClickListener(this);

		// 相关产品
		productTop = (LinearLayout) findViewById(R.id.product_top);
		mProductImage = (LinearLayout) findViewById(R.id.product_image_layout);

		// 相关阅读
		topicTop = (LinearLayout) findViewById(R.id.topic_top);
		mTopicLayout = (LinearLayout) findViewById(R.id.topic_layout);

		isAddorNoAddTask();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.plan_detalis_add:
			if (AppUtil.isNetworkAvailable(PlanDetailsActivity.this)) {
				if (YKCurrentUserManager.getInstance().isLogin()) {
					if (mTask == null) {
						return;
					}
					addTaskFromServer(mTask);
					return;
				}
				Intent intent = new Intent(PlanDetailsActivity.this, LoginActivity.class);
				startActivityForResult(intent, 0);
			} else {
				Toast.makeText(PlanDetailsActivity.this, R.string.intent_no, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.plan_detalis_black_layout:
			finish();
			break;
		case R.id.plan_detalis_taskdetalis:
			Intent planDetalis = new Intent(PlanDetailsActivity.this, TaskBriefActivity.class);
			planDetalis.putExtra("task", mTask);
			startActivity(planDetalis);
			break;
		default:
			break;
		}
	}

	private void addTaskFromServer(final YKTask task) {
		/*用户token*/
		String authToken = "";
		if(null != YKCurrentUserManager.getInstance().getUser()){

			authToken = YKCurrentUserManager.getInstance().getUser().getToken();
		}
		if (task != null) {
			final boolean isAdd = task.ismIsAdd();
			loadingView();
			if (!isAdd) {
				final YKSyncTaskManagers.SyncTaskCallBack callback = new YKSyncTaskManagers.SyncTaskCallBack() {

					@Override
					public void callback(YKResult result) {
						AppUtil.dismissDialogSafe(mCustomButterfly);
						if (result.isSucceeded()) {
							addTaskFromNative(task);
						} else {

						}
					}
				};
				if (!isAdd) {
					YKSyncTaskManagers.getInstance().addTaskSyncToServer(authToken,task.getID(), callback);
				}
			} else {
				AppUtil.dismissDialogSafe(mCustomButterfly);
			}
		}

	}

	private void addTaskFromNative(final YKTask task) {
		boolean isAdd = task.ismIsAdd();
		if (!isAdd) {
			task.setmIsAdd(true);
			YKTaskManager.getInstnace().addTask(task);
			YKTask tmpTask = YKTaskManagers.getInstance().getTask(task.getID());
			if (tmpTask != null) {
				tmpTask.setmIsAdd(true);
			}
		}
		YKTaskManager.getInstnace().notifyTaskDataChanged();
		String toastContent = null;
		if (!isAdd) {
			toastContent = getString(R.string.task_remove_sucess);
			mAddTask.setText(getString(R.string.plan_have));
			mAddTask.setFocusable(false);
			mAddTask.setBackgroundResource(R.drawable.task_undone_bg);
		}
		Toast.makeText(PlanDetailsActivity.this, toastContent, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void update(Observable observable, Object data) {
		YKTaskManager.getInstnace().notifyTaskDataChanged();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		YKTaskManager.getInstnace().deleteObserver(this);
		YKActivityManager.getInstance().removeActivity(this);
		//        TrackManager.getInstance().addTrack(
		//                TrackUrl.PAGE_CLOSE + "PlanDetailsAvtivity");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == RESULT_OK) {
			boolean haveLogin = data.getBooleanExtra(LoginActivity.UESR, false);
			if (haveLogin) {
				addTaskFromServer(mTask);
			} else {

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 判断首页点击进去任务是否是添加的
	private void isAddorNoAddTask() {
		// if(mIsAdd){

		YKTask task = YKTaskManager.getInstnace().getTaskById(taskid);
		if (task == null) {
			mAddTask.setText(getString(R.string.plan_detalis_addtask));
			mAddTask.setEnabled(true);
			mAddTask.setBackgroundResource(R.drawable.error_release_plan);
		} else {
			mAddTask.setText(getString(R.string.plan_have));
			mAddTask.setEnabled(false);
			mAddTask.setBackgroundResource(R.drawable.task_undone_bg);

		}
		// ArrayList<YKTopicData> mListTopic = (ArrayList<YKTopicData>)
		// YKNewTopicDataManagers
		// .getInstance().getTopicData();
		//
		// for (int i = 0; i < task.size(); i++) {
		// for (int j = 0; j < mListTopic.size(); j++) {
		// String taskURL = mListTopic.get(j).getmTopicUrl();
		// if(taskURL!= null){
		// Uri uri = Uri.parse(taskURL);
		// String id = uri.getQueryParameter("id");
		// if (task != null && task.get(i).getID().equals(id)&&
		// task.get(i).ismIsAdd()) {
		// mAddTask.setText(getString(R.string.plan_have));
		// mAddTask.setEnabled(false);
		// mAddTask.setBackgroundResource(R.drawable.task_undone_bg);
		// }
		// }
		// }
		// }
		// }
	}

	/**
	 * 友盟统计需要的俩个方法
	 */
	public void onResume() {
		super.onResume();
		isAddorNoAddTask();
		if (mTask != null && mTask.ismIsAdd()) {
			mAddTask.setText(getString(R.string.plan_have));
			mAddTask.setFocusable(false);
			mAddTask.setBackgroundResource(R.drawable.task_undone_bg);
		}

		MobclickAgent.onPageStart("PlanDetailsActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		JPushInterface.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("PlanDetailsActivity"); // 保证 onPageEnd 在onPause
		// 之前调用,因为 onPause
		// 中会保存信息
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}
}
