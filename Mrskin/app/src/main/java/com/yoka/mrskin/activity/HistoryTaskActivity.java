package com.yoka.mrskin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.managers.task.TimeUtil;

public class HistoryTaskActivity extends BaseActivity implements
        OnClickListener
{
    private LinearLayout mBack;
    private MyAdapter mAdapter;
    private ListView mListView;
    private YKTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_task);
        YKActivityManager.getInstance().addActivity(this);
        initData();
        init();
    }

    private void init() {
        mBack = (LinearLayout) findViewById(R.id.history_task_layout);
        mBack.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.history_task_listview);
        mListView.setDivider(null);
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
    }

    private class MyAdapter extends BaseAdapter
    {
        private ViewHolder viewHolder = null;

        @Override
        public int getCount() {
            if (mTask == null) {
                return 0;
            }
            if (mTask.getmSubtask() == null) {
                return 0;
            }
            return mTask.getmSubtask().size();
        }

        @Override
        public Object getItem(int position) {
            if (mTask == null) {
                return null;
            }
            if (mTask.getmSubtask() == null) {
                return null;
            }
            return mTask.getmSubtask().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (mTask == null) {
                return null;
            }

            if (convertView == null) {
                convertView = LayoutInflater.from(HistoryTaskActivity.this)
                        .inflate(R.layout.history_task_test, null);

                viewHolder = new ViewHolder();
                viewHolder.timeYear = (TextView) convertView
                        .findViewById(R.id.history_task_timeyear);
                viewHolder.time = (TextView) convertView
                        .findViewById(R.id.history_task_time);
                viewHolder.title = (TextView) convertView
                        .findViewById(R.id.history_task_title);
                viewHolder.image = (ImageView) convertView
                        .findViewById(R.id.history_task_image);
                viewHolder.fimage = (ImageView) convertView
                        .findViewById(R.id.history_task_back);
                viewHolder.staus = (TextView) convertView.
                        findViewById(R.id.history_task_imagetest);
                
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            YKTask subTask = mTask.getmSubtask().get(position);
            long time = 0;
            if (mTask.getmCycleTime() == -1) {
                time = TimeUtil.getTodayZero()
                        + subTask.getmRemindTime().getmMills();
            } else {
                time = subTask.getmRemindTime().getmMills();
            }
            if (subTask.ismTheDayFirst()) {
                viewHolder.timeYear.setVisibility(View.VISIBLE);
                viewHolder.fimage.setVisibility(View.VISIBLE);
                viewHolder.timeYear.setText(TimeUtil.forTimeForYearMonthDay(time));
            } else {
                viewHolder.timeYear.setVisibility(View.GONE);
                viewHolder.fimage.setVisibility(View.GONE);
            }
            viewHolder.time.setText(TimeUtil.forTimeForHourAndSeconed(time));
            viewHolder.title.setText(subTask.getmTitle());
              
            if (subTask.isFinished()) {
                viewHolder.image
                        .setBackgroundResource(R.drawable.list_underway_btn);
                viewHolder.staus.setTextColor(0xffFF5B90);
            } else {
                viewHolder.image
                        .setBackgroundResource(R.drawable.list_done_btn);
                viewHolder.staus.setTextColor(0xffD0D0D0);
            }
            return convertView;
        }
    }

    class ViewHolder
    {
        private TextView timeYear, time, title,staus;
        private ImageView image,fimage;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.history_task_layout:
            finish();
            break;
        }
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        YKTask task = (YKTask) intent.getSerializableExtra("task");
        if (task == null) {
            return;
        }
        mTask = task;
    }

    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("HistoryTaskActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
        JPushInterface.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("HistoryTaskActivity"); // 保证 onPageEnd 在onPause
        // 之前调用,因为 onPause
        // 中会保存信息
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        YKActivityManager.getInstance().removeActivity(this);
    }
}
