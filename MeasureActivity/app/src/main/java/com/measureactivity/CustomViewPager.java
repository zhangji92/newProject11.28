package com.measureactivity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2016/10/21.
 *
 */

public class CustomViewPager extends ViewPager {
    //先给出了一个Map的数据结构，保存了所有的子控件的位置以及对应的高度的映射表
    private Map<Integer,Integer> map=new HashMap<>();
    //表示当前显示的页面的索引
    private int current;
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height=0;
        if (map.size()>current){
            height=map.get(current+1);
        }
        heightMeasureSpec=MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public void resetHeight(int current){
        this.current=current;
        if (map.size()>current){
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) getLayoutParams();
            if (layoutParams==null){
                layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,map.get(current+1));
            }else {
                layoutParams.height=map.get(current+1);
            }
            setLayoutParams(layoutParams);
        }
    }

    public void calculate(int type,int height){
        map.put(type,height);
    }
}
