package com.yoka.mrskin.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.db.PlanTaskDao;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.PlanTask;
import com.yoka.mrskin.model.data.YKDate;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKPlanTaskManager;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers;
import com.yoka.mrskin.model.managers.YKTaskManagers;
import com.yoka.mrskin.model.managers.task.TimeUtil;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.model.managers.task.YKTaskManager.HomeCardData;
import com.yoka.mrskin.util.AlarmHelper;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.NetWorkUtil;
import com.yoka.mrskin.util.RoundImage;

/**
 * 美丽计划页
 * 
 * @author yuhailong@yoka.com
 * 
 */
public class TaskFinishActivity extends BaseActivity implements
OnClickListener
{
    private YKTask mTask;
    private ImageView mCoverImage;
    private boolean mIsNative = false;
    private TextView mLayoutTitle, mDeleteTask, mTaskPerson, mTaskDesc;
    private LinearLayout mSubTaskList, mBack;
    private CustomButterfly mCustomButterfly = null;
    private DisplayImageOptions options;

    private View removeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.task_finish);
	YKActivityManager.getInstance().addActivity(this);
	initData();
	Intent initNative = getIntent();
	mIsNative = initNative.getBooleanExtra("isNative", false);
	options = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
		.resetViewBeforeLoading(true).considerExifParams(true)
		.build();
	init();
	setData();

    }

    private void init() {
	mCoverImage = (ImageView) findViewById(R.id.all_task_list_activity_cover);
	mTaskPerson = (TextView) findViewById(R.id.task_addperson);
	mTaskDesc = (TextView) findViewById(R.id.task_desc);
	mLayoutTitle = (TextView) findViewById(R.id.task_layout_title);

	mSubTaskList = (LinearLayout) findViewById(R.id.plan_detail_sub_task_list);

	mBack = (LinearLayout) findViewById(R.id.task_finish_back_layout);

	mDeleteTask = (TextView) findViewById(R.id.task_layout_delete);
	mDeleteTask.setOnClickListener(this);
	TextView taskSetting = (TextView) findViewById(R.id.task_setting_taday_layout);
	taskSetting.setOnClickListener(this);
	TextView taskDetail = (TextView) findViewById(R.id.task_detalis_taday_layout);
	taskDetail.setOnClickListener(this);

	mBack.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		finish();
	    }
	});
    }

    private void initData() {

	Intent intent = getIntent();
	if (intent == null) {
	    return;
	}
	String taskId = (String) intent.getSerializableExtra("taskId");

	if(!TextUtils.isEmpty(taskId)){
	    YKTask task = YKTaskManager.getInstnace().getTaskById(taskId);
	    mTask = task;
	    return;
	}


	YKTask task = (YKTask) intent.getSerializableExtra("task");
	if (task == null) {
	    return;
	}
	mTask = task;

	Log.d("TaskFinishing", mTask.toString());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (requestCode == 12) {
	    YKTask savedTask = YKTaskManager.getInstnace().getTaskById(mTask.getID());
	    mTask  = null;
	    mTask = savedTask;
	    mSubTaskList.removeAllViews();
	    setData();
	}
	super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.task_detalis_taday_layout:
	    Intent history = new Intent(TaskFinishActivity.this,
		    PlanDetailsActivity.class);
	    history.putExtra("task", mTask);
	    history.putExtra("isNative", mIsNative);
	    startActivity(history);
	    break;
	case R.id.task_setting_taday_layout:
	    Intent setting = new Intent(TaskFinishActivity.this,
		    TaskSettingActivity.class);
	    setting.putExtra("task", mTask);
	    startActivityForResult(setting,12);
	    break;
	case R.id.task_layout_delete:
	    if (AppUtil.isNetworkAvailable(TaskFinishActivity.this)) {
		AlertDialog dialog = new AlertDialog.Builder(this)
		.setTitle(R.string.dialog_delete_title)
		.setPositiveButton(
			getString(R.string.dialog_delete_confirm),
			new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog,
				    int which) {
				RemoveTaskFromServer(mTask);
			    }
			})
			.setNegativeButton(
				getString(R.string.dialog_delete_cancle),
				new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog,
					    int which) {

				    }
				}).create();
		AppUtil.showDialogSafe(dialog);
	    } else {
		Toast.makeText(TaskFinishActivity.this,
			getString(R.string.intent_error), Toast.LENGTH_SHORT)
			.show();
	    }
	    break;
	default:
	    break;
	}
    }

    private void RemoveTaskFromServer(final YKTask task) {
	try {
	    mCustomButterfly = CustomButterfly.show(this);
	} catch (Exception e) {
	    mCustomButterfly = null;
	}
	final boolean isAdd = task.ismIsAdd();
	final YKSyncTaskManagers.SyncTaskCallBack callback = new YKSyncTaskManagers.SyncTaskCallBack() {

	    @Override
	    public void callback(YKResult result) {
		AppUtil.dismissDialogSafe(mCustomButterfly);
		if (result.isSucceeded()) {
		    RemoveTaskFromNative(task);
		} else {
		    Toast.makeText(TaskFinishActivity.this,
			    getString(R.string.intent_error),
			    Toast.LENGTH_SHORT).show();
		}
	    }
	};
	if (isAdd) {
	    YKSyncTaskManagers.getInstance().removeTaskSyncToServer(
		    task.getID(), callback);
	} else {
	    Toast.makeText(TaskFinishActivity.this,
		    getString(R.string.task_add_planfaile), Toast.LENGTH_SHORT)
		    .show();
	}

    }

    private void RemoveTaskFromNative(final YKTask task) {
	boolean isAdd = task.ismIsAdd();
	if (isAdd) {
	    task.setmIsAdd(false);
	    YKTaskManager.getInstnace().removeTask(task.getID());
	    YKTask tmptask = YKTaskManagers.getInstance().getTask(task.getID());
	    if (tmptask != null) {
		tmptask.setmIsAdd(false);
	    }
	} else {
	    Toast.makeText(TaskFinishActivity.this,
		    getString(R.string.task_add_planfaile), Toast.LENGTH_SHORT)
		    .show();
	}
	YKTaskManager.getInstnace().notifyTaskDataChanged();
	String toastContent = null;
	if (isAdd) {
	    toastContent = getString(R.string.task_add_planfaile);
	    finish();
	}
	Toast.makeText(TaskFinishActivity.this, toastContent,
		Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("deprecation")
    private void setData() {

	// calculate image height
	WindowManager wm = (WindowManager) this.getSystemService(
		Context.WINDOW_SERVICE);
	int screenWidth = wm.getDefaultDisplay().getWidth();
	int imageWidth;
	int imageHeight;
	if(mTask.getmCoverImage() == null){
	    imageWidth = 450;
	    imageHeight = 450;
	}else{
	    imageWidth = mTask.getmCoverImage().getMwidth();
	    imageHeight = mTask.getmCoverImage().getMheight();
	}
	int tmpHeight = 0;
	tmpHeight = screenWidth * imageHeight / imageWidth;
	mCoverImage
	.setLayoutParams(new LinearLayout.LayoutParams(
		screenWidth, tmpHeight));
	if (mTask != null) {
	    try {
		/* 	Glide.with(TaskFinishActivity.this).load(mTask.getmCoverImage().getmURL())
        		.into(mCoverImage);*/
		ImageLoader.getInstance().displayImage(mTask.getmCoverImage().getmURL(), mCoverImage, options);
	    } catch (Exception e) {
		e.getMessage();
	    }
	    mLayoutTitle.setText(mTask.getmTitle());
	    mTaskPerson.setText(mTask.getmUsercount()
		    + getString(R.string.task_weizhi));
	    mTaskDesc.setText(mTask.getmDesc());

	    setupSubTaskInfo(mTask);


	}
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
	YKActivityManager.getInstance().removeActivity(this);
    }

    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume() {
	super.onResume();
	MobclickAgent.onPageStart("TaskFinishActivity"); // 统计页面
	MobclickAgent.onResume(this); // 统计时长
	JPushInterface.onResume(this);

    }


    public void onPause() {
	super.onPause();
	MobclickAgent.onPageEnd("TaskFinishActivity"); // 保证 onPageEnd
	// 在onPause
	// 之前调用,因为 onPause
	// 中会保存信息
	MobclickAgent.onPause(this);
	JPushInterface.onPause(this);
    }

    private void setupSubTaskInfo(final YKTask task) {
	ArrayList<YKTask> subTasks = task.getmSubtask();
	String currentDate = TimeUtil.forTimeForYearMonthDay(System
		.currentTimeMillis());
	if (subTasks != null && subTasks.size() > 0) {
	    String taskDate;
	    boolean isCycleTask;
	    for (YKTask subTask : subTasks) {
		taskDate = TimeUtil.forTimeForYearMonthDay(subTask
			.getmRemindTime().getmMills());
		isCycleTask = task.getmCycleTime() == -1;
		if (isCycleTask
			|| (!isCycleTask && currentDate.equals(taskDate))) {
		    setupSubTaskView(subTask, isCycleTask);
		}
	    }
	} else {
	    Toast.makeText(TaskFinishActivity.this, "今天无计划", Toast.LENGTH_SHORT)
	    .show();
	}
    }
		
    @SuppressLint("InflateParams")
    private void setupSubTaskView(final YKTask task, boolean isCycledTask) {
	LayoutInflater inflater = getLayoutInflater();
	removeView = inflater.inflate(R.layout.plan_detail_task_info, null);
	removeView.setTag(task.getID());
	// remind time
	TextView remindTime = (TextView) removeView.findViewById(R.id.task_today_time);
	long time = task.getmRemindTime().getmMills();
	if (isCycledTask) {
	    time += TimeUtil.getNextDayZero();
	}
	remindTime.setText(TimeUtil.forTimeForHourAndSeconed(time));

	// title
	TextView titleView = (TextView) removeView.findViewById(R.id.task_today_title);
	titleView.setText(task.getmTitle());

	// task status
	//		final TextView taskStatusView = (TextView) v
	//				.findViewById(R.id.plan_detail_task_info_status_text);
	final RoundImage taskStatusBackground = (RoundImage) removeView
		.findViewById(R.id.plan_detail_task_info_status_background);

	taskStatusBackground.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		YKTask subTask = task;
		if (mTask.getmCycleTime() == -1
			|| (mTask.getmCycleTime() != -1 && !subTask
			.isFinished())) {
		    requestCollections();
		    //关闭闹钟
		    //周期性任务
		    YKDate ykDate = subTask.getmRemindTime();
		    HomeCardData data = new HomeCardData(mTask.getmTitle(), subTask.getmTitle(), subTask.getID(), mTask.getID(), ykDate, subTask.getmScore(), mTask.ismCanreMind(), subTask.getmRemindContent());
		    AlarmHelper.getInstance().closeAlarm(data);

		    subTask.finish();
		    YKTaskManager.getInstnace().notifyTaskDataChanged();
		    //                    taskStatusView.setText(getString(R.string.task_finished));
		    taskStatusBackground.setEnabled(false);
		    taskStatusBackground
		    .setBackgroundResource(R.drawable.task_has_finished);


		}
	    }
	});

	mSubTaskList.addView(removeView);
	updateSubTaskStatus(task);
    }
    /**
     * 配合服务器进行统计--zlz
     */
    private void requestCollections(){

	/*用户token*/
	String authToken = "";
	String UserID = "-1";
	if(null != YKCurrentUserManager.getInstance().getUser()){
	    UserID = YKCurrentUserManager.getInstance().getUser().getId();
	    authToken = YKCurrentUserManager.getInstance().getUser().getToken();
	}

	PlanTask task = new PlanTask();
	task.setUid(Integer.parseInt(UserID));
	task.setTaskId(Integer.parseInt(mTask.getID()));
	task.setIndexTime(mTask.getmIndexTime());

	if(!NetWorkUtil.isNetworkAvailable(TaskFinishActivity.this)){
	    PlanTaskDao dao = new PlanTaskDao(TaskFinishActivity.this);
	    dao.insertTask(task);
	    return;
	}
	ArrayList<JSONObject> list = new ArrayList<>();
	JSONObject tempObject = new JSONObject();
	try {
	    tempObject.put("uid",UserID);
	    tempObject.put("tid", mTask.getID());
	    tempObject.put("index_time",mTask.getmIndexTime());
	} catch (JSONException e) {
	    e.printStackTrace();
	}
	list.add(tempObject);
	/*联网进行统计*/
	YKPlanTaskManager.getInstance().requestFinishTask(authToken,list,new YKPlanTaskManager.Callback(){

	    @Override
	    public void callback(YKResult result) {
		if(result.isSucceeded()){
		    //					Toast.makeText(TaskFinishActivity.this, "统计成功~", Toast.LENGTH_SHORT).show();
		}else{
		    Toast.makeText(TaskFinishActivity.this, "统计失败..."+result.getMessage().toString(), Toast.LENGTH_SHORT).show();
		}

	    }

	});
    }

    private void updateSubTaskStatus(YKTask task) {
	View container = mSubTaskList.findViewWithTag(task.getID());
	if (container == null) return;
	RoundImage taskStatusBackground = (RoundImage) container
		.findViewById(R.id.plan_detail_task_info_status_background);
	//		final TextView taskStatusView = (TextView) container
	//				.findViewById(R.id.plan_detail_task_info_status_text);

	// remind time
	TextView remindTime = (TextView) container.findViewById(R.id.task_today_time);
	long time = task.getmRemindTime().getmMills();
	if (mTask.getmCycleTime() == -1) {
	    time += TimeUtil.getNextDayZero();
	}
	String strtmp = TimeUtil.forTimeForHourAndSeconed(time);
	remindTime.setText(strtmp);

	if (task.isFinished()) {
	    // 已完成
	    taskStatusBackground
	    .setBackgroundResource(R.drawable.task_has_finished);
	    //            taskStatusView.setText(getString(R.string.task_finished));
	    taskStatusBackground.setEnabled(false);
	} else {
	    // 过期的任务状态置成未完成
	    if (task.isOverdued(mTask.getmCycleTime() != -1)) {
		taskStatusBackground
		.setBackgroundResource(R.drawable.task_no_fished);
		//                taskStatusView.setText(getString(R.string.task_fail));
		taskStatusBackground.setEnabled(false);
	    } else {

		taskStatusBackground.setBackgroundResource(R.drawable.task_to_finish);
		//                taskStatusView.setText(getString(R.string.task_undergoing));
		taskStatusBackground.setEnabled(true);
	    }
	}        
    }


}
