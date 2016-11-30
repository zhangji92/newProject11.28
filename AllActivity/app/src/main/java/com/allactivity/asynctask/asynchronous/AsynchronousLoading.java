package com.allactivity.asynctask.asynchronous;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.allactivity.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张继 on 2016/11/23.
 * 异步加载
 */

public class AsynchronousLoading extends Activity {
    private ListView mListView;
    private static String URL = "http://www.imooc.com/api/teacher?type=4&num=30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asynchronous_loading);
        initVIews();
        initData();

    }

    /**
     *  获取数据
     */
    private void initData() {
        new LoadingAsyncTask().execute(URL);
    }

    private void initVIews() {
        mListView = (ListView) findViewById(R.id.asynchronous_listView);
    }

    class LoadingAsyncTask extends AsyncTask<String, Void, List<NewBean>> {


        @Override
        protected List<NewBean> doInBackground(String... params) {
            return getJsonData(params[0]);
        }
    }

    private List<NewBean> getJsonData(String url) {
        List<NewBean> newsList=new ArrayList<>();
        try {
            //此句功能与url.openConnection().getInputStream()相同
            //可根据URL直接联网获取网络数据，简单粗暴！返回值类型为InputStream
            String jsonString=readStream(new URL(url).openStream());
            Log.e("json",jsonString);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsList;
    }

    /**
     * 数据的读取
     * @param is 字节流
     * @return 返回所需要的数据
     */
    private String readStream(InputStream is){
        InputStreamReader isr;
        String result="";
        try {
            String len="";
            isr=new InputStreamReader(is,"utf-8");//字节流转化成字符流
            BufferedReader br=new BufferedReader(isr);
            while ((len=br.readLine())!=null){
                result+=len;//将内容读取出来
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
