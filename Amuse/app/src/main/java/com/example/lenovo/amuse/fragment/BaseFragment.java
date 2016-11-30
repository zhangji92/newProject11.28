package com.example.lenovo.amuse.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.lenovo.amuse.util.HttpTools;

/**
 * Created by lenovo on 2016/9/22.
 *
 */

public class BaseFragment extends Fragment {
    public HttpTools httpTools;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取网络连接的实例
        httpTools=HttpTools.getInstance();
    }
}
