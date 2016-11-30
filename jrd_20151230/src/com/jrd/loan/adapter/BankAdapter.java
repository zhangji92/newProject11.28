package com.jrd.loan.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.bean.BankCardBean;

public class BankAdapter extends BaseAdapter {

    private List<BankCardBean> cityList;
    private Context mContext;

    public BankAdapter(List<BankCardBean> cityList, Context mContext) {
        this.cityList = cityList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return cityList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View view, ViewGroup arg2) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.loan_city_item_layout, null);
            holder.cityName = (TextView) view.findViewById(R.id.autoTv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.cityName.setText(cityList.get(arg0).getBankName());

        return view;
    }

    class ViewHolder {
        TextView cityName;
    }

}
