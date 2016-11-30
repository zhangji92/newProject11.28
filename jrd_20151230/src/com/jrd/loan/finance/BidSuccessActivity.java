package com.jrd.loan.finance;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jrd.loan.MainActivity;
import com.jrd.loan.R;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.widget.WindowView;

/**
 * 投标成功
 *
 * @author Aaron
 */
public class BidSuccessActivity extends BaseActivity {

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_bid_success_layout;
    }

    @Override
    protected void initViews() {
        // 我的账户
        Button joinSuccessBtn = (Button) findViewById(R.id.loan_join_success_Btn);
        joinSuccessBtn.setOnClickListener(this.btnClickListener);
    }

    @Override
    public void onBackPressed() {
        this.showAccountScreen();
    }

    private void showAccountScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("currMenu", MainActivity.BOTTOM_MENU_MY_ACCOUNT);
        startActivity(intent);
    }

    private final OnClickListener btnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loan_join_success_Btn: // 我的账户
                    showAccountScreen();
                    break;
            }
        }
    };

    @Override
    protected void initTitleBar() {
        WindowView windowView = (WindowView) findViewById(R.id.windowView);

        // 投标成功
        windowView.setTitle(R.string.loan_bid_success);

        // 隐藏返回按钮
        windowView.setLeftButtonVisibility(View.GONE);
    }

}
