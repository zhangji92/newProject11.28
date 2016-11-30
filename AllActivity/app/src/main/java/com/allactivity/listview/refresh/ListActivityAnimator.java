package com.allactivity.listview.refresh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.allactivity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张继 on 2016/11/17.
 * 下拉刷新之顶部刷新动画
 */

public class ListActivityAnimator extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener{
    //
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView mListView;
    private static final int FLAG = 0x01;
    private List<String> mDate=new ArrayList<>();
    private BaseAdapter mAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            int what = msg.what;
            if (what == FLAG) {
                mDate.add("fruit");
                //适配器刷新
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);//处理完事物之后必须调用该函数，取消加载动画
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_list_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        //设置监听时间
        swipeRefreshLayout.setOnRefreshListener(this);
        mListView = (ListView) findViewById(R.id.listView);
//        ButterKnife.bind(this);
        initData();
        bindData();
    }

    private void initData() {
        for (int i=0;i<20;i++){
            mDate.add("apple"+i);
        }
    }
    private void bindData() {
        mAdapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return mDate.size()==0?0:mDate.size();
            }

            @Override
            public Object getItem(int position) {
                return mDate.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView=new TextView(ListActivityAnimator.this);
                textView.setText(mDate.get(position));
                return textView;
            }
        };

        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        Message msg=Message.obtain();
        msg.what=FLAG;
        //延长动画效果
        mHandler.sendMessageDelayed(msg,2000);
    }
}
