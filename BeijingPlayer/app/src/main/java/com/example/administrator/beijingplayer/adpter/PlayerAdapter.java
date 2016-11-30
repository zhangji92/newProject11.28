package com.example.administrator.beijingplayer.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.beijingplayer.R;
import com.example.administrator.beijingplayer.mode.Player;
import com.example.administrator.beijingplayer.util.ModeCode;

import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class PlayerAdapter extends SexAdpter<Player.ResultCodeBean> {


    public PlayerAdapter(Context context, List<Player.ResultCodeBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.player_list,null);

        ImageView img1 = (ImageView) view.findViewById(R.id.player_list_img);
        finalBitmap.display(img1, ModeCode.HTTP+list.get(position).getPic());

        TextView text1 = (TextView) view.findViewById(R.id.player_list_text1);
        text1.setText(list.get(position).getShopname());

        TextView text2 = (TextView) view.findViewById(R.id.player_list_text2);
        text2.setText("接单数量:"+list.get(position).getOrdernum()+"单");

        TextView text3 = (TextView) view.findViewById(R.id.player_list_text3);
        text3.setText("地址:"+list.get(position).getAddress());

        TextView text4 = (TextView) view.findViewById(R.id.player_list_text4);
        text4.setText(list.get(position).getContent());

        return view;
    }


}
