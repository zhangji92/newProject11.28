package com.jrd.loan.myaccount;

import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.SplashActivity;
import com.jrd.loan.api.StatisticsApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.HuoqiList;
import com.jrd.loan.constant.Const;
import com.jrd.loan.util.DateUtil;
import com.jrd.loan.widget.WindowView;

public class TurnOutToSucceedActivity extends BaseActivity {
  private RelativeLayout billStartLayout;
  private TextView billTypeName;
  private TextView billMoney;
  private TextView billDateTV;

  private TextView billOptType;
  private TextView billTypeTV;
  private TextView billTypeTime;

  private TextView endDateTV;
  private ImageView endImg;
  private TextView endTipsTV;

  private View endTopLine;

  private String type;// 类型
  private String name;
  private String amount;// 转入金额
  private String oprateTime;// 时间

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

    this.billStartLayout = (RelativeLayout) findViewById(R.id.billStartLayout);
    billStartLayout.setVisibility(View.GONE);

    this.billTypeName = (TextView) findViewById(R.id.billTypeName);
    this.billMoney = (TextView) findViewById(R.id.billMoney);
    this.billDateTV = (TextView) findViewById(R.id.billDate);

    this.billOptType = (TextView) findViewById(R.id.billOptType);
    this.billTypeTV = (TextView) findViewById(R.id.billOptName);
    this.billTypeTime = (TextView) findViewById(R.id.billOptTime);

    this.endDateTV = (TextView) findViewById(R.id.billProfitEndDate);
    this.endImg = (ImageView) findViewById(R.id.billEndImg);
    this.endTipsTV = (TextView) findViewById(R.id.billProfitEndTips);

    this.endTopLine = findViewById(R.id.billEndLineTop);

    initData();

  }

  private void initData() {
    Intent intent = getIntent();
    name = getIntent().getStringExtra("name");
    type = intent.getExtras().getString("type", "");
    amount = intent.getExtras().getString("amount", "");
    oprateTime = intent.getExtras().getString("oprateTime", "");

    @SuppressWarnings("unchecked")
    List<HuoqiList> listRecords = (List<HuoqiList>) this.getIntent().getSerializableExtra("records");
    if (type.equals("1")) {
      billTypeName.setText("银行卡转出");
    } else {
      if (!TextUtils.isEmpty(name)) {
        billTypeName.setText(name);
      } else {
        billTypeName.setText("余额转出");
      }
    }

    billMoney.setText("+" + amount);
    billMoney.setTextColor(getResources().getColor(R.color.loan_color_outcome));
    billDateTV.setText(DateUtil.getYADWBS(Long.parseLong(oprateTime)));


    billOptType.setText(listRecords.get(0).getTitle());
    if (type.equals("1")) {
      billTypeTV.setText(listRecords.get(0).getDetail());
    } else {
      billTypeTV.setVisibility(View.GONE);
    }
    billTypeTime.setText(DateUtil.getYMD(Long.parseLong(listRecords.get(0).getDatetime())));

    if (listRecords.get(0).getStatus().equals("1")) {
      if (type.equals("1")) {
        billTypeTV.setTextColor(backgroundColor);
      } else {
        billTypeTV.setVisibility(View.GONE);
      }
      billOptType.setTextColor(backgroundColor);
      billTypeTime.setTextColor(backgroundColor);
    }

    endImg.setImageResource(R.drawable.loan_profit_start_icon);
    endDateTV.setText(listRecords.get(1).getTitle());
    endTipsTV.setText(DateUtil.getYMD(Long.parseLong(listRecords.get(1).getDatetime())));

    if (TextUtils.isEmpty(listRecords.get(1).getStatus())) {
      return;
    }

    if (listRecords.get(1).getStatus().equals("1")) {
      endTopLine.setBackgroundColor(backgroundColor);
      endTipsTV.setTextColor(backgroundColor);
      endDateTV.setTextColor(backgroundColor);
      endImg.setImageResource(R.drawable.loan_profit_start_checked);
    }
  }
}
