package com.jrd.loan.myaccount;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.HuoqiList;
import com.jrd.loan.util.DateUtil;
import com.jrd.loan.widget.WindowView;

/**
 * 投资成功
 *
 * @author Aaron
 */
public class InvestmentSuccessActivity extends BaseActivity {
    private WindowView windowView;

    private int backgroundColor;

    private TextView bankCard;// 银行卡
    private TextView time;// 投资时间
    private TextView money;// 投资金额
    private ImageView progressImg;// 根据不同的时间段显示不同的图片
    private TextView transferTime;// 转入时间
    private TextView firstIncome;// 第一笔收益到账
    private TextView startCalEarning;// 开始计算收益

    private TextView detailText1;
    private TextView detailText2;
    private TextView detailText3;

    private String detail;// 转入账户名
    private String amount;// 转入金额
    private String oprateTime;// 时间
    private List<HuoqiList> listRecords;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_investment_success;
    }

    @Override
    protected void initViews() {
        this.backgroundColor = getResources().getColor(R.color.loan_color_btn_normal);
        // 银行卡
        bankCard = (TextView) findViewById(R.id.loan_investment_bank_card);
        // 投资时间
        time = (TextView) findViewById(R.id.loan_investment_time);
        // 投资金额
        money = (TextView) findViewById(R.id.loan_investment_money);
        // 转入时间
        transferTime = (TextView) findViewById(R.id.loan_transfer_time);
        // 第一笔收益到账
        firstIncome = (TextView) findViewById(R.id.loan_first_income);
        // 开始计算收益
        startCalEarning = (TextView) findViewById(R.id.loan_start_calculating_earning);
        // 根据不同的时间段显示不同的图片
        progressImg = (ImageView) findViewById(R.id.loan_progress_img);

        detailText1 = (TextView) findViewById(R.id.loan_detail1);
        detailText2 = (TextView) findViewById(R.id.loan_detail2);
        detailText3 = (TextView) findViewById(R.id.loan_detail3);

        initData();
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        Intent intent = getIntent();
        detail = intent.getExtras().getString("detail", "");
        amount = intent.getExtras().getString("amount", "");
        oprateTime = intent.getExtras().getString("oprateTime", "");


        bankCard.setText(detail);
        time.setText("" + DateUtil.getYADWBS(Long.parseLong(oprateTime)));
        money.setText("+" + amount);

        if (null != this.getIntent().getSerializableExtra("records")) {
            listRecords = (List<HuoqiList>) this.getIntent().getSerializableExtra("records");
            detailText1.setText(listRecords.get(0).getDetail());
            transferTime.setText(DateUtil.getYMD(Long.parseLong(listRecords.get(0).getDatetime())));
            if (listRecords.get(0).getStatus().equals("1")) {
                detailText1.setTextColor(backgroundColor);
                transferTime.setTextColor(backgroundColor);
                progressImg.setImageResource(R.drawable.loan_progress_img01);
            }

            detailText2.setText(listRecords.get(1).getDetail());
            firstIncome.setText(DateUtil.getYMD(Long.parseLong(listRecords.get(1).getDatetime())));
            if (listRecords.get(1).getStatus().equals("1")) {
                detailText2.setTextColor(backgroundColor);
                firstIncome.setTextColor(backgroundColor);
                progressImg.setImageResource(R.drawable.loan_progress_img02);
            }

            detailText3.setText(listRecords.get(2).getDetail());
            startCalEarning.setText(DateUtil.getYMD(Long.parseLong(listRecords.get(2).getDatetime())));
            if (listRecords.get(2).getStatus().equals("1")) {
                detailText3.setTextColor(backgroundColor);
                startCalEarning.setTextColor(backgroundColor);
                progressImg.setImageResource(R.drawable.loan_progress_img03);
            }
        }
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(R.string.loan_InvestmentSuccess);
        windowView.setVisibility(View.VISIBLE);
        windowView.setLeftButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
