package com.yoka.mrskin.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;
import com.xlistview.XListViewFooter;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.managers.YKSearchManager;
import com.yoka.mrskin.model.managers.YKSearchManager.cosmeticsCallback;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.YKSharelUtil;

public class ProductItemActivity extends BaseActivity implements
OnClickListener, IXListViewListener
{
	private String mId,mType,mTitle;
	private TextView mTopTitle,mNoSearchText;
	private XListView mXListView;
	private ArrayList<YKProduct> mProducts;
	private ProductAdapter mProductAdapter;
	private LinearLayout mCancle;
	private int mProductPageIndex;
	private XListViewFooter mListViewFooter;
	private CustomButterfly mCustomButterfly = null;
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
	.build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_item_list);
		getinitIntent();
		init();
		initData();
		//        TrackManager.getInstance().addTrack(
				//                TrackUrl.PAGE_OPEN + "SearchLayoutActivity");
	}

	@Override
	protected void onDestroy() {
		//        TrackManager.getInstance().addTrack(
		//                TrackUrl.PAGE_CLOSE + "SearchLayoutActivity");
		super.onDestroy();
	}

	private void getinitIntent(){
		Intent pro = getIntent();
		if(pro != null){
			mId = pro.getStringExtra("id");
			mType = pro.getStringExtra("type");
			mTitle = pro.getStringExtra("title");
		}
	}

	private void init() {
		mCancle = (LinearLayout) findViewById(R.id.product_list_back_layout);
		mCancle.setOnClickListener(this);


		mNoSearchText = (TextView) findViewById(R.id.search_no_text);
		mTopTitle = (TextView) findViewById(R.id.product_item_title);
		mTopTitle.setText(mTitle);
		mXListView = (XListView) findViewById(R.id.product_list__list);
		mXListView.setXListViewListener(this);
		mListViewFooter = new XListViewFooter(ProductItemActivity.this);
		mXListView.setOnScrollListener(new OnScrollListener()
		{

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				if(android.os.Build.MODEL != null && android.os.Build.MODEL.equals("MX5")){
					if (view.getLastVisiblePosition() == (view.getCount() - 1)){
						mListViewFooter.setState(XListViewFooter.STATE_READY);
						mXListView.startLoadMore();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount)
			{

			}
		});
		mProductAdapter = new ProductAdapter();
		mXListView.setAdapter(mProductAdapter);
		mXListView.setPullLoadEnable(true);

	}

	private void initData() {
		refreshData(true);
	}

	// 产品适配器
	private class ProductAdapter extends BaseAdapter
	{
		private View mLastMediaView = null;
		private ViewHolder viewHolder = null;

		@Override
		public int getCount() {
			if (mProducts == null) {
				return 0;
			}
			return mProducts.size();
		}

		@Override
		public Object getItem(int position) {
			if (mProducts == null) {
				return null;
			}
			return mProducts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final YKProduct pro = mProducts.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(ProductItemActivity.this)
						.inflate(R.layout.search_layout_item, null);
				viewHolder = new ViewHolder();

				viewHolder.sTitle = (TextView) convertView
						.findViewById(R.id.search_layout_title);
				viewHolder.sPrice = (TextView) convertView
						.findViewById(R.id.search_layout_price);
				viewHolder.sMl = (TextView) convertView
						.findViewById(R.id.search_layout_ml);
				viewHolder.sRatBar = (RatingBar) convertView
						.findViewById(R.id.search_layout_bar);
				viewHolder.sBigImage = (ImageView) convertView
						.findViewById(R.id.search_layout_image);

				convertView.setTag(R.id.search_layout_list, viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView
						.getTag(R.id.search_layout_list);
			}
			/* Glide.with(ProductItemActivity.this).load(pro.getCover().getmURL())
    		.into( viewHolder.sBigImage);*/
    		ImageLoader.getInstance().displayImage(pro.getCover().getmURL(), viewHolder.sBigImage, options);

    		mLastMediaView = viewHolder.sBigImage;
    		//            }
		if(pro.getSpecification() == null){
			viewHolder.sMl.setText("暂无报价");
			viewHolder.sPrice.setText("");
		}else{
			if (!TextUtils.isEmpty(pro.getSpecification().getTitle())) {
				viewHolder.sMl.setText("/"
						+ pro.getSpecification().getTitle());
			}
			float price = 0f;
			if (pro.getSpecification().getPrice() != null) {
				price = pro.getSpecification().getPrice();
				// 构造方法的字符格式这里如果小数不足2位,会以0补足
				DecimalFormat decimalFormat = new DecimalFormat("0");
				// format 返回的是字符串
				if (price == 0) {
					viewHolder.sPrice.setText(getString(R.string.mybag_price));
				} else {
					String newPric = decimalFormat.format(price);
					viewHolder.sPrice.setText("￥" + newPric);
				}
			}
		}
		viewHolder.sTitle.setText(pro.getTitle());
		viewHolder.sRatBar.setRating(pro.getRating());

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String webPager = null;
				if (pro.getDescription_url() != null) {
					webPager = YKSharelUtil.tryToGetWebPagemUrl(
							ProductItemActivity.this, pro.getDescription_url());
				}
				Intent shareTop = new Intent(ProductItemActivity.this,
						YKWebViewActivity.class);
				if (AppUtil.isNetworkAvailable(ProductItemActivity.this)) {
					if (webPager != null) {
						String url = pro.getDescription_url();
						Uri uri = Uri.parse(url);
						shareTop.putExtra("productdetalis",
								uri.getQueryParameter("url"));
						shareTop.putExtra("identification", "cosmetics");
						shareTop.putExtra("title", "品牌");
						startActivity(shareTop);
					}
				}
			}
		});
		//            TrackManager.getInstance().addTrack(
		//                    TrackUrl.ITEM_EXPOSURE + pro.getID() + "&type=product");
		return convertView;
		}
	}



	private class ViewHolder
	{
		// Product
		private TextView sTitle, sPrice, sMl;
		private ImageView sBigImage;
		private RatingBar sRatBar;


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.product_list_back_layout:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		refreshData(false);
	}

	@Override
	public void onLoadMore() {
		YKSearchManager.getInstance().requestProductCosmetics(mProductPageIndex,
				Integer.parseInt(mType), 10,Integer.parseInt(mId),3, new cosmeticsCallback() {

			@Override
			public void callback(YKResult result,
					ArrayList<YKProduct> product)
			{
				mXListView.stopLoadMore();
				if (result.isSucceeded()) {
					++mProductPageIndex;
					if (mProducts != null) {
						mProducts.addAll(product);
						mProductAdapter.notifyDataSetChanged();
					} 
				}
			}
		});
	}

	public void refreshData(final boolean showDialog) {
		if (showDialog) {
			try {
				mCustomButterfly = CustomButterfly.show(this);
			} catch (Exception e) {
				mCustomButterfly = null;
			}
		}
		mProductPageIndex = 0;
		YKSearchManager.getInstance().requestProductCosmetics(mProductPageIndex, Integer.parseInt(mType), 10, Integer.parseInt(mId),3, new cosmeticsCallback()
		{

			@Override
			public void callback(YKResult result, ArrayList<YKProduct> product)
			{
				mXListView.stopRefresh();
				if (showDialog) {
					AppUtil.dismissDialogSafe(mCustomButterfly);
				}
				if (result.isSucceeded()) {
					if (product != null && product.size() > 0) {
						++mProductPageIndex;
						mProducts = product;
						mNoSearchText.setVisibility(View.GONE);
						mXListView.setVisibility(View.VISIBLE);
						mProductAdapter.notifyDataSetChanged();
					} else {
						mXListView.setVisibility(View.GONE);
						mNoSearchText.setVisibility(View.VISIBLE);
						mNoSearchText.setText("没有与" + mTitle
								+ "相关的产品，换个词再试一试");
					}
				} else {
					Toast.makeText(ProductItemActivity.this,
							(String) result.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}
}
