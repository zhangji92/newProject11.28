package com.jrd.loan.fragment;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jrd.loan.R;
import com.jrd.loan.adapter.CouponsAdapter;
import com.jrd.loan.api.FinanceApi;
import com.jrd.loan.base.BaseFragment;
import com.jrd.loan.bean.NotUsedCoupon;
import com.jrd.loan.bean.NotUsedCouponBean;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.DialogUtils.OnButtonEventListener;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.XListView;
import com.jrd.loan.widget.XListView.IXListViewListener;

/**
 * 未使用优惠券
 *
 * @author Aaron
 */
public class NotUsedFragment extends BaseFragment implements IXListViewListener {
  private String TYPE = "1";
  private final static int PAGESIZE = 10;
  private int PAGENO = 1;

  private List<NotUsedCoupon> listCoupon;

  private LinearLayout homeLayout;
  private XListView mListView;
  private CouponsAdapter mCouponsAdapter;

  @Override
  protected int getLoadViewId() {
    return R.layout.loan_notused_layout;
  }


  @Override
  protected void initView(View view) {
    homeLayout = (LinearLayout) view.findViewById(R.id.loan_coupons_layoutId);
    homeLayout.setVisibility(View.GONE);

    mListView = (XListView) view.findViewById(R.id.loan_coupons_ListView);
    mListView.setOnItemClickListener(mOnClickListener);
    mListView.setEmptyView(view.findViewById(R.id.nodatalayout));
    mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    mListView.setPullLoadEnable(true);
    mListView.setXListViewListener(this);
  }

  private OnItemClickListener mOnClickListener = new OnItemClickListener() {


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      showDialog(listCoupon.get(position - 1));
    }
  };

  private void showDialog(NotUsedCoupon notUsedCoupon) {
    String btnText = getResources().getString(R.string.loan_close);

    String mHbDesc = notUsedCoupon.getHbDesc();
    mHbDesc = mHbDesc.replaceAll("<br>", "\n");

    DialogUtils.showCouponsDialog(getActivity(), mHbDesc, btnText, false, new OnButtonEventListener() {

      @Override
      public void onConfirm() {}

      @Override
      public void onCancel() {}

    });
  }

  @Override
  protected void initData() {
    NotUsedData(TYPE, PAGESIZE, 1);
  }


  @Override
  public void onRefresh() {
    refreshNotUsedData(TYPE, PAGESIZE);
  }


  @Override
  public void onLoadMore() {
    NotUsedData(TYPE, PAGESIZE, PAGENO);
  }

  private void NotUsedData(String type, int pageSize, int pageNo) {
    FinanceApi.getCouponInfoList(getActivity(), type, pageSize, pageNo, new OnHttpTaskListener<NotUsedCouponBean>() {
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
          this.noNetworkLayoutView = getView().findViewById(R.id.couponsLayout);
          this.noNetworkLayoutView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
          this.noNetworkLayoutView.setVisibility(View.GONE);

          ShowDrawDialog(getActivity());
        }
      }

      @Override
      public void onFinishTask(NotUsedCouponBean bean) {

        onLoad();
        if (PAGENO == 1) {
          DismissDialog();
        }

        if (bean.getResultCode() == 0) {

          homeLayout.setVisibility(View.VISIBLE);


          if (bean.getCouponsList().isEmpty()) {
            return;
          }

          listCoupon = bean.getCouponsList();

          if (listCoupon.size() < 10) {
            mListView.viewGone();
          }

          PAGENO++;
          if (mCouponsAdapter == null) {
            mCouponsAdapter = new CouponsAdapter(getActivity(), listCoupon);
            mListView.setAdapter(mCouponsAdapter);
          } else {
            mCouponsAdapter.updateMoreBids(listCoupon);
          }
        } else {
          ToastUtil.showShort(getActivity(), bean.getResultMsg());
        }

      }
    });
  }

  private void refreshNotUsedData(String type, int pageSize) {
    FinanceApi.getCouponInfoList(getActivity(), type, pageSize, 1, new OnHttpTaskListener<NotUsedCouponBean>() {

      @Override
      public void onTaskError(int resposeCode) {
        onLoad();
      }

      @Override
      public void onStart() {}

      @Override
      public void onFinishTask(NotUsedCouponBean bean) {
        if (bean.getResultCode() == 0) {

          homeLayout.setVisibility(View.VISIBLE);

          listCoupon.clear();


          if (bean.getCouponsList().isEmpty()) {
            mCouponsAdapter.updatePageFirstBid(listCoupon);
            return;
          }

          listCoupon = bean.getCouponsList();

          if (listCoupon.size() < 10) {
            mListView.viewGone();
          }

          PAGENO = 2;
          if (mCouponsAdapter == null) {
            mCouponsAdapter = new CouponsAdapter(getActivity(), listCoupon);
            mListView.setAdapter(mCouponsAdapter);
          } else {
            mCouponsAdapter.updatePageFirstBid(listCoupon);
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
