package com.example.administrator.beijingplayer.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.beijingplayer.R;
import com.example.administrator.beijingplayer.mode.ThreePage;
import com.example.administrator.beijingplayer.util.ModeCode;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class ThreeAdapter extends SexAdpter<ThreePage.ResultCodeBean> {

    public ThreeAdapter(Context context, List<ThreePage.ResultCodeBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.thress_list,null);

        ImageView img1 = (ImageView) view.findViewById(R.id.three_list_img);
        finalBitmap.display(img1, ModeCode.HTTP+list.get(position).getPic());

        TextView text1 = (TextView) view.findViewById(R.id.three_list_text1);
        text1.setText(list.get(position).getShopname());

        TextView text2 = (TextView) view.findViewById(R.id.three_list_text2);
        text2.setText(list.get(position).getHuodong());

        TextView text3 = (TextView) view.findViewById(R.id.three_list_text3);
        text3.setText("地址:"+list.get(position).getAddress());

        TextView text4 = (TextView) view.findViewById(R.id.three_list_text4);
        text4.setText(list.get(position).getContent());

        TextView text5 = (TextView) view.findViewById(R.id.three_list_text5);
        text5.setText(list.get(position).getJuli());

        return view;
    }
}
