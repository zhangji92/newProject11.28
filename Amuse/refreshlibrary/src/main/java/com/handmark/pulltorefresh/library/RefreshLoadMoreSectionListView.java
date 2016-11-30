package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.handmark.pulltorefresh.library.LoadMoreListView.IListViewState;
import com.handmark.pulltorefresh.library.LoadMoreListView.ILoadMoreViewState;
import com.handmark.pulltorefresh.library.LoadMoreListView.IOnLoadMoreListener;
import com.handmark.pulltorefresh.library.SectionHeaderListView.PinnedSectionedHeaderAdapter;

public class RefreshLoadMoreSectionListView  extends PullToRefreshListView_Section implements View.OnClickListener {
	
	
	public interface IListViewState
	{
		int LVS_NORMAL = 0;					//  ��ͨ״̬
		int LVS_PULL_REFRESH = 1;			//  ����ˢ��״̬
		int LVS_RELEASE_REFRESH = 2;		//  �ɿ�ˢ��״̬
		int LVS_LOADING = 3;				//  ����״̬
	}
	
	
	public interface IOnRefreshListener
	{
		void OnRefresh();
	}
	

	
	public RefreshLoadMoreSectionListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	
	public RefreshLoadMoreSectionListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	
	private void init(Context context)
	{
		initLoadMoreView(context);
	}
	// ��ʼ��footview��ͼ
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
	private IOnLoadMoreListener mLoadMoreListener;						// ���ظ�������
	private int mLoadMoreState = IListViewState.LVS_NORMAL;
	
	public interface ILoadMoreViewState
	{
		int LMVS_NORMAL= 0;					//  ��ͨ״̬
		int LMVS_LOADING = 1;				//  ����״̬
		int LMVS_OVER = 2;					//  ����״̬
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
	//��ʼ������
	public void onLoadMoreStart(){
		updateLoadMoreViewState(ILoadMoreViewState.LMVS_LOADING);
	}
	// flag ����Ƿ���ȫ���������
	public void onLoadMoreComplete(boolean flag)
	{
		if (flag)
		{
			updateLoadMoreViewState(ILoadMoreViewState.LMVS_OVER);
		}else{
			updateLoadMoreViewState(ILoadMoreViewState.LMVS_NORMAL);
		}
		
	}
	
	
	// ����footview��ͼ
	private void updateLoadMoreViewState(int state)
	{
		switch(state)
		{
			case ILoadMoreViewState.LMVS_NORMAL:
				mLoadingView.setVisibility(View.GONE);
				mLoadMoreTextView.setVisibility(View.VISIBLE);
				mLoadMoreTextView.setText("�鿴���");
				break;
			case ILoadMoreViewState.LMVS_LOADING:
				mLoadingView.setVisibility(View.VISIBLE);
				mLoadMoreTextView.setVisibility(View.GONE);
				break;
			case ILoadMoreViewState.LMVS_OVER:
				mLoadingView.setVisibility(View.GONE);
				mLoadMoreTextView.setVisibility(View.VISIBLE);
				mLoadMoreTextView.setText("û�������");
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
