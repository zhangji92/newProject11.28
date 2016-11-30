package com.example.administrator.beijingplayer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.beijingplayer.mode.UserMessage;
import com.example.administrator.beijingplayer.mode.Validate;
import com.example.administrator.beijingplayer.util.HttpTools;
import com.example.administrator.beijingplayer.util.ModeCode;
import com.example.administrator.beijingplayer.util.SharedUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import net.tsz.afinal.FinalBitmap;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class MyActivity extends AppCompatActivity implements View.OnClickListener{
    //退出按钮
    private Button bank;
    //保存按钮
    private TextView keep;
    //头像
    private RoundedImageView head;
    //用户名
    private EditText username;
    //昵称
    private EditText nicheng;
    //性别
    private RadioGroup sex;
    //男
    private RadioButton man;
    //女
    private RadioButton girl;
    //年龄
    private EditText age;
    //地址
    private EditText address;

    private UserMessage userMessage;

    private SharedUtil shareUtil = SharedUtil.getSharedUtil();

    private HttpTools httpTools = HttpTools.getHttpTools();
    private FinalBitmap finalBitmap ;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ModeCode.PHOTO_WHAT:
                    updateHeadImg(msg.obj);
                    break;
                case ModeCode.SET_WHAT:
                    setSuccess(msg.obj);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
         //初始化控件
        initView();
        //设置个人信息
        setUserMessage();

    }

    /**
     * 设置个人信息
     */
    private void setUserMessage(){

        userMessage =(UserMessage) shareUtil.getObject(this, ModeCode.USER,new UserMessage());
        finalBitmap.display(head,ModeCode.HTTP+userMessage.getImgUrl());
        username.setText(userMessage.getMobile());
        nicheng.setText(userMessage.getNickname());
        age.setText(userMessage.getAge());
        if("1".equals(userMessage.getGender())){
          man.setChecked(true);
        }else{
            girl.setChecked(true);
        }
        address.setText(userMessage.getAddress());
    }
    /***
     * 初始化控件
     */
    private void initView(){
        finalBitmap = FinalBitmap.create(this);
        bank = (Button) findViewById(R.id.set_back);
        bank.setOnClickListener(this);
        keep = (TextView)findViewById(R.id.set_keep);
        keep.setOnClickListener(this);
        head = (RoundedImageView) findViewById(R.id.set_head);
        head.setOnClickListener(this);
        username = (EditText) findViewById(R.id.set_username);
        nicheng = (EditText) findViewById(R.id.set_nicheng);
        sex = (RadioGroup) findViewById(R.id.set_sex);
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.set_sex_man){
                    userMessage.setGender("1");
                }else if(checkedId==R.id.set_sex_girl){
                    userMessage.setGender("2");
                }
            }
        });
        man = (RadioButton) findViewById(R.id.set_sex_man);
        girl = (RadioButton) findViewById(R.id.set_sex_girl);
        age = (EditText) findViewById(R.id.set_age);
        address = (EditText) findViewById(R.id.set_address);
    }
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.set_back: //退出登录
                shareUtil.putBoolean(this,ModeCode.IS_LOGIN,false);
                finish(); break;

            case R.id.set_keep://保存设置信息
                keepUser(); break;

            case R.id.set_head://设置图片路径
                setImgurl();
        }

    }

    /***
     * 点击头像的方法
     */
    private void setImgurl(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new String[]{"拍照", "手机相册", "取消"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                   switch (which) {
                       case 0://打开摄像头
                           getImageFromCamera();
                           break;
                       case 1://获取手机相册
                           Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                           intent.setType("image/*");//相片类型
                           startActivityForResult(intent, ModeCode.PHOTO_ALBUM);
                           break;
                       case 2:

                           break;

                   }
            }
        });
        AlertDialog alert =   builder.create();
        Window window = alert.getWindow();
        alert.show();
        window.setGravity(Gravity.BOTTOM);



    }

    /**
     * 保存个人信息
     */
    private void keepUser(){
        getUser();
        httpTools.getSetResult(handler,userMessage);
    }

    /***
     * 获取修改的信息
     */
    private void getUser(){
        userMessage.setNickname(nicheng.getText().toString());
        userMessage.setAge(age.getText().toString());
        userMessage.setAddress(address.getText().toString());
    }

    /**
     * 设置完执行此方法
     */
    private void setSuccess(Object obj){
        if(obj != null &&obj instanceof Validate){
            Validate aa = (Validate) obj;
            if(aa.getMessage().equals("成功")){
                SharedUtil shareUtil = SharedUtil.getSharedUtil();
                shareUtil.putObject(this,ModeCode.USER,userMessage);
                finish();
            }
        }
    }

    /**
     * 进入系统拍照
     */
    protected void getImageFromCamera() {
            String state = Environment.getExternalStorageDirectory().getPath();
            Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(state+"/head.jpg")));
            startActivityForResult(getImageByCamera, ModeCode.START_CAMERA);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri=null;
        File file =null;
        if (requestCode == ModeCode.PHOTO_ALBUM && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            file = new File(uri.getPath());
            Log.e("onActivityResult: ",file.getPath() );
        } else if (requestCode == ModeCode.START_CAMERA  && resultCode == Activity.RESULT_OK) {
            file = new File(Environment.getExternalStorageDirectory().getPath()+"/head.jpg");
        }
        httpTools.getPhoto(handler,file);
        finalBitmap.display(head,file.getPath());
    }

    /**
     * 修改头像路径
     * @param obj
     */
    private void updateHeadImg(Object obj){
        if(obj != null &&obj instanceof Validate){
            Validate aa = (Validate) obj;
            userMessage.setImgUrl(aa.getResultCode());
        }
    }

}
