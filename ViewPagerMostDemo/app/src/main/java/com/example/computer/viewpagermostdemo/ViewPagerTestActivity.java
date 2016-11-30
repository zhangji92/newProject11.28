package com.example.computer.viewpagermostdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 现在的只是适合2张以上的图片，
 * 如果是一张图片，则在
 *            1添加图片的时候需要循环添加第一张图（setTag仍为0）
 *            2在图片滑动的时候下面的小点设置只有一张图时的始终显示小点（选中状态即可）
 *
 */

public class ViewPagerTestActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager vp;
    private ArrayList<View> images;
    private ImageView[] circyles;
    private ViewGroup viewGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_test);
        vp = (ViewPager) findViewById(R.id.viewPager);
        viewGroup = (ViewGroup) findViewById(R.id.viewGroup);
        initData();

    }

    private void initData() {
        images = new ArrayList<View>();


        for(int i=0;i<3;i++){
            ImageView image = new ImageView(this);
            image.setBackgroundResource(R.drawable.test1);
            image.setTag(i);
            image.setOnClickListener(this);
            images.add(image);
        }
        circyles = new ImageView[images.size()];
        for (int i=0;i<images.size();i++){
            ImageView image = new ImageView(this);
            image.setLayoutParams(new ViewGroup.LayoutParams(20,20));
            image.setPadding(20,0,20,0);
            circyles[i] = image;
            if(i==0){
                circyles[i].setBackgroundResource(R.drawable.banner_dian_focus);
            }else{
                circyles[i].setBackgroundResource(R.drawable.banner_dian_blur);
            }
            viewGroup.addView(image);
        }
        MyViewPagerAdater ada = new MyViewPagerAdater();

        vp.setAdapter(ada);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, position, 0));
                position %= (images.size());
                for (int i=0;i<images.size();i++){
                    if(i == position){
                        circyles[i].setBackgroundResource(R.drawable.banner_dian_focus);
                    }else {
                        circyles[i].setBackgroundResource(R.drawable.banner_dian_blur);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });
        vp.setCurrentItem(0);
        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);


    }
    private ImageHandler handler = new ImageHandler(new WeakReference<ViewPagerTestActivity>(this));

    @Override
    public void onClick(View v) {
        int ii = (int) v.getTag();
        Log.i("yang","当前点击的索引是："+ii);

    }

    private static class ImageHandler extends Handler {

        /**
         * 请求更新显示的View。
         */
        protected static final int MSG_UPDATE_IMAGE  = 1;
        /**
         * 请求暂停轮播。
         */
        protected static final int MSG_KEEP_SILENT   = 2;
        /**
         * 请求恢复轮播。
         */
        protected static final int MSG_BREAK_SILENT  = 3;
        /**
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
         */
        protected static final int MSG_PAGE_CHANGED  = 4;

        //轮播间隔时间
        protected static final long MSG_DELAY = 3000;

        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
        private WeakReference<ViewPagerTestActivity> weakReference;
        private int currentItem = 0;
//		private int currentItem = Integer.MAX_VALUE/2;

        protected ImageHandler(WeakReference<ViewPagerTestActivity> wk){
            weakReference = wk;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Log.d("yang", "receive message " + msg.what);
            ViewPagerTestActivity activity = weakReference.get();
            if (activity==null){
                //Activity已经回收，无需再处理UI了
                return ;
            }
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (activity.handler.hasMessages(MSG_UPDATE_IMAGE)){
                activity.handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    currentItem++;
                    activity.vp.setCurrentItem(currentItem);
                    //准备下次播放
                    activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    currentItem = msg.arg1;
                    break;
                default:
                    break;
            }
        }
    }
    private class MyViewPagerAdater extends PagerAdapter{
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try{
                container.addView(images.get(position%images.size()));
            }catch (Exception e){

            }
           /* position %= (images.size());
            if(position<0){
                position = images.size()+position;
            }
            ImageView view = (ImageView) images.get(position);
            ViewParent vp = view.getParent();
            if(vp!=null){
                ViewGroup vg = (ViewGroup) vp;
                vg.removeView(view);
            }
            final int finalPosition = position;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("yang",""+ finalPosition);
                }
            });


            container.addView(view);*/
            return images.get(position%images.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(images.size()>=3){
                ((ViewPager) container).removeView(images.get(position%images.size()));
            }
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
