package com.allactivity.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allactivity.R;

import java.util.List;

/**
 * Created by 张继 on 2016/11/10.
 * 下拉刷新的数据
 */

public class ListViewAdapter extends BaseAdapter {
    private List<ImageMessage> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContent;

    public ListViewAdapter(Context mContent, List<ImageMessage> mList) {
        this.mContent = mContent;
        this.mList = mList;
    }
    public void onDateChange(List<ImageMessage> mList){
        this.mList = mList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size() == 0 ? 0 : mList.size();
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
        View view=mLayoutInflater.from(mContent).inflate(R.layout.list_view_adapter,null);

        ImageView imageView_img= (ImageView) view.findViewById(R.id.adapter_image);
        imageView_img.setImageResource(mList.get(position).getPic());

        TextView text_title= (TextView) view.findViewById(R.id.adapter_title);
        text_title.setText(mList.get(position).getTitle());

        TextView text_content= (TextView) view.findViewById(R.id.adapter_content);
        text_content.setText(mList.get(position).getContent());

        return view;
    }
}
