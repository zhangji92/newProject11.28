package com.jrd.loan.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CircleBar extends View {

    private final RectF mColorWheelRectangle = new RectF();
    private Paint mDefaultWheelPaint;
    private Paint mColorWheelPaint;
    private Paint mColorWheelPaintCentre;
    private Paint mTextnum;
    private float circleStrokeWidth;
    private float mSweepAnglePer;
    private float currentAngleper;
    private final float textSize = -127;
    private int stepnumber, stepnumbernow;
    private float pressExtraStrokeWidth;
    private BarAnimation anim;
    private final int stepnumbermax = 100;// 默认最大步数

    public CircleBar(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        mColorWheelPaint = new Paint();
        mColorWheelPaint.setColor(Color.rgb(229, 228, 226));
        mColorWheelPaint.setStyle(Paint.Style.STROKE);// 空心
        mColorWheelPaint.setStrokeCap(Paint.Cap.ROUND);// 圆角画笔
        mColorWheelPaint.setAntiAlias(true);// 去锯齿

        mColorWheelPaintCentre = new Paint();
        mColorWheelPaintCentre.setColor(Color.rgb(229, 228, 226));
        mColorWheelPaintCentre.setStyle(Paint.Style.STROKE);
        mColorWheelPaintCentre.setStrokeCap(Paint.Cap.ROUND);
        mColorWheelPaintCentre.setAntiAlias(true);

        mDefaultWheelPaint = new Paint();
        mDefaultWheelPaint.setColor(Color.rgb(229, 228, 226));
        mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
        mDefaultWheelPaint.setStrokeCap(Paint.Cap.ROUND);
        mDefaultWheelPaint.setAntiAlias(true);

        mTextnum = new Paint();
        mTextnum.setAntiAlias(true);
        mTextnum.setColor(Color.BLACK);
        mTextnum.setTextSize(textSize);
        mTextnum.setTextAlign(Paint.Align.CENTER);

        anim = new BarAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(mColorWheelRectangle, 270, 360, false, mDefaultWheelPaint);
        canvas.drawArc(mColorWheelRectangle, 270, 360, false, mColorWheelPaintCentre);
        canvas.drawArc(mColorWheelRectangle, 270, mSweepAnglePer, false, mColorWheelPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);// 获取View最短边的长度
        setMeasuredDimension(min, min);// 强制改View为以最短边为长度的正方形
        circleStrokeWidth = Textscale(10, min);// 圆弧的宽度
        pressExtraStrokeWidth = Textscale(20, min);// 圆弧离矩形的距离
        mColorWheelRectangle.set(circleStrokeWidth + pressExtraStrokeWidth, circleStrokeWidth + pressExtraStrokeWidth, min - circleStrokeWidth - pressExtraStrokeWidth, min - circleStrokeWidth
                - pressExtraStrokeWidth);// 设置矩形
        mColorWheelPaint.setStrokeWidth(circleStrokeWidth);
        mColorWheelPaintCentre.setStrokeWidth(circleStrokeWidth);
    }

    /**
     * 进度条动画
     *
     * @author Administrator
     */
    public class BarAnimation extends Animation {
        /**
         * 每次系统调用这个方法时， 改变mSweepAnglePer，mPercent，stepnumbernow的值，
         * 然后调用postInvalidate()不停的绘制view。
         */
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation transformation) {
            if (interpolatedTime < 1.0f) {
                mSweepAnglePer = interpolatedTime * stepnumber * 360 / stepnumbermax;
                stepnumbernow = (int) (interpolatedTime * stepnumber);
                currentAngleper = mSweepAnglePer;
            } else {
                mSweepAnglePer = stepnumber * 360 / stepnumbermax;
                currentAngleper = mSweepAnglePer;
                stepnumbernow = stepnumber;
            }
            postInvalidate();
        }
    }

    /**
     * 根据控件的大小改变绝对位置的比例
     *
     * @param n
     * @param m
     * @return
     */
    public float Textscale(float n, float m) {
        return n / 500 * m;
    }

    /**
     * 更新步数和设置一圈动画时间
     *
     * @param stepnumber
     * @param time
     */
    public void update(int stepnumber) {
        this.stepnumber = stepnumber;
        this.startAnimation(anim);
    }

    /**
     * 设置进度条颜色
     *
     * @param red
     * @param green
     * @param blue
     */
    public void setColor(int red, int green, int blue) {
        mColorWheelPaint.setColor(Color.rgb(red, green, blue));
    }

}
