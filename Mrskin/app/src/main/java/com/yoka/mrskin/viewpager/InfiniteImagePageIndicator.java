/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2012 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yoka.mrskin.viewpager;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import support.vx.t3party.pagerindicator.IconPagerAdapter;
import support.vx.t3party.pagerindicator.IcsLinearLayout;
import support.vx.t3party.pagerindicator.PageIndicator;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.yoka.mrskin.R;
import com.yoka.mrskin.infiniteviewpager.InfiniteViewPager;
import com.yoka.mrskin.main.AppContext;
import com.yoka.mrskin.util.YKUtil;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class InfiniteImagePageIndicator extends HorizontalScrollView implements
        PageIndicator {
    private final IcsLinearLayout mIconsLayout;

    private InfiniteViewPager mViewPager;
    private OnPageChangeListener mListener;
    private Runnable mIconSelector;
    private int mSelectedIndex;
    private int mPadding = YKUtil.dip2px(AppContext.getInstance(),3);

    public InfiniteImagePageIndicator(Context context) {
        this(context, null);
    }

    public InfiniteImagePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);

        mIconsLayout = new IcsLinearLayout(context,
                R.attr.vpiIconPageIndicatorStyle);
        addView(mIconsLayout, new LayoutParams(WRAP_CONTENT, MATCH_PARENT,
                Gravity.CENTER));
    }

    private void animateToIcon(final int position) {
        final View iconView = mIconsLayout.getChildAt(position);
        if (mIconSelector != null) {
            removeCallbacks(mIconSelector);
        }
        mIconSelector = new Runnable() {
            public void run() {
                final int scrollPos = iconView.getLeft()
                        - (getWidth() - iconView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mIconSelector = null;
            }
        };
        post(mIconSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mIconSelector != null) {
            // Re-post the selector we saved
            post(mIconSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mIconSelector != null) {
            removeCallbacks(mIconSelector);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {
        position = convertPagePosition(position);

        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset,
                    positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        position = convertPagePosition(position);

        setCurrentItem(position);
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        mViewPager = (InfiniteViewPager) view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    private int getPagerCount() {
        return ((InfinitePagerAdapter) mViewPager.getAdapter()).getRealCount();
    }

    public void notifyDataSetChanged() {
        mIconsLayout.removeAllViews();
        IconPagerAdapter iconAdapter = (IconPagerAdapter) mViewPager
                .getAdapter();
        int count = getPagerCount();
        for (int i = 0; i < count; i++) {
            ImageView view = new ImageView(getContext(), null,
                    R.attr.vpiIconPageIndicatorStyle);
            view.setImageResource(iconAdapter.getIconResId(i));
            view.setPadding(mPadding, 0, mPadding, 0);
            mIconsLayout.addView(view);
        }
        if (mSelectedIndex > count) {
            mSelectedIndex = count - 1;
        }
        setCurrentItem(mSelectedIndex);
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    private int convertPagePosition(int position) {
        int count = getPagerCount();
        return position % count;
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }

        item = convertPagePosition(item);

        mSelectedIndex = item;
        // mViewPager.setCurrentItem(item);

        int tabCount = mIconsLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            View child = mIconsLayout.getChildAt(i);
            boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToIcon(item);
            }
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }
}
