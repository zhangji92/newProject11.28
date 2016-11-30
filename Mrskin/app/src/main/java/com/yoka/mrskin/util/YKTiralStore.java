package com.yoka.mrskin.util;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.text.TextUtils;
import android.util.LruCache;

public class YKTiralStore
{

    private final static int CACHE_SIZE = 100;
    private LruCache<String, String> mContent;
    private String mName;

    public YKTiralStore(Context context, String name)
    {
        this(context, name, CACHE_SIZE);
    }

    public YKTiralStore(Context context, String name, int size)
    {
        super();
        this.mName = name;
        mContent = new LruCache<String, String>(size);
        JSONArray arry = AppUtil.getTopicReadData(context, name);
        if (arry == null) {
            return;
        }
        for (int i = 0; i < arry.length(); i++) {
            String content = null;
            try {
                content = arry.getString(i);
            } catch (JSONException e) {
            }
            if (TextUtils.isEmpty(content)) {
                continue;
            }
            putReadData(content);
        }
    }

    public boolean isExsit(String key) {
        String value = mContent.get(key);
        return !TextUtils.isEmpty(value);
    }

    public void putReadData(String key) {
        if (TextUtils.isEmpty(key)) return;
        mContent.put(key, key);
    }

    public void saveDataToFile(Context context) {
        JSONArray json = new JSONArray();
        Map<String, String> map = mContent.snapshot();
        for (String id : map.values()) {
            json.put(id);
        }
        AppUtil.saveTopicReadData(context, mName, json);
    }

    public void releaseData() {
        if (mContent == null) {
            mContent.evictAll();
        }
    }
}
