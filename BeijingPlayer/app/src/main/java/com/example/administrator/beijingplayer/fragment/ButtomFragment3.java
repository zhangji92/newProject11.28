package com.example.administrator.beijingplayer.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.administrator.beijingplayer.R;
import com.example.administrator.beijingplayer.adpter.ThreeAdapter;
import com.example.administrator.beijingplayer.mode.ThreePage;
import com.example.administrator.beijingplayer.util.ModeCode;

import java.util.ArrayList;
import java.util.List;


public class ButtomFragment3 extends FatherFragment {

    private Spinner spinner1;
    private Spinner spinner2;
    private ListView listView;

    private List<ThreePage.ResultCodeBean> list = new ArrayList<>();

    private ThreeAdapter threeAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
             switch (msg.what){
                 case ModeCode.THREE_WHAT:
                     Log.e(ModeCode.TAG, "handleMessage: ------33333333" );
                     getServerMessage(msg.obj);
                     break;
             }
            super.handleMessage(msg);
        }
    };

    public ButtomFragment3() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpTools.getThreeMessage(handler,null,null,"1","1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttom_fragment3, container, false);
        initView(view);
        return view;
    }


    @Override
    protected void initView(View view) {
       spinner1 = (Spinner) view.findViewById(R.id.fragment3_spinner1);
       spinner2 = (Spinner) view.findViewById(R.id.fragment3_spinner2);
        listView = (ListView) view.findViewById(R.id.fragment3_list);
        threeAdapter = new ThreeAdapter(getActivity(),list);
        listView.setAdapter(threeAdapter);


    }

    @Override
    protected void getServerMessage(Object obj) {
        if(obj !=null && obj instanceof ThreePage){
            ThreePage player = ( ThreePage)obj;
            List<ThreePage.ResultCodeBean> aa = player.getResultCode();
            list.addAll(aa);
            threeAdapter.setList(list);
            threeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {

    }
}
