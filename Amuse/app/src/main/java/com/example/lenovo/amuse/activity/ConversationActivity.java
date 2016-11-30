package com.example.lenovo.amuse.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.example.lenovo.amuse.MyApplication;
import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.util.BaseUri;
import com.makeramen.roundedimageview.RoundedImageView;

import io.rong.imkit.RongIM;

/**
 * Created by 张继 on 2016/10/30.
 * 会话界面
 */

public class ConversationActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);

        initView();
        initData();

//        RongIM.connect()
    }

    private void initData() {
        if (((MyApplication)getApplication()).getmTableListMode()!=null){

        }
    }

    private void initView() {
//        //个人头像
//        RoundedImageView roundedImageView_user = (RoundedImageView) view.findViewById(R.id.table_adapter_userImg);
//        mFinalBitmap.display(roundedImageView_user, BaseUri.BASE + mList.get(position).getUpic());
//        //用户名
//        TextView text_user = (TextView) view.findViewById(R.id.table_adapter_user);
//        text_user.setText(mList.get(position).getNickname());
//        //时间
//        TextView text_time = (TextView) view.findViewById(R.id.table_adapter_time);
//        text_time.setText(mList.get(position).getAddtime());
//        //距离
//        TextView text_distance = (TextView) view.findViewById(R.id.table_adapter_location);
//        text_distance.setText(mList.get(position).getAway());
//        //群聊人数
//        TextView text_number = (TextView) view.findViewById(R.id.table_adapter_number);
//        text_number.setText("群聊人数:" + mList.get(position).getStarlevel());
//        //图片
//        RoundedImageView roundedImageView_viewUser = (RoundedImageView) view.findViewById(R.id.table_adapter_viewImg);
//        mFinalBitmap.display(roundedImageView_viewUser, BaseUri.BASE + mList.get(position).getPic());
//        //主题
//        TextView text_title = (TextView) view.findViewById(R.id.table_adapter_title);
//        text_title.setText(mList.get(position).getTitle());
//        //shopName
//        TextView text_shop = (TextView) view.findViewById(R.id.table_adapter_viewTitle);
//        text_shop.setText(mList.get(position).getShopname());
//        //地址
//        TextView text_address = (TextView) view.findViewById(R.id.table_adapter_viewAddress);
//        text_address.setText(mList.get(position).getAddress());
//        //内容
//        TextView text_content = (TextView) view.findViewById(R.id.table_adapter_viewContent);
//        text_content.setText(mList.get(position).getContent());

    }
}
