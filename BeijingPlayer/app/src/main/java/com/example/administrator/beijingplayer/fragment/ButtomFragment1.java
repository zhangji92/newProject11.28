package com.example.administrator.beijingplayer.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.beijingplayer.R;
import com.example.administrator.beijingplayer.adpter.HomeAdapter;
import com.example.administrator.beijingplayer.adpter.ViewPagerAdapter;
import com.example.administrator.beijingplayer.mode.Pictury;
import com.example.administrator.beijingplayer.mode.Shop;
import com.example.administrator.beijingplayer.mode.ShopMessage;
import com.example.administrator.beijingplayer.util.ModeCode;
import com.example.administrator.beijingplayer.util.MyLocationListener;

import java.util.ArrayList;
import java.util.List;


public class ButtomFragment1 extends FatherFragment {

    //fragment组件
    private ListView listView;
    private ViewPager viewPager;
    private TextView text1;


    //存放服务器的数据
    private List<Pictury> picList = new ArrayList<>();
    private List<ShopMessage> shopMessageList = new ArrayList<>();
    //list集合和分页的适配器
    private HomeAdapter homeAdapter;
    private ViewPagerAdapter viewPagerAdapter;

    public LocationClient mLocationClient = null;
    public MyLocationListener myListener = new MyLocationListener();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case ModeCode.HOME_WHAT:
                    getServerMessage(msg.obj);
                    break;
                case ModeCode.VIEW_PAGER:
                    imgCarousel();
                    break;
                case ModeCode.ADDRESS:
                    String res = (String) msg.obj;
                    text1.setText(res);
                    mLocationClient.stop();
                    break;
            }
        }
    };


    public ButtomFragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取服务器数据
        httpTools.get6bu6Messege(handler,"1","1",null);
        handler.sendEmptyMessageDelayed(ModeCode.VIEW_PAGER,5000);

        //地图相关
        myListener.setHandler(handler);
        mLocationClient = new LocationClient(getActivity().getApplicationContext());  //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        initLocation();
        mLocationClient.start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buttom_fragment1, container, false);
        initView(view);

        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    @Override
    protected void initView(View view){
        listView = (ListView) view.findViewById(R.id.home_list);
        homeAdapter = new HomeAdapter(getActivity(),shopMessageList);
        listView.setAdapter(homeAdapter);


        viewPager = (ViewPager) view.findViewById(R.id.fragment1_viewpager);
        viewPagerAdapter = new ViewPagerAdapter(picList,getActivity());
        viewPager.setAdapter(viewPagerAdapter);

        text1 = (TextView) view.findViewById(R.id.fragment_text1);

    }
    /***
     * 获取子线程内查询的数据
     * @param obj 发送的对象
     */
    @Override
    protected void getServerMessage(Object obj){
        if(obj!=null && obj instanceof Shop){
            Shop shop = (Shop) obj;
            //将服务器的广告信息存进list集合中
            Pictury[] pics = shop.getHomeRes().getPicList();
            for(int i=0;i<pics.length;i++){
                picList.add(pics[i]);
            }
            viewPagerAdapter.setPicList(picList);
            viewPagerAdapter.notifyDataSetChanged();
            //将服务器的商家信息存入list集合中
            shopMessageList.addAll(shop.getHomeRes().getRecommend().getHeng());
            for(int i=0;i<shop.getHomeRes().getRecommend().getShu().size();i++){
                shopMessageList.add(shop.getHomeRes().getRecommend().getShu().get(i));
            }
            homeAdapter.setList(shopMessageList);
            homeAdapter.notifyDataSetChanged();
        }
    }

    /***
     * 广告位轮播
     */
    private void imgCarousel(){
        int pager_index =viewPager.getCurrentItem()+1;
        if(pager_index ==picList.size()){
            viewPager.setCurrentItem(0);
        }else{
            viewPager.setCurrentItem(pager_index);
        }
        handler.sendEmptyMessageDelayed(ModeCode.VIEW_PAGER,5000);
    }

    /**
     * 百度地图信息
     */
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000*3600;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onClick(View v) {

    }
}
