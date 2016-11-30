package com.example.lenovo.amuse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.lenovo.amuse.R;
import com.example.lenovo.amuse.mode.SnapShortDetailsMode;
import com.example.lenovo.amuse.util.BaseUri;
import com.makeramen.roundedimageview.RoundedImageView;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * Created by 张继 on 2016/10/25.
 * 评论适配器
 */

public class CommentAdapter extends BaseAdapter {

    List<SnapShortDetailsMode.ResultCodeBean.CommentBean> mList;
    Context context;
    LayoutInflater layoutInflater;

    public CommentAdapter(List<SnapShortDetailsMode.ResultCodeBean.CommentBean> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList.size()==0?0:mList.size();
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
        View view=layoutInflater.from(context).inflate(R.layout.comment_adapter,null);
        RoundedImageView roundedImageView= (RoundedImageView) view.findViewById(R.id.comment_adapter_img);
        FinalBitmap finalBitmap=FinalBitmap.create(context);
        finalBitmap.display(roundedImageView, BaseUri.BASE+mList.get(position).getPic());

        TextView textView_user= (TextView) view.findViewById(R.id.comment_adapter_user);
        textView_user.setText(mList.get(position).getNickname());

        TextView textView_time= (TextView) view.findViewById(R.id.comment_adapter_time);
        textView_time.setText(mList.get(position).getAddtime());

        TextView textView_content= (TextView) view.findViewById(R.id.comment_adapter_content);
        textView_content.setText(mList.get(position).getContent());

        return view;
    }
}
