package com.example.lenovo.amuse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.adapter.PlaceAdapter;
import com.example.lenovo.amuse.mode.LovePlayMode;
import com.example.lenovo.amuse.mode.PlaceMode;
import com.example.lenovo.amuse.util.BaseUri;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 场所
 */
public class PlaceActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    private List<PlaceMode.ResultCodeBean> list = new ArrayList<>();

    private PullToRefreshListView pullToRefreshListView;
    private PlaceAdapter placeAdapter;
    //    private LovePlayMode placeMode;
    private PlaceMode placeMode;
    String flag;
    //精度
    int lat = 1;
    //维度
    int lng = 1;
    int index;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseUri.PLACE_CODE:
                    placeMode = parseMode(msg.obj);

                    if (placeMode != null) {
                        if (index == 0) {
                            for (int i = 0; i < placeMode.getResultCode().size(); i++) {
                                list.add(placeMode.getResultCode().get(i));
                            }
                        } else {
                            if (placeMode.getResultCode().get(0).getShopname().equals(list.get(0).getShopname())) {
                                Toast.makeText(PlaceActivity.this, "已全部加载", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < placeMode.getResultCode().size(); i++) {
                                    list.add(placeMode.getResultCode().get(i));
                                }
                            }
                        }
                    }
                    pullToRefreshListView.setAdapter(placeAdapter);
                    pullToRefreshListView.onRefreshComplete();//停止刷新
                    break;
            }
        }
    };

    private PlaceMode parseMode(Object obj) {
        PlaceMode placeMode = null;
        if (obj != null && obj instanceof PlaceMode) {
            placeMode = (PlaceMode) obj;
        }
        return placeMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        flag = getIntent().getStringExtra("Table");

        index = 0;
        //网络传输数据
        httpTools.getPlace(mHandler, String.valueOf(lat), String.valueOf(lng), String.valueOf(index), "10", null, null, null, 1);
        //适配器
        placeAdapter = new PlaceAdapter(list, PlaceActivity.this, flag);
        //控件
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.place_list);
        //点击监听事件
        pullToRefreshListView.setOnItemClickListener(this);
        //刷新监听
        pullToRefreshListView.setOnRefreshListener(this);
        //适配器
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);


    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        index = 0;
        httpTools.getPlace(mHandler, String.valueOf(lat), String.valueOf(lng), String.valueOf(index), "10", null, null, null, 1);
        //清空数据
        list.clear();
//        refreshListView.onRefreshComplete(); //停止刷新
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        ++index;
        httpTools.getPlace(mHandler, String.valueOf(lat), String.valueOf(lng), String.valueOf(index), "10", null, null, null, 1);


//        refreshListView.onRefreshComplete(); //停止刷新

    }

    Boolean isFirstPull = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            pullToRefreshListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (isFirstPull) {
                        isFirstPull = false;
                        pullToRefreshListView.setRefreshing();
                        //添加适配器
                        pullToRefreshListView.setAdapter(placeAdapter);
                        pullToRefreshListView.onRefreshComplete(); //停止刷新
                    }
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (flag != null && flag.equals("flag")) {
            --position;
            Intent intent = new Intent();
            intent.putExtra("mode", placeMode.getResultCode().get(position));
            setResult(0, intent);
            finish();
        } else {
            Intent intent = new Intent(this, PlaceDetails.class);
            intent.putExtra("shopId", list.get(position).getId());
            this.startActivity(intent);
        }
    }
}
