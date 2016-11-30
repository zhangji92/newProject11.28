package com.yoka.mrskin.model.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.util.YKFile;

public class YKTaskManagers extends YKManager
{
	private static final String PATH = getBase() + "task/refresh";
	private static final String PATH_LOADMORE = getBase() + "task/loadmore";
	public static String CACHE_NAME = "planDate";

	private static YKTaskManagers singleton = null;
	private static Object lock = new Object();
	private ArrayList<YKTask> mTaskList;
	private HashMap<String, YKTask> mTaskTable;

	public static YKTaskManagers getInstance() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new YKTaskManagers();
			}
		}
		return singleton;
	}

	private YKTaskManagers()
	{
		mTaskList = new ArrayList<YKTask>();
		mTaskTable = new HashMap<String, YKTask>();
	}

	public ArrayList<YKTask> getTaskList() {
		return mTaskList;
	}

	public YKTask getTask(String ID) {
		if (ID == null) return null;

		return mTaskTable.get(ID);
	}

	public void clearTaskJoinedStatus() {
		for (YKTask task : mTaskList) {
			task.setmIsAdd(false);
		}
		YKTaskManager.getInstnace().notifyTaskDataChanged();
	}

	public YKHttpTask refreshTaskList(final YKTaskCallback callback) {
		return super.postURL(PATH, null, new Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {

				if(null == object){
					return ;
				}
				printRequestResult("postYKTask", object, result);

				// parse json data
				ArrayList<YKTask> tasks = null;
				if (result.isSucceeded()) {
					JSONArray tmpArray = object.optJSONArray("tasks");
					if (tmpArray != null) {
						tasks = new ArrayList<YKTask>();
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								tasks.add(YKTask.parse(tmpArray
										.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}

				// update task list in memory
				if (tasks != null) {
					clearTaskList();
					addTasks(tasks);
				}
				// save tasks to file 
				saveDataToFile(tasks);

				// do callback
				if (callback != null) {
					callback.callback(result, tasks);
				}
			}
		});

	}

	public YKHttpTask loadmoreTaskList(final YKLoadMoreCallback callback,
			String task_id) {

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("task_id", task_id);
		return super.postURL(PATH_LOADMORE, parameters, new Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {
				printRequestResult("postYKTask", object, result);

				// parse json data
				ArrayList<YKTask> tasks = null;
				if (result.isSucceeded()) {
					JSONArray tmpArray = object.optJSONArray("tasks");
					if (tmpArray != null) {
						tasks = new ArrayList<YKTask>();
						for (int i = 0; i < tmpArray.length(); ++i) {
							try {
								tasks.add(YKTask.parse(tmpArray
										.getJSONObject(i)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				if (tasks != null) {
					addTasks(tasks);
				}
				// do callback
				if (callback != null) {
					callback.callback(result, tasks);
				}
			}
		});
	}

	private void clearTaskList() {
		mTaskList.clear();
		mTaskTable.clear();
	}
	private void addTasks(ArrayList<YKTask> taskList) {
		if (taskList == null) return;

		for (YKTask task : taskList) {
			if (task.getID() == null) continue;
			mTaskTable.put(task.getID(), task);
			mTaskList.add(task);
		}
	}

	private boolean saveDataToFile(ArrayList<YKTask> taska) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		byte[] data = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(taska);
			data = baos.toByteArray();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
		}

		YKFile.save(CACHE_NAME, data);
		return true;
	}

	private ArrayList<YKTask> loadDataFromFile() {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		byte[] data = YKFile.read(CACHE_NAME);
		try {
			bais = new ByteArrayInputStream(data);
			ois = new ObjectInputStream(bais);
			@SuppressWarnings("unchecked")
			ArrayList<YKTask> objectData = (ArrayList<YKTask>) ois.readObject();
			return objectData;
		} catch (Exception e) {
		} finally {
			try {
				ois.close();
			} catch (Exception e) {

			}
			try {
				bais.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	public ArrayList<YKTask> getPlanData() {
		return loadDataFromFile();
	}

}
