package com.jrd.loan.widget;

import java.util.ArrayList;

import org.w3c.dom.Text;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.autoscroll.viewpager.AutoScrollViewPager;
import com.autoscroll.viewpager.CircleIndicator;
import com.autoscroll.viewpager.MyBasePagerAdapter;
import com.jrd.loan.R;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.bean.BannerList;
import com.jrd.loan.finance.ProjectIntroduceActivity;
import com.jrd.loan.net.imageloader.ImageLoader;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.AppInfoUtil;
import com.jrd.loan.util.FormatUtils;

/**
 * 自定义轮播图
 */
public class MyBanner extends RelativeLayout {
  // 自动轮播的时间间隔
  private final static int TIME_INTERVAL = 3;

  // 自动轮播启用开关
  private final static boolean isAutoPlay = true;
  private ArrayList<BannerList> bannerList;
  private AutoScrollViewPager viewPager;
  private Context context;
  private CircleIndicator pageIndex;

  private int downX;
  private int downY;

  public MyBanner(Context context) {
    this(context, null);
  }

  public MyBanner(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MyBanner(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;
  }

  public void setData(ArrayList<BannerList> list) {
    this.bannerList = list;

    initUI(this.context);

    if (isAutoPlay) {
      viewPager.setInterval(TIME_INTERVAL * 1000);
      viewPager.startAutoScroll();
    }
  }

  public void setDurtion(double d) {
    viewPager.setAutoScrollDurationFactor(d);
  }

  private void initUI(Context context) {
    viewPager.setAdapter(new MyPagerAdapter(context, bannerList.toArray()));

    pageIndex.setViewPager(viewPager);
    pageIndex.setSelectedPos(0);
  }

  private class MyPagerAdapter extends MyBasePagerAdapter {
    
    public MyPagerAdapter(Context context, Object[] objects) {
      super(context, objects);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      position = (getCount() + position % getCount()) % getCount();

      ImageView iv = new ImageView(context);
      iv.setScaleType(ScaleType.CENTER_CROP);

      ImageLoader.loadImage(iv, ((BannerList) objects[position]).getImgSrc());

      iv.setTag(R.id.appIcon, ((BannerList) objects[position]).getImgTitlle());
      iv.setTag(R.id.appSize, ((BannerList) objects[position]).getImgHref());
      iv.setOnClickListener(this.btnClickListener);

      container.addView(iv);

      return iv;
    }

    private OnClickListener btnClickListener = new OnClickListener() {
      @Override
      public void onClick(View view) {
        String htmlUrl = (String) view.getTag(R.id.appSize);

        if (TextUtils.isEmpty(htmlUrl.trim())) {
          // url为空, 点击没有效果
          return;

        } else if (htmlUrl.startsWith(WebViewActivity.PROJECT_DETAIL_INFO_URL)) {

          // 如果返回的id是空的 没有点击效果
          if (TextUtils.isEmpty(htmlUrl.substring(htmlUrl.lastIndexOf("=") + 1))) {
            return;
          }

          // 进入标的详情界面
          Intent intent = new Intent(context, ProjectIntroduceActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("mockId", htmlUrl.substring(htmlUrl.lastIndexOf("=") + 1));
          context.startActivity(intent);
        } else {// 进入活动界面
          if (FormatUtils.isHttpAddr(htmlUrl)) { //正则是否是url
            StringBuffer buffer = new StringBuffer();
            buffer.append(htmlUrl);

            if (UserInfoPrefs.isLogin()) {// 登录的情况下
              if (htmlUrl.contains("?")) {// url中包含了?
                buffer.append("&channel=").append(AppInfoUtil.getChannel());
              } else {
                buffer.append("?channel=").append(AppInfoUtil.getChannel());
              }
              buffer.append("&userId=").append(UserInfoPrefs.getUserId());
            }

            Intent intent = new Intent(context, WebViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("htmlTitle", (String) view.getTag(R.id.appIcon));
            intent.putExtra("htmlUrl", buffer.toString());
            context.startActivity(intent);
          }
        }
      }
    };
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        // 父控件不要拦截
        getParent().requestDisallowInterceptTouchEvent(true);

        downX = (int) ev.getX();
        downY = (int) ev.getY();
        viewPager.stopAutoScroll();
        break;
      case MotionEvent.ACTION_MOVE:
        int moveX = (int) ev.getX();
        int moveY = (int) ev.getY();
        // 下滑
        if (Math.abs(moveY - downY) > Math.abs(moveX - downX)) {
          getParent().requestDisallowInterceptTouchEvent(false);
        } else {
          getParent().requestDisallowInterceptTouchEvent(true);

        }
        break;
      case MotionEvent.ACTION_CANCEL:
        viewPager.startAutoScroll();
        break;
      case MotionEvent.ACTION_UP:
        viewPager.startAutoScroll();
        break;
      default:
        break;
    }
    return super.dispatchTouchEvent(ev);
  }

  @SuppressLint("NewApi")
  @Override
  protected void onFinishInflate() {
    if (!this.isInEditMode()) {
      // pic scale
      DisplayMetrics dm = new DisplayMetrics();
      ((Activity) (this.context)).getWindowManager().getDefaultDisplay().getMetrics(dm);
      int thumbnailWidth = dm.widthPixels;
      double xScale = 240d / 640.0d;
      int thumbnailHeight = (int) (thumbnailWidth * xScale);
      LinearLayout.LayoutParams bannerLP = new LinearLayout.LayoutParams(thumbnailWidth, thumbnailHeight);
      this.setLayoutParams(bannerLP);

      // 3.Viewpager
      viewPager = (AutoScrollViewPager) findViewById(R.id.picslooper);
      viewPager.setFocusable(true);

      // 2. 圆点
      pageIndex = (CircleIndicator) findViewById(R.id.pageIndexor);
      pageIndex.setDotMargin(10);
      pageIndex.setPaddingBottom(6);
    }

    super.onFinishInflate();
  }
}
