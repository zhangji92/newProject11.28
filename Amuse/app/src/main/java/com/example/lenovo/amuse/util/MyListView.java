package com.example.lenovo.amuse.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.lenovo.amuse.R;

/**
 * Created by lenovo on 2016/10/21.
 *
 */

public class MyListView extends ListView {
    private int height;
    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        View view=adapter.getView(0,null,null);
        view.measure(MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
        int itemHeight=view.getMeasuredHeight();
        int count=adapter.getCount();
        height=itemHeight*count+getResources().getDimensionPixelSize(R.dimen.divider)*count;
        measure(MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED),height);
    }

    public int getRealHeight(){
        return height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY));
    }
}
