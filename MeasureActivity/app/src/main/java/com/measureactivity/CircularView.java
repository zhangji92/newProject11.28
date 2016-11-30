package com.measureactivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 张继 on 2016/10/24.
 * 自定义画圆
 */

public class CircularView extends View {
    Paint paint=new Paint();

    public CircularView(Context context) {
       this(context,null);
    }

    public CircularView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircularView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性集合
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.CircularView);
        int color=typedArray.getColor(R.styleable.CircularView_color_circular,0);
        paint.setColor(color);
        typedArray.recycle();
    }
    int width;
    int height;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽的测量模式和宽度
        int specWidthMode=MeasureSpec.getMode(widthMeasureSpec);
        int specWidthSize=MeasureSpec.getSize(widthMeasureSpec);
        //获取高的测量模式和宽度
        int specHeightMode=MeasureSpec.getMode(heightMeasureSpec);
        int specHeightSize=MeasureSpec.getSize(heightMeasureSpec);
        if (specWidthMode==MeasureSpec.EXACTLY){
            width=specWidthSize;
        }else if (specWidthMode==MeasureSpec.AT_MOST){
            width=Math.min(specWidthSize,200);
        }

        if (specHeightMode==MeasureSpec.EXACTLY){
            height=specHeightSize;
        }else if (specHeightMode==MeasureSpec.AT_MOST){
            height=Math.min(specHeightSize,200);
        }
        //设置宽高
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        int paddingLeft=getPaddingLeft();
        int paddingRight=getPaddingRight();
        canvas.drawCircle(width/2,height/2,(width-paddingLeft-paddingRight)/2,paint);
        super.onDraw(canvas);
    }
}
