package com.allactivity.viewpager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.allactivity.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张继 on 2016/11/22.
 *
 */

public class ViewPagerActivity1 extends Activity {
    private static final String LOG_TAG = "MainActivity";
    public ImageViewHandler handler = new ImageViewHandler(new WeakReference<ViewPagerActivity1>(this));
    public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_activity);
        //初始化iewPager的内容
        viewPager = (ViewPager) findViewById(R.id.viewPager_activity);
        ImageView imageView1 = new ImageView(this);
        imageView1.setImageResource(R.drawable.www);
        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.xiaxue);
        List<View> views = new ArrayList<>();
        views.add(imageView1);
        views.add(imageView2);
        viewPager.setAdapter(new ImageAdapter(views));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //配合Adapter的currentItem字段进行设置。
            @Override
            public void onPageSelected(int arg0) {
                handler.sendMessage(Message.obtain(handler, ImageViewHandler.MSG_PAGE_CHANGED, arg0, 0));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            //覆写该方法实现轮播效果的暂停和恢复
            @Override
            public void onPageScrollStateChanged(int arg0) {
                switch (arg0) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handler.sendEmptyMessage(ImageViewHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        handler.sendEmptyMessageDelayed(ImageViewHandler.MSG_UPDATE_IMAGE, ImageViewHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);//默认在中间，使用户看不到边界
        //开始轮播效果
        handler.sendEmptyMessageDelayed(ImageViewHandler.MSG_UPDATE_IMAGE, ImageViewHandler.MSG_DELAY);
    }//end of onCreate
}