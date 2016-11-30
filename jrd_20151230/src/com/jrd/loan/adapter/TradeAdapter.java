package com.jrd.loan.adapter;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import com.jrd.loan.R;
import com.jrd.loan.bean.TradeBean;
import com.jrd.loan.bean.TradeBean.DetailList;
import com.jrd.loan.util.DoubleUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TradeAdapter extends BaseAdapter {

    private Context mContext;
    private List<DetailList> mList = new ArrayList<DetailList>();

    public TradeAdapter(List<DetailList> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.loan_trade_item_layout, null);
            holder.money = (TextView) convertView.findViewById(R.id.trade_item_amount);
            holder.time = (TextView) convertView.findViewById(R.id.trade_item_time);
            holder.title = (TextView) convertView.findViewById(R.id.trade_item_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int type = Integer.valueOf(mList.get(position).getType());
        if (type == 101) {
            holder.title.setText("充值");
        } else if (type == 102) {
            holder.title.setText("提现");
        } else if (type == 201 || type == 202 || type == 203) {
            holder.title.setText("投资");
        } else if (type == 401 || type == 402 || type == 403 || type == 4011 || type == 4021 || type == 4013) {
            holder.title.setText("回款");
        } else if (type == 205) {
            holder.title.setText("奖励");
        } else if (type == 206) {
            holder.title.setText("优惠券兑换");
        } else if (type == 4020) {
            holder.title.setText("卖出回款");
        } else if (type == 2010) {
            holder.title.setText("购买");
        } else if (type == 4010 || type == 4030) {
            holder.title.setText("购入回款");
        } else if (type == 2011 || type == 2012 || type == 2013) {
            holder.title.setText("7付你");
        } else {
            holder.title.setText("全部");
        }
        if (mList.get(position).getMoney().contains("-")) {
            holder.money.setTextColor(Color.rgb(53, 169, 149));
            holder.money.setText(DoubleUtil.getMoney(mList.get(position).getMoney()));
        } else {
            holder.money.setTextColor(Color.rgb(232, 109, 139));
            holder.money.setText("+" + DoubleUtil.getMoney(mList.get(position).getMoney()));
        }
        holder.time.setText(mList.get(position).getTime());
        return convertView;
    }

    class ViewHolder {

        TextView time;
        TextView title;
        TextView money;
    }
}
