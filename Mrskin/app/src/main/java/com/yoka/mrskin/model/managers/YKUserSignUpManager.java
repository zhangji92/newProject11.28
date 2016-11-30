package com.yoka.mrskin.model.managers;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.YKManager;

public class YKUserSignUpManager extends YKManager {

	private static final String PATH = getBase()+"sign/up";
	private static YKUserSignUpManager singleton = null;
	private static Object lock = new Object();


	public static YKUserSignUpManager getInstance() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new YKUserSignUpManager();
			}
		}
		return singleton;
	}

	public YKHttpTask postUserSignUpManager(final String user_id,String money, final Callback callback) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("money", money);
		params.put("user_id", user_id);

		// String url = BASE + PATH;
		String url = PATH;
		return super.postURL(url, params, new com.yoka.mrskin.model.managers.base.Callback() {
			@Override
			public void doCallback(YKHttpTask task, JSONObject object, YKResult result) {
			
				String flag = null;
				String money = null;
				String sign_day = null;
				if (object != null) {
					try {
						flag=object.getString("flag");
						money=object.getString("money");
						sign_day=object.getString("sign_day");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (callback != null) {
					callback.callback(result, flag,sign_day,money);
				}
			}
		});
	}
	public interface Callback {
		public void callback(YKResult result, String code,String day,String money );
	}
}
