package com.example.lenovo.amuse.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.lenovo.amuse.MyApplication;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.activity.LoginActivity;
import com.example.lenovo.amuse.activity.PlaceActivity;
import com.example.lenovo.amuse.activity.PlaceDetails;
import com.example.lenovo.amuse.activity.SnapShortActivity;
import com.example.lenovo.amuse.activity.TableListActivity;
import com.example.lenovo.amuse.activity.UpToHall;
import com.example.lenovo.amuse.activity.WineActivity;
import com.example.lenovo.amuse.adapter.LovePlayAdapter;
import com.example.lenovo.amuse.mode.LovePlayMode;
import com.example.lenovo.amuse.util.BaseUri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/9/22.
 * 同城爱玩
 */

public class LovePlay extends BaseFragment implements View.OnClickListener {
    //添加数据集合
    private List<LovePlayMode.ResultCodeBean> modeList = new ArrayList<>();
    //实体类
    private LovePlayMode mLovePlayMode;
    //list控件
    ListView listView;
    //适配器
    private LovePlayAdapter lovePlayAdapter;
    //快拍
    LinearLayout linearLayout_snapShort;
    LinearLayout linearLayout_place;
    //拼桌
    private LinearLayout linearLayout_share;
    //达人馆
    LinearLayout linearLayout_talent;
    //精度
    int lat = 1;
    //维度
    int lng = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseUri.LOVE_PLAY_CODE:
                    mLovePlayMode = parseMode(msg.obj);
                    //把当前的商家ID赋值给MyApplication
//                    ((MyApplication)getActivity().getApplication()).setShopId(mLovePlayMode.getResultCode().get(0).getId());
                    for (int i = 0; i < mLovePlayMode.getResultCode().size(); i++) {
                        modeList.addAll(mLovePlayMode.getResultCode());
                    }
                    //计算list高度
                    getViewHright(listView);
                    lovePlayAdapter.notifyDataSetChanged();
                    break;
                case BaseUri.LOCATION:
                    //定位数据获取地址
                    lat = msg.arg1;
                    lng = msg.arg2;
                    break;
            }
        }
    };

    //解析数据
    private LovePlayMode parseMode(Object obj) {
        LovePlayMode lovePlayMode = null;
        if (obj != null && obj instanceof LovePlayMode) {
            lovePlayMode = (LovePlayMode) obj;
        }
        return lovePlayMode;
    }

    /**
     * 每次创建（Fragment） 都会绘制Fragemnt 的View 组件时回调该方法
     *
     * @param inflater           加载布局文件
     * @param container          加载Layout 布局的 父（ViewGroup）
     * @param savedInstanceState 是否返回父 ViewGroup  false 为不返回
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.love_play, container, false);
        //网络传输数据
        httpTools.getDate(mHandler, String.valueOf(lat), String.valueOf(lng), null, null, null, null, null, 2);
        //适配器
        lovePlayAdapter = new LovePlayAdapter(getActivity(), modeList);
        //控件
        listView = (ListView) view.findViewById(R.id.play_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PlaceDetails.class);
                String shopId = mLovePlayMode.getResultCode().get(position).getId();
                //设置ID
                //((MyApplication)getActivity().getApplication()).setShopId(mLovePlayMode.getResultCode().get(position).getId());
                intent.putExtra("shopId", shopId);
                getActivity().startActivity(intent);
            }
        });
        listView.setAdapter(lovePlayAdapter);
        //酒水代理
        RelativeLayout r1 = (RelativeLayout) view.findViewById(R.id.r1);
        r1.setOnClickListener(this);

        //快拍
        linearLayout_snapShort = (LinearLayout) view.findViewById(R.id.liner_snapShort);
        linearLayout_snapShort.setOnClickListener(this);
        linearLayout_place = (LinearLayout) view.findViewById(R.id.liner_place);
        linearLayout_place.setOnClickListener(this);
        //拼桌
        linearLayout_share = (LinearLayout) view.findViewById(R.id.liner_share);
        linearLayout_share.setOnClickListener(this);
        //达人馆
        linearLayout_talent= (LinearLayout) view.findViewById(R.id.liner_talent);
        linearLayout_talent.setOnClickListener(this);
        //scroll+listView设置一起滚动
        RelativeLayout zhiding = (RelativeLayout) view.findViewById(R.id.scroll_relative);
        zhiding.setFocusable(true);
        zhiding.setFocusableInTouchMode(true);
        zhiding.requestFocus();
        return view;
    }


    /**
     * 测量ListView的高度
     *
     * @param listView
     */
    public void getViewHright(ListView listView) {

        Adapter adapter = listView.getAdapter();
        int gd = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i, null, listView);
            view.measure(0, 0);
            int item = view.getMeasuredHeight();
            gd += item;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = gd + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.liner_snapShort:
                //跳转到快拍页面
                startActivity(new Intent(getActivity(), SnapShortActivity.class));
                break;
            case R.id.liner_place:
                //跳转到场所页面
                startActivity(new Intent(getActivity(), PlaceActivity.class));
                break;
            case R.id.r1:
                //跳转到酒水代理页面
                startActivity(new Intent(getActivity(), WineActivity.class));
                break;
            case R.id.liner_share:
                boolean flag = ((MyApplication) getActivity().getApplication()).isFlag();
                if (!flag) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    //跳转到拼桌页面
                    startActivity(new Intent(getActivity(), TableListActivity.class));
                }
                break; case R.id.liner_talent:
                //跳转到拼桌页面
                startActivity(new Intent(getActivity(), UpToHall.class));
                break;

        }
    }
}
