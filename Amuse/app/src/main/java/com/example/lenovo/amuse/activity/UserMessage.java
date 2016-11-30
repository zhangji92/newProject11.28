package com.example.lenovo.amuse.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.amuse.MyApplication;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.mode.ResultCodeBean;
import com.example.lenovo.amuse.mode.SuccessMode;
import com.example.lenovo.amuse.mode.VerificationCode;
import com.example.lenovo.amuse.util.BaseUri;
import com.example.lenovo.amuse.util.JsonParserTools;
import com.example.lenovo.amuse.util.MyFinalDB;
import com.makeramen.roundedimageview.RoundedImageView;

import net.tsz.afinal.FinalBitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 个人信息
 */
public class UserMessage extends BaseActivity implements View.OnClickListener {
    MyApplication myApplication;

    RoundedImageView roundedImageView;
    RadioButton radioButton_boy;
    RadioButton radioButton_girl;
    TextView textView_phone;
    EditText editText_name;
    EditText editText_age;
    EditText editText_address;
    FinalBitmap finalBitmap;
    MyFinalDB myFinalDB;
    ResultCodeBean resultCodeBean;
    //保存
    private Button bnt_save;
    //退出登陆
    private Button bnt_esc;
    //照片文件
    File outFile;
    //照片uri
    Uri imageUri;
    //图片路径
    String img;
    String user;
    String pad;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == BaseUri.MESSAGE_CODE) {
                VerificationCode verificationCode = parserMode(msg.obj);
                if (verificationCode != null) {
                    if (verificationCode.getMessage().contains("成功")) {
                        myApplication.setSuccessMode(null);
                        myFinalDB.deleteFinalDB(resultCodeBean);
                        user=myApplication.getUser();
                        pad=myApplication.getPad();
                        httpTools.postLogin(mHandler, user, pad, null, 2);
                        Toast.makeText(UserMessage.this, "信息保存成功", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(UserMessage.this, "信息保存失败", Toast.LENGTH_LONG).show();
                }
            } else if (msg.what == BaseUri.PIC_CODE) {
                VerificationCode verificationCode = parserMode(msg.obj);
                if (verificationCode != null) {
                    if (verificationCode.getMessage().contains("成功")) {
                        resultCodeBean.setImgUrl(img);
                        img=verificationCode.getResultCode();
                        finalBitmap.display(roundedImageView, BaseUri.BASE + img);
                        Toast.makeText(UserMessage.this, "照片上次成功", Toast.LENGTH_LONG).show();
                    }
                }
            }else if (msg.what==BaseUri.LOGIN_SUCCESS){
                String str = (String) msg.obj;
                if (str.contains("成功")) {
                    SuccessMode successMode = (SuccessMode) JsonParserTools.parserMode(str, 6);
                    if (successMode.getMessage().contains("成功")) {
                        //把数据保存到数据库
                        ResultCodeBean bean=successMode.getResultCode();
                        MyFinalDB.getInstance(UserMessage.this).saveFinalDB(bean);
                        //添加数据
                        myApplication.setSuccessMode(successMode);
                    }
                }
            }
        }
    };

    private VerificationCode parserMode(Object obj) {
        VerificationCode verificationCode = null;
        if (obj != null && obj instanceof VerificationCode) {
            verificationCode = (VerificationCode) obj;
        }
        return verificationCode;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        //保存
        bnt_save = (Button) findViewById(R.id.save);
        bnt_save.setOnClickListener(this);
        //退出
        bnt_esc = (Button) findViewById(R.id.esc);
        bnt_esc.setOnClickListener(this);
        //头像
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.userImg_liner);
        linearLayout.setOnClickListener(this);


        myApplication = (MyApplication) getApplication();
        //头像
        roundedImageView = (RoundedImageView) findViewById(R.id.userImg);
        //用户
        textView_phone = (TextView) findViewById(R.id.userPhone);
        //昵称
        editText_name = (EditText) findViewById(R.id.userName);
        radioButton_boy = (RadioButton) findViewById(R.id.boy);
        radioButton_girl = (RadioButton) findViewById(R.id.girl);

        //年龄
        editText_age = (EditText) findViewById(R.id.userAge);
        //地址
        editText_address = (EditText) findViewById(R.id.userAddress);
        //图片
        finalBitmap = FinalBitmap.create(this);
        //初始化数据
        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        if (myApplication.getSuccessMode() != null) {
//            resultCodeBean = myApplication.getSuccessMode().getResultCode();
            myFinalDB = MyFinalDB.getInstance(UserMessage.this);
            resultCodeBean = myFinalDB.selectFinalDB();
            img=resultCodeBean.getImgUrl();
            finalBitmap.display(roundedImageView, BaseUri.BASE + resultCodeBean.getImgUrl());
            textView_phone.setText(resultCodeBean.getMobile());
            editText_name.setText(resultCodeBean.getNickname());
            editText_age.setText(resultCodeBean.getAge());
            editText_address.setText(resultCodeBean.getAddress());
            if (resultCodeBean.getGender().contains("1")) {
                radioButton_boy.setChecked(true);
            } else {
                radioButton_girl.setChecked(true);
            }
        }
    }

    private void getData() {
        String name = editText_name.getText().toString();
        String token = resultCodeBean.getToken();
        String age = editText_age.getText().toString();
        String address = editText_address.getText().toString();
        String sex = "1";
        if (radioButton_boy.isChecked() == true) {
            sex = "1";
        } else if (radioButton_girl.isChecked() == true) {
            sex = "2";
        }
        httpTools.postMessage(mHandler, token, name, sex, age, address, img);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.save) {
            getData();
            finish();
        } else if (id == R.id.esc) {
            myApplication.setSuccessMode(null);
            myFinalDB.deleteFinalDB(resultCodeBean);
            myApplication.setFlag(true);
            finish();
        } else if (id == R.id.userImg_liner) {
            dialog();
        }
    }
    private void dialog() {
        final String[] items = {"拍照", "手机相册", "取消"};
        //创建对话框构造器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(UserMessage.this, items[which], Toast.LENGTH_LONG).show();
                if (which == 0) {
                    getCamera();
                }

            }
        });
        builder.create().show();
    }



    //获取相机
    public void getCamera() {
        //创建File对象，用于存储拍照后的照片
        outFile = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
        //判断是否有这个文件
        if (outFile.exists()) {
            //删除这个文件
            outFile.delete();
        } else {
            try {
                //创建这个文件
                outFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageUri = Uri.fromFile(outFile);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //启动相机程序
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                trimImage(imageUri);
                break;
            case 200:
                try {
                    outFile = new File(new URI(imageUri.toString()));
                    Log.i("getFirstDate", "getFirstDate" + outFile);
//                    Bitmap bitmap = callImage(imageUri);
//                    displayPhoto(bitmap);
                    httpTools.postWine(mHandler, null, null, outFile, 2);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
//            case 300:
//                // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
//                if (data != null) {
//                    //此处注释掉的部分是针对android 4.4路径修改的一个测试
//                    //有兴趣的读者可以自己调试看看
//                    imageUri = data.getData();
//                    trimImage(imageUri);
//                }
//                break;
        }
    }

    /**
     * 裁剪图片
     */
    private void trimImage(Uri uri) {
        //跳到裁剪页面
        Intent intent = new Intent("com.android.camera.action.CROP");
        //数据和类型
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        //是否缩放
        intent.putExtra("scale", true);
        //剪切图片-->把剪切出来图片的路径放入imageUri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // 启动裁剪程序
        startActivityForResult(intent, 200);
    }
}
