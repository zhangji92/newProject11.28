package com.yoka.mrskin.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yoka.mrskin.R;

public class LoadingAnimUtil extends LinearLayout
{

    public LoadingAnimUtil(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    public LoadingAnimUtil(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public LoadingAnimUtil(Context context)
    {
        super(context);
        init(context);
    } 

    private ImageView mRotate;

    private void init(Context context) {
        setOrientation(VERTICAL);
        setBackgroundColor(0x00000000);
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.anim_anim_anim, null, false);
        addView(rootView);
        mRotate = (ImageView) findViewById(R.id.loading_rotate);
    }

    public void startAnim() {
        final Animation rotateAnim = AnimationUtils.loadAnimation(getContext(),
                R.anim.tip);
        LinearInterpolator polator = new LinearInterpolator();
        rotateAnim.setInterpolator(polator);
        mRotate.startAnimation(rotateAnim);
    }

    public void stopAnim() {
        mRotate.clearAnimation();
    }

}
