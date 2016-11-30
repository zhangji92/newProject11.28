package com.jrd.loan.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.bean.RepaymentPlanList;
import com.jrd.loan.util.DateUtil;
import com.jrd.loan.util.DigitUtil;

public class RepaymentAdapter extends BaseAdapter {
    private Context context;
    public List<RepaymentPlanList> list;

    public RepaymentAdapter(Context context, List<RepaymentPlanList> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public void updateMoreBids(List<RepaymentPlanList> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.loan_repayment_item, null);
            viewHolder = new ViewHolder();
            viewHolder.repaymentDate = (TextView) convertView.findViewById(R.id.loan_repaymentDate);
            viewHolder.type = (TextView) convertView.findViewById(R.id.loan_type);
            viewHolder.money = (TextView) convertView.findViewById(R.id.loan_money);
            viewHolder.status = (TextView) convertView.findViewById(R.id.loan_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final RepaymentPlanList mRepaymentPlanList = list.get(position);

        String repaymentDate = DateUtil.getYMD(Long.parseLong(mRepaymentPlanList.getRepaymentDate()));
        String Type = mRepaymentPlanList.getType();
        Double Money = mRepaymentPlanList.getMoney();
        String Status = mRepaymentPlanList.getStatus();

        if (repaymentDate != null) {
            viewHolder.repaymentDate.setText(repaymentDate);
        } else {
            viewHolder.repaymentDate.setText("");
        }

        if (!TextUtils.isEmpty(Type)) {
            if (Type.equals("501")) {
                viewHolder.type.setText("利息");
            } else {
                viewHolder.type.setText("本金+利息");
            }
        } else {
            viewHolder.type.setText("");
        }

        if (!TextUtils.isEmpty(Money + "")) {
            viewHolder.money.setText(DigitUtil.getMoney(Money + ""));
        } else {
            viewHolder.money.setText("");
        }

        if (!TextUtils.isEmpty(Status)) {
            if (Status.equals("200")) {
                viewHolder.status.setText("等待还款");
            } else if (Status.equals("300")) {
                viewHolder.status.setText("已还款");
            } else {
                viewHolder.status.setText("未启动");
            }
        } else {
            viewHolder.status.setText("");
        }

        return convertView;
    }

    class ViewHolder {
        TextView repaymentDate;// List中的还款日期
        TextView type;// List还款类型
        TextView money;// List还款金额
        TextView status;// List还款状态
    }
}
