package com.allactivity.volley;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.allactivity.R;
import com.allactivity.util.MyApplication;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 张继 on 2016/11/21.
 * volley框架
 * 1、Volley的get和post请求方式的使用
 * 2、Volley的网络请求队列建立和取消队列请求
 * 3、Volley与Activity生命周期的联动
 * 4、volley简单的二次回调封装
 */

public class VolleyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volley_activity);
        volley_Get();
//        volley_Post();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getQueue().cancelAll("abcPost");
    }

    private void volley_Post() {
        String url = "http://tc.ceol8.com//service/index.php?model=user&action=login";
        HashMap<String, String> hasMap = new HashMap<>();
        hasMap.put("username", "17073353257");
        hasMap.put("passwd", "123456");
        JSONObject jsonObject = new JSONObject(hasMap);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("JSONObject", "s:" + jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("JSONObject", "s:" + volleyError.toString());
            }
        });
        jsonObjectRequest.setTag("abcPost");
        MyApplication.getQueue().add(jsonObjectRequest);

//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Log.e("JSONObject", "s:" + s);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.e("JSONObject", "volleyError:" + volleyError.toString());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> hasMap = new HashMap<>();
//                hasMap.put("username", "17073353257");
//                hasMap.put("passwd", "123456");
//                return hasMap;
//            }
//        };
//        stringRequest.setTag("abcPost");
//        MyApplication.getQueue().add(stringRequest);
    }

    private void volley_Get() {
        String url="https://www.baidu.com/";
        VolleyRequest.RequestGet(this, url, "abcGet", new VolleyInterface(this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Log.e("JSONObject", "JSONObject:" + result);
            }

            @Override
            public void onMyError(VolleyError error) {
                Log.e("JSONObject", "volleyError:" + error.toString());
            }
        });

//        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                //数据请求成功
//                Log.e("abc","sss:"+s);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                //数据请求失败
//                Log.e("abc","sss:"+volleyError.toString());
//            }
//        });
//
//        request.setTag("abcGet");
//        MyApplication.getQueue().add(request);
//        String url = "http://tc.ceol8.com/service/index.php?model=home&action=home_new&lat=" + "1" + "&lng=" + "1";
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                Log.e("JSONObject", "JSONObject:" + jsonObject);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.e("JSONObject", "volleyError:" + volleyError.toString());
//            }
//        });
//        jsonObjectRequest.setTag("abcGet");
//        MyApplication.getQueue().add(jsonObjectRequest);

    }


}
