package com.yoka.mrskin.model.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKImage;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.base.Callback;
import com.yoka.mrskin.model.managers.base.YKManager;
/**
 * 点评商品
 * 
 */
public class YKCommendShopManagers extends YKManager
{
	private static final String PATH = getBase() + "add/comment";

	private static YKCommendShopManagers singleton = null;
	private static Object lock = new Object();


	public static YKCommendShopManagers getInstance() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new YKCommendShopManagers();
			}
		}
		return singleton;
	}


	public YKHttpTask postYKcommentShop(final CommentShopCallback callback,String product_id,String rating,String title,String description,ArrayList<YKImage> images) {

		// 后期增加的字段
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("product_id", product_id);
		parameters.put("rating", rating);
		parameters.put("title", title);
		parameters.put("description", description);
		parameters.put("images", images);

		// do request
		return super.postURL(PATH, parameters, new Callback() {

			@Override
			public void doCallback(YKHttpTask task, JSONObject object,
					YKResult result) {
				printRequestResult("postYKcommentShop", object, result);

				if (result.isSucceeded()) {

				}
				// do callback
				if (callback != null) {
					callback.callback(result);
				}



			}
		});
	}

	public interface CommentShopCallback{
		public void callback(YKResult result); 
	}
}
