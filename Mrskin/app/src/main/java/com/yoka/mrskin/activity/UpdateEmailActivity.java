package com.yoka.mrskin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager.YKGeneralCallBack;
import com.yoka.mrskin.util.AppUtil;

/**
 * 修改邮箱
 * 
 * @author z l l
 * 
 */
public class UpdateEmailActivity extends BaseActivity implements
OnClickListener
{
    private static final int RESULT_CODE_EMAIL = 0;
    private View mBackBtn;
    private EditText mEditText;
    private TextView mSaveText;
    private String mEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_email);
        getEmailIntent();
        init();
    }
    private String getEmailIntent(){
        Intent email = getIntent();
        String ema = email.getStringExtra("email");
        if(!TextUtils.isEmpty(ema)){
            return ema;
        }
        return null;
    }

    private void init() {
        mBackBtn = findViewById(R.id.update_email_back);
        mSaveText = (TextView) findViewById(R.id.update_email_save);
        mBackBtn.setOnClickListener(this);
        mSaveText.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.update_email_edit);
        mSaveText.setTextColor(0XFFFF5934);
        String email = getEmailIntent();
        if(email != null){
            mEditText.setText(email);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.update_email_back:
            finish();
            break;
        case R.id.update_email_save:
            mEmail = mEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(mEmail)) {
                if(AppUtil.isEmail(mEmail)){
                    saveEmail("email", mEmail);
                }else{
                    Toast.makeText(UpdateEmailActivity.this, "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(UpdateEmailActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private void saveEmail(String key, String value) {
        YKUpdateUserInfoManager.getInstance().requestUpdateUSerInfo(key, value,
                new YKGeneralCallBack() {

            @Override
            public void callback(YKResult result,String imageUrl) {
                if (result.isSucceeded()) {
                    Toast.makeText(UpdateEmailActivity.this, "修改成功",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("email", mEmail);
                    setResult(RESULT_CODE_EMAIL, intent);
                    finish();
                } else {
                    Toast.makeText(UpdateEmailActivity.this,
                            result.getMessage().toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
