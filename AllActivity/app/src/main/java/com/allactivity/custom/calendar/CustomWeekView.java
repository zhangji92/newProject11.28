package com.allactivity.custom.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created by 张继 on 2016/11/1.
 * 自定义周
 */

public class CustomWeekView extends View {

    private int mTopLineColor = Color.parseColor("#CCE4F2");
    private int mBottomLineColor = Color.parseColor("#CCE4F2");
    //周一-->周五字体的颜色
    private int mWeekDayColor = Color.parseColor("#1FC2F3");
    //周六日的颜色
    private int mWeekendColor = Color.parseColor("#fa4451");
    //线的宽度
    private int mLineWidth = 4;
    //字体大小
    private int mWorldSize = 14;
    //画笔
    private Paint mPaint;
    private DisplayMetrics mDisplayMetrics;
    private String[] weekString = new String[]{"日", "一", "二", "三", "四", "五", "六"};

    public CustomWeekView(Context context) {
        this(context, null);
    }

    public CustomWeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomWeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化画笔
        mPaint = new Paint();
        //利用DisplayMetrics获取屏幕信息
        mDisplayMetrics = getResources().getDisplayMetrics();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDisplayMetrics.densityDpi * 30;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        //上横线
        mPaint.setColor(mTopLineColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mLineWidth * mDisplayMetrics.densityDpi);
        canvas.drawLine(0, 0, width, 0, mPaint);
        //下横线
        mPaint.setColor(mBottomLineColor);
        canvas.drawLine(0, height, width, height, mPaint);
        //画笔为实心
        mPaint.setStyle(Paint.Style.FILL);
        //字体大小以sp为单位
        mPaint.setTextSize(mWorldSize * mDisplayMetrics.scaledDensity);
        //列的宽度
        int columnWidth = width / 7;
        for (int i = 0; i < weekString.length; i++) {
            //一个字体
            String text = weekString[i];
            //一个字体的宽度
            int fontWidth = (int) mPaint.measureText(text);
            //字体起始位置且剧中
            int startX = columnWidth * i + (columnWidth - fontWidth) / 2;
            int startY = (int) (height / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
            Log.i("data_Week", "mPaint.ascent():" + mPaint.ascent());
            Log.i("data_Week", "mPaint.descent():" + mPaint.descent());
            //周六日的颜色
            if (text.indexOf("日") > -1 || text.indexOf("六") > -1) {
                mPaint.setColor(mWeekendColor);
            } else {
                mPaint.setColor(mWeekDayColor);
            }
            canvas.drawText(text, startX, startY, mPaint);
        }
    }

    public void setWeekString(String[] weekString) {
        this.weekString = weekString;
    }

    public void setmWorldSize(int mWorldSize) {
        this.mWorldSize = mWorldSize;
    }

    public void setmLineWidth(int mLineWidth) {
        this.mLineWidth = mLineWidth;
    }

    public void setmWeekendColor(int mWeekendColor) {
        this.mWeekendColor = mWeekendColor;
    }

    public void setmWeekDayColor(int mWeekDayColor) {
        this.mWeekDayColor = mWeekDayColor;
    }

    public void setmBottomLineColor(int mBottomLineColor) {
        this.mBottomLineColor = mBottomLineColor;
    }

    public void setmTopLineColor(int mTopLineColor) {
        this.mTopLineColor = mTopLineColor;
    }
}
