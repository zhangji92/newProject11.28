package com.allactivity.viewpager.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allactivity.R;

import java.util.List;

/**
 * Created by 张继 on 2016/11/23.
 * 自定义导航区域
 */

public class ViewPagerIndicator extends LinearLayout {
    private static final int COLOR_TEXT_NORMAL = 0x77ffffff;
    private static final int HIGHLIGHT = 0xffffffff;//高亮的颜色
    //画笔
    private Paint mPaint;
    //
    private Path mPath;
    //三角形宽高
    private int mTriangleWidth;
    private int mTriangleHeight;
    //三角形底边的宽度和tag的一个比例-->用户可以自己设置（适配）
    private static final float RADIO_TRIANGLE_WIDTH = 1 / 6f;
    /**
     * 三角形底边的最大宽度
     */
    private final float DIMENSION_TRIANGLE_WIDTH_MAX = getScreeWidth()/3*RADIO_TRIANGLE_WIDTH;


    //初始化的偏移位置
    private int mInitTranslationX;
    //移动时的位置
    private int mTranslation;
    //tab数量
    private int mTabVisibleCont;
    //可显示的tab
    private static final int COUNT_DEFAULT_TAB = 4;

    private List<String> mTitle;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取可见tab的数量
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mTabVisibleCont = a.getInt(R.styleable.ViewPagerIndicator_visible_tab_count, COUNT_DEFAULT_TAB);
        if (mTabVisibleCont < 0) {
            mTabVisibleCont = COUNT_DEFAULT_TAB;
        }
        //释放
        a.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffff"));
        //画笔属性
        mPaint.setStyle(Paint.Style.FILL);
        //圆滑的三角形
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //底边的宽度
        mTriangleWidth = (int) (w / mTabVisibleCont * RADIO_TRIANGLE_WIDTH);
        mTriangleWidth=Math.min(mTriangleWidth,mTriangleWidth);
        //三角形初始时的偏移量
        mInitTranslationX = w / mTabVisibleCont / 2 - mTriangleWidth / 2;
        initTriangle();
    }

    /**
     * 子View个数的确定
     */
    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        //获取子view位数
        int cCount = getChildCount();

        if (cCount == 0) {
            return;
        }
        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            //设置每个tab 宽度
            lp.width = getScreeWidth() / mTabVisibleCont;
            view.setLayoutParams(lp);
        }
        setItemClickEvent();//点击tab事件
    }

    /**
     * 初始化三角形
     */
    private void initTriangle() {
        mTriangleHeight = mTriangleWidth / 2;
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.close();

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        //getHeight()+2降低三角形
        canvas.translate(mInitTranslationX + mTranslation, getHeight() + 2);
        canvas.drawPath(mPath, mPaint);


        canvas.restore();
        super.dispatchDraw(canvas);

    }

    /**
     * 指示器跟随手指滚动
     *
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        //一个tab的宽度
        int tabWidth = getWidth() / mTabVisibleCont;
        mTranslation = (int) (tabWidth * (offset + position));
        //容器移动，在tab处于移动至最后一个时
        if (position >= (mTabVisibleCont - 2) && offset > 0 && getChildCount() > mTabVisibleCont) {
            if (mTabVisibleCont != 1) {
                this.scrollTo((int) ((position - (mTabVisibleCont - 2)) * tabWidth + tabWidth * offset), 0);
            } else {
                this.scrollTo((int) (position * tabWidth + tabWidth * offset), 0);
            }

        }

        invalidate();
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public int getScreeWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public void setTabItem(List<String> title) {
        if (title != null && title.size() > 0) {
            this.removeAllViews();
            mTitle = title;
            for (String titles : mTitle) {
                addView(generateTextView(titles));
            }
        }
        //设置高亮
        highlightTextView(0);
        setItemClickEvent();//点击事件
    }


    //在setTabItem之前调用
    public void setmTabVisibleCont(int mTabVisibleCont) {
        this.mTabVisibleCont = mTabVisibleCont;
    }

    /**
     * 根据指定title创建tab
     *
     * @param titles
     * @return
     */
    private View generateTextView(String titles) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.width = getScreeWidth() / mTabVisibleCont;
        tv.setText(titles);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setLayoutParams(lp);
        return tv;
    }

    private ViewPager mViewPager;
    private PageChangeListener mListener;

    public void setmListener(PageChangeListener mListener) {
        this.mListener = mListener;
    }

    public interface PageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    /**
     * 设置管理的ViewPager
     *
     * @param viewPager
     * @param pos
     */
    public void setViewPager(ViewPager viewPager, int pos) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //tabWidth*positionOffset + position*tabWidth三角形的偏移量
                scroll(position, positionOffset);
                if (mListener != null) {
                    mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mListener != null) {
                    mListener.onPageSelected(position);
                }
                highlightTextView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mListener != null) {
                    mListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(pos);
//        highlightTextView(pos);
    }

    /**
     * 重置tab文本颜色
     */
    private void resetTextView() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }


    /**
     * 高亮某个tab文本颜色
     *
     * @param pos
     */
    private void highlightTextView(int pos) {
        resetTextView();
        View view = getChildAt(pos);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(HIGHLIGHT);
        }
    }

    /**
     * 设置tab点击事件
     */
    private void setItemClickEvent() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
}
