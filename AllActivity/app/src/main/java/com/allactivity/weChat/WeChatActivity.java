package com.allactivity.weChat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import com.allactivity.R;
import com.allactivity.fragment.TabFragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/22.
 * 微信
 */

public class WeChatActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private String[] mTitle = new String[]{"first Fragment", "second Fragment", "Third Fragment", "Fourth Fragment"};

    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //自定义样式
        this.setTheme(R.style.MyTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.we_chat_activity);
        //使用反射机制让系统显示菜单
        setOverflowButtonAlways();
        //把ActionBar图标隐藏
        getActionBar().setDisplayShowHomeEnabled(false);
        //初始化控件
        initView();
        //初始化数据
        initData();
        //添加数据
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
        initEvent();
    }

    private void initEvent() {
        mViewPager.addOnPageChangeListener(this);
    }

    private void initData() {
        for (String title : mTitle) {
            //获取实例对象
            TabFragment tabFragment = new TabFragment();
            Bundle bundle = new Bundle();
            //传递数据
            bundle.putString(TabFragment.TITLE, title);
            //添加数据
            tabFragment.setArguments(bundle);
            //添加到ViewPager
            mFragmentList.add(tabFragment);
        }
    }

    private void initView() {
        //初始化控件
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
        ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
        ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
        ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
        mTabIndicators.add(one);
        mTabIndicators.add(two);
        mTabIndicators.add(three);
        mTabIndicators.add(four);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        one.setIconAlpha(1.0f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //获取菜单
        getMenuInflater().inflate(R.menu.we_chat, menu);
        return true;
    }

    /**
     * 使用反射强制让系统把菜单图标显示-->一直显示
     * 且菜单显示的位置
     */
    private void setOverflowButtonAlways() {
        //
        ViewConfiguration configuration = ViewConfiguration.get(this);
        try {
            //显示OverflowButton的Field
            Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.set(configuration, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置显示子菜单中的图标
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onClick(View v) {
        /**
         * tab点击事件
         */
        clickTab(v);

    }

    /**
     * tab点击事件
     * @param v 点击控件的试图
     */
    private void clickTab(View v) {
        resetOtherTabs();
        switch (v.getId()) {
            case R.id.id_indicator_one:
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_three:
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.id_indicator_four:
                mTabIndicators.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.e("TAG", "position=" + position + ",positionOffset=" + positionOffset);
        if (positionOffset>0){
            ChangeColorIconWithText left=mTabIndicators.get(position);
            ChangeColorIconWithText right=mTabIndicators.get(position+1);
            left.setIconAlpha(1-positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
