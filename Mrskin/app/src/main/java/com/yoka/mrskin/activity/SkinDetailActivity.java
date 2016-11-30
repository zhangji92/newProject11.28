package com.yoka.mrskin.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.main.MainActivity;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.util.ProgressWebView;
import com.yoka.mrskin.util.ProgressWebView.YKURIHandler;
import com.yoka.mrskin.util.YKDeviceInfo;

/**
 * 肌肤测试结果详情页
 * 
 * @author z l l
 * 
 */
public class SkinDetailActivity extends BaseActivity implements OnClickListener
{

    private View mDoneLayout;
    private ProgressWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skin_detail_activity);

        initView();
    }

    private void initView() {
        String url = getIntent().getStringExtra("report_url");
        mDoneLayout = findViewById(R.id.skin_detail_done_layout);
        mDoneLayout.setOnClickListener(this);
        mWebView = (ProgressWebView) findViewById(R.id.skin_detail_webview);
        mWebView.setIsCache(false);
        mWebView.loadUrl(this, url + YKDeviceInfo.getClientID());
        mWebView.setURIHandler(new YKURIHandler() {

            @Override
            public boolean handleURI(String url) {
                System.out.println("skin detail activity url = " + url);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.skin_detail_done_layout:
            doNext();
            break;
        }
    }

    private void doNext() {
        if (YKCurrentUserManager.getInstance().isLogin()) {
            startMainActivity(false);
        } else {
            showDialog();
        }
    }

    private void showDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle(R.string.dialog_title)
                .setMessage(R.string.skin_detail_dialog_title)
                .setPositiveButton(R.string.skin_detail_dialog_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                                startMainActivity(true);
                            }
                        })
                .setNegativeButton(R.string.skin_detail_dialog_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                                startMainActivity(false);
                            }
                        });
        alert.create().show();
    }

    private void startMainActivity(boolean isLogin) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("main_login_state", isLogin);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }
}
