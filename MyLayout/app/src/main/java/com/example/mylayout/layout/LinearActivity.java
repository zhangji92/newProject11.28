package com.example.mylayout.layout;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mylayout.R;

public class LinearActivity extends AppCompatActivity implements View.OnClickListener {
    private Button top_btn, middle_btn, bottom_btn;
    private LinearLayout divider_linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear);
        initViews();
    }

    private void initViews() {
        top_btn = (Button) findViewById(R.id.top_btn);
        middle_btn = (Button) findViewById(R.id.middle_btn);
        bottom_btn = (Button) findViewById(R.id.bottom_btn);
        divider_linear = (LinearLayout) findViewById(R.id.linear_divi);
        top_btn.setOnClickListener(this);
        middle_btn.setOnClickListener(this);
        bottom_btn.setOnClickListener(this);
//        divider_linear.setDividerDrawable(getResources().getDrawable(R.drawable.my_divider, null));
//        divider_linear.setDividerPadding(10);
    }

    @Override
    public void onClick(View v) {
        //这里有个问题 不能全部设置？
        switch (v.getId()) {
            case R.id.top_btn:
                divider_linear.setShowDividers(LinearLayout.SHOW_DIVIDER_BEGINNING);
                break;
            case R.id.middle_btn:
                divider_linear.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                break;
            case R.id.bottom_btn:
                divider_linear.setShowDividers(LinearLayout.SHOW_DIVIDER_END);
                break;
            default:
                break;
        }
    }
}
