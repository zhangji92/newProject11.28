package com.example.administrator.beijingplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.beijingplayer.ChangsuoActivity;
import com.example.administrator.beijingplayer.KuaipaiActivity;
import com.example.administrator.beijingplayer.R;
import com.example.administrator.beijingplayer.WineAgentActivity;
import com.example.administrator.beijingplayer.adpter.PlayerAdapter;
import com.example.administrator.beijingplayer.mode.Player;
import com.example.administrator.beijingplayer.util.ModeCode;

import java.util.ArrayList;
import java.util.List;


public class ButtomFragment2 extends FatherFragment {

    private List<Player.ResultCodeBean> list = new ArrayList<>();

    private LinearLayout kuaipai;
    private LinearLayout changsuo;
    private LinearLayout jiushuidl;
    private ListView listView;
    private PlayerAdapter playerAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ModeCode.PLAYER_WHAT:
                    getServerMessage(msg.obj); break;

            }
        }
    };

    public ButtomFragment2() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取服务器数据
        httpTools.getPlayerMessage(handler,null,null,null,null,null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_buttom_fragment2, container, false);
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    @Override
    protected void initView(View view){
        listView = (ListView) view.findViewById(R.id.fragment2_list);
        playerAdapter = new PlayerAdapter(getActivity(),list);
        listView.setAdapter(playerAdapter);

        kuaipai =(LinearLayout) view.findViewById(R.id.fragment2_linear23);
        kuaipai.setOnClickListener(this);
        changsuo =(LinearLayout) view.findViewById(R.id.fragment2_linear21);
        changsuo.setOnClickListener(this);
        jiushuidl =(LinearLayout) view.findViewById(R.id.fragment2_linear11);
        jiushuidl.setOnClickListener(this);
    }

    /**
     * 获取服务器的信息
     * @param obj
     */
    @Override
    protected void getServerMessage(Object obj){
        if(obj !=null && obj instanceof Player){
            Player player = (Player)obj;
            List<Player.ResultCodeBean> aa = player.getResultCode();
            list.addAll(aa);
            playerAdapter.setList(list);
            playerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.fragment2_linear23:
                Intent intent1 = new Intent(getActivity(), KuaipaiActivity.class);
                startActivity(intent1); break;
            case R.id.fragment2_linear21:

                Intent intent2 = new Intent(getActivity(), ChangsuoActivity.class);
                startActivity(intent2); break;
            case R.id.fragment2_linear11:
                Intent intent3 = new Intent(getActivity(), WineAgentActivity.class);
                startActivity(intent3); break;
        }

    }
}
