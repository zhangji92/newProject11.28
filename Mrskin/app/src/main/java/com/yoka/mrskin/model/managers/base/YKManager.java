package com.yoka.mrskin.model.managers.base;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.yoka.mrskin.login.YKUser;
import com.yoka.mrskin.main.AppContext;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.http.AsyncHttpMethod;
import com.yoka.mrskin.model.http.CallBack;
import com.yoka.mrskin.model.http.IHttpTaskObserver;
import com.yoka.mrskin.model.http.YKHttpTask;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.YKDeviceInfo;

public class YKManager
{
	@SuppressWarnings("unused")
	private static final String TAG = "YKManager";
	private static boolean isDebug = false;//true 测试环境    false 正式环境

	public static final String BASE = "http://fujun.yoka.com/api/";//正式环境
	public static final String DEBUG_BASE = "http://192.168.57.91:8038/fujun/api/";//测试环境

	public static final String FOUR = "http://hzp.yoka.com/fujun/web/";//正式环境
	public static final String DEBUG_FOUR = "http://192.168.57.91:8038/fujun/web/";//测试环境

	public static final String AD_URL = "http://mobservices3.yoka.com/service.ashx";//正式环境
	public static final String DEBUG_AD_URL ="http://10.0.50.242:9081/service.ashx";//测试环境

	public static final String URL = "http://hzp.yoka.com/fujun/web/skin/show?clientid=";//正式环境
	public static final String DEBUG_URL = "http://192.168.57.91:8038/fujun/web/skin/show?clientid=";//测试环境

	public static final String DEFAULT_ERROR_MESSAGE = "网络不给力，请稍后再试";
	private static final String DEFAULT_DATA_ERROR_MESSAGE = "数据异常";

	private static IHttpTaskObserver httpTaskObserver;

	private boolean checkNetWorkAvailable(Callback callback) {
		if (!AppUtil.isNetworkAvailable(AppContext.getInstance())) {
			YKResult ykResult = new YKResult();
			ykResult.fail();
			ykResult.setMessage(DEFAULT_ERROR_MESSAGE);
			callback.doCallback(null, null, ykResult);
			return false;
		}
		return true;
	}

	public YKHttpTask getURL(final String url,
			final HashMap<String, String> parameters, final Callback callback) {
		// get
		// code = 0 成功 code = 1 失败
		if (!checkNetWorkAvailable(callback)) {
			return null;
		}
		YKHttpTask task = AsyncHttpMethod.requestByGet(url, parameters,
				new CallBack() {
			@Override
			public void success(YKHttpTask task, byte[] bt,
					HashMap<String, String> headers, YKResult result) {
				handleResult(task, bt, result, callback);
				observeTaskFinished(task);
			}
		});
		observeTask(task);
		return task;
	}

	public YKHttpTask getURL(final String url,
			final HashMap<String, String> header,
			final HashMap<String, String> parameters, final Callback callback) {
		// get
		// code = 0 成功 code = 1 失败
		if (!checkNetWorkAvailable(callback)) {
			return null;
		}
		YKHttpTask task = AsyncHttpMethod.requestByGet(url, header, parameters,
				new CallBack() {
			@Override
			public void success(YKHttpTask task, byte[] bt,
					HashMap<String, String> headers, YKResult result) {
				handleResult(task, bt, result, callback);
				observeTaskFinished(task);
			}
		});
		observeTask(task);
		return task;
	}

	// post
	// code = 0 成功 code = 1 失败
	public YKHttpTask postURL(final String url,
			final HashMap<String, Object> parameters, final Callback callback) {
		if (!checkNetWorkAvailable(callback)) {
			return null;
		}
		JSONObject object = addDefaultParams(parameters);
		Log.i("-----loginobject---", object.toString());
		YKHttpTask task = AsyncHttpMethod.requestByPost(url, object.toString(),
				new CallBack() {
			@Override
			public void success(YKHttpTask task, byte[] bt,
					HashMap<String, String> headers, YKResult result) {

				handleResult(task, bt, result, callback);
				observeTaskFinished(task);
			}
		});
		observeTask(task);
		return task;
	}
	// post
	// code = 0 成功 code = 1 失败
	public YKHttpTask postPlanURL(final String url,
			final HashMap<String, Object> parameters, final Callback callback) {
		if (!checkNetWorkAvailable(callback)) {
			return null;
		}
		JSONObject object = addPlanDefaultParams(parameters);
		Log.i("-----loginobject---", object.toString());
		YKHttpTask task = AsyncHttpMethod.requestByPost(url, object.toString(),
				new CallBack() {
			@Override
			public void success(YKHttpTask task, byte[] bt,
					HashMap<String, String> headers, YKResult result) {

				handleResult(task, bt, result, callback);
				observeTaskFinished(task);
			}
		});
		observeTask(task);
		return task;
	}

	// post
	// code = 0 成功 code = 1 失败
	public YKHttpTask postURL4JsonObj(final String url,
			final HashMap<String, JSONObject> parameters,
			final Callback callback) {
		if (!checkNetWorkAvailable(callback)) {
			return null;	
		}
		JSONObject object = addDefaultParams4JsonObj(parameters);
		Log.i("-----loginobject---", object.toString());
		YKHttpTask task = AsyncHttpMethod.requestByPost(url, object.toString(),
				new CallBack() {
			@Override
			public void success(YKHttpTask task, byte[] bt,
					HashMap<String, String> headers, YKResult result) {
				handleResult(task, bt, result, callback);
				observeTaskFinished(task);
			}
		});
		observeTask(task);
		return task;
	}

	public YKHttpTask postURL4JsonArray(final String url,final HashMap<String, JSONArray> parameters, final Callback callback) {
		if (!checkNetWorkAvailable(callback)) {
			return null;
		}
		JSONObject object = addDefaultParams4JsonArray(parameters);
		// Log.i("-----loginobject---", object.toString());
		YKHttpTask task = AsyncHttpMethod.requestByPost(url, object.toString(),new CallBack() {
			@Override
			public void success(YKHttpTask task, byte[] bt,HashMap<String, String> headers, YKResult result) {
				handleResult(task, bt, result, callback);
				observeTaskFinished(task);
			}
		});
		observeTask(task);
		return task;
	}

	// postFile 下载
	// code = 0 成功 code = 1 失败
	public YKHttpTask postFile(final String url,
			final HashMap<String, Object> parameters, final String filePath,
			final Callback callback) {
		if (!checkNetWorkAvailable(callback)) {
			return null;
		}
		YKHttpTask task = AsyncHttpMethod.requestPostFile(url, parameters,
				null, new CallBack() {
			@Override
			public void success(YKHttpTask task, byte[] bt,
					HashMap<String, String> headers, YKResult result) {
				handleResult(task, bt, result, callback);
				observeTaskFinished(task);
			}
		});
		observeTask(task);
		return task;
	}

	//
	// @SuppressWarnings("rawtypes")
	// public YKHttpTask postFiles(final String url,
	// HashMap<String, String> parameters, HashMap<String, String> files,
	// final Callback callback) {
	// if (!checkNetWorkAvailable(callback)) {
	// return null;
	// }
	// // HashMap<String, Object> params = new HashMap<String, Object>();
	// // if (parameters != null) {
	// // Iterator iter = parameters.entrySet().iterator();
	// // while (iter.hasNext()) {
	// // Map.Entry entry = (Map.Entry) iter.next();
	// // Object key = entry.getKey();
	// // Object val = entry.getValue();
	// // params.put((String) key, val);
	// // }
	// // }
	//
	// HashMap<String, Object> params = addDefaultParamsForFiles(parameters);
	// if (files != null) {
	// Iterator iter = files.entrySet().iterator();
	// while (iter.hasNext()) {
	// Map.Entry entry = (Map.Entry) iter.next();
	// Object key = entry.getKey();
	// Object val = entry.getValue();
	// try {
	// File f = new File((String) val);
	// if (f.exists()) {
	// params.put((String) key, f);
	// }
	// } catch (Exception e) {
	// }
	//
	// }
	// }
	// return AsyncHttpMethod.requestPostFile(url, params, null,
	// new CallBack() {
	// @Override
	// public void success(YKHttpTask task, byte[] bt,
	// HashMap<String, String> headers, YKResult result) {
	// try {
	// handleResult(task, bt, result, callback);
	// } catch (Exception e) {
	// }
	// }
	// });
	// }

	@SuppressWarnings("rawtypes")
	public YKHttpTask postFiles(final String url,
			HashMap<String, Object> parameters, HashMap<String, String> files,
			final Callback callback) {
		if (!checkNetWorkAvailable(callback)) {
			return null;
		}
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}

		HashMap<String, Object> content = addDefaultParamsForFiles(parameters);
		if (files != null) {

			Iterator iter = files.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				try {
					File f = new File((String) val);
					if (f.exists()) {
						content.put((String) key, f);
					}
				} catch (Exception e) {
				}

			}
		}
		System.out.println("upload skin data manager = " + content.toString());
		YKHttpTask task = AsyncHttpMethod.requestPostFile(url, content, null,
				new CallBack() {
			@Override
			public void success(YKHttpTask task, byte[] bt,
					HashMap<String, String> headers, YKResult result) {
				File tempFile = new File("/storage/emulated/0/Yoka/Images/01.png");
				if(tempFile.exists()){

					tempFile.delete();
				}
				handleResult(task, bt, result, callback);
				observeTaskFinished(task);
			}
		});
		observeTask(task);
		return task;
	}



	private void handleResult(YKHttpTask task, byte[] bt, YKResult result,
			Callback callback) {
		JSONObject jsonObject = null;

		if (result.isSucceeded()) {
			// create json object;
			try {
				String tmpString = new String(bt, "utf-8");
				Log.i("res", tmpString);
				try {
					jsonObject = new JSONObject(tmpString);
				} catch (JSONException e) {
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.fail();
				result.setMessage(DEFAULT_DATA_ERROR_MESSAGE);
			}
			int code = -1;

			if (jsonObject != null) {
				try {
					code = jsonObject.optInt("code");
				} catch (Exception e) {
					result.fail();
					result.setMessage(DEFAULT_DATA_ERROR_MESSAGE);
				}

				if (code == 0) {
					result.succeed();
				} else {
					String message = null;
					try {
						message = jsonObject.optString("message");
					} catch (Exception e) {
						message = DEFAULT_DATA_ERROR_MESSAGE;
					}
					result.fail();
					result.setMessage(message);
					if (TextUtils.isEmpty((String) result.getMessage())) {
						result.setMessage(DEFAULT_ERROR_MESSAGE);
					}
				}
			}
		} else {
			if (TextUtils.isEmpty((String) result.getMessage())) {
				result.setMessage(DEFAULT_ERROR_MESSAGE);
			}
		}
		if (callback != null) {
			callback.doCallback(task, jsonObject, result);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JSONObject addDefaultParams(HashMap<String, Object> params) {
		JSONObject object = new JSONObject();

		// system json
		JSONObject systemObject = new JSONObject();
		addDefaultParamsDefault(systemObject);
		try {
			object.put("system", systemObject);
		} catch (JSONException e) {
		}

		// post json
		if (params != null) {
			Iterator<Entry<String, Object>> iter = params.entrySet().iterator();
			JSONObject postObject = new JSONObject();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				if (key != null && value != null) {
					if (value instanceof ArrayList<?>) {
						try {
							JSONArray jsonArray = new JSONArray();
							ArrayList<String> tmpArrayList = (ArrayList<String>) value;
							for (String tmpString : tmpArrayList) {
								jsonArray.put(tmpString);
							}
							postObject.put((String) key, jsonArray);
						} catch (Exception e) {
						}
					} else if (value instanceof Integer) {
						try {
							postObject.put((String) key,
									((Integer) value).intValue());
						} catch (Exception e) {
						}
					} else if (value instanceof Boolean) {
						try {
							postObject.put((String) key,
									((Boolean) value).booleanValue());
						} catch (Exception e) {
						}
					} else if (value instanceof Float) {
						try {
							postObject.put((String) key,
									((Float) value).floatValue());
						} catch (Exception e) {
						}
					} else {
						try {
							postObject.put((String) key, (String) value);
						} catch (JSONException e) {

						}

					}
				}
			}
			try {
				object.put("postdata", postObject);
			} catch (JSONException e) {
			}
		}
		return object;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JSONObject addPlanDefaultParams(HashMap<String, Object> params) {
		JSONObject object = new JSONObject();

		// system json
		JSONObject systemObject = new JSONObject();
		addDefaultParamsDefault(systemObject);
		try {
			object.put("system", systemObject);
		} catch (JSONException e) {
		}

		// post json
		if (params != null) {
			Iterator<Entry<String, Object>> iter = params.entrySet().iterator();
			JSONObject postObject = new JSONObject();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				if (key != null && value != null) {
					if (value instanceof ArrayList<?>) {
						try {
							JSONArray jsonArray = new JSONArray();
							ArrayList<JSONObject> tmpArrayList = (ArrayList<JSONObject>) value;
							for (JSONObject tmpString : tmpArrayList) {
								jsonArray.put(tmpString);
							}
							postObject.put((String) key, jsonArray);
						} catch (Exception e) {
						}
					} else if (value instanceof Integer) {
						try {
							postObject.put((String) key,
									((Integer) value).intValue());
						} catch (Exception e) {
						}
					} else if (value instanceof Boolean) {
						try {
							postObject.put((String) key,
									((Boolean) value).booleanValue());
						} catch (Exception e) {
						}
					} else if (value instanceof Float) {
						try {
							postObject.put((String) key,
									((Float) value).floatValue());
						} catch (Exception e) {
						}
					} else {
						try {
							postObject.put((String) key, (String) value);
						} catch (JSONException e) {

						}

					}
				}
			}
			try {
				object.put("postdata", postObject);
			} catch (JSONException e) {
			}
		}
		return object;
	}


	@SuppressWarnings("rawtypes")
	private JSONObject addDefaultParams4JsonObj(
			HashMap<String, JSONObject> parameters) {
		// TODO Auto-generated method stub
		JSONObject object = new JSONObject();
		// system json
		JSONObject systemObject = new JSONObject();
		addDefaultParamsDefault(systemObject);
		try {
			object.put("system", systemObject);
		} catch (JSONException e) {
		}

		// post json
		if (parameters != null) {
			Iterator<Entry<String, JSONObject>> iter = parameters.entrySet()
					.iterator();
			JSONObject postObject = new JSONObject();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				JSONObject value = (JSONObject) entry.getValue();
				if (key != null && value != null) {
					try {
						postObject.put((String) key, value);
					} catch (JSONException e) {
					}
				}
			}
			try {
				object.put("postdata", postObject);
			} catch (JSONException e) {
			}
		}

		return object;
	}


	private JSONObject addDefaultParamsDefault(JSONObject systemObject) {
		try {
			systemObject.put("app_version", YKDeviceInfo.getAppVersion());
		} catch (JSONException e) {
		}
		try {
			systemObject.put("os", "Android");
		} catch (JSONException e) {
		}
		try {
			systemObject.put("os_version", android.os.Build.VERSION.RELEASE);
		} catch (JSONException e) {
		}
		try {
			systemObject.put("device_model", android.os.Build.MODEL);
		} catch (JSONException e1) {
		}
		try {
			systemObject.put("clientid", YKDeviceInfo.getClientID());
		} catch (JSONException e) {
		}
		try {
			systemObject.put("user-agent", YKDeviceInfo.getUserAgent());
		} catch (JSONException e) {
		}
		try {
			systemObject.put("screenwidth", YKDeviceInfo.getScreenWidth());
		} catch (JSONException e) {
		}
		// 后期添加的
		// TODO: handle exception
		try {
			systemObject.put("api_version", "v8");
		} catch (JSONException e) {
		}
		try {
			systemObject.put("screenheight", YKDeviceInfo.getScreenHeight());
		} catch (JSONException e) {
		}
		// try {
		// systemObject.put("access_token",);
		// } catch (JSONException e) {
		// }
		if (YKCurrentUserManager.getInstance().isLogin()) {
			try {
				YKUser user = YKCurrentUserManager.getInstance().getUser();
				systemObject.put("uid", user.getId());
			} catch (JSONException e) {
			}
		}
		// }
		// try {
		// systemObject.put("is_push_on", AppUtil.getPushState());
		// } catch (JSONException e) {
		// }
		return systemObject;
	}


	/**
	 * 获取公共头参数
	 * 
	 * @return 公共头参数map
	 */
	public static HashMap<String, String> getAddHeaderMap(String interfaceId,
			Context context) {
		HashMap<String, String> headerMap = new HashMap<String, String>();
		// 获取设备信息
		// 获取用户id
		String uid = "";
		if (YKCurrentUserManager.getInstance().isLogin()) {
			YKUser user = YKCurrentUserManager.getInstance().getUser();
			uid = user.getId();
			headerMap.put("hucid", uid);
		}
		// 广告标识符
		String idfa = "Android";

		// 设置参数
		headerMap.put("hc", "809");
		headerMap.put("hmd", android.os.Build.MODEL);
		headerMap.put(
				"hsz",
				YKDeviceInfo.getScreenWidth() + "*"
						+ YKDeviceInfo.getScreenHeight());
		headerMap.put("hv", YKDeviceInfo.getAppVersion());
		headerMap.put("hsv", android.os.Build.VERSION.RELEASE);
		headerMap.put("hu", YKDeviceInfo.getClientID());
		headerMap.put("ha", AppUtil.getAppChannel(context));
		headerMap.put("ham", AppUtil.getCurrentNetworkTypeName(context));
		headerMap.put("hi", interfaceId);
		headerMap.put("idfa", idfa);
		headerMap.put("User-Agent", YKDeviceInfo.getUserAgent());
		// 将参数返回
		return headerMap;
	}

	/**
	 * 编码字符串为utf-8
	 * 
	 * @param str
	 *            原始字符串
	 * @return 编码后的字符串
	 * @throws UnsupportedEncodingException
	 */
	private String encodeStr(String str) throws UnsupportedEncodingException {
		return URLEncoder.encode(str, HTTP.UTF_8);
	}

	@SuppressWarnings("rawtypes")
	private JSONObject addDefaultParams4JsonArray(
			HashMap<String, JSONArray> parameters) {
		JSONObject object = new JSONObject();
		// system json
		JSONObject systemObject = new JSONObject();
		addDefaultParamsDefault(systemObject);
		try {
			object.put("system", systemObject);
		} catch (JSONException e) {
		}

		// post json
		if (parameters != null) {
			Iterator<Entry<String, JSONArray>> iter = parameters.entrySet()
					.iterator();
			JSONObject postObject = new JSONObject();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				JSONArray value = (JSONArray) entry.getValue();
				if (key != null && value != null) {
					try {
						postObject.put((String) key, value);
					} catch (JSONException e) {
					}
				}
			}
			try {
				object.put("postdata", postObject);
			} catch (JSONException e) {
			}
		}

		return object;
	}

	@SuppressWarnings("rawtypes")
	private HashMap<String, Object> addDefaultParamsForFiles(
			HashMap<String, Object> params) {

		HashMap<String, Object> content = new HashMap<String, Object>();
		// JSONObject object = new JSONObject();

		// system json
		JSONObject systemObject = addDefaultParamsDefault(new JSONObject());
		// try {
		// object.put("system", systemObject);
		// } catch (JSONException e) {
		// }
		content.put("system", systemObject.toString());
		// post json
		if (params != null) {
			Iterator<Entry<String, Object>> iter = params.entrySet().iterator();
			JSONObject postObject = new JSONObject();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				if (key != null && value != null) {
					try {
						postObject.put((String) key, (Object) value);
					} catch (JSONException e) {
					}
				}
			}
			// object = new JSONObject();
			// try {
			// object.put("postdata", postObject);
			// } catch (JSONException e) {
			// }
			content.put("postdata", postObject.toString());
		}
		return content;
	}

	protected static String getBase() {

		if(isDebug){
			return DEBUG_BASE;
		}
		return BASE;
	}

	public static String getAdUrl(){
		if(isDebug){
			return DEBUG_AD_URL;
		}
		return AD_URL;
	}
	public static String getUrl(){
		if(isDebug){
			return DEBUG_URL;
		}
		return URL;
	}

	public static String getFour(){
		if(isDebug){
			return DEBUG_FOUR;
		}
		return FOUR;
	}

	public void printRequestResult(String tag, JSONObject obj, YKResult result) {
		System.out.println("=============" + tag
				+ "============start==============");
		System.out.println("YKResult  " + result);
		if (obj == null) {
			System.out.println("JSONObject ==null");
		} else {
			System.out.println(obj.toString());
		}
		System.out.println("============end==============");
	}

	private void observeTask(YKHttpTask task) {
		if (task == null)
			return;
		if (httpTaskObserver != null) {
			httpTaskObserver.taskCreated(task);
		}
	}

	private void observeTaskFinished(YKHttpTask task) {
		if (task == null)
			return;
		if (httpTaskObserver != null) {
			httpTaskObserver.taskFinished(task);
		}
	}

	public static void init(IHttpTaskObserver observer) {
		httpTaskObserver = observer;
	}
}
