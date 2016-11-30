package com.app.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener, View.OnClickListener, View.OnTouchListener {
    //追踪速度
    private VelocityTracker velocityTracker;
    //检测手势
    private GestureDetector gestureDetector;
    private MyImageView img;

    private int mCount = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mCount++;
                    if (mCount < 20) {
//                        float s = (mCount / (float) 50);
//                        int scrollX = (int) (s * 1000);
                        int distance = (int) ((mCount / (float)20) * 50);
                        img.scrollTo(-distance, -distance);
                        handler.sendEmptyMessageDelayed(1, 50);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //手势检测
        getInstanceGestureDetector();

        Button bnt_click = (Button) findViewById(R.id.bnt_click);
        bnt_click.setOnClickListener(this);
        //自定义ImageView
        Button bnt_click2 = (Button) findViewById(R.id.bnt_click2);
        bnt_click2.setOnClickListener(this);
        //弹性滑动
        Button bnt_click3 = (Button) findViewById(R.id.bnt_click3);
        bnt_click3.setOnClickListener(this);
        //跳转页面
        Button bnt_intent = (Button) findViewById(R.id.bnt_intent);
        bnt_intent.setOnClickListener(this);

        img = (MyImageView) findViewById(R.id.img);
        img.setOnTouchListener(this);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getVelocityTracker(event);

        //获取手机触屏的时间
        int i = event.getAction();
        //获取手机屏幕移动的常量值
//        int a=ViewConfiguration.get(this).getScaledTouchSlop();
        switch (i) {
            case MotionEvent.ACTION_DOWN:
//                Log.i("app","ACTION_DOWN");
//                Log.i("app","ACTION_DOWN"+a);
                break;
            case MotionEvent.ACTION_MOVE:

//                Log.i("app","ACTION_MOVE");
//                Log.i("app","ACTION_DOWN"+a);
                break;
            case MotionEvent.ACTION_UP:
//                Log.i("app","ACTION_UP");
//                Log.i("app","ACTION_DOWN"+a);
                break;
        }
        //检测手势
        boolean flag = gestureDetector.onTouchEvent(event);
        return flag;
    }

    private void getVelocityTracker(MotionEvent event) {
        //获取速度追踪实例
        velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);
        //速度时间
        velocityTracker.computeCurrentVelocity(1000);
        //获取x,y轴速度
        float xSpeed = velocityTracker.getXVelocity();
        float ySpeed = velocityTracker.getYVelocity();
        Toast.makeText(this, "xSpeedX:" + xSpeed + " ySpeed:" + ySpeed, Toast.LENGTH_LONG).show();
//        Log.i("app", "xSpeedX:" + xSpeed + " ySpeed:" + ySpeed);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("app", "stop");
        if (velocityTracker != null) {
            velocityTracker.clear();
            velocityTracker.recycle();
        }
    }

    public void getInstanceGestureDetector() {
        gestureDetector = new GestureDetector(this, this);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i("app", "GestureDetector onDown");
        Toast.makeText(this, "GestureDetector onDown", Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.i("app", "GestureDetector onShowPress");
        Toast.makeText(this, "GestureDetector onShowPress", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i("app", "GestureDetector onSingleTapUp");
        Toast.makeText(this, "GestureDetector onSingleTapUp", Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i("app", "GestureDetector onScroll");
        Toast.makeText(this, "GestureDetector onScroll", Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i("app", "GestureDetector onLongPress");
        Toast.makeText(this, "GestureDetector onLongPress", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i("app", "GestureDetector onFling");
        Toast.makeText(this, "GestureDetector onFling", Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bnt_click) {
            testScrollTo();
        } else if (id == R.id.bnt_click2) {//自定义ImageVIew
            img.smoothScrollTo(50, 50);
        } else if (id == R.id.bnt_click3) {//弹性动画
            handler.sendEmptyMessageDelayed(1, 50);
        }else if (id == R.id.bnt_intent) {//跳转页面

            Intent intent=new Intent(this,ViewActivityCustom.class);
            startActivity(intent);
        }
    }

    /**
     * 内容的移动
     */
    private void testScrollTo() {
        //移动的内容
        img.scrollTo(0 - 50, 0 - 50);
    }

    int mLastX;
    int mLastY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(this, "click", Toast.LENGTH_LONG).show();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                int translationX = (int) (ViewHelper.getTranslationX(v) + deltaX);
                int translationY = (int) (ViewHelper.getTranslationY(v) + deltaY);
                ViewHelper.setTranslationX(v, translationX);
                ViewHelper.setTranslationY(v, translationY);
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        mLastX = x;
        mLastY = y;
        return true;
    }
}
