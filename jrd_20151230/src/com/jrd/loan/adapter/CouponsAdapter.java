package com.jrd.loan.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.api.FinanceApi;
import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.bean.NotUsedCoupon;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.ToastUtil;

public class CouponsAdapter extends BaseAdapter {
  private Context context;
  public List<NotUsedCoupon> list;
  private int item_back_id;

  public CouponsAdapter(Context context, List<NotUsedCoupon> list) {
    super();
    this.context = context;
    this.list = list;
  }

  public void updatePageFirstBid(List<NotUsedCoupon> list) {
    this.list.clear();
    this.list.addAll(list);

    notifyDataSetChanged();
  }

  public void updateMoreBids(List<NotUsedCoupon> list) {
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

  @SuppressLint("ResourceAsColor")
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    final ViewHolder viewHolder;
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.loan_coupons_item, null);
      viewHolder = new ViewHolder();
      viewHolder.coupons_item_bck = (LinearLayout) convertView.findViewById(R.id.loan_coupons_item_bck);
      viewHolder.coupons_time = (TextView) convertView.findViewById(R.id.loan_coupons_time);
      viewHolder.coupons_hbDesc = (TextView) convertView.findViewById(R.id.loan_coupons_hbDesc);
      viewHolder.coupons_bagVal = (TextView) convertView.findViewById(R.id.loan_coupons_bagVal);
      viewHolder.coupons_cash = (ImageView) convertView.findViewById(R.id.loan_coupons_cash);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    final NotUsedCoupon mNotUsedCoupon = list.get(position);
    // 1,现金券、2,加息券
    if (mNotUsedCoupon.getHbType().equals("1")) {

      StringBuffer BagValStr = new StringBuffer();
      BagValStr.append("￥").append(new DecimalFormat("#.00").format(Double.valueOf(mNotUsedCoupon.getBagVal())));
      viewHolder.coupons_bagVal.setText(Html.fromHtml("<font color=#E86D8B>" + BagValStr.toString() + "</font>"));

      // 红包使用状态（0未使用、1正使用、2已领取、3已失效）
      if (mNotUsedCoupon.getUseStatus().equals("0")) {
        item_back_id = R.drawable.loan_notused_cash_coupon;
        if ((mNotUsedCoupon.getHbStatus().equals("3") && (mNotUsedCoupon.getHbType().equals("1") || mNotUsedCoupon.getHbType().equals("99")))) {
          viewHolder.coupons_cash.setVisibility(View.VISIBLE);
          viewHolder.coupons_cash.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
              FinanceApi.exchangeRedPacket(context, mNotUsedCoupon.getHbEntId(), new OnHttpTaskListener<BaseBean>() {

                @Override
                public void onTaskError(int resposeCode) {}

                @Override
                public void onStart() {}

                @Override
                public void onFinishTask(BaseBean bean) {
                  if (bean.getResultCode() == 0) {
                    ToastUtil.showShort(context, bean.getResultMsg());
                    viewHolder.coupons_cash.setVisibility(View.GONE);
                  } else {
                    ToastUtil.showShort(context, bean.getResultMsg());
                  }
                }
              });
            }
          });
        } else {
          viewHolder.coupons_cash.setVisibility(View.GONE);
        }
      } else if (mNotUsedCoupon.getUseStatus().equals("1")) {
        item_back_id = R.drawable.loan_notused_cash_coupon;
      } else if (mNotUsedCoupon.getUseStatus().equals("2")) {
        item_back_id = R.drawable.loan_use_cash_coupon;
      } else if (mNotUsedCoupon.getUseStatus().equals("3")) {
        item_back_id = R.drawable.loan_use_cash_coupon;
      }
    } else if (mNotUsedCoupon.getHbType().equals("2")) {

      StringBuffer BagValStr = new StringBuffer();
      BagValStr.append("+").append(new DecimalFormat("#.0").format(Double.valueOf(mNotUsedCoupon.getBagVal())));
      viewHolder.coupons_bagVal.setText(Html.fromHtml("<font color=#3BB6A2>" + BagValStr + "%</font>"));

      if (mNotUsedCoupon.getUseStatus().equals("0")) {
        item_back_id = R.drawable.loan_notused_plus_rate_coupon;
        if ((mNotUsedCoupon.getHbStatus().equals("3") && (mNotUsedCoupon.getHbType().equals("1") || mNotUsedCoupon.getHbType().equals("99")))) {
          viewHolder.coupons_cash.setVisibility(View.VISIBLE);
          viewHolder.coupons_cash.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
              FinanceApi.exchangeRedPacket(context, mNotUsedCoupon.getHbEntId(), new OnHttpTaskListener<BaseBean>() {

                @Override
                public void onTaskError(int resposeCode) {}

                @Override
                public void onStart() {}

                @Override
                public void onFinishTask(BaseBean bean) {
                  if (bean.getResultCode() == 0) {
                    ToastUtil.showShort(context, bean.getResultMsg());
                    viewHolder.coupons_cash.setVisibility(View.GONE);
                  } else {
                    ToastUtil.showShort(context, bean.getResultMsg());
                  }
                }
              });
            }
          });
        } else {
          viewHolder.coupons_cash.setVisibility(View.GONE);
        }
      } else if (mNotUsedCoupon.getUseStatus().equals("1")) {
        item_back_id = R.drawable.loan_notused_plus_rate_coupon;
      } else if (mNotUsedCoupon.getUseStatus().equals("2")) {
        item_back_id = R.drawable.loan_use_plusrate_coupon;
      } else if (mNotUsedCoupon.getUseStatus().equals("3")) {
        item_back_id = R.drawable.loan_use_plusrate_coupon;
      }
    }
    viewHolder.coupons_item_bck.setBackgroundResource(item_back_id);
    viewHolder.coupons_time.setText("有效期至：" + mNotUsedCoupon.getExpireTime());

    String mHbDesc = mNotUsedCoupon.getHbDesc();
    mHbDesc = mHbDesc.replaceAll("<br>", "\n");

    viewHolder.coupons_hbDesc.setText(mHbDesc.toString());


    return convertView;
  }

  class ViewHolder {
    LinearLayout coupons_item_bck;// item背景图片
    ImageView coupons_cash;// 兑换状态
    TextView coupons_time;// 失效时间
    TextView coupons_hbDesc;// 红包描述
    TextView coupons_bagVal;// 红包值
  }


}
