package com.jrd.loan.myaccount;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.adapter.BankCardItemAdapter;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.api.StatisticsApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BankListBean;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.current.pocket.rehcarge.RechargeSuccessActivity;
import com.jrd.loan.current.pocket.rehcarge.SelectCardActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.BankLogoUtil;
import com.jrd.loan.util.FormatUtils;
import com.jrd.loan.util.KeyBoardUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

import java.util.List;

/**
 * 添加银行卡
 *
 * @author Luke
 */
public class AddBankCardActivity extends BaseActivity implements OnClickListener {

  private Context mContext;
  private EditText nameEdit;
  private EditText cardNumEdit;

  private int getIntentCode;

  private String bankCode;
  private PopupWindow paymentPopWin;
  private LinearLayout selectLayout;
  private LinearLayout bankLayout;
  private TextView bankNameTV;
  private TextView selectTV;
  private ImageView bankIcon;
  private List<BankListBean.RecordsEntity> mList;

  private boolean isBindCard = false;// 是否绑卡

  @Override
  protected int loadWindowLayout() {
    return R.layout.loan_activity_addbankcard;
  }

  @Override
  protected void initTitleBar() {
    WindowView windowView = (WindowView) findViewById(R.id.windowView);
    windowView.setTitle(getResources().getString(R.string.loan_add_bankcard));
    windowView.setLeftButtonClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        finish();
      }
    });
  }

  @Override
  protected void initViews() {
    mContext = AddBankCardActivity.this;
    String cardName = UserInfoPrefs.loadLastLoginUserProfile().userName;
    getIntentCode = getIntent().getIntExtra("intent", 0);

    selectLayout = (LinearLayout) findViewById(R.id.loan_bindcard_selectLayout);
    selectLayout.setOnClickListener(this);

    selectTV = (TextView) findViewById(R.id.loan_bindcard_selectTV);
    selectTV.setVisibility(View.VISIBLE);

    bankLayout = (LinearLayout) findViewById(R.id.loan_bindcard_bankLayout);
    bankLayout.setVisibility(View.GONE);

    bankNameTV = (TextView) findViewById(R.id.loan_bindcard_bankName);
    bankIcon = (ImageView) findViewById(R.id.loan_bindcard_bankImage);

    nameEdit = (EditText) findViewById(R.id.loan_addcard_cardName_Edit);
    nameEdit.setText(cardName);

    cardNumEdit = (EditText) findViewById(R.id.loan_addcard_input_cardNum_Edit);
    FormatUtils.CardEdit(cardNumEdit);

    Button addCardBtn = (Button) findViewById(R.id.loan_addcard_enterBtn);
    addCardBtn.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    KeyBoardUtil.closeKeybord(bankNameTV, mContext);
    switch (v.getId()) {
      case R.id.loan_bindcard_selectLayout:
        getBankListData();
        break;
      case R.id.loan_addcard_enterBtn:
        AddCardBtn();
        break;
    }
  }

  /**
   * 添加银行卡按钮事件
   */
  private void AddCardBtn() {
    if (nameEdit.getText().toString().isEmpty()) {
      ToastUtil.showShort(mContext, R.string.loan_opening_name_no_empty);
      return;
    } else if (!isBindCard) {
      ToastUtil.showShort(mContext, R.string.loan_please_select_card);
      return;
    } else if (cardNumEdit.getText().toString().isEmpty()) {// 银行卡号不能为空
      ToastUtil.showShort(mContext, R.string.loan_please_input_cardnumber);
      return;
    } else if (cardNumEdit.getText().length() < 16) {// 小于16位, 提示:
      // 您输入的银行卡格式错误
      ToastUtil.showShort(mContext, R.string.loan_please_input_cardnumber_error);
      return;
    }

    BankApi.WithDrawCard(mContext, bankCode, cardNumEdit.getText().toString().replace(" ", ""), new OnHttpTaskListener<BaseBean>() {
      @Override
      public void onStart() {
        ShowProDialog(mContext);
      }

      @Override
      public void onTaskError(int resposeCode) {

        DismissDialog();
      }

      @Override
      public void onFinishTask(BaseBean bean) {
        DismissDialog();
        if (bean.getResultCode() == 0) {
          // 用户行为统计接口
          StatisticsApi.getUserBehavior(AddBankCardActivity.this, Const.EventName.TIECARD, Const.EventId.TIECARD, null, null, null);

          switch (getIntentCode) {
          // 从提现界面过来的
            case Const.IntentCode.WITHDRAW_TO_ADDCRAD_ITNENT_CODE:
              Intent withIntent = new Intent(mContext, WithdrawActivity.class);
              withIntent.putExtra("bankCode", bankCode);
              withIntent.putExtra("cardNumber", cardNumEdit.getText().toString().replace(" ", ""));
              withIntent.putExtra("cardName", bankNameTV.getText().toString());
              setResult(RESULT_OK, withIntent);
              break;
            // 从我的银行卡界面过来的
            case Const.IntentCode.MYCARD_TO_ADDCRAD_ITNENT_CODE:
              setResult(RESULT_OK);
              break;
            // 从选择银行卡界面过来的
            case Const.IntentCode.SELECT_TO_ADD_BANK_CARD_INTENT_CODE:
              Intent selectIntent = new Intent(mContext, SelectCardActivity.class);
              selectIntent.putExtra("bankCode", bankCode);
              selectIntent.putExtra("cardNumber", cardNumEdit.getText().toString().replace(" ", ""));
              selectIntent.putExtra("cardName", bankNameTV.getText().toString());
              setResult(RESULT_OK, selectIntent);
              break;
          }
          finish();
        }
        ToastUtil.showShort(mContext, bean.getResultMsg());
      }
    });
  }

  // 获取银行卡列表
  private void getBankListData() {

    BankApi.getBankList(mContext, new OnHttpTaskListener<BankListBean>() {

      @Override
      public void onStart() {
        ShowProDialog(mContext);
      }

      @Override
      public void onTaskError(int resposeCode) {
        DismissDialog();
      }

      @Override
      public void onFinishTask(BankListBean bean) {
        DismissDialog();
        if (bean != null && bean.getResultCode() == 0) {
          if (bean.getRecords() != null && bean.getRecords().size() > 0) {

            mList = bean.getRecords();

            initPopupWindow();

          }
        }

      }
    });
  }

  /**
   * 初始化选择银行卡页面
   */
  private void initPopupWindow() {
    View PopupView = this.getLayoutInflater().inflate(R.layout.loan_payment_dialog, null);

    TextView title = (TextView) PopupView.findViewById(R.id.loan_payment_title);
    title.setText("请选择银行卡（仅限储蓄卡）");

    ListView mListView = (ListView) PopupView.findViewById(R.id.loan_payment_ListView);

    paymentPopWin = new PopupWindow(PopupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    // 实例化一个ColorDrawable颜色为半透明
    ColorDrawable dw = new ColorDrawable(0xb0000000);

    // 设置SelectPicPopupWindow弹出窗体的背景
    paymentPopWin.setBackgroundDrawable(dw);

    // 设置SelectPicPopupWindow弹出窗体的宽
    paymentPopWin.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);

    // 设置SelectPicPopupWindow弹出窗体的高
    paymentPopWin.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

    // 设置SelectPicPopupWindow弹出窗体动画效果
    paymentPopWin.setAnimationStyle(R.style.AnimBottom);

    // 设置点击窗口外边窗口消失
    paymentPopWin.setOutsideTouchable(true);

    // 设置此参数活的焦点，否在无法点击
    paymentPopWin.setFocusable(true);

    // mListView.setOnItemClickListener(mListViewItem);

    paymentPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {

      // 在dismiss中恢复透明度
      @Override
      public void onDismiss() {
        backgroundAlpha(1f);
      }
    });

    BankCardItemAdapter adapter = new BankCardItemAdapter(mList, mContext);

    mListView.setAdapter(adapter);

    mListView.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        selectTV.setVisibility(View.GONE);
        bankLayout.setVisibility(View.VISIBLE);

        bankCode = mList.get(position).getBankCode();
        bankNameTV.setText(mList.get(position).getOrderName());
        // ImageLoader.loadImage(bankIcon, mList.get(position).getBankImg());
        BankLogoUtil.setBankLog(bankIcon, Integer.parseInt(bankCode));
        backgroundAlpha(1f);
        isBindCard = true;
        paymentPopWin.dismiss();

      }
    });

    if (!paymentPopWin.isShowing()) {
      paymentPopWin.showAsDropDown(selectLayout);
    }
    backgroundAlpha(1.4f);
  }

  /**
   * 设置添加屏幕的背景透明度
   *
   * @param bgAlpha 透明的 0-1
   */
  public void backgroundAlpha(float bgAlpha) {
    WindowManager.LayoutParams lp = getWindow().getAttributes();
    lp.alpha = bgAlpha; // 0.0-1.0
    getWindow().setAttributes(lp);
  }
}
