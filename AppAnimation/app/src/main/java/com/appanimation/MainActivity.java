package com.appanimation;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.animation.Animation.AnimationListener;
import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //平移动画
    Button button_translate;
    //偏移量
    Button button_offset;
    //xml动画
    Button button_xml;
    //缩放动画
    Button button_scale;
    Button button_Rotate;
    Button button_alpha;
    Button button_combination;
    Button button_frameAnimation;
    Button button_intent1;
    Button button_intent2;


    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.view);
        textView.setOnClickListener(this);

        //平移动画
        button_translate = (Button) findViewById(R.id.TranslateAnimation);
        button_translate.setOnClickListener(this);

        //偏移量
        button_offset = (Button) findViewById(R.id.offset);
        button_offset.setOnClickListener(this);
        //xml动画
        button_xml = (Button) findViewById(R.id.XML);
        button_xml.setOnClickListener(this);
        //缩放
        button_scale = (Button) findViewById(R.id.ScaleAnimation);
        button_scale.setOnClickListener(this);
        //旋转
        button_Rotate = (Button) findViewById(R.id.RotateAnimation);
        button_Rotate.setOnClickListener(this);
        //透明
        button_alpha = (Button) findViewById(R.id.AlphaAnimation);
        button_alpha.setOnClickListener(this);
        //组合动画
        button_combination = (Button) findViewById(R.id.combination);
        button_combination.setOnClickListener(this);

        //帧动画
        button_frameAnimation = (Button) findViewById(R.id.frameAnimation);
        button_frameAnimation.setOnClickListener(this);

        button_intent1 = (Button) findViewById(R.id.intent_activity1);
        button_intent1.setOnClickListener(this);

        button_intent2 = (Button) findViewById(R.id.intent_activity2);
        button_intent2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.view) {
            Toast.makeText(MainActivity.this, "onClick", Toast.LENGTH_LONG).show();
            int arr = 2/0;
        } else if (id == R.id.TranslateAnimation) {//平移动画
            testTranslateAnimation();
        } else if (id == R.id.offset) {//偏移量
            testOffset();
        } else if (id == R.id.XML) {//xml平移动画
            testXML();
        } else if (id == R.id.ScaleAnimation) {//缩放动画
            testScaleAnimation();
        } else if (id == R.id.RotateAnimation) {//轩转动画
            testRotateAnimation();
        } else if (id == R.id.AlphaAnimation) {//透明动画
            testAlphaAnimation();
        } else if (id == R.id.combination) {//组合动画
            testCombinationAnimation();
        } else if (id == R.id.frameAnimation) {//帧动画
            testFrameAnimation();
        } else if (id == R.id.intent_activity1) {//Activity切换动画
            Intent intent = new Intent(this, ActivityAnimation.class);
            startActivity(intent);
            overridePendingTransition(R.anim.testtranscate, R.anim.testtranscate1);
        } else if (id == R.id.intent_activity2) {//Activity切换动画
            Intent intent = new Intent(this, ActivityAnimator.class);
            startActivity(intent);
//            overridePendingTransition(R.anim.testtranscate, R.anim.stay);
        }
    }

    //帧动画
    private void testFrameAnimation() {
        button_frameAnimation.setBackgroundResource(R.drawable.frame_ainmation);
        button_frameAnimation.setText("");
        AnimationDrawable animation = (AnimationDrawable) button_frameAnimation.getBackground();
        animation.start();
    }

    //组合动画
    private void testCombinationAnimation() {
//        AnimationSet as=new AnimationSet(true);
//
//        Animation translate=new TranslateAnimation(0,200,0,200);
//        as.addAnimation(translate);
//
//        Animation scale=new ScaleAnimation(0,2,0,2);
//        as.addAnimation(scale);
//
//        Animation rotate=new RotateAnimation(0,360f);
//        as.addAnimation(rotate);
//
//        Animation alpha=new AlphaAnimation(1,0.5f);
//        as.addAnimation(alpha);
//        as.setDuration(2000);
//        as.setFillAfter(true);
//        textView.startAnimation(as);
//        as.start();
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.combination);

        textView.startAnimation(animation);

    }

    //透明
    private void testAlphaAnimation() {
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        textView.startAnimation(animation);
    }

    //旋转
    private void testRotateAnimation() {
        Animation animation = new RotateAnimation(0, 360f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        textView.startAnimation(animation);
    }

    //缩放动画
    private void testScaleAnimation() {
//        Animation animation=new ScaleAnimation(0,1.5f,0,1.5f, RELATIVE_TO_SELF,0.5f, RELATIVE_TO_SELF,0.5f);
//        animation.setDuration(2000);
//        animation.setFillAfter(true);
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.testscale);
        textView.startAnimation(animation);

    }

    private void testXML() {
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.testtranscate);
        textView.startAnimation(animation);
    }

    private void testOffset() {
        //创建动画实例
        /**
         * x轴起始位置的差值
         * x轴结束位置的差值
         * y轴起始位置的差值
         * y轴结束位置的差值
         */
        TranslateAnimation animation = new TranslateAnimation(dip2px(MainActivity.this, 100), dip2px(MainActivity.this, 150), 0, 0);
        //设置动画时间
        animation.setDuration(2000);
        //动画结束后停留在结束位置
//       animation.setFillAfter(true);


        //动画监听器
        animation.setAnimationListener(new AnimationListener() {
            //开始
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //结束
            @Override
            public void onAnimationEnd(Animation animation) {
                //清除动画
                textView.clearAnimation();
                //获取布局
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                //设置控件距离
                params.setMargins(dip2px(MainActivity.this, 150), 0, 0, 0);
                textView.setLayoutParams(params);

            }

            //重复
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //启动动画
        textView.startAnimation(animation);
    }

    //平移动画
    private void testTranslateAnimation() {
        //创建动画实例
        /**
         * x轴起始位置的差值
         * x轴结束位置的差值
         * y轴起始位置的差值
         * y轴结束位置的差值
         */
        TranslateAnimation animation = new TranslateAnimation(0, dip2px(MainActivity.this, 100), 0, 0);
        //设置动画时间
        animation.setDuration(2000);
        //动画结束后停留在结束位置
        animation.setFillAfter(true);
        //启动动画
        textView.startAnimation(animation);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
