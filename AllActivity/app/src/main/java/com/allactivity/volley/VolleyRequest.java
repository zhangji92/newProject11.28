package com.allactivity.volley;

import android.content.Context;

import com.allactivity.util.MyApplication;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 张继 on 2016/11/22.
 * volley管理
 */

public class VolleyRequest {
    public static StringRequest stringRequest;
    public static Context context;

    public static void RequestGet(Context mContent,String url,String tag,VolleyInterface vif){
        MyApplication.getQueue().cancelAll(tag);
        stringRequest=new StringRequest(Request.Method.GET,url,vif.loadingListener(),vif.errorListener());
        stringRequest.setTag(tag);
        MyApplication.getQueue().add(stringRequest);
        MyApplication.getQueue().start();
    }
    public static void RequestPost(Context mContent, String url, String tag, final HashMap<String,String> params, VolleyInterface vif){
        MyApplication.getQueue().cancelAll(tag);
        stringRequest=new StringRequest(Request.Method.POST,url,vif.loadingListener(),vif.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setTag(tag);
        MyApplication.getQueue().add(stringRequest);
        MyApplication.getQueue().start();
    }
}
