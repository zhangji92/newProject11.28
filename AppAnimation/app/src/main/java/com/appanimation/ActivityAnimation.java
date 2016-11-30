package com.appanimation;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityAnimation extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    Button bnt_back;
    Button bnt_back_xml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //返回
        Button bnt_return = (Button) findViewById(R.id.Return);
        bnt_return.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.img);

        //偏移
        Button bnt_deviation = (Button) findViewById(R.id.deviation);
        bnt_deviation.setOnClickListener(this);
        //偏移XML
        Button bnt_deviation_xml = (Button) findViewById(R.id.deviation_xml);
        bnt_deviation_xml.setOnClickListener(this);

        //背景
        bnt_back = (Button) findViewById(R.id.back);
        bnt_back.setOnClickListener(this);
        //背景XML
        bnt_back_xml = (Button) findViewById(R.id.back_xml);
        bnt_back_xml.setOnClickListener(this);
        //集合
        Button bnt_aggregate = (Button) findViewById(R.id.aggregate);
        bnt_aggregate.setOnClickListener(this);
        //集合xml
        Button bnt_aggregateXMl = (Button) findViewById(R.id.aggregate_xml);
        bnt_aggregateXMl.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.Return) {//返回
            startActivity(new Intent(ActivityAnimation.this, MainActivity.class));
        } else if (id == R.id.deviation) {//移动
            float curTranslationX = imageView.getTranslationX();
            ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", curTranslationX, 200f, curTranslationX);
            animator.setDuration(1000).start();
        } else if (id == R.id.deviation_xml) {//移动
            Toast.makeText(ActivityAnimation.this, "00000", Toast.LENGTH_LONG).show();
            AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(ActivityAnimation.this, R.animator.nature_translation);
            animatorSet.setTarget(imageView);
            animatorSet.setStartDelay(2000);
            animatorSet.start();
        } else if (id == R.id.back) {
            ObjectAnimator animator = ObjectAnimator.ofInt(imageView, "backgroundColor", 0xFFFF8080, 0XFF8080FF);
            animator.setDuration(2000);
            //无线循环
            animator.setRepeatCount(-1);
            animator.start();
        } else if (id == R.id.back_xml) {
            AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(ActivityAnimation.this, R.animator.nature_back);
            animatorSet.setTarget(imageView);
            animatorSet.setStartDelay(2000);
            animatorSet.start();
        } else if (id == R.id.aggregate) {
            //y轴移动
            ObjectAnimator moveIn = ObjectAnimator.ofFloat(imageView, "translationY", 0, 200f, 0f);
            //旋转
            ObjectAnimator rotation = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
            //透明
            ObjectAnimator fadeInout = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f, 1f);
            //集合
            AnimatorSet animatorSet = new AnimatorSet();
            //移动、旋转、透明
            animatorSet.play(rotation).with(fadeInout).after(moveIn);
            //间隔时间
            animatorSet.setDuration(2000);
            //开始
            animatorSet.start();
        } else if (id == R.id.aggregate_xml) {
            AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(ActivityAnimation.this, R.animator.nature_aggregate);
            animatorSet.setTarget(imageView);
            animatorSet.setStartDelay(2000);
            animatorSet.start();
        }
    }
}
