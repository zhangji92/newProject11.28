package com.jrd.loan.myaccount;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.api.PayApi;
import com.jrd.loan.api.PocketApi;
import com.jrd.loan.api.StatisticsApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.HuoqiInvestBean;
import com.jrd.loan.bean.PaymentAccountBean;
import com.jrd.loan.bean.PocketBean;
import com.jrd.loan.bean.RecordsList;
import com.jrd.loan.constant.Const;
import com.jrd.loan.current.pocket.rehcarge.ExplainActivity;
import com.jrd.loan.current.pocket.rehcarge.IdCheckActivity;
import com.jrd.loan.current.pocket.rehcarge.RechargeAgainActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.umeng.UMengEvent;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.DialogUtils.OnButtonEventListener;
import com.jrd.loan.util.DoubleUtil;
import com.jrd.loan.util.MoneyFormatUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.AnimTextView;
import com.jrd.loan.widget.WindowView;

/**
 * 账户转入
 * 
 * @author Aaron
 */
public class AccountSwitchToActivity extends BaseActivity {
  private WindowView windowView;
  private Context mContext;
  private LinearLayout mLinearLayout;

  private ArrayList<RecordsList> recordsList;

  private TextView annualRateTV;// 年化利率
  private TextView leftInvestAmountTV;// 剩余额度
  private AnimTextView switchToMax;// 剩余额度

  private double balanceDouble; // 账户余额额度

  private TextView rechargeTV;// 充值

  private TextView dayGoinTV; // 每日可赚
  private TextView bankGoinTV; // 银行同期可赚

  private EditText transferAmount;// 输入的金额
  private Button accountBtn;// 立即加入
  private TextView accountTime;// 预计到账时间

  private String investAmount;

  private String shiftToEdit;
  private String annualRate;
  private String leftInvestAmount;

  private String amount = "0";

  @Override
  protected int loadWindowLayout() {
    return R.layout.loan_account_switch_to;
  }

  /**
   * 初始化组建
   */
  @Override
  protected void initViews() {
    mContext = AccountSwitchToActivity.this;

    mLinearLayout = (LinearLayout) findViewById(R.id.loan_CloseOntouch);
    mLinearLayout.setOnClickListener(this.clincListener);

    // 年化利率
    annualRateTV = (TextView) findViewById(R.id.loan_annualRate);

    // 剩余额度
    leftInvestAmountTV = (TextView) findViewById(R.id.loan_leftInvestAmount);

    // 可用余额
    switchToMax = (AnimTextView) findViewById(R.id.loan_switchTo_max);

    dayGoinTV = (TextView) findViewById(R.id.loan_pocket_dayGoinTV);
    dayGoinTV.setText(R.string.loan_amount_default_text);
    bankGoinTV = (TextView) findViewById(R.id.loan_pocket_bankGoinTV);
    bankGoinTV.setText(R.string.loan_amount_default_text);

    // 充值
    rechargeTV = (TextView) findViewById(R.id.loan_recharge_tv);
    rechargeTV.setOnClickListener(this.clincListener);

    // 预计到账时间
    accountTime = (TextView) findViewById(R.id.loan_account_time);

    // 转入的金额
    transferAmount = (EditText) findViewById(R.id.loan_transfer_amount_ed);
    transferAmount.setSelection(transferAmount.getText().length());// 投资金额光标定位在最后边

    // 立即加入
    accountBtn = (Button) findViewById(R.id.loan_account_btn);
    accountBtn.setOnClickListener(this.clincListener);

    obtainFateToady();
  }

  /**
   * 加载数据
   */
  private void initData() {
    shiftToEdit = getIntent().getStringExtra("amount");

    if (!TextUtils.isEmpty(shiftToEdit)) {
      IncomeMethod(shiftToEdit.toString());
      transferAmount.setText(shiftToEdit);
    }

    if (!TextUtils.isEmpty(annualRate)) {
      annualRateTV.setText(new StringBuffer().append("年化利率：").append(annualRate).append("%").toString());
    }

    if (!TextUtils.isEmpty(leftInvestAmount)) {
      leftInvestAmountTV.setText(new StringBuffer().append("剩余额度：").append(leftInvestAmount).append("元").toString());
    }

    accountTime.setText(AccountRollOutActivity.ToDBC(getResources().getString(R.string.loan_ExpectedArrivalTime)));

    // 活期信息数据请求
    RequestData();

    // EditText监听 transferAmount
    ExpectedReturn();
  }

  /**
   * 输入金额监听事件
   */
  private void ExpectedReturn() {
    transferAmount.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence txt, int arg1, int arg2, int arg3) {

        if (txt.toString().length() == 1) {
          if (txt.toString().equals(".")) {
            transferAmount.setText("");
            return;
          }
        }

        if (TextUtils.isEmpty(txt.toString())) {
          ToastUtil.showShort(AccountSwitchToActivity.this, R.string.loan_add_money);
          return;
        } else {
          IncomeMethod(txt.toString());
        }
      }

      @Override
      public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

      @Override
      public void afterTextChanged(Editable edt) {
        String temp = edt.toString();
        int posDot = temp.indexOf(".");
        if (posDot <= 0) {
          if (temp.length() <= 10) {
            return;
          } else {
            edt.delete(9, 10);
            return;
          }
        }
        if (temp.length() - posDot - 1 > 2) {
          edt.delete(posDot + 3, posDot + 4);
        }
      }
    });
  }

  private void IncomeMethod(String money) {
    /**
     * 预计每日可赚=本金*年利率*天数/365
     */

    if (TextUtils.isEmpty(annualRate.toString())) {
      return;
    }

    double dayGoin = Double.valueOf(money) * (Double.valueOf(annualRate) / 100) / 365;

    BigDecimal bg = new BigDecimal(dayGoin);

    dayGoinTV.setText(MoneyFormatUtil.getMoney(String.valueOf(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));

    /**
     * 银行同期可赚：本金×实际天数×0.35%/360
     */

    double bankGoin = Double.valueOf(money) * (0.35 / 100) / 360;

    BigDecimal bankBg = new BigDecimal(bankGoin);

    bankGoinTV.setText(MoneyFormatUtil.getMoney(String.valueOf(bankBg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));
  }

  OnClickListener clincListener = new OnClickListener() {

    @Override
    public void onClick(View view) {
      switch (view.getId()) {
        case R.id.loan_account_btn: // 同意协议并投资
          CloseOntouch();
          showAccountBtn();
          break;
        case R.id.loan_recharge_tv:// 充值
          if (UserInfoPrefs.loadLastLoginUserProfile().idNumberFlag.equals("0")) {// 用户是否身份认证
            startActivity(new Intent(mContext, IdCheckActivity.class));
          } else {
            if (UserInfoPrefs.loadLastLoginUserProfile().quickCardFlag.equals("1")) {
              // 已经帮过快捷卡的用户跳转到再次充值界面
              startActivity(new Intent(mContext, RechargeAgainActivity.class));
            } else {
              Intent intent = new Intent(mContext, ExplainActivity.class);
              startActivity(intent);
            }
          }
          break;
        case R.id.loan_CloseOntouch:
          CloseOntouch();
          break;
      }
    }
  };

  // 立即转入
  private void showAccountBtn() {
    investAmount = transferAmount.getText().toString();

    if (TextUtils.isEmpty(investAmount)) {// 投标金额不能为空
      ToastUtil.showShort(this, R.string.loan_add_money);
      return;
    }

    double bidMoney = Double.parseDouble(investAmount);

    if (bidMoney < 1) {// 转入金额不能小于1元
      ToastUtil.showShort(this, R.string.loan_MinimumAmount);
      return;
    }

    double balance = Double.parseDouble(amount);
    if (bidMoney > balance) {// 输入金额大于可用余额
      showDialog();
      return;
    }

    showAccountData();
  }

  /**
   * 活期信息数据请求
   */
  private void RequestData() {
    PayApi.paymentAccount(this, new OnHttpTaskListener<PaymentAccountBean>() {
      @Override
      public void onTaskError(int resposeCode) {
        DismissDialog();

        windowView.setNoNetworkClick(new OnClickListener() {
          @Override
          public void onClick(View v) {
            RequestData();
          }
        });
      };

      @Override
      public void onStart() {
        ShowDrawDialog(mContext);
      }

      @Override
      public void onFinishTask(PaymentAccountBean bean) {
        DismissDialog();
        if (bean != null && bean.getResultCode() == 0) {
          recordsList = bean.getRecords();
          if (recordsList.isEmpty()) {
            return;
          }

          if (recordsList.get(0).getType().equals("0")) {
            amount = recordsList.get(0).getAmount();
            balanceDouble = Double.parseDouble(DoubleUtil.getMoney(recordsList.get(0).getAmount()));
            switchToMax.playNumber(balanceDouble);
            // switchToMax.playNumber(new
            // StringBuffer().append("可用余额").append(balanceDouble).append("元").toString());
          }

        } else {
          ToastUtil.showShort(mContext, bean.getResultMsg());
          finish();
        }
      }
    });
  }

  // 获取今日利率
  public void obtainFateToady() {
    PocketApi.obtainCurrentPocketFateToday(mContext, new OnHttpTaskListener<PocketBean>() {

      @Override
      public void onStart() {
        ShowDrawDialog(mContext);
      }

      @Override
      public void onTaskError(int resposeCode) {
        windowView.setNoNetworkClick(new OnClickListener() {
          @Override
          public void onClick(View v) {
            obtainFateToady();
          }
        });

        DismissDialog();
      }

      @Override
      public void onFinishTask(PocketBean bean) {
        if (bean != null) {
          if (bean.getResultCode() == 0) {
            annualRate = bean.getAnnualRate();
            leftInvestAmount = bean.getLeftInvestAmount();

            initData();
          } else {
            ToastUtil.showShort(mContext, bean.getResultMsg());
          }
        }
      }
    });
  }

  /**
   * 立即转入数据请求
   */
  private void showAccountData() {
    PayApi.userHuoqiInvest(mContext, "0", Double.valueOf(investAmount), new OnHttpTaskListener<HuoqiInvestBean>() {

      @Override
      public void onTaskError(int resposeCode) {
        windowView.setNoNetworkClick(new OnClickListener() {
          @Override
          public void onClick(View v) {
            showAccountData();
          }
        });

        DismissDialog();
      }

      @Override
      public void onStart() {
        ShowDrawDialog(mContext);
      }

      @Override
      public void onFinishTask(HuoqiInvestBean bean) {
        DismissDialog();
        if (bean != null && bean.getResultCode() == 0) {

          if (bean.getRecords().isEmpty()) {
            return;
          }

          UMengEvent.onEvent(mContext, UMengEvent.EVENT_ID_TRANSFERIN, UMengEvent.EVENT_MODULE_TRANSFERIN_ACCOUNTBALANCE);
          // 用户行为统计接口
          StatisticsApi.getUserBehavior(AccountSwitchToActivity.this, Const.EventName.TRANSFERIN, Const.EventId.TRANSFERIN, null, null, bean.getAmount());

          Intent intent = new Intent(mContext, InvestmentSuccessActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("detail", bean.getDetail());
          intent.putExtra("amount", bean.getAmount());
          intent.putExtra("oprateTime", bean.getOprateTime());
          intent.putExtra("records", bean.getRecords());
          startActivity(intent);
          finish();
        } else {
          Intent intent = new Intent(mContext, InvestmentFailureActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("msg", bean.getResultMsg());
          startActivity(intent);
          // ToastUtil.showShort(mContext, bean.getResultMsg());
          finish();
        }
        
      }
    });
  }

  private void showDialog() {
    DialogUtils.showDialogs(mContext, new OnButtonEventListener() {

      @Override
      public void onConfirm() {
        if (UserInfoPrefs.loadLastLoginUserProfile().idNumberFlag.equals("0")) {// 用户是否身份认证
          startActivity(new Intent(mContext, IdCheckActivity.class));
        } else {
          if (UserInfoPrefs.loadLastLoginUserProfile().quickCardFlag.equals("1")) {
            // 已经帮过快捷卡的用户跳转到再次充值界面
            startActivity(new Intent(mContext, RechargeAgainActivity.class));
          } else {
            Intent intent = new Intent(mContext, ExplainActivity.class);
            startActivity(intent);
          }
        }
      }

      @Override
      public void onCancel() {
        return;
      }

    });
  }

  // 关闭软键盘
  private void CloseOntouch() {
    View view = getWindow().peekDecorView();
    if (view != null) {
      InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  @Override
  protected void initTitleBar() {
    windowView = (WindowView) findViewById(R.id.windowView);
    windowView.setTitle(R.string.loan_current_pocket_income);
    windowView.setVisibility(View.VISIBLE);
    windowView.setLeftButtonClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

}
