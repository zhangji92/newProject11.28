package com.allactivity.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.allactivity.R;

/**
 * Created by 张继 on 2016/11/23.
 * 图片路径
 */

public class AsyncTaskActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.async_task_activity);
        MyAsyncTask task=new MyAsyncTask();
        task.execute();//启动AsyncTask
        Button async_bnt= (Button) findViewById(R.id.async_bnt);
        Button progressBar_activity_bnt= (Button) findViewById(R.id.progressBar_activity);
        async_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AsyncTaskActivity.this,ImageTest.class);
                startActivity(intent);
            }
        });
        progressBar_activity_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AsyncTaskActivity.this,ProgressBarActivity.class);
                startActivity(intent);
            }
        });
    }
}
