package com.jrd.loan.net.framework;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.text.TextUtils;

import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.encrypt.RSAUtil;
import com.jrd.loan.parser.JsonGenerator;
import com.jrd.loan.parser.Parser;
import com.jrd.loan.util.LogUtil;
import com.loopj.android.http.RequestParams;

public class HttpTask {
  private final static String TAG = "HttpRequest";
  private final static String JSON_PARAM = "paras";
  private final static String BASE_URL = JrdConfig.getBaseUrl();
  public final static int METHOD_GET = 0;
  public final static int METHOD_POST = 1;

  private Context context;
  private Parser parser;
  private final RequestParams params;
  private final String url;
  private final int method;
  private boolean useJson;
  private JsonGenerator jsonGenerator;

  private HttpTask(Context context, String url, int method) {
    this.context = context;
    this.params = new RequestParams();
    this.params.setContentEncoding("UTF-8");

    this.url = new StringBuffer(BASE_URL).append(url).toString();
    this.method = method;

    LogUtil.d(TAG, this.url);
  }

  public static HttpTask getHttpTask(Context context, String url, int method) {
    return new HttpTask(context, url, method);
  }

  public static HttpTask getHttpTask(Context context, String url) {
    return getHttpTask(context, url, METHOD_POST);
  }

  public void setUseJson(boolean useJson) {
    this.useJson = useJson;
    this.jsonGenerator = JsonGenerator.getJsonGenerator();
  }

  public void putParam(String key, String value) {
    if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
      return;
    }

    if (!this.useJson) {
      this.params.put(key, value);
    } else {
      this.jsonGenerator.putParam(key, value);
    }
  }

  public void putParam(String key, boolean value) {
    if (TextUtils.isEmpty(key)) {
      return;
    }

    if (!this.useJson) {
      this.params.put(key, value);
    } else {
      this.jsonGenerator.putParam(key, value);
    }
  }

  public void putParam(String key, int value) {
    if (TextUtils.isEmpty(key)) {
      return;
    }

    if (!this.useJson) {
      this.params.put(key, value);
    } else {
      this.jsonGenerator.putParam(key, value);
    }
  }

  public void putParam(String key, long value) {
    if (TextUtils.isEmpty(key)) {
      return;
    }

    if (!this.useJson) {
      this.params.put(key, value);
    } else {
      this.jsonGenerator.putParam(key, value);
    }
  }

  public void putParam(String key, double value) {
    if (TextUtils.isEmpty(key)) {
      return;
    }

    if (!this.useJson) {
      this.params.put(key, value);
    } else {
      this.jsonGenerator.putParam(key, value);
    }
  }

  public void putParam(HashMap<String, String> paramsMap) {
    if (paramsMap == null || paramsMap.isEmpty()) {
      return;
    }

    Set<Entry<String, String>> setParams = paramsMap.entrySet();

    if (!this.useJson) {
      for (Entry<String, String> entry : setParams) {
        this.params.put(entry.getKey(), entry.getValue());
      }
    } else {
      for (Entry<String, String> entry : setParams) {
        this.jsonGenerator.putParam(entry.getKey(), entry.getValue());
      }
    }
  }

  public void setParser(Parser parser) {
    this.parser = parser;
  }

  @SuppressWarnings("unchecked")
  public <T> void runTask(Class<T> tClass, OnHttpTaskListener<T> httpTaskListener) {
    if (this.method == METHOD_POST) {
      if (!this.useJson) {
        LogUtil.d(TAG, "--------------- runTask--> " + this.params.toString());

        HttpRequest.excuteHttpPost(this.context, this.parser, this.url, this.params, tClass, httpTaskListener);
      } else {// 以Json方式传递, 并且用RSA加密
        String jsonStr = this.jsonGenerator.generateJson();
        String rsaJsonStr = RSAUtil.encryptBase64ByPublicKey(jsonStr);

        LogUtil.d(TAG, "--------------- runTask--> " + jsonStr);
        LogUtil.d(TAG, "--------------- runTask--> " + rsaJsonStr);

        this.params.put(JSON_PARAM, rsaJsonStr);
        HttpRequest.excuteHttpPost(this.context, this.parser, this.url, this.params, tClass, httpTaskListener);
      }
    } else {
      LogUtil.d(TAG, "--------------- runTask--> " + this.params.toString());

      HttpRequest.excuteHttpGet(this.context, this.parser, this.url, this.params, tClass, httpTaskListener);
    }
  }
}
