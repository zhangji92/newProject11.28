package com.jrd.loan.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.bean.ReturnBean.BackPlans;
import com.jrd.loan.util.DateUtil;

public class ReturnMoneyAdapter extends BaseAdapter {

    private Context mContext;
    private List<BackPlans> mList;

    public ReturnMoneyAdapter(Context mContext, List<BackPlans> mList) {
        this.mContext = mContext;
        this.mList = mList;
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
        ViewHoler holer;
        if (convertView == null) {
            holer = new ViewHoler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.loan_return_item_layout, null);
            holer.money = (TextView) convertView.findViewById(R.id.loan_return_item_money);
            holer.name = (TextView) convertView.findViewById(R.id.loan_return_item_name);
            holer.time = (TextView) convertView.findViewById(R.id.loan_return_item_time);
            holer.type = (TextView) convertView.findViewById(R.id.loan_return_item_type);
            convertView.setTag(holer);
        } else {
            holer = (ViewHoler) convertView.getTag();
        }
        holer.money.setText(mList.get(position).getMoney());
        holer.name.setText(mList.get(position).getProjectName());
        holer.time.setText(DateUtil.getYMD(Long.parseLong(mList.get(position).getRepaymentDate())));
        String type = mList.get(position).getType();
        if (type.equals("501")) {
            holer.type.setText("利息");
        } else if (type.equals("502")) {
            holer.type.setText("本息");
        } else if (type.equals("503")) {
            holer.type.setText("本金");
        }
        return convertView;
    }

    class ViewHoler {

        TextView type;
        TextView name;
        TextView time;
        TextView money;
    }
}
