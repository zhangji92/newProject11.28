package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.PlanTask;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 美丽计划
 * @author zlz
 * @Data 2016年8月5日
 */
public class YKPlanTaskManager extends YKManager{
	private static final String PATH = getBase() + "finish/subtask";
	private static YKPlanTaskManager singleton = null;
	private static Object lock = new Object();

	public static YKPlanTaskManager getInstance() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new YKPlanTaskManager();
			}
		}
		return singleton;
	}



	/**
	 * 请求网络，完成任务
	 */

	public void requestFinishTask(String authtoken,ArrayList<JSONObject>_taskList,final Callback callback){
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("authtoken",authtoken);

		parameters.put("tasklist", _taskList);
		super.postPlanURL(PATH, parameters, new com.yoka.mrskin.model.managers.base.Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {

				if (callback != null) {
					callback.callback(result);
				}

			}

		});



	}
	



	public interface Callback
	{
		public void callback(YKResult result);
	}


}
