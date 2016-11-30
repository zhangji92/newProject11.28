package com.yoka.mrskin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager;
import com.yoka.mrskin.model.managers.YKUpdateUserInfoManager.YKGeneralCallBack;
import com.yoka.mrskin.util.ClassPathResource;
import com.yoka.mrskin.util.MD5;

/**
 * 修改手机号
 * 
 * @author z l l
 * 
 */
public class UpdatePhoneNumberActivity extends BaseActivity implements
        OnClickListener
{
    private static final int RESULT_CODE_PHONE = 0;
    private View mBackBtn;
    private EditText mPhoneEdit, mCheckEdit;
    private TextView mSaveText;
    private String mPhone = "";
    private String mCheck = "";
    private Button mGetBtn;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phonenumber);

        init();
    }

    private void init() {
        mBackBtn = findViewById(R.id.update_phonenumber_back);
        mSaveText = (TextView) findViewById(R.id.update_phone_save);
        mGetBtn = (Button) findViewById(R.id.update_phone_check_get_btn);
        mBackBtn.setOnClickListener(this);
        mGetBtn.setOnClickListener(this);
        mSaveText.setOnClickListener(this);
        mSaveText.setEnabled(false);
        mGetBtn.setEnabled(false);
        time = new TimeCount(60000, 1000);
        mPhoneEdit = (EditText) findViewById(R.id.update_phone_number_edit);
        mPhoneEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPhone = s.toString().trim();
                if (!TextUtils.isEmpty(mPhone)) {
                    mGetBtn.setEnabled(true);
                } else {
                    mGetBtn.setEnabled(false);
                }
            }
        });

        mCheckEdit = (EditText) findViewById(R.id.update_phone_check_edit);
        mCheckEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCheck = s.toString().trim();
                if (!TextUtils.isEmpty(mCheck)) {
                    mSaveText.setEnabled(true);
                    mSaveText.setTextColor(0XFFFF5934);
                } else {
                    mSaveText.setEnabled(false);
                    mSaveText.setTextColor(0XFFDEDEDE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.update_phonenumber_back:
            finish();
            break;
        case R.id.update_phone_check_get_btn:
            if (ClassPathResource.isMobileNO(mPhone)) {
                String phoneNumbermd5 = "smsYOKAsms"+mPhone;
                String sign = MD5.Md5(phoneNumbermd5);
                YKUpdateUserInfoManager.getInstance().requestUpdatePhone("telephone", mPhone, "sign",sign, new YKGeneralCallBack()
                {
                    
                    @Override
                    public void callback(YKResult result, String imageUrl)
                    {
                        if(result.isSucceeded()){
                            time.start();
                            Toast.makeText(UpdatePhoneNumberActivity.this,
                                    R.string.login_havephone,
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(UpdatePhoneNumberActivity.this,
                                    result.getMessage().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        
                        
                    }
                });
            } else {
                Toast.makeText(UpdatePhoneNumberActivity.this, "请输入正确的手机号码",
                        Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.update_phone_save:
            if (mCheck.length() < 5) {
                Toast.makeText(UpdatePhoneNumberActivity.this, "请输入正确的验证码",
                        Toast.LENGTH_SHORT).show();
            } else {
                YKUpdateUserInfoManager.getInstance().requestSureUpdatePhone("telephone", mPhone, "code", mCheck, new YKGeneralCallBack()
                {
                    
                    @Override
                    public void callback(YKResult result, String imageUrl)
                    {
                        if(result.isSucceeded()){
                            Intent intent = new Intent();
                            intent.putExtra("phone", mPhone);
                            setResult(RESULT_CODE_PHONE, intent);
                            Toast.makeText(UpdatePhoneNumberActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(UpdatePhoneNumberActivity.this,
                                    result.getMessage().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            break;
        }
    }

    @Override
    protected void onDestroy() {
        if (time != null) {
            time.cancel();
        }
        super.onDestroy();
    }

    class TimeCount extends CountDownTimer
    {
        public TimeCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mGetBtn.setText("获取");
            mGetBtn.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            mGetBtn.setEnabled(false);
            mGetBtn.setText(millisUntilFinished / 1000 + "S");
        }
    }
}
