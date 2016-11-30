package com.example.mylayout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mylayout.layout.AbsoluteActivity;
import com.example.mylayout.layout.FrameActivity;
import com.example.mylayout.layout.LinearActivity;
import com.example.mylayout.layout.RelativeActivity;
import com.example.mylayout.layout.TableActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button linear_btn,relative_btn,frame_btn,absolute_btn,table_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        linear_btn= (Button) findViewById(R.id.linear_btn);
        relative_btn= (Button) findViewById(R.id.relative_btn);
        frame_btn= (Button) findViewById(R.id.frame_btn);
        absolute_btn= (Button) findViewById(R.id.absolute_btn);
        table_btn= (Button) findViewById(R.id.table_btn);
        linear_btn.setOnClickListener(this);
        relative_btn.setOnClickListener(this);
        frame_btn.setOnClickListener(this);
        absolute_btn.setOnClickListener(this);
        table_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_btn:
                Intent intent1=new Intent(this, LinearActivity.class);
                startActivity(intent1);
                break;
            case R.id.relative_btn:
                Intent intent2=new Intent(this, RelativeActivity.class);
                startActivity(intent2);
                break;
            case R.id.frame_btn:
                Intent intent3=new Intent(this, FrameActivity.class);
                startActivity(intent3);
                break;
            case R.id.absolute_btn:
                Intent intent4=new Intent(this, AbsoluteActivity.class);
                startActivity(intent4);
                break;
            case R.id.table_btn:
                Intent intent5=new Intent(this, TableActivity.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
    }
}
