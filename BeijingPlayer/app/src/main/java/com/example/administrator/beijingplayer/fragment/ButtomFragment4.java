package com.example.administrator.beijingplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.beijingplayer.LoginActivity;
import com.example.administrator.beijingplayer.MyActivity;
import com.example.administrator.beijingplayer.R;
import com.example.administrator.beijingplayer.RegisterActivity;
import com.example.administrator.beijingplayer.application.App;
import com.example.administrator.beijingplayer.mode.UserMessage;
import com.example.administrator.beijingplayer.util.ModeCode;
import com.example.administrator.beijingplayer.util.SharedUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import net.tsz.afinal.FinalBitmap;


public class ButtomFragment4 extends FatherFragment {

    private RoundedImageView img1;

    private TextView text1;

    public ButtomFragment4() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttom_fragment4, container, false);
        initView(view);
        return view;
    }

    @Override
    protected void initView(View view) {
        text1 = (TextView) view.findViewById(R.id.my_text2);
        text1.setOnClickListener(this);
        img1 = (RoundedImageView) view.findViewById(R.id.my_img1);
        img1.setOnClickListener(this);
    }

    @Override
    protected void getServerMessage(Object object) {}

    @Override
    public void onResume() {
        boolean islogin = SharedUtil.getSharedUtil().getBoolean(getActivity(),ModeCode.IS_LOGIN);
        if(islogin==true){
            UserMessage userMessage = (UserMessage) SharedUtil.getSharedUtil().getObject(getActivity(),ModeCode.USER,new UserMessage());
            text1.setText(userMessage.getNickname()+"\n\n关注:"+userMessage.getAttention_count()+" | 粉丝:"+userMessage.getFans_count());
            FinalBitmap finalBitmap = FinalBitmap.create(getActivity());
            finalBitmap.display(img1, ModeCode.HTTP+userMessage.getImgUrl());
        }else{
            text1.setText(R.string.fragment4_login);
            img1.setImageResource(R.mipmap.ic_launcher);
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.my_text2:
                clickMyMessage();break;
            case R.id.my_img1:
                clickMyMessage();break;

        }
    }

    /**
     * 点击第四页上部分执行
     */
    private void clickMyMessage(){
        String res = text1.getText().toString();
        if(res.contains("登录")){
            Intent intent1 = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent1);
        }else{
            Intent intent2 = new Intent(getActivity(), MyActivity.class);
            startActivity(intent2);
        }

    }
}
