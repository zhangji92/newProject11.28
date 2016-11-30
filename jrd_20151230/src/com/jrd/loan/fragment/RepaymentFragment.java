package com.jrd.loan.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jrd.loan.R;
import com.jrd.loan.adapter.RepaymentAdapter;
import com.jrd.loan.api.FinanceApi;
import com.jrd.loan.base.BaseFragment;
import com.jrd.loan.bean.DetailBean;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.XListView;
import com.jrd.loan.widget.XListView.IXListViewListener;

/**
 * 还款表现
 *
 * @author Aaron
 */
public class RepaymentFragment extends BaseFragment implements IXListViewListener {
  private String FLAG = "2";

  private Boolean mBoolean = true;

  private LinearLayout homeLayout;

  private XListView mListView;
  private RepaymentAdapter mRepaymentAdapter;

  @Override
  protected int getLoadViewId() {
    return R.layout.loan_repayment_layout;
  }


  @Override
  protected void initView(View view) {

    homeLayout = (LinearLayout) view.findViewById(R.id.loan_repayment_layoutId);
    homeLayout.setVisibility(View.GONE);

    mListView = (XListView) view.findViewById(R.id.loan_repayment_ListView);

    this.mListView.setPullRefreshEnable(true);
    this.mListView.setPullLoadEnable(true);
    this.mListView.setXListViewListener(this);
    this.mListView.setOverScrollMode(mListView.OVER_SCROLL_NEVER);
  }


  @Override
  protected void initData() {
    DetailInfoData(getArguments().getString("mockId"), FLAG);
  }

  private void DetailInfoData(String mockId, String flag) {
    FinanceApi.getDetailInfo(getActivity(), mockId, flag, new OnHttpTaskListener<DetailBean>() {
      private View noNetworkLayoutView;

      @Override
      public void onTaskError(int resposeCode) {
        if (mBoolean) {
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
        if (mBoolean) {
          this.noNetworkLayoutView = getView().findViewById(R.id.repaymentLayout);
          this.noNetworkLayoutView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
          this.noNetworkLayoutView.setVisibility(View.GONE);

          ShowDrawDialog(getActivity());
        }
      }


      @Override
      public void onFinishTask(DetailBean bean) {
        DismissDialog();

        if (bean.getResultCode() == 0) {

          homeLayout.setVisibility(View.VISIBLE);

          if (bean.getRepaymentPlan() == null) {
            return;
          }

          mBoolean = false;

          if (mRepaymentAdapter == null) {
            mRepaymentAdapter = new RepaymentAdapter(getActivity(), bean.getRepaymentPlan());
            mListView.setAdapter(mRepaymentAdapter);
          } else {
            mRepaymentAdapter.updateMoreBids(bean.getRepaymentPlan());
          }

          // 隐藏加载更多
          mListView.viewGone();

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


  @Override
  public void onRefresh() {
    DetailInfoData(getArguments().getString("mockId"), FLAG);
  }


  @Override
  public void onLoadMore() {}

}
