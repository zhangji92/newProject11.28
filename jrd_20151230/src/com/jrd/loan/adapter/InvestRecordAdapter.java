package com.jrd.loan.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.bean.ProjectInvestsList;
import com.jrd.loan.util.DoubleUtil;

public class InvestRecordAdapter extends BaseAdapter {
    private Context context;
    public List<ProjectInvestsList> list;
    private String status;


    public InvestRecordAdapter(Context context, List<ProjectInvestsList> list, String status) {
        super();
        this.context = context;
        this.list = list;
        this.status = status;
    }

    public void updatePageFirstBid(List<ProjectInvestsList> list) {
        this.list.clear();
        this.list.addAll(list);

        notifyDataSetChanged();
    }

    public void updateMoreBids(List<ProjectInvestsList> list) {
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.loan_investrecord_item, null);
            viewHolder = new ViewHolder();
            viewHolder.nameandcode = (TextView) convertView.findViewById(R.id.loan_nameandcode);
            viewHolder.investAmount = (TextView) convertView.findViewById(R.id.loan_investAmount);
            viewHolder.moneyRate = (TextView) convertView.findViewById(R.id.loan_moneyRate);
            viewHolder.collectAmount = (TextView) convertView.findViewById(R.id.loan_collectAmount);
            viewHolder.collectAmountTitle = (TextView) convertView.findViewById(R.id.loan_collectAmount_title);
            viewHolder.perCent = (TextView) convertView.findViewById(R.id.loan_per_cent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ProjectInvestsList mProjectInvestsList = list.get(position);

        StringBuffer nameAndCode = new StringBuffer();
        nameAndCode.append(mProjectInvestsList.getName()).append(mProjectInvestsList.getCode());


        viewHolder.nameandcode.setText(nameAndCode.toString());
        viewHolder.investAmount.setText(mProjectInvestsList.getInvestAmount());
        viewHolder.moneyRate.setText(mProjectInvestsList.getMoneyRate());

        // 还款中
        if (status.equals("301")) {
            viewHolder.collectAmountTitle.setText("待收金额");
            viewHolder.perCent.setVisibility(View.GONE);
            String CollectAmount = DoubleUtil.getMoney(mProjectInvestsList.getCollectAmount());
            viewHolder.collectAmount.setText(Html.fromHtml("<font color=#ea6d8d>" + CollectAmount + "</font>"));
            // 投资中
        } else if (status.equals("201")) {
            viewHolder.collectAmountTitle.setText("投标进度");
            viewHolder.perCent.setVisibility(View.VISIBLE);
            String bidSchedule = mProjectInvestsList.getBidSchedule();
            viewHolder.collectAmount.setText(Html.fromHtml("<font color=#818181>" + bidSchedule + "</font>"));
            // 已还款
        } else if (status.equals("309")) {
            viewHolder.collectAmountTitle.setText("已收利息");
            viewHolder.perCent.setVisibility(View.GONE);
            String Interest = DoubleUtil.getMoney(mProjectInvestsList.getInterest());
            viewHolder.collectAmount.setText(Html.fromHtml("<font color=#ea6d8d>" + Interest + "</font>"));
        }

        return convertView;
    }

    class ViewHolder {
        TextView nameandcode;// 标题
        TextView investAmount;// 投资金额
        TextView moneyRate;// 收益率
        TextView collectAmountTitle;
        TextView perCent;// 百分号
        TextView collectAmount;// 代收金额
    }
}
