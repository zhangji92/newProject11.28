package com.jrd.loan.myaccount;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jrd.loan.R;
import com.jrd.loan.adapter.AutoCompleteTextAdapter;
import com.jrd.loan.adapter.BankAdapter;
import com.jrd.loan.api.BankApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BankCardBean;
import com.jrd.loan.bean.OpenBean;
import com.jrd.loan.constant.Const;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.widget.WindowView;

/**
 * 开户行
 *
 * @author Luke
 */
public class OpeningBankAct extends BaseActivity {

    private Context mContext;
    private WindowView windowView;
    private AutoCompleteTextView autoTV;
    private String getCityCode;
    private String getbankCode;
    private ListView listview;
    private RelativeLayout autoLayout;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_opening_bank;
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(R.string.loan_select_opening_text);
        windowView.setLeftButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

    }

    @Override
    protected void initViews() {
        mContext = OpeningBankAct.this;
        getCityCode = getIntent().getStringExtra("cityCode");
        getbankCode = getIntent().getStringExtra("bankCode");
        autoLayout = (RelativeLayout) findViewById(R.id.autocompleate_layout);
        listview = (ListView) findViewById(R.id.loan_opencard_listview);
        listview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        listview.setEmptyView(findViewById(R.id.nodatalayout));
        RequestData();
    }

    /**
     * 获取开户行地区信息
     */
    private void RequestData() {
        BankApi.GetBankAreaList(mContext, getCityCode, getbankCode, true, new OnHttpTaskListener<OpenBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowProDialog(mContext);
            }

            @Override
            public void onFinishTask(final OpenBean bean) {
                DismissDialog();
                if (bean.getResultCode() == 0) {
                    if (bean.getBankInfoList() != null) {
                        listview.setAdapter(new BankAdapter(bean.getBankInfoList(), mContext));
                        listview.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                Intent intent = new Intent(mContext, AddBankCardActivity.class);
                                intent.putExtra("bankName", bean.getBankInfoList().get(arg2).getBankName());
                                intent.putExtra("bankCode", bean.getBankInfoList().get(arg2).getBankCode());
                                setResult(Const.IntentCode.ADDCORD_TO_OPENING_INTENT_CODE, intent);
                                finish();
                            }
                        });
                        autoTV = (AutoCompleteTextView) findViewById(R.id.autocompleateTV);
                        final AutoCompleteTextAdapter<BankCardBean> autoTVAdapter = new AutoCompleteTextAdapter<BankCardBean>(mContext, R.layout.loan_autotext_item_layout, bean.getBankInfoList());
                        autoTV.setAdapter(autoTVAdapter);
                        autoTV.setThreshold(1);
                        autoTV.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                                BankCardBean bankCardBean = autoTVAdapter.getItem(position);
                                autoTV.setText(bankCardBean.getBankName());
                                Intent intent = new Intent(mContext, AddBankCardActivity.class);
                                intent.putExtra("bankName", bankCardBean.getBankName());
                                intent.putExtra("bankCode", bankCardBean.getBankCode());
                                setResult(Const.IntentCode.ADDCORD_TO_OPENING_INTENT_CODE, intent);
                                finish();
                            }
                        });

                    }else{
                        listview.setAdapter(null);
                    }
                }
            }
        });
    }
}
