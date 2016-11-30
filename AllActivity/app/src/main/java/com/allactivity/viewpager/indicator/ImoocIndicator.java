package com.allactivity.viewpager.indicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.allactivity.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 张继 on 2016/11/23.
 *
 */

public class ImoocIndicator extends FragmentActivity {
    private ViewPager mViewPager;
    private ViewPagerIndicator mViewPagerIndicator;
    private List<String> mTitles = Arrays.asList("短信1", "收藏2", "推荐3","短信4", "收藏5", "推荐6","短信7", "收藏8", "推荐9");
    private List<ViewPagerSimpleFragment> mContents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imooc_indicator2);

        initViews();
        initData();

        mViewPagerIndicator.setmTabVisibleCont(4);
        mViewPagerIndicator.setTabItem(mTitles);
//        mViewPage

    }

    private void initData() {
        for (String title:mTitles){
            ViewPagerSimpleFragment fragment=ViewPagerSimpleFragment.newInstance(title);
            mContents.add(fragment);
        }
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mContents.get(position);
            }

            @Override
            public int getCount() {
                return mContents.size()==0?0:mContents.size();
            }
        });
        mViewPagerIndicator.setViewPager(mViewPager,0);
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.imooc_indicator_viewPager);
        mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.viewPagerIndicator);
    }
}
