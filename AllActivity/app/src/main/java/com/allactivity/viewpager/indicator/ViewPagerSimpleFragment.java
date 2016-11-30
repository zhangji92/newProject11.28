package com.allactivity.viewpager.indicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 张继 on 2016/11/23.
 * 自定义的Fragment
 */

public class ViewPagerSimpleFragment extends Fragment {
    private String mTitle;
    public static final String BUNDLE_TITLE = "title";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取Bundle中的数据
        Bundle bundle=getArguments();
        if (bundle!=null){
            //赋值
            mTitle=bundle.getString(BUNDLE_TITLE);
        }
        TextView tv=new TextView(getActivity());
        tv.setText(mTitle);
        tv.setGravity(Gravity.CENTER);
        return tv;

    }

    public static ViewPagerSimpleFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        //发送数据
        bundle.putString(BUNDLE_TITLE, title);
        ViewPagerSimpleFragment fragment = new ViewPagerSimpleFragment();
        //赋值
        fragment.setArguments(bundle);
        return fragment;
    }
}
