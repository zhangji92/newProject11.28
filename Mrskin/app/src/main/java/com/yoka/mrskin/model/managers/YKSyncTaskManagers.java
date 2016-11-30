package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKSyncTaskManagers extends YKManager
{
	// 下载已添加任务列表
	private static final String TASK_DOWNLOAD = getBase() + "task/list";
	// 上传已添加地址
	private static final String TASK_UPLOAD = getBase() + "task/save";
	// 通知服务器添加
	private static final String TASK_ADD = getBase() + "task/add";
	// 通知服务器删除
	private static final String TASK_REMOVE = getBase() + "task/del";
	// 查询单个新任务
	private static final String TASK_GET = getBase() + "task/id";

	private static YKSyncTaskManagers singleton = null;
	private static Object lock = new Object();

	public static YKSyncTaskManagers getInstance() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new YKSyncTaskManagers();
			}
		}
		return singleton;
	}

	private YKSyncTaskManagers()
	{

	}

	public YKHttpTask uploadTask(ArrayList<YKTask> nativeTaskList,
			final SyncTaskCallBack callback) {
		if (nativeTaskList == null) {
			return null;
		}
		JSONArray array = new JSONArray();
		for (int i = 0; i < nativeTaskList.size(); i++) {
			YKTask task = nativeTaskList.get(i);
			if (task == null) {
				continue;
			}
			array.put(task.toJson());
		}

		HashMap<String, JSONArray> parameters = new HashMap<String, JSONArray>();
		parameters.put("tasks", array);

		return super.postURL4JsonArray(TASK_UPLOAD, parameters, new Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {
				printRequestResult("uploadTask", object, result);

				// do callback
				if (callback != null) {
					callback.callback(result);
				}
			}
		});
	}

	public YKHttpTask downLoadTask(final DownloadTaskCallBack callback) {
		return super.postURL(TASK_DOWNLOAD, null, new Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {
				printRequestResult("downLoadTask", object, result);
				ArrayList<YKTask> taska = null;
				if (result.isSucceeded()) {
					JSONArray tmpArray = object.optJSONArray("tasks");
					if (tmpArray != null) {
						taska = new ArrayList<YKTask>();
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								taska.add(YKTask.parse(tmpArray
										.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				// do callback
				if (callback != null) {
					callback.callback(taska, result);
				}
			}
		});
	}

	public YKHttpTask addTaskSyncToServer(String authorToken,String parentTaskId,
			final SyncTaskCallBack callback) {
		if (TextUtils.isEmpty(parentTaskId)) {
			return null;
		}
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("tid", parentTaskId);
		parameters.put("authtoken", authorToken);
		return super.postURL(TASK_ADD, parameters, new Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {
				printRequestResult("addTaskSyncToServer", object, result);
				// do callback
				if (callback != null) {
					callback.callback(result);
				}
			}
		});
	}

	public YKHttpTask removeTaskSyncToServer(String parentTaskId,
			final SyncTaskCallBack callback) {
		if (TextUtils.isEmpty(parentTaskId)) {
			return null;
		}
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("tid", parentTaskId);
		return super.postURL(TASK_REMOVE, parameters, new Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {
				printRequestResult("removeTaskSyncToServer", object, result);
				// do callback
				if (callback != null) {
					callback.callback(result);
				}
			}
		});
	}

	public YKHttpTask getNewTaskFromServer(String parentTaskId,
			final GetTaskByIdCallBack callback) {
		if (TextUtils.isEmpty(parentTaskId)) {
			return null;
		}
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("tid", parentTaskId);
		return super.postURL(TASK_GET, parameters, new Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {
				printRequestResult("getNewTaskFromServer", object, result);
				YKTask yktask = null;
				if (result.isSucceeded()) {
					JSONObject taskObj = object.optJSONObject("task");
					if (taskObj != null) {
						yktask = YKTask.parse(taskObj);
					}
				}

				// do callback
				if (callback != null) {
					callback.callback(yktask, result);
				}
			}
		});
	}

	public static interface SyncTaskCallBack
	{
		public void callback(YKResult result);
	}

	public static interface DownloadTaskCallBack
	{
		public void callback(ArrayList<YKTask> taskList, YKResult result);
	}

	public static interface GetTaskByIdCallBack
	{
		public void callback(YKTask task, YKResult result);
	}
}
