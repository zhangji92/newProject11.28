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
import com.example.lenovo.amuse.activity.WineBusiness;
import com.example.lenovo.amuse.mode.SuccessMode;
import com.example.lenovo.amuse.mode.WineMode;
import com.example.lenovo.amuse.util.BaseUri;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

import static com.example.lenovo.amuse.mode.WineMode.*;

/**
 * Created by lenovo on 2016/10/14.
 * 酒水适配器
 */

public class WineAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    Context context;
    List<WineMode.ResultCodeBean> list;

    public WineAdapter(Context context, List<WineMode.ResultCodeBean> list) {
        this.context = context;
        this.list = list;
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

        View view = layoutInflater.from(context).inflate(R.layout.wine_son, null);
        //图片
        ImageView imageView = (ImageView) view.findViewById(R.id.wine_img);
        FinalBitmap finalBitmap = FinalBitmap.create(context);
        finalBitmap.display(imageView, BaseUri.BASE + list.get(position).getPic());
        //标题
        TextView textView_title = (TextView) view.findViewById(R.id.wine_title);
        textView_title.setText(list.get(position).getAgentname());
        //品牌
        TextView textView_brand = (TextView) view.findViewById(R.id.wine_brand);
        textView_brand.setText("品牌：" + list.get(position).getBrand());
        //品种
        TextView textView_breed = (TextView) view.findViewById(R.id.wine_varieties);
        textView_breed.setText("品种：" + list.get(position).getBreed());
        //代理人
        TextView textView_person = (TextView) view.findViewById(R.id.wine_agent);
        textView_person.setText("代理人：" + list.get(position).getPerson());
        //代理人联系方式
        TextView textView_phone = (TextView) view.findViewById(R.id.wine_contact);
        textView_phone.setText("联系方式：" + list.get(position).getPhone());
        //地址
        TextView textView_address = (TextView) view.findViewById(R.id.wine_address);
        textView_address.setText("联系方式：" + list.get(position).getAddress());


        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.wine_intent);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, WineBusiness.class);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
