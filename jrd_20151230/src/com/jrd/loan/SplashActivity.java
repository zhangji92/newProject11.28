package com.jrd.loan;

import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.location.LocationClientOption.LocationMode;
import com.jrd.loan.api.StatisticsApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.constant.Const;
import com.jrd.loan.constant.Const.Extra;
import com.jrd.loan.gesture.lock.GestureEditActivity;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.LogUtil;
import com.jrd.loan.util.ScreenUtil;
import com.umeng.analytics.MobclickAgent;

public class SplashActivity extends Activity {


  public LocationClient mLocationClient = null;
  public BDLocationListener myListener = new MyLocationListener();

  private FrameLayout logoLayout;
  private ImageView textImg;
  private ImageView searchImg;
  private int screenHeight;
  private int searchHeight;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.loan_splash_layout);
    mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
    mLocationClient.registerLocationListener(myListener); // 注册监听函数

    initLocation();

    mLocationClient.start();

    logoLayout = (FrameLayout) findViewById(R.id.loan_splash_logoLayout);
    searchImg = (ImageView) findViewById(R.id.loan_splash_searchImg);
    textImg = (ImageView) findViewById(R.id.loan_splash_textImg);
    searchImg.post(new Runnable() {
      @Override
      public void run() {
        screenHeight = ScreenUtil.getScreenHeight(SplashActivity.this);
        searchHeight = searchImg.getHeight();
        StartAnimator();
      }
    });

    // 用户行为统计接口
    StatisticsApi.getUserBehavior(SplashActivity.this, Const.EventName.START, Const.EventId.START, null, null, null);

  }

  /**
   * 启动动画
   */
  private void StartAnimator() {
    AnimatorSet animatorSet = new AnimatorSet();
    ObjectAnimator search = ObjectAnimator.ofFloat(searchImg, "alpha", 0f, 1f);
    search.setInterpolator(new AccelerateDecelerateInterpolator());
    search.setDuration(2500);
    ObjectAnimator logo = ObjectAnimator.ofFloat(logoLayout, "translationY", 0f, -((screenHeight - searchHeight) / 3f));
    logo.setInterpolator(new AccelerateDecelerateInterpolator());
    logo.setDuration(1500);
    logo.addListener(new AnimatorListenerAdapter() {

      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        textImg.setVisibility(View.VISIBLE);
      }

      @Override
      public void onAnimationStart(Animator animation) {
        super.onAnimationStart(animation);
        searchImg.setVisibility(View.VISIBLE);
      }
    });

    ObjectAnimator text = ObjectAnimator.ofFloat(textImg, "alpha", 0f, 1f);
    text.setDuration(1000);
    text.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        StartActivity();
      }
    });

    animatorSet.playTogether(logo, search);
    animatorSet.playSequentially(logo, text);
    animatorSet.setStartDelay(500);
    animatorSet.start();
  }

  /**
   * 启动首页
   */
  private void StartActivity() {
    if (AppInfoPrefs.isFirstStartApp(SplashActivity.this)) {// 如果是首次启动app, 进入引导界面
      Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
      finish();
    } else {
      if (AppInfoPrefs.isSetGestureLock(SplashActivity.this) && UserInfoPrefs.isLogin()) {// 如果用户登录了,
        // 并且设置了手势密码锁
        BaseActivity.setIsPresseHomeKey(true);
        BaseActivity.setLastTimes(0);
      }

      if (!AppInfoPrefs.isSetGestureLock(SplashActivity.this) && UserInfoPrefs.isLogin()) {// 如果用户登录了,
        // 并且没有设置手势密码锁
        Intent intent = new Intent(SplashActivity.this, GestureEditActivity.class);
        intent.putExtra(Extra.Select_ID, 0);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
      } else {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
      }
    }
  }

  private void initLocation() {
    LocationClientOption option = new LocationClientOption();
    option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
    option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
    int span = 1000;
    option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
    option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
    option.setOpenGps(true);// 可选，默认false,设置是否使用gps
    option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
    option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
    option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
    option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
    option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
    option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
    mLocationClient.setLocOption(option);
  }

  @Override
  public void onResume() {
    super.onResume();
    MobclickAgent.onResume(this);
  }

  @Override
  public void onPause() {
    super.onPause();
    MobclickAgent.onPause(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mLocationClient.stop();
  }

  public class MyLocationListener implements BDLocationListener {

    @Override
    public void onReceiveLocation(BDLocation location) {

      double longitude = location.getLongitude();
      double latitude = location.getLatitude();
      String province = null;
      String city = null;

      if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

        province = location.getProvince();
        city = location.getCity();

      } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果

        province = location.getProvince();
        city = location.getCity();

      } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果

        province = location.getProvince();
        city = location.getCity();

      }

      LogUtil.d("location", "省：" + province + "\n市：" + city + "\n经度：" + longitude + "\n纬度：" + latitude);

      if (province != null && city != null) {
        UserInfoPrefs.setLocationInfo(SplashActivity.this, province, city, longitude + "", latitude + "");
      }
    }
  }
}
