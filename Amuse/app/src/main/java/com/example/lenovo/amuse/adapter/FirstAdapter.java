package com.example.lenovo.amuse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.mode.FirstPageMode;
import com.example.lenovo.amuse.util.BaseUri;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * Created by lenovo on 2016/9/26.
 * 首页的适配器
 */

public class FirstAdapter extends BaseAdapter {

    private List<FirstPageMode.ResultCodeBean.RecommendBean.HengBean> list;

    private LayoutInflater layoutInflater;
    private Context context;

    public FirstAdapter(List<FirstPageMode.ResultCodeBean.RecommendBean.HengBean> list, Context context) {
        this.list = list;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.from(context).inflate(R.layout.first, null);
        TextView textView_title = (TextView) view.findViewById(R.id.id_first_title);
        TextView textView_address = (TextView) view.findViewById(R.id.id_fist_address);
        textView_title.setText(list.get(position).getShopname());
        textView_address.setText(list.get(position).getAddress());
        ImageView imageView = (ImageView) view.findViewById(R.id.id_first_image);
        FinalBitmap finalBitmap = FinalBitmap.create(context);
        finalBitmap.display(imageView, BaseUri.BASE + list.get(position).getPic());
        return view;
    }
}
