package com.allactivity.conflict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.allactivity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张继 on 2016/11/22.
 * 滑动冲突
 */

public class SlidingConflict extends FragmentActivity {
    private ViewPager mViewPager;
    private List<Fragment> mList = new ArrayList<>();
    SlidingCustomListView listView;
    SlidingCustomListView s;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_conflict);
        initViews();
        initData();
    }

    private void initData() {
        for (int i=0;i<5;i++){
            SlidingConflictFragment s=new SlidingConflictFragment();
            mList.add(s);
        }
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mList.get(position);
            }
            @Override
            public int getCount() {
                return mList.size();
            }
        });
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, getStringData());
//        listView.setAdapter(arrayAdapter);
//        setListViewHeight(listView);
//        arrayAdapter.notifyDataSetChanged();
    }


    private void initViews() {
        mViewPager= (ViewPager) findViewById(R.id.sliding_viewPager);
//        listView = (SlidingCustomListView) findViewById(R.id.listView);

    }

//    public List<String> getStringData() {
//        for (int i = 0; i < 15; i++) {
//            mList.add("listView" + i);
//        }
//        return mList;
//    }

    public void setListViewHeight(ListView listViewHeight) {
        ListAdapter listAdapter = listViewHeight.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        s.setmHeight(params.height);
        Log.e("abc", "height" + params.height);
        listView.setLayoutParams(params);
    }
}
