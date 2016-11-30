package com.jrd.loan.current.pocket.rehcarge;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.adapter.SelectCardAdapter;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BankCardBean;
import com.jrd.loan.bean.BindCardBean;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.MoneyFormatUtil;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

import java.util.List;

/**
 * 老用户充值界面
 */
public class RechargeOldActivity extends BaseActivity {
    private Context mContext;
    OnClickListener clincListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loan_current_recharge_addBankcardTV:
                    startActivity(new Intent(mContext, RechargeActivity.class));
                    break;
            }
        }

    };
    private ListView mListView;
    private List<BankCardBean> cardList;
    private SelectCardAdapter cardAdapter;
    private String cardNum;
    private String cardCode;
    private String cardName;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_recharge_old;
    }

    @Override
    protected void initViews() {
        mContext = RechargeOldActivity.this;

        mListView = (ListView) findViewById(R.id.loan_current_recharge_listview);
        TextView addCardTV = (TextView) findViewById(R.id.loan_current_recharge_addBankcardTV);
        addCardTV.setOnClickListener(this.clincListener);

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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                cardName = cardList.get(arg2).getBankName();
                cardNum = cardList.get(arg2).getCardNumber();
                String cardUrl = "";
                cardCode = cardList.get(arg2).getBankCode();

                String oneTime = cardList.get(arg2).getOneTime();
                String oneDay = cardList.get(arg2).getOneDay();
                String oneMonth = cardList.get(arg2).getOneMonth();

                String hintText = getString(R.string.loan_recharge_again_kindly_remind, MoneyFormatUtil.getMoney(oneTime), oneDay.equals("0.00") ? getString(R.string.loan_no_district) : "限额" + MoneyFormatUtil.getMoney(oneDay) + "元", oneMonth.equals("0.00") ? getString(R.string.loan_no_district) : "限额" + MoneyFormatUtil.getMoney(oneMonth) + "元");
                String[] bankInfo = new String[6];
                bankInfo[0] = cardList.get(arg2).getOrderName();
                bankInfo[1] = cardNum;
                bankInfo[2] = cardUrl;
                bankInfo[3] = cardCode;
                bankInfo[4] = hintText;
                bankInfo[5] = cardName;

                Intent intent = new Intent(mContext, RechargeActivity.class);
                intent.putExtra("cardInfo", bankInfo);
                startActivity(intent);
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
                        if (bean.getUsrCardInfoList().size() > 0) {
                            cardList = bean.getUsrCardInfoList();
                            cardAdapter = new SelectCardAdapter(cardList, mContext);
                            mListView.setAdapter(cardAdapter);
                        } else {
                            startActivity(new Intent(mContext, RechargeActivity.class));
                            finish();
                        }
                    }
                } else {
                    ToastUtil.showShort(mContext, bean.getResultMsg());
                }
            }
        });
    }

    @Override
    protected void initTitleBar() {
        WindowView windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle("充值");
        windowView.setLeftButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
