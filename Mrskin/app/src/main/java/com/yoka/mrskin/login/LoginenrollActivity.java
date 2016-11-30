package com.yoka.mrskin.login;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.model.base.YKResult;
import com.yoka.mrskin.model.data.YKTask;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.YKLoginEnrollCallback;
import com.yoka.mrskin.model.managers.YKLoginEnrollManager;
import com.yoka.mrskin.model.managers.YKObtainCallback;
import com.yoka.mrskin.model.managers.YKObtainManagers;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers;
import com.yoka.mrskin.model.managers.YKSyncTaskManagers.DownloadTaskCallBack;
import com.yoka.mrskin.model.managers.YKValidationUserNameManager;
import com.yoka.mrskin.model.managers.YKValidationUserNameManager.UserNameCallback;
import com.yoka.mrskin.model.managers.task.YKTaskManager;
import com.yoka.mrskin.model.managers.task.YKTaskStore;
import com.yoka.mrskin.util.AppUtil;
import com.yoka.mrskin.util.ClassPathResource;
import com.yoka.mrskin.util.CustomButterfly;
import com.yoka.mrskin.util.LoadingDialog;
import com.yoka.mrskin.util.MD5;
/**
 * 注册页面
 * @author Y H L 
 */
public class LoginenrollActivity extends BaseActivity implements
android.view.View.OnClickListener
{
    private TextView mSuccess, mObtainBut;
    private LinearLayout mBack;
    private TimeCount time;
    private ImageView mLoginNameImage,mLoginPassWordImage;
    public static final String UESRENROLL = "UESRENROLL";
    private EditText mUserName, mPassWord, mPhone, mCaptcha;
    //private CustomProgress mProgressDialog = null;
    private CustomButterfly mCustomButterfly = null;
    
    //密码
    private final int charMaxNumPass = 20;
    private final int charMinNumPass = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_enroll);

        // 构造CountDownTimer对象
        time = new TimeCount(60000, 1000);
        init();
    }

    private void init() {

        mBack = (LinearLayout) findViewById(R.id.enroll_back_layout);
        mBack.setOnClickListener(this);
        mSuccess = (TextView) findViewById(R.id.login_success);
        mSuccess.setOnClickListener(this);
        mObtainBut = (TextView) findViewById(R.id.login_obtain);
        mObtainBut.setOnClickListener(this);

        mUserName = (EditText) findViewById(R.id.login_username);
        mUserName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});  
        mPassWord = (EditText) findViewById(R.id.login_password);
        mPassWord.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); 

        mPhone = (EditText) findViewById(R.id.login_phone);
        mCaptcha = (EditText) findViewById(R.id.login_captcha);

        mLoginNameImage = (ImageView) findViewById(R.id.login_username_image);
        mLoginPassWordImage = (ImageView) findViewById(R.id.login_password_image);

        //        mUserName.addTextChangedListener(new TextWatcher() {
        //            private CharSequence tempUserName;//监听前的文本  
        //
        //            @Override
        //            public void onTextChanged(CharSequence s, int start, int before, int count) {
        //                // TODO Auto-generated method stub
        //                tempUserName = s;  
        //            }
        //
        //            @Override
        //            public void beforeTextChanged(CharSequence s, int start, int count,
        //                    int after) {
        //                // TODO Auto-generated method stub
        //                //Toast.makeText(LoginenrollActivity.this, String.valueOf(charMaxNum - s.length()), 0).show();
        //            }
        //
        //            @Override
        //            public void afterTextChanged(Editable s) {
        //                // TODO Auto-generated method stub
        //                if (tempUserName.length() > charMaxNumUser) {  
        //                    Toast.makeText(getApplicationContext(), "最多输入14个字符", Toast.LENGTH_LONG).show();  
        //                }
        //                if(tempUserName.length() < charMinNumUser){
        //                    Toast.makeText(getApplicationContext(), "最少输入4个字符", Toast.LENGTH_LONG).show(); 
        //                }
        //            }
        //        });

        mUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    //获得焦点,在这里可以对获得焦点进行处理
                }else{
                    //失去焦点,在这里可以对输入的文本内容进行有效的验证
                    String name = mUserName.getText().toString().trim();
                    if(!TextUtils.isEmpty(name)){
                        mLoginNameImage.setVisibility(View.VISIBLE);
                                YKValidationUserNameManager.getInstance().postYKValidationUserName(name, new UserNameCallback() {

                                    @Override
                                    public void callback(YKResult result) {
                                        // TODO Auto-generated method stub
                                        String message = (String) result.getMessage();
                                        if(result.isSucceeded()){
                                            mLoginNameImage.setBackgroundResource(R.drawable.hook);
                                        }else{
                                            Toast.makeText(LoginenrollActivity.this, message, Toast.LENGTH_SHORT).show();
                                            mLoginNameImage.setBackgroundResource(R.drawable.exclamation);
                                        }
                                    }
                                });
                    }else{
                        mLoginNameImage.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginenrollActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //        mPassWord.addTextChangedListener(new TextWatcher() {
        //            private CharSequence tempPassWord;//监听前的文本  
        //
        //            @Override
        //            public void onTextChanged(CharSequence s, int start, int before, int count) {
        //                // TODO Auto-generated method stub
        //                tempPassWord = s;
        //            }
        //
        //            @Override
        //            public void beforeTextChanged(CharSequence s, int start, int count,
        //                    int after) {
        //                // TODO Auto-generated method stub
        //
        //            }
        //
        //            @Override
        //            public void afterTextChanged(Editable s) {
        //                if (tempPassWord.length() > charMaxNumPass) {  
        //                    Toast.makeText(getApplicationContext(), "最多输入20个字符", Toast.LENGTH_LONG).show();  
        //                }
        //                if(tempPassWord.length() < charMinNumPass){
        //                    Toast.makeText(getApplicationContext(), "最少输入6个字符", Toast.LENGTH_LONG).show(); 
        //                }
        //
        //            }
        //        });

        mPassWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    //获得焦点,在这里可以对获得焦点进行处理  
                }else{
                    //失去焦点,在这里可以对输入的文本内容进行有效的验证  
                    if(!TextUtils.isEmpty(mPassWord.getText().toString())){
                        mLoginPassWordImage.setVisibility(View.VISIBLE);
                        int passLength = mPassWord.getText().toString().length();
                            if(passLength <= charMaxNumPass && passLength >= charMinNumPass){
                                mLoginPassWordImage.setBackgroundResource(R.drawable.hook);
                            }else{
                                mLoginPassWordImage.setBackgroundResource(R.drawable.exclamation);
                                Toast.makeText(getApplicationContext(), "最少输入6个字符,最多输入20个字符", Toast.LENGTH_SHORT).show();
                            }
                    }else{
                        mLoginPassWordImage.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginenrollActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    class TimeCount extends CountDownTimer
    {
        public TimeCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mObtainBut.setText(getString(R.string.login_resobtain));
            mObtainBut.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            mObtainBut.setClickable(false);
            mObtainBut.setText(millisUntilFinished / 1000
                    + getString(R.string.login_second));
        }

        public void onAgain() {// 失败的方法
            mObtainBut.setClickable(true);
            mObtainBut.setText(getString(R.string.login_obtain));
            time.cancel();
        }
    }

    private void loginSuccess() {
        try {
            mCustomButterfly = CustomButterfly.show(this);
        } catch (Exception e) {
            mCustomButterfly = null;
        }

        YKLoginEnrollManager.getInstance().postLoginEnroll(
                mUserName.getText().toString(), mPassWord.getText().toString(),
                mPhone.getText().toString(), mCaptcha.getText().toString(),
                new YKLoginEnrollCallback() {
                    @Override
                    public void callback(YKResult result) {
                        AppUtil.dismissDialogSafe(mCustomButterfly );
                        if (AppUtil
                                .isNetworkAvailable(LoginenrollActivity.this)) {
                            if (result.isSucceeded()) {
                                mLoginPassWordImage.setBackgroundResource(R.drawable.hook);
                                mLoginNameImage.setBackgroundResource(R.drawable.hook);
                                YKUser user = YKCurrentUserManager
                                        .getInstance().getUser();
                                sendLoginData(user);
                            } else {
                                
                                mUserName.clearFocus();
                                mPassWord.clearFocus();
                                mPhone.clearFocus();
                                mCaptcha.clearFocus();
                                
                                String message = (String) result.getMessage();
                                Toast.makeText(LoginenrollActivity.this,
                                        message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            
                            mUserName.clearFocus();
                            mPassWord.clearFocus();
                            mPhone.clearFocus();
                            mCaptcha.clearFocus();
                            
                            Toast.makeText(LoginenrollActivity.this,
                                    getString(R.string.intent_no),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendLoginData(YKUser user) {
        final LoadingDialog dialog = new LoadingDialog(this);
        dialog.setCancelable(false);
        AppUtil.showDialogSafe(dialog);
        YKSyncTaskManagers.getInstance().downLoadTask(
                new DownloadTaskCallBack() {

                    @Override
                    public void callback(ArrayList<YKTask> taskList,
                            YKResult result) {
                        AppUtil.dismissDialogSafe(dialog);
                        if (result.isSucceeded()) {
                            if (taskList != null && taskList.size() > 0) {
                                YKTaskStore.getInstnace()
                                .saveTaskList(taskList);
                                YKTaskManager.getInstnace()
                                .notifyTaskDataChanged();
                            }
                            Toast.makeText(LoginenrollActivity.this,
                                    getString(R.string.login_enroll),
                                    Toast.LENGTH_LONG).show();
                            Intent login = new Intent();
                            login.putExtra(UESRENROLL, true);
                            setResult(RESULT_OK, login);
                            LoginenrollActivity.this.finish();
                        } else {
                            YKCurrentUserManager.getInstance().clearLoginUser();
                            Toast.makeText(LoginenrollActivity.this,
                                    getString(R.string.task_synchroniznonono),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.enroll_back_layout:
            finish();
            break;
        case R.id.login_success:
            if (TextUtils.isEmpty(mUserName.getText())) {
                Toast.makeText(LoginenrollActivity.this, R.string.please_enroll_youname,
                        Toast.LENGTH_SHORT).show();
                break;
            }
            if (TextUtils.isEmpty(mPassWord.getText())) {
                Toast.makeText(LoginenrollActivity.this, R.string.please_enroll_youpass,
                        Toast.LENGTH_SHORT).show();
                break;
            }
            if (TextUtils.isEmpty(mPhone.getText())) {
                Toast.makeText(LoginenrollActivity.this, R.string.please_enroll_youphone,
                        Toast.LENGTH_SHORT).show();
                break;
            }
            if (TextUtils.isEmpty(mCaptcha.getText())) {
                Toast.makeText(LoginenrollActivity.this, R.string.please_enroll_youcode,
                        Toast.LENGTH_SHORT).show();
                break;
            }
            int userPassLenght = mPassWord.length();
            if (!TextUtils.isEmpty(mUserName.getText()) && !TextUtils.isEmpty(mPassWord.getText()) && !TextUtils.isEmpty(mPhone.getText()) && !TextUtils.isEmpty(mCaptcha.getText())) {
                mLoginNameImage.setVisibility(View.VISIBLE);
                mLoginPassWordImage.setVisibility(View.VISIBLE);
                
                if(userPassLenght <= charMaxNumPass && userPassLenght >= charMinNumPass){
                    //mLoginPassWordImage.setBackgroundResource(R.drawable.hook);
                }else{
                    Toast.makeText(LoginenrollActivity.this, "请检查密码", Toast.LENGTH_SHORT).show();
                    mLoginPassWordImage.setBackgroundResource(R.drawable.exclamation);
                }
                if(userPassLenght <= charMaxNumPass && userPassLenght >= charMinNumPass){
                    loginSuccess();
                }
            }
            break;
        case R.id.login_obtain:
            String phoneNumber = mPhone.getText().toString().trim();
            String phoneNumbermd5 = "smsYOKAsms"+mPhone.getText().toString().trim();
            String sign = MD5.Md5(phoneNumbermd5);
            if (phoneNumber.length() == 11
                    && ClassPathResource.isMobileNO(phoneNumber)) {
                YKObtainManagers.getInstance().postYKobtain(
                        new YKObtainCallback() {
                            @Override
                            public void callback(YKResult result) {

                                if (result.isSucceeded()) {
                                    //开始计时
                                    time.start();
                                    Toast.makeText(
                                            LoginenrollActivity.this,
                                            getString(R.string.login_havephone),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    time.onAgain();
                                    String message = (String) result.getMessage();
                                    Toast.makeText(LoginenrollActivity.this,
                                            message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, phoneNumber,sign);
                break;
            }
            Toast.makeText(LoginenrollActivity.this,
                    getString(R.string.login_nullphone), Toast.LENGTH_SHORT)
                    .show();
            break;
        default:
            break;
        }
    }
}
