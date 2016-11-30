package com.jrd.loan.current.pocket;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.api.PocketApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.BillInfoBean;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.DateUtil;
import com.jrd.loan.widget.WindowView;

/**
 * 活期口袋账单流水操作结果界面
 *
 * @author Jacky
 */
public class BillInfoResult extends BaseActivity {

    private RelativeLayout billStartLayout;
    private int billType; // 0 转入 1 转出
    private String getkind;
    private TextView billTypeName;
    private String getId;
    private TextView billOptType;
    private TextView billDateTV;
    private TextView billTypeTV;
    private TextView billTypeTime;
    private TextView startDateTV;
    private TextView startTipsTV;
    private TextView endDateTV;
    private TextView endTipsTV;

    private ImageView startImg;
    private View startTopLine;
    private View startBottomLine;
    private ImageView endImg;
    private View endTopLine;
    private TextView billMoney;

    private int backgroundColor;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_bill_info_result_layout;
    }

    @Override
    protected void initTitleBar() {
        WindowView windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(getIntent().getStringExtra("title"));
        // back
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        this.backgroundColor = getResources().getColor(R.color.loan_color_btn_normal);
        this.getId = getIntent().getStringExtra("id");
        this.getkind = getIntent().getStringExtra("kind");

        this.billStartLayout = (RelativeLayout) findViewById(R.id.billStartLayout);
        this.billOptType = (TextView) findViewById(R.id.billOptType);
        this.billMoney = (TextView) findViewById(R.id.billMoney);
        this.billDateTV = (TextView) findViewById(R.id.billDate);
        this.billTypeName = (TextView) findViewById(R.id.billTypeName);
        this.billTypeTV = (TextView) findViewById(R.id.billOptName);
        this.billTypeTime = (TextView) findViewById(R.id.billOptTime);

        this.startDateTV = (TextView) findViewById(R.id.billOptDate);
        this.startTipsTV = (TextView) findViewById(R.id.billOptTips);
        this.startBottomLine = findViewById(R.id.billStartLinebuttom);
        this.startImg = (ImageView) findViewById(R.id.billStartImg);
        this.startTopLine = findViewById(R.id.billStartLinetop);

        this.endDateTV = (TextView) findViewById(R.id.billProfitEndDate);
        this.endTipsTV = (TextView) findViewById(R.id.billProfitEndTips);
        this.endImg = (ImageView) findViewById(R.id.billEndImg);
        this.endTopLine = findViewById(R.id.billEndLineTop);

        // 0 转入 1 转出
        this.billType = getIntent().getIntExtra("bill_type", 0);
        if (this.billType == 0) {// 转入
            billStartLayout.setVisibility(View.VISIBLE);
            IncomeDetails();
        } else if (billType == 1) {// 转出
            billStartLayout.setVisibility(View.GONE);
            OutDetails();
        }
    }

    /**
     * 转入详情
     */
    private void IncomeDetails() {
        PocketApi.IntoDetails(BillInfoResult.this, getId, new OnHttpTaskListener<BillInfoBean>() {

            @Override
            public void onTaskError(int resposeCode) {

                DismissDialog();
            }

            @Override
            public void onStart() {

                ShowDrawDialog(BillInfoResult.this);
            }

            @Override
            public void onFinishTask(BillInfoBean bean) {

                DismissDialog();
                billTypeTime.setVisibility(View.GONE);
                if (bean != null && bean.getResultCode() == 0) {
                    if (bean.getType().equals("0")) {
                        billTypeName.setText("余额转入");
                    } else {
                        billTypeName.setText("银行卡转入");
                    }
                    billMoney.setText("+" + bean.getAmount());
                    billDateTV.setText(DateUtil.getYADWBS(Long.parseLong(bean.getOprateTime())));

                    startDateTV.setText(DateUtil.getYMD(bean.getRecords().get(1).getDatetime()));
                    endDateTV.setText(DateUtil.getYMD(bean.getRecords().get(2).getDatetime()));

                    billTypeTV.setText(bean.getRecords().get(0).getDetail());
                    billTypeTV.setTextColor(backgroundColor);

                    if (bean.getRecords().get(1).getStatus().equals("1")) {
                        startBottomLine.setBackgroundColor(backgroundColor);
                        startTopLine.setBackgroundColor(backgroundColor);
                        startDateTV.setTextColor(backgroundColor);
                        startTipsTV.setTextColor(backgroundColor);
                        startImg.setImageResource(R.drawable.loan_profit_start_checked);
                    }
                    if (bean.getRecords().get(2).getStatus().equals("1")) {
                        endDateTV.setTextColor(backgroundColor);
                        endTipsTV.setTextColor(backgroundColor);
                        endTopLine.setBackgroundColor(backgroundColor);
                        endImg.setImageResource(R.drawable.loan_profit_end_checked);
                    }
                }
            }
        });
    }

    /**
     * 转出详情
     */
    private void OutDetails() {
        PocketApi.OutoDetails(BillInfoResult.this, getId, getkind, new OnHttpTaskListener<BillInfoBean>() {

            @Override
            public void onStart() {
                ShowDrawDialog(BillInfoResult.this);
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onFinishTask(BillInfoBean bean) {
                DismissDialog();
                billTypeTime.setVisibility(View.VISIBLE);
                if (bean != null && bean.getResultCode() == 0) {

                    if (bean.getType().equals("1")) {
                        billTypeName.setText("银行卡转出");
                    } else {
                        billTypeName.setText("余额转出");
                    }
                    billMoney.setText("-" + bean.getAmount());
                    billMoney.setTextColor(getResources().getColor(R.color.loan_color_outcome));
                    billDateTV.setText(DateUtil.getYADWBS(Long.parseLong(bean.getOprateTime())));
                    billTypeTV.setText(bean.getRecords().get(0).getDetail());
                    billTypeTime.setText(DateUtil.getYMD(bean.getRecords().get(0).getDatetime()));
                    billOptType.setText(bean.getRecords().get(0).getTitle());
                    endDateTV.setText(DateUtil.getYMD(bean.getRecords().get(1).getDatetime()));
                    endTipsTV.setText(bean.getRecords().get(1).getTitle());

                    if (bean.getRecords() != null && bean.getRecords().size() == 2) {
                        if (bean.getRecords().get(0).getStatus().equals("1")) {

                            billTypeTV.setTextColor(backgroundColor);
                            billOptType.setTextColor(backgroundColor);
                            billTypeTime.setTextColor(backgroundColor);
                        }
                        if (bean.getRecords().get(1).getStatus().equals("1")) {

                            endTopLine.setBackgroundColor(backgroundColor);
                            endTipsTV.setTextColor(backgroundColor);
                            endDateTV.setTextColor(backgroundColor);
                            endImg.setImageResource(R.drawable.loan_profit_start_checked);
                        }
                    }


                }

            }
        });
    }
}
