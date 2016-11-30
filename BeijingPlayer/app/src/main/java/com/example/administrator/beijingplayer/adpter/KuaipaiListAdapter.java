package com.example.administrator.beijingplayer.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.beijingplayer.LoginActivity;
import com.example.administrator.beijingplayer.R;
import com.example.administrator.beijingplayer.mode.KuaipaiMessage;
import com.example.administrator.beijingplayer.KuaipaiDetialsActivity;
import com.example.administrator.beijingplayer.mode.UserMessage;
import com.example.administrator.beijingplayer.util.ModeCode;
import com.example.administrator.beijingplayer.util.SharedUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class KuaipaiListAdapter extends SexAdpter<KuaipaiMessage.ResultCodeBean.QuickphotoBean>  {

    public KuaipaiListAdapter(Context context, List<KuaipaiMessage.ResultCodeBean.QuickphotoBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  LayoutInflater.from(context).inflate(R.layout.kuaipai_gridview,null);
        ImageView img1 =(ImageView) view.findViewById(R.id.kuaipai_list_img);
        finalBitmap.display(img1, list.get(position).getVimage());
        TextView text1 =(TextView) view.findViewById(R.id.kuaipai_list_text1);
        text1.setText(list.get(position).getTitle());
        TextView text2 =(TextView) view.findViewById(R.id.kuaipai_list_text2);
        text2.setText(list.get(position).getNickname());
        TextView text3 =(TextView) view.findViewById(R.id.kuaipai_list_text3);
        text3.setText(list.get(position).getCount());
        viewClick(view,list.get(position));
        return view;
    }

    private void viewClick(View view, final KuaipaiMessage.ResultCodeBean.QuickphotoBean qui){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedUtil sharedUtil = SharedUtil.getSharedUtil();
                boolean isLogin = sharedUtil.getBoolean(context, ModeCode.IS_LOGIN);
                UserMessage user = (UserMessage) sharedUtil.getObject(context,ModeCode.USER,new UserMessage());
                if(isLogin){
                    Intent intent = new Intent(context, KuaipaiDetialsActivity.class);
                    intent.putExtra("kuaipai",qui.getId());
                    intent.putExtra("token",user.getToken());
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }

            }
        });
    }
}
