package com.jrd.loan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jrd.loan.R;

public class RulerWheel extends View {
    // 默认刻度模式
    public static final int MOD_TYPE_SCALE = 5;

    // 1/2模式
    public static final int MOD_TYPE_HALF = 2;

    // 字体大小
    private int mTextSize = 36;

    // 分隔线(大号)
    private int mLineHeighMax;
    private int mLineColorMax;

    // 分隔线(中号)
    private int mLineHeighMid;
    private int mLineColorMid;

    // 分隔线(小号)
    private int mLineHeighMin;
    private int mLineColorMin;

    private int scaleWidth;

    private int txtPaddingBottom;

    private int lineMidLineHeight;

    // 每个小刻度代表的实际值
    private int scaleValue;

    // 当前值
    private int mCurrValue;

    // 显示最大值
    private int mMaxValue;

    // 分隔模式
    private int mModType = MOD_TYPE_SCALE;

    // 分隔线之间间隔
    private int mLineDivder;

    // 滚动器
    private WheelHorizontalScroller scroller;

    // 是否执行滚动
    private boolean isScrollingPerformed;

    // 滚动偏移量
    private int scrollingOffset;

    // 显示刻度值
    private boolean isScaleValue;

    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint markPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private float mDownFocusX;
    private float mDownFocusY;
    private boolean isDisallowIntercept;

    public RulerWheel(Context context) {
        this(context, null);
    }

    public RulerWheel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RulerWheel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.txtPaddingBottom = getResources().getDimensionPixelSize(R.dimen.loan_ruler_txt_paddint_bottom);

        this.scroller = new WheelHorizontalScroller(context, this.scrollingListener);

        // 获取自定义属性和默认值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RulerWheel);

        // 刻度宽度
        this.scaleWidth = mTypedArray.getDimensionPixelSize(R.styleable.RulerWheel_scaleWidth, 4);
        this.linePaint.setStrokeWidth(this.scaleWidth);

        int lineColorMidFlag = mTypedArray.getColor(R.styleable.RulerWheel_lineColorMidFlag, Color.RED);

        this.markPaint.setStrokeWidth(this.scaleWidth);
        this.markPaint.setColor(lineColorMidFlag);

        // 刻度颜色
        this.mLineColorMax = mTypedArray.getColor(R.styleable.RulerWheel_lineColorMax, Color.BLACK);
        this.mLineColorMid = mTypedArray.getColor(R.styleable.RulerWheel_lineColorMid, Color.BLACK);
        this.mLineColorMin = mTypedArray.getColor(R.styleable.RulerWheel_lineColorMin, Color.BLACK);

        this.mTextSize = mTypedArray.getInteger(R.styleable.RulerWheel_text_Size, 36);
        this.mCurrValue = mTypedArray.getInteger(R.styleable.RulerWheel_def_value, 0);
        this.mMaxValue = mTypedArray.getInteger(R.styleable.RulerWheel_max_value, 100);

        this.mLineHeighMax = mTypedArray.getDimensionPixelSize(R.styleable.RulerWheel_lineHeightMax, 0);
        this.mLineHeighMid = mTypedArray.getDimensionPixelSize(R.styleable.RulerWheel_lineHeightMid, 0);
        this.mLineHeighMin = mTypedArray.getDimensionPixelSize(R.styleable.RulerWheel_lineHeightMin, 0);

        this.lineMidLineHeight = mTypedArray.getDimensionPixelSize(R.styleable.RulerWheel_lineMidLineHeight, 0);

        this.scaleValue = mTypedArray.getInteger(R.styleable.RulerWheel_scaleValue, 100);

        // 刻度模式
        this.mModType = obtainMode(mTypedArray.getInteger(R.styleable.RulerWheel_mode, 0));

        // 线条间距
        this.mLineDivder = obtainLineDivder(mTypedArray.getDimensionPixelSize(R.styleable.RulerWheel_line_divider, 0));

        // 显示刻度值
        this.isScaleValue = mTypedArray.getBoolean(R.styleable.RulerWheel_showScaleValue, false);
        int txtColor = mTypedArray.getColor(R.styleable.RulerWheel_text_Color, Color.BLACK);

        this.textPaint.setTextSize(mTextSize);
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        this.textPaint.setColor(txtColor);

        mTypedArray.recycle();
    }

    private int obtainMode(int mode) {
        if (mode == 1) {
            return MOD_TYPE_HALF;
        }

        return MOD_TYPE_SCALE;
    }

    private int obtainLineDivder(int lineDivder) {
        if (0 == lineDivder) {
            if (this.mModType == MOD_TYPE_HALF) {
                this.mLineDivder = 80;
            } else {
                this.mLineDivder = 20;
            }

            return this.mLineDivder;
        }

        return lineDivder;
    }

    // Scrolling listener
    private WheelHorizontalScroller.ScrollingListener scrollingListener = new WheelHorizontalScroller.ScrollingListener() {
        @Override
        public void onStarted() {
            isScrollingPerformed = true;
            notifyScrollingListenersAboutStart();
        }

        @Override
        public void onScroll(int distance) {
            doScroll(distance);
        }

        @Override
        public void onFinished() {
            if (thatExceed()) {
                return;
            }

            if (isScrollingPerformed) {
                notifyScrollingListenersAboutEnd();
                isScrollingPerformed = false;
            }

            scrollingOffset = 0;

            invalidate();
        }

        @Override
        public void onJustify() {
            if (thatExceed()) {
                return;
            }

            if (Math.abs(scrollingOffset) > WheelHorizontalScroller.MIN_DELTA_FOR_SCROLLING) {
                if (scrollingOffset < -mLineDivder / 2) {
                    scroller.scroll(mLineDivder + scrollingOffset, 0);
                } else if (scrollingOffset > mLineDivder / 2) {
                    scroller.scroll(scrollingOffset - mLineDivder, 0);
                } else {
                    scroller.scroll(scrollingOffset, 0);
                }
            }
        }
    };

    /**
     * 越界回滚
     *
     * @return
     */
    private boolean thatExceed() {
        int outRange = 0;

        if (this.mCurrValue < 0) {
            outRange = this.mCurrValue * this.mLineDivder;
        } else if (this.mCurrValue > this.mMaxValue) {
            outRange = (this.mCurrValue - this.mMaxValue) * this.mLineDivder;
        }

        if (0 != outRange) {
            this.scrollingOffset = 0;
            this.scroller.scroll(-outRange, 100);

            return true;
        }

        return false;
    }

    public void setValue(int current, int maxValue) {
        current = current / this.scaleValue;
        maxValue = maxValue / this.scaleValue;

        if (current < 0) {
            current = 0;
        }

        if (maxValue < 0) {
            maxValue = 100;
        }

        this.mCurrValue = current;
        this.mMaxValue = maxValue;

        invalidate();
    }

    /**
     * 获取当前值
     *
     * @return
     */
    public int getValue() {
        int value = Math.min(Math.max(0, this.mCurrValue * this.scaleValue), this.mMaxValue * this.scaleValue);

        return value == 0 ? 1 : value;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int heightSize = this.lineMidLineHeight + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w == 0 || h == 0) {
            return;
        }

        int rHeight = h - getPaddingTop() - getPaddingBottom();

        if (this.mLineHeighMax == 0) {
            this.mLineHeighMax = rHeight / 2;
        }

        if (this.mLineHeighMid == 0) {
            this.mLineHeighMid = rHeight / 4;
        }

        if (this.mLineHeighMin == 0) {
            this.mLineHeighMin = rHeight / 8;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (this.mLineHeighMin == 0) {
            return;
        }

        int rWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int rHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int ry = getPaddingTop() + (rHeight - this.mLineHeighMax);

        // 绘制刻度线
        drawScaleLine(canvas, rWidth, rHeight, ry);

        // 绘制中间刻度线
        drawMiddleLine(canvas, rWidth, ry);
    }

    private void doScroll(int delta) {
        this.scrollingOffset += delta;
        int offsetCount = this.scrollingOffset / this.mLineDivder;

        if (0 != offsetCount) {
            // 显示在范围内
            int oldValue = Math.min(Math.max(0, this.mCurrValue), this.mMaxValue);
            this.mCurrValue -= offsetCount;
            this.scrollingOffset -= offsetCount * this.mLineDivder;

            if (null != this.onWheelListener) {
                this.onWheelListener.onChanged(this, oldValue * this.scaleValue, Math.min(Math.max(0, this.mCurrValue * this.scaleValue), this.mMaxValue * this.scaleValue));
            }
        }

        invalidate();
    }

    /**
     * @param canvas
     * @param rWidth  显示宽度
     * @param rHeight 显示高度
     * @param ry      线y坐标
     */
    private void drawScaleLine(Canvas canvas, int rWidth, int rHeight, int ry) {
        // 根据间隔计算当前一半宽度的个数+偏移2个
        final int halfCount = (int) Math.ceil(rWidth / 2f / this.mLineDivder) + 2;
        final int distanceX = this.scrollingOffset;
        final int currValue = this.mCurrValue;
        int value;
        float xPosition;

        for (int i = 0; i < halfCount; i++) {
            // right
            xPosition = rWidth / 2f + i * this.mLineDivder + distanceX;
            value = currValue + i;

            if (xPosition <= rWidth && value >= 0 && value <= this.mMaxValue) {
                if (value % this.mModType == 0) {
                    // 每个单位只绘制中间一条线
                    if (this.mModType == MOD_TYPE_HALF) {
                        // 绘制大格刻度线
                        this.linePaint.setColor(this.mLineColorMax);
                        canvas.drawLine(xPosition, ry, xPosition, ry + this.mLineHeighMax, this.linePaint);

                        if (this.isScaleValue) {
                            // 绘制刻度文本
                            canvas.drawText(String.valueOf(value / 2), xPosition, ry - this.txtPaddingBottom, this.textPaint);
                        }
                    } else if (this.mModType == MOD_TYPE_SCALE) {// 绘制每个小格刻度
                        if (value % (MOD_TYPE_SCALE * 2) == 0) {
                            // 绘制大格刻度线
                            this.linePaint.setColor(this.mLineColorMax);
                            canvas.drawLine(xPosition, ry, xPosition, ry + this.mLineHeighMax, this.linePaint);

                            if (this.isScaleValue) {
                                // 绘制刻度文本
                                canvas.drawText(String.valueOf(value == 0 ? 1 : value * this.scaleValue), xPosition, ry - this.txtPaddingBottom, this.textPaint);
                            }
                        } else {
                            // 绘制中格刻度线
                            this.linePaint.setColor(this.mLineColorMid);
                            canvas.drawLine(xPosition, ry + this.mLineHeighMax, xPosition, ry + this.mLineHeighMax - this.mLineHeighMid, this.linePaint);
                        }
                    }
                } else {
                    // 绘制最小刻度线
                    this.linePaint.setColor(this.mLineColorMin);
                    canvas.drawLine(xPosition, ry + this.mLineHeighMax, xPosition, ry + this.mLineHeighMax - this.mLineHeighMin, this.linePaint);
                }
            }

            // left
            xPosition = rWidth / 2f - i * this.mLineDivder + distanceX;
            value = currValue - i;

            if (xPosition > getPaddingLeft() && value >= 0 && value <= this.mMaxValue) {
                if (value % this.mModType == 0) {
                    // 每个单位只绘制中间一条线
                    if (this.mModType == MOD_TYPE_HALF) {
                        // 绘制大格刻度线
                        this.linePaint.setColor(this.mLineColorMax);
                        canvas.drawLine(xPosition, ry, xPosition, ry + this.mLineHeighMax, this.linePaint);

                        if (this.isScaleValue) {
                            // 绘制刻度文本
                            canvas.drawText(String.valueOf(value / 2), xPosition, ry - this.txtPaddingBottom, textPaint);
                        }
                    } else if (this.mModType == MOD_TYPE_SCALE) {// 绘制每个小格刻度
                        if (value % (MOD_TYPE_SCALE * 2) == 0) {
                            // 绘制大格刻度线
                            this.linePaint.setColor(this.mLineColorMax);
                            canvas.drawLine(xPosition, ry, xPosition, ry + this.mLineHeighMax, this.linePaint);

                            if (this.isScaleValue) {
                                // 绘制刻度文本
                                canvas.drawText(String.valueOf(value == 0 ? 1 : value * this.scaleValue), xPosition, ry - this.txtPaddingBottom, this.textPaint);
                            }
                        } else {
                            // 绘制中格刻度线
                            this.linePaint.setColor(this.mLineColorMid);
                            canvas.drawLine(xPosition, ry + this.mLineHeighMax, xPosition, ry + this.mLineHeighMax - this.mLineHeighMid, this.linePaint);
                        }
                    }
                } else {
                    // 绘制最小刻度线
                    this.linePaint.setColor(this.mLineColorMin);
                    canvas.drawLine(xPosition, ry + this.mLineHeighMax, xPosition, ry + this.mLineHeighMax - this.mLineHeighMin, this.linePaint);
                }
            }
        }
    }

    private void drawMiddleLine(Canvas canvas, int rWidth, int ry) {
        canvas.drawLine(rWidth / 2, ry + this.mLineHeighMax - this.lineMidLineHeight, rWidth / 2, ry + this.mLineHeighMax, this.markPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!this.isEnabled()) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.mDownFocusX = event.getX();
                this.mDownFocusY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                if (!this.isDisallowIntercept && Math.abs(event.getY() - this.mDownFocusY) < Math.abs(event.getX() - this.mDownFocusX)) {
                    this.isDisallowIntercept = true;

                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                this.isDisallowIntercept = false;

                break;
        }

        return scroller.onTouchEvent(event);
    }

    private OnWheelScrollListener onWheelListener;

    /**
     * Adds wheel changing listener
     *
     * @param listener the listener
     */
    public void setScrollingListener(OnWheelScrollListener listener) {
        this.onWheelListener = listener;
    }

    /**
     * Removes wheel changing listener
     */
    public void removeScrollingListener() {
        this.onWheelListener = null;
    }

    /**
     * Notifies changing listeners
     *
     * @param oldValue the old wheel value
     * @param newValue the new wheel value
     */
    protected void notifyScrollingListeners(int oldValue, int newValue) {
        this.onWheelListener.onChanged(this, oldValue * this.scaleValue, newValue * this.scaleValue);
    }

    private void notifyScrollingListenersAboutStart() {
        if (null != this.onWheelListener) {
            this.onWheelListener.onScrollingStarted(this);
        }
    }

    private void notifyScrollingListenersAboutEnd() {
        if (null != this.onWheelListener) {
            this.onWheelListener.onScrollingFinished(this);
        }
    }

    public interface OnWheelScrollListener {
        /**
         * Callback method to be invoked when current item changed
         *
         * @param wheel    the wheel view whose state has changed
         * @param oldValue the old value of current item
         * @param newValue the new value of current item
         */
        void onChanged(RulerWheel wheel, int oldValue, int newValue);

        /**
         * Callback method to be invoked when scrolling started.
         *
         * @param wheel the wheel view whose state has changed.
         */
        void onScrollingStarted(RulerWheel wheel);

        /**
         * Callback method to be invoked when scrolling ended.
         *
         * @param wheel the wheel view whose state has changed.
         */
        void onScrollingFinished(RulerWheel wheel);
    }
}