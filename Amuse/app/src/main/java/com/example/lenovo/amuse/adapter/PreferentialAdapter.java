package com.example.lenovo.amuse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.mode.PreferentialMode;
import com.example.lenovo.amuse.util.BaseUri;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * Created by lenovo on 2016/9/27.
 * 优惠专区适配器
 */

public class PreferentialAdapter extends BaseAdapter {
    private List<PreferentialMode.ResultCodeBean> modeList;
    private Context context;
    private LayoutInflater layoutInflater;

    public PreferentialAdapter(List<PreferentialMode.ResultCodeBean> modeList, Context context) {
        this.modeList = modeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return modeList.size()==0?0:modeList.size();
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
        View view = layoutInflater.from(context).inflate(R.layout.pre, null);
        //获取图片
        ImageView imageView = (ImageView) view.findViewById(R.id.pre_img);
        FinalBitmap finalBitmap = FinalBitmap.create(context);
        finalBitmap.display(imageView, BaseUri.BASE + modeList.get(position).getPic());
        //主题
        TextView textView_title = (TextView) view.findViewById(R.id.pre_title);
        textView_title.setText(modeList.get(position).getShopname());

        ImageView imageView1_money = (ImageView) view.findViewById(R.id.pre_money);
        TextView textView_fracture = (TextView) view.findViewById(R.id.pre_fracture);

        //打折
        if (modeList.get(position).getHuodong().contains("折")) {
            imageView1_money.setImageResource(R.mipmap.fracture);
            textView_fracture.setText(modeList.get(position).getHuodong());
        } else {
            imageView1_money.setImageResource(R.mipmap.money);
            textView_fracture.setText(modeList.get(position).getHuodong());
        }
        //内容
        TextView textView_context = (TextView) view.findViewById(R.id.pre_context);
        textView_context.setText(modeList.get(position).getContent());

        //地址
        TextView textView_address = (TextView) view.findViewById(R.id.pre_address);
        textView_address.setText(modeList.get(position).getAddress());
        //距离
        TextView textView_distance = (TextView) view.findViewById(R.id.pre_distance);
        textView_distance.setText(modeList.get(position).getJuli());
        return view;
    }
}
