package com.jrd.loan.fragment;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jrd.loan.R;
import com.jrd.loan.adapter.TenderRecordAdapter;
import com.jrd.loan.api.PocketApi;
import com.jrd.loan.base.BaseFragment;
import com.jrd.loan.bean.TenderRecordBean;
import com.jrd.loan.bean.TenderRecordBean.TenderList;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.NetUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.XListView;
import com.jrd.loan.widget.XListView.IXListViewListener;

public class TenderRecordFragment extends BaseFragment implements IXListViewListener {

    private XListView mListView;
    private List<TenderList> mList;
    private TenderRecordAdapter mAdapter;
    private LinearLayout titleLayout;
    private int pageSize = 10;
    private int pageNo = 1;
    private boolean isRefresh = false;
    private View noNetworkLayoutView;

    @Override
    protected int getLoadViewId() {
        return R.layout.loan_fragment_tender_record;
    }

    @Override
    protected void initView(View view) {
        this.noNetworkLayoutView = view.findViewById(R.id.noNetworkLayout);
        this.noNetworkLayoutView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        titleLayout = (LinearLayout) view.findViewById(R.id.loan_tender_titleLayout);
        titleLayout.setVisibility(View.GONE);
        mListView = (XListView) view.findViewById(R.id.loan_tender_listview);
        if (NetUtil.hasMobileNet()) {
            mListView.setEmptyView(view.findViewById(R.id.nodatalayout));
        }
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOverScrollMode(mListView.OVER_SCROLL_NEVER);
    }

    @Override
    protected void initData() {
        RequestData();
    }

    /**
     * 网络请求
     */
    private void RequestData() {
        PocketApi.PocketInvestRecord(getActivity(), getArguments().getString("mockId"), pageNo + "", pageSize + "", getArguments().getString("url"), new OnHttpTaskListener<TenderRecordBean>() {

            @Override
            public void onStart() {
                if (!isRefresh) {
                    ShowDrawDialog(getActivity());
                }
                noNetworkLayoutView.setVisibility(View.GONE);
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
                mListView.stopRefresh();
                mListView.stopLoadMore();
                noNetworkLayoutView.setVisibility(View.VISIBLE);
                noNetworkLayoutView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        RequestData();
                    }
                });
            }

            @Override
            public void onFinishTask(TenderRecordBean bean) {
                DismissDialog();
                mListView.stopRefresh();
                mListView.stopLoadMore();
                isRefresh = false;
                if (bean != null) {
                    if (bean.getResultCode() == 0) {
                        if (bean.getResList() != null && bean.getResList().size() > 0) {
                            titleLayout.setVisibility(View.VISIBLE);
                            pageNo++;
                            mList = bean.getResList();
                            mAdapter = new TenderRecordAdapter(getActivity(), mList);
                            mListView.setAdapter(mAdapter);
                        }
                    } else {
                        ToastUtil.showLong(getActivity(), bean.getResultMsg());
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        pageSize = 10;
        pageNo = 1;
        isRefresh = true;
        RequestData();
    }

    @Override
    public void onLoadMore() {
        PocketApi.PocketInvestRecord(getActivity(), getArguments().getString("mockId"), pageNo + "", pageSize + "", getArguments().getString("url"), new OnHttpTaskListener<TenderRecordBean>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onTaskError(int resposeCode) {
                mListView.stopRefresh();
                mListView.stopLoadMore();
            }

            @Override
            public void onFinishTask(TenderRecordBean bean) {
                mListView.stopRefresh();
                mListView.stopLoadMore();
                isRefresh = false;
                if (bean != null) {
                    if (bean.getResultCode() == 0) {

                        if (bean.getResList() != null && bean.getResList().size() > 1) {
                            pageNo++;
                            mList.addAll(bean.getResList());
                            mAdapter = new TenderRecordAdapter(getActivity(), mList);
                            mListView.setAdapter(mAdapter);
                        } else {
                            ToastUtil.showShort(getActivity(), R.string.loan_request_data_compleate);
                        }
                    } else {
                        ToastUtil.showLong(getActivity(), bean.getResultMsg());
                    }
                }
            }
        });
    }
}
