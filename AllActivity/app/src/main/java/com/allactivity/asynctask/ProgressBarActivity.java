package com.allactivity.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.allactivity.R;

/**
 * Created by 93836 on 2016/11/28.
 * 自定义进度条
 */

public class ProgressBarActivity extends Activity {
    private ProgressBar mProgressBar;
    private MyAsyncTask myAsyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_activity);
        initViews();
        initData();
    }

    private void initData() {
        myAsyncTask=new MyAsyncTask();
        myAsyncTask.execute();//启动
    }

    private void initViews() {
        mProgressBar= (ProgressBar) findViewById(R.id.progress_activity_progressBar);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myAsyncTask!=null && myAsyncTask.getStatus()==AsyncTask.Status.RUNNING){
            //cancel方法只是将对应的AsyncTask标记为cancel状态，
            // 并不是真正的取消线程的执行，而且在java中不能直接粗暴的直接停止线程，必须得等线程执行完成后才能停止
            myAsyncTask.cancel(true);
        }
    }

    class MyAsyncTask extends AsyncTask<Void,Integer,Void>{
        @Override
        protected Void doInBackground(Void... params) {

            //模拟进度更新
            for(int i=0;i<101;i++){
                if (isCancelled()){
                   break;
                }
                publishProgress(i);
                try {
                    //延缓时间进度
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (isCancelled()){
                return;
            }
            //获取进度的更新值
            mProgressBar.setProgress(values[0]);
        }
    }
}
