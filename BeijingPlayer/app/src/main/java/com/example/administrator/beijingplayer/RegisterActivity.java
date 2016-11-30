package com.example.administrator.beijingplayer;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.beijingplayer.mode.Validate;
import com.example.administrator.beijingplayer.util.HttpTools;
import com.example.administrator.beijingplayer.util.ModeCode;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edit1;
    private EditText edit2;
    private EditText edit3;
    private Button btn1;
    private Button register;

    private ImageView xieyi ;
    private ImageView bank;
    private TextView textView;
    private TextView login;

    private String validateMessage;

    private String phoneNub;
    private String yanzhengCode;
    private String password;

    HttpTools httpTools = HttpTools.getHttpTools();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ModeCode.REGISTE_VALIDATE://获取验证码
                    getValidateMessage(msg.obj);break;
                case ModeCode.REGISTER_WHAT:
                    getRegisterSucess(msg.obj);break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

    }

    /**
     * 初始化控件
     */
    private void initView(){
        edit1 = (EditText) findViewById(R.id.register_edit1);
        edit2 = (EditText) findViewById(R.id.register_edit2);
        edit3 = (EditText) findViewById(R.id.register_edit3);
        btn1 = (Button) findViewById(R.id.register_btn1);
        btn1.setOnClickListener(this);
        register = (Button) findViewById(R.id.register_btn2);
        register.setOnClickListener(this);

        xieyi = (ImageView) findViewById(R.id.register_img6);
        xieyi.setOnClickListener(this);
        bank = (ImageView) findViewById(R.id.register_img1);
        bank.setOnClickListener(this);
        textView =(TextView) findViewById(R.id.register_text1);
        textView.setOnClickListener(this);
        login  = (TextView) findViewById(R.id.register_text2);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.register_btn1://获取验证码
                phoneNub = edit1.getText().toString();
                if(phoneNub!=null&&phoneNub.length()==11){
                    httpTools.getValidateMessage(handler,phoneNub);
                }else{
                    Toast.makeText(this,"请输入您的手机号,或者验证您的手机号是否正确",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_btn2://注册
               registerUser(); break;
            case R.id.register_img6:
                xieyi.setImageResource(R.drawable.login_xieyi2);break;
            case R.id.register_text1://跳转协议页面
                Intent intent1 = new Intent(); startActivity(intent1); break;
            case R.id.register_text2://跳转登录页面
                Intent intent2 = new Intent(this,LoginActivity.class); startActivity(intent2); break;
            case R.id.register_img1: finish();break;

        }
    }

    /**
     * 获取验证信息
     * @param obj
     */
    private void getValidateMessage(Object obj){
        if(obj!=null&&obj instanceof Validate){
            Validate validate = (Validate) obj;
            validateMessage = validate.getResultCode();
        }

    }



    /***
     * 点击注册方法
     */
    private void registerUser(){
        yanzhengCode = edit2.getText().toString();
        password = edit3.getText().toString();
        if(yanzhengCode.equals(validateMessage)){
          httpTools.getRegister(handler,phoneNub,password,yanzhengCode);
        }
    }

    /**
     * 注册成功
     */
    private void getRegisterSucess(Object obj){
        if(obj!=null&&obj instanceof Validate){
             Validate validate = (Validate) obj;
            if(validate.getMessage().contains("成功")){
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,validate.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
