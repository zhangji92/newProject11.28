package com.example.administrator.beijingplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class WineAgentActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2{

    private PullToRefreshListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_agent);

        list = (PullToRefreshListView)findViewById(R.id.wine_listview);
        list.setMode(PullToRefreshBase.Mode.BOTH);
        list.setOnRefreshListener(this);

       // list.onRefreshComplete(); //停止刷新
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        Toast.makeText(this,"你好，下拉",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        Toast.makeText(this,"你好，上拉",Toast.LENGTH_SHORT).show();
    }
    Boolean isfristPull=true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){
            list.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if(isfristPull){
                        isfristPull =false;
                        list.setRefreshing();
                    }
                }
            });
        }
    }
}
