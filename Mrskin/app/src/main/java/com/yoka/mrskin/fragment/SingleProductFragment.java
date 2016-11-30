package com.yoka.mrskin.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;
import com.xlistview.XListViewFooter;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.SingleProductTabActivity;
import com.yoka.mrskin.adapter.SingleProductAdapter;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.managers.YKSearchManager;
import com.yoka.mrskin.model.managers.YKSearchManager.cosmeticsCallback;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
/**
 * 单品列表 
 * Flag 3:默认 / 1:最热  / 2:最新
 * @author zlz
 * @Data 2016年7月1日
 */
public class SingleProductFragment extends Fragment implements IXListViewListener{
	private static final String TAG = SingleProductFragment.class.getSimpleName();
	private int mSearchType;
	private Context mContext;
	private XListView xListView;
	private XListViewFooter mListViewFooter;
	private int mPageIndex = 0;//页数标记
	private CustomButterfly mCustomButterfly = null;

	private SingleProductAdapter mAdapter;
	private ArrayList<YKProduct> mProducts = new ArrayList<>();
	private boolean isScrolled  = false; //是否滚动到 需要回滚位置

	public SingleProductFragment(int flag) {
		super();
		this.mSearchType = flag;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.single_product_fragment, null);
		xListView = (XListView) view.findViewById(R.id.single_product_xlist);
		this.mContext = getActivity();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initData();

		refreshData(true);

	}

	private void initData() {
		xListView.setXListViewListener(this);
		mListViewFooter = new XListViewFooter(mContext);
		xListView.setOnScrollListener(new OnScrollListener()
		{

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				if(android.os.Build.MODEL != null && android.os.Build.MODEL.equals("MX5")){
					if (view.getLastVisiblePosition() == (view.getCount() - 1)){
						mListViewFooter.setState(XListViewFooter.STATE_READY);
						xListView.startLoadMore();
					}
				}

				if(xListView.getFirstVisiblePosition() >= 5){
					((SingleProductTabActivity)mContext).return_top.setVisibility(View.VISIBLE);
					isScrolled = true;
				}else{
					((SingleProductTabActivity)mContext).return_top.setVisibility(View.GONE);
					isScrolled = false;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount)
			{

			}
		});

		mAdapter = new SingleProductAdapter(mContext, mProducts);
		xListView.setAdapter(mAdapter);
		xListView.setPullLoadEnable(true);

	}
	public void smoothTop(){
		if(null != xListView){
			xListView.smoothScrollToPosition(0);
		}

	}
	public boolean isScrolled(){
		return isScrolled;
	}


	/**
	 * 刷新
	 */
	@Override
	public void onRefresh() {
		refreshData(true);
	}
	/**
	 * 加载更多
	 */
	@Override
	public void onLoadMore() {
		YKSearchManager.getInstance().requestProductCosmetics(mPageIndex,
				Integer.parseInt(SingleProductTabActivity.mType), 10
				,Integer.parseInt(SingleProductTabActivity.mId),mSearchType, new cosmeticsCallback() {

			@Override
			public void callback(YKResult result,
					ArrayList<YKProduct> product)
			{
				xListView.stopLoadMore();
				if (result.isSucceeded()) {
					mPageIndex++;
					if (mProducts != null) {
						mProducts.addAll(product);
						mAdapter.notifyDataSetChanged();
					} 
				}
			}
		});
	}


	public void refreshData(final boolean showDialog) {
		if (showDialog) {
			try {
				mCustomButterfly = CustomButterfly.show(mContext);
			} catch (Exception e) {
				mCustomButterfly = null;
			}
		}
		mPageIndex = 0;
		Log.d(TAG, mPageIndex+"--"+SingleProductTabActivity.mType+"--"+SingleProductTabActivity.mId);
		YKSearchManager.getInstance().requestProductCosmetics(mPageIndex, Integer.parseInt(SingleProductTabActivity.mType), 10
				, Integer.parseInt(SingleProductTabActivity.mId),mSearchType, new cosmeticsCallback()
		{

			@Override
			public void callback(YKResult result, ArrayList<YKProduct> product)
			{
				xListView.stopRefresh();
				if (showDialog) {
					AppUtil.dismissDialogSafe(mCustomButterfly);
				}
				if (result.isSucceeded()) {
					if (product != null && product.size() > 0) {
						mPageIndex ++;
						//						mProducts = product;
						xListView.setVisibility(View.VISIBLE);
						mAdapter.update(product, true);
					} else {
						xListView.setVisibility(View.GONE);

					}
				} else {
					Toast.makeText(mContext,
							(String) result.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}


}
