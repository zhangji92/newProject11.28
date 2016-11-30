package com.yoka.mrskin.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.umeng.socialize.utils.Log;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;
import com.xlistview.XListViewFooter;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.BrandTabActivity;
import com.yoka.mrskin.adapter.ExperienceLvAdapter;
import com.yoka.mrskin.adapter.NewsAdapter;
import com.yoka.mrskin.adapter.SingleProductAdapter;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKExperience;
import com.yoka.mrskin.model.data.YKProduct;
import com.yoka.mrskin.model.data.YKTopicData;
import com.yoka.mrskin.model.managers.YKSearchManager;
import com.yoka.mrskin.model.managers.YKSearchManager.YKSearchType;
import com.yoka.mrskin.model.managers.YKSearchManager.searchCallback;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.CustomButterfly;
/**
 * 品牌落地页 
 * @author zlz
 * @Data 2016年7月4日
 */
public class BrandFragment extends Fragment implements IXListViewListener{
	private static final String TAG = SingleProductFragment.class.getSimpleName();
	//	private String mBrand; //品牌名称--根据查询产品，资讯，心得

	private YKSearchType mCurrentTab;
	private Context mContext;
	private XListView xListView;
	private XListViewFooter mListViewFooter;
	private int mPageIndex = 1;//页数标记
	private CustomButterfly mCustomButterfly = null;


	private BaseAdapter mAdapter;
	private ArrayList<YKProduct> mProducts = new ArrayList<>();
	private ArrayList<YKExperience> mTopicDatas = new ArrayList<YKExperience>();
	private ArrayList<YKTopicData> mInformationDatas = new ArrayList<YKTopicData>();

	private float  titleAlpha = 0;



	public BrandFragment(YKSearchType mCurrentTab) {
		super();
		this.mCurrentTab = mCurrentTab;
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

		initViews();
		switch(mCurrentTab){
		case SEARCHTYPE_PRODUCT://产品
			mAdapter = new SingleProductAdapter(mContext, mProducts);
			break;
		case SEARCHTYPE_EXPERIENCE://心得
			mAdapter = new ExperienceLvAdapter(mContext, mTopicDatas);

			break;
		case SEARCHTYPE_INFORMATION://资讯
			mAdapter = new NewsAdapter(mContext, mInformationDatas);
			break;
		default :
			break;
		}

		xListView.setAdapter(mAdapter);
		xListView.setPullLoadEnable(true);

		onRefresh();

	}
	/**
	 * listView滑动到顶部
	 */
	public void smoothTop(){
		if(null != xListView){
			xListView.smoothScrollToPosition(0);
		}

	}


	private void initViews() {
		xListView.addHeaderView(((BrandTabActivity)mContext).headerView);
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
				float headerY = ((BrandTabActivity)mContext).headerView.getY();
				LinearLayout tab_old = ((BrandTabActivity)mContext).tabLayout_old;
				int headerHeight = ((BrandTabActivity)mContext).headerView.getHeight();

				Log.d(TAG, headerY +"**"+headerHeight+"--"+ tab_old.getHeight()+"++"+((BrandTabActivity)mContext).titleView.getHeight());	
				//后期显示
				/*if(headerY < 0 && ((0 -headerY) 
						+ tab_old.getHeight() + ((BrandTabActivity)mContext).titleView.getHeight()) > headerHeight){
					((BrandTabActivity)mContext).tabLayout_new.setVisibility(View.VISIBLE);
					showTabLayout = true;

				}else{
					((BrandTabActivity)mContext).tabLayout_new.setVisibility(View.GONE);
					showTabLayout = false;
				}*/

				if(xListView.getFirstVisiblePosition() >= 5){
					((BrandTabActivity)mContext).return_top.setVisibility(View.VISIBLE);
				}else{
					((BrandTabActivity)mContext).return_top.setVisibility(View.GONE);
				}

				if(headerY <= 0){

					((BrandTabActivity)mContext).titleView.setAlpha( (0 - headerY)/50 ); 
					titleAlpha = (0 - headerY)/50;

				}else{
					((BrandTabActivity)mContext).titleView.setAlpha( 0 );
					titleAlpha = 0;
				}





			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount)
			{
				float headerY = ((BrandTabActivity)mContext).headerView.getY();
				//后期显示
				/*if(headerY <= 0 && ((0 -headerY) 
						+ ((BrandTabActivity)mContext).tabLayout_old.getHeight() 
						+ ((BrandTabActivity)mContext).titleView.getHeight()) > ((BrandTabActivity)mContext).headerView.getHeight()){
					((BrandTabActivity)mContext).tabLayout_new.setVisibility(View.VISIBLE);
					showTabLayout = true;

				}else{
					((BrandTabActivity)mContext).tabLayout_new.setVisibility(View.GONE);
					showTabLayout = false;
				}*/
				if(headerY <= 0){

					((BrandTabActivity)mContext).titleView.setAlpha( (0 - headerY)/50 );
					titleAlpha = (0 - headerY)/50 ;

				}else{
					((BrandTabActivity)mContext).titleView.setAlpha( 0 );
					titleAlpha = 0;
				}

			}
		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Log.d(TAG, "hidden="+hidden+xListView.getFirstVisiblePosition());


		if(!hidden){
			//TODO :后期显示
			/*if(showTabLayout){
				((BrandTabActivity)mContext).tabLayout_new.setVisibility(View.VISIBLE);

			}else{
				((BrandTabActivity)mContext).tabLayout_new.setVisibility(View.GONE);
			}*/
			((BrandTabActivity)mContext).titleView.setAlpha(titleAlpha);

		}
	}

	/**
	 * 刷新
	 * 
	 */
	@Override
	public void onRefresh() {

		try {
			mCustomButterfly = CustomButterfly.show(mContext);
		} catch (Exception e) {
			mCustomButterfly = null;
		}

		refreshData();
	}
	/**
	 * 加载更多
	 */
	@Override
	public void onLoadMore() {

		YKSearchManager.getInstance().requestBrandResult(((BrandTabActivity)mContext).brandId,((BrandTabActivity)mContext).brandName,
				mCurrentTab, mPageIndex, new searchCallback() {
			@Override
			public void callback(YKResult result,
					ArrayList<YKProduct> product,
					ArrayList<YKExperience> experience,
					ArrayList<YKTopicData> information) {
				xListView.stopLoadMore();
				if (result.isSucceeded()) {
					mPageIndex ++;
					//TODO :刷新数据
					switch(mCurrentTab){
					case SEARCHTYPE_PRODUCT://产品
						((SingleProductAdapter)mAdapter).update(product, false);
						break;
					case SEARCHTYPE_EXPERIENCE://心得
						((ExperienceLvAdapter)mAdapter).update(experience, false);
						break;
					case SEARCHTYPE_INFORMATION://资讯
						((NewsAdapter)mAdapter).update(information, false);
						break;
					default :
						break;
					}
				}

			}
		});

	}
	/**
	 * 请求刷新数据
	 */
	private void refreshData(){

		mPageIndex = 1;
		YKSearchManager.getInstance().requestBrandResult(((BrandTabActivity)mContext).brandId,((BrandTabActivity)mContext).brandName,
				mCurrentTab, mPageIndex, new searchCallback() {
			@Override
			public void callback(YKResult result,
					ArrayList<YKProduct> product,
					ArrayList<YKExperience> experience,
					ArrayList<YKTopicData> information) {
				xListView.stopRefresh();
				if (result.isSucceeded()) {
					AppUtil.dismissDialogSafe(mCustomButterfly);

					mPageIndex ++;
					//TODO :刷新数据
					switch(mCurrentTab){
					case SEARCHTYPE_PRODUCT://产品
						((SingleProductAdapter)mAdapter).update(product, true);
						break;
					case SEARCHTYPE_EXPERIENCE://心得
						((ExperienceLvAdapter)mAdapter).update(experience, true);
						break;
					case SEARCHTYPE_INFORMATION://资讯
						((NewsAdapter)mAdapter).update(information, true);
						break;
					default :
						break;
					}

				}else{
					AppUtil.dismissDialogSafe(mCustomButterfly);


				}
			}
		});
	}


}
