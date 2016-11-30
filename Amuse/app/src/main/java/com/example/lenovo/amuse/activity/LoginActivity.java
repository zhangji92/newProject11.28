package com.example.lenovo.amuse.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.amuse.MyApplication;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.mode.FirstPageMode;
import com.example.lenovo.amuse.mode.ResultCodeBean;
import com.example.lenovo.amuse.mode.SuccessMode;
import com.example.lenovo.amuse.util.BaseUri;
import com.example.lenovo.amuse.util.JsonParserTools;
import com.example.lenovo.amuse.util.MyFinalDB;

import net.tsz.afinal.FinalDb;

/**
 * 登陆
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    //账号
    private EditText editText_user;
    private EditText editText_pad;
    MyApplication myApplication;

    String user;
    String pad;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseUri.LOGIN_SUCCESS:
                    String str = (String) msg.obj;
                    if (str.contains("成功")) {
                        SuccessMode successMode = (SuccessMode) JsonParserTools.parserMode(str, 6);
                        if (successMode.getMessage().contains("成功")) {
                            Toast.makeText(LoginActivity.this, "登陆" + successMode.getMessage(), Toast.LENGTH_LONG).show();
                            myApplication.setUser(user);
                            myApplication.setPad(pad);
                            //把数据保存到数据库
                            ResultCodeBean bean=successMode.getResultCode();
//                          bean.setId("1");
                            MyFinalDB.getInstance(LoginActivity.this).saveFinalDB(bean);
                            //添加数据
                            myApplication.setSuccessMode(successMode);
                            //登陆成功的标志符
                            myApplication.setFlag(true);
                            finish();
                        }
                    } else if (str.contains("错误")) {
                        Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //注册
        TextView textView_register = (TextView) findViewById(R.id.login_register);
        textView_register.setOnClickListener(this);
        //账号
        editText_user = (EditText) findViewById(R.id.login_user);
        //创建MyApplication实例
        myApplication = (MyApplication) getApplication();
        //密码
        editText_pad = (EditText) findViewById(R.id.login_pad);
        //登陆
        Button button_login = (Button) findViewById(R.id.login);
        button_login.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.login:
                //数据源清空
                myApplication.setSuccessMode(null);
                user = editText_user.getText().toString();
                pad = editText_pad.getText().toString();
                if (user.equals("")) {
                    Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_LONG).show();
                } else if (pad.equals("")) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                } else {
                    httpTools.postLogin(mHandler, user, pad, null, 2);
                }
                break;
        }
    }
}
