package com.example.lenovo.amuse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.mode.WineDetails;
import com.example.lenovo.amuse.util.BaseUri;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/19.
 * 酒水代理详情
 */

public class WineDetailsAdapter extends BaseAdapter {
//    List<String> list=new ArrayList<>();
    List<String> list;
    Context context;
    LayoutInflater layoutInflater;

    public WineDetailsAdapter(List<String> list, Context context) {
//        this.list .addAll(list);
        this.list=list;
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
        View view = layoutInflater.from(context).inflate(R.layout.wine_details, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.details_adapter_img);
        FinalBitmap finalBitmap = FinalBitmap.create(context);
        finalBitmap.display(imageView, BaseUri.BASE + list.get(position));
        return view;
    }
}
