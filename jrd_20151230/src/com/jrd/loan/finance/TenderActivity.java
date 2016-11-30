package com.jrd.loan.finance;

import java.text.DecimalFormat;
import java.text.ParseException;

import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.api.FinanceApi;
import com.jrd.loan.api.StatisticsApi;
import com.jrd.loan.api.WebApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.bean.AccountBean;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.Coupon;
import com.jrd.loan.bean.CouponBean;
import com.jrd.loan.bean.ProjectList;
import com.jrd.loan.constant.Const;
import com.jrd.loan.current.pocket.rehcarge.ExplainActivity;
import com.jrd.loan.current.pocket.rehcarge.RechargeAgainActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.umeng.UMengEvent;
import com.jrd.loan.util.DateUtil;
import com.jrd.loan.util.DigitUtil;
import com.jrd.loan.util.NetUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

/**
 * 立即投标
 * 
 * @author Jacky
 */
public class TenderActivity extends BaseActivity {

  private ProjectList projectList;
  private EditText etBidMoney;
  private double accountBal; // 用户的账户余额
  private CheckBox cbAgreeProtocol;
  private TextView tvCoupon;
  private Coupon coupon;
  private Coupon preCoupon;
  private TextView futureProfit; // 预期收益

  @Override
  protected int loadWindowLayout() {
    return R.layout.loan_tender_at_once_layout;
  }

  @Override
  protected void initViews() {
    this.projectList = (ProjectList) getIntent().getSerializableExtra("projectList");

    btnRecharge = (Button) findViewById(R.id.btnRecharge);
    btnRecharge.setOnClickListener(this.btnClickListener);

    // 预期收益
    this.futureProfit = (TextView) findViewById(R.id.futureProfit);

    this.etBidMoney = (EditText) findViewById(R.id.etBidMoney);
    this.etBidMoney.setText(DigitUtil.getMoneys(String.valueOf(this.projectList.getMinInvestmentAmount())));
    this.etBidMoney.setSelection(this.etBidMoney.getText().toString().length());

    this.futureProfit.setText(calBal((long) this.projectList.getMinInvestmentAmount()));

    this.etBidMoney.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence txt, int arg1, int arg2, int arg3) {
        if (!TextUtils.isEmpty(txt)) {// 输入不为空
          long money = Long.parseLong(txt.toString());

          if (coupon != null) {
            preCoupon = coupon;
            coupon = null;
            tvCoupon.setText(R.string.loan_choose_coupon_please);
            tvCoupon.append("  ");
          }

          futureProfit.setText(calBal(money));
        } else {
          futureProfit.setText(" ");
          preCoupon = coupon;
          coupon = null;

          if (!tvCoupon.getText().toString().equals(getString(R.string.loan_use_coupon))) {
            tvCoupon.setText(R.string.loan_choose_coupon_please);
            tvCoupon.append("  ");
          }
        }
      }

      @Override
      public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

      @Override
      public void afterTextChanged(Editable txt) {

      }
    });

    this.cbAgreeProtocol = (CheckBox) findViewById(R.id.cbAgreeProtocol);
    this.cbAgreeProtocol.setChecked(true);

    // 项目名称和编码
    TextView projectNameAndCode = (TextView) findViewById(R.id.projectNameAndCode);
    projectNameAndCode.setText(this.projectList.getProjectName());
    projectNameAndCode.append("   ");
    projectNameAndCode.append(this.projectList.getProjectCode());

    // 项目名称
    TextView projectName = (TextView) findViewById(R.id.projectName);
    projectName.setText(this.projectList.getBrandName());

    // 融资产品类型
    ImageView proType = (ImageView) findViewById(R.id.proType);
    String type = this.projectList.getType();

    if (type.equals("100")) {// 企
      proType.setImageResource(R.drawable.loan_pro_enterprises);
    } else if (type.equals("200") || type.equals("600")) {// 房
      proType.setImageResource(R.drawable.loan_pro_real);
    } else if (type.equals("300")) {// 供
      proType.setImageResource(R.drawable.loan_pro_type);
    } else if (type.equals("400")) {// 核
      proType.setImageResource(R.drawable.loan_pro_nuclear);
    } else if (type.equals("500")) {// 消
      proType.setImageResource(R.drawable.loan_pro_fire);
    } else {
      proType.setVisibility(View.GONE);
    }

    btnBidNow = (Button) findViewById(R.id.btnBidNow);
    btnBidNow.setOnClickListener(this.btnClickListener);
    btnBidNow.setVisibility(View.GONE);

    // 年利率
    TextView tvYearRate = (TextView) findViewById(R.id.tvYearRate);
    tvYearRate.setText(R.string.loan_year_rate_title);
    tvYearRate.append("\n");

    StringBuffer annualRateBuffer = new StringBuffer();
    annualRateBuffer.append("<font color='#EA6D8D'><big><big><big>");
    annualRateBuffer.append(this.projectList.getAnnualRate());
    annualRateBuffer.append("</big></big></big></font>");
    tvYearRate.append(Html.fromHtml(annualRateBuffer.toString()));

    if (!(TextUtils.isEmpty(projectList.getRewardRate()) || projectList.getRewardRate().equals("0") || projectList.getRewardRate().equals("0.0"))) {
      annualRateBuffer = new StringBuffer();
      annualRateBuffer.append("<font color='#EA6D8D'><big><big><big>+");
      annualRateBuffer.append(this.projectList.getRewardRate());
      annualRateBuffer.append("</big></big></big></font>");
      tvYearRate.append(Html.fromHtml(annualRateBuffer.toString()));
      tvYearRate.append("%");
    } else {
      tvYearRate.append("%");
    }

    // 项目期限
    TextView projectPeriod = (TextView) findViewById(R.id.projectPeriod);
    projectPeriod.setText(R.string.loan_Project_duration);
    projectPeriod.append("\n");

    StringBuffer periodBuffer = new StringBuffer();
    periodBuffer.append("<big><big><big>");
    periodBuffer.append(DigitUtil.getMonth(this.projectList.getInvestmentPeriod()));
    periodBuffer.append("</big></big></big>");
    projectPeriod.append(Html.fromHtml(periodBuffer.toString()));
    projectPeriod.append(getString(R.string.loan_a_month));

    // 可投金额
    TextView canInvestMoney = (TextView) findViewById(R.id.canInvestMoney);
    canInvestMoney.setText(R.string.loan_amount_investment);
    canInvestMoney.append("\n");

    StringBuffer canInvestBuffer = new StringBuffer();
    canInvestBuffer.append("<big><big><big>");
    canInvestBuffer.append(DigitUtil.getMoneys(this.projectList.getInvestmentAmount()));
    canInvestBuffer.append("</big></big></big>");

    if (Double.parseDouble(this.projectList.getInvestmentAmount()) >= 10000) {
      canInvestBuffer.append(getString(R.string.loan_wan_text));
    } else {
      canInvestBuffer.append(getString(R.string.loan_yuan_text));
    }

    canInvestMoney.append(Html.fromHtml(canInvestBuffer.toString()));

    // 优惠券
    this.tvCoupon = (TextView) findViewById(R.id.tvCoupon);

    LinearLayout couponLayout = (LinearLayout) findViewById(R.id.couponLayout);
    couponLayout.setOnClickListener(this.btnClickListener);

    // 投资协议
    TextView tvProtocol = (TextView) findViewById(R.id.tvProtocol);
    tvProtocol.setOnClickListener(this.btnClickListener);

    // 获取账户余额
    this.getMyAccount();

    setNoNetworkClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        getMyAccount();
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (AppInfoPrefs.getRecharge()) {
      AppInfoPrefs.setRecharge(false);
      // 获取账户余额
      this.getMyAccount();
    }
  }

  // 收益计算 = ( ( 投资期限（日）/ 365 )* (标的收益率/100) * 实际投资金额
  private String calBal(long money) {
    double profit = 0;

    try {
      int dateInterval = DateUtil.daysBetween(new java.util.Date(), DateUtil.getDate(projectList.getRepaymentEndDate()));

      String rewindRate = projectList.getRewardRate();

      if (TextUtils.isEmpty(rewindRate)) {
        profit = (dateInterval / 365.0) * (Double.parseDouble(projectList.getAnnualRate()) / 100) * money;
      } else {
        profit = (dateInterval / 365.0) * ((Double.parseDouble(projectList.getAnnualRate()) + Double.parseDouble(rewindRate)) / 100) * money;
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return new DecimalFormat("#.##").format(profit);
  }

  // 我的账户信息接口获取可用余额
  private void getMyAccount() {
    BankApi.GetMyAccount(this, new OnHttpTaskListener<AccountBean>() {

      @Override
      public void onTaskError(int resposeCode) {
        DismissDialog();
      }

      @Override
      public void onStart() {
        ShowDrawDialog(TenderActivity.this);
      }

      @Override
      public void onFinishTask(AccountBean bean) {
        DismissDialog();

        if (bean.getResultCode() == 0 && bean.getUserInfo() != null) {

          windowView.setVisibility(View.VISIBLE);
          btnBidNow.setVisibility(View.VISIBLE);

          TextView balance = (TextView) findViewById(R.id.balance);
          balance.setText(DigitUtil.formatMoney(bean.getUserInfo().getAccountBalance()));
          accountBal = Double.parseDouble(balance.getText().toString());
        } else {
          ToastUtil.showShort(TenderActivity.this, bean.getResultMsg());
        }
      }
    });
  }

  private OnClickListener btnClickListener = new OnClickListener() {
    @Override
    public void onClick(View view) {
      switch (view.getId()) {
        case R.id.couponLayout: // 优惠券
          showCouponScreen();
          break;
        case R.id.btnBidNow: // 确定投标
          confirmBid();
          break;
        case R.id.tvProtocol: // 投资协议
          showUserProtocolScreen();
          break;
        case R.id.btnRecharge: // 充值
          showRechargeScreen();
          break;
      }
    }
  };
  private Button btnRecharge;
  private Button btnBidNow;
  private WindowView windowView;

  private void showRechargeScreen() {
    if (UserInfoPrefs.loadLastLoginUserProfile().quickCardFlag.equals("1")) {
      // 已经帮过快捷卡的用户跳转到再次充值界面
      startActivity(new Intent(this, RechargeAgainActivity.class));
    } else {
      Intent intent = new Intent(this, ExplainActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      intent.putExtra("mType", UserInfoPrefs.loadLastLoginUserProfile().boundCardFlag);
      startActivity(intent);
    }
  }

  private void showUserProtocolScreen() {
    Intent intent = new Intent(this, WebViewActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra("htmlUrl", JrdConfig.getBaseUrl() + WebApi.FinanceApi.GETAGREEMENTINFO);
    intent.putExtra("htmlTitle", getString(R.string.loan_bid_protocol_title));
    startActivity(intent);
  }

  private void confirmBid() {
    if (!NetUtil.hasMobileNet()) {// 无网络
      ToastUtil.showShort(this, R.string.loan_no_network_tips);

      return;
    }

    String bidMoneyStr = this.etBidMoney.getText().toString();

    if (TextUtils.isEmpty(bidMoneyStr)) {// 投标金额不能为空
      ToastUtil.showShort(this, R.string.loan_add_money);
      return;
    }

    int bidMoney = Integer.parseInt(bidMoneyStr);

    if (bidMoney == 0) {// 投资金额不能为0
      ToastUtil.showShort(this, R.string.loan_bid_money_0);
      return;
    }

    if (bidMoney % 100 != 0) {// 投标金额必须100整数倍
      ToastUtil.showShort(this, R.string.loan_bid_money_must_100_times);
      return;
    }

    if (bidMoney > 0 && (accountBal == 0 || accountBal == 0.0)) {// 您的账户余额为{0}元，小于您输入的投资金额
      ToastUtil.showShort(this, getString(R.string.loan_bid_account_bal_0, String.valueOf(accountBal)));
      return;
    }

    if (bidMoney > accountBal) {// 您的账户余额为{0}元，小于您输入的投资金额
      ToastUtil.showShort(this, getString(R.string.loan_bid_account_bal_less_money, String.valueOf(accountBal)));
      return;
    }

    double investmentAmount = Double.parseDouble(this.projectList.getInvestmentAmount());
    if (bidMoney > investmentAmount) {// 投资金额必须小于可投金额{0}元
      if (investmentAmount < 10000) {
        ToastUtil.showShort(this, getString(R.string.loan_bid_bid_bal_less_money, DigitUtil.getMoneys(this.projectList.getInvestmentAmount())));
      } else {
        ToastUtil.showShort(this, getString(R.string.loan_bid_bid_bal_less_money, DigitUtil.getMoney(this.projectList.getInvestmentAmount())));
      }
      return;
    }

    if (!this.cbAgreeProtocol.isChecked()) {// 没有选中投资协议
      ToastUtil.showShort(this, R.string.loan_choose_bid_protocol);
      return;
    }

    FinanceApi.confirmBid(this, this.projectList.getProjectId(), UserInfoPrefs.getUserId(), this.etBidMoney.getText().toString(), this.coupon != null ? "" + this.coupon.getHbEntId() : null, new OnHttpTaskListener<BaseBean>() {
      @Override
      public void onTaskError(int resposeCode) {
        DismissDialog();
      }

      @Override
      public void onStart() {
        ShowProDialog(TenderActivity.this);
      }

      @Override
      public void onFinishTask(BaseBean bean) {
        DismissDialog();

        if (bean.getResultCode() == 0) {// 投资成功
          UMengEvent.onEvent(TenderActivity.this, UMengEvent.EVENT_ID_INVEST);

          // 用户行为统计接口
          StatisticsApi.getUserBehavior(TenderActivity.this, Const.EventName.INVEST, Const.EventId.INVEST, projectList.getProjectId(), projectList.getFirstFlag() + "", etBidMoney.getText().toString());


          showBidResultScreen();
        } else {// 投资失败
          ToastUtil.showShort(TenderActivity.this, bean.getResultMsg());
        }
      }
    });
  }

  private void showBidResultScreen() {
    Intent intent = new Intent(this, BidSuccessActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
  }

  // 优惠券
  private void showCouponScreen() {
    if (TextUtils.isEmpty(this.etBidMoney.getText().toString())) {// 投标金额不能为空
      ToastUtil.showShort(this, R.string.loan_add_money);
      return;
    }

    if (this.projectList.getIsNovice().equals("1")) {// 新手标暂不能使用优惠券
      ToastUtil.showShort(this, R.string.loan_new_hand_bid_cannot_yhq);
      return;
    }

    double investPeriod = Double.parseDouble(this.projectList.getInvestmentPeriod());
    if (investPeriod < 3) {// 标的期限小于3个月不能使用优惠券
      ToastUtil.showShort(this, R.string.loan_cannot_use_yhq);
      return;
    }

    this.getCouponList();
  }

  private void getCouponList() {
    String projectId = this.projectList.getProjectId();
    String investMoney = etBidMoney.getText().toString();
    String investmentPeriod = this.projectList.getInvestmentPeriod();
    String isNovice = this.projectList.getIsNovice();

    FinanceApi.obtainCouponList(this, projectId, investMoney, investmentPeriod, isNovice, new OnHttpTaskListener<CouponBean>() {
      @Override
      public void onTaskError(int resposeCode) {}

      @Override
      public void onStart() {}

      @Override
      public void onFinishTask(CouponBean bean) {

        if (bean.getResultCode() == 0) {// 投标成功
          if (bean.getPackets() == null || bean.getPackets().isEmpty() || !bean.getPackets().get(0).isAvailable()) {
            ToastUtil.showShort(TenderActivity.this, R.string.loan_not_coupon);
            tvCoupon.setText(getResources().getString(R.string.loan_not_coupon));
          } else {
            Intent intent = new Intent(TenderActivity.this, CouponActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("mPackets", bean.getPackets());
            intent.putExtra("coupon", coupon == null ? preCoupon : coupon);
            startActivityForResult(intent, 1000);
          }
        } else {
          ToastUtil.showShort(TenderActivity.this, bean.getResultMsg());
        }
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == 1000 && data != null) {
      this.coupon = (Coupon) data.getSerializableExtra("coupon");

      if (this.coupon.getHbType().equals("1")) {// 现金券
        tvCoupon.setText(getString(R.string.loan_coupon_choose, DigitUtil.getMoneys("" + this.coupon.getBagVal())));
        tvCoupon.append("  ");
      } else {// 加息券
        tvCoupon.setText(getString(R.string.loan_coupon_percent_choose, DigitUtil.getMoneys("" + this.coupon.getBagVal())));
        tvCoupon.append("%加息券  ");
      }
    }
  }

  @Override
  protected void initTitleBar() {
    windowView = (WindowView) findViewById(R.id.windowView);

    windowView.setVisibility(View.INVISIBLE);
    // 立即投标
    windowView.setTitle(R.string.loan_bid_now);

    // 返回按钮单击事件
    windowView.setLeftButtonClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }
}
