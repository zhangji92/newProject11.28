package com.example.administrator.beijingplayer;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.example.administrator.beijingplayer.adpter.KuaipaiListAdapter;
import com.example.administrator.beijingplayer.mode.KuaipaiMessage;
import com.example.administrator.beijingplayer.mode.Validate;
import com.example.administrator.beijingplayer.util.HttpTools;
import com.example.administrator.beijingplayer.util.ModeCode;
import com.example.administrator.beijingplayer.util.ServiceMessage;
import com.example.administrator.beijingplayer.util.SharedUtil;

import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

public class KuaipaiActivity extends AppCompatActivity {

    private GridView gridView;

    private KuaipaiListAdapter kuaipaiAdapter;
    private List<KuaipaiMessage.ResultCodeBean.QuickphotoBean>  list = new ArrayList<>();

    private HttpTools httpTools  = HttpTools.getHttpTools();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what ){
                case ModeCode.KUAIPAI_WHAT:getKuaipaiMessage(msg.obj);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuaipai);

        initView();
    }

    /**
     * 初始化控件
     * 获取服务器数据
     */
    private void initView(){
        gridView =(GridView) findViewById(R.id.kuaipai_listview);
        kuaipaiAdapter = new KuaipaiListAdapter(this,list);
        gridView.setAdapter(kuaipaiAdapter);

        //获取服务器数据
        ServiceMessage<KuaipaiMessage> service = new ServiceMessage<KuaipaiMessage>(ModeCode.KUAIPAI,ModeCode.KUAIPAI_WHAT,new KuaipaiMessage());
        httpTools.getServiceMessage(handler, service);
    }

    /***
     * 获取从服务器获取到的数据
     * @param obj
     */
    private void getKuaipaiMessage(Object obj){
        if(obj != null&&obj instanceof KuaipaiMessage ){
            KuaipaiMessage aa = (KuaipaiMessage) obj;
            List<KuaipaiMessage.ResultCodeBean.QuickphotoBean> arr = aa.getResultCode().getQuickphoto();
            list.addAll(arr);
            kuaipaiAdapter.setList(list);
            kuaipaiAdapter.notifyDataSetChanged();
        }
    }
}
