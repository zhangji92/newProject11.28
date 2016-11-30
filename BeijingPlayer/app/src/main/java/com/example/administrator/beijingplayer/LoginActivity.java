package com.example.administrator.beijingplayer;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.beijingplayer.application.App;
import com.example.administrator.beijingplayer.mode.User;
import com.example.administrator.beijingplayer.mode.UserMessage;
import com.example.administrator.beijingplayer.util.HttpTools;
import com.example.administrator.beijingplayer.util.ModeCode;
import com.example.administrator.beijingplayer.util.SharedUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edit1;
    private EditText edit2;

    private ImageView remenber;
    private ImageView bank;
    private TextView text1;

    private Button btn1;
    private Button btn2;

    private UserMessage userMessage;

    HttpTools httpTools = HttpTools.getHttpTools();

    private App app = new App();

    private SharedUtil shareUtil = SharedUtil.getSharedUtil();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case  ModeCode.LOGIN_WHAT:
                    getLoginSuccess(msg.obj);break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    /***
     * 初始化控件
     */
    private void initView(){
        edit1 =(EditText)findViewById(R.id.login_edit1);
        edit2 =(EditText)findViewById(R.id.login_edit2);
        remenber = (ImageView)findViewById(R.id.login_img6);
        remenber.setOnClickListener(this);
        bank = (ImageView)findViewById(R.id.login_img1);
        bank.setOnClickListener(this);
        text1 = (TextView)findViewById(R.id.login_text2);
        text1.setOnClickListener(this);

        btn1 = (Button) findViewById(R.id.login_btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.login_btn2);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.login_img6:
                remenber.setImageResource(R.drawable.login_xieyi2); break;
            case R.id.login_text2:
                Intent intent1 = new Intent(this,RegisterActivity.class);
                startActivity(intent1); break;
            case R.id.login_btn1:onClickLogin(); break;
        }
    }

    /**
     * 点击登录执行
     */
    private void onClickLogin(){
        String username = edit1.getText().toString();
        String password = edit2.getText().toString();
        httpTools.getLogin(handler,username,password);
    }

    /**
     * 登录成功
     * @param obj
     */
    private void getLoginSuccess(Object obj){
        if(obj!=null && obj instanceof User){
            User user = (User) obj;
            if(user.getMessage().contains("成功")){
                userMessage = user.getResultCode();
                shareUtil.putObject(this,ModeCode.USER,userMessage);
                shareUtil.putBoolean(this,ModeCode.IS_LOGIN,true);
                finish();
            }
        }
    }
}
