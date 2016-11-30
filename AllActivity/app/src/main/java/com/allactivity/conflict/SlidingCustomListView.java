package com.allactivity.conflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by 张继 on 2016/11/22.
 * 重写listView
 */

public class SlidingCustomListView extends ListView {
    private int mHeight;

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public SlidingCustomListView(Context context) {
        this(context, null);
    }

    public SlidingCustomListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingCustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        getParent().requestDisallowInterceptTouchEvent(true);
//        return true;
//    }
}
