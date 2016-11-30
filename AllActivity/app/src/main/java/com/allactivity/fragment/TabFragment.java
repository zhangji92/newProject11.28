package com.allactivity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 张继 on 2016/10/23.
 * 微信ViewPager
 */

public class TabFragment extends Fragment {
    private String mTitle = "Default";
    public static final String TITLE = "title";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //判断初始化时是否给赋值
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }
        TextView textView = new TextView(getActivity());
        textView.setTextSize(30);
        textView.setBackgroundColor(Color.WHITE);
        textView.setText(mTitle);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
