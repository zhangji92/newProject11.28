package com.jrd.loan.finance;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.Coupon;
import com.jrd.loan.util.DialogUtils;
import com.jrd.loan.util.DialogUtils.OnButtonEventListener;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;

/**
 * 使用优惠券
 *
 * @author Jacky
 */
public class CouponActivity extends BaseActivity {
  private ListView couponList;
  private CouponAdapter couponAdapter;
  private ArrayList<Coupon> listCoupon;
  private Coupon gCounpon;

  @Override
  protected int loadWindowLayout() {
    return R.layout.loan_coupon_layout;
  }

  @Override
  protected void initViews() {
    if (getIntent().getSerializableExtra("coupon") != null) {
      this.gCounpon = (Coupon) getIntent().getSerializableExtra("coupon");
    }

    this.couponList = (ListView) findViewById(R.id.couponList);
    this.couponList.setOnItemClickListener(this.itemClickListener);

    this.getCouponList();
  }

  private void getCouponList() {
    if (null != this.getIntent().getSerializableExtra("mPackets")) {
      listCoupon = (ArrayList<Coupon>) this.getIntent().getSerializableExtra("mPackets");
      couponAdapter = new CouponAdapter(CouponActivity.this, listCoupon);
      couponList.setAdapter(couponAdapter);
    }
  }

  private OnItemClickListener itemClickListener = new OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
      showDialog(listCoupon.get(position).getHbDesc());
    }
  };

  private void showDialog(String mHbDesc) {
    String btnText = getResources().getString(R.string.loan_close);

    DialogUtils.showCouponsDialog(CouponActivity.this, mHbDesc, btnText, false, new OnButtonEventListener() {

      @Override
      public void onConfirm() {}

      @Override
      public void onCancel() {}

    });
  }

  private class CouponAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<Coupon> couponList;

    public CouponAdapter(Context context, ArrayList<Coupon> couponList) {
      this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      this.couponList = couponList;
    }

    public void updateCouponState(int position) {
      int size = this.couponList.size();
      Coupon coupon = null;

      for (int i = 0; i < size; i++) {
        coupon = this.couponList.get(i);

        if (i != position && coupon.getState() == 1) {
          coupon.setState(0);
        } else if (i == position) {
          coupon.setState(1);
        }
      }

      notifyDataSetChanged();
    }

    public Coupon getCoupon() {
      for (Coupon coupon : this.couponList) {
        if (coupon.getState() == 1) {
          return coupon;
        }
      }

      return null;
    }

    @Override
    public int getCount() {
      return this.couponList.size();
    }

    @Override
    public Object getItem(int position) {
      return this.couponList.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
      ViewHolder viewHolder = null;

      if (convertView == null) {
        convertView = this.layoutInflater.inflate(R.layout.loan_coupon_list_item_layout, null);
        viewHolder = new ViewHolder();
        convertView.setTag(viewHolder);

        viewHolder.content = (TextView) convertView.findViewById(R.id.tvUseRule);
        viewHolder.deadLine = (TextView) convertView.findViewById(R.id.tvDeadline);
        viewHolder.money = (TextView) convertView.findViewById(R.id.tvMoney);
        viewHolder.chooseCoupon = (ImageView) convertView.findViewById(R.id.chooseCoupon);
        viewHolder.couponLayout = (LinearLayout) convertView.findViewById(R.id.couponLayout);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      Coupon coupon = this.couponList.get(position);

      // 有限期
      viewHolder.deadLine.setText(R.string.loan_expire);
      viewHolder.deadLine.append(coupon.getExpireTime());

      // 券的描述
      viewHolder.content.setText(coupon.getHbDesc());

      if (coupon.isAvailable()) {// 有效的优惠券
        viewHolder.chooseCoupon.setVisibility(View.VISIBLE);
        viewHolder.couponLayout.setBackgroundResource(R.drawable.loan_coupon_list_item_shape);
        if (coupon.getHbType().equals(Coupon.COUPON_TYPE_CASH)) {// 现金券
          viewHolder.couponLayout.setBackgroundResource(R.drawable.loan_notused_cash_coupon);
          StringBuffer buffer = new StringBuffer();
          buffer.append("<font color='#EA6D8D'>").append("￥").append(new DecimalFormat("#.00").format(coupon.getBagVal())).append("</font>");
          viewHolder.money.setText(Html.fromHtml(buffer.toString()));
        } else if (coupon.getHbType().equals(Coupon.COUPON_TYPE_ADD_PROFIT)) {// 加息券
          viewHolder.couponLayout.setBackgroundResource(R.drawable.loan_notused_plus_rate_coupon);
          StringBuffer buffer = new StringBuffer();
          buffer.append("<font color='#30B49F'>").append("+").append(new DecimalFormat("#.0").format(coupon.getBagVal())).append("%</font>");
          viewHolder.money.setText(Html.fromHtml(buffer.toString()));
        }

        if (null != gCounpon) {
          if (gCounpon.getHbEntId() == coupon.getHbEntId()) {
            coupon.setState(1);
          }
        }
        if (coupon.getState() == 1) {// 选中
          viewHolder.chooseCoupon.setImageResource(R.drawable.loan_regist_complete_pinkarrow);
        } else {// 未选中
          viewHolder.chooseCoupon.setImageResource(R.drawable.loan_coupon_unsel);
        }

        viewHolder.chooseCoupon.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            switch (view.getId()) {
              case R.id.chooseCoupon:
                backBidScreen();
                break;
            }
          }

          private void backBidScreen() {
            updateCouponState(position);
            Coupon coupon = couponAdapter.getCoupon();
            if (coupon == null) {
              ToastUtil.showShort(CouponActivity.this, R.string.loan_choose_coupon);
            } else {
              Intent intent = new Intent();
              intent.putExtra("coupon", coupon);
              setResult(2000, intent);
              finish();
            }
          }
        });

      } else {// 无效的优惠券
        viewHolder.chooseCoupon.setVisibility(View.GONE);
        // viewHolder.chooseCoupon.setImageResource(R.drawable.loan_coupon_expire_drawable);
        if (coupon.getHbType().equals(Coupon.COUPON_TYPE_CASH)) {// 现金券
          viewHolder.couponLayout.setBackgroundResource(R.drawable.loan_use_cash_coupon);
          StringBuffer buffer = new StringBuffer();
          buffer.append("<font color='#9FA1A0'>").append("￥").append(new DecimalFormat("#.00").format(coupon.getBagVal())).append("</font>");
          viewHolder.money.setText(Html.fromHtml(buffer.toString()));
        } else if (coupon.getHbType().equals(Coupon.COUPON_TYPE_ADD_PROFIT)) {// 加息券
          viewHolder.couponLayout.setBackgroundResource(R.drawable.loan_use_plusrate_coupon);
          StringBuffer buffer = new StringBuffer();
          buffer.append("<font color='#9FA1A0'>").append("+").append(new DecimalFormat("#.0").format(coupon.getBagVal())).append("%</font>");
          viewHolder.money.setText(Html.fromHtml(buffer.toString()));
        }
      }


      return convertView;
    }

    private class ViewHolder {
      TextView deadLine;
      TextView content;
      TextView money;
      ImageView chooseCoupon;
      LinearLayout couponLayout;
    }
  }

  @Override
  protected void initTitleBar() {
    WindowView windowView = (WindowView) findViewById(R.id.windowView);

    // 使用优惠券
    windowView.setTitle(R.string.loan_use_coupon);

    // 返回按钮单击事件
    windowView.setLeftButtonClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }
}
