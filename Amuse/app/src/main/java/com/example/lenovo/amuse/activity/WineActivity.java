package com.example.lenovo.amuse.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.ListViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.amuse.MyApplication;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.adapter.WineAdapter;
import com.example.lenovo.amuse.mode.WineMode;
import com.example.lenovo.amuse.util.BaseUri;
import com.example.lenovo.amuse.util.HttpTools;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.SectionHeaderListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/14.
 * 酒水
 */

public class WineActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2 {
    private PullToRefreshListView refreshListView;
    private List<WineMode.ResultCodeBean> list = new ArrayList<>();
    private WineAdapter listAdapter;
    private int index;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //根据code获取数据
                case BaseUri.WINE_CODE:
                    WineMode successMode = parseMode(msg.obj);
                    if (successMode.getResultCode() != null) {
                     ((MyApplication) getApplication()).setAgentId(successMode.getResultCode().get(0).getId());
                        for (int i = 0; i < successMode.getResultCode().size(); i++) {
                            list.add(successMode.getResultCode().get(i));
                        }
                        refreshListView.setAdapter(listAdapter);
                        refreshListView.onRefreshComplete();//停止刷新
                    }
                    break;
            }
        }
    };

    //解析数据
    private WineMode parseMode(Object obj) {
        WineMode successMode = null;
        if (obj != null && obj instanceof WineMode) {
            successMode = (WineMode) obj;
        }
        return successMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wine_activity);
        //网络获取数据
        HttpTools.getInstance().postWine(mHandler, "0", "4", null, 1);
        //listView列表
        refreshListView = (PullToRefreshListView) findViewById(R.id.wine_list);
        refreshListView.setOnRefreshListener(this);
//        ListView  listView= (ListView) findViewById(R.id.list_item);
        //适配器
        listAdapter = new WineAdapter(WineActivity.this, list);
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        index = 0;
        HttpTools.getInstance().postWine(mHandler, String.valueOf(index), "4", null, 1);
        //清空数据
        list.clear();
//        refreshListView.onRefreshComplete(); //停止刷新
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        ++index;
        HttpTools.getInstance().postWine(mHandler, String.valueOf(index), "4", null, 1);

//        refreshListView.onRefreshComplete(); //停止刷新

    }

    Boolean isFirstPull = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            refreshListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (isFirstPull) {
                        isFirstPull = false;
                        refreshListView.setRefreshing();
                        //添加适配器
                        refreshListView.setAdapter(listAdapter);
                        refreshListView.onRefreshComplete(); //停止刷新
                    }
                }
            });
        }
    }
}
