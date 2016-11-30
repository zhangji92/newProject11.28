package com.jrd.loan.current.pocket;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jrd.loan.R;
import com.jrd.loan.account.LoginActivity;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.api.PocketApi;
import com.jrd.loan.api.WebApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.current.pocket.rehcarge.BindBankCardActivity;
import com.jrd.loan.current.pocket.rehcarge.IdCheckActivity;
import com.jrd.loan.fragment.ProjectDetailsFragment;
import com.jrd.loan.fragment.RiskControlFragment;
import com.jrd.loan.fragment.TenderRecordFragment;
import com.jrd.loan.myaccount.AccountSwitchToActivity;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.widget.WindowView;

/**
 * 标的详情
 *
 * @author Javen
 */
public class PocketDetailsActivity extends BaseActivity {

    private Boolean mBoolean = true;
    private final static int BOTTOM_MENU_HOME_PAGE = 0;
    private final static int BOTTOM_MENU_FINANCE = 1;
    public final static int BOTTOM_MENU_MY_ACCOUNT = 2;
    private int currMenu = BOTTOM_MENU_HOME_PAGE;
    private FragmentManager fragmentManager;
    private ProjectDetailsFragment mDetailsFragment;
    private RiskControlFragment mControlFragment;
    private TenderRecordFragment mTenderRecordFragment;
    private RadioButton mProjectDetails;// 产品介绍
    private RadioButton mRiskControl; // 风控管理
    private RadioButton mRepayment; // 投资记录
    private String amount;
    private Bundle bundle = new Bundle();

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_more_details;
    }

    @Override
    protected void initViews() {
        amount = getIntent().getStringExtra("amount");
        bundle.putString("mockId", getIntent().getStringExtra("mockId"));
        bundle.putString("url", WebApi.PocketApi.POCIKETINVESTRECORD);
        bundle.putInt("flag", 10010);

        this.fragmentManager = getFragmentManager();

        RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.loan_details_radio);
        mRadioGroup.setOnCheckedChangeListener(btnClickListener);

        // 项目介绍
        this.mProjectDetails = (RadioButton) findViewById(R.id.loan_more_project_details);
        this.mProjectDetails.setText(R.string.loan_project_introduction);
        this.mProjectDetails.setChecked(true);

        // 风控管理
        this.mRiskControl = (RadioButton) findViewById(R.id.loan_more_risk_control);
        this.mRiskControl.setText(R.string.loan_risk_control);

        // 投资记录
        this.mRepayment = (RadioButton) findViewById(R.id.loan_more_repayment);
        this.mRepayment.setText(R.string.loan_investment_record);

        // 投资按钮
        Button enterBtn = (Button) findViewById(R.id.loan_details_btn);
        enterBtn.setOnClickListener(enterBtnClickListener);
        enterBtn.setVisibility(View.VISIBLE);

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
                case R.id.loan_more_project_details: // 项目介绍
                    showProjectDetails();
                    mProjectDetails.setChecked(true);
                    break;
                case R.id.loan_more_risk_control: // 风控管理
                    showRiskControl();
                    break;
                case R.id.loan_more_repayment: // 投资记录
                    showMoreRepayment();
                    break;
            }
        }
    };
    private OnClickListener enterBtnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!UserInfoPrefs.isLogin()) {// 判断用户是否登录
                startActivity(new Intent(PocketDetailsActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.loan_bottom_to_top, R.anim.loan_default);
            } else if (UserInfoPrefs.loadLastLoginUserProfile().idNumberFlag.equals("0")){
                startActivity(new Intent(PocketDetailsActivity.this, IdCheckActivity.class));
            } else {
                Intent intent = new Intent(PocketDetailsActivity.this, AccountSwitchToActivity.class);
                intent.putExtra("amount", amount);
                startActivity(intent);
            }
        }
    };

    // 项目介绍
    private void showProjectDetails() {
        this.changeMenu(BOTTOM_MENU_HOME_PAGE);
    }

    // 风控管理
    private void showRiskControl() {
        this.changeMenu(BOTTOM_MENU_FINANCE);
    }

    // 投资记录
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
        if (this.currMenu == BOTTOM_MENU_HOME_PAGE) {// 项目介绍
            if (mDetailsFragment == null) {
                this.mDetailsFragment = new ProjectDetailsFragment();
                this.mDetailsFragment.setArguments(bundle);
            }
            trans.replace(R.id.loan_moreLayout, this.mDetailsFragment);
            if (this.mControlFragment != null) {
                trans.remove(this.mControlFragment);
                this.mControlFragment = null;
            }
            if (this.mTenderRecordFragment != null) {
                trans.remove(this.mTenderRecordFragment);
                this.mTenderRecordFragment = null;
            }
        } else if (this.currMenu == BOTTOM_MENU_FINANCE) {// 风控管理
            if (this.mDetailsFragment != null) {
                trans.remove(this.mDetailsFragment);
                this.mDetailsFragment = null;
            }
            if (mControlFragment == null) {
                this.mControlFragment = new RiskControlFragment();
                this.mControlFragment.setArguments(bundle);
            }
            trans.replace(R.id.loan_moreLayout, this.mControlFragment);
            if (this.mTenderRecordFragment != null) {
                trans.remove(this.mTenderRecordFragment);
                this.mTenderRecordFragment = null;
            }
        } else if (this.currMenu == BOTTOM_MENU_MY_ACCOUNT) {// 投资记录
            if (this.mDetailsFragment != null) {
                trans.remove(this.mDetailsFragment);
                this.mDetailsFragment = null;
            }
            if (this.mControlFragment != null) {
                trans.remove(this.mControlFragment);
                this.mControlFragment = null;
            }
            if (mTenderRecordFragment == null) {
                this.mTenderRecordFragment = new TenderRecordFragment();
                this.mTenderRecordFragment.setArguments(bundle);
            }
            trans.replace(R.id.loan_moreLayout, this.mTenderRecordFragment);
        }
        trans.commit();
    }

    @Override
    protected void initTitleBar() {
        WindowView windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle(R.string.loan_project_details_text);
        windowView.setVisibility(View.VISIBLE);
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        windowView.setRightButtonClickListener(getString(R.string.loan_agree_text), new OnClickListener() {

            @Override
            public void onClick(View v) {

                // 跳转到协议界面
                Intent intent = new Intent(PocketDetailsActivity.this, WebViewActivity.class);
                intent.putExtra("htmlTitle", "活期标投资协议");
                intent.putExtra("htmlUrl", JrdConfig.getBaseUrl() + PocketApi.POCKET_AGREE);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.loan_top_to_buttom, R.anim.loan_default);
    }
}
