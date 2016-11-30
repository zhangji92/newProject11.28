package com.example.administrator.beijingplayer.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.administrator.beijingplayer.mode.ShopMessage;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * 基类适配器
 */
public abstract class SexAdpter<T> extends BaseAdapter {

    protected List<T> list = new ArrayList<>();
    protected Context context;
    protected FinalBitmap finalBitmap ;

    public SexAdpter(Context context, List<T> list){
        this.context =context;
        this.list.addAll(list);
        finalBitmap = FinalBitmap.create(context);
    }
    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list.addAll(list) ;
    }
}
