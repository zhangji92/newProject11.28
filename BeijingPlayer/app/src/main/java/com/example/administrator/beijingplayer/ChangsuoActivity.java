package com.example.administrator.beijingplayer;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.beijingplayer.adpter.ChangsuoSpinnerAdapter;
import com.example.administrator.beijingplayer.adpter.PlayerAdapter;
import com.example.administrator.beijingplayer.mode.Player;
import com.example.administrator.beijingplayer.util.HttpTools;
import com.example.administrator.beijingplayer.util.ModeCode;
import com.example.administrator.beijingplayer.util.ServiceMessage;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class ChangsuoActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView changsuo_fanhui;

    private LinearLayout changsuo_shaixuan,changsuo_juli,changsuo_pinglun,changsuo_xiaoliang;

    private TextView changsuo_shaixuan_text,changsuo_juli_text,changsuo_pinglun_text,changsuo_xiaoliang_text;



    private PopupWindow popupwindow;//下拉菜单

    private View customView;//自定义布局下拉菜单的视图
    private ListView customView_list;

    private List<String> mSuaixuan;
    private List<String> mJuli;
    private List<String> mPingjia;
    private List<String> mXiaoliang;

    private HttpTools httpTools = HttpTools.getHttpTools();

    //场所页的数据相关
    private PullToRefreshListView pullToRefreshListView;
    private List<Player.ResultCodeBean> messagelist = new ArrayList<>();
    private  PlayerAdapter playerAdapter;
    //上拉刷新下拉加载的listView
    private PullToRefreshListView lv;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ModeCode.CHANGSUO_WHAT) {
                  getServiceMessage(msg.obj);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changsuo);

        initView();
        initEvent();
        getPullData();
    }

    private void initView() {
        changsuo_fanhui = (ImageView) findViewById(R.id.changsuo_bank);
        changsuo_shaixuan = (LinearLayout) findViewById(R.id.changsuo_shaixuan);
        changsuo_juli = (LinearLayout) findViewById(R.id.changsuo_juli);
        changsuo_pinglun = (LinearLayout) findViewById(R.id.changsuo_pingjia);
        changsuo_xiaoliang = (LinearLayout) findViewById(R.id.changsuo_xiaoliang);

        changsuo_shaixuan_text = (TextView) findViewById(R.id.changsuo_shaixuan_text);
        changsuo_juli_text = (TextView) findViewById(R.id.changsuo_juli_text);
        changsuo_pinglun_text = (TextView) findViewById(R.id.changsuo_pingjia_text);
        changsuo_xiaoliang_text = (TextView) findViewById(R.id.changsuo_xiaoliang_text);

        //获取自定义布局下拉菜单的视图
        customView = LayoutInflater.from(this).inflate(R.layout.chuangsuo_pull, null);
        customView_list = (ListView) customView.findViewById(R.id.changsuo_pull_list);

        popupwindow = new PopupWindow(customView, getWidth() / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
        //点击PopupWindow以外的区域，PopupWindow是否会消失
        popupwindow.setBackgroundDrawable(new BitmapDrawable());
        popupwindow.setOutsideTouchable(true);
         //设置listview的数据源
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.changsuo_list);
        playerAdapter = new PlayerAdapter(this,messagelist);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setAdapter(playerAdapter);

        //请求服务器数据
        String url = ModeCode.CHANGSUO;
        ServiceMessage<Player> serviceMessage = new ServiceMessage<Player>(url,ModeCode.CHANGSUO_WHAT,new Player());
        httpTools.getServiceMessage(handler,serviceMessage);

    }

    private void initEvent() {
        changsuo_fanhui.setOnClickListener(this);
        changsuo_shaixuan.setOnClickListener(this);
        changsuo_juli.setOnClickListener(this);
        changsuo_pinglun.setOnClickListener(this);
        changsuo_xiaoliang.setOnClickListener(this);
    }

    /***
     * 获取服务器数据
     * @param obj
     */
    private void getServiceMessage(Object obj) {
        if(obj !=null && obj instanceof Player){
            Player player = (Player)obj;
            List<Player.ResultCodeBean> aa = player.getResultCode();
            Log.e("getServiceMessage: ","----"+aa.get(0).getContent() );
            messagelist.addAll(aa);
            playerAdapter.setList(aa);
            playerAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 获取屏幕宽高
     */
    public int getWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeigh = dm.heightPixels;
        return screenWidth;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.changsuo_bank://返回
                finish();
                break;
            case R.id.changsuo_shaixuan:
                //添加数据到下拉列表
                customView_list.setAdapter(new ChangsuoSpinnerAdapter(this, mSuaixuan));
                //显示下拉菜单
                popupwindow.showAsDropDown(changsuo_shaixuan);

                customView_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        changsuo_shaixuan_text.setText(mSuaixuan.get(position));
                        popupwindow.dismiss();
                    }
                });
                break;
            case R.id.changsuo_juli:
                //添加数据到下拉列表
                customView_list.setAdapter(new ChangsuoSpinnerAdapter(this, mJuli));
                //显示下拉菜单
                popupwindow.showAsDropDown(changsuo_juli);

                customView_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        changsuo_juli_text.setText(mJuli.get(position));
                        popupwindow.dismiss();
                    }
                });
                break;
            case R.id.changsuo_pingjia:
                //添加数据到下拉列表
                customView_list.setAdapter(new ChangsuoSpinnerAdapter(this, mPingjia));
                //显示下拉菜单
                popupwindow.showAsDropDown(changsuo_pinglun);

                customView_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        changsuo_pinglun_text.setText(mPingjia.get(position));
                        popupwindow.dismiss();
                    }
                });
                break;
            case R.id.changsuo_xiaoliang:
                //添加数据到下拉列表
                customView_list.setAdapter(new ChangsuoSpinnerAdapter(this, mXiaoliang));
                //显示下拉菜单
                popupwindow.showAsDropDown(changsuo_xiaoliang);

                customView_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        changsuo_xiaoliang_text.setText(mXiaoliang.get(position));
                        popupwindow.dismiss();
                    }
                });
                break;
        }
    }

    /**
     * 下拉列表数据源
     */
    private void getPullData() {
        mSuaixuan = new ArrayList<>();
        mJuli = new ArrayList<>();
        mPingjia = new ArrayList<>();
        mXiaoliang = new ArrayList<>();

        mSuaixuan.add("全部");
        mSuaixuan.add("KTV");
        mSuaixuan.add("酒吧");
        mSuaixuan.add("足疗");
        mSuaixuan.add("演艺吧");
        mSuaixuan.add("夜总会");
        mSuaixuan.add("SPA养生馆");
        mSuaixuan.add("商务会所");

        mJuli.add("由近及远");
        mJuli.add("由远及近");

        mPingjia.add("好评");
        mPingjia.add("中评");
        mPingjia.add("差评");

        mXiaoliang.add("销量高到底");
        mXiaoliang.add("销量低到高");
    }


}
