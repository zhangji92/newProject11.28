package com.measureactivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lenovo on 2016/10/21.
 *
 */

public class CustomView extends View {
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode=MeasureSpec.getMode(widthMeasureSpec);//获取测量模式
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);//获取宽度
        int result=0;
        if (specMode==MeasureSpec.EXACTLY){
            result=widthSize;
        }else if (specMode==MeasureSpec.AT_MOST){
            result=Math.min(widthSize,400);
        }
        //设置宽高
        setMeasuredDimension(result,result);
    }
}
