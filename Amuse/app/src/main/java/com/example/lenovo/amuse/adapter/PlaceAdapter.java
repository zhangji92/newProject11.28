package com.example.lenovo.amuse.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.activity.PlaceDetails;
import com.example.lenovo.amuse.activity.TableAddActivity;
import com.example.lenovo.amuse.mode.LovePlayMode;
import com.example.lenovo.amuse.mode.PlaceMode;
import com.example.lenovo.amuse.util.BaseUri;

import net.tsz.afinal.FinalBitmap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2016/10/16.
 * 场所适配器
 */

public class PlaceAdapter extends BaseAdapter {

    private List<PlaceMode.ResultCodeBean> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private String strFlag;

    public PlaceAdapter(List<PlaceMode.ResultCodeBean> list, Context context, String flag) {
        this.list = list;
        this.context = context;
        this.strFlag = flag;
    }

    @Override
    public int getCount() {
        return list.size() == 0 ? 0 : list.size();
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
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = layoutInflater.from(context).inflate(R.layout.love, null);

        //主题
        TextView textView_title = (TextView) view.findViewById(R.id.love_title);
        textView_title.setText(list.get(position).getShopname());
        ImageView imageView = (ImageView) view.findViewById(R.id.love_image);

        FinalBitmap finalBitmap = FinalBitmap.create(context);
        finalBitmap.display(imageView, BaseUri.BASE + list.get(position).getPic());

        //接单数量
        TextView textView_single = (TextView) view.findViewById(R.id.love_sigle);
        textView_single.setText("接单数量:" + list.get(position).getOrdernum() + "单");
        //地址
        TextView textView_address = (TextView) view.findViewById(R.id.love_address);
        textView_address.setText("地址:" + list.get(position).getAddress());
        //距离
        TextView textView_location = (TextView) view.findViewById(R.id.love_location_data);
        textView_location.setText(list.get(position).getJuli() + "km");
        //内容
        TextView textView_context = (TextView) view.findViewById(R.id.love_context);
        textView_context.setText(list.get(position).getContent());
        return view;
    }
}
