package com.jrd.loan.adapter;

import java.util.List;

import com.jrd.loan.R;
import com.jrd.loan.bean.TenderRecordBean.TenderList;
import com.jrd.loan.util.DateUtil;
import com.jrd.loan.util.DoubleUtil;
import com.jrd.loan.util.LogUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TenderRecordAdapter extends BaseAdapter {

    private Context mContext;
    private List<TenderList> mList;

    public TenderRecordAdapter(Context mContext, List<TenderList> mList) {
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.loan_tender_item_layout, null);
            holder.account = (TextView) convertView.findViewById(R.id.tender_person);
            holder.money = (TextView) convertView.findViewById(R.id.tender_money);
            holder.title = (TextView) convertView.findViewById(R.id.tender_time);
            holder.phoneImg = (ImageView) convertView.findViewById(R.id.tende_phone_img);
            holder.quanImg = (ImageView) convertView.findViewById(R.id.tende_quan_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //是否区分手机、PC端投资
//		if (mList.get(position).getPlatMode().equals("1")) {
//			holder.phoneImg.setVisibility(View.VISIBLE);
//		} else {
//			holder.phoneImg.setVisibility(View.GONE);
//		}
        //判断是否使用优惠券投资
//		if (mList.get(position).getUseRedPacketFlag().equals("1")) {
//			holder.quanImg.setVisibility(View.VISIBLE);
//		} else {
//			holder.quanImg.setVisibility(View.GONE);
//		}
        holder.account.setText(mList.get(position).getAccount());
        holder.money.setText(DoubleUtil.getMoney(mList.get(position).getInvestAmount()));
        String time = mList.get(position).getCreateTime();
        LogUtil.d("-------", time);
        holder.title.setText(DateUtil.getYMD(DateUtil.getTime(time)) + "\n" + DateUtil.getHMS(DateUtil.getTime(time)));
        return convertView;
    }

    class ViewHolder {

        TextView title;
        TextView account;
        TextView money;
        ImageView phoneImg;
        ImageView quanImg;
    }
}
