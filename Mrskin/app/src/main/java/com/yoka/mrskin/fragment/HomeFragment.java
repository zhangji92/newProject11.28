package com.yoka.mrskin.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adsame.main.AdListener;
import com.adsame.main.AdsameBannerAd;
import com.adsame.main.AdsameManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.YKWebViewActivity;
import com.yoka.mrskin.adapter.HomeAdapter;
import com.yoka.mrskin.main.AppContext;
import com.yoka.mrskin.main.MainActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.HomeData;
import com.yoka.mrskin.model.data.YKFocusImage;
import com.yoka.mrskin.model.data.YKNewExperience;
import com.yoka.mrskin.model.data.YKRecommendation;
import com.yoka.mrskin.model.data.YKTopicData;
import com.yoka.mrskin.model.data.YKWebentry;
import com.yoka.mrskin.model.managers.YKAdRecordManager;
import com.yoka.mrskin.model.managers.YKAfternoonTeaManager;
import com.yoka.mrskin.model.managers.YKAfternoonTeaManager.YKAfternoonTeaCallback;
import com.yoka.mrskin.model.managers.YKNewFocusImageCallback;
import com.yoka.mrskin.model.managers.YKNewFocusImageManagers;
import com.yoka.mrskin.model.managers.YKRecommendationManager;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.model.managers.task.YKTaskManager.HomeCardData;
import com.yoka.mrskin.util.AlarmHelper;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.viewpager.InfinitePagerAdapter;
import com.yoka.mrskin.viewpager.InfiniteViewPager;
/**
 * 首页
 * @author zlz
 * @date 2016年6月15日
 */
@SuppressLint("InflateParams")
public class HomeFragment extends Fragment implements IXListViewListener,AdListener
{
	private static final int REQUEST_CODE = 13;
	private static final int RESULT_CODE = 8;
	private View mRootView;
	private XListView mList;

	private DisplayMetrics mDisplayMetrics;
	private int mLastPosition;
	/*viewpager 下标点*/
	private LinearLayout mLine;
	private HomeAdapter homeAdapter;
	private ImageView mLineImages;
	public InfiniteViewPager mPager;

	/*点击回滚listview 头部*/ 
	private CustomButterfly mCustomButterfly = null;
	private static long mLastClickTime = -1;
	private ArrayList<YKFocusImage> mFocusImages = new ArrayList<YKFocusImage>();
	private ArrayList<YKFocusImage> mAdFocusImages = new ArrayList<YKFocusImage>();
	private ArrayList<YKFocusImage> mFinalFocusImages = new ArrayList<YKFocusImage>();
	private ArrayList<YKRecommendation> mListRecommendation = new ArrayList<YKRecommendation>();
	private List<HomeData> homeList = new ArrayList<HomeData>();

	private LinearLayout mHeadView;
	private static Context mContext;
	/*viewpager 适配器*/
	private AdsAdapter mAdsAdapter;

	/* 显示美丽下午茶 */
	private RecyclerView mRecyclerView;
	private ArrayList<YKWebentry> mWebentries = new ArrayList<>();
	private AfternoonAdapter mAfternoonAdapter;
	private String[] clikUrl;
	private String[] showUrl;
	private  DisplayImageOptions     options = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true)
			.resetViewBeforeLoading(true).considerExifParams(true)
			.build();
	private String mSplit = "<@y>";
	private boolean isFocusSucceed;//资讯焦点图是否请求成功
	private Map<String, Integer> mFocusAdCount = new HashMap<>();

	private int datePosition ;
	private boolean hasCache;
	private ArrayList<AdsameBannerAd> bannerAds = new ArrayList<>();
	private String[] mAdIds = {"150","151"};
	private int itemSize;
	private int time = 0;
	/*焦点图广告Id*/
	private String[] mAdStrings = {"103","104","105","106"};
	private String publishId = "34";
	private int times = 0;
	private ArrayList<String> failedPosition = new ArrayList<>();
	private int windowWidth;

	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 201://加载焦点图广告完成
				addNewsToFinalList();
				break;
			case 202://加载列表广告完成
				break;
			default:
				break;
			}
		};
	};

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		AdsameManager.setPublishID(publishId);
		WindowManager wm = (WindowManager) getContext()
				.getSystemService(Context.WINDOW_SERVICE);

		windowWidth= wm.getDefaultDisplay().getWidth();
		mRootView = inflater.inflate(R.layout.home_fragment_two, container,
				false);
		return mRootView;
	}

	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		init(mRootView);
		updateTopicDataUI();
		updatePlanDataToUI();
		upDateAternoonDataUI();
		upDatePargerDataUI();


		getDataTopic(true);
		getDataView();
		getAfternoonData();
	}


	private void updatePlanDataToUI() {
		ArrayList<HomeCardData> data = YKTaskManager.getInstnace()
				.getHomeCardData();
		AlarmHelper.getInstance().openAlarm(data);
	}

	private void updateTopicDataUI() {
		YKRecommendation topic = YKRecommendationManager.getInstance()
				.getTopicData();
		if(null == topic){
			return;
		}
		//判断列表是否有缓存
		if(topic.getmFlag()!= null){
			hasCache = true;
		}else{
			hasCache = false;
		}
		homeAdapter.update(reSetData(topic), true);
		mList.setPullLoadEnable(true);
	}

	private void upDateAternoonDataUI(){
		ArrayList<YKWebentry> after = YKAfternoonTeaManager.getInstnace().getAfternoonData();
		if(after != null){
			mAfternoonAdapter.update(after);
		}
	}

	private void upDatePargerDataUI(){
		ArrayList<YKFocusImage> focus = YKNewFocusImageManagers.getInstance().getFocusImageData();
		if(focus != null){
			for (YKFocusImage ykFocusImage : focus) {
				ykFocusImage.setType(2);
			}
			initViewPager(focus);
		}

	}

	private void init(View rootView) {
		mContext = getActivity();
		mDisplayMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
		.getMetrics(mDisplayMetrics);

		mHeadView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
				R.layout.home_fragment_twoheard, null);
		mLine = (LinearLayout) mHeadView.findViewById(R.id.point_group);
		// mRecyclerView 显示美丽下午茶
		mRecyclerView = (RecyclerView) mHeadView.findViewById(R.id.hScrollView);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
				getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecyclerView.setLayoutManager(linearLayoutManager);
		mAfternoonAdapter = new AfternoonAdapter();
		mRecyclerView.setAdapter(mAfternoonAdapter);

		//
		mPager = (InfiniteViewPager) mHeadView.findViewById(R.id.viewpager);
		mList = (XListView) rootView.findViewById(R.id.home_fragment_listview);
		mPager.setNestedpParent((ViewGroup) mPager.getParent());
		//添加viewpager and 美丽下午茶  to mHeadView
		mList.addHeaderView(mHeadView);

		mList.setPullRefreshEnable(true);
		mList.setPullLoadEnable(false);
		homeAdapter = new HomeAdapter(homeList,getActivity());

		//适配listview 
		mList.setAdapter(homeAdapter);
		mList.setXListViewListener(this);
		/*	mList.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case SCROLL_STATE_IDLE:
					Glide.with(view.getContext()).resumeRequests();
					break;
				case SCROLL_STATE_TOUCH_SCROLL:
				case SCROLL_STATE_FLING:
					Glide.with(view.getContext()).pauseRequests();
					break;}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}


		});*/

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
			MainActivity.isHome = false;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void getDataTopic(boolean isRefresh) {
		if (isRefresh) {
			try {
				mCustomButterfly = CustomButterfly.show(getActivity());
			} catch (Exception e) {
				mCustomButterfly = null;
			}
		}
		YKRecommendationManager.getInstance().postYKTopicData("0",
				new YKRecommendationManager.Callback() {

			@Override
			public void callback(YKResult result,
					YKRecommendation topicData) {
				mList.stopLoadMore();
				mList.stopRefresh();
				AppUtil.dismissDialogSafe(mCustomButterfly);
				if (result.isSucceeded()) {
					if (topicData != null) {
						mListRecommendation.clear();
						mListRecommendation.add(topicData);
						homeList = reSetData(topicData);

					}
					mList.setPullLoadEnable(true);
					Log.d("vote3", homeList.toString());
					homeAdapter.update(homeList, true);;
				} else {
					if (AppUtil.isNetworkAvailable(mContext)) {
						Toast.makeText(getActivity(),
								R.string.intent_error,
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(),
								R.string.intent_no, Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		});
	}
	/**
	 * 请求焦点图
	 */
	public void getDataView() {
		mFinalFocusImages.clear();
		YKNewFocusImageManagers.getInstance().postYKFocusImage(
				new YKNewFocusImageCallback() {

					@Override
					public void callback(YKResult result,
							ArrayList<YKFocusImage> focusImage) {
						mList.stopLoadMore();
						mList.stopRefresh();
						if (result.isSucceeded()) {
							ArrayList<YKFocusImage> focusImage_type = new ArrayList<>();
							if (focusImage != null) {
								for (YKFocusImage ykFocusImage : focusImage) {
									ykFocusImage.setType(2);
									focusImage_type.add(ykFocusImage);
								}
								mFocusImages.clear();
								mFocusImages = focusImage_type;
							} else {
								mFocusImages = focusImage_type;
							}
							isFocusSucceed = true;
						} else {
							isFocusSucceed = false;

						}
						//			广告焦点图
						requestAdData();
					}
				});
	}
	/**
	 * 请求列表广告(焦点图)
	 */
	private AdsameBannerAd mFocusBannerAd;
	private void requestAdData() {
		Log.e("width", windowWidth+"-"+(5/8) * windowWidth);
		mFocusBannerAd = new AdsameBannerAd(getActivity(),mAdStrings[times],windowWidth, (windowWidth/8)*5);
		mFocusBannerAd.setAdListener(this);

	}

	@Override
	public void onRefresh() {
		times = 0;//控制焦点图
		failedPosition.clear();
		//列表广告顺序
		itemSize = 0;
		hasCache = false;

		if (null != mRootView) {
			getDataView();
		} else {
			getDataView();
		}
		getDataTopic(false);
		getAfternoonData();
	}

	@Override
	public void onLoadMore() {
		if (!AppUtil.isNetworkAvailable(getActivity())) {
			Toast.makeText(getActivity(), R.string.intent_no, Toast.LENGTH_SHORT).show();
			mList.stopLoadMore();
			return;
		}
		String index = null;
		if (mListRecommendation != null && mListRecommendation.size() > 0) {
			int sizeRem = mListRecommendation.size() - 1;
			index = mListRecommendation.get(sizeRem).getmPage();
		}
		YKRecommendationManager.getInstance().postYKNewLoadMore(
				new YKRecommendationManager.Callback() {
					@Override
					public void callback(YKResult result,
							YKRecommendation topicData) {
						mList.stopLoadMore();
						if (result.isSucceeded()) {
							if (topicData != null) {
								mListRecommendation.add(topicData);
								homeList = reSetData(topicData);
								Log.d("vote3", homeList.toString());
								homeAdapter.update(homeList, false);
							} else {
								Toast.makeText(getActivity(),
										getString(R.string.task_no_task),
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(getActivity(),
									getString(R.string.intent_error),
									Toast.LENGTH_LONG).show();
						}
					}
				}, index);
	}

	@SuppressWarnings("deprecation")
	private void initViewPager(final ArrayList<YKFocusImage> focusImgs) {
		mLine.removeAllViews();
		mLastPosition = 0;
		if(focusImgs.size() <= 0){
			return;
		}

		for (int i = 0; i < focusImgs.size(); i++) {
			mLineImages = new ImageView(AppContext.getInstance());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(10, 0, 10, 10);
			mLineImages.setLayoutParams(params);
			mLineImages.setBackgroundResource(R.drawable.point_selector);
			if (i == 0) {
				mLineImages.setEnabled(true);
			} else {
				mLineImages.setEnabled(false);
			}
			mLine.addView(mLineImages);
		}

		mPager.pageSize = focusImgs.size();
		mAdsAdapter = new AdsAdapter(focusImgs);

		mPager.setAdapter(new InfinitePagerAdapter(mAdsAdapter));
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				try {
					int position = arg0 % focusImgs.size();
					mLine.getChildAt(position).setEnabled(true);
					mLine.getChildAt(mLastPosition).setEnabled(false);
					mLastPosition = position;
				} catch (Exception e) {
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}


	/**
	 * 广告 viewpager 适配器
	 */
	private class AdsAdapter extends PagerAdapter
	{
		private ArrayList<YKFocusImage> focusImages;

		private AdsAdapter(ArrayList<YKFocusImage> focusImages)
		{
			this.focusImages = focusImages;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			final YKFocusImage focusImage = focusImages.get(position);
			//TODO: type == 1 or type == 2
			View view = null;
			//			Toast.makeText(getActivity(), ""+focusImage.getType(), Toast.LENGTH_SHORT).show();

			switch (focusImage.getType()) {
			case 1://广告
				view = initAdItem(focusImage);
				break;
			case 2://资讯
				view = initNewsItem(focusImage);
				break;

			default:
				view = new View(getActivity()) ;
				break;
			}

			container.addView(view);

			//            TrackManager.getInstance().addTrack(
			//                    TrackUrl.FOUCS_IMAGE_SHOW + focusImage.getID()
			//                            + "&position=" + (position + 1));
			return view;
		}

		private View initAdItem(YKFocusImage focusImage) {
			AdsameBannerAd banner = focusImage.getBanner();
			return banner;
		}

		/**
		 * 添加资讯焦点图
		 * @param focusImage
		 */
		private View initNewsItem(final YKFocusImage focusImage) {
			ImageView image = new ImageView(AppContext.getInstance());
			WindowManager wm = (WindowManager) AppContext.getInstance()
					.getSystemService(Context.WINDOW_SERVICE);
			int screenWidth = 0;
			int imageWidth = 0;
			int imageHeight = 0;
			if (focusImage != null) {
				if (focusImage.getmImage() != null) {
					imageWidth = focusImage.getmImage().getMwidth();
					imageHeight = focusImage.getmImage().getMheight();
				}
			}
			screenWidth = wm.getDefaultDisplay().getWidth();
			if (imageWidth == 0) {
				imageWidth = 200;
			} else if (imageHeight == 0) {
				imageHeight = 200;
			}
			int tmpHeight = screenWidth * imageHeight / imageWidth;
			image.setLayoutParams(new LayoutParams(screenWidth, tmpHeight));
			image.setScaleType(ScaleType.CENTER_CROP);
			if (focusImage != null) {
				if (focusImage.getmImage() != null) {

					//Glide.with(MrSkinApplication.getApplication()).load(focusImage.getmImage().getmURL()).into(image);
					ImageLoader.getInstance().displayImage(focusImage.getmImage().getmURL(), image, options);
				} else {
					image.setBackgroundResource(R.drawable.ic_launcher);
				}
			}

			if (focusImage.ismIsPro() && MainActivity.mIsHomeShow) {
				String clickurlString = focusImage.getmClickUrl();
				if (!TextUtils.isEmpty(clickurlString)) {
					clikUrl = null;
					clikUrl = clickurlString.split(mSplit);
				}
				String showUrlString = focusImage.getmShowUrl();
				if (!TextUtils.isEmpty(showUrlString)) {
					showUrl = null;
					showUrl = showUrlString.split(mSplit);
				}
				int n=0;
				if(mFocusAdCount.get(focusImage.getmId())!=null)n=mFocusAdCount.get(focusImage.getmId());
				if(focusImage.getmBgCount()>n||focusImage.getmBgCount()==0){

					YKAdRecordManager.getInstance().requestAdRecord(mContext,
							YKAdRecordManager.ListInterFaceId, focusImage.getmId(),
							focusImage.getmShowUrl(), YKAdRecordManager.ListAdShow);
					if (showUrl != null && showUrl.length != 0) {
						for (int j = 0; j < showUrl.length; j++) {
							YKAdRecordManager.getInstance()
							.requestAdUrl(showUrl[j]);
						}
					}
					++n;

					if(mFocusAdCount.get(focusImage.getmId())!=null)mFocusAdCount.put(focusImage.getmId(), n);
				}

			}


			//曝光上限
			image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					long currentTime = System.currentTimeMillis();
					Intent intent = new Intent(getActivity(),YKWebViewActivity.class);
					if (currentTime - mLastClickTime > 2000) {
						if (focusImage.ismIsPro()) {
							YKAdRecordManager.getInstance().requestAdRecord(
									mContext,
									YKAdRecordManager.ListInterFaceId,
									focusImage.getmId(),
									focusImage.getmClickUrl(),
									YKAdRecordManager.ListAdClick);
							if (clikUrl != null && clikUrl.length != 0) {
								for (int i = 0; i < clikUrl.length; i++) {
									if(clikUrl.length <= 8){
										YKAdRecordManager.getInstance()
										.requestAdUrl(clikUrl[i]);
									}
								}
							}
							intent.putExtra("isFocusAd", true);
							intent.putExtra("focusImage", focusImage);
						}
						mLastClickTime = currentTime;

						intent.putExtra("url", focusImage.getmUrl());
						intent.putExtra("identification", "index_banner");
						intent.putExtra("noShareBut", true);
						intent.putExtra("istrial_product", true);
						intent.putExtra("isHomeTop", true);
						startActivityForResult(intent, REQUEST_CODE);
						//                        TrackManager.getInstance().addTrack(
						//                                TrackUrl.FOUCS_IMAGE_CLICK + focusImage.getID()
						//                                        + "&position=" + (position + 1));
					}
				}
			});
			
			return image;
		}

		@Override
		public int getCount() {
			return focusImages != null ? focusImages.size() : 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

	private void getAfternoonData() {
		YKAfternoonTeaManager.getInstnace().requestData(
				new YKAfternoonTeaCallback() {

					@Override
					public void callback(YKResult result,
							ArrayList<YKWebentry> webentries) {
						if (webentries != null) {

							mAfternoonAdapter.update(webentries);
						} else {

						}
					}
				});
	}

	private class AfternoonAdapter extends
	RecyclerView.Adapter<AfternoonViewHolder>
	{

		private LayoutInflater mInflater;

		@Override
		public int getItemCount() {
			if (mWebentries == null) {
				return 0;
			}
			return mWebentries.size();
		}
		private void update(ArrayList<YKWebentry> list){
			mWebentries.clear();
			mWebentries.addAll(list);
			notifyDataSetChanged();

		}

		@Override
		public AfternoonViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
			@SuppressWarnings("static-access")
			View view = mInflater.from(getActivity()).inflate(
					R.layout.afternoon_tea_item_layout, arg0, false);
			AfternoonViewHolder holder = new AfternoonViewHolder(view);
			holder.view = view.findViewById(R.id.item_view);
			holder.title = (TextView) view
					.findViewById(R.id.afternoon_tea_title);
			holder.content = (TextView) view
					.findViewById(R.id.afternoon_tea_subTitle);
			holder.image = (ImageView) view
					.findViewById(R.id.afternoon_tea_image);
			return holder;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public void onBindViewHolder(final AfternoonViewHolder holder,final int position) {
			final YKWebentry entry = mWebentries.get(position);
			if (!TextUtils.isEmpty(entry.getmTitle())) {
				holder.title.setText(entry.getmTitle());
			}
			if (!TextUtils.isEmpty(entry.getmSubTitle())) {
				holder.content.setText(entry.getmSubTitle());
			}
			holder.image.setScaleType(ScaleType.CENTER_CROP);
			/*	Glide.with(MrSkinApplication.getApplication()).load(entry.getmCover().getmURL())
			.into(holder.image);*/
			ImageLoader.getInstance().displayImage(entry.getmCover().getmURL(), holder.image, options);

			holder.view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MainActivity fragment = ((MainActivity) getActivity());
					String type = entry.getmActionType();
					if(!TextUtils.isEmpty(type)){
						if(type.equals("0")){
							if (!TextUtils.isEmpty(entry.getmUrl())) {
								Intent intent = new Intent(getActivity(),
										YKWebViewActivity.class);
								intent.putExtra("afternoon_url", entry.getmUrl());
								intent.putExtra("isHomeTop", true);
								intent.putExtra("identification", "index_subbanner");   
								intent.putExtra("ykwebentry", entry);
								startActivityForResult(intent, REQUEST_CODE);
							}
						}else if(type.equals("1")){
							fragment.showMeSelectFragment();
						}else if(type.equals("2")){
							fragment.showExperienceFragment();
						}else if(type.equals("3")){
							fragment.showProductFragment();
						}else if(type.equals("4")){
							fragment.showProbationFragment();
						}
					}
				}
			});
		}
	}

	private class AfternoonViewHolder extends RecyclerView.ViewHolder
	{
		public AfternoonViewHolder(View arg0)
		{
			super(arg0);
		}

		TextView title, content;
		ImageView image;
		View view;
	}

	/**
	 * 友盟统计需要的俩个方法
	 */
	public void onResume() {
		super.onResume();
		if (mList == null) {
			getDataView();
		}
		MobclickAgent.onPageStart("HomeFragment"); // 统计页面
		MobclickAgent.onResume(getActivity()); // 统计时长
	}

	public void onPause() {
		super.onPause();
		// 保证 onPageEnd 在onPause之前调用,因为 onPause中会保存信息
		MobclickAgent.onPageEnd("HomeFragment");
		ImageLoader.getInstance().clearMemoryCache();
		MobclickAgent.onPause(getActivity());
	}

	private List<HomeData> reSetData(YKRecommendation data){
		datePosition++;

		Log.d("haha", datePosition+"--"+itemSize+"--"+ hasCache);


		List<HomeData> homeList = new ArrayList<>();
		//添加日期
		if(null != data.getmFlag()){

			HomeData homeData = new HomeData();
			homeData.setYkFlag(data.getmFlag());
			homeData.setType(1);
			homeList.add(homeData);
		}


		//添加头条
		List<YKTopicData> toplist = data.getmContent().get(0).getmTopicDatas();
		if(null != toplist && toplist.size() > 0){

			//添加头条标题
			HomeData topicTitle = new HomeData();
			topicTitle.setTitle(data.getmContent().get(0).getmTitle());
			topicTitle.setType(4);
			homeList.add(topicTitle);
			for (YKTopicData ykTopicData : toplist) {
				HomeData topicHome = new HomeData();
				topicHome.setTopic(ykTopicData);
				topicHome.setType(2);
				homeList.add(topicHome);
				if(hasCache && datePosition != 1){

					itemSize ++;
				}
				if(!hasCache){
					itemSize ++;
				}

				addAD(homeList);
			}
		}

		//		添加投票
		if(null != data.getVote()){

			HomeData voteData = new HomeData();
			voteData.setVote(data.getVote());
			voteData.setType(5);
			homeList.add(voteData);
			if(hasCache && datePosition != 1){

				itemSize ++;
			}

			if(!hasCache){
				itemSize ++;
			}
			addAD(homeList);
		}


		//添加晒物志
		List<YKNewExperience> experList = data.getmContent().get(1).getmExperiences();
		if(null != experList && experList.size() > 0){

			//添加晒物志标题
			HomeData experTitle = new HomeData();
			experTitle.setTitle(data.getmContent().get(1).getmTitle());
			experTitle.setType(4);
			homeList.add(experTitle);

			for (YKNewExperience ykNewExperience : experList) {
				HomeData expertHome = new HomeData();
				expertHome.setExpert(ykNewExperience);
				expertHome.setType(3);
				homeList.add(expertHome);
				if(hasCache && datePosition != 1){

					itemSize ++;
				}
				if(!hasCache){
					itemSize ++;
				}
				addAD(homeList);
			}
		}




		return homeList;

	}

	private void addAD(List<HomeData> _homeList){
		//添加广告
		if(itemSize == 3){//
			HomeData adHome = new HomeData();
			adHome.setType(6);
			adHome.setBannerAdId(mAdIds[0]);
			_homeList.add(adHome);

		}
		if( itemSize == 6 ){
			HomeData adHome = new HomeData();
			adHome.setType(6);
			adHome.setBannerAdId(mAdIds[1]);
			_homeList.add(adHome);
		}
	}
	/***
	 * 点击事件 return false;//自动调用SDK中的WebView return true;
	 * //不会调用SDK中的WebView,可以自定义
	 */
	@Override
	public boolean onClickAd(String url) {
		Log.i("AdsameBannerAd", "onClickAd()+"+url);
		//		Intent intent = new Intent(getActivity(),AdWebViewActivity.class);
		//		intent.putExtra(AdWebViewActivity.AD_URL, url);

		Intent intent = new Intent(mContext,YKWebViewActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("identification", "index");
		startActivity(intent);
		return true;
	}

	@Override
	public void onReadyAd(AdsameBannerAd arg0) {
		Log.i("AdsameBannerAd", "onReadyAd()");

	}
	// 广告载入成功
	@Override
	public void onReceiveAd(AdsameBannerAd banner) {
		Log.i("AdsameBannerAd", "onReceiveAd()");
		//加入展示集合
		YKFocusImage adFocus = new YKFocusImage();
		adFocus.setType(1);
		adFocus.setBanner(banner);

		mFinalFocusImages.add(adFocus);

		//标记增加
		times ++;
		//开始下一次请求 to 保持同步
		if(times < 4){
			mFocusBannerAd = new AdsameBannerAd(getActivity(),mAdStrings[times],windowWidth,(windowWidth/8)*5);
			mFocusBannerAd.setAdListener(this);
		}
		//请求完成，开始实例化viewpager
		if(times == 4){
			mHandler.sendEmptyMessage(201);
		}

	}
	/**
	 * 添加到对应位置
	 * @param position
	 * @param focusImage
	 */
	private void addNewsToFinalList(){
		int size = failedPosition.size();
		//补全失败广告位置
		if(size <= mFocusImages.size()){
			for (int i = 0; i < size; i++) {
				addToPosition(Integer.parseInt(failedPosition.get(i)),mFocusImages.get(i));
			}

		}

		//补全5个广告位
		int _size = 5 - mFinalFocusImages.size();
		if(_size > 0 && _size <= (mFocusImages.size() - size)){
			addToPosition(4,mFocusImages.get(size));
		}



		initViewPager(mFinalFocusImages);
	}



	private void addToPosition(int position,YKFocusImage focus){
		mFinalFocusImages.add(position, focus);
	}
	// 广告载入失败
	@Override
	public void onReceiveFailed(AdsameBannerAd arg0, int arg1) {
		//Log.i("AdsameBannerAd", "onReceiveFailed()"+mAdStrings[times]+"width"+ windowWidth+"-" + (windowWidth/8)*5);
		failedPosition.add(times+"");
		times ++;

		//开始下一次请求 to 保持同步
		if(times < 4){
			mFocusBannerAd = new AdsameBannerAd(getActivity(),mAdStrings[times],windowWidth,(windowWidth/8)*5);
			mFocusBannerAd.setAdListener(this);
		}
		//请求完成，开始实例化viewpager
		if(times == 4){
			mHandler.sendEmptyMessage(201);
		}
	}

	@Override
	public void onSwitchAd(AdsameBannerAd arg0) {
		Log.i("AdsameBannerAd", "onSwitchAd()");
	}



}