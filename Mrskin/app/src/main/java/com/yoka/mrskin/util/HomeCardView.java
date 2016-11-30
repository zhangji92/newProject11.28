package com.yoka.mrskin.util;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yoka.mrskin.R;
import com.yoka.mrskin.model.managers.task.YKTaskManager.HomeCardData;

public class HomeCardView extends FrameLayout
{

    private final static String TAG = "HomeCardView";

    /**
     * 手势监听
     */
    private GestureDetector mGesturedDetector;
    private int[] mThemeColor;
    private int[] mThemeBackground;
    private int[] mThemeButtonBackground;
    private int[] mThemeSubTitle;

    private void resetStyleColor() {
        mThemeColor = new int[] { 0xffFCFFFF, 0xffFCFFFF, 0xfff26196 };
        mThemeBackground = new int[] { R.drawable.home_card_red_bg,
                R.drawable.home_card_orange_bg, R.drawable.home_card_white_bg };
        mThemeButtonBackground = new int[] { R.drawable.home_done_white_btn,
                R.drawable.home_done_orange_btn, R.drawable.home_done_red_btn };
        mThemeSubTitle = new int[] { 0xffFCFFFF, 0xffFCFFFF, 0xffB5B5B5 };
    }

    @SuppressWarnings("unused")
    private View mPopAnthorView;
    private View mNoContentView;
    private ArrayList<HomeCardData> mContentData;
    private boolean isAnimationStart;
    private String mAdage;

    public HomeCardView(Context context)
    {
        super(context);
        init(context);
    }

    public HomeCardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);

    }

    public HomeCardView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        resetStyleColor();
        YKGestureListener mGestureListener = new YKGestureListener();
        mGesturedDetector = new GestureDetector(context, mGestureListener);
    }

    public void setContentData(ArrayList<HomeCardData> content) {
        mNoContentView = null;
        removeAllViews();
        resetStyleColor();
        mContentData = content;
        if (content == null || content.size() < 1) {
            addView(createNoContentChildView(getContext()));
            return;
        }
        int size = content.size();
        switch (size) {
        default:
        case 3:
            addView(createChildView(getContext()));
        case 2:
            addView(createChildView(getContext()));
        case 1:
            addView(createChildView(getContext()));
            break;
        }
    }

    public void setNoContentAlert(String adage) {
        mAdage = adage;
        updateNoContentAlert();
    }

    public void changeDataSort() {
        mContentData.add(mContentData.remove(0));
    }


    public void setPopAuthorView(View view) {
        mPopAnthorView = view;
    }

    private final int MARGIN_TOP_BOTTOM_ADD_STEP = 30;
    private final int MARGIN_LEFT_RIGHT_ADD_STEP = 20;
    private final int MARGIN_RIGHT_LEFT_ADD_STEP = 10;

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mNoContentView != null) {
            return;
        }
        int childCount = getChildCount();
        LayoutParams layoutParams = null;
        View child = null;
        TextView title = null;
        // ImageView right = null;
        TextView subTitle = null;
        HomeCardData task = null;
        switch (childCount) {
        default:
        case 3:
            child = getChildAt(2);
            layoutParams = new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = MARGIN_TOP_BOTTOM_ADD_STEP;
            layoutParams.bottomMargin = 0;
            layoutParams.leftMargin = MARGIN_RIGHT_LEFT_ADD_STEP;
            layoutParams.rightMargin = MARGIN_RIGHT_LEFT_ADD_STEP;
            child.setLayoutParams(layoutParams);

            title = (TextView) child.findViewById(R.id.card_title);
            // right = (ImageView) child.findViewById(R.id.card_right_image);
            subTitle = (TextView) child.findViewById(R.id.card_subtitle);

            task = mContentData.get(0);
            applyDataToView(task, child);

            child.setBackgroundResource(mThemeBackground[2]);
            // right.setBackgroundResource(mThemeButtonBackground[2]);
            title.setTextColor(mThemeColor[2]);
            subTitle.setTextColor(mThemeSubTitle[2]);

            child = getChildAt(1);
            layoutParams = new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = MARGIN_LEFT_RIGHT_ADD_STEP;
            layoutParams.bottomMargin = MARGIN_RIGHT_LEFT_ADD_STEP;
            layoutParams.leftMargin = MARGIN_LEFT_RIGHT_ADD_STEP;
            layoutParams.rightMargin = MARGIN_LEFT_RIGHT_ADD_STEP;
            layoutParams.gravity = Gravity.BOTTOM;
            child.setLayoutParams(layoutParams);

            title = (TextView) child.findViewById(R.id.card_title);
            // right = (ImageView) child.findViewById(R.id.card_right_image);
            subTitle = (TextView) child.findViewById(R.id.card_subtitle);

            task = mContentData.get(1);
            applyDataToView(task, child);

            child.setBackgroundResource(mThemeBackground[1]);
            // right.setBackgroundResource(mThemeButtonBackground[1]);
            title.setTextColor(mThemeColor[1]);
            subTitle.setTextColor(mThemeSubTitle[1]);

            child = getChildAt(0);
            layoutParams = new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = MARGIN_RIGHT_LEFT_ADD_STEP;
            layoutParams.bottomMargin = MARGIN_LEFT_RIGHT_ADD_STEP;
            layoutParams.leftMargin = MARGIN_TOP_BOTTOM_ADD_STEP;
            layoutParams.rightMargin = MARGIN_TOP_BOTTOM_ADD_STEP;
            layoutParams.gravity = Gravity.BOTTOM;
            child.setLayoutParams(layoutParams);

            title = (TextView) child.findViewById(R.id.card_title);
            // right = (ImageView) child.findViewById(R.id.card_right_image);
            subTitle = (TextView) child.findViewById(R.id.card_subtitle);

            task = mContentData.get(2);
            applyDataToView(task, child);
            System.out.println(mThemeBackground[0] + "   "
                    + R.drawable.home_card_white_bg);
            child.setBackgroundResource(mThemeBackground[0]);
            // right.setBackgroundResource(mThemeButtonBackground[0]);
            title.setTextColor(mThemeColor[0]);
            subTitle.setTextColor(mThemeSubTitle[0]);
            break;
        case 2:
            child = getChildAt(1);
            layoutParams = new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = MARGIN_TOP_BOTTOM_ADD_STEP;
            layoutParams.bottomMargin = 0;
            layoutParams.leftMargin = MARGIN_RIGHT_LEFT_ADD_STEP;
            layoutParams.rightMargin = MARGIN_RIGHT_LEFT_ADD_STEP;
            child.setLayoutParams(layoutParams);

            title = (TextView) child.findViewById(R.id.card_title);
            // right = (ImageView) child.findViewById(R.id.card_right_image);
            subTitle = (TextView) child.findViewById(R.id.card_subtitle);

            task = mContentData.get(0);
            applyDataToView(task, child);

            child.setBackgroundResource(mThemeBackground[2]);
            // right.setBackgroundResource(mThemeButtonBackground[1]);
            title.setTextColor(mThemeColor[2]);
            subTitle.setTextColor(mThemeSubTitle[2]);

            child = getChildAt(0);
            layoutParams = new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = MARGIN_LEFT_RIGHT_ADD_STEP;
            layoutParams.bottomMargin = MARGIN_RIGHT_LEFT_ADD_STEP;
            layoutParams.leftMargin = MARGIN_LEFT_RIGHT_ADD_STEP;
            layoutParams.rightMargin = MARGIN_LEFT_RIGHT_ADD_STEP;
            layoutParams.gravity = Gravity.BOTTOM;
            child.setLayoutParams(layoutParams);

            title = (TextView) child.findViewById(R.id.card_title);
            // right = (ImageView) child.findViewById(R.id.card_right_image);
            subTitle = (TextView) child.findViewById(R.id.card_subtitle);

            task = mContentData.get(1);
            applyDataToView(task, child);

            child.setBackgroundResource(mThemeBackground[1]);
            // right.setBackgroundResource(mThemeButtonBackground[0]);
            title.setTextColor(mThemeColor[1]);
            subTitle.setTextColor(mThemeSubTitle[1]);

            break;
        case 1:
            child = getChildAt(0);
            layoutParams = new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = MARGIN_TOP_BOTTOM_ADD_STEP;
            layoutParams.bottomMargin = 0;
            layoutParams.leftMargin = MARGIN_RIGHT_LEFT_ADD_STEP;
            layoutParams.rightMargin = MARGIN_RIGHT_LEFT_ADD_STEP;
            child.setLayoutParams(layoutParams);

            title = (TextView) child.findViewById(R.id.card_title);
            // right = (ImageView) child.findViewById(R.id.card_right_image);
            subTitle = (TextView) child.findViewById(R.id.card_subtitle);

            task = mContentData.get(0);
            applyDataToView(task, child);

            child.setBackgroundResource(mThemeBackground[2]);
            // right.setBackgroundResource(mThemeButtonBackground[0]);
            title.setTextColor(mThemeColor[2]);
            subTitle.setTextColor(mThemeSubTitle[2]);
            break;
        case 0:
            break;
        }
    }

    private void applyDataToView(HomeCardData task, View childView) {
        if (task == null || childView == null) {
            return;
        }
        TextView title = (TextView) childView.findViewById(R.id.card_title);
        TextView subTitle = (TextView) childView
                .findViewById(R.id.card_subtitle);
        title.setText(task.getmTitle());
        subTitle.setText(task.getmSubTitle());
    }

    private void changeStyleSort() {
        int tmpThemeBackground = mThemeBackground[2];
        int tmpThemeButtonBackground = mThemeButtonBackground[2];
        int tmpThemeCoror = mThemeColor[2];
        int tmpThemeSubTitle = mThemeSubTitle[2];

        for (int i = 2; i > -1; i--) {
            if (i == 0) {
                mThemeBackground[i] = tmpThemeBackground;
                mThemeButtonBackground[i] = tmpThemeButtonBackground;
                mThemeColor[i] = tmpThemeCoror;
                mThemeSubTitle[i] = tmpThemeSubTitle;
            } else {
                mThemeBackground[i] = mThemeBackground[i - 1];
                mThemeButtonBackground[i] = mThemeButtonBackground[i - 1];
                mThemeColor[i] = mThemeColor[i - 1];
                mThemeSubTitle[i] = mThemeSubTitle[i - 1];
            }

        }
    }

    class YKGestureListener extends SimpleOnGestureListener
    {

        @Override
        public boolean onDown(MotionEvent arg0) {
            Log.d(TAG, "on Down");
            return false;
        }

        @Override
        public boolean onFling(MotionEvent arg0, MotionEvent arg1,
                float velocityX, float velocityY) {
            if (getChildCount() < 3) {
                return false;
            }
            if (Math.abs(velocityX) > Math.abs(velocityY) && !isAnimationStart) {
                animAyion(velocityX > 0);
                return true;
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent arg0) {
        }

        @Override
        public boolean onScroll(MotionEvent downEvent, MotionEvent movEvent,
                float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent arg0) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent arg0) {
            return true;
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mGesturedDetector != null && mNoContentView == null) {
            mGesturedDetector.onTouchEvent(ev);
        }
        super.dispatchTouchEvent(ev);
        return true;
    }

    private View createChildView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.card, null);
    }

    private View createNoContentChildView(Context context) {
        if (mNoContentView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mNoContentView = inflater.inflate(R.layout.card_nocard, null);
            updateNoContentAlert();
        }
        return mNoContentView;
    }

    private void updateNoContentAlert() {
        if (mNoContentView == null || TextUtils.isEmpty(mAdage)) {
            return;
        }
        TextView mLoveHen = (TextView) mNoContentView
                .findViewById(R.id.card_nocard);
        mLoveHen.setText(mAdage);
    }


    public void animAyion(boolean isLeft) {
        AnimationSet animation = new AnimationSet(true);
        // TranslateAnimation translate = new TranslateAnimation(
        // Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
        // 500.0f, Animation.RELATIVE_TO_SELF, 0f,
        // Animation.RELATIVE_TO_SELF, 0f);
        TranslateAnimation translate = new TranslateAnimation(0, isLeft ? 500
                : -500, 0, 0);
        translate.setDuration(200);
        animation.addAnimation(translate);
        animation.setAnimationListener(mAninAnimationListener);

        getChildAt(getChildCount() - 1).startAnimation(animation);

    }

    private AnimationListener mAninAnimationListener = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
            isAnimationStart = true;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            changeDataSort();
            changeStyleSort();
            requestLayout();
            isAnimationStart = false;

        }
    };
}