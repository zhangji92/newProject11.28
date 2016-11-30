package com.yoka.mrskin.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.YKWebViewActivity;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.YKSharelUtil;

/**
 * 
 * @author zlz
 * @Data 2016年7月1日
 */
public class SingleProductAdapter extends BaseAdapter{
	private static final String TAG = SingleProductAdapter.class.getSimpleName();
	private Context mContext;
	private ArrayList<YKProduct> mProducts;
	private View mLastMediaView = null;//作用？？？？？？
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
	.build();


	public SingleProductAdapter(Context mContext, ArrayList<YKProduct> mProducts) {
		super();
		if(null == mProducts){
			return;
		}
		this.mContext = mContext;
		this.mProducts = mProducts;
	}
	public void update(ArrayList<YKProduct> _products,boolean isRefresh){
		if(isRefresh){
			mProducts.clear();
		}
		mProducts.addAll(_products);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mProducts == null ? 0 :mProducts.size();
	}

	@Override
	public Object getItem(int position) {
		return mProducts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProductHolder holder = null;
		if(null == convertView){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.search_layout_item, null);
			holder = new ProductHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ProductHolder) convertView.getTag();
		}

		final YKProduct pro = (YKProduct) getItem(position);
		holder.bindData(pro);

		/** 设置点击监听 */
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String webPager = null;
				if (pro.getDescription_url() != null) {
					webPager = YKSharelUtil.tryToGetWebPagemUrl(
							mContext, pro.getDescription_url());
				}
				Intent shareTop = new Intent(mContext,
						YKWebViewActivity.class);
				if (AppUtil.isNetworkAvailable(mContext)) {
					if (webPager != null) {
						String url = pro.getDescription_url();
						Uri uri = Uri.parse(url);
						shareTop.putExtra("productdetalis",
								uri.getQueryParameter("url"));
						shareTop.putExtra("identification", "cosmetics");
						shareTop.putExtra("title", "品牌");
						mContext.startActivity(shareTop);
					}
				}
			}
		});
		return convertView;
	}

	/**
	 * 单品Holder
	 */
	class ProductHolder{
		private TextView sTitle, sPrice, sMl;
		private ImageView sBigImage;
		private RatingBar sRatBar;

		public ProductHolder(View view) {

			sTitle = (TextView) view
					.findViewById(R.id.search_layout_title);
			sPrice = (TextView) view
					.findViewById(R.id.search_layout_price);
			sMl = (TextView) view
					.findViewById(R.id.search_layout_ml);
			sRatBar = (RatingBar) view
					.findViewById(R.id.search_layout_bar);
			sBigImage = (ImageView) view
					.findViewById(R.id.search_layout_image);
		}

		private void bindData(YKProduct pro){
			/* Glide.with(ProductItemActivity.this).load(pro.getCover().getmURL())
    		.into( viewHolder.sBigImage);*/
			ImageLoader.getInstance().displayImage(pro.getCover().getmURL(),sBigImage, options);

			mLastMediaView = sBigImage;
			//            }
			if(pro.getSpecification() == null){
				sMl.setText("暂无报价");
				sPrice.setText("");
			}else{
				if (!TextUtils.isEmpty(pro.getSpecification().getTitle())) {
					sMl.setText("/"
							+ pro.getSpecification().getTitle());
				}
				float price = 0f;
				if (pro.getSpecification().getPrice() != null) {
					price = pro.getSpecification().getPrice();
					// 构造方法的字符格式这里如果小数不足2位,会以0补足
					DecimalFormat decimalFormat = new DecimalFormat("0");
					// format 返回的是字符串
					if (price == 0) {
						sPrice.setText(mContext.getString(R.string.mybag_price));
					} else {
						String newPric = decimalFormat.format(price);
						sPrice.setText("￥" + newPric);
					}
				}
			}
			sTitle.setText(pro.getTitle());
			sRatBar.setRating(pro.getRating());
		}


	}

}
