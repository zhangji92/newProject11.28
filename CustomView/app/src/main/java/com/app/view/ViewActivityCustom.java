package com.app.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by lenovo on 2016/10/14.
 *
 */

public class ViewActivityCustom extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);

        Button button = (Button) findViewById(R.id.bnt);
        button.setOnClickListener(this);
        button.setOnTouchListener(this);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setOnClickListener(this);
        textView.setOnTouchListener(this);

        textView.setClickable(false);

        RelativeLayout myRelativeLayout = (RelativeLayout) findViewById(R.id.myRelative);
        myRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("app", "MyRelativeLayout ");
                return false;
            }
        });

    }

    class MyClassGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bnt) {
            Log.i("app", "button onClick");
        } else if (id == R.id.textView) {
            Log.i("app", "--------textView onClick");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("app", "-----------onTouch ACTION_DOWN");
                return false;
            case MotionEvent.ACTION_MOVE:
                Log.i("app", "-----------onTouch ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i("app", "-----------onTouch ACTION_UP");
                break;
        }
        return true;
    }
}
