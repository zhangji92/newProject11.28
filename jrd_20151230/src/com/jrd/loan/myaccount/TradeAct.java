package com.jrd.loan.myaccount;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.jrd.loan.R;
import com.jrd.loan.adapter.TradeAdapter;
import com.jrd.loan.api.PayApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.TradeBean;
import com.jrd.loan.bean.TradeBean.DetailList;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.pulltorefresh.library.PullToRefreshBase;
import com.jrd.loan.pulltorefresh.library.PullToRefreshBase.Mode;
import com.jrd.loan.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.jrd.loan.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jrd.loan.pulltorefresh.library.PullToRefreshBase.State;
import com.jrd.loan.pulltorefresh.library.PullToRefreshListView;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;
import com.jrd.loan.widget.XListView;
import com.jrd.loan.widget.XListView.IXListViewListener;

public class TradeAct extends BaseActivity implements IXListViewListener {

    private WindowView windowView;
    private XListView mListView;
    private TradeAdapter tradeAdapter;
    private List<DetailList> mList;
    private int pageSize = 10;
    private int pageNo = 1;
    private boolean isRefresh = false;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_trade;
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setVisibility(View.INVISIBLE);
        windowView.setTitle("交易记录");
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        mListView = (XListView) findViewById(R.id.loan_trade_listview);
        mListView.setEmptyView(findViewById(R.id.nodatalayout));
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        RequsetData();
        setNoNetworkClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RequsetData();
            }
        });
    }

    private void RequsetData() {
        PayApi.queryBusinessRecords(TradeAct.this, pageSize + "", pageNo + "", new OnHttpTaskListener<TradeBean>() {


            @Override
            public void onStart() {
                if (!isRefresh) {
                    ShowDrawDialog(TradeAct.this);
                }
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
                mListView.stopRefresh();
                mListView.stopLoadMore();
            }

            @Override
            public void onFinishTask(TradeBean bean) {
                DismissDialog();
                mListView.stopRefresh();
                mListView.stopLoadMore();
                isRefresh = false;
                if (bean != null && bean.getResultCode() == 0) {
                    windowView.setVisibility(View.VISIBLE);
                    pageNo++;
                    mList = bean.getDetailLists();
                    tradeAdapter = new TradeAdapter(mList, TradeAct.this);
                    mListView.setAdapter(tradeAdapter);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        pageSize = 10;
        pageNo = 1;
        isRefresh = true;
        RequsetData();
    }

    @Override
    public void onLoadMore() {
        PayApi.queryBusinessRecords(TradeAct.this, pageSize + "", pageNo + "", new OnHttpTaskListener<TradeBean>() {

            private View noNetworkLayoutView;

            @Override
            public void onStart() {
            }

            @Override
            public void onTaskError(int resposeCode) {
                mListView.stopRefresh();
                mListView.stopLoadMore();
                this.noNetworkLayoutView.setVisibility(View.VISIBLE);
                this.noNetworkLayoutView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        RequsetData();
                    }
                });
            }

            @Override
            public void onFinishTask(TradeBean bean) {
                mListView.stopRefresh();
                mListView.stopLoadMore();
                isRefresh = false;
                if (bean != null && bean.getResultCode() == 0) {
                    if (bean.getDetailLists() != null && bean.getDetailLists().size() > 1) {
                        pageNo++;
                        mList.addAll(bean.getDetailLists());
                        tradeAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showShort(TradeAct.this, R.string.loan_request_data_compleate);
                    }
                }
            }
        });
    }
}
