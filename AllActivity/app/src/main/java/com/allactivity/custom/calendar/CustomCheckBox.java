package com.allactivity.custom.calendar;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.CheckBox;

/**
 * Created by 张继 on 2016/11/22.
 */

public class CustomCheckBox extends CheckBox {

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        Log.e("abc","dispatchPopulateAccessibilityEvent");
        return super.dispatchPopulateAccessibilityEvent(event);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        Log.e("abc","onRestoreInstanceState");
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        Log.e("abc","onCreateDrawableState");
        return super.onCreateDrawableState(extraSpace);
    }

    public CustomCheckBox(Context context) {
        super(context);
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
