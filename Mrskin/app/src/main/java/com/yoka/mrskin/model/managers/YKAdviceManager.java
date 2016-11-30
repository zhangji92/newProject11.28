package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 意见反馈
 * @author zlz
 * @Data 2016年7月7日
 */
public class YKAdviceManager extends YKManager{

	private static final String PATH = "user/feedback";

	private static YKAdviceManager singleton = null;
	private static Object lock = new Object();

	public static YKAdviceManager getInstance() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new YKAdviceManager();
			}
		}
		return singleton;
	}

	public void commitAdvice(String authToken,String content,String contact,String phoneToken,final AdviceCallback callBack){
		String url = getBase() + PATH;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("authtoken", authToken);
		parameters.put("content", content);
		parameters.put("contact", contact);
		parameters.put("phonetoken", phoneToken);

		super.postURL(url, parameters, new Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {
				if(callBack != null){
					callBack.adviceCallback(result);
				}

			}
		});

	}

	public interface AdviceCallback{
		public void adviceCallback(YKResult result); 
	}





}
