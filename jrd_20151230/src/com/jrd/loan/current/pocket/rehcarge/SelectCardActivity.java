package com.jrd.loan.current.pocket.rehcarge;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jrd.loan.R;
import com.jrd.loan.adapter.SelectCardAdapter;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BankCardBean;
import com.jrd.loan.bean.BindCardBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.myaccount.AddBankCardActivity;
import com.jrd.loan.myaccount.WithdrawActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

import java.util.List;

public class SelectCardActivity extends BaseActivity {

    private ListView cardListView;
    private List<BankCardBean> cardList;
    private SelectCardAdapter cardAdapter;
    private Context mContext;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_select_card_layout;
    }

    @Override
    protected void initTitleBar() {
        WindowView windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(R.string.loan_select_bankcard);
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        windowView.setRightButtonClickListener("添加", new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCardActivity.this, AddBankCardActivity.class);
                intent.putExtra("intent", Const.IntentCode.SELECT_TO_ADD_BANK_CARD_INTENT_CODE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void initViews() {
        mContext = SelectCardActivity.this;
        cardListView = (ListView) findViewById(R.id.loan_select_card_listview);
        cardListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        RequestData();
        ListViewItemClick();
        setNoNetworkClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestData();
            }
        });

    }

    /**
     * listview点击事件
     */
    private void ListViewItemClick() {
        cardListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String cardNum = cardList.get(arg2).getCardNumber();
                String cardUrl = cardList.get(arg2).getBankImg();
                String cardName = cardList.get(arg2).getOrderName();

                Intent intent = new Intent(mContext, WithdrawActivity.class);
                intent.putExtra("cardUrl", cardUrl);
                intent.putExtra("bankCode", cardList.get(arg2).getBankCode());
                intent.putExtra("cardNumber", cardNum);
                intent.putExtra("cardName", cardName);
                setResult(RESULT_OK, intent);
                finish();
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
                        cardAdapter = new SelectCardAdapter(cardList, mContext);
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
            String cardName = data.getStringExtra("cardName");
            String bankCode = data.getStringExtra("bankCode");
            String cardNum = data.getStringExtra("cardNumber");
            Intent withIntent = new Intent(mContext, WithdrawActivity.class);
            withIntent.putExtra("bankCode", bankCode);
            withIntent.putExtra("cardNumber", cardNum);
            withIntent.putExtra("cardName", cardName);
            setResult(RESULT_OK, withIntent);
            finish();
        }
    }
}
