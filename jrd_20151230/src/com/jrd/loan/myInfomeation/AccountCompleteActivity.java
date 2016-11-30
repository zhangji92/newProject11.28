package com.jrd.loan.myInfomeation;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.account.LoginActivity;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.constant.Const.Extra;
import com.jrd.loan.constant.Const.Type;

/**
 * 个人信息相关成功界面,根据intent区分
 */
public class AccountCompleteActivity extends BaseActivity implements OnClickListener {
    private TextView titleTv;
    private TextView completeTv;
    private Button enterBtn;
    private int i = 0;
    private Intent mIntent;
    private Context context;

    @Override
    protected int loadWindowLayout() {
        context = AccountCompleteActivity.this;
        return R.layout.loan_activity_account_complete;
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    protected void initViews() {
        titleTv = (TextView) findViewById(R.id.loan_account_complete_tvTitle);
        completeTv = (TextView) findViewById(R.id.loan_account_complete_tv);
        i = getIntent().getIntExtra(Extra.Select_ID, 0);
        enterBtn = (Button) findViewById(R.id.loan_account_complete_enterbtn);
        if (i == Type.Complete_Psw_Transaction_Reset) {
            titleTv.setText(R.string.loan_account_personal_reset_transactionpsw);
            completeTv.setText(R.string.loan_account_reset_transpsw_complete);
        } else if (i == Type.Complete_Psw_Transaction_Update) {
            titleTv.setText(R.string.loan_account_login_changeloginpsw);
            completeTv.setText(R.string.loan_account_update_transpsw_complete);
        } else if (i == Type.Complete_Psw_Transaction_Set) {
            titleTv.setText(R.string.loan_account_personal_set_transactionpsw);
            completeTv.setText(R.string.loan_account_set_transpsw_complete);
        } else if (i == Type.Complete_Psw_Login_Update) {
            titleTv.setText(R.string.loan_account_login_changeloginpsw);
            completeTv.setText(R.string.loan_account_reset_loginpsw_complete);
        } else if (i == Type.Complete_Psw_Login_Reset) {
            titleTv.setText(R.string.loan_account_login_resetpsw);
            completeTv.setText(R.string.loan_account_reset_loginpsw_complete);
        }
        enterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (i) {
            case Type.Complete_Psw_Transaction_Reset:
                titleTv.setText(R.string.loan_account_personal_reset_transactionpsw);
                completeTv.setText(R.string.loan_account_reset_transpsw_complete);
                mIntent = new Intent(context, UpdateTransactionPswActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                break;
            case Type.Complete_Psw_Transaction_Update:
                titleTv.setText(R.string.loan_account_login_changeloginpsw);
                completeTv.setText(R.string.loan_account_update_transpsw_complete);
                finish();
                break;
            case Type.Complete_Psw_Transaction_Set:
                titleTv.setText(R.string.loan_account_personal_set_transactionpsw);
                completeTv.setText(R.string.loan_account_set_transpsw_complete);
                finish();
                break;
            case Type.Complete_Psw_Login_Update:
                titleTv.setText(R.string.loan_account_login_changeloginpsw);
                completeTv.setText(R.string.loan_account_reset_loginpsw_complete);
                finish();
                break;
            case Type.Complete_Psw_Login_Reset:
                titleTv.setText(R.string.loan_account_login_resetpsw);
                completeTv.setText(R.string.loan_account_reset_loginpsw_complete);
                mIntent = new Intent(context, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                finish();
                break;
            default:
                break;
        }
    }
}
