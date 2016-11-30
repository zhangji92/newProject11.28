package com.measureactivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/21.
 *
 */

public class ListFragment extends Fragment {

    private CustomViewPager customViewPager;
    private int type;

    public ListFragment() {
    }

    @SuppressLint("ValidFragment")
    public ListFragment(CustomViewPager customViewPager, int type) {
        this.customViewPager = customViewPager;
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, null);
        CustomListView customListView = (CustomListView) view.findViewById(R.id.listView);
        final List<String> data = new ArrayList<>();
        for (int i = 0; i < type * 5; i++) {
            data.add("aaa");
        }

        customListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data));
        Log.e("aaaaaaaaa","aaaaaaaaaaaa"+customListView.getRealHeight());
        customViewPager.calculate(type, customListView.getRealHeight());
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
