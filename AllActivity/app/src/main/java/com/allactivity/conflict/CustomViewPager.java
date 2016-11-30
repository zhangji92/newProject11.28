package com.allactivity.conflict;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by 张继 on 2016/11/23.
 * 自定义的viewPager
 */

public class CustomViewPager extends ViewPager {
    public CustomViewPager(Context context) {
        this(context,null);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
