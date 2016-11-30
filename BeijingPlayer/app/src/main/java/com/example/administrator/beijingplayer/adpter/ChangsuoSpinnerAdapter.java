package com.example.administrator.beijingplayer.adpter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ChangsuoSpinnerAdapter extends SexAdpter<String> {
    public ChangsuoSpinnerAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView text = new TextView(context);
        text.setGravity(Gravity.CENTER);
        text.setText(list.get(position));
        return text;
    }
}
