package com.example.yan.addressbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yan.addressbook.mode.Friend;
import com.example.yan.addressbook.util.ContentUtil;

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener{

    private Button quxiao;
    private Button queding;
    private EditText name;
    private EditText gongsi;
    private EditText photo;
    private EditText email;

    private Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        quxiao =(Button) findViewById(R.id.add_quxiao);
        quxiao.setOnClickListener(this);
        queding =(Button) findViewById(R.id.add_sure);
        queding.setOnClickListener(this);
        name =(EditText) findViewById(R.id.add_name);
        gongsi = (EditText) findViewById(R.id.add_gongsi);
        photo = (EditText) findViewById(R.id.add_photo);
        email = (EditText) findViewById(R.id.add_email);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add_quxiao){
            finish();
        }else{
            addToLocal();
        }
    }

    private void addToLocal(){
        String friendName = name.getText().toString();
        String friendPhoto = photo.getText().toString();
        String friendEmail = email.getText().toString();

        if(friendName!=null && friendPhoto!=null){
            if(friend==null){
                friend = new Friend(friendName,friendPhoto);
                friend.setFriendEmail(friendEmail);
                ContentUtil.getContentUtil().addFriendToLocal(this,friend);

            }else {
                Log.e( "修改页","aaaaaaaaaaaaaaaaaaaaaaaaaaaa" );
                friend.setFriendName(friendName);
                friend.setFriendNub(friendPhoto);
                friend.setFriendEmail(friendEmail);
                Log.e( "修改页","aaaaaaaaaaaaaaaaaaaaaaaaaaaa"+ friend.getFriendNub() );
                ContentUtil.getContentUtil().updateFriend(this,friend);
            }
            finish();
        }else{
            Toast.makeText(this,"请输入名字和电话",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        Intent intent = getIntent();
        friend =(Friend) intent.getSerializableExtra("edit");
        if(friend!=null){
            name.setText(friend.getFriendName());
            photo.setText(friend.getFriendNub());
        }
        super.onStart();
    }
}
