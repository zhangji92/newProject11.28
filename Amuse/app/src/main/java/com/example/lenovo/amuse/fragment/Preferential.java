package com.example.lenovo.amuse.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.adapter.PreferentialAdapter;
import com.example.lenovo.amuse.mode.LovePlayMode;
import com.example.lenovo.amuse.mode.PreferentialMode;
import com.example.lenovo.amuse.util.BaseUri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/9/22.
 * 优惠专区
 */

public class Preferential extends BaseFragment {
    private ListView listView;
    private PreferentialMode mPreferentialMode;
    List<PreferentialMode.ResultCodeBean> list = new ArrayList<>();
    private PreferentialAdapter preferentialAdapter;

    //精度
    int lat = 1;
    //维度
    int lng = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseUri.PRE:
                    mPreferentialMode = parseMode(msg.obj);
//                    if (mPreferentialMode!=null&&mPreferentialMode.getResultCode()!=null){
                    for (int i = 0; i < mPreferentialMode.getResultCode().size(); i++) {
                        list.addAll(mPreferentialMode.getResultCode());
//                        }
                    }
                    preferentialAdapter.notifyDataSetChanged();

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
    private PreferentialMode parseMode(Object obj) {
        PreferentialMode preferentialMode = null;
        if (obj != null && obj instanceof PreferentialMode) {
            preferentialMode = (PreferentialMode) obj;
        }
        return preferentialMode;
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
        View view = inflater.inflate(R.layout.preferential, container, false);
        httpTools.postData(mHandler, "1", "1", null, null);
        ListView listView = (ListView) view.findViewById(R.id.pre_list);
        preferentialAdapter = new PreferentialAdapter(list, getActivity());
        listView.setAdapter(preferentialAdapter);
        return view;
    }
}
