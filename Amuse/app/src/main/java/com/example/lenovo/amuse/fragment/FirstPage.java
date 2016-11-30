package com.example.lenovo.amuse.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.activity.PlaceDetails;
import com.example.lenovo.amuse.adapter.FirstAdapter;
import com.example.lenovo.amuse.adapter.ViewPageAdapter;
import com.example.lenovo.amuse.mode.FirstPageMode;
import com.example.lenovo.amuse.mode.FirstPageMode.ResultCodeBean.RecommendBean.HengBean;
import com.example.lenovo.amuse.util.BaseUri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/9/22.
 * 首页
 */

public class FirstPage extends BaseFragment {
    //
    private ListView listView;
    TextView textView_location;

    //list数据
    private List<HengBean> list = new ArrayList<>();
    //适配器
    private FirstAdapter firstAdapter;

    //广告相关
    private List<FirstPageMode.ResultCodeBean.AdBean> adBeanList;
    private List<View> viewList = new ArrayList<>();
    //轮播的空间
    private ViewPager viewPager;
    //适配器
    private ViewPageAdapter viewPageAdapter;
    //实例类
    FirstPageMode mFirstPageMode;
    //定位相关
    public LocationClient mLocationClient = null;
    public MyLocationListener myLocationListener;
    //定位地址
    String locationData;
    //精度
    int lat = 1;
    //维度
    int lng = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseUri.FIRSTCODE:
                    mFirstPageMode = getMode(msg.obj);
                    //遍历firstPageMode.getResultCode().getRecommend().getHeng().size()
                    for (int i = 0; i < mFirstPageMode.getResultCode().getRecommend().getHeng().size(); i++) {
                        //把数据添加到list中
                        list.addAll(mFirstPageMode.getResultCode().getRecommend().getHeng());
                    }
                    //判断firstPageMode是否为空
                    if (mFirstPageMode != null) {
                        //初始化广告list
                        adBeanList = mFirstPageMode.getResultCode().getAd();
                        //遍历
                        for (int i = 0; i < adBeanList.size(); i++) {
                            //创建图片控件
                            ImageView imageView = new ImageView(getActivity());
                            //设置图片属性
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            //把图片添加到视图集合中
                            viewList.add(imageView);
                        }
                        viewPageAdapter.setAdList(adBeanList);
                        viewPageAdapter.setViewList(viewList);
                        viewPager.setAdapter(viewPageAdapter);
                        //更新数据
                        viewPageAdapter.notifyDataSetChanged();
                        //当视图集合不为空和视图集合长度大于1时轮播开始
                        if (viewList != null && viewList.size() > 1) {
                            mHandler.sendEmptyMessageDelayed(BaseUri.FIRST_CODE_CAROUSE, 2000);
                        }
                    }
                    //跟新数据
                    firstAdapter.notifyDataSetChanged();
                    break;
                case BaseUri.FIRST_CODE_CAROUSE:
                    //轮播
                    initCarousel();
                    viewPageAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    /**
     * 广告位 图片轮播
     */
    public void initCarousel() {
        //当前的图片页数
        int item = viewPager.getCurrentItem();
        //当前页数等于最后一页时
        if (item == viewPageAdapter.getAdList().size() - 1) {
            //当前页数为0
            viewPager.setCurrentItem(0);
        } else {
            //当前页数+1
            viewPager.setCurrentItem(item + 1);
        }
        mHandler.sendEmptyMessageDelayed(BaseUri.FIRST_CODE_CAROUSE, 2000);
    }
    //解析数据
    public FirstPageMode getMode(Object obj) {
        FirstPageMode mode = null;
        if (obj != null && obj instanceof FirstPageMode) {
            mode = (FirstPageMode) obj;
        }
        return mode;
    }


    /**
     * 每次创建（Fragment） 都会绘制Fragemnt 的View 组件时回调该方法
     *
     * @param inflater           加载布局文件
     * @param container          加载Layout 布局的 父（ViewGroup）
     * @param savedInstanceState 是否返回父 ViewGroup  false 为不返回
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.first_page, container, false);

        //定位监听器
        myLocationListener = new MyLocationListener();
        mLocationClient = new LocationClient(getActivity().getApplicationContext());     //声明LocationClient类
        //初始化定位数据
        initLocation();
        mLocationClient.registerLocationListener(myLocationListener);    //注册监听函数
        //定位开始
        mLocationClient.start();
        //网络连接
        httpTools.getDate(mHandler, String.valueOf(lat), String.valueOf(lng), null, null, null, null, null, 1);
        //list控件
        listView = (ListView) view.findViewById(R.id.id_first_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    getActivity().startActivity(new Intent(getActivity(), PlaceDetails.class));
                }
            }
        });

        textView_location = (TextView) view.findViewById(R.id.tool_location);

        //list适配器
        firstAdapter = new FirstAdapter(list, getActivity());
        listView.setAdapter(firstAdapter);
        //轮播控件
        viewPager = new ViewPager(getActivity());
//        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        //适配器实例化
        viewPageAdapter = new ViewPageAdapter(getActivity());
        //设定布局的宽度和高度
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 260);
        //把布局的高度和宽度设置给viewPager
        viewPager.setLayoutParams(layoutParams);
        listView.addHeaderView(viewPager);
        return view;
    }





    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            locationData=location.getAddrStr();
            textView_location.setText(locationData);
            Log.i("BaiduLocationApiDem", sb.toString());
        }
    }
}

