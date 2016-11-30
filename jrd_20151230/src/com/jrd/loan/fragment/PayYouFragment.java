package com.jrd.loan.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.account.LoginActivity;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.api.ProjectInfoApi;
import com.jrd.loan.api.WebApi;
import com.jrd.loan.base.BaseFragment;
import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.bean.SevenInfo;
import com.jrd.loan.bean.SfnInfoBean;
import com.jrd.loan.constant.Const.Type;
import com.jrd.loan.current.pocket.rehcarge.BindBankCardActivity;
import com.jrd.loan.finance.ImmediatelyJoinActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.DigitUtil;
import com.jrd.loan.util.ToastUtil;

/**
 * 7付你
 *
 * @author Aaron
 */
public class PayYouFragment extends BaseFragment implements OnClickListener {
    private final static String BASE_URL = JrdConfig.getBaseUrl();
    private Context mContext;
    private ImageView sevenPay;
    private TextView mUpdateDesc;
    private TextView mAnnualRate;
    private TextView mInvestmentPeriod;
    private TextView mSurplusAmount;
    private TextView mSurpAmount;

    private TextView mProDetItem;
    private LinearLayout mAaddItem;
    private TextView mdinanceAddTest;
    private Button mJoinBtn;
    private Button mIsFull;
    private Boolean isButton = false;

    private SevenInfo mSevenInfo;

    @Override
    protected int getLoadViewId() {
        return R.layout.loan_finance_pay_you;
    }

    @Override
    protected void initView(View view) {
        mContext = getActivity();
        mSevenInfo = new SevenInfo();
        sevenPay = (ImageView) view.findViewById(R.id.sevenPay);
        mUpdateDesc = (TextView) view.findViewById(R.id.loan_finance_updateDesc);
        mAnnualRate = (TextView) view.findViewById(R.id.loan_annual_interest_rate_test);
        mInvestmentPeriod = (TextView) view.findViewById(R.id.loan_project_duration_test);
        mSurplusAmount = (TextView) view.findViewById(R.id.loan_surplus_amount_test);
        mSurpAmount = (TextView) view.findViewById(R.id.loan_surplus_amount);

        mProDetItem = (TextView) view.findViewById(R.id.loan_finance_pro_det_item);
        mAaddItem = (LinearLayout) view.findViewById(R.id.loan_finance_add_item);
        mdinanceAddTest = (TextView) view.findViewById(R.id.loan_finance_dinance_addTest);
        mJoinBtn = (Button) view.findViewById(R.id.loan_finance_joinBtn);
        mIsFull = (Button) view.findViewById(R.id.loan_finance_is_full);
        mProDetItem.setOnClickListener(this);
        mAaddItem.setOnClickListener(this);
        mJoinBtn.setOnClickListener(this);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        LayoutParams layoutParams = new LayoutParams(screenWidth, (int) (241.0 / 720 * screenWidth));
        sevenPay.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        get7fnInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 产品详情
            case R.id.loan_finance_pro_det_item:
                showProductDetails();
                break;
            // 加入记录
            case R.id.loan_finance_add_item:
                showAddRecord();
                break;
            // 立即加入
            case R.id.loan_finance_joinBtn:
                if (isButton) {
                    showImmediatelyJoin();
                }
                break;
        }
    }

    // 产品详情
    private void showProductDetails() {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("htmlUrl", new StringBuffer(BASE_URL).append(WebApi.SevenPayYou.GET7FNDETAILINFO).toString());
        intent.putExtra("htmlTitle", getActivity().getResources().getString(R.string.loan_finance_pro_det));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // 加入记录
    private void showAddRecord() {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("htmlUrl", new StringBuffer(BASE_URL).append(WebApi.SevenPayYou.GET7FNRECORDINFO).append("?projectId=").append(mSevenInfo.getProjectId()).toString());
        intent.putExtra("htmlTitle", getActivity().getResources().getString(R.string.loan_finance_add_record));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // 立即加入
    @SuppressLint("ResourceAsColor")
    private void showImmediatelyJoin() {

        if (UserInfoPrefs.isLogin()) {// 立即投标
            if (UserInfoPrefs.loadLastLoginUserProfile().getIdNumberInfo().equals(Type.Psw_NotSet)) {// 立即投标
                Intent intent = new Intent(mContext, BindBankCardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, ImmediatelyJoinActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("mSevenInfo", mSevenInfo);
                startActivity(intent);
            }
        } else {// 登录
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    // 查询7付你信息
    private void get7fnInfo() {
        ProjectInfoApi.get7fnInfo(mContext, new OnHttpTaskListener<SfnInfoBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowProDialog(mContext);
            }

            @Override
            public void onFinishTask(SfnInfoBean bean) {
                DismissDialog();
                if (bean.getResultCode() == 0) {
                    set7fnInfo(bean);
                } else {
                    ToastUtil.showShort(mContext, bean.getResultMsg());
                }
            }
        });
    }

    // 7付你赋值
    @SuppressLint("ResourceAsColor")
    private void set7fnInfo(SfnInfoBean bean) {
        mSevenInfo = bean.getSevenInfo();

        if (mSevenInfo.getIsInvestment() == true) {
            isButton = true;
            mJoinBtn.setVisibility(View.VISIBLE);
            mIsFull.setVisibility(View.GONE);
        } else if (mSevenInfo.getIsInvestment() == false) {
            isButton = false;
            mJoinBtn.setVisibility(View.GONE);
            mIsFull.setVisibility(View.VISIBLE);
            ToastUtil.showShort(mContext, bean.getResultMsg());
        }

        mUpdateDesc.setText(bean.getSevenInfo().getUpdateDesc());// 7付你更新描述
        mAnnualRate.setText(bean.getSevenInfo().getAnnualRate());// 年化利率如：7.0%

        mInvestmentPeriod.setText(DigitUtil.getMonth(bean.getSevenInfo().getInvestmentPeriod()));// 7付你期限

        // 可投金额
        mSurplusAmount.setText(DigitUtil.getMoneys(bean.getSevenInfo().getInvestmentAmount()));

        if (Double.parseDouble(bean.getSevenInfo().getInvestmentAmount()) < 10000) {
            mSurpAmount.setText("元");
        } else {
            mSurpAmount.setText("万");
        }

        if (!(bean.getSevenInfo().getInverstRecordCount().equals("0"))) {
            mdinanceAddTest.setText(bean.getSevenInfo().getInverstRecordCount() + "人已投");// 加入记录
        }
    }
}
