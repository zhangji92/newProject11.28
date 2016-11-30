package com.jrd.loan.net.framework;

import android.content.Context;

import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.encrypt.MD5Util;
import com.jrd.loan.parser.DefaultParser;
import com.jrd.loan.parser.Parser;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.UuidUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class HttpRequest {
    private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";

    public final static String VERSION = "1.0";
    private static final int TIME_OUT = 30000;
    private static final String TAG = "HttpRequest";
    private static final AsyncHttpClient httpClient = new AsyncHttpClient();

    static {
        httpClient.setResponseTimeout(TIME_OUT);
        httpClient.setConnectTimeout(TIME_OUT);
        httpClient.setMaxRetriesAndTimeout(0, TIME_OUT);
    }

    /**
     * Get请求
     *
     */
    public static <T> void excuteHttpGet(Context context, Parser<T> parser, String url, final RequestParams params, final Class<T> tClass, final OnHttpTaskListener<T> httpTaskListener) {
        httpClient.get(url, params, getHttpResponseHandler(context, parser, tClass, httpTaskListener));
    }

    /**
     * post请求
     *
     */
    public static <T> void excuteHttpPost(Context context, Parser<T> parser, String url, final RequestParams params, final Class<T> tClass, final OnHttpTaskListener<T> httpTaskListener) {
        httpClient.post(context, url, addHeader(), params, CONTENT_TYPE, getHttpResponseHandler(context, parser, tClass, httpTaskListener));
    }

    /**
     * 取消post请求
     *
     */
    public static void CancelHttp(Context context) {
        httpClient.cancelRequests(context, true);
    }

    private static <T> TextHttpResponseHandler getHttpResponseHandler(final Context context, final Parser<T> parser, final Class<T> tClass, final OnHttpTaskListener<T> httpTaskListener) {
        return new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                if (httpTaskListener != null) {
                    httpTaskListener.onStart();
                }
            }

            @Override
            public void onSuccess(int code, Header[] header, String content) {
                LogUtil.d(TAG, "---------------- post返回值-->" + content);

                T bean;

                if (parser == null) {
                    bean = new DefaultParser<T>().parser(content, tClass);
                } else {
                    bean = parser.parser(content, tClass);
                }
                if (httpTaskListener != null) {
                    httpTaskListener.onFinishTask(bean);
                }
            }

            @Override
            public void onFailure(int code, Header[] header, String content, Throwable throwable) {
                LogUtil.d(TAG, "------------- code = " + code + "   --- content = " + content);

                if (httpTaskListener != null) {
                    httpTaskListener.onTaskError(code);
                }
            }
        };
    }

    private static Header[] addHeader() {
        if (UserInfoPrefs.isLogin()) {// 登录
            BasicHeader deviceHeader = new BasicHeader("deviceid", UuidUtil.getUUID());
            BasicHeader verHeader = new BasicHeader("ver", VERSION);
            String timestamp = String.valueOf(System.currentTimeMillis());
            String sessionId = UserInfoPrefs.getSessionId();
            BasicHeader timestampHeader = new BasicHeader("timestamp", timestamp);
            BasicHeader signHeader = new BasicHeader("sign", MD5Util.getMD5Str(sessionId + timestamp + JrdConfig.getAppKey()));
            BasicHeader authorizationHeader = new BasicHeader("authorization", sessionId);
            return new BasicHeader[]{deviceHeader, verHeader, timestampHeader, signHeader, authorizationHeader};
        } else {// 未登录
            BasicHeader deviceHeader = new BasicHeader("deviceid", UuidUtil.getUUID());
            BasicHeader verHeader = new BasicHeader("ver", VERSION);
            BasicHeader timestampHeader = new BasicHeader("timestamp", String.valueOf(System.currentTimeMillis()));
            return new BasicHeader[]{deviceHeader, verHeader, timestampHeader};
        }
    }
}
