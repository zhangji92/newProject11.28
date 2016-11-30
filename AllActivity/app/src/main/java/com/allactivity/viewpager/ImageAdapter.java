package com.allactivity.viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * Created by 张继 on 2016/11/22.
 * viewPager适配器
 */

class ImageAdapter extends PagerAdapter {
    private List<View> mList;

    public ImageAdapter(List<View> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position %= mList.size();
        if (position < 0) {
            position = mList.size() + position;
        }
        View view = mList.get(position);
        ViewParent vt = view.getParent();
        if (vt != null) {
            ViewGroup parent = (ViewGroup) vt;
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }
}
