package com.yoka.mrskin.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class InfiniteViewPager extends ViewPager
{

    private static final long AUTO_PAGE_DELAY = 3000L;
    private boolean mAutoPage;
    private boolean mPostAutoPage;
    private ViewGroup mParent;

    public InfiniteViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public InfiniteViewPager(Context context)
    {
        super(context);
    }

    public void setNestedpParent(ViewGroup parent) {
        this.mParent = parent;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        startAutoPage();

        if (!mPostAutoPage) {
            mPostAutoPage = true;
            postDelayed(mAutoPageRunnable, AUTO_PAGE_DELAY);
        }
    }

    /**
     * 开启自动翻页
     */
    public void startAutoPage() {
        mAutoPage = true;
    }

    /**
     * 关闭自动翻页
     */
    public void stopAutoPage() {
        mAutoPage = false;
    }

    private Runnable mAutoPageRunnable = new Runnable() {
        @Override
        public void run() {
            PagerAdapter adapter = getAdapter();
            if (adapter != null && mAutoPage) {
                int currentItem = getCurrentItem();
                int pageCount = adapter.getCount();

                if (pageCount > 1) {
                    int nextPage;
                    if (currentItem < pageCount - 1) {
                        nextPage = currentItem + 1;
                    } else {
                        nextPage = 0;
                    }
                    setCurrentItem(nextPage, true);
                }
            }
            postDelayed(this, AUTO_PAGE_DELAY);
        }
    };

    /**
     * 解决滑动事件的冲突
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float downX = 0, downY = 0;
        switch (ev.getActionMasked()) {
        case MotionEvent.ACTION_DOWN:
            downX = ev.getX();
            downY = ev.getY();
            getParent().requestDisallowInterceptTouchEvent(false);
            // 当用户在操作ViewPager时，关闭自动翻页
            stopAutoPage();
            break;
        case MotionEvent.ACTION_MOVE:
            if (Math.abs(ev.getX() - downX) > Math.abs(ev.getY() - downY)) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            getParent().requestDisallowInterceptTouchEvent(false);
            startAutoPage();
            break;
        }
        // if (mParent != null) {
        // mParent.requestDisallowInterceptTouchEvent(false);
        // }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (mParent != null) {
            mParent.requestDisallowInterceptTouchEvent(false);
        }
        return super.onInterceptTouchEvent(arg0);
    }

    /**
     * 点击按钮左滑和右滑 type -1 左 1 右
     */
    public void moveLeftOrRight(int type) {
        int currentItem = getCurrentItem();
        PagerAdapter adapter = getAdapter();
        int pageCount = adapter.getCount();
        if (pageCount > 1) {
            int nextPage = 0;
            if (type == 1) {
                if (currentItem < pageCount - 1)
                    nextPage = currentItem + 1;
                else
                    nextPage = 0;
            } else {
                if (currentItem > 0) {
                    nextPage = currentItem - 1;
                } else if (currentItem == 0) {
                    nextPage = pageSize;
                }
            }
            setCurrentItem(nextPage, true);
        }
    }

    public int pageSize = 4;
}
