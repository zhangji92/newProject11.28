package com.yoka.mrskin.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.UHCity;
import com.yoka.mrskin.model.managers.UHCityManager;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager.YKGeneralCallBack;
import com.yoka.mrskin.util.GPS;
import com.yoka.mrskin.util.LocationUtils;
import com.yoka.mrskin.util.NetWorkUtil;
/**
 * 个人资料中 地区定位
 * @author zlz
 * @date 2016年6月8日
 */
public class UpdateCityActivity2 extends BaseActivity implements AMapLocationListener{
	private static final String TAG = UpdateCityActivity2.class.getSimpleName();

	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
		Manifest.permission.ACCESS_FINE_LOCATION
	};



	public static final String REQUEST_CODE = "REQUEST_CODE";
	public static final int REQUEST_CODE_APP_FIRST_IN = 0;
	public static final int REQUEST_CODE_MENU_DELETE_ALL = 1;
	public static final int REQUEST_CODE_MENU_ADD = 2;
	private static final int RESULT_CODE_CITY = 0;
	private ListView mSearchresult;
	private SearchResultAdapter mSearchResultAdapter;
	private EditText mSearchBox;
	private ArrayList<UHCity> mSearchResultData;
	private ProgressDialog mProgressDialog;
	private int mRequestCode = REQUEST_CODE_MENU_ADD;
	private TimerTask mtask;
	private boolean isUpdateAddress;

	//声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;
	//	定位参数设置
	private AMapLocationClientOption mLocationOption = null;
	private LinearLayout location_ll;
	private TextView mLocationTv;

	private AMapLocation locmap;
	UHCity resultCity = null;
	private static UHCityManager manager = null;
	private String mKeyWord = "";
	private boolean isRequesting = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_city);
		manager = new UHCityManager(UpdateCityActivity2.this);
		readCityList();
		init();
		verifyStoragePermissions(UpdateCityActivity2.this);


	}

	/**
	 * 
	 */
	private void readCityList() {
		new Thread(){

			@Override
			public void run() {

				//查询本地城市列表
				manager.setup();
				mHandler.sendEmptyMessage(201);
			}

		}.start();

	}

	/**
	 * 实例化定位对象---zlz
	 * 开启定位
	 */
	private void initLocation() {


		mLocationClient = new AMapLocationClient(this.getApplicationContext());
		mLocationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置定位监听
		mLocationClient.setLocationListener(this);

		initOption();
		mLocationClient.setLocationOption(mLocationOption);
		// 启动定位
		if(isConnected()){
			//			showProgress();
			mLocationClient.startLocation();
			mHandler.sendEmptyMessage(LocationUtils.MSG_LOCATION_START);
		}else{
			Toast.makeText(UpdateCityActivity2.this, "请连接网络", Toast.LENGTH_SHORT).show();

		}

	}
	/**
	 *  设置定位参数
	 */
	private void initOption() {
		// 设置是否需要显示地址信息
		mLocationOption.setNeedAddress(true);
		/**
		 * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
		 * 注意：只有在高精度模式下的单次定位有效，其他方式无效
		 */
		mLocationOption.setGpsFirst(false);
		// 设置是否开启缓存
		mLocationOption.setLocationCacheEnable(true);
		//			if (!TextUtils.isEmpty(strInterval)) {
		//				// 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
		mLocationOption.setInterval(10000);
		//			}

	}
	/**
	 * 判断网络链接
	 * @return 打开GPS or链接WiFi 状态
	 */
	private boolean isConnected(){
		boolean isConnecte = false;
		LocationManager locationManager = (LocationManager)
				getSystemService(Context.LOCATION_SERVICE);
		boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if(isGPS || NetWorkUtil.isNetworkAvailable(UpdateCityActivity2.this)){
			return true;
		}
		return isConnecte;
	}

	private void verifyStoragePermissions(Activity activity) {
		int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

		if (permission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(
					activity,
					PERMISSIONS_STORAGE,
					REQUEST_EXTERNAL_STORAGE
					);
		}

		if(permission == PackageManager.PERMISSION_GRANTED){

			initLocation();

		}else{
			Toast.makeText(UpdateCityActivity2.this, "请打开定位权限", Toast.LENGTH_SHORT).show();
			mLocationTv.setText("定位失败，请手动输入");

		}
	}



	Handler mHandler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			//开始定位
			case LocationUtils.MSG_LOCATION_START:
				Log.d(TAG, "result = 正在定位……");

				break;
				// 定位完成
			case LocationUtils.MSG_LOCATION_FINISH:
				AMapLocation loc = (AMapLocation) msg.obj;
				String result = LocationUtils.getLocationStr(loc);
				Log.d(TAG, "result = "+result);
				if(loc.getErrorCode() == 0){//定位成功

					if("".endsWith(loc.getDistrict()) || null == loc.getDistrict()){
						mLocationTv.setText("[当前位置] "+loc.getCity()+"-"+loc.getProvince());
					}else{
						mLocationTv.setText("[当前位置] "+loc.getDistrict()+"-"+loc.getProvince());
					}



				}else{
					//失败状态判断处理
					mLocationTv.setText("定位失败，请手动输入");
					//					Toast.makeText(UpdateCityActivity2.this, "定位失败，请手动输入", Toast.LENGTH_SHORT).show();

				}


				mLocationClient.stopLocation();
				//				hideProgress();
				break;
				//停止定位
			case LocationUtils.MSG_LOCATION_STOP:
				Log.d(TAG, "result = 停止定位……");

				break;
			case 201://加载城市列表成功
				if(null != locmap && locmap.getCity().length() > 0){
					location_ll.setEnabled(true);
					if("".equals(locmap.getDistrict()) || null == locmap.getDistrict()){

						resultCity = manager.searchCities(locmap.getCity().substring(0, locmap.getCity().length()-1)).get(0);
					}else{
						resultCity = manager.searchCities(locmap.getDistrict().substring(0, locmap.getDistrict().length()-1)).get(0);
					}
					if(isRequesting){
						updateArea(resultCity);

					}
					isRequesting = false;
				}
				if(!"".equals(mKeyWord)){
//					mSearchBox.setEnabled(true);

					mSearchResultData = manager.searchCities(
							mKeyWord);
					mSearchResultAdapter.setData(mSearchResultData);
					mSearchResultAdapter.notifyDataSetChanged();
					mKeyWord = "";
				}


				break;
			default:
				break;
			}
		};
	};

	private void init() {
		isUpdateAddress = getIntent().getBooleanExtra("isUpdateAddress", false);

		location_ll = (LinearLayout) findViewById(R.id.location_ll);
		location_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(locmap != null){

					if(null != resultCity){

						updateArea(resultCity);
					}else{
						Toast.makeText(UpdateCityActivity2.this, "修改中，请稍等……", 0).show();
						location_ll.setEnabled(false);
						isRequesting = true;
					}
				}

			}
		});
		mLocationTv = (TextView)findViewById(R.id.location_text);
		View backBtn = (View) findViewById(R.id.update_city_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});



		// search result
		mSearchresult = (ListView) findViewById(R.id.add_city_search_result);
		mSearchResultAdapter = new SearchResultAdapter(this);
		mSearchresult.setAdapter(mSearchResultAdapter);
		mSearchresult.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.d(TAG, "mSearchresult position =" + arg2);
				UHCity city = (UHCity) mSearchResultAdapter.getItem(arg2);

				citySelected(city);
			}
		});
		mSearchresult.setVisibility(View.GONE);

		// search box
		mSearchBox = (EditText) findViewById(R.id.add_city_search_box);

		mSearchBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				Log.d(TAG, "beforeTextChanged" + arg0);

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				Log.d(TAG, "afterTextChanged" + arg0.toString());

				String keyword = arg0.toString();
				if (keyword.length() > 0) {
					location_ll.setVisibility(View.GONE);
					mSearchresult.setVisibility(View.VISIBLE);
				} else {
					mSearchresult.setVisibility(View.GONE);
				}

				if(keyword.length() > 0){
					mSearchResultData = manager.searchCities(
							keyword);
					mSearchResultAdapter.setData(mSearchResultData);
					if(mSearchResultData.size() <= 0){

						mKeyWord = keyword;
						Toast.makeText(UpdateCityActivity2.this, "搜索中，请稍等……", Toast.LENGTH_SHORT).show();
//						mSearchBox.setEnabled(false);
					}
				}


				mSearchResultAdapter.notifyDataSetChanged();
			}
		});
	}

	private void citySelected(UHCity city) {
		Intent data = new Intent();
		if (city != null) {
			data.putExtra("city_id", city.getID());
			data.putExtra("city_name", city.getCityName());
			data.putExtra("province_name", city.getProvinceName());
			if (city.getID().equals("-1")) {

				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				// 接受软键盘输入的编辑文本或其它视图
				inputMethodManager.showSoftInput(mSearchBox,
						InputMethodManager.SHOW_FORCED);

				if (GPS.openGPSSettings(this)) {
					startGPS();
				}

			} else {
				// 请求代码可以自己设置，这里设置成20
				if (!isUpdateAddress) {
					updateArea(city);
				}
				setResult(RESULT_CODE_CITY, data);
				finish();
			}
		}

	}

	private void updateArea(final UHCity city) {
		YKUpdateUserInfoManager.getInstance().requestUpdateAddress(city,
				new YKGeneralCallBack() {

			@Override
			public void callback(YKResult result,String imageUrl) {
				if (result.isSucceeded()) {
					Toast.makeText(UpdateCityActivity2.this, "修改成功",
							Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					data.putExtra("city_id", city.getID());
					data.putExtra("city_name", city.getCityName());
					data.putExtra("province_name", city.getProvinceName());
					setResult(RESULT_CODE_CITY, data);
					finish();
				}
			}
		});
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (GPS.openGPSSettings(this)) {
			startGPS();
		}
	}

	private void startGPS() {
		mtask = new TimerTask() {
			public void run() {
				Log.d(TAG, "time dotimeout");
				Message message = new Message();
				message.what = 1;
				mHandler.sendMessage(message);
			}
		};

		Timer timer = new Timer(true);
		timer.schedule(mtask, 10000, 10000);
		//		showProgress();
		//        GPS.getLocation(UpdateCityActivity.this, this);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	static class RecommendationCityAdapter extends BaseAdapter
	{
		private Context mContext;
		private LayoutInflater mInflater;

		public RecommendationCityAdapter(Context context)
		{
			mContext = context;
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		private ArrayList<UHCity> mDatas;

		@Override
		public int getCount() {
			Log.d(TAG, "getcount " + mDatas.size());
			return mDatas.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mDatas.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.city_gridlayout_item,
						null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView
						.findViewById(R.id.tv_cityname);
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.img_checked);
				holder.locationImageView = (ImageView) convertView
						.findViewById(R.id.add_city_location);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (pos == 0) {
				holder.locationImageView.setVisibility(View.VISIBLE);
			} else {
				holder.locationImageView.setVisibility(View.INVISIBLE);
			}


			holder.textView.setText(mDatas.get(pos).getCityName());
			if (manager.hasCity(mDatas.get(pos))) {
				holder.imageView.setVisibility(View.VISIBLE);
				// holder.imageView.setImageResource(R.drawable.checked);
			} else {
				holder.imageView.setVisibility(View.GONE);
			}
			return convertView;
		}

		public void setData(ArrayList<UHCity> data) {
			mDatas = data;
		}

		class ViewHolder
		{
			public TextView textView;
			public ImageView imageView;
			public ImageView locationImageView;
		}
	}

	static class SearchResultAdapter extends BaseAdapter
	{
		private Context mContext;
		private ArrayList<UHCity> mDatas;

		public SearchResultAdapter(Context context)
		{
			mContext = context;
		}

		@Override
		public int getCount() {
			if (mDatas != null) {
				return mDatas.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			if (mDatas != null) {
				return mDatas.get(arg0);
			}
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			TextView buttonView;
			if (convertView == null) {
				buttonView = new TextView(mContext);
				buttonView.setTextColor(0xFF999999);
				buttonView.setTextSize(18);
				buttonView.setLayoutParams(new GridView.LayoutParams(
						LayoutParams.MATCH_PARENT, 100));// 设置ImageView对象布局
				buttonView.setPadding(48, 8, 8, 8);// 设置间距
			} else {
				buttonView = (TextView) convertView;
			}
			buttonView.setText(mDatas.get(arg0).getCityName() + " - "
					+ mDatas.get(arg0).getProvinceName());
			return buttonView;
		}

		public void setData(ArrayList<UHCity> data) {
			mDatas = data;
		}
	}


	/**
	 * 加载进度
	 */
	private void showProgress() {
		mProgressDialog = ProgressDialog.show(this, "提示", "正在获取地理位置信息...",
				true, false);
		mProgressDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getRepeatCount() == 0) {

					hideProgress();
				}
				return false;
			}
		});
	}

	private void hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
	/**
	 * 定位方法
	 */
	@Override
	public void onLocationChanged(AMapLocation loc) {
		if (null != loc) {
			locmap = loc;

			Message msg = mHandler.obtainMessage();
			msg.obj = loc;
			msg.what = LocationUtils.MSG_LOCATION_FINISH;
			mHandler.sendMessage(msg);
		}


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != mLocationClient) {
			/**
			 * 如果AMapLocationClient是在当前Activity实例化的，
			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
			 */
			mLocationClient.onDestroy();
			mLocationClient = null;
			mLocationOption = null;
		}

	}


}
