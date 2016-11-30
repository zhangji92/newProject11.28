package com.jrd.loan.fragment;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.api.PocketApi;
import com.jrd.loan.api.ProjectInfoApi;
import com.jrd.loan.base.BaseFragment;
import com.jrd.loan.bean.BannerInfoBean;
import com.jrd.loan.bean.BannerList;
import com.jrd.loan.bean.PocketBean;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.pulltorefresh.library.PullToRefreshBase;
import com.jrd.loan.pulltorefresh.library.PullToRefreshScrollView;
import com.jrd.loan.util.NetUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.MyBanner;
import com.jrd.loan.widget.RoundProgressBar;

/**
 * 活期口袋(首页)
 * 
 * @author Jacky
 */
public class BoutiqueCommendFragment extends BaseFragment {
  private TextView canInvestMoney; // 可投金额
  private ArrayList<BannerList> imageList; // 滑动的图片集合
  private LinearLayout currentPocketLayout;
  private View noNetworkLayoutView;
  private OnCurrentPocketListener currentPocketListener;
  private PullToRefreshScrollView pullToRefreshView;
  private MyBanner mViewPager;
  private TextView rateToady;
  private ImageView bannerImage;
  private boolean isRefresh;// 是否是下拉刷新

  public void setOnCurrentPocketListener(OnCurrentPocketListener currentPocketListener) {
    this.currentPocketListener = currentPocketListener;
  }

  private void showCurrentPocketScreen() {
    this.currentPocketListener.onShowCurrentPocketScreen();
  }

  @Override
  protected int getLoadViewId() {
    return R.layout.loan_boutique_commend_layout;
  }

  @Override
  protected void initView(View view) {

    pullToRefreshView = (PullToRefreshScrollView) view.findViewById(R.id.loan_main_refreshscrollview);
    pullToRefreshView.setPullToRefreshEnabled(true);
    pullToRefreshView.setPullToRefreshOverScrollEnabled(false);
    pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
      @Override
      public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
        isRefresh = true;
        obtainFateToady();
      }
    });

    bannerImage = (ImageView) view.findViewById(R.id.banner_iamge);
    bannerImage.setVisibility(View.GONE);
    // Banner控件
    mViewPager = (MyBanner) view.findViewById(R.id.banner);
    // 可投金额
    this.canInvestMoney = (TextView) view.findViewById(R.id.canInvestMoney);

    this.initProSize(view);

    // 设置今日利率
    this.rateToady = (TextView) view.findViewById(R.id.rateToady);
    this.currentPocketLayout = (LinearLayout) view.findViewById(R.id.currentPocketLayout);
    this.currentPocketLayout.setVisibility(View.GONE);

    // 立即投标
    Button btnBidAtOnce = (Button) view.findViewById(R.id.btnBidNow);
    btnBidAtOnce.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        showCurrentPocketScreen();
      }
    });
  }

  @Override
  protected void initData() {
    // 获取今日利率
    this.obtainFateToady();
  }

  private void initProSize(View view) {
    this.noNetworkLayoutView = view.findViewById(R.id.noNetworkLayout);
    this.noNetworkLayoutView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    RoundProgressBar circleProgress = (RoundProgressBar) view.findViewById(R.id.circlePro);
    int proSize = (int) (getResources().getDisplayMetrics().widthPixels * 0.53);

    LayoutParams layoutParams = new LayoutParams(proSize, proSize);
    layoutParams.topMargin = (int) getResources().getDimension(R.dimen.loan_current_pocket_margin_top);
    layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.loan_round_pro_margin_bottom);
    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
    circleProgress.setLayoutParams(layoutParams);
  }

  private void SetBannerData() {
    mViewPager.setData(imageList);
    mViewPager.setDurtion(4.0f);
  }

  // 查询广告位banner信息
  private void getIdxBannerInfo() {
    ProjectInfoApi.getIdxBannerInfoNew(getActivity(), new OnHttpTaskListener<BannerInfoBean>() {
      @Override
      public void onStart() {}

      @Override
      public void onTaskError(int resposeCode) {
        pullToRefreshView.onRefreshComplete();
        DismissDialog();
      }

      @Override
      public void onFinishTask(BannerInfoBean bean) {
        DismissDialog();
        pullToRefreshView.onRefreshComplete();
        if (bean != null && bean.getResultCode() == 0) {
          currentPocketLayout.setVisibility(View.VISIBLE);
          if (bean.getBannerList() != null && bean.getBannerList().size() > 0) {
            bannerImage.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
            imageList = bean.getBannerList();
            SetBannerData();
          } else {
            bannerImage.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
          }
        } else {
          if (bean != null) {
            ToastUtil.showShort(getActivity(), bean.getResultMsg());
          }
        }
      }
    });
  }

  // 获取今日利率
  public void obtainFateToady() {
    PocketApi.obtainCurrentPocketFateToday(getActivity(), new OnHttpTaskListener<PocketBean>() {

      @Override
      public void onStart() {
        noNetworkLayoutView.setVisibility(View.GONE);

        if (!NetUtil.hasMobileNet()) {
          this.showNetworkExp();
          return;
        }
        if (!isRefresh) {
          ShowDrawDialog(getActivity());
        }
      }

      @Override
      public void onTaskError(int resposeCode) {
        this.showNetworkExp();
        DismissDialog();
      }

      @Override
      public void onFinishTask(PocketBean bean) {
        if (bean != null) {
          if (bean.getResultCode() == 0) {
            rateToady.setText(bean.getAnnualRate());

            // 设置投资余额
            canInvestMoney.setText(String.format("可投金额:%s", bean.getLeftInvestAmount()));
          } else {
            ToastUtil.showShort(getActivity(), bean.getResultMsg());
          }
        }
        // 获取顶部banner
        getIdxBannerInfo();
      }

      private void showNetworkExp() {
        noNetworkLayoutView.setVisibility(View.VISIBLE);
        noNetworkLayoutView.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            initData();
          }
        });
        currentPocketLayout.setVisibility(View.GONE);
        pullToRefreshView.onRefreshComplete();
      }
    });
  }

  public interface OnCurrentPocketListener {
    void onShowCurrentPocketScreen();
  }
}
