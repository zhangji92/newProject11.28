package com.jrd.loan.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.bean.RecordsList;
import com.jrd.loan.net.imageloader.ImageLoader;

public class PaymentPopWinAdapter extends BaseAdapter {
    private Context context;
    public List<RecordsList> list;
    private int mType;// 0付款PopupWindow, 1收款PopupWindow

    public PaymentPopWinAdapter(Context context, List<RecordsList> list, int mType) {
        super();
        this.context = context;
        this.list = list;
        this.mType = mType;
    }

    public void updateMoreBids(List<RecordsList> list) {
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.loan_paymentpopwin_item, null);
            viewHolder = new ViewHolder();
            viewHolder.bankCardImg = (ImageView) convertView.findViewById(R.id.loan_bankCardImg);
            viewHolder.tvBackCard = (TextView) convertView.findViewById(R.id.loan_tvBackCard);
            viewHolder.tvBackCardMoney = (TextView) convertView.findViewById(R.id.loan_tvBackCardMoney);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final RecordsList mNotUsedCoupon = list.get(position);

        if (mType == 0) {// 收款
            StringBuffer switchToMaxSb = new StringBuffer();
            if (mNotUsedCoupon.getType().equals("0")) {
                switchToMaxSb.append(context.getResources().getString(R.string.loan_AvailableAmount)).append("<font color=#0f9be3>").append(mNotUsedCoupon.getAmount()).append("元</font>");
            } else if (mNotUsedCoupon.getType().equals("1")) {
                switchToMaxSb.append(context.getResources().getString(R.string.loan_TransferableAmount)).append("<font color=#0f9be3>").append(mNotUsedCoupon.getAmount()).append("元</font>");
            }

            if (mNotUsedCoupon.getType().equals("1")) {
                StringBuffer CouponSb = new StringBuffer();
                String strCoupon = mNotUsedCoupon.getCardNumber();
                strCoupon = strCoupon.substring(strCoupon.length() - 4, strCoupon.length());
                CouponSb.append(mNotUsedCoupon.getName()).append("(尾号").append(strCoupon).append(")");
                viewHolder.tvBackCard.setText(CouponSb.toString());
            } else {
                viewHolder.tvBackCard.setText(mNotUsedCoupon.getName());
            }

            ImageLoader.loadLocalImage(viewHolder.bankCardImg, mNotUsedCoupon.getBankImg());

            viewHolder.tvBackCardMoney.setText(Html.fromHtml(switchToMaxSb.toString()));
        } else if (mType == 1) {// 付款
            if (mNotUsedCoupon.getType().equals("1")) {
                StringBuffer CouponSb = new StringBuffer();

                String strCoupon = mNotUsedCoupon.getCardNumber();
                strCoupon = strCoupon.substring(strCoupon.length() - 4, strCoupon.length());

                CouponSb.append("(尾号").append(strCoupon).append(")");

                ImageLoader.loadLocalImage(viewHolder.bankCardImg, mNotUsedCoupon.getBankImg());

                viewHolder.tvBackCard.setText(mNotUsedCoupon.getName());

                viewHolder.tvBackCardMoney.setText(CouponSb.toString());

            } else if (mNotUsedCoupon.getType().equals("2")) {
                StringBuffer switchToMaxSb = new StringBuffer();
                switchToMaxSb.append(context.getResources().getString(R.string.loan_AvailableAmount)).append("<font color=#0f9be3>").append(mNotUsedCoupon.getAmount()).append("元</font>");
                if (mNotUsedCoupon.getName().equals(context.getResources().getString(R.string.loan_account_pocket_text_profit))) {
                    viewHolder.tvBackCard.setText(mNotUsedCoupon.getName());
                    viewHolder.tvBackCardMoney.setText(Html.fromHtml(switchToMaxSb.toString()));
                    viewHolder.bankCardImg.setImageResource(R.drawable.loan_current_income_img);
                } else if (mNotUsedCoupon.getName().equals(context.getResources().getString(R.string.loan_account_pocket_text_principal))) {
                    viewHolder.tvBackCard.setText(mNotUsedCoupon.getName());
                    viewHolder.tvBackCardMoney.setText(Html.fromHtml(switchToMaxSb.toString()));
                    viewHolder.bankCardImg.setImageResource(R.drawable.loan_current_pocket_img);
                }
            } else {
                ImageLoader.loadLocalImage(viewHolder.bankCardImg, mNotUsedCoupon.getBankImg());
                viewHolder.tvBackCard.setText(mNotUsedCoupon.getName());
                viewHolder.tvBackCardMoney.setVisibility(View.GONE);
            }

        }

        return convertView;
    }

    class ViewHolder {
        ImageView bankCardImg;// 银行卡图标
        TextView tvBackCard;// 银行卡名称
        TextView tvBackCardMoney;// 可用额度
    }

}
