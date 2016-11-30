package com.handmark.pulltorefresh.library;


import com.handmark.pulltorefresh.library.PullToRefreshListView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class LoadMoreListView extends PullToRefreshListView implements  OnClickListener{

	public interface IListViewState
	{
		int LVS_NORMAL = 0;
		int LVS_PULL_REFRESH = 1;			//
		int LVS_RELEASE_REFRESH = 2;		//
		int LVS_LOADING = 3;
	}
	
	
	public interface IOnRefreshListener
	{
		void OnRefresh();
	}
	

	
	public LoadMoreListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	
	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	
	private void init(Context context)
	{
		initLoadMoreView(context);
	}

	private void initLoadMoreView(Context context)
	{
		mFootView= LayoutInflater.from(context).inflate(R.layout.loadmore, null);
		
		mLoadMoreView = mFootView.findViewById(R.id.load_more_view);
		
		mLoadMoreTextView = (TextView) mFootView.findViewById(R.id.load_more_tv);
		
		mLoadingView = mFootView.findViewById(R.id.loading_layout);
	
		mLoadMoreView.setOnClickListener(this);
		
		addFooterView(mFootView);

	}
	

	
	
	
	
	
	
	private View mFootView;								
	private View mLoadMoreView;
	private TextView mLoadMoreTextView;
	private View mLoadingView;
	private IOnLoadMoreListener mLoadMoreListener;
	private int mLoadMoreState = IListViewState.LVS_NORMAL;
	
	public interface ILoadMoreViewState
	{
		int LMVS_NORMAL= 0;
		int LMVS_LOADING = 1;
		int LMVS_OVER = 2;
	}
	
	public interface IOnLoadMoreListener
	{
		void OnLoadMore();
	}
	
	public void setOnLoadMoreListener(IOnLoadMoreListener listener)
	{
		mLoadMoreListener = listener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.load_more_view) {
			if (mLoadMoreListener != null && mLoadMoreState == IListViewState.LVS_NORMAL)
			{
				updateLoadMoreViewState(ILoadMoreViewState.LMVS_LOADING);
				mLoadMoreListener.OnLoadMore();
			}
		}
	}

	public void onLoadMoreStart(){
		updateLoadMoreViewState(ILoadMoreViewState.LMVS_LOADING);
	}
	//
	public void onLoadMoreComplete(boolean flag)
	{
		if (flag)
		{
			updateLoadMoreViewState(ILoadMoreViewState.LMVS_OVER);
		}else{
			updateLoadMoreViewState(ILoadMoreViewState.LMVS_NORMAL);
		}
		
	}
	
	
	//
	private void updateLoadMoreViewState(int state)
	{
		switch(state)
		{
			case ILoadMoreViewState.LMVS_NORMAL:
				mLoadingView.setVisibility(View.GONE);
				mLoadMoreTextView.setVisibility(View.VISIBLE);
				mLoadMoreTextView.setText("加载更多");
				break;
			case ILoadMoreViewState.LMVS_LOADING:
				mLoadingView.setVisibility(View.VISIBLE);
				mLoadMoreTextView.setVisibility(View.GONE);
				break;
			case ILoadMoreViewState.LMVS_OVER:
				mLoadingView.setVisibility(View.GONE);
				mLoadMoreTextView.setVisibility(View.VISIBLE);
				mLoadMoreTextView.setText("加载完成");
				break;
				default:
					break;
		}
		
		mLoadMoreState = state;
	}

	
	public void removeFootView()
	{
		removeFooterView(mFootView);
	}


}
