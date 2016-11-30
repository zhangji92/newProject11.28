package com.allactivity.imkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.allactivity.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by 张继 on 2016/10/28.
 *
 */

public class HuiHuaActivity extends FragmentActivity {
    /**
     * 目标ID
     */
    private String mTargetId;
    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId 
     */
    private String mTargetIds;
    /**
     * 会话类型 
     */
    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hui_hua);

    }
}
