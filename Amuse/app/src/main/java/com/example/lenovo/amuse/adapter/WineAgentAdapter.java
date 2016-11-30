package com.example.lenovo.amuse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.mode.AgentMode;
import com.example.lenovo.amuse.util.BaseUri;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * Created by lenovo on 2016/10/18.
 * 酒水代理上GridView适配器
 */

public class WineAgentAdapter extends BaseAdapter {

    List<AgentMode.ResultCodeBean.AgentshopBean> list;
    LayoutInflater layoutInflater;
    Context context;

    public WineAgentAdapter(List<AgentMode.ResultCodeBean.AgentshopBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size()==0?0:list.size();
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
        View view=layoutInflater.from(context).inflate(R.layout.wine_agent,null);

        ImageView imageView_snap = (ImageView) view.findViewById(R.id.son_agent_img);
        FinalBitmap finalBitmap = FinalBitmap.create(context);
        finalBitmap.display(imageView_snap, BaseUri.BASE+list.get(position).getPic());
        //主题
        TextView textView_title = (TextView) view.findViewById(R.id.son_agent_title);
        textView_title.setText(list.get(position).getShopname());
        //价钱
        TextView textView_money = (TextView) view.findViewById(R.id.son_agent_money);
        textView_money.setText(list.get(position).getMoney());

        return view;
    }
}
