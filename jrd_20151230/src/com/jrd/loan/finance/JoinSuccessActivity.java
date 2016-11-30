package com.jrd.loan.finance;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.jrd.loan.MainActivity;
import com.jrd.loan.R;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.widget.WindowView;

/**
 * 加入成功
 *
 * @author Aaron
 */
public class JoinSuccessActivity extends BaseActivity {

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_join_success_layout;
    }

    @Override
    protected void initViews() {
        ImageView sevenPay = (ImageView) findViewById(R.id.sevenPay);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        LayoutParams layoutParams = new LayoutParams(screenWidth, (int) (241.0 / 720 * screenWidth));
        sevenPay.setLayoutParams(layoutParams);

        // 我的账户
        Button joinSuccessBtn = (Button) findViewById(R.id.loan_join_success_Btn);
        joinSuccessBtn.setOnClickListener(this.btnClickListener);
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
    public void onBackPressed() {
        this.showAccountScreen();
    }

    private void showAccountScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("currMenu", MainActivity.BOTTOM_MENU_MY_ACCOUNT);
        startActivity(intent);
    }

    @Override
    protected void initTitleBar() {
        WindowView windowView = (WindowView) findViewById(R.id.windowView);

        // 项目介绍
        windowView.setTitle(R.string.loan_join_success);

        // 隐藏返回按钮
        windowView.setLeftButtonVisibility(View.GONE);
    }

}
