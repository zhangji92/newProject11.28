package com.jrd.loan.finance;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jrd.loan.R;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.fragment.AmongThemFragment;
import com.jrd.loan.fragment.InvestmentFragment;
import com.jrd.loan.fragment.RepaymentEndFragment;
import com.jrd.loan.widget.WindowView;

/**
 * 投资记录
 *
 * @author Aaron
 */
public class InvestRecordActivity extends BaseActivity {
    private WindowView windowView;

    private Boolean mBoolean = true;

    private final static int BOTTOM_MENU_HOME_PAGE = 0;
    private final static int BOTTOM_MENU_FINANCE = 1;
    public final static int BOTTOM_MENU_MY_ACCOUNT = 2;

    private int currMenu = BOTTOM_MENU_HOME_PAGE;

    private FragmentManager fragmentManager;

    private AmongThemFragment mAmongThemFragment;// 还款中
    private InvestmentFragment mInvestmentFragment;// 投资中
    private RepaymentEndFragment mRepaymentEndFragment; // 已还款

    private RadioGroup mRadioGroup;
    private RadioButton mAmongThem;// 还款中
    private RadioButton mInvestment; // 投资中
    private RadioButton mRepaymentEnd; // 已还款

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_investrecord;
    }


    @Override
    protected void initViews() {
        this.fragmentManager = getFragmentManager();

        mRadioGroup = (RadioGroup) findViewById(R.id.loan_investrecord_radio);
        mRadioGroup.setOnCheckedChangeListener(btnClickListener);

        // 还款中
        this.mAmongThem = (RadioButton) findViewById(R.id.loan_investrecord_state_payback);
        this.mAmongThem.setChecked(true);

        // 投资中
        this.mInvestment = (RadioButton) findViewById(R.id.loan_investrecord_investment);

        // 已还款
        this.mRepaymentEnd = (RadioButton) findViewById(R.id.loan_investrecord_repayment_end);

        if (this.currMenu == BOTTOM_MENU_HOME_PAGE) {
            this.mAmongThem.performClick();
        } else if (this.currMenu == BOTTOM_MENU_FINANCE) {
            this.mInvestment.performClick();
        } else if (this.currMenu == BOTTOM_MENU_MY_ACCOUNT) {
            this.mRepaymentEnd.performClick();
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
                case R.id.loan_investrecord_state_payback: // 还款中
                    showProjectDetails();
                    mAmongThem.setChecked(true);
                    break;
                case R.id.loan_investrecord_investment: // 投资中
                    showRiskControl();
                    break;
                case R.id.loan_investrecord_repayment_end: // 已还款
                    showMoreRepayment();
                    break;
            }
        }

    };

    // 还款中
    private void showProjectDetails() {
        this.changeMenu(BOTTOM_MENU_HOME_PAGE);
    }

    // 投资中
    private void showRiskControl() {
        this.changeMenu(BOTTOM_MENU_FINANCE);
    }

    // 已还款
    private void showMoreRepayment() {
        this.changeMenu(BOTTOM_MENU_MY_ACCOUNT);
    }

    private void changeMenu(int menu) {
        this.currMenu = menu;

        this.setCurrMenuState();
        this.changeFragment();
    }

    private void setCurrMenuState() {
        if (this.currMenu == BOTTOM_MENU_HOME_PAGE) {// 还款中
            this.mAmongThem.setSelected(true);
            this.mInvestment.setSelected(false);
            this.mRepaymentEnd.setSelected(false);
        } else if (this.currMenu == BOTTOM_MENU_FINANCE) {// 投资中
            this.mAmongThem.setSelected(false);
            this.mInvestment.setSelected(true);
            this.mRepaymentEnd.setSelected(false);
        } else if (this.currMenu == BOTTOM_MENU_MY_ACCOUNT) { // 已还款
            this.mAmongThem.setSelected(false);
            this.mInvestment.setSelected(false);
            this.mRepaymentEnd.setSelected(true);
        }
    }

    private void changeFragment() {
        FragmentTransaction trans = this.fragmentManager.beginTransaction();

        if (this.currMenu == BOTTOM_MENU_HOME_PAGE) {// 还款中

            if (mAmongThemFragment == null) {
                this.mAmongThemFragment = new AmongThemFragment();
                trans.replace(R.id.loan_investrecord_Layout, this.mAmongThemFragment);
            } else {
                trans.replace(R.id.loan_investrecord_Layout, this.mAmongThemFragment);
            }

            if (this.mInvestmentFragment != null) {
                trans.remove(this.mInvestmentFragment);
                this.mInvestmentFragment = null;
            }

            if (this.mRepaymentEndFragment != null) {
                trans.remove(this.mRepaymentEndFragment);
                this.mRepaymentEndFragment = null;
            }

        } else if (this.currMenu == BOTTOM_MENU_FINANCE) {// 投资中

            if (this.mAmongThemFragment != null) {
                trans.remove(this.mAmongThemFragment);
                this.mAmongThemFragment = null;
            }

            if (mInvestmentFragment == null) {
                this.mInvestmentFragment = new InvestmentFragment();
                trans.replace(R.id.loan_investrecord_Layout, this.mInvestmentFragment);
            } else {
                trans.replace(R.id.loan_investrecord_Layout, this.mInvestmentFragment);
            }

            if (this.mRepaymentEndFragment != null) {
                trans.remove(this.mRepaymentEndFragment);
                this.mRepaymentEndFragment = null;
            }

        } else if (this.currMenu == BOTTOM_MENU_MY_ACCOUNT) {// 已还款

            if (this.mAmongThemFragment != null) {
                trans.remove(this.mAmongThemFragment);
                this.mAmongThemFragment = null;
            }

            if (this.mInvestmentFragment != null) {
                trans.remove(this.mInvestmentFragment);
                this.mInvestmentFragment = null;
            }

            if (mRepaymentEndFragment == null) {
                this.mRepaymentEndFragment = new RepaymentEndFragment();
                trans.replace(R.id.loan_investrecord_Layout, this.mRepaymentEndFragment);
            } else {
                trans.replace(R.id.loan_investrecord_Layout, this.mRepaymentEndFragment);
            }

        }

        trans.commit();
    }


    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(R.string.loan_investment_record);
        windowView.setVisibility(View.VISIBLE);
        windowView.setLeftButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
