package com.jrd.loan.myaccount;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jrd.loan.R;
import com.jrd.loan.adapter.BankCardAdapter;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BankCardBean;
import com.jrd.loan.bean.BindCardBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.current.pocket.rehcarge.BindBankCardActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class SelectCardActivity extends BaseActivity {

    private WindowView windowView;
    private ListView cardListView;
    private List<BankCardBean> cardList;
    private BankCardAdapter cardAdapter;
    private Context mContext;

    private View footerView;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_select_card_layout;
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(R.string.loan_select_bankcard);
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

    }

    @Override
    protected void initViews() {
        mContext = SelectCardActivity.this;
        footerView = View.inflate(mContext, R.layout.loan_addcard_include_layout, null);
        cardListView = (ListView) findViewById(R.id.loan_select_card_listview);
        cardListView.addFooterView(footerView);

        RequestData();
        ListViewItemClick();

    }

    /**
     * listview点击事件
     */
    private void ListViewItemClick() {
        cardListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == cardList.size()) {
                    if (UserInfoPrefs.loadLastLoginUserProfile().idNumberInfo.equals("未设置")) {
                        startActivity(new Intent(mContext, BindBankCardActivity.class));
                    } else {
                        Intent addIntent = new Intent(mContext, AddBankCardActivity.class);
                        addIntent.putExtra("intent", Const.IntentCode.MYCARD_TO_ADDCRAD_ITNENT_CODE);
                        startActivityForResult(addIntent, Const.IntentCode.MYCARD_TO_ADDCRAD_ITNENT_CODE);
                    }
                } else {
                    String cardCode = cardList.get(arg2).getBankCode();
                    String cardNum = cardList.get(arg2).getCardNumber();

                    Intent intent = new Intent(mContext, WithdrawActivity.class);
                    intent.putExtra("cardCode", cardCode);
                    intent.putExtra("cardNumber", cardNum);
                    setResult(Const.IntentCode.WITHDRAW_SELECTOR_CARD_INTENT_RESULT_CODE, intent);
                    finish();
                }

            }
        });
    }

    /**
     * 获取银行卡数据
     */
    private void RequestData() {
        BankApi.MyBindCard(mContext, new OnHttpTaskListener<BindCardBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowDrawDialog(mContext);
            }

            @Override
            public void onFinishTask(BindCardBean bean) {
                DismissDialog();
                if (bean.getResultCode() == 0) {
                    if (bean.getUsrCardInfoList() != null) {
                        cardList = bean.getUsrCardInfoList();
                        cardAdapter = new BankCardAdapter(cardList, mContext, null);
                        cardListView.setAdapter(cardAdapter);
                    }

                } else {
                    ToastUtil.showShort(mContext, bean.getResultMsg());
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            RequestData();
        }
    }

    ;
}
