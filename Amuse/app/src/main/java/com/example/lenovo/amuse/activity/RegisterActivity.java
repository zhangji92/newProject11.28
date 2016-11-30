package com.example.lenovo.amuse.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.mode.RegisterSuccess;
import com.example.lenovo.amuse.mode.VerificationCode;
import com.example.lenovo.amuse.util.BaseUri;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    //手机号
    EditText editText_phone;
    EditText editText_inputCode;
    EditText editText_inputPad;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseUri.PHONE:
                    VerificationCode verificationCode = (VerificationCode) msg.obj;
                    Toast.makeText(RegisterActivity.this, verificationCode.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                case BaseUri.REGISTER_SUCCESS:
                    RegisterSuccess registerSuccess = (RegisterSuccess) msg.obj;
                    Toast.makeText(RegisterActivity.this, "注册"+registerSuccess.getMessage(), Toast.LENGTH_LONG).show();
                    if (registerSuccess.getMessage().contains("成功")) {
                        finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //验证码
        Button button_code = (Button) findViewById(R.id.register_code);
        button_code.setOnClickListener(this);
        //手机号
        editText_phone = (EditText) findViewById(R.id.register_phone);
        //输入验证码
        editText_inputCode = (EditText) findViewById(R.id.register_inputCode);
        //密码
        editText_inputPad = (EditText) findViewById(R.id.register_inputPad);
        //立即注册
        Button button_register = (Button) findViewById(R.id.register);
        button_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_code:
                //获取手机号
                String phone = editText_phone.getText().toString();
                Log.i("getFirstDate", "onSuccess:" + phone);
                httpTools.getDate(mHandler, null, null, phone, null, null, null, null, 3);
                break;
            case R.id.register:
                //获取手机号
                phone = editText_phone.getText().toString();
                String inputCode = editText_inputCode.getText().toString();
                String inputPad = editText_inputPad.getText().toString();
                if (phone.equals("")) {
                    Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_LONG).show();
                } else if (inputCode.equals("")) {
                    Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_LONG).show();
                } else if (inputPad.equals("")) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                } else {
                    httpTools.postLogin(mHandler, phone, inputPad, inputCode, 1);
                }
                break;

        }
    }
}
