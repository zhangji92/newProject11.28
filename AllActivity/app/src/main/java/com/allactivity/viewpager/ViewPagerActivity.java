package com.allactivity.viewpager;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.allactivity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张继 on 2016/11/21.
 * 轮播图
 */

public class ViewPagerActivity extends Activity {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (mHandler.hasMessages(1)) {
                mHandler.removeMessages(1);
            }
            if (msg.what == 1) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                mHandler.sendEmptyMessageDelayed(1, 3000);
            }
        }
    };
    private ViewPager mViewPager;
    private List<View> mList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_activity);
        initViews();
        initDat();
    }

    private void initDat() {
        //图片适配器
        ImageViewPagerAdapter imageViewPagerAdapter = new ImageViewPagerAdapter(mList);
        //添加数据
        mViewPager.setAdapter(imageViewPagerAdapter);
        //默认在中间，使用户看不到边界
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);

        mHandler.sendEmptyMessageDelayed(1, 3000);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            /**
             * onPageScrollStateChanged()// state == ViewPager.SCROLL_STATE_DRAGGING  正在滑动状态
             *  nPageScrolled()  //滑动状态
             * onPageScrollStateChanged() // state ==SCROLL_STATE_SETTLING  自动沉降状态
             * onPageSelected() // 选中状态
             * onPageScrolled()  //滑动状态
             * onPageScrollStateChanged()  //state==SCROLL_STATE_IDLE   空闲状态  滑动结束
             * @param state 状态
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                //正在滑动状态
                if (state == mViewPager.SCROLL_STATE_DRAGGING) {
                    mHandler.removeMessages(1);
                    //自动沉降状态
                } else if (state == mViewPager.SCROLL_STATE_SETTLING) {
                    mHandler.removeMessages(1);
                } else {
                    mHandler.sendEmptyMessageDelayed(1, 3000);
                }
            }
        });
    }

    /**
     * 加载视图控件
     */
    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager_activity);
        ImageView imageView1 = new ImageView(this);
        imageView1.setImageResource(R.drawable.www);
        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.xiaxue);
//        ImageView imageView3 = new ImageView(this);
//        imageView3.setImageResource(R.drawable.xueren);

        mList.add(imageView1);
        mList.add(imageView2);
//        mList.add(imageView3);
    }

    class ImageViewPagerAdapter extends PagerAdapter {
        private List<View> mListView;


        public ImageViewPagerAdapter(List<View> mListView) {
            this.mListView = mListView;
        }

        @Override
        public int getCount() {
            //设置成最大，使用户看不到边界
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //对ViewPager页号求余取出View列表中要显示的项
            position %= mListView.size();
            if (position < 0) {
                position = mListView.size() + 1;
            }
            View view = mListView.get(position);
            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewParent vt = view.getParent();
            if (vt != null) {
                ViewGroup parent = (ViewGroup) vt;
                parent.removeView(view);
            }
            container.addView(view);

            return view;
        }
    }
}
