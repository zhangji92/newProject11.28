package com.yoka.mrskin.model.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.yoka.mrskin.main.AppContext;
import com.yoka.mrskin.model.base.YKResult;

/**
 * 
 * @author z l l
 * @date 2014-11-17
 * 
 */
public class AsyncHttpMethod
{
    private static final String TAG = "AsyncHttpMethod";
    private static AsyncHttpClient mClient = new AsyncHttpClient();

    /**
     * get请求
     * 
     * @param url
     *            请求地址
     * @param map
     *            参数
     * @param callBack
     *            回调函数
     */

    public static void init() {
        mClient.setTimeout(1000 * 40);
    }

    public static YKHttpTask requestByGet(final String url,
            Map<String, String> map, final CallBack callBack) {
        Log.d(TAG, "get url[" + url + "] ");

        // create task
        final YKHttpTask task = new YKHttpTask("GET", map, callBack, url);

        // handle header
        Header[] headers = AsyncHttpMethod.handleHeaders(map);

        // do request
        RequestHandle handler = mClient.get(AppContext.getInstance(), url, headers, null,
                new BinaryHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                            byte[] binaryData) {
                        // handle result
                        YKResult result = new YKResult();
                        result.succeed();
                        // handle response headers
                        HashMap<String, String> responseHeaders = new HashMap<String, String>();
                        if (headers != null) {
                            Header header;
                            for (int i = 0; i < headers.length; ++i) {
                                header = headers[i];
                                responseHeaders.put(header.getName(),
                                        header.getValue());
                            }
                        }

                        // do callback
                        callBack.success(task, binaryData, responseHeaders,
                                result);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                            byte[] binaryData, Throwable error) {
                        YKResult result = new YKResult();
                        if (error != null) {
                            result.setMessage(null);
                        }
                        result.fail();
                        callBack.success(task, null, null, result);
                    }
                });
        task.setRequestHandler(handler);
        return task;
    }
    public static YKHttpTask requestByGet(final String url,
            Map<String, String> map,HashMap<String, String> params, final CallBack callBack) {
        Log.d(TAG, "get url[" + url + "] ");

        // create task
        final YKHttpTask task = new YKHttpTask("GET", map, callBack, url);

        // handle header
        Header[] headers = AsyncHttpMethod.handleHeaders(map);

        // do request
        RequestHandle handler = mClient.get(AppContext.getInstance(), url, headers, new RequestParams(params),
                new BinaryHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                            byte[] binaryData) {
                        // handle result
                        YKResult result = new YKResult();
                        result.succeed();
                        // handle response headers
                        HashMap<String, String> responseHeaders = new HashMap<String, String>();
                        if (headers != null) {
                            Header header;
                            for (int i = 0; i < headers.length; ++i) {
                                header = headers[i];
                                responseHeaders.put(header.getName(),
                                        header.getValue());
                            }
                        }

                        // do callback
                        callBack.success(task, binaryData, responseHeaders,
                                result);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                            byte[] binaryData, Throwable error) {
                        YKResult result = new YKResult();
                        if (error != null) {
                            result.setMessage(null);
                        }
                        result.fail();
                        callBack.success(task, null, null, result);
                    }
                });
        task.setRequestHandler(handler);
        return task;
    }
    /**
     * post请求
     * 
     * @param url
     *            请求地址
     * @param map
     *            参数
     * @param callBack
     *            回调函数
     * @return
     */
    public static YKHttpTask requestByPost(String url, String postContent,
            final CallBack callBack) {
        Log.d(TAG, "post url[" + url + "] ");
        // create task
        final YKHttpTask task = new YKHttpTask("POST", null, callBack, url);

        // handle post content
        StringEntity entity = null;
        try {
            entity = new StringEntity(postContent, "utf-8");
        } catch (Exception e) {
        }
        try {
            Log.d(TAG, "POST JSON IS =" + postContent);
        } catch (Exception e) {
        }
        System.out.println("result.getCode() do post");
        // do POST
        RequestHandle handler = mClient.post(AppContext.getInstance(), url, null, entity,
                "application/json", new BinaryHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                            byte[] binaryData) {
                        System.out.println("result.getCode() do post success");
                        try {
                            Log.d(TAG, "result="
                                    + new String(binaryData, "utf-8"));
                        } catch (Exception e) {
                        }
                        YKResult result = new YKResult();
                        result.succeed();

                        HashMap<String, String> responseHeaders = new HashMap<String, String>();
                        if (headers != null) {
                            Header header;
                            for (int i = 0; i < headers.length; ++i) {
                                header = headers[i];
                                responseHeaders.put(header.getName(),
                                        header.getValue());
                            }
                        }
                        System.out.println("AsyncHttpMethod -----------  succeed");
                        System.out.println("result.getCode() do post callback");
                        callBack.success(task, binaryData, responseHeaders,
                                result);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                            byte[] binaryData, Throwable error) {
                        System.out.println("result.getCode() do post fail");
                        YKResult result = new YKResult();
                        result.fail();
                        System.out.println("AsyncHttpMethod -----------  error = " + error.toString());
                        if (error != null) {
                            result.setMessage(null);
                        }
                        callBack.success(task, null, null, result);

                    }
                });
        task.setRequestHandler(handler);
        return task;
    }

    /**
     * 上传文件
     * 
     * @param url
     * @param map
     * @param callBack
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static YKHttpTask requestPostFile(String url,
            HashMap<String, Object> content, HashMap<String, String> map,
            final CallBack callBack) {
        // create task
        final YKHttpTask task = new YKHttpTask("POST", map, callBack, url);
        // handle header
        Header[] headers = AsyncHttpMethod.handleHeaders(map);

        // handle post content
        RequestParams params = new RequestParams();

        if (content != null) {
            Iterator iter = content.entrySet().iterator();

            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                Object val = entry.getValue();
                if (val instanceof String) {
                    params.add(key, (String) val);
                } else if (val instanceof File) {
                    try {
                        params.put(key, (File) val, "image/jpg");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        RequestHandle handler = mClient.post(AppContext.getInstance(), url, headers, params, "",
                new BinaryHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                            byte[] binaryData) {
                        YKResult result = new YKResult();
                        result.succeed();

                        HashMap<String, String> responseHeaders = new HashMap<String, String>();
                        if (headers != null) {
                            Header header;
                            for (int i = 0; i < headers.length; ++i) {
                                header = headers[i];
                                responseHeaders.put(header.getName(),
                                        header.getValue());
                            }
                        }
                        callBack.success(task, binaryData, responseHeaders,
                                result);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                            byte[] binaryData, Throwable error) {
                        YKResult result = new YKResult();
                        result.fail();
                        if (error != null) {
                            result.setMessage(null);
                        }
                        callBack.success(task, null, null, result);

                    }
                });
        task.setRequestHandler(handler);
        return task;
    }

    @SuppressWarnings("rawtypes")
    private static Header[] handleHeaders(Map<String, String> map) {
        Header[] headers = null;
        if (map != null) {
            headers = new Header[map.size()];

            Iterator iter = map.entrySet().iterator();
            int i = 0;
            Header header;
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(val)) {
                    continue;
                }
                header = new BasicHeader(key, val);
                headers[i] = header;
                i++;
            }
        }
        return headers;
    }
}
