package com.jrd.loan.myaccount;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
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
import com.jrd.loan.current.pocket.rehcarge.ExplainActivity;
import com.jrd.loan.current.pocket.rehcarge.IdCheckActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

import java.util.List;

/**
 * 银行卡列表
 *
 * @author Luke
 */
public class MyBankCardActivity extends BaseActivity {

    private Context mContext;
    private ListView cardListView;
    private List<BankCardBean> cardList;
    private BankCardAdapter cardAdapter;
    private View mView;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_my_bankcard;
    }

    @Override
    protected void initTitleBar() {
        WindowView windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(getResources().getString(R.string.loan_my_bankcard));
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        mContext = MyBankCardActivity.this;
        mView = LayoutInflater.from(mContext).inflate(R.layout.loan_addcard_include_layout, null);
        cardListView = (ListView) findViewById(R.id.loan_my_card_listview);
        cardListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        cardListView.addFooterView(mView);

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
                if (arg2 == cardList.size()) {
                    if (UserInfoPrefs.loadLastLoginUserProfile().idNumberFlag.equals("0")) {
                        startActivity(new Intent(mContext, IdCheckActivity.class));
                    }else{
//                        Intent addIntent = new Intent(mContext, AddBankCardActivity.class);
//                        addIntent.putExtra("intent", Const.IntentCode.MYCARD_TO_ADDCRAD_ITNENT_CODE);
//                        startActivityForResult(addIntent, Const.IntentCode.MYCARD_TO_ADDCRAD_ITNENT_CODE);

                        Intent intent = new Intent(mContext,ExplainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("recharge",Const.IntentCode.MYBANKCARD_TO_RECHARGE_INTENT_CODE);
                        startActivity(intent);

                    }
                }
            }
        });
    }

    /**
     * 请求数据
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

                        if (cardList.isEmpty()) {
                            mView.setVisibility(View.VISIBLE);
                        } else {
                            mView.setVisibility(View.GONE);
                        }
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

}
