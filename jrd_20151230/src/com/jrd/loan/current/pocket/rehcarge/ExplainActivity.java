package com.jrd.loan.current.pocket.rehcarge;

import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.activity.WebViewActivity;
import com.jrd.loan.api.WebApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.base.JrdConfig;
import com.jrd.loan.bean.PersonalInfo;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

public class ExplainActivity extends BaseActivity implements OnClickListener {

    private ImageView agreementImage;
    private Button nextBtn;
    private PersonalInfo userInfo;
    private int getIntentCode; //从我的银行卡界面跳转过来的

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_explain_activity;
    }

    @Override
    protected void initTitleBar() {
        getIntentCode = getIntent().getIntExtra("recharge", 0);
        WindowView windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle("快捷卡说明");
        windowView.setLeftButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void initViews() {
        userInfo = UserInfoPrefs.loadLastLoginUserProfile();
        LinearLayout agreementLayout = (LinearLayout) findViewById(R.id.loan_explain_agreementLayout);
        agreementLayout.setOnClickListener(this);
        agreementImage = (ImageView) findViewById(R.id.loan_explain_agreementImg);
        agreementImage.setSelected(true);

        // 服务协议及风险提示
        TextView agreementTV = (TextView) findViewById(R.id.loan_explain_agreementTV);
        agreementTV.setOnClickListener(this);

        String text = agreementTV.getText().toString();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new RelativeSizeSpan(1.2f), 0, text.length(), 0);
        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.loan_current_text_color_blue)), 18, text.length(), 0);
        agreementTV.setText(builder);

        nextBtn = (Button) findViewById(R.id.loan_explain_nextBtn);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loan_explain_agreementLayout:

                if (agreementImage.isSelected()) {
                    agreementImage.setSelected(false);
                    nextBtn.setClickable(false);
                    nextBtn.setSelected(true);
                } else {
                    agreementImage.setSelected(true);
                    nextBtn.setClickable(true);
                    nextBtn.setSelected(false);
                }

                break;
            case R.id.loan_explain_nextBtn:
                if (!agreementImage.isSelected()) {
                    ToastUtil.showShort(ExplainActivity.this, "勾选同意后才可点击下一步哦!");
                    return;
                }

                // 判断用户是否重新绑定过快捷卡 1是已经重新绑定过 0是没有
                if (UserInfoPrefs.loadLastLoginUserProfile().boundCardFlag.equals("0")) {
                    Intent intent = new Intent(ExplainActivity.this, RechargeActivity.class);
                    if (getIntentCode != 0) {
                        intent.putExtra("recharge", getIntentCode);
                    }
                    startActivity(intent);
                } else {
                    startActivity(new Intent(ExplainActivity.this, RechargeOldActivity.class));
                }
                AppInfoPrefs.setFristRecharge(ExplainActivity.this, false);
                finish();
                break;
            case R.id.loan_explain_agreementTV: // 服务协议及风险提示
                showServiceCompactAndRemindTips();
                break;
        }
    }

    private void showServiceCompactAndRemindTips() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("htmlUrl", JrdConfig.getBaseUrl() + WebApi.FinanceApi.SERVICE_COMPACT_AND_REMIND_TIPS);
        intent.putExtra("htmlTitle", getString(R.string.loan_service_compact_and_remind_tips));
        startActivity(intent);
    }
}
