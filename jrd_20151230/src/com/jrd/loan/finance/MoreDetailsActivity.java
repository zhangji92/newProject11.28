package com.jrd.loan.finance;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jrd.loan.R;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.fragment.ProjectDetailsFragment;
import com.jrd.loan.fragment.RepaymentFragment;
import com.jrd.loan.fragment.RiskControlFragment;
import com.jrd.loan.widget.WindowView;

/**
 * 更多详情
 *
 * @author Aaron
 */
public class MoreDetailsActivity extends BaseActivity {

    private WindowView windowView;
    private Boolean mBoolean = true;
    private final static int BOTTOM_MENU_HOME_PAGE = 0;
    private final static int BOTTOM_MENU_FINANCE = 1;
    public final static int BOTTOM_MENU_MY_ACCOUNT = 2;
    private int currMenu = BOTTOM_MENU_HOME_PAGE;
    private FragmentManager fragmentManager;
    private ProjectDetailsFragment mDetailsFragment;
    private RiskControlFragment mControlFragment;
    private RepaymentFragment mRepaymentFragment;
    private RadioGroup mRadioGroup;
    private RadioButton mProjectDetails;// 项目详情
    private RadioButton mRiskControl; // 风控信息
    private RadioButton mRepayment; // 还款表现
    private String mockId;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_more_details;
    }

    @Override
    protected void initViews() {
        this.mockId = ProjectIntroduceActivity.projectList.getMockId();
        bundle = new Bundle();
        bundle.putString("mockId", mockId);
        bundle.putInt("flag", 10011);
        this.fragmentManager = getFragmentManager();
        mRadioGroup = (RadioGroup) findViewById(R.id.loan_details_radio);
        mRadioGroup.setOnCheckedChangeListener(btnClickListener);
        // 项目详情
        this.mProjectDetails = (RadioButton) findViewById(R.id.loan_more_project_details);
        this.mProjectDetails.setChecked(true);
        // 风控信息
        this.mRiskControl = (RadioButton) findViewById(R.id.loan_more_risk_control);
        // 还款表现
        this.mRepayment = (RadioButton) findViewById(R.id.loan_more_repayment);
        if (this.currMenu == BOTTOM_MENU_HOME_PAGE) {
            this.mProjectDetails.performClick();
        } else if (this.currMenu == BOTTOM_MENU_FINANCE) {
            this.mRiskControl.performClick();
        } else if (this.currMenu == BOTTOM_MENU_MY_ACCOUNT) {
            this.mRepayment.performClick();
        }
        if (mBoolean) {
            mBoolean = false;
            showProjectDetails();
        }
    }

    private final OnCheckedChangeListener btnClickListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.loan_more_project_details: // 项目详情
                    showProjectDetails();
                    mProjectDetails.setChecked(true);
                    break;
                case R.id.loan_more_risk_control: // 风控信息
                    showRiskControl();
                    break;
                case R.id.loan_more_repayment: // 还款表现
                    showMoreRepayment();
                    break;
            }
        }
    };
    private Bundle bundle;

    // 项目详情
    private void showProjectDetails() {
        this.changeMenu(BOTTOM_MENU_HOME_PAGE);
    }

    // 风控信息
    private void showRiskControl() {
        this.changeMenu(BOTTOM_MENU_FINANCE);
    }

    // 还款表现
    private void showMoreRepayment() {
        this.changeMenu(BOTTOM_MENU_MY_ACCOUNT);
    }

    private void changeMenu(int menu) {
        this.currMenu = menu;
        this.setCurrMenuState();
        this.changeFragment();
    }

    private void setCurrMenuState() {
        if (this.currMenu == BOTTOM_MENU_HOME_PAGE) {// 项目详情
            this.mProjectDetails.setSelected(true);
            this.mRiskControl.setSelected(false);
            this.mRepayment.setSelected(false);
        } else if (this.currMenu == BOTTOM_MENU_FINANCE) {// 风控信息
            this.mProjectDetails.setSelected(false);
            this.mRiskControl.setSelected(true);
            this.mRepayment.setSelected(false);
        } else if (this.currMenu == BOTTOM_MENU_MY_ACCOUNT) { // 还款表现
            this.mProjectDetails.setSelected(false);
            this.mRiskControl.setSelected(false);
            this.mRepayment.setSelected(true);
        }
    }

    private void changeFragment() {
        FragmentTransaction trans = this.fragmentManager.beginTransaction();
        if (this.currMenu == BOTTOM_MENU_HOME_PAGE) {// 项目详情
            if (mDetailsFragment == null) {
                this.mDetailsFragment = new ProjectDetailsFragment();
                this.mDetailsFragment.setArguments(bundle);
            }
            trans.replace(R.id.loan_moreLayout, this.mDetailsFragment);
            if (this.mControlFragment != null) {
                trans.remove(this.mControlFragment);
                this.mControlFragment = null;
            }
            if (this.mRepaymentFragment != null) {
                trans.remove(this.mRepaymentFragment);
                this.mRepaymentFragment = null;
            }
        } else if (this.currMenu == BOTTOM_MENU_FINANCE) {// 风控信息
            if (this.mDetailsFragment != null) {
                trans.remove(this.mDetailsFragment);
                this.mDetailsFragment = null;
            }
            if (mControlFragment == null) {
                this.mControlFragment = new RiskControlFragment();
                this.mControlFragment.setArguments(bundle);
            }
            trans.replace(R.id.loan_moreLayout, this.mControlFragment);
            if (this.mRepaymentFragment != null) {
                trans.remove(this.mRepaymentFragment);
                this.mRepaymentFragment = null;
            }
        } else if (this.currMenu == BOTTOM_MENU_MY_ACCOUNT) {// 还款表现
            if (this.mDetailsFragment != null) {
                trans.remove(this.mDetailsFragment);
                this.mDetailsFragment = null;
            }
            if (this.mControlFragment != null) {
                trans.remove(this.mControlFragment);
                this.mControlFragment = null;
            }
            if (mRepaymentFragment == null) {
                this.mRepaymentFragment = new RepaymentFragment();
                this.mRepaymentFragment.setArguments(bundle);
            }
            trans.replace(R.id.loan_moreLayout, this.mRepaymentFragment);
        }
        trans.commit();
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(R.string.loan_more_detail);
        windowView.setVisibility(View.VISIBLE);
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
