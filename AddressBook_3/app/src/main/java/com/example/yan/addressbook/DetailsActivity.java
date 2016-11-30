package com.example.yan.addressbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yan.addressbook.mode.Friend;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView bank;
    private TextView name;
    private TextView photoNub;
    private ImageView xiugai;

    private Friend friend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        friend = (Friend) getIntent().getSerializableExtra("friend");
        initView();
    }

    private void initView(){
        bank = (TextView) findViewById(R.id.details_bank);
        bank.setOnClickListener(this);
        name =(TextView) findViewById(R.id.details_name);
        name.setText(friend.getFriendName());
        photoNub = (TextView) findViewById(R.id.details_nub);
        photoNub.setText(friend.getFriendNub());
        photoNub.setOnClickListener(this);
        xiugai =(ImageView) findViewById(R.id.details_xiugai);
        xiugai.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case  R.id.details_bank: finish(); break;
            case R.id.details_nub:
                Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + friend.getFriendNub()));
                if (ActivityCompat.checkSelfPermission(DetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent2); break;
            case R.id.details_xiugai:
                Intent intent1 = new Intent(DetailsActivity.this,AddFriendActivity.class);
                intent1.putExtra("edit",friend);
                startActivity(intent1);
        }
    }
}
