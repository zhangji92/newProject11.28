package com.jrd.loan.fragment;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.base.BaseFragment;
import com.jrd.loan.bean.AccountBean;
import com.jrd.loan.bean.PersonalInfo;
import com.jrd.loan.current.pocket.CurrentPocketActivity;
import com.jrd.loan.current.pocket.rehcarge.ExplainActivity;
import com.jrd.loan.current.pocket.rehcarge.IdCheckActivity;
import com.jrd.loan.current.pocket.rehcarge.RechargeAgainActivity;
import com.jrd.loan.current.pocket.rehcarge.WithdrawActivity;
import com.jrd.loan.current.pocket.rehcarge.WithdrawNewActivity;
import com.jrd.loan.finance.InvestRecordActivity;
import com.jrd.loan.myInfomeation.PersonalActivity;
import com.jrd.loan.myInfomeation.SetTransactionPswActivity;
import com.jrd.loan.myaccount.CouponsActivity;
import com.jrd.loan.myaccount.MyAssetsAct;
import com.jrd.loan.myaccount.MyBankCardActivity;
import com.jrd.loan.myaccount.ReturnMoneyAct;
import com.jrd.loan.myaccount.TradeAct;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.pulltorefresh.library.PullToRefreshBase;
import com.jrd.loan.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jrd.loan.pulltorefresh.library.PullToRefreshScrollView;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.DoubleUtil;
import com.jrd.loan.util.NetUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.AnimTextView;

public class MyAccountFragment extends BaseFragment implements OnClickListener {

  private PullToRefreshScrollView pullToRefreshView;
  private LinearLayout layoutId;
  private RelativeLayout verifyLayout;
  // private RoundImageView headImage;
  private TextView phoneTV; // 手机号
  private AnimTextView earningTV; // 昨日收益
  private AnimTextView balanceTV; // 账户余额
  private TextView contactTV; // 电话
  private LinearLayout couponLaout;
  private boolean isRefresh;
  private double earningDouble; // 昨日收益额度
  private double balanceDouble; // 账户余额额度
  // 获取persoalInfo实例
  private PersonalInfo info;

  /**
   * 拼接银行卡账号字符串
   */
  public static String getProtectedMobile(String phoneNumber) {
    if (phoneNumber.isEmpty() || phoneNumber.length() < 11) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    builder.append(phoneNumber.subSequence(0, 3));
    builder.append("****");
    builder.append(phoneNumber.subSequence(7, 11));
    return builder.toString();
  }

  @Override
  protected int getLoadViewId() {
    return R.layout.loan_activity_my_account;
  }

  @Override
  protected void initView(View view) {

    layoutId = (LinearLayout) view.findViewById(R.id.loan_account_layoutId);
    layoutId.setVisibility(View.INVISIBLE);

    pullToRefreshView = (PullToRefreshScrollView) view.findViewById(R.id.loan_account_refreshscrollview);
    pullToRefreshView.setPullToRefreshEnabled(true);
    pullToRefreshView.setPullToRefreshOverScrollEnabled(false);
    pullToRefreshView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
      @Override
      public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
        isRefresh = true;
        requestData();
      }
    });

    RelativeLayout infoLayout = (RelativeLayout) view.findViewById(R.id.loan_my_account_info_layout);
    infoLayout.setOnClickListener(this);

    verifyLayout = (RelativeLayout) view.findViewById(R.id.loan_my_account_no_verify_layout);
    verifyLayout.setOnClickListener(this);

    // headImage = (RoundImageView)
    // view.findViewById(R.id.loan_my_account_head_iamge);
    phoneTV = (TextView) view.findViewById(R.id.loan_my_account_phone_tv);

    // 昨日收益
    earningTV = (AnimTextView) view.findViewById(R.id.loan_my_account_earnings_tv);

    // 账户余额
    balanceTV = (AnimTextView) view.findViewById(R.id.loan_my_account_balance_tv);

    // 客服电话
    contactTV = (TextView) view.findViewById(R.id.loan_tv_my_account_contact_tv);
    contactTV.setOnClickListener(this);

    // 充值
    TextView rechargeTV = (TextView) view.findViewById(R.id.loan_my_account_recharge_tv);
    rechargeTV.setOnClickListener(this);

    // 提现
    TextView withdrawTV = (TextView) view.findViewById(R.id.loan_my_account_withdraw_tv);
    withdrawTV.setOnClickListener(this);

    // 活期口袋
    LinearLayout pocketLayout = (LinearLayout) view.findViewById(R.id.loan_my_account_pocket_layout);
    pocketLayout.setOnClickListener(this);

    // 我的资产
    LinearLayout assetlayout = (LinearLayout) view.findViewById(R.id.loan_my_account_asset_layout);
    assetlayout.setOnClickListener(this);

    // 投资记录
    LinearLayout investLayout = (LinearLayout) view.findViewById(R.id.loan_my_account_invest_layout);
    investLayout.setOnClickListener(this);

    // 回款计划
    LinearLayout planLayout = (LinearLayout) view.findViewById(R.id.loan_my_account_plan_layout);
    planLayout.setOnClickListener(this);

    // 交易记录
    LinearLayout tradeLayout = (LinearLayout) view.findViewById(R.id.loan_my_account_trade_layout);
    tradeLayout.setOnClickListener(this);

    // 优惠券
    couponLaout = (LinearLayout) view.findViewById(R.id.loan_my_account_coupon_layout);
    couponLaout.setOnClickListener(this);

    // 银行卡
    LinearLayout bankCardLayout = (LinearLayout) view.findViewById(R.id.loan_my_account_bankcard_layout);
    bankCardLayout.setOnClickListener(this);
  }

  @Override
  protected void initData() {
    if (AppInfoPrefs.getToCoupon()) {
      couponLaout.performClick();
      AppInfoPrefs.setIntentToCoupon(false);
    }

    requestData();
  }

  /**
   * 获取账户信息
   */
  private void requestData() {
    BankApi.GetMyAccount(getActivity(), new OnHttpTaskListener<AccountBean>() {
      private View noNetworkLayoutView;

      @Override
      public void onTaskError(int resposeCode) {
        ShowNoNetWorkView();
        DismissDialog();
        if (isRefresh) {
          isRefresh = false;
        }
        pullToRefreshView.onRefreshComplete();
      }

      @Override
      public void onStart() {
        this.noNetworkLayoutView = getView().findViewById(R.id.noNetworkLayout);
        this.noNetworkLayoutView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.noNetworkLayoutView.setVisibility(View.GONE);
        if (!NetUtil.hasMobileNet()) {
          ShowNoNetWorkView();
          return;
        }

        if (!isRefresh) {
          ShowDrawDialog(getActivity());
        }
      }

      private void ShowNoNetWorkView() {
        this.noNetworkLayoutView.setVisibility(View.VISIBLE);
        this.noNetworkLayoutView.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            initData();
          }
        });
        layoutId.setVisibility(View.INVISIBLE);
      }

      @Override
      public void onFinishTask(AccountBean bean) {
        pullToRefreshView.onRefreshComplete();
        if (isRefresh) {
          isRefresh = false;
        }

        if (bean.getResultCode() == 0 && bean.getUserInfo() != null) {
          layoutId.setVisibility(View.VISIBLE);

          PersonalInfo info = new PersonalInfo();
          info.userName = bean.getUserName();
          info.idNumberInfo = bean.getIdNumberInfo();
          info.boundCardFlag = bean.getBoundCardFlag();
          info.transPwdFlag = bean.getTransPwdFlag();
          info.passwordInfo = bean.getPasswordInfo();
          info.quickCardFlag = bean.getQuickCardFlag();

          if (bean.getIdNumberInfo().equals("")) {
            verifyLayout.setVisibility(View.VISIBLE);
            info.idNumberFlag = "0";
          } else {
            verifyLayout.setVisibility(View.GONE);
            info.idNumberFlag = "1";
          }

          UserInfoPrefs.saveUserProfileInfo(info);

          phoneTV.setText(getProtectedMobile(bean.getUserInfo().getMobileNumber()));

          if (!TextUtils.isEmpty(bean.getUserInfo().getYesterdayEarnings())) {
            earningDouble = Double.parseDouble(DoubleUtil.getMoney(bean.getUserInfo().getYesterdayEarnings()));

            // 昨日收益
            earningTV.playNumber(earningDouble);
          }

          if (!TextUtils.isEmpty(bean.getUserInfo().getAccountBalance())) {
            balanceDouble = Double.parseDouble(DoubleUtil.getMoney(bean.getUserInfo().getAccountBalance()));

            // 账户余额
            balanceTV.playNumber(balanceDouble);
          }

          AppInfoPrefs.setWithDraw(false);
        } else {
          ToastUtil.showShort(getActivity(), bean.getResultMsg());
        }

        DismissDialog();
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    info = UserInfoPrefs.loadLastLoginUserProfile();

    // 未设置
    if (info.idNumberFlag != null && info.idNumberFlag.equals("1")) {
      verifyLayout.setVisibility(View.GONE);
    } else {
      verifyLayout.setVisibility(View.VISIBLE);
    }

    if (AppInfoPrefs.getWithDraw()) {
      requestData();
    }

    if (AppInfoPrefs.getRecharge()) {
      AppInfoPrefs.setRecharge(false);
      requestData();
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.loan_my_account_info_layout:// 个人信息
        Intent myInfoIntent = new Intent(getActivity(), PersonalActivity.class);
        startActivity(myInfoIntent);
        break;
      case R.id.loan_my_account_no_verify_layout:// 未验证
        Intent IdCheckIntent = new Intent(getActivity(), IdCheckActivity.class);
        startActivity(IdCheckIntent);
        break;
      case R.id.loan_my_account_recharge_tv:// 充值
        info = UserInfoPrefs.loadLastLoginUserProfile();
        if (info.idNumberFlag != null && info.idNumberFlag.equals("1")) {// 判断用户是否重新绑定过快捷卡 1是已经重新绑定过
                                                                         // 0是没有
          if (info.quickCardFlag.equals("1")) {
            // 已经帮过快捷卡的用户跳转到再次充值界面
            startActivity(new Intent(getActivity(), RechargeAgainActivity.class));
          } else {
            Intent intent = new Intent(getActivity(), ExplainActivity.class);
            intent.putExtra("mType", info.boundCardFlag);
            startActivity(intent);
          }
        } else {
          startActivity(new Intent(getActivity(), IdCheckActivity.class));
        }
        break;
      case R.id.loan_my_account_withdraw_tv:// 提现
        info = UserInfoPrefs.loadLastLoginUserProfile();
        if (info.idNumberFlag != null && info.idNumberFlag.equals("1")) {
          if (info.transPwdFlag.equals("0")) {// 未设置交易密码
            startActivity(new Intent(getActivity(), SetTransactionPswActivity.class));
          } else {// 已设置交易密码
            // info.quickCardFlag = "1";

            if (info.quickCardFlag.equals("1")) {// 新用户已绑定快捷卡
              startActivity(new Intent(getActivity(), WithdrawNewActivity.class));
            } else {// 未绑卡
              startActivity(new Intent(getActivity(), WithdrawActivity.class));
            }
          }
        } else {
          startActivity(new Intent(getActivity(), IdCheckActivity.class));
        }
        break;
      case R.id.loan_my_account_pocket_layout:// 活期口袋
        startActivity(new Intent(getActivity(), CurrentPocketActivity.class));
        break;
      case R.id.loan_my_account_asset_layout:// 我的资产
        Intent assetIntent = new Intent(getActivity(), MyAssetsAct.class);
        startActivity(assetIntent);
        break;
      case R.id.loan_my_account_invest_layout:// 投资记录
        Intent investIntent = new Intent(getActivity(), InvestRecordActivity.class);
        investIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(investIntent);
        break;
      case R.id.loan_my_account_plan_layout:// 回款计划
        Intent planIntent = new Intent(getActivity(), ReturnMoneyAct.class);
        startActivity(planIntent);
        break;
      case R.id.loan_my_account_trade_layout:// 交易记录
        Intent tradeIntent = new Intent(getActivity(), TradeAct.class);
        startActivity(tradeIntent);
        break;
      case R.id.loan_my_account_coupon_layout:// 优惠券
        Intent couponIntent = new Intent(getActivity(), CouponsActivity.class);
        couponIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(couponIntent);
        break;
      case R.id.loan_my_account_bankcard_layout:// 银行卡
        Intent bankIntent = new Intent(getActivity(), MyBankCardActivity.class);
        startActivity(bankIntent);
        break;
      case R.id.loan_tv_my_account_contact_tv:
        String tel = contactTV.getText().toString().replace("-", "");
        // 用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
        startActivity(intent);
        break;
    }
  }

  @Override
  public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    if (!hidden) {// 显示

      if (layoutId != null && layoutId.getVisibility() == View.INVISIBLE) {
        requestData();
        return;
      }
      if (earningTV != null && balanceTV != null) {
        earningTV.playNumber(earningDouble);
        balanceTV.playNumber(balanceDouble);
      }
    }
  }
}
