package com.jrd.loan.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;

import com.jrd.loan.R;
import com.jrd.loan.adapter.FinanceDirectFragmentAdapter;
import com.jrd.loan.api.ProjectInfoApi;
import com.jrd.loan.base.BaseFragment;
import com.jrd.loan.bean.ProjectInfoBean;
import com.jrd.loan.bean.ProjectList;
import com.jrd.loan.finance.ProjectIntroduceActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.XListView;
import com.jrd.loan.widget.XListView.IXListViewListener;

/**
 * 直投项目
 * 
 * @author Aaron
 */
public class DirectFragment extends BaseFragment implements IXListViewListener, OnItemClickListener {
	private Context mContext;
	private final static int PAGESIZE = 10;
	private int PAGENO = 1;
	private XListView mListView;
	private FinanceDirectFragmentAdapter mDirectAdapter;
	private List<ProjectList> projectList;
	private Boolean isLoading = false;// 是否在加载中

	@Override
	protected int getLoadViewId() {
		return R.layout.loan_finance_direct_investment;
	}

	@Override
	protected void initView(View view) {
		this.mContext = getActivity();
		this.mListView = (XListView) view.findViewById(R.id.loan_finance_direct_ListView);
		this.mListView.setVisibility(View.GONE);

		// 跳转到项目介绍
		this.mListView.setOnItemClickListener(this.itemClickListener);

		this.mListView.setPullRefreshEnable(true);
		this.mListView.setPullLoadEnable(true);
		this.mListView.setXListViewListener(this);
		this.mListView.setOverScrollMode(mListView.OVER_SCROLL_NEVER);
	}

	@Override
	protected void initData() {
		PAGENO = 1;
		ProjectListInfo(PAGESIZE, PAGENO, true, false);
	}

	// 跳转到项目介绍
	private final OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			ProjectList projectList = (ProjectList) mDirectAdapter.getItem(position - 1);

			this.showBidScreen(projectList);
		}

		private void showBidScreen(ProjectList projectList) {
			Intent intent = new Intent(mContext, ProjectIntroduceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("mockId", projectList.getMockId());
			startActivity(intent);
		}
	};

	// 查询直投项目列表
	private void ProjectListInfo(int pageSize, final int pageNo, final Boolean isFirst, final Boolean isClear) {
		if (!isLoading) {
			ProjectInfoApi.getProjectListInfo(mContext, pageSize, pageNo, new OnHttpTaskListener<ProjectInfoBean>() {
				private View noNetworkLayoutView;

				@Override
				public void onTaskError(int resposeCode) {
					isLoading = false;

					if (pageNo == 1 && !(isFirst && isClear)) {
						this.showNetworkExp();

						DismissDialog();
					}
				}

				@Override
				public void onStart() {
					if (pageNo == 1 && !(isFirst && isClear)) {
						this.noNetworkLayoutView = getView().findViewById(R.id.noNetworkLayout);
						this.noNetworkLayoutView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
						this.noNetworkLayoutView.setVisibility(View.GONE);

						ShowDrawDialog(mContext);
					}

					isLoading = true;
					if (isFirst) {
						projectList = new ArrayList<ProjectList>();
					}

					if (isClear) {
						projectList.clear();
					}
				}

				@Override
				public void onFinishTask(ProjectInfoBean bean) {
					if (pageNo == 1 && !(isFirst && isClear)) {
						DismissDialog();
					}

					if (bean.getResultCode() == 0) {
						if (pageNo == 1) {
							mListView.setVisibility(View.VISIBLE);
						}

						PAGENO++;

						projectList = bean.getProjectList();

						if (isFirst) {
							mDirectAdapter = new FinanceDirectFragmentAdapter(mContext, projectList);
							mListView.setAdapter(mDirectAdapter);
							mDirectAdapter.notifyDataSetChanged();
						} else {
							mDirectAdapter.addBidList(projectList);
						}
					} else {
						ToastUtil.showShort(mContext, bean.getResultMsg());
					}

					stopOnLoad();
					isLoading = false;
				}

				private void showNetworkExp() {
					this.noNetworkLayoutView.setVisibility(View.VISIBLE);
					this.noNetworkLayoutView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							initData();
						}
					});
				}
			});
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onRefresh() {
		if (!isLoading) {
			PAGENO = 1;
			ProjectListInfo(PAGESIZE, PAGENO, true, true);
		}
	}

	@Override
	public void onLoadMore() {
		if (!isLoading) {
			ProjectListInfo(PAGESIZE, PAGENO, false, false);
		}
	}

	private void stopOnLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}

}
