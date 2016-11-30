package com.jrd.loan.myaccount;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.SplashActivity;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.adapter.PaymentPopWinAdapter;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.api.PayApi;
import com.jrd.loan.api.PocketApi;
import com.jrd.loan.api.StatisticsApi;
import com.jrd.loan.api.WebApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.bean.CostBean;
import com.jrd.loan.bean.HuoqiTakeoutBean;
import com.jrd.loan.bean.MyPocketBean;
import com.jrd.loan.bean.RecordsList;
import com.jrd.loan.constant.Const;
import com.jrd.loan.current.pocket.rehcarge.BindBankCardActivity;
import com.jrd.loan.myInfomeation.SetTransactionPswActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.umeng.UMengEvent;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.DialogUtils.OnPaymentAccountListener;
import com.jrd.loan.util.KeyBoardUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

/**
 * 账户转出
 *
 * @author Aaron
 */
public class AccountRollOutActivity extends BaseActivity {
  private WindowView windowView;
  private Context mContext;

  private Boolean PocketBoolean = false;

  private LinearLayout mLinearLayout;

  private PopupWindow paymentPopWin;

  private TextView Popuptitle;

  private View PopupView;

  private ArrayList<RecordsList> recordsList;
  private ListView mListView;
  private PaymentPopWinAdapter mPopWinAdapter;

  private String investAmount;

  private String flag;
  private String name;
  private String amount;
  private String cardNumber;

  private double bidMoney;

  private String moneyCostString; // 提现手续费 string
  private String realityString; // 实际扣除 String
  private String sysRemainderAmtCanTakeOutTs; // 剩余提取额度 String

  private TextView currentPocket;// 活期口袋
  private EditText rollOutMoney;// 转出金额
  private TextView allTurnOut;// 全部转出
  private TextView withdrawalsFeeMoney;// 提现手续费
  private TextView practicalDeduction;// 实际扣除
  private TextView availableCash;// 可提现金额
  private TextView sysRemainderAmtCanTakeOut;// 剩余提取额度
  private TextView loan_detailedIntroduction;// 详细介绍
  private Button confirmTurnOutBtn;// 确定转出
  private TextView tvAssure;// 承保保险公司

  @Override
  protected int loadWindowLayout() {
    return R.layout.loan_account_roll_out;
  }

  @Override
  protected void initTitleBar() {
    mContext = AccountRollOutActivity.this;
    windowView = (WindowView) findViewById(R.id.windowView);
    windowView.setTitle(R.string.loan_current_pocket_outcome);
    windowView.setVisibility(View.VISIBLE);
    windowView.setLeftButtonClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  @Override
  protected void initViews() {

    mLinearLayout = (LinearLayout) findViewById(R.id.loan_CloseOntouch);
    mLinearLayout.setOnClickListener(this.clincListener);

    // 活期口袋
    currentPocket = (TextView) findViewById(R.id.loan_current_pocket);
    currentPocket.setOnClickListener(clincListener);

    // 转出金额
    rollOutMoney = (EditText) findViewById(R.id.loan_rollOutMoney);
    rollOutMoney.setSelection(rollOutMoney.getText().length());

    // 全部转出
    allTurnOut = (TextView) findViewById(R.id.loan_allTurnOut);
    allTurnOut.setOnClickListener(clincListener);

    // 提现手续费
    withdrawalsFeeMoney = (TextView) findViewById(R.id.loan_withdrawalsFeeMoney);

    // 实际扣除
    practicalDeduction = (TextView) findViewById(R.id.loan_practicalDeduction);

    // 可提现金额
    availableCash = (TextView) findViewById(R.id.loan_availableCash);

    // 剩余提取额度
    sysRemainderAmtCanTakeOut = (TextView) findViewById(R.id.loan_sysRemainderAmtCanTakeOut);

    // 详细介绍
    loan_detailedIntroduction = (TextView) findViewById(R.id.loan_detailedIntroduction);
    loan_detailedIntroduction.setOnClickListener(clincListener);

    // 确定转出
    confirmTurnOutBtn = (Button) findViewById(R.id.loan_confirmTurnOutBtn);
    confirmTurnOutBtn.setOnClickListener(clincListener);

    // 承保保险公司
    tvAssure = (TextView) findViewById(R.id.loan_account_tvAssure);

    initData();

    initPopupWindow();

    // EditText监听
    ExpectedReturn();
  }

  private void initData() {
    // RequestData();

    // 活期口袋
    currentPocket.setText(mContext.getResources().getString(R.string.loan_payment_account));

    // 详细介绍
    loan_detailedIntroduction.setText(Html.fromHtml(mContext.getResources().getString(R.string.loan_withdraw_hint_totheaccount) + "<font color=#0f9be3>" + mContext.getResources().getString(R.string.loan_details_introduce) + "</font>"));

    // 承保保险公司
    tvAssure.setText(mContext.getResources().getString(R.string.loan_sate_tips));
  }

  OnClickListener clincListener = new OnClickListener() {

    @Override
    public void onClick(View view) {
      switch (view.getId()) {
        case R.id.loan_current_pocket: // 活期口袋
          MyPocketInfo();
          CloseOntouch();
          break;
        case R.id.loan_allTurnOut:// 全部转出
          showAllTurnOut();
          CloseOntouch();
          break;
        case R.id.loan_detailedIntroduction: // 详细介绍
          showDetailedIntroduction();
          CloseOntouch();
          break;
        case R.id.loan_confirmTurnOutBtn: // 确定转出
          showConfirmTurnOutBtn();
          CloseOntouch();
          break;
        case R.id.loan_CloseOntouch:
          CloseOntouch();
          break;
      }
    }
  };

  // 选择付款方式
  private void showCurrentPocket() {
    if (!paymentPopWin.isShowing()) {
      showAsDropDown();
      Popuptitle.setText(mContext.getResources().getString(R.string.loan_choice_payment_method));
    } else {
      onDismiss();
    }

    if (mPopWinAdapter == null) {
      mPopWinAdapter = new PaymentPopWinAdapter(this, recordsList, 1);
      mListView.setAdapter(mPopWinAdapter);
    } else {
      mPopWinAdapter.updateMoreBids(recordsList);
    }
  }

  // 全部转出
  private void showAllTurnOut() {
    if (!PocketBoolean) {
      ToastUtil.showShort(mContext, R.string.loan_payment_account);
      return;
    }

    double balance = Double.parseDouble(amount);

    if (balance == 0) {
      ToastUtil.showShort(mContext, R.string.loan_roll_out_money);
      return;
    }

    if (!TextUtils.isEmpty(amount)) {
      rollOutMoney.setText(amount);
      rollOutMoney.setSelection(rollOutMoney.getText().length());
    }
  }

  // 详细介绍
  private void showDetailedIntroduction() {
    Intent intent = new Intent(mContext, WebViewActivity.class);
    intent.putExtra("htmlUrl", new StringBuffer(JrdConfig.getBaseUrl()).append(WebApi.PayApi.WITHDRAWAGREEMENT).toString());
    intent.putExtra("htmlTitle", mContext.getResources().getString(R.string.loan_details_introduce));
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
  }

  // 确定转出
  private void showConfirmTurnOutBtn() {
    investAmount = rollOutMoney.getText().toString();

    if (!PocketBoolean) {
      ToastUtil.showShort(mContext, R.string.loan_payment_account);
      return;
    }

    if (TextUtils.isEmpty(investAmount)) {// 投标金额不能为空
      ToastUtil.showShort(mContext, R.string.loan_add_money);
      return;
    }

    bidMoney = Double.parseDouble(investAmount);

    double balance = Double.parseDouble(amount);

    if (balance == 0) {
      ToastUtil.showShort(mContext, R.string.loan_roll_out_money);
      return;
    }

    // if (bidMoney > Double.valueOf(sysRemainderAmtCanTakeOutTs)) {// 输入金额大于剩余提取额度
    // ToastUtil.showShort(mContext, R.string.loan_sysRemainderAmtCanTakeOutTs);
    // return;
    // }

    if (bidMoney > balance) {// 输入金额大于可用余额
      ToastUtil.showShort(mContext, R.string.loan_available_bal_money);
      return;
    }

    // 判断是否是本金转出
    if (mContext.getResources().getString(R.string.loan_account_pocket_text_principal).equals(currentPocket.getText().toString())) {
      flag = "1";
    } else {
      flag = "0";
    }

    if (UserInfoPrefs.loadLastLoginUserProfile().transPwdFlag.equals("0")) {// 未设置支付密码
      startActivity(new Intent(this, SetTransactionPswActivity.class));
      return;
    }

    if (name.equals(mContext.getResources().getString(R.string.loan_account_pocket_text_principal))) {
      DialogUtils.showTradePwdDialog(mContext, new OnPaymentAccountListener() {

        @Override
        public void onConfirm(String type, Dialog dialog) {
          if (type.equals("")) {
            ToastUtil.showShort(mContext, "请输入交易密码");
            return;
          } else {
            dialog.dismiss();
            KeyBoardUtil.closeKeybord(rollOutMoney, mContext);
            userHuoqiTakeout(type);
          }
        }
      });
    } else {
      userHuoqiTakeout(null);
    }
  }

  /**
   * 活期口袋信息请求
   */
  private void RequestData() {
    PocketApi.MyPocketInfo(mContext, new OnHttpTaskListener<MyPocketBean>() {

      @Override
      public void onTaskError(int resposeCode) {
        DismissDialog();
      }

      @Override
      public void onStart() {
        ShowDrawDialog(mContext);
      }

      @Override
      public void onFinishTask(MyPocketBean bean) {
        DismissDialog();
        if (bean != null && bean.getResultCode() == 0) {
          if (!bean.getCount().equals("0")) {
            // sysRemainderAmtCanTakeOutTs = bean.getSysRemainderAmtCanTakeOut();
            // sysRemainderAmtCanTakeOut.setText(new
            // StringBuffer().append(mContext.getResources().getString(R.string.loan_sysRemainderAmtCanTakeOut)).append(sysRemainderAmtCanTakeOutTs).append(mContext.getResources().getString(R.string.loan_yuan_text)).toString());
          }
        }
      }
    });
  }

  private void userHuoqiTakeout(String password) {
    PayApi.userHuoqiTakeout(mContext, flag, "0", cardNumber, bidMoney, password, new OnHttpTaskListener<HuoqiTakeoutBean>() {

      @Override
      public void onTaskError(int resposeCode) {
        DismissDialog();
      }

      @Override
      public void onStart() {
        ShowDrawDialog(mContext);
      }

      @Override
      public void onFinishTask(HuoqiTakeoutBean bean) {
        DismissDialog();
        if (bean != null && bean.getResultCode() == 0) {
          
          // 用户行为统计接口
          StatisticsApi.getUserBehavior(AccountRollOutActivity.this, Const.EventName.TRANSFEROUT, Const.EventId.TRANSFEROUT, null, null, bean.getAmount());

          if (bean.getRecords().isEmpty()) {
            return;
          }

          if (bean.getType().equals("0")) {// 账户余额0
            UMengEvent.onEvent(mContext, UMengEvent.EVENT_ID_TRANSFEROUT, UMengEvent.EVENT_MODULE_TRANSFERIN_ACCOUNTBALANCE);
          } else if (bean.getType().equals("1")) {// 银行卡1
            UMengEvent.onEvent(mContext, UMengEvent.EVENT_ID_TRANSFEROUT, UMengEvent.EVENT_MODULE_TRANSFERIN_BANKCARD);
          }

          Intent intent = new Intent(mContext, TurnOutToSucceedActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("title", getString(R.string.loan_current_pocket_outcome_success));
          intent.putExtra("name", name);
          intent.putExtra("type", bean.getType());
          intent.putExtra("amount", bean.getAmount());
          intent.putExtra("oprateTime", bean.getOprateTime());
          intent.putExtra("records", bean.getRecords());
          startActivity(intent);
          finish();

        } else {
          ToastUtil.showShort(mContext, bean.getResultMsg());
        }


      }
    });
  }

  // 我的活期宝------>从哪儿转出
  private void MyPocketInfo() {
    PocketApi.MyPocketInfo(mContext, new OnHttpTaskListener<MyPocketBean>() {

      @Override
      public void onTaskError(int resposeCode) {
        DismissDialog();
      }

      @Override
      public void onStart() {
        ShowProDialog(mContext);
      }

      @Override
      public void onFinishTask(MyPocketBean bean) {
        DismissDialog();
        if (bean != null && bean.getResultCode() == 0) {
          if ((TextUtils.isEmpty(bean.getProfitCanTakeOut())) || (TextUtils.isEmpty(bean.getRemainderAmt()))) {
            return;
          }
          double allTheMoney = (Double.parseDouble(bean.getRemainderAmt()) - Double.parseDouble(bean.getRedeemAmtSum()));
          recordsList = new ArrayList<RecordsList>();
          recordsList.add(new RecordsList("2", mContext.getResources().getString(R.string.loan_account_pocket_text_profit), bean.getProfitCanTakeOut(), bean.getAnnualRate()));
          recordsList.add(new RecordsList("2", mContext.getResources().getString(R.string.loan_account_pocket_text_principal), String.valueOf(allTheMoney), bean.getAnnualRate()));
          showCurrentPocket();
        }
      }
    });
  }

  // 输入金额监听事件
  private void ExpectedReturn() {
    rollOutMoney.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence txt, int arg1, int arg2, int arg3) {

        if (!PocketBoolean) {
          ToastUtil.showShort(mContext, R.string.loan_payment_account);
          return;
        }

        // if (TextUtils.isEmpty(txt)) {
        // ToastUtil.showShort(mContext, R.string.loan_add_money);
        // return;
        // }

        if (txt.toString().equals(".")) {// 如果提现费用第一位为., 清空
          rollOutMoney.setText("");
          return;
        }

        if (txt.toString().length() >= 2 && txt.toString().charAt(0) == '0' && (txt.toString().charAt(1) >= '0' && txt.toString().charAt(1) <= '9')) {
          rollOutMoney.setText(txt.toString().charAt(1) + "");
          rollOutMoney.setSelection(rollOutMoney.getText().length());
          return;
        }

        if (txt.toString().contains(".")) {
          if (txt.length() - 1 - txt.toString().indexOf(".") > 2) {
            txt = txt.toString().subSequence(0, txt.toString().indexOf(".") + 3);
            rollOutMoney.setText(txt);
            rollOutMoney.setSelection(txt.length());
          }
        }

        if (txt.toString().trim().substring(0).equals(".")) {
          txt = "0" + txt;
          rollOutMoney.setText(txt);
          rollOutMoney.setSelection(2);
        }

        if (txt.toString().startsWith("0") && txt.toString().trim().length() > 1) {
          if (!txt.toString().substring(1, 2).equals(".")) {
            rollOutMoney.setText(txt.subSequence(0, 1));
            rollOutMoney.setSelection(1);
            return;
          }
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

        // if (!edt.toString().isEmpty() &&
        // Double.valueOf(edt.toString()) > 0) {
        // RequestCost(edt);
        // } else {
        // withdrawalsFeeMoney.setText("0元");
        // practicalDeduction.setText(R.string.loan_deduct_0);
        // }

      }
    });
  }

  // 提现费用结算接口
  private void RequestCost(Editable str) {
    BankApi.WithdrawCost(mContext, UserInfoPrefs.getUserId(), str.toString(), new OnHttpTaskListener<CostBean>() {

      private double moneyCost; // 提现手续费 double
      private double deduct; // 实际扣除double

      @Override
      public void onStart() {}

      @Override
      public void onTaskError(int resposeCode) {}

      @Override
      public void onFinishTask(CostBean bean) {
        if (bean != null && bean.getResultCode() == 0) {
          moneyCostString = bean.getMoneyCost();
          BigDecimal bd = new BigDecimal(moneyCostString);
          moneyCost = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
          withdrawalsFeeMoney.setText(new StringBuffer().append(String.valueOf(moneyCost)).append(mContext.getResources().getString(R.string.loan_yuan_text)).toString());
          realityString = bean.getActuallyCharge();
          BigDecimal bd1 = new BigDecimal(realityString);
          deduct = bd1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
          practicalDeduction.setText(new StringBuffer().append(mContext.getResources().getString(R.string.loan_deduct)).append(String.valueOf(deduct)).append(mContext.getResources().getString(R.string.loan_yuan_text)).toString());
        }
      }
    });
  }

  /**
   * 初始化选择银行卡页面
   */
  private void initPopupWindow() {
    PopupView = this.getLayoutInflater().inflate(R.layout.loan_payment_dialog, null);

    Popuptitle = (TextView) PopupView.findViewById(R.id.loan_payment_title);

    mListView = (ListView) PopupView.findViewById(R.id.loan_payment_ListView);

    paymentPopWin = new PopupWindow(PopupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    // 实例化一个ColorDrawable颜色为半透明
    ColorDrawable dw = new ColorDrawable(0xb0000000);

    // 设置SelectPicPopupWindow弹出窗体的背景
    paymentPopWin.setBackgroundDrawable(dw);

    // 设置SelectPicPopupWindow弹出窗体的宽
    paymentPopWin.setWidth(LayoutParams.MATCH_PARENT);

    // 设置SelectPicPopupWindow弹出窗体的高
    paymentPopWin.setHeight(LayoutParams.WRAP_CONTENT);

    // 设置SelectPicPopupWindow弹出窗体动画效果
    paymentPopWin.setAnimationStyle(R.style.AnimBottom);

    // 设置点击窗口外边窗口消失
    paymentPopWin.setOutsideTouchable(true);

    // 设置此参数活的焦点，否在无法点击
    paymentPopWin.setFocusable(true);

    mListView.setOnItemClickListener(mListViewItem);

    paymentPopWin.setOnDismissListener(new OnDismissListener() {

      // 在dismiss中恢复透明度
      @Override
      public void onDismiss() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
      }
    });
  }

  /**
   * 选择银行卡点击事件
   */
  OnItemClickListener mListViewItem = new OnItemClickListener() {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      onDismiss();

      name = recordsList.get(position).getName();
      cardNumber = recordsList.get(position).getCardNumber();
      amount = recordsList.get(position).getAmount();

      PocketBoolean = true;

      if (name.equals(mContext.getResources().getString(R.string.loan_account_pocket_text_profit))) {
        rollOutMoney.setText(amount);
        allTurnOut.setTextColor(Color.WHITE);
        allTurnOut.setText(Html.fromHtml("<font color=#FFFFFF>" + mContext.getResources().getString(R.string.loan_all_turn_out) + "</font>"));
        rollOutMoney.setEnabled(false);
      } else if (name.equals(mContext.getResources().getString(R.string.loan_account_pocket_text_principal))) {
        allTurnOut.setText(Html.fromHtml("<font color=#0f9be3>" + mContext.getResources().getString(R.string.loan_all_turn_out) + "</font>"));
        rollOutMoney.setEnabled(true);
        if (rollOutMoney.length() > 0) {
          rollOutMoney.setText("");
        }
      }

      currentPocket.setText(name);

      availableCash.setText(new StringBuffer().append(mContext.getResources().getString(R.string.loan_TransferAmounts)).append(amount).append(mContext.getResources().getString(R.string.loan_yuan_text)).toString());

    }
  };

  /**
   * 显示PopWin
   */
  private void showAsDropDown() {
    WindowManager.LayoutParams lp = getWindow().getAttributes();
    lp.alpha = 0.4f;
    getWindow().setAttributes(lp);

    int screenHeight = (getScreenHeight(this) / 3 - 100);

    paymentPopWin.showAsDropDown(this.findViewById(R.id.loan_parent), 0, screenHeight);
  }

  /**
   * 隱藏PowWin
   */
  public void onDismiss() {
    WindowManager.LayoutParams lp = getWindow().getAttributes();
    lp.alpha = 1f;
    getWindow().setAttributes(lp);

    paymentPopWin.dismiss();
  }

  /**
   * 获取屏幕高度
   *
   * @param context
   */
  public static int getScreenHeight(Context context) {
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = manager.getDefaultDisplay();
    return display.getHeight();
  }

  // 关闭软键盘
  private void CloseOntouch() {
    View view = getWindow().peekDecorView();
    if (view != null) {
      InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  /**
   * 将textview中的字符全角化。即将所有的数字、字母及标点全部转为全角字符，使它们与汉字同占两个字节，避免由于占位导致的排版混乱问题
   *
   * @param input
   */
  public static String ToDBC(String input) {
    char[] c = input.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (c[i] == 12288) {
        c[i] = (char) 32;
        continue;
      }
      if (c[i] > 65280 && c[i] < 65375) c[i] = (char) (c[i] - 65248);
    }
    return new String(c);
  }

}
