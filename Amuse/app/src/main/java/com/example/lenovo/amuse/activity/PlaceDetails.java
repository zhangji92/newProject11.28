package com.example.lenovo.amuse.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.amuse.MyApplication;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.fragment.PlaceAssessFragment;
import com.example.lenovo.amuse.fragment.PlaceDetailsContentFragment;
import com.example.lenovo.amuse.fragment.PlaceDetailsSynopsisFragment;
import com.example.lenovo.amuse.mode.ShopMode;
import com.example.lenovo.amuse.util.BaseUri;
import com.example.lenovo.amuse.util.HttpTools;
import com.example.lenovo.amuse.util.MyViewPager;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/19.
 * 商家详情
 */

public class PlaceDetails extends FragmentActivity {

    TextView textView_toolBar;
    TextView textView_title;
    TextView textView_money;
    TextView textView_address;
    TextView textView_contact;
    TextView textView_contact1;
    //下划线
    TextView textView_line1;
    TextView textView_line2;
    TextView textView_line3;
    ImageView imageView_img;
    //
    FinalBitmap mFinalBitmap;

    //轮播控件
    private MyViewPager mViewPager;
    private List<Fragment> mList;
    //接受过来的数据
    public ShopMode shopMode;
    PlaceDetailsSynopsisFragment mPlaceDetailsSynopsisFragment;
    PlaceDetailsContentFragment mPlaceDetailsContentFragment;
    PlaceAssessFragment mPlaceAssessFragment;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseUri.SHOP_DETAILS_CODE:
                    shopMode = parseMode(msg.obj);
                    if (shopMode != null) {
                        mFinalBitmap.display(imageView_img, BaseUri.BASE + shopMode.getResultCode().getPic());
                        textView_toolBar.setText(shopMode.getResultCode().getShopname());
                        textView_title.setText(shopMode.getResultCode().getShopname());
                        textView_money.setText("价格:" + shopMode.getResultCode().getMinconsume());
                        textView_address.setText("地址:" + shopMode.getResultCode().getAddress());
                        textView_contact.setText("联系方式:" + shopMode.getResultCode().getTel());
                        textView_contact1.setText("联系方式:" + shopMode.getResultCode().getPhone());
                        //初始化数据
                        initData();
                    }
                    break;
            }
        }
    };

    private void initData() {
        //创建PlaceDetailsFragment实例
        mPlaceDetailsSynopsisFragment = new PlaceDetailsSynopsisFragment();
        mPlaceDetailsSynopsisFragment.setGetInstanceHeight(new PlaceDetailsSynopsisFragment.getInstanceHeight() {
            @Override
            public void getHeight(int height) {
                mViewPager.calculate(1,height);
            }
        });
        mPlaceDetailsContentFragment = new PlaceDetailsContentFragment();
//        mPlaceDetailsContentFragment.setGetInstanceHeight(new PlaceDetailsContentFragment.getInstanceHeight() {
//            @Override
//            public void getHeight(int height) {
//                mViewPager.calculate(2,height);
//            }
//        });
        mPlaceAssessFragment=new PlaceAssessFragment();
        mList = new ArrayList<>();
        mList.add(mPlaceDetailsSynopsisFragment);
        mList.add(mPlaceDetailsContentFragment);
        mList.add(mPlaceAssessFragment);
        //设置数据
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mList.get(position);
            }
            @Override
            public int getCount() {
                return mList.size();
            }
        });
        //第二条数据
        selectLine(1);
        //显示第二页
        mViewPager.setCurrentItem(1);

        //设置mViewPager监听器
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                //获取当前mViewPager
                int i = mViewPager.getCurrentItem();
                //设置mViewPager的高度
                mViewPager.resetHeight(position);
                //修改下划线颜色
                selectLine(i);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //设置mViewPager的高度
        mViewPager.resetHeight(0);


    }

    //解析数据
    private ShopMode parseMode(Object obj) {
        ShopMode successMode = null;
        if (obj != null && obj instanceof ShopMode) {
            successMode = (ShopMode) obj;
        }
        return successMode;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_details);
        //把当前的商家ID赋值给MyApplication
        String shopId=getIntent().getStringExtra("shopId");
        //String shopId=((MyApplication)getApplication()).getShopId();
        //请求数据
        HttpTools.getInstance().getPlace(mHandler, null, null, null, null, null, null, shopId, 4);
        //标题栏
        textView_toolBar = (TextView) findViewById(R.id.tool_title);
        //主题
        textView_title = (TextView) findViewById(R.id.place_details_title);
        //图片
        imageView_img = (ImageView) findViewById(R.id.place_details_img);
        //价格
        textView_money = (TextView) findViewById(R.id.place_details_money);
        //地址
        textView_address = (TextView) findViewById(R.id.place_details_address);
        //联系方式1-->电话
        textView_contact = (TextView) findViewById(R.id.place_details_contact);
        //联系方式2-->手机
        textView_contact1 = (TextView) findViewById(R.id.place_details_contact1);
        //下划线
        textView_line1 = (TextView) findViewById(R.id.place_details_line1);
        textView_line2 = (TextView) findViewById(R.id.place_details_line2);
        textView_line3 = (TextView) findViewById(R.id.place_details_line3);
        //初始化控件
        initView();
        //初始化数据
//        initData();
        //下划线显示第二页
    }

//    private void initData() {
//        for (String title : mTitle) {
//            Bundle bundle = new Bundle();
//            bundle.putString(PlaceDetailsSynopsisFragment.TITLE, title);
//            placeDetailsFragment.setArguments(bundle);
//        }
//
//        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return mList.get(position);
//            }
//            @Override
//            public int getCount() {
//                return mList.size();
//            }
//        };
//    }

    private void initView() {
        //获取控件
        mViewPager = (MyViewPager) findViewById(R.id.place_details_viewPager);
        //初始化显示图片
        mFinalBitmap = FinalBitmap.create(this);
    }

    private void selectLine(int i) {
        if (i == 0) {
            textView_line1.setBackgroundColor(Color.RED);
            textView_line2.setBackgroundColor(Color.WHITE);
            textView_line3.setBackgroundColor(Color.WHITE);
        } else if (i == 1) {
            textView_line1.setBackgroundColor(Color.WHITE);
            textView_line2.setBackgroundColor(Color.RED);
            textView_line3.setBackgroundColor(Color.WHITE);
        } else if (i == 2) {
            textView_line1.setBackgroundColor(Color.WHITE);
            textView_line2.setBackgroundColor(Color.WHITE);
            textView_line3.setBackgroundColor(Color.RED);
        }
    }
}
