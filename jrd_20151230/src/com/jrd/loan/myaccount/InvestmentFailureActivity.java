package com.jrd.loan.myaccount;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.widget.WindowView;

/**
 * 投资失败
 *
 * @author Aaron
 */
public class InvestmentFailureActivity extends BaseActivity {
  private WindowView windowView;
  private TextView investFailureCue;

  @Override
  protected int loadWindowLayout() {
    return R.layout.loan_loan_investment_failure;
  }

  @Override
  protected void initTitleBar() {
    windowView = (WindowView) findViewById(R.id.windowView);
    windowView.setTitle(R.string.loan_InvestmentFailure);
    windowView.setVisibility(View.VISIBLE);
    windowView.setLeftButtonClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  @Override
  protected void initViews() {
    investFailureCue = (TextView) findViewById(R.id.loan_investFailureCue);
    investFailureCue.setText(getIntent().getStringExtra("msg"));

  }

}
