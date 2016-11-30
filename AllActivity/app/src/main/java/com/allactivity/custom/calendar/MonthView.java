package com.allactivity.custom.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.allactivity.util.DateUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by 张继 on 2016/11/3.
 * 自定义月份
 */

public class MonthView extends View {
    //画笔
    private Paint mPaint;
    //
    private int mDayColor = Color.parseColor("#000000");
    private int mSelectDayColor = Color.parseColor("#ffffff");
    private int mSelectBGColor = Color.parseColor("#1FC2F3");
    private int mCurrentColor = Color.parseColor("#ff0000");
    //当前的年月日
    private int mCurrYear, mCurrMonth, mCurrDay;
    private int mSelYear, mSelMonth, mSelDay;
    //列宽和列高
    private int mColumnSize, mRowSize;
    //获取屏幕的数据
    private DisplayMetrics mDisplayMetrics;

    private int mDaySize = 18;

    private TextView tv_date, tv_week;

    private int weekRow;

    private int[][] daysString;
    //事务圆点
    private int mCircleRadius = 6;
    //日期点击事件回掉
    private DateClick dateClick;
    //圆点的颜色
    private int mCircleColor = Color.parseColor("#ff0000");

    private List<Integer> daysHasThingList;

    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取屏幕数据
        mDisplayMetrics = getResources().getDisplayMetrics();
        Calendar calendar = Calendar.getInstance();
        mPaint = new Paint();
        //当前的年分
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);
        //设置当前的年月日
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
        //初始化行高列宽
        mColumnSize = getWidth() / 7;
        mRowSize = getHeight() / 6;
        //放置日期数据
        daysString = new int[6][7];
        //字体大小以sp为单位
        mPaint.setTextSize(mDaySize * mDisplayMetrics.scaledDensity);
        //获取一个月有多少天
        int MonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);
        //获取当月一号是周几
        int weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);
        String dayString;
        for (int day = 0; day < MonthDays; day++) {
            //第几天
            dayString = (day + 1) + "";
            //烈数
            int column = (day + weekNumber - 1) % 7;
            //行数
            int row = (day + weekNumber - 1) / 7;
            daysString[row][column] = day + 1;
            //起始位置
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString)) / 2);
            int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);

            if (dayString.equals(mSelDay + "")) {
                //绘制背景色矩形
                int startRecX = mColumnSize * column;
                int startRecY = mRowSize * row;
                int endRecX = startRecX + mColumnSize;
                int endRecY = startRecY + mRowSize;
                //矩形颜色
                mPaint.setColor(mSelectBGColor);
                canvas.drawRect(startRecX, startRecY, endRecX, endRecY, mPaint);
                //记录第几行，即第几周
                weekRow = row + 1;
            }
            //绘制事务圆形标志
            drawCircle(row, column, day + 1, canvas);

            if (dayString.equals(mSelDay + "")) {
                mPaint.setColor(mSelectDayColor);
            } else if (dayString.equals(mCurrDay + "") && mCurrDay != mSelDay && mCurrMonth == mSelMonth) {
                //正常月，选中其他日期，则今日为红色
                mPaint.setColor(mCurrentColor);
            } else {
                mPaint.setColor(mDayColor);
            }
            //绘制日期
            canvas.drawText(dayString, startX, startY, mPaint);
            if(tv_date != null){
                tv_date.setText(mSelYear + "年" + (mSelMonth + 1) + "月");
            }

            if(tv_week != null){
                tv_week.setText("第" + weekRow  +"周");
            }
        }

    }
    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int downX=0,downY=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX= (int) event.getX();
                downY= (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX= (int) event.getX();
                int upY= (int) event.getY();
                if(Math.abs(upX-downX) < 10 && Math.abs(upY - downY) < 10){//点击事件
                    performClick();
                    doClickAction((upX + downX)/2,(upY + downY)/2);
                }
                break;
        }
        return true;

    }
    /**
     * 执行点击事件
     * @param x
     * @param y
     */
    private void doClickAction(int x,int y){
        int row = y / mRowSize;
        int column = x / mColumnSize;
        setSelectYearMonth(mSelYear,mSelMonth,daysString[row][column]);
        invalidate();
        //执行activity发送过来的点击处理事件
        if(dateClick != null){
            dateClick.onClick();
        }
    }
    /**
     * 左点击，日历向后翻页
     */
    public void onLeftClick(){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 0){//若果是1月份，则变成12月份
            year = mSelYear-1;
            month = 11;
        }else if(DateUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month-1;
            day = DateUtils.getMonthDays(year, month);
        }else{
            month = month-1;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }

    /**
     * 右点击，日历向前翻页
     */
    public void onRightClick(){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 11){//若果是12月份，则变成1月份
            year = mSelYear+1;
            month = 0;
        }else if(DateUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = DateUtils.getMonthDays(year, month);
        }else{
            month = month + 1;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }

    /**
     * 绘制事务圆形标志
     * @param row 行
     * @param column 列
     * @param day 天数
     * @param canvas
     */
    private void drawCircle(int row,int column,int day,Canvas canvas){
        if(daysHasThingList != null && daysHasThingList.size() >0){
            if(!daysHasThingList.contains(day))return;
            mPaint.setColor(mCircleColor);
            float circleX = (float) (mColumnSize * column +	mColumnSize*0.8);
            float circley = (float) (mRowSize * row + mRowSize*0.2);
            canvas.drawCircle(circleX, circley, mCircleRadius, mPaint);
        }
    }

    public void setDaysHasThingList(List<Integer> daysHasThingList) {
        this.daysHasThingList = daysHasThingList;
    }
    /**
     * 设置显示当前日期的控件
     * @param tv_date
     * 		显示日期
     * @param tv_week
     * 		显示周
     */
    public void setTextView(TextView tv_date,TextView tv_week){
        this.tv_date = tv_date;
        this.tv_week = tv_week;
        invalidate();
    }

    /**
     * 获取选择的年份
     * @return
     */
    public int getmSelYear() {
        return mSelYear;
    }
    /**
     * 获取选择的月份
     * @return
     */
    public int getmSelMonth() {
        return mSelMonth;
    }

    public int getmSelDay() {
        return this.mSelDay;
    }
    /**
     * 普通日期的字体颜色，默认黑色
     * @param mDayColor
     */
    public void setmDayColor(int mDayColor) {
        this.mDayColor = mDayColor;
    }

    /**
     * 选择日期的颜色，默认为白色
     * @param mSelectDayColor
     */
    public void setmSelectDayColor(int mSelectDayColor) {
        this.mSelectDayColor = mSelectDayColor;
    }

    /**
     * 选中日期的背景颜色，默认蓝色
     * @param mSelectBGColor
     */
    public void setmSelectBGColor(int mSelectBGColor) {
        this.mSelectBGColor = mSelectBGColor;
    }
    /**
     * 当前日期不是选中的颜色，默认红色
     * @param mCurrentColor
     */
    public void setmCurrentColor(int mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
    }

    /**
     * 日期的大小，默认18sp
     * @param mDaySize
     */
    public void setmDaySize(int mDaySize) {
        this.mDaySize = mDaySize;
    }
    /**
     * 设置年月日
     *
     * @param year  年
     * @param month 月
     * @param day   日
     */
    private void setSelectYearMonth(int year, int month, int day) {
        this.mSelYear = year;
        this.mSelMonth = month;
        this.mSelDay = day;
    }

    /**
     * 设置日期的点击回掉事件
     */
    public interface DateClick {
        void onClick();
    }
    /***
     * 设置圆圈的半径，默认为6
     * @param mCircleRadius
     */
    public void setmCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }


    /**
     * 设置圆圈的半径
     * @param mCircleColor
     */
    public void setmCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
    }
    /**
     * 设置日期点击事件
     *
     * @param dateClick
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }

    /**
     * 跳转至今天
     */
    public void setTodayToView() {
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
        invalidate();
    }
}
