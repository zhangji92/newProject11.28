package com.jrd.loan.myaccount;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.fragment.BeenUsedFragment;
import com.jrd.loan.fragment.ExpiredFragment;
import com.jrd.loan.fragment.NotUsedFragment;
import com.jrd.loan.widget.WindowView;

import org.w3c.dom.Text;

/**
 * 优惠券
 *
 * @author Aaron
 */
public class CouponsActivity extends BaseActivity {
    private Boolean mBoolean = true;

    private final static int BOTTOM_MENU_HOME_PAGE = 0;
    private final static int BOTTOM_MENU_FINANCE = 1;
    public final static int BOTTOM_MENU_MY_ACCOUNT = 2;

    private int currMenu = BOTTOM_MENU_HOME_PAGE;

    private FragmentManager fragmentManager;

    private NotUsedFragment mNotUsedFragment;// 未使用
    private BeenUsedFragment mBeenUsedFragment;// 已使用
    private ExpiredFragment mExpiredFragment; // 已过期

    private RadioGroup mRadioGroup;
    private RadioButton mNotUsed;// 未使用
    private RadioButton mbeenUsed; // 已使用
    private RadioButton mExpired; // 已过期

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_coupons;
    }


    @Override
    protected void initViews() {
        this.fragmentManager = getFragmentManager();

        mRadioGroup = (RadioGroup) findViewById(R.id.loan_coupons_radio);
        mRadioGroup.setOnCheckedChangeListener(btnClickListener);

        // 未使用
        this.mNotUsed = (RadioButton) findViewById(R.id.loan_coupons_notUsed);
        this.mNotUsed.setChecked(true);

        // 已使用
        this.mbeenUsed = (RadioButton) findViewById(R.id.loan_coupons_beenUsed);

        // 已过期
        this.mExpired = (RadioButton) findViewById(R.id.loan_coupons_expired);

        if (this.currMenu == BOTTOM_MENU_HOME_PAGE) {
            this.mNotUsed.performClick();
        } else if (this.currMenu == BOTTOM_MENU_FINANCE) {
            this.mbeenUsed.performClick();
        } else if (this.currMenu == BOTTOM_MENU_MY_ACCOUNT) {
            this.mExpired.performClick();
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
                case R.id.loan_coupons_notUsed: // 未使用
                    showProjectDetails();
                    mNotUsed.setChecked(true);
                    break;
                case R.id.loan_coupons_beenUsed: // 已使用
                    showRiskControl();
                    break;
                case R.id.loan_coupons_expired: // 已过期
                    showMoreRepayment();
                    break;
            }
        }

    };

    // 未使用
    private void showProjectDetails() {
        this.changeMenu(BOTTOM_MENU_HOME_PAGE);
    }

    // 已使用
    private void showRiskControl() {
        this.changeMenu(BOTTOM_MENU_FINANCE);
    }

    // 已过期
    private void showMoreRepayment() {
        this.changeMenu(BOTTOM_MENU_MY_ACCOUNT);
    }

    private void changeMenu(int menu) {
        this.currMenu = menu;

        this.setCurrMenuState();
        this.changeFragment();
    }

    private void setCurrMenuState() {
        if (this.currMenu == BOTTOM_MENU_HOME_PAGE) {// 未使用
            this.mNotUsed.setSelected(true);
            this.mbeenUsed.setSelected(false);
            this.mExpired.setSelected(false);
        } else if (this.currMenu == BOTTOM_MENU_FINANCE) {// 已使用
            this.mNotUsed.setSelected(false);
            this.mbeenUsed.setSelected(true);
            this.mExpired.setSelected(false);
        } else if (this.currMenu == BOTTOM_MENU_MY_ACCOUNT) { // 已过期
            this.mNotUsed.setSelected(false);
            this.mbeenUsed.setSelected(false);
            this.mExpired.setSelected(true);
        }
    }

    private void changeFragment() {
        FragmentTransaction trans = this.fragmentManager.beginTransaction();

        if (this.currMenu == BOTTOM_MENU_HOME_PAGE) {// 未使用

            if (mNotUsedFragment == null) {
                this.mNotUsedFragment = new NotUsedFragment();
                trans.replace(R.id.loan_coupons_Layout, this.mNotUsedFragment);
            } else {
                trans.replace(R.id.loan_coupons_Layout, this.mNotUsedFragment);
            }

            if (this.mBeenUsedFragment != null) {
                trans.remove(this.mBeenUsedFragment);
                this.mBeenUsedFragment = null;
            }

            if (this.mExpiredFragment != null) {
                trans.remove(this.mExpiredFragment);
                this.mExpiredFragment = null;
            }

        } else if (this.currMenu == BOTTOM_MENU_FINANCE) {// 已使用

            if (this.mNotUsedFragment != null) {
                trans.remove(this.mNotUsedFragment);
                this.mNotUsedFragment = null;
            }

            if (mBeenUsedFragment == null) {
                this.mBeenUsedFragment = new BeenUsedFragment();
                trans.replace(R.id.loan_coupons_Layout, this.mBeenUsedFragment);
            } else {
                trans.replace(R.id.loan_coupons_Layout, this.mBeenUsedFragment);
            }

            if (this.mExpiredFragment != null) {
                trans.remove(this.mExpiredFragment);
                this.mExpiredFragment = null;
            }

        } else if (this.currMenu == BOTTOM_MENU_MY_ACCOUNT) {// 已过期

            if (this.mNotUsedFragment != null) {
                trans.remove(this.mNotUsedFragment);
                this.mNotUsedFragment = null;
            }

            if (this.mBeenUsedFragment != null) {
                trans.remove(this.mBeenUsedFragment);
                this.mBeenUsedFragment = null;
            }

            if (mExpiredFragment == null) {
                this.mExpiredFragment = new ExpiredFragment();
                trans.replace(R.id.loan_coupons_Layout, this.mExpiredFragment);
            } else {
                trans.replace(R.id.loan_coupons_Layout, this.mExpiredFragment);
            }

        }

        trans.commit();
    }


    @Override
    protected void initTitleBar() {
        Button backBtn = (Button) findViewById(R.id.btnLeft);
        TextView titleTV = (TextView) findViewById(R.id.tvTitle);
        titleTV.setText(R.string.loan_account_coupon_text);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
