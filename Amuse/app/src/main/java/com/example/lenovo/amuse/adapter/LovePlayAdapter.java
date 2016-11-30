package com.example.lenovo.amuse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.mode.LovePlayMode;
import com.example.lenovo.amuse.util.BaseUri;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * Created by lenovo on 2016/9/27.
 * 同城爱玩适配器
 */

public class LovePlayAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<LovePlayMode.ResultCodeBean> modeList;

    public LovePlayAdapter(Context context, List<LovePlayMode.ResultCodeBean> modeList) {
        this.context = context;
        this.modeList = modeList;
    }

    @Override
    public int getCount() {
        return modeList.size() == 0 ? 0 : modeList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.from(context).inflate(R.layout.love, null);
        //主题
        TextView textView_title = (TextView) view.findViewById(R.id.love_title);
        textView_title.setText(modeList.get(position).getShopname());
        ImageView imageView = (ImageView) view.findViewById(R.id.love_image);

        FinalBitmap finalBitmap = FinalBitmap.create(context);
        finalBitmap.display(imageView, BaseUri.BASE + modeList.get(position).getPic());

        //接单数量
        TextView textView_single = (TextView) view.findViewById(R.id.love_sigle);
        textView_single.setText("接单数量:" + modeList.get(position).getOrdernum() + "单");
        //地址
        TextView textView_address = (TextView) view.findViewById(R.id.love_address);
        textView_address.setText("地址:" + modeList.get(position).getAddress());
        //距离
        TextView textView_location = (TextView) view.findViewById(R.id.love_location_data);
        textView_location.setText(modeList.get(position).getJuli() + "km");
        //内容
        TextView textView_context = (TextView) view.findViewById(R.id.love_context);
        textView_context.setText(modeList.get(position).getContent());
        return view;
    }
}
