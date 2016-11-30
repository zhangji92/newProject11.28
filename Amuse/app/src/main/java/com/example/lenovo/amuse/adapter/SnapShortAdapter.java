package com.example.lenovo.amuse.adapter;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.activity.BaseActivity;
import com.example.lenovo.amuse.mode.SnapShortMode;
import com.example.lenovo.amuse.util.BaseUri;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * Created by lenovo on 2016/9/27.
 * GridView适配器
 */

public class SnapShortAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    List<SnapShortMode.ResultCodeBean.QuickphotoBean> beanList;

    public SnapShortAdapter(Context context, List<SnapShortMode.ResultCodeBean.QuickphotoBean> beanList) {
        this.context = context;
        this.beanList = beanList;
    }

    @Override
    public int getCount() {
        return beanList.size() == 0 ? 0 : beanList.size();
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
        View view = layoutInflater.from(context).inflate(R.layout.snap, null);
        ImageView imageView_snap = (ImageView) view.findViewById(R.id.son_snap_img);
        FinalBitmap finalBitmap = FinalBitmap.create(context);
        finalBitmap.display(imageView_snap, beanList.get(position).getVimage());
        //主题
        TextView textView_title = (TextView) view.findViewById(R.id.son_snap_title);
        textView_title.setText(beanList.get(position).getTitle());
        //名字
        TextView textView_name = (TextView) view.findViewById(R.id.son_snap_name);
        textView_name.setText(beanList.get(position).getNickname());
        //数量
        TextView textView_count = (TextView) view.findViewById(R.id.son_snap_count);
        textView_count.setText(beanList.get(position).getCount());

        return view;
    }
}
