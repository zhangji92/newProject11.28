package com.measureactivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/21.
 *
 */

public class ListFragmentAdapter extends FragmentPagerAdapter {

    private List<ListFragment> fragments=new ArrayList<>();

    public ListFragmentAdapter(CustomViewPager customViewPager,FragmentManager fm) {
        super(fm);
        fragments.add(new ListFragment(customViewPager,1));
        fragments.add(new ListFragment(customViewPager,2));
        fragments.add(new ListFragment(customViewPager,3));
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
    @Override
    public int getCount() {
        return fragments.size();
    }
}
