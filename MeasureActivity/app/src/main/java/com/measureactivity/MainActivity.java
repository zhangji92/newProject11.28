package com.measureactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * 测量
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bnt = (Button) findViewById(R.id.bnt_intent);
        bnt.setOnClickListener(this);

        textView= (TextView) findViewById(R.id.textView);
        textView.post(new Runnable() {
            @Override
            public void run() {
                Log.e("textView","post TextView-->width:"+textView.getWidth()+" height:"+textView.getHeight());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewTreeObserver viewTreeObserver=textView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.e("textView","ViewTreeObserver TextView-->width:"+textView.getWidth()+" height:"+textView.getHeight());
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            Log.e("textView","onWindowFocusChanged TextView-->width:"+textView.getWidth()+" height:"+textView.getHeight());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bnt_intent:
                startActivity(new Intent(MainActivity.this, ListViewActivity.class));
                break;
        }

    }
}
