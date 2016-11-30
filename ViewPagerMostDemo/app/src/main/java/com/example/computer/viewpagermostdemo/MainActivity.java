package com.example.computer.viewpagermostdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * author-sky
 * 解决viewpager图片小于2时左滑出现空白
 */
public class MainActivity extends Activity {
	private ViewPager viewPager;
	private List<ImageView> list;// viewpager资源
	private int[] imagesid;
	private int num = 300;
	private ImageView imageView1, imageView2, imageView3,imageView4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);
		imageView1 = (ImageView) findViewById(R.id.ad1);
		imageView2 = (ImageView) findViewById(R.id.ad2);
		imageView3 = (ImageView) findViewById(R.id.ad3);
		imageView4 = (ImageView) findViewById(R.id.ad4);
		list = new ArrayList<ImageView>();
		imagesid = new int[] { R.drawable.test1 ,R.drawable.test2};

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.viewGroup);

		viewPager = (ViewPager) findViewById(R.id.viewPager);


		for (int i = 0; i < imagesid.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imagesid[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			list.add(imageView);
		}
		
		/**
		 * 图片小于2时再次添加图片数量，使list.size = list.size * 2
		 * 这是最简单的方法
		 */
		if(list.size() == 2){
			for (int i = 0; i < imagesid.length; i++) {
				ImageView imageView = new ImageView(this);
				imageView.setImageResource(imagesid[i]);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				list.add(imageView);
			}
		}

		viewPager.setAdapter(new MyAdapter());
		viewPager.setOnPageChangeListener(new MyListener());
		if(list.size() > 1){
			viewPager.setCurrentItem(300);//小于2时不能左滑
		}
		//mHandler.postDelayed(mRunnable, 2000);

	}

	/**
	 * 开启无限循环
	 */
	private Handler mHandler = new Handler();
	private Runnable mRunnable = new Runnable() {
		public void run() {
			// 每隔多长时间执行一次
			// mHandler.postDelayed(mRunnable, 1000*PhoneConstans.TIMEVALUE);
			mHandler.postDelayed(mRunnable, 1000 * 3);
			num++;
			viewHandler.sendEmptyMessage(num);
		}

	};

	// private View getView(String id, Intent intent) {
	// return manager.startActivity(id, intent).getDecorView();
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			this.finish();
			mHandler.removeCallbacks(mRunnable);
		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}

	private final Handler viewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			viewPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}

	};

	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return list.size() > 1 ? Integer.MAX_VALUE : list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			//((ViewPager) arg0).removeView(list.get(arg1 % list.size()));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
//			try {
//				//((ViewPager) arg0).addView(list.get(arg1),0);
//				((ViewPager) arg0).addView((View) list.get(arg1 % list.size()),0);
//			} catch (Exception e) {
//			}

			arg1 %= list.size();
			if (arg1<0){
				arg1 = list.size()+arg1;
			}
			ImageView view = (ImageView)list.get(arg1);
			//如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
			ViewParent vp =view.getParent();
			if (vp!=null){
				ViewGroup parent = (ViewGroup)vp;
				parent.removeView(view);
			}
			((ViewPager)arg0).addView(view,0);
			final int finalArg = arg1;
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i("yang","finalArg     "+ finalArg);
				}
			});


			return list.size() > 1 ? view : list.get(0);

		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	class MyListener implements OnPageChangeListener {
		// 当滑动状态改变时调用
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// arg0=arg0%list.size();
		}

		// 当当前页面被滑动时调用
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		// 当新的页面被选中时调用
		@Override
		public void onPageSelected(int arg0) {
			num = arg0;
			arg0 = arg0 % list.size();
			Toast.makeText(getApplicationContext(), arg0 + "", Toast.LENGTH_SHORT).show();
			switch (arg0) {
			case 0:
				imageView1.setBackgroundResource(R.drawable.ad1);
				imageView2.setBackgroundResource(R.drawable.ad2);
				imageView3.setBackgroundResource(R.drawable.ad2);
				imageView4.setBackgroundResource(R.drawable.ad2);
				break;
			case 1:
				imageView1.setBackgroundResource(R.drawable.ad2);
				imageView2.setBackgroundResource(R.drawable.ad1);
				imageView3.setBackgroundResource(R.drawable.ad2);
				imageView4.setBackgroundResource(R.drawable.ad2);
				break;
			case 2:
				imageView1.setBackgroundResource(R.drawable.ad2);
				imageView2.setBackgroundResource(R.drawable.ad2);
				imageView3.setBackgroundResource(R.drawable.ad1);
				imageView4.setBackgroundResource(R.drawable.ad2);
				break;
				case 3:
					imageView1.setBackgroundResource(R.drawable.ad2);
					imageView2.setBackgroundResource(R.drawable.ad2);
					imageView3.setBackgroundResource(R.drawable.ad2);
					imageView4.setBackgroundResource(R.drawable.ad1);
					break;

			default:
				break;
			}
			// for (int i = 0; i < imageViews.length; i++) {
			// imageViews[arg0].setBackgroundResource(R.drawable.ad1);
			// if (arg0 != i) {
			// imageViews[i].setBackgroundResource(R.drawable.ad2);
			// }
			// }
			//System.out.println("当前是第" + arg0 + "页");
		}

	}
}