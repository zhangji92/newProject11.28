package com.jrd.loan.myaccount;

import android.view.View;
import android.view.View.OnClickListener;

import com.jrd.loan.R;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.widget.WindowView;

public class CompleteActivity extends BaseActivity {

    private WindowView windowView;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_compleate;
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(getResources().getString(R.string.loan_withdraw));
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

    }

    @Override
    protected void initViews() {
        findViewById(R.id.loan_compleate_enterBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                finish();
            }
        });

    }

}
