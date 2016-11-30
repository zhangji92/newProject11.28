package com.yoka.mrskin.view.calendar;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yoka.mrskin.R;

public class HotwordsViewgroup extends LinearLayout implements
        View.OnClickListener
{
    private final static int VIEW_MARGIN_HORIZONTAL = 50;
    private final static int VIEW_MARGIN_VERTICAL = 20;

    private HotwordsOnclickListener mHotwordsOnclickListener;

    public static interface HotwordsOnclickListener
    {
        /**
         * 触发onclik回调，根据TextView.getText去做相关事情
         * 
         * @param view
         */
        public void hotwordOnclick(TextView view);
    }

    public HotwordsViewgroup(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public HotwordsViewgroup(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public HotwordsViewgroup(Context context)
    {
        super(context);
        init();
    }

    /**
     * 设置每个关键词点击事件监听
     * 
     * @param listener
     */
    public void setHotwordOnclickListener(HotwordsOnclickListener listener) {
        mHotwordsOnclickListener = listener;
    }

    private void init() {
        setOrientation(HORIZONTAL);
    }

    public void setData(final ArrayList<String> list) {
        removeAllViews();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String content = list.get(i);
                if (TextUtils.isEmpty(content)) {
                    continue;
                }
                TextView textview = new TextView(getContext());
                textview.setTextColor(Color.WHITE);
                textview.setBackgroundResource(R.drawable.ic_launcher);
                textview.setTextSize(18);
                textview.setOnClickListener(this);
                textview.setMinimumWidth(100);
                textview.setSingleLine();
                textview.setEllipsize(TextUtils.TruncateAt.END);
                textview.setText(content);
                LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                addView(textview, layoutparams);
            }
        }
    }

    /**
     * 重写onLayout,在layout每个元素之前，需要计算该元素是否需要折行到下一行显示
     */
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        if (count == 0) {
            super.onLayout(changed, l, count, r, b);
            return;
        }
        int lengthX = 0;
        int lengthY = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(lengthX, lengthY, lengthX + width, lengthY + height);
            int right = lengthX + width + VIEW_MARGIN_HORIZONTAL;
            int nextWidth = 0;
            if (i < count - 1) {
                nextWidth = getChildAt(i + 1).getMeasuredWidth();
            }
            if (right + nextWidth > (r - l)) {
                lengthX = 0;
                lengthY += height + VIEW_MARGIN_VERTICAL;
            } else {
                lengthX = right;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        if (count == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int lengthX = 0;
        int lengthY = 0;
        int r = MeasureSpec.getSize(widthMeasureSpec);
        // 定义子View的宽度最大不能超过r宽度
        int childWidth = MeasureSpec.makeMeasureSpec(r, MeasureSpec.AT_MOST);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(childWidth, MeasureSpec.UNSPECIFIED);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            int right = lengthX + width + VIEW_MARGIN_HORIZONTAL;
            int nextWidth = 0;
            // 非最后一个元素需要尝试计算一下能否放的下
            if (i < count - 1) {
                nextWidth = getChildAt(i + 1).getMeasuredWidth();
            } else {
                // 最后一个计算高度的时候要加最后一排的高度
                lengthY += height;
            }
            if (right + nextWidth > r) {
                // 如果放不下，换行
                lengthX = 0;
                lengthY += height + VIEW_MARGIN_VERTICAL;
            } else {
                // 如果能放下，往后加
                lengthX = right;
            }
        }
        setMeasuredDimension(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(lengthY,
                        MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override
    public void onClick(View view) {
        if (mHotwordsOnclickListener != null) {
            mHotwordsOnclickListener.hotwordOnclick((TextView) view);
        }
    }
}
