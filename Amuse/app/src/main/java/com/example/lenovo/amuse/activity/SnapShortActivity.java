package com.example.lenovo.amuse.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.lenovo.amuse.MyApplication;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.adapter.SnapShortAdapter;
import com.example.lenovo.amuse.fragment.LovePlay;
import com.example.lenovo.amuse.mode.SnapShortMode;
import com.example.lenovo.amuse.util.BaseUri;

import java.util.ArrayList;
import java.util.List;

/**
 * 快拍
 */
public class SnapShortActivity extends BaseActivity implements View.OnClickListener{

    private SnapShortAdapter snapShortAdapter;
    private List<SnapShortMode.ResultCodeBean.QuickphotoBean> beanList = new ArrayList<>();
    //数据类
    private SnapShortMode snapShortMode;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseUri.SNAP:
                    snapShortMode = parseMode(msg.obj);
                    for (int i = 0; i < snapShortMode.getResultCode().getQuickphoto().size(); i++) {
                        beanList.add(snapShortMode.getResultCode().getQuickphoto().get(i));
                    }
                    snapShortAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private SnapShortMode parseMode(Object obj) {
        SnapShortMode snapShortMode = null;
        if (obj != null && obj instanceof SnapShortMode) {
            snapShortMode = (SnapShortMode) obj;
        }
        return snapShortMode;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_short);
        //请求数据
        httpTools.getDate(mHandler, null, null, null, null, "1", "10", null, 4);
        //
        GridView gridView = (GridView) findViewById(R.id.snap_gridView);
        //点击时间
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                boolean flag = ((MyApplication) getApplication()).isFlag();
                if (!flag) {
                    Intent intent = new Intent(SnapShortActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SnapShortActivity.this, SnapShortDetailsActivity.class);
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
        //添加数据
        gridView.setAdapter(snapShortAdapter);
        ImageView imageView= (ImageView) findViewById(R.id.snap_return);
        imageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.snap_return:
                finish();
                break;
        }
    }
}
