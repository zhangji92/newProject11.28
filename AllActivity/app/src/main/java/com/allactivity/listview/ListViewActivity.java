package com.allactivity.listview;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewGroup;
import android.widget.ListView;

import com.allactivity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张继 on 2016/11/10.
 * listView下拉刷新
 */

public class ListViewActivity extends Activity implements ReFlashListView.IReflashListener {
    private ReFlashListView mListView;
    private List<ImageMessage> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        //初始化控件
        initView();
        initData();
    }

    ListViewAdapter listViewAdapter;

    private void initData() {
        ImageMessage imageMessage = new ImageMessage("哈哈哈哈", "你好你好你好你好", R.drawable.www);
        for (int i = 0; i < 6; i++) {
            list.add(imageMessage);
        }
        if (listViewAdapter == null) {
            listViewAdapter = new ListViewAdapter(ListViewActivity.this, list);
            mListView.setAdapter(listViewAdapter);
        } else {
            listViewAdapter.onDateChange(list);
        }
    }

    /**
     * 刷新的数据
     */
    private void setReflashData() {
        for (int i = 0; i < 2; i++) {
            ImageMessage imageMessage = new ImageMessage();
            imageMessage.setTitle("刷新数据");
            imageMessage.setPic(R.drawable.www);
            imageMessage.setContent("50w用户");
            list.add(0, imageMessage);
        }
    }
    /**
     * 刷新的数据
     */
    private void getLoadData() {
        for (int i = 0; i < 2; i++) {
            ImageMessage imageMessage = new ImageMessage();
            imageMessage.setTitle("更多程序");
            imageMessage.setPic(R.drawable.chijing);
            imageMessage.setContent("50w用户");
            list.add(imageMessage);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mListView = (ReFlashListView) findViewById(R.id.activity_list_view);



        //设置监听
        mListView.setiReflashListener(this);

    }

    @Override
    public void onReflash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取最新数据
                setReflashData();
                //通知界面显示数据
                initData();
                //通知listView刷新数据完毕
                mListView.reFlashComplete();
            }
        }, 2000);

    }

    @Override
    public void onLoad() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //跟新界面显示
                getLoadData();
                //通知listView加载完毕
                mListView.loadComplete();
            }
        }, 2000);


    }
}
