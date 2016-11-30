package com.allactivity.imkit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.allactivity.R;

import io.rong.imkit.RongIM;

/**
 * Created by 张继 on 2016/10/28.
 *
 */

public class HuiHuaListActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hui_hua_list);
        RongIM.getInstance().startPrivateChat(this, "100", "你好");
    }
}
