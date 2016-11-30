package com.jrd.loan.finance;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.jrd.loan.R;
import com.jrd.loan.api.WebApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.fragment.ProjectDetailsFragment;
import com.jrd.loan.fragment.RepaymentFragment;
import com.jrd.loan.fragment.RiskControlFragment;
import com.jrd.loan.fragment.TenderRecordFragment;
import com.jrd.loan.widget.WindowView;

public class TenderRecordAct extends BaseActivity {

    private WindowView windowView;
    public static String mockId;
    private FragmentManager fragmentManager;
    private TenderRecordFragment tenderFragment;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_tender_record_act;
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle("投标记录");
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        this.fragmentManager = getFragmentManager();
        mockId = getIntent().getStringExtra("mockId");
        changeFragment();
    }

    private void changeFragment() {
        FragmentTransaction trans = this.fragmentManager.beginTransaction();
        if (tenderFragment == null) {
            this.tenderFragment = new TenderRecordFragment();
            Bundle bundle = new Bundle();
            bundle.putString("mockId", mockId);
            bundle.putString("url", WebApi.FinanceApi.GETPROJECTRECORDS);
            this.tenderFragment.setArguments(bundle);
        }
        trans.replace(R.id.loan_tender_record_layout, this.tenderFragment);
        trans.commit();
    }
}
