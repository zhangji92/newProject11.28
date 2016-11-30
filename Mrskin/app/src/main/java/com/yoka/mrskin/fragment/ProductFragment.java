package com.yoka.mrskin.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.BeautyRankingActivity;
import com.yoka.mrskin.activity.BrandTabActivity;
import com.yoka.mrskin.activity.NewProductValuatingActivity;
import com.yoka.mrskin.activity.SearchLayoutActivity;
import com.yoka.mrskin.activity.SingleProductTabActivity;
import com.yoka.mrskin.activity.WriteExperienceActivity;
import com.yoka.mrskin.activity.YKWebViewActivity;
import com.yoka.mrskin.login.LoginActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKBrand;
import com.yoka.mrskin.model.data.YKCategory;
import com.yoka.mrskin.model.data.YKEfficacy;
import com.yoka.mrskin.model.data.YKImage;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKSearchManager;
import com.yoka.mrskin.model.managers.YKSearchManager.RecommendationCallback;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.MyGallery;
import com.yoka.mrskin.util.MyGridView;
import com.yoka.mrskin.util.RoundImage;
import com.yoka.mrskin.util.YKUtil;

/**
 * 妆品库
 * 
 * @author yuhailong
 */
public class ProductFragment extends Fragment implements OnClickListener,IXListViewListener
{
	private View mRootView;
	private TextView mSearch, mRightWrite;
	private MyGridView  mEfficGridView, mTypeGridView,mProductGridView;
	//	private GridView mProductGridView;
	private BrandAdapter mBrandAdapter;
	private ProductAdpter mProductAdapter;
	private RelativeLayout layoutMore;
	private TextView btnMore;
	private EffectAdapter mEffectAdapter;
	private CategoriesAdapter mCategoriesAdapter;
	private DisplayMetrics dm;
	// private RelativeLayout mDoubleTop, mRightMenu;
	private ImageView imageOne, imageTwo;
	private LinearLayout mNoView/* ,mHeader */;
	private CustomButterfly mCustomButterfly = null;
	// 功效
	private ArrayList<YKEfficacy> mEfficacies;
	// 分类
	private ArrayList<YKCategory> mCategories;
	// 推荐
	private ArrayList<YKImage> mCoverImage;
	// 品牌
	private ArrayList<YKBrand> mBrands;
	private YKBrand brand;

	private MyGallery mGallery;
	private XListView mXListView;
	private LinearLayout mListViewHeader;
	private ArrayList<YKImage> mBrandImages;
	private ArrayList<ArrayList<YKProduct>> mProducts;
	private ImageView mBrandImg;
	public int mCurrentIndex = 0;

	private  DisplayImageOptions     options1 = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
			.resetViewBeforeLoading(true).considerExifParams(true).showImageOnLoading(R.drawable.list_default_bg)
			.build();
	private  DisplayImageOptions     options = new DisplayImageOptions.Builder().cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
			.resetViewBeforeLoading(true).considerExifParams(true)
			.build();
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return mRootView = inflater.inflate(R.layout.product_fragment,
				container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		init();
		try {
			mCustomButterfly = CustomButterfly.show(getActivity());
		} catch (Exception e) {
			mCustomButterfly = null;
		}
		//读取缓存显示
		updateDataUI();
		upDataTagLayout();
		initData();
	}

	@SuppressWarnings("deprecation")
	private void init() {

		dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

		mXListView = (XListView) mRootView.findViewById(R.id.product_list);
		mXListView.setXListViewListener(this);
		mXListView.setPullLoadEnable(false);
		mListViewHeader = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
				R.layout.productfr_headerview, null);

		mBrandImg = (ImageView) mListViewHeader
				.findViewById(R.id.product_fragment_brand_image);
		mGallery = (MyGallery) mListViewHeader
				.findViewById(R.id.product_brand_gallery);
		mGallery.setCallbackDuringFling(false);
		mGallery.setSelection(0);
		mProductGridView = (MyGridView) mListViewHeader
				.findViewById(R.id.product_brand_gridview);
		layoutMore = (RelativeLayout) mListViewHeader
				.findViewById(R.id.product_more_layout);
		btnMore = (TextView) mListViewHeader
				.findViewById(R.id.product_more_tv);
		btnMore.setOnClickListener(this);
		mProductGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mTypeGridView = (MyGridView) mListViewHeader
				.findViewById(R.id.product_type_grid);
		mEfficGridView = (MyGridView) mListViewHeader
				.findViewById(R.id.product_efficacies_grid);

		mRightWrite = (TextView) mRootView
				.findViewById(R.id.product_slidingimage);
		mRightWrite.setOnClickListener(this);
		// 标题
		// mRightMenu = (RelativeLayout) getActivity().findViewById(
		// R.id.product_slidingimage_layout);
		// mRightMenu.setOnClickListener(this);
		// mDoubleTop = (RelativeLayout) rootView
		// .findViewById(R.id.product_title_layout);
		// mDoubleTop.setOnClickListener(this);

		imageOne = (ImageView) mListViewHeader
				.findViewById(R.id.product_title_imageleft);
		imageOne.setOnClickListener(this);
		imageTwo = (ImageView) mListViewHeader
				.findViewById(R.id.product_title_imageright);
		imageTwo.setOnClickListener(this);

		mSearch = (TextView) mRootView
				.findViewById(R.id.serach_and_searchlayout);
		mSearch.setOnClickListener(this);

		mNoView = (LinearLayout) mListViewHeader.findViewById(R.id.view_nodata);
		//mNoView.setVisibility(View.GONE);

		// 解决ScrollView 与 GridView 一起用时item不能显示第一项的问题
		mProductGridView.setFocusable(false);
		mEfficGridView.setFocusable(false);
		mTypeGridView.setFocusable(false);

		mBrandAdapter = new BrandAdapter();
		mProductGridView.setVerticalSpacing(40);
		mProductGridView.setAdapter(mBrandAdapter);

		mEffectAdapter = new EffectAdapter();
		mEfficGridView.setVerticalSpacing(40);
		mEfficGridView.setAdapter(mEffectAdapter);

		mCategoriesAdapter = new CategoriesAdapter();
		mTypeGridView.setVerticalSpacing(40);
		mTypeGridView.setAdapter(mCategoriesAdapter);

		mBrandAdapter = new BrandAdapter();
		mGallery.setAdapter(mBrandAdapter);

		mProductAdapter = new ProductAdpter();
		mProductGridView.setAdapter(mProductAdapter);
		/*用于嵌套在HorizontalScrollView*/
		//		setGridView();

		mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentIndex = position;
				mBrandAdapter.notifyDataSetChanged();
				setCoverImg(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		mXListView.addHeaderView(mListViewHeader);
		mXListView.setAdapter(null);

	}

	private void setGridView() {

		int size = 4;

		int length = 120;

		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int gridviewWidth = (int) (size * (length + 4) * density);
		int itemWidth = (int) (length * density);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
		mProductGridView.setLayoutParams(params); // 重点
		mProductGridView.setColumnWidth(itemWidth); // 重点
		mProductGridView.setHorizontalSpacing(5); // 间距
		mProductGridView.setStretchMode(GridView.NO_STRETCH);
		mProductGridView.setNumColumns(size); // 重点

		mProductGridView.setAdapter(mProductAdapter);
	}

	private void upDataTagLayout() {
		// calculate image height
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		int screenWidth = wm.getDefaultDisplay().getWidth() / 2;

		// 1
		try {
			if (mCoverImage != null) {
				if (mCoverImage.get(0).getmCover() != null) {
					int imageWidth = mCoverImage.get(0).getmCover().getMwidth();
					int imageHeight = mCoverImage.get(0).getmCover().getMheight();
					int tmpHeight = screenWidth * imageHeight / imageWidth;
					imageOne.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, tmpHeight));
				}
			}

			ImageLoader.getInstance().displayImage(mCoverImage.get(0).getmCover().getmURL(), imageOne, options);
			/*Glide.with(ProductFragment.this).load(mCoverImage.get(0).getmCover().getmURL())
    		.into( imageOne);*/



		} catch (Exception e) {
		}
		// 2
		try {
			if (mCoverImage != null) {
				if (mCoverImage.get(1).getmCover() != null) {
					int imageWidth = mCoverImage.get(1).getmCover().getMwidth();
					int imageHeight = mCoverImage.get(1).getmCover().getMheight();
					int tmpHeight = screenWidth * imageHeight / imageWidth;
					imageTwo.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, tmpHeight));
				}
			}

			/*  Glide.with(ProductFragment.this).load(mCoverImage.get(1).getmCover().getmURL())
    		.into( imageTwo);*/
			ImageLoader.getInstance().displayImage(mCoverImage.get(1).getmCover().getmURL(), imageTwo, options);

		} catch (Exception e) {
		}
	}
	/**
	 * 获取缓存
	 */
	private void updateDataUI() {
		mBrandImages = new ArrayList<YKImage>();
		mProducts = new ArrayList<ArrayList<YKProduct>>();
		//		品牌
		ArrayList<YKBrand> brandList = (ArrayList<YKBrand>) YKSearchManager
				.getInstance().getBrandData();

		//		分类
		ArrayList<YKCategory> category = (ArrayList<YKCategory>) YKSearchManager
				.getInstance().getCategorData();
		//		功效
		ArrayList<YKEfficacy> effic = (ArrayList<YKEfficacy>) YKSearchManager
				.getInstance().getEfficData();
		//		 图片（美妆榜单，新品评测）
		ArrayList<YKImage> image = (ArrayList<YKImage>) YKSearchManager
				.getInstance().getImageData();
		for (YKBrand brd : brandList) {
			mBrandImages.add(brd.getmImgBig());
			mProducts.add(brd.getmProducts());
		}


		Log.d("Product", "effic---"+effic.toString());

		mCoverImage = image;
		mBrands = brandList;
		mEfficacies = effic;
		mCategories = category;

		if (mCoverImage != null || mBrands != null || mEfficacies != null
				|| mCategories != null) {
			mNoView.setVisibility(View.VISIBLE);
		}

		mProductAdapter.notifyDataSetChanged();
		mBrandAdapter.notifyDataSetChanged();
		mEffectAdapter.notifyDataSetChanged();
		mCategoriesAdapter.notifyDataSetChanged();
	}
	/**
	 * 联网 获取全部数据
	 */
	private void initData() {
		YKSearchManager.getInstance().requestRecommendations(
				new RecommendationCallback() {

					@Override
					public void callback(YKResult result,
							ArrayList<YKBrand> brands,
							ArrayList<YKCategory> categories,
							ArrayList<YKEfficacy> efficacies,
							ArrayList<YKImage> mCover) {
						AppUtil.dismissDialogSafe(mCustomButterfly);
						mXListView.stopRefresh();
						if (result.isSucceeded()) {
							mBrands = brands;
							mEfficacies = efficacies;
							mCategories = categories;
							mCoverImage = mCover;
							mNoView.setVisibility(View.VISIBLE);
							upDataTagLayout();
							if (mBrands != null && mBrands != null && mBrands.size() > 0 ) {
								for (int i = 0; i < mBrands.size(); i++) {
									mBrandImages.add(mBrands.get(i).getmImgBig());
									mProducts.add(mBrands.get(i).getmProducts());
								}
								// ImageUtils.getImageLoader().displayImage(
								// mBrandImages.get(0).getmURL(),
								// mBrandImg);
							}

							mProductAdapter.setData(mBrands.get(mCurrentIndex)
									.getmProducts());
							mProductAdapter.notifyDataSetChanged();
							mBrandAdapter.notifyDataSetChanged();
							mEffectAdapter.notifyDataSetChanged();
							mCategoriesAdapter.notifyDataSetChanged();
						}
					}
				});
	}

	private void setCoverImg(int position) {
		if (mBrandImg != null && mBrandImages != null && mBrandImages.size() > 0) {

			/*   Glide.with(ProductFragment.this).load(mBrandImages.get(position).getmURL())
    		.into( mBrandImg);*/

			ImageLoader.getInstance().displayImage(mBrandImages.get(position).getmURL(), mBrandImg, options);
		}

		if (mProductAdapter != null && mProducts.size() > 0) {
			mProductAdapter.setData(mProducts.get(position));
			mProductAdapter.notifyDataSetChanged();
			//是否显示查看更多按钮
			if(mProducts.size() >= 3){
				layoutMore.setVisibility(View.VISIBLE);
			}else{
				layoutMore.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 品牌适配器
	 */
	private class BrandAdapter extends BaseAdapter
	{
		ListViewHolder holder = null;

		@Override
		public int getCount() {
			if (mBrands == null) {
				return 0;
			}
			return mBrands.size();
		}

		@Override
		public Object getItem(int position) {
			if (mBrands == null) {
				return null;
			}
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			brand = mBrands.get(position);

			Log.d("BrandId", brand.toString());
			if (convertView == null) {
				holder = new ListViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.product_fragment_list_item_layout, null);
				holder.img = (RoundImage) convertView
						.findViewById(R.id.product_fragment_list_item_name_img);
				// holder.name = (TextView) convertView
				// .findViewById(R.id.product_fragment_list_item_name_text);
				holder.bigLayout = convertView
						.findViewById(R.id.product_fragment_list_item_big_layout);
				convertView.setTag(holder);
			} else {
				holder = (ListViewHolder) convertView.getTag();
			}
			if (!TextUtils.isEmpty(brand.getmImg().getmURL())) {

				/* Glide.with(ProductFragment.this).load(brand.getmImg().getmURL())
        		.into( holder.img);*/
				ImageLoader.getInstance().displayImage(brand.getmImg().getmURL(),  holder.img, options);
			}
			RelativeLayout.LayoutParams params = (LayoutParams) holder.bigLayout
					.getLayoutParams();
			android.view.ViewGroup.LayoutParams param = holder.img
					.getLayoutParams();
			if (mCurrentIndex == position) {
				holder.bigLayout
				.setBackgroundResource(R.drawable.giftest_selected_bg);
				params.height = YKUtil.dip2px(getActivity(), 100);
				params.width = YKUtil.dip2px(getActivity(), 100);
				holder.bigLayout.setLayoutParams(params);

				param.height = YKUtil.dip2px(getActivity(), 100);
				param.width = YKUtil.dip2px(getActivity(), 100);
				holder.img.setLayoutParams(param);
			} else {
				holder.bigLayout.setBackgroundResource(R.drawable.giftest_bg);
				params.height = YKUtil.dip2px(getActivity(), 85);
				params.width = YKUtil.dip2px(getActivity(), 85);
				holder.bigLayout.setLayoutParams(params);

				param.height = YKUtil.dip2px(getActivity(), 85);
				param.width = YKUtil.dip2px(getActivity(), 85);
				holder.img.setLayoutParams(param);
			}
			return convertView;
		}
	}

	private class ListViewHolder
	{
		RoundImage img;
		View bigLayout;
	}

	// 品牌所对应的产品
	private class ProductAdpter extends BaseAdapter
	{

		GridViewHolder holder;

		private ArrayList<YKProduct> products;

		public void setData(ArrayList<YKProduct> products) {
			this.products = products;
		}

		@Override
		public int getCount() {
			if (products == null) {
				return 0;
			}
			if (products.size() > 2) {
				return 3 ;
			}
			return products.size();
		}

		@Override
		public Object getItem(int position) {
			if (products == null) {
				return null;
			}
			return products.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView == null || (convertView != null && convertView.getId() == R.id.product_more_layout)) {
				holder = new GridViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.product_fragment_grid_item_layout, null);
				holder.image = (ImageView) convertView
						.findViewById(R.id.brand_grid_item_img);
				holder.name = (TextView) convertView
						.findViewById(R.id.brand_grid_item_name);
				holder.ratingBar = (RatingBar) convertView
						.findViewById(R.id.brand_grid_item_ratingbar);
				convertView.setTag(holder);
			} else {
				holder = (GridViewHolder) convertView.getTag();
			}
			final YKProduct product = products.get(position);
			if (null != product.getCover() && !TextUtils.isEmpty(product.getCover().getmURL())) {

				/* Glide.with(ProductFragment.this).load(product.getCover().getmURL())
        		.into( holder.image);*/
				ImageLoader.getInstance().displayImage(product.getCover().getmURL(),  holder.image, options);
			}
			if (!TextUtils.isEmpty(product.getTitle())) {
				holder.name.setText(product.getTitle());
			}
			holder.ratingBar.setRating(product.getRating());
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							YKWebViewActivity.class);
					intent.putExtra("productdetalis",product.getDescription_url());
					intent.putExtra("identification", "cosmetics");
					startActivity(intent);
				}
			});
			return convertView;
		}
	}

	private class GridViewHolder
	{
		ImageView image;
		TextView name;
		RatingBar ratingBar;
	}

	// 功效适配器
	public class EffectAdapter extends BaseAdapter
	{
		private ViewHolder viewHolder = null;

		@Override
		public int getCount() {
			if (mEfficacies == null) {
				return 0;
			}
			return mEfficacies.size();
		}

		@Override
		public Object getItem(int position) {
			if (mEfficacies == null) {
				return null;
			}
			return mEfficacies.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final YKEfficacy efficacy = mEfficacies.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.product_fragment_list, null);
				viewHolder = new ViewHolder();
				viewHolder.sTitle = (TextView) convertView
						.findViewById(R.id.product_round_title);
				viewHolder.sImage = (RoundImage) convertView
						.findViewById(R.id.product_round_image);
				viewHolder.sLayoutBrand = (LinearLayout) convertView
						.findViewById(R.id.product_brand_image_layout);
				viewHolder.sLayoutEffectCat = (LinearLayout) convertView
						.findViewById(R.id.product_type_layout);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.sLayoutEffectCat.setVisibility(View.VISIBLE);
			viewHolder.sLayoutBrand.setVisibility(View.GONE);
			viewHolder.sTitle.setText(efficacy.getTitle());
			try {
				/*        Glide.with(ProductFragment.this).load(efficacy.getmThumbnail().getmURL())
        		.into( viewHolder.sImage);*/
				ImageLoader.getInstance().displayImage(efficacy.getmThumbnail().getmURL(),  viewHolder.sImage, options1);
			} catch (Exception e) {
				viewHolder.sImage
				.setBackgroundResource(R.drawable.list_default_bg);
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent goBrand = new Intent(getActivity(),
							SingleProductTabActivity.class);
					goBrand.putExtra("id", efficacy.getID());
					goBrand.putExtra("type", "2");
					goBrand.putExtra("title", efficacy.getTitle());
					if (!TextUtils.isEmpty(efficacy.getID())) {
						startActivity(goBrand);
					}
				}
			});
			return convertView;
		}
	}

	/**
	 * 分类适配器
	 */
	public class CategoriesAdapter extends BaseAdapter
	{
		private ViewHolder viewHolder = null;

		@Override
		public int getCount() {
			if (mCategories == null) {
				return 0;
			}
			return mCategories.size();
		}

		@Override
		public Object getItem(int position) {
			if (mCategories == null) {
				return null;
			}
			return mCategories.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final YKCategory category = mCategories.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.product_fragment_list, null);
				viewHolder = new ViewHolder();
				viewHolder.sTitle = (TextView) convertView
						.findViewById(R.id.product_round_title);
				viewHolder.sImage = (RoundImage) convertView
						.findViewById(R.id.product_round_image);
				viewHolder.sLayoutBrand = (LinearLayout) convertView
						.findViewById(R.id.product_brand_image_layout);
				viewHolder.sLayoutEffectCat = (LinearLayout) convertView
						.findViewById(R.id.product_type_layout);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.sLayoutEffectCat.setVisibility(View.VISIBLE);
			viewHolder.sLayoutBrand.setVisibility(View.GONE);
			viewHolder.sTitle.setText(category.getTitle());
			try {
				/* Glide.with(ProductFragment.this).load(category.getThumbnail().getmURL())
        		.into( viewHolder.sImage);*/
				ImageLoader.getInstance().displayImage(category.getThumbnail().getmURL(),  viewHolder.sImage, options1);
			} catch (Exception e) {
				viewHolder.sImage
				.setBackgroundResource(R.drawable.list_default_bg);
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent goBrand = new Intent(getActivity(),
							SingleProductTabActivity.class);
					goBrand.putExtra("id", category.getID());
					goBrand.putExtra("type", "3");
					goBrand.putExtra("title", category.getTitle());
					if (!TextUtils.isEmpty(category.getID())) {
						startActivity(goBrand);
					}
				}
			});
			return convertView;
		}
	}

	private class ViewHolder
	{
		// GridView
		private TextView sTitle;
		private RoundImage sImage;
		private LinearLayout sLayoutBrand, sLayoutEffectCat;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.serach_and_searchlayout:
			Intent searchAll = new Intent(getActivity(),
					SearchLayoutActivity.class);
			startActivity(searchAll);
			break;
			// case R.id.product_slidingimage_layout:
			// if (YKCurrentUserManager.getInstance().isLogin()) {
			// Intent writeExperience = new Intent(getActivity(),
			// WriteExperienceActivity.class);
			// startActivity(writeExperience);
			// } else {
			// Intent goLogin = new Intent(getActivity(), LoginActivity.class);
			// startActivity(goLogin);
			// }
			// break;
			// case R.id.product_title_layout:
			// DoubleOnClick.doubleClick(null, mProductGridView);
			// break;
		case R.id.product_slidingimage:
			if (YKCurrentUserManager.getInstance().isLogin()) {
				Intent writeExperience = new Intent(getActivity(),
						WriteExperienceActivity.class);
				startActivity(writeExperience);
			} else {
				Intent goLogin = new Intent(getActivity(), LoginActivity.class);
				startActivity(goLogin);
			}
			break;

		case R.id.product_title_imageleft:
			Intent BeautyRanking = new Intent(getActivity(),
					BeautyRankingActivity.class);
			startActivity(BeautyRanking);
			break;
		case R.id.product_title_imageright:
			Intent NewProductValuating = new Intent(getActivity(),
					NewProductValuatingActivity.class);
			startActivity(NewProductValuating);
			break;
		case R.id.product_more_tv:
			Intent intent = new Intent(getActivity(),BrandTabActivity.class);
			intent.putExtra("brand", brand);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	/**
	 * 友盟统计需要的俩个方法
	 */
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ProductFragment"); // 统计页面
		MobclickAgent.onResume(getActivity()); // 统计时长
	}

	public void onPause() {
		super.onPause();
		// 保证 onPageEnd 在onPause之前调用,因为 onPause中会保存信息
		MobclickAgent.onPageEnd("ProductFragment");
		MobclickAgent.onPause(getActivity());
	}

	// 外面调用刷新
	public void onRefresh() {
		initData();
	}

	@Override
	public void onLoadMore()
	{

	}
}
