package com.example.lenovo.amuse.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.util.HttpTools;

public class BaseActivity extends AppCompatActivity {
    public static HttpTools httpTools;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        httpTools=HttpTools.getInstance();
    }
}
