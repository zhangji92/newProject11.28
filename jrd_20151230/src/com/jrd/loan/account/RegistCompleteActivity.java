package com.jrd.loan.account;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jrd.loan.MainActivity;
import com.jrd.loan.R;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.constant.Const.Extra;
import com.jrd.loan.constant.Const.Type;
import com.jrd.loan.shared.prefs.AppInfoPrefs;

public class RegistCompleteActivity extends BaseActivity {
    private Intent mIntent;
    private Context context;
    private Button investmentBtn;// 投资项目
    private Button couponBtn;// 我的优惠券
    private TextView titleTv;
    private TextView couponTv;

    @Override
    protected int loadWindowLayout() {
        context = RegistCompleteActivity.this;
        return R.layout.loan_activity_regist_complete;
    }

    @Override
    protected void initViews() {
        titleTv = (TextView) findViewById(R.id.loan_regist_complete_tvTitle);
        titleTv.setText(R.string.loan_account_regist_success_title);
        couponTv = (TextView) findViewById(R.id.loan_regist_complete_getcoupon_tv);
        couponBtn = (Button) findViewById(R.id.loan_regist_complete_couponbtn);
        investmentBtn = (Button) findViewById(R.id.loan_regist_complete_investmentbtn);
        investmentBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mIntent = new Intent(context, MainActivity.class);
                mIntent.putExtra(Extra.Select_Info, 1);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                finish();
            }
        });
        couponBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AppInfoPrefs.setIntentToCoupon(true);
                mIntent = new Intent(context, MainActivity.class);
                mIntent.putExtra(Extra.Select_Info, 2);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                finish();
            }
        });
        SpannableStringBuilder builder = new SpannableStringBuilder(couponTv.getText().toString());
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.loan_color_regist_complete_pinknormal));
        builder.setSpan(span, 5, couponTv.getText().length() - 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        couponTv.setText(builder);
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
