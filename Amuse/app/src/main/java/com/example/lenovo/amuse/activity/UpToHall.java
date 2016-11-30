package com.example.lenovo.amuse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.amuse.MyApplication;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.adapter.SnapShortAdapter;
import com.example.lenovo.amuse.mode.SnapShortMode;
import com.example.lenovo.amuse.util.BaseUri;
import com.example.lenovo.amuse.util.ServiceMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张继 on 2016/10/28.
 * 达人馆
 */

public class UpToHall extends BaseActivity implements View.OnClickListener {

    private SnapShortMode snapShortMode;
    private SnapShortAdapter snapShortAdapter;
    private GridView gridView;
    private List<SnapShortMode.ResultCodeBean.QuickphotoBean> beanList = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseUri.UP_TO_HALL_CODE:
                    parserMode(msg.obj);
                    break;
            }
        }
    };

    private void parserMode(Object obj) {
        if (obj != null && obj instanceof SnapShortMode) {
            snapShortMode = (SnapShortMode) obj;
        }
        if (snapShortMode != null) {
            for (int i = 0; i < snapShortMode.getResultCode().getQuickphoto().size(); i++) {
                beanList.add(snapShortMode.getResultCode().getQuickphoto().get(i));
            }
            //添加数据
            gridView.setAdapter(snapShortAdapter);
            snapShortAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_short);
        initView();
        initData();

        //适配器
        snapShortAdapter = new SnapShortAdapter(this, beanList);
    }

    private void initData() {
        //pageNumber 是 string 页码
        //pageSize 是 string 条数
        String uri = BaseUri.UP_TO_HALL + "&pageNumber=" + "0" + "&pageSize=" + "10";
        ServiceMessage<SnapShortMode> serviceMessage = new ServiceMessage<SnapShortMode>(uri, BaseUri.UP_TO_HALL_CODE, new SnapShortMode());
        httpTools.getServiceMessage(mHandler, serviceMessage);
    }

    private void initView() {
        TextView text_toolBar= (TextView) findViewById(R.id.snap_toolBar);
        text_toolBar.setText("达人馆");
        gridView = (GridView) findViewById(R.id.snap_gridView);
        //点击时间
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                boolean flag = ((MyApplication) getApplication()).isFlag();
                if (!flag) {
                    Intent intent = new Intent(UpToHall.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(UpToHall.this, SnapShortDetailsActivity.class);
                    //当前的快拍ID
                    String snapId = snapShortMode.getResultCode().getQuickphoto().get(position).getId();
                    intent.putExtra("snapId", snapId);
                    startActivity(intent);
                    Log.i("getFirstDate", " SnapShortActivity snapId" + snapId);
                }
            }
        });
        //适配器
        snapShortAdapter = new SnapShortAdapter(this, beanList);
        ImageView imageView = (ImageView) findViewById(R.id.snap_return);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.snap_return:
                finish();
                break;
        }
    }
}
