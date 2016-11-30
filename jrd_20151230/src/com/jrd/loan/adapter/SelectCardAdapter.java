package com.jrd.loan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.bean.BankCardBean;
import com.jrd.loan.util.BankLogoUtil;

import java.util.List;

public class SelectCardAdapter extends BaseAdapter {

    private List<BankCardBean> mList;
    private Context mContext;

    public SelectCardAdapter(List<BankCardBean> mList, Context mContext) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.loan_select_card_item_layout, null);
            holder.cardLayout = (RelativeLayout) convertView.findViewById(R.id.loan_recharge_bankinfoLayout);
            holder.cardName = (TextView) convertView.findViewById(R.id.loan_recharge_bankNameTV);
            holder.cardImg = (ImageView) convertView.findViewById(R.id.loan_recharge_bankiconImg);
            holder.cardTail = (TextView) convertView.findViewById(R.id.loan_recharge_bankTailNumTV);
            holder.cardTail.setVisibility(View.GONE);

            holder.arrowImg = (ImageView) convertView.findViewById(R.id.loan_recharge_arrowImg);
            holder.arrowImg.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.cardImg.setImageResource(R.drawable.loan_current_income_img);
        // ImageLoader.loadImage(holder.cardImg,
        // mList.get(position).getBankImg());

        BankLogoUtil.setBankLog(holder.cardImg, Integer.parseInt(mList.get(position).getBankCode()));

        String tail = mList.get(position).getCardNumber();
        holder.cardName.setText(new StringBuffer().append(mList.get(position).getOrderName()).append("(尾号").append(tail.substring(tail.length() - 4, tail.length())).append(")").toString());

        return convertView;
    }

    class ViewHolder {

        RelativeLayout cardLayout;
        TextView cardName;
        TextView cardTail;
        ImageView cardImg;
        ImageView arrowImg;
    }

}
