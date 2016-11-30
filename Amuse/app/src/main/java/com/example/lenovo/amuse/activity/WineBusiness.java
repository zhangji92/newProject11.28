package com.example.lenovo.amuse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.amuse.MyApplication;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.adapter.WineAgentAdapter;
import com.example.lenovo.amuse.mode.AgentMode;
import com.example.lenovo.amuse.util.BaseUri;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/18.
 * 酒水商家
 */

public class WineBusiness extends BaseActivity implements AdapterView.OnItemClickListener {
    private ImageView imageView;
    private TextView textView_title;
    private TextView textView_brand;
    private TextView textView_varieties;
    private TextView textView_agent;
    private TextView textView_contact;
    private TextView textView_address;
    private AgentMode agentMode;

    List<AgentMode.ResultCodeBean.AgentshopBean> list = new ArrayList<>();
    WineAgentAdapter wineAgentAdapter;

    GridView gridView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BaseUri.WINE_AGENT_CODE:
                    agentMode = parseMode(msg.obj);
                    //显示图片
                    FinalBitmap finalBitmap = FinalBitmap.create(WineBusiness.this);
                    String img = agentMode.getResultCode().getPic();
                    finalBitmap.display(imageView, BaseUri.BASE + img);

                    textView_title.setText(agentMode.getResultCode().getAgentname());
                    textView_brand.setText("品牌：" + agentMode.getResultCode().getBrand());
                    textView_varieties.setText("品种：" + agentMode.getResultCode().getBreed());
                    textView_contact.setText("联系方式：" + agentMode.getResultCode().getPhone());
                    textView_agent.setText("代理人：" + agentMode.getResultCode().getPerson());
                    textView_address.setText("地址：" + agentMode.getResultCode().getAddress());

                    //商品ID
                    ((MyApplication) getApplication()).setGoodsId(agentMode.getResultCode().getAgentshop().get(0).getId());

                    for (int i = 0; i < agentMode.getResultCode().getAgentshop().size(); i++) {
                        list.add(agentMode.getResultCode().getAgentshop().get(i));
                    }
                    gridView.setAdapter(wineAgentAdapter);
                    wineAgentAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    //解析数据
    private AgentMode parseMode(Object obj) {
        AgentMode successMode = null;
        if (obj != null && obj instanceof AgentMode) {
            successMode = (AgentMode) obj;
        }
        return successMode;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wine_business);
        String id = ((MyApplication) getApplication()).getAgentId();

        imageView = (ImageView) findViewById(R.id.agent_img);
        //主题
        textView_title = (TextView) findViewById(R.id.agent_title);
        //品牌
        textView_brand = (TextView) findViewById(R.id.agent_brand);
        //品种
        textView_varieties = (TextView) findViewById(R.id.agent_varieties);
        //代理商
        textView_agent = (TextView) findViewById(R.id.agent_agent);
        //联系方式
        textView_contact = (TextView) findViewById(R.id.agent_contact);
        //地址
        textView_address = (TextView) findViewById(R.id.agent_address);

        gridView = (GridView) findViewById(R.id.agent_gridView);
        gridView.setOnItemClickListener(this);
        wineAgentAdapter = new WineAgentAdapter(list, this);
        gridView.setAdapter(wineAgentAdapter);
        httpTools.getPlace(mHandler, "1", null, null, null, "10", null, id, 2);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position==0){
            startActivity(new Intent(WineBusiness.this,WineAgentDetails.class));
        }
    }
}
