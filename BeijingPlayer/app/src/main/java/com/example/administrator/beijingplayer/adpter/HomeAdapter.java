package com.example.administrator.beijingplayer.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.beijingplayer.R;
import com.example.administrator.beijingplayer.mode.ShopMessage;
import com.example.administrator.beijingplayer.util.ModeCode;

import java.util.List;

/**
 * home页listview适配器
 */
public class HomeAdapter extends SexAdpter<ShopMessage> {

    public HomeAdapter(Context context, List<ShopMessage> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  LayoutInflater.from(context).inflate(R.layout.home_list,null);
        ImageView img1 = (ImageView) view.findViewById(R.id.home_list_img);
        finalBitmap.display(img1, ModeCode.HTTP+list.get(position).getPic());
        TextView text1 = (TextView) view.findViewById(R.id.home_list_text2);
        text1.setText(list.get(position).getAddress());
        TextView text2 = (TextView) view.findViewById(R.id.home_list_text1);
        text2.setText(list.get(position).getBusinessname());

        return view;
    }
}
