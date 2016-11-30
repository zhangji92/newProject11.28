package com.jrd.loan.fragment;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jrd.loan.R;
import com.jrd.loan.adapter.InvestRecordAdapter;
import com.jrd.loan.api.FinanceApi;
import com.jrd.loan.base.BaseFragment;
import com.jrd.loan.bean.InvestRecordBean;
import com.jrd.loan.bean.ProjectInvestsList;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.XListView;
import com.jrd.loan.widget.XListView.IXListViewListener;

/**
 * 投资中
 *
 * @author Aaron
 */
public class InvestmentFragment extends BaseFragment implements IXListViewListener {
  private String STATUS = "201";
  private final static int PAGESIZE = 10;
  private int PAGENO = 1;

  private List<ProjectInvestsList> listProjectInvests;

  private LinearLayout homeLayout;
  private XListView mListView;
  private InvestRecordAdapter mInvestRecordAdapter;

  @Override
  protected int getLoadViewId() {
    return R.layout.loan_investrecord_layout;
  }

  @Override
  protected void initView(View view) {
    homeLayout = (LinearLayout) view.findViewById(R.id.loan_investRecord_layoutId);
    homeLayout.setVisibility(View.GONE);

    mListView = (XListView) view.findViewById(R.id.loan_investRecord_ListView);
    mListView.setEmptyView(view.findViewById(R.id.nodatalayout));
    mListView.setOverScrollMode(mListView.OVER_SCROLL_NEVER);
    mListView.setPullLoadEnable(true);
    mListView.setXListViewListener(this);
  }

  @Override
  protected void initData() {
    InvestRecordData(PAGESIZE, 1, STATUS);
  }

  @Override
  public void onRefresh() {
    refreshData(PAGESIZE, STATUS);
  }

  @Override
  public void onLoadMore() {
    InvestRecordData(PAGESIZE, PAGENO, STATUS);
  }

  private void InvestRecordData(int pageSize, int pageNo, String status) {
    FinanceApi.getInvestRecord(getActivity(), pageSize, pageNo, status, new OnHttpTaskListener<InvestRecordBean>() {
      private View noNetworkLayoutView;

      @Override
      public void onTaskError(int resposeCode) {
        if (PAGENO == 1) {
          this.noNetworkLayoutView.setVisibility(View.VISIBLE);
          this.noNetworkLayoutView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              initData();
            }
          });

          DismissDialog();
        }

        onLoad();
      }

      @Override
      public void onStart() {
        if (PAGENO == 1) {
          this.noNetworkLayoutView = getView().findViewById(R.id.investRecordLayout);
          this.noNetworkLayoutView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
          this.noNetworkLayoutView.setVisibility(View.GONE);

          ShowDrawDialog(getActivity());
        }
      }

      @Override
      public void onFinishTask(InvestRecordBean bean) {
        if (PAGENO == 1) {
          DismissDialog();
        }

        if (bean.getResultCode() == 0) {

          homeLayout.setVisibility(View.VISIBLE);

          listProjectInvests = bean.getProjectInvests();

          if (listProjectInvests.isEmpty()) {
            return;
          }

          if (listProjectInvests.size() < 10) {
            mListView.viewGone();
          }

          PAGENO++;
          if (mInvestRecordAdapter == null) {
            mInvestRecordAdapter = new InvestRecordAdapter(getActivity(), listProjectInvests, STATUS);
            mListView.setAdapter(mInvestRecordAdapter);
          } else {
            mInvestRecordAdapter.updateMoreBids(listProjectInvests);
          }
        } else {
          ToastUtil.showShort(getActivity(), bean.getResultMsg());
        }

        onLoad();
      }
    });
  }

  private void refreshData(int pageSize, String status) {
    FinanceApi.getInvestRecord(getActivity(), pageSize, 1, status, new OnHttpTaskListener<InvestRecordBean>() {

      @Override
      public void onTaskError(int resposeCode) {
        onLoad();
      }

      @Override
      public void onStart() {}

      @Override
      public void onFinishTask(InvestRecordBean bean) {
        if (bean.getResultCode() == 0) {

          homeLayout.setVisibility(View.VISIBLE);

          listProjectInvests = bean.getProjectInvests();

          if (listProjectInvests.isEmpty()) {
            return;
          }

          if (listProjectInvests.size() < 10) {
            mListView.viewGone();
          }

          PAGENO = 2;
          if (mInvestRecordAdapter == null) {
            mInvestRecordAdapter = new InvestRecordAdapter(getActivity(), listProjectInvests, STATUS);
            mListView.setAdapter(mInvestRecordAdapter);
          } else {
            mInvestRecordAdapter.updatePageFirstBid(listProjectInvests);
          }
        } else {
          ToastUtil.showShort(getActivity(), bean.getResultMsg());
        }
        onLoad();
      }
    });
  }

  public void onLoad() {
    mListView.stopRefresh();
    mListView.stopLoadMore();
  }

}
