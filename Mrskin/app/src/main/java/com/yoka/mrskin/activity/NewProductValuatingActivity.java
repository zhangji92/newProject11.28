package com.yoka.mrskin.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.yoka.mrskin.model.data.YKWebentry;
import com.yoka.mrskin.model.managers.YKNewProductValuatManager;
import com.yoka.mrskin.model.managers.YKNewProductValuatManager.Callback;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.YKSharelUtil;

/**
 * 新品评测
 * @author yuhailong
 */
public class NewProductValuatingActivity extends BaseActivity implements OnClickListener,IXListViewListener
{
	private XListView mListView;
	private LinearLayout mBack;
	private MyAdapter myAdapter;
	private int mPageIndex;
	private ArrayList<YKWebentry> mYkWebentry;
	private Context mContext;
	private CustomButterfly mCustomButterfly = null;
	private RelativeLayout mErrorRelativeLayout;
	private XListViewFooter mListViewFooter;
	private DisplayImageOptions options;

	private int screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_new_valuating);

		// calculate image height
		WindowManager wm = (WindowManager) NewProductValuatingActivity.this.getSystemService(
				Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
		
		mContext=this;
		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true).considerExifParams(true).showImageOnLoading(R.drawable.list_default_bg)
				.build();

		init();
		initData();
	}
	private void init(){
		mListView = (XListView) findViewById(R.id.new_product_listview);
		mListView.setPullRefreshEnable(false);
		mListView.setPullLoadEnable(true);
		mListViewFooter = new XListViewFooter(mContext);
		mListView.setOnScrollListener(new OnScrollListener()
		{

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				if(android.os.Build.MODEL != null && android.os.Build.MODEL.equals("MX5")){
					if (view.getLastVisiblePosition() == (view.getCount() - 1)){
						mListViewFooter.setState(XListViewFooter.STATE_READY);
						mListView.startLoadMore();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount)
			{

			}
		});
		mBack = (LinearLayout) findViewById(R.id.newproduct_back_layout);
		mBack.setOnClickListener(this);
		myAdapter = new MyAdapter();
		mListView.setAdapter(myAdapter);
		mListView.setXListViewListener(this);
		mErrorRelativeLayout = (RelativeLayout) findViewById(R.id.newproductErrorLayout);
		mErrorRelativeLayout.setOnClickListener(this);
	}

	private void initData(){
		try {
			mCustomButterfly = CustomButterfly.show(this);
		} catch (Exception e) {
			mCustomButterfly = null;
		}
		mPageIndex = 0;
		mErrorRelativeLayout.setVisibility(View.GONE);
		String page = String.valueOf(mPageIndex);
		YKNewProductValuatManager.getInstance().requestNewProductValuat(page, 10, new Callback()
		{

			@Override
			public void callback(ArrayList<YKWebentry> webentry, YKResult result)
			{
				AppUtil.dismissDialogSafe(mCustomButterfly);
				if(result.isSucceeded()){
					mYkWebentry =  webentry;
					myAdapter.notifyDataSetChanged();
					++mPageIndex;
				}
				else {
					if (mYkWebentry == null || mYkWebentry.size() == 0) {
						mErrorRelativeLayout.setVisibility(View.VISIBLE);
					}
					if (AppUtil.isNetworkAvailable(mContext)) {
						Toast.makeText(mContext, R.string.intent_error, Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(mContext, R.string.intent_no, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newproduct_back_layout:
			finish();
			break;
		case R.id.newproductErrorLayout:
			initData();
			break;
		default:
			break;
		}
	}
	private class MyAdapter extends BaseAdapter
	{

		private ViewHolder viewHolder = null;

		@Override
		public int getCount() {
			if (mYkWebentry == null) {
				return 0;
			}
			return mYkWebentry.size();
		}

		@Override
		public Object getItem(int position) {
			if (mYkWebentry == null) {
				return null;
			}
			return mYkWebentry.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final YKWebentry webentry = mYkWebentry.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(NewProductValuatingActivity.this).inflate(
						R.layout.product_new_valuating_item, null);
				viewHolder = new ViewHolder();

				viewHolder.sTitle = (TextView) convertView
						.findViewById(R.id.product_new_title);
				viewHolder.sBigImage = (ImageView) convertView
						.findViewById(R.id.product_new_image);

				convertView.setTag(R.id.new_product_listview, viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag(R.id.new_product_listview);
			}


			int imageWidth = webentry.getmCover().getMwidth();
			int imageHeight = webentry.getmCover().getMheight();
			int tmpHeight = 0;
			if(imageWidth == 0 || imageHeight == 0 ){
				imageHeight = 320;
				imageWidth  = 640;
			}
			
			tmpHeight = screenWidth * imageHeight / imageWidth;
			viewHolder.sBigImage.setLayoutParams(new LinearLayout.LayoutParams(
					screenWidth, tmpHeight));


			/*    viewHolder.sBigImage.setBackground(getResources().getDrawable(R.drawable.list_default_bg));
            Glide.with(mContext).load(webentry.getmCover().getmURL()).into( viewHolder.sBigImage);*/

			ImageLoader.getInstance().displayImage(
					webentry.getmCover().getmURL(),
					viewHolder.sBigImage, options);

			viewHolder.sTitle.setText(webentry.getmTitle());

			convertView.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					String webPager = null;
					if (webentry.getmUrl() != null) {
						webPager = YKSharelUtil.tryToGetWebPagemUrl(
								NewProductValuatingActivity.this, webentry.getmUrl());
					}
					Intent shareTop = new Intent(NewProductValuatingActivity.this,
							YKWebViewActivity.class);
					if (AppUtil.isNetworkAvailable(NewProductValuatingActivity.this)) {
						if (webPager != null) {
							String url = webentry.getmUrl();
							Uri uri = Uri.parse(url);
							shareTop.putExtra("url",
									uri.getQueryParameter("url"));
							shareTop.putExtra("title", webentry.getmTitle());
							shareTop.putExtra("track_type", "home_topic_topic");
							startActivity(shareTop);
						}
					}
				}
			});
			return convertView;
		}
	}

	private class ViewHolder
	{
		// ListView
		private TextView sTitle;
		private ImageView sBigImage;
	}

	@Override
	public void onRefresh()
	{

	}
	@Override
	public void onLoadMore()
	{
		String loadMoreINdex = String.valueOf(mPageIndex);
		YKNewProductValuatManager.getInstance().requestNewProductValuat(loadMoreINdex, 10, new Callback()
		{

			@Override
			public void callback(ArrayList<YKWebentry> webentry, YKResult result)
			{
				mListView.stopLoadMore();

				if(result.isSucceeded()){
					if(webentry.size() == 0){
						Toast.makeText(NewProductValuatingActivity.this,
								getString(R.string.task_no_task),
								Toast.LENGTH_LONG).show();
					}else{
						mYkWebentry.addAll(webentry);
						myAdapter.notifyDataSetChanged();
					}
					++mPageIndex;
				}
			}
		}); 
	}
}
