package com.jrd.loan.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.bean.BankListBean;
import com.jrd.loan.util.BankLogoUtil;

import java.util.List;

public class BankCardItemAdapter extends BaseAdapter {

	private List<BankListBean.RecordsEntity> mList;
	private Context mContext;

	public BankCardItemAdapter(List<BankListBean.RecordsEntity> mList, Context mContext) {
		super();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.loan_bankcard_item_layout, null);
			holder.bankIcon = (ImageView) convertView.findViewById(R.id.loan_bankcard_item_image);
			holder.bankName = (TextView) convertView.findViewById(R.id.loan_bankcard_item_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// ImageLoader.loadImage(holder.bankIcon,
		// mList.get(position).getBankImg());

		BankLogoUtil.setBankLog(holder.bankIcon, Integer.parseInt(mList.get(position).getBankCode()));

		holder.bankName.setText(mList.get(position).getOrderName());

		return convertView;
	}

	class ViewHolder {
		TextView bankName;
		ImageView bankIcon;
	}
}
