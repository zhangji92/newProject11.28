package com.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.Scroller;

/**
 * Created by lenovo on 2016/10/13.
 *
 */

public class MyImageView extends ImageView {

    private Context context;
    private Scroller mScroller;


    public MyImageView(Context context) {
        super(context);
        this.context=context;
        mScroller = new Scroller(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        mScroller = new Scroller(context);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        mScroller = new Scroller(context);
    }

    public void smoothScrollTo(int destX,int destY) {
        int startX = getScrollX();
        int startY = getScrollY();

        int dx=destX-mScroller.getFinalX();
        int dy=destY-mScroller.getFinalY();

        //1000ms内滑向destX,destY效果是慢慢滑动
        mScroller.startScroll(startX,startY,dx,dy,2000);
        //重绘
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }
}
