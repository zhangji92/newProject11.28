package com.allactivity.asynctask;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by 张继 on 2016/11/23.
 * 自定义AsyncTask
 */

public class MyAsyncTask extends AsyncTask<Void,Void,Void> {

    //必须实现的
    @Override
    protected Void doInBackground(Void... params) {
        Log.e("MyAsyncTask","doInBackground");
        publishProgress();
        return null;
    }

    @Override
    protected void onPreExecute() {
        Log.e("MyAsyncTask","onPreExecute");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.e("MyAsyncTask","onPostExecute");
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        Log.e("MyAsyncTask","onProgressUpdate");
        super.onProgressUpdate(values);
    }
}
