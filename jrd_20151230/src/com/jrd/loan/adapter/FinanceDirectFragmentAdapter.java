package com.jrd.loan.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.bean.ProjectList;
import com.jrd.loan.util.DigitUtil;
import com.jrd.loan.widget.RoundProgressBar;

public class FinanceDirectFragmentAdapter extends BaseAdapter {
  public List<ProjectList> list;
  private final Context context;

  public FinanceDirectFragmentAdapter(Context context, List<ProjectList> list) {
    super();
    this.context = context;
    this.list = list;
  }

  public void addBidList(List<ProjectList> list) {
    if (this.list == null) {
      this.list = list;
    } else {
      this.list.addAll(list);
    }

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

  private String getBidStartTime(String bidStartTime) {
    StringBuffer buffer = new StringBuffer();

    String[] strArr = bidStartTime.split(" ");

    if (strArr != null) {
      for (String str : strArr) {
        if (!TextUtils.isEmpty(str)) {
          buffer.append(str.trim());
          buffer.append("\n");
        }
      }

      buffer.deleteCharAt(buffer.length() - 1);
    }

    return buffer.toString();
  }

  // 处理标的状态
  private void handBidStatus(String status, RoundProgressBar proBar, TextView moneyTitle, TextView InvestmentAmount, String bidStartTime, ImageView finance_noviceImg) {
    if (status.equals("110")) {
      proBar.setProgress(0);

      InvestmentAmount.setVisibility(View.GONE);
      moneyTitle.setText(this.getBidStartTime(bidStartTime));
    } else if (status.equals("209")) {// 投标完成
      proBar.setProgress(0);

      InvestmentAmount.setVisibility(View.GONE);
      moneyTitle.setText(R.string.loan_btn_bid_state_full);
    } else if (status.equals("301")) {// 还款中
      proBar.setProgress(0);

      finance_noviceImg.setVisibility(View.GONE);

      InvestmentAmount.setVisibility(View.GONE);
      moneyTitle.setText(R.string.loan_btn_bid_state_payback);
    } else if (status.equals("309")) {// 还款完成
      proBar.setProgress(0);

      InvestmentAmount.setVisibility(View.GONE);
      moneyTitle.setText(R.string.loan_btn_bid_state_pay_complete);
    } else if (status.equals("401")) {// 关闭---已满额
      proBar.setProgress(0);

      InvestmentAmount.setVisibility(View.GONE);
      moneyTitle.setText(R.string.loan_btn_bid_state_closed);
    } else {
      InvestmentAmount.setVisibility(View.VISIBLE);
      moneyTitle.setText(R.string.loan_amount_investment);
    }
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.loan_finance_direct_investment_item, null);
      viewHolder = new ViewHolder();
      viewHolder.finance_direct_name = (TextView) convertView.findViewById(R.id.loan_finance_direct_name);
      viewHolder.finance_direct_proLogo = (ImageView) convertView.findViewById(R.id.loan_finance_direct_proLogo);
      viewHolder.finance_direct_projectName = (TextView) convertView.findViewById(R.id.loan_finance_direct_projectName);
      viewHolder.finance_direct_proType = (ImageView) convertView.findViewById(R.id.loan_finance_direct_proType);
      viewHolder.finance_direct_Vip = (ImageView) convertView.findViewById(R.id.loan_finance_direct_vip);
      viewHolder.finance_yearRate = (TextView) convertView.findViewById(R.id.loan_finance_yearRate);
      viewHolder.finance_add = (TextView) convertView.findViewById(R.id.loan_finance_add);
      viewHolder.finance_yearRatePercentNum = (TextView) convertView.findViewById(R.id.loan_finance_yearRatePercentNum);
      viewHolder.finance_project_duration = (TextView) convertView.findViewById(R.id.loan_finance_project_duration);
      viewHolder.finance_circlePro = (RoundProgressBar) convertView.findViewById(R.id.loan_finance_circlePro);
      viewHolder.directMoneyTitle = (TextView) convertView.findViewById(R.id.directMoneyTitle);
      viewHolder.finance_investment_amount = (TextView) convertView.findViewById(R.id.loan_finance_investment_amount);
      viewHolder.finance_noviceImg = (ImageView) convertView.findViewById(R.id.loan_finance_noviceImg);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    final ProjectList mProjectList = list.get(position);

    if (mProjectList.getIsNovice().equals("1")) {
      viewHolder.finance_circlePro.setCricleProgressColor(0xFFea6d8d);
      viewHolder.finance_noviceImg.setVisibility(View.VISIBLE);
    } else if (mProjectList.getIsNovice().equals("0")) {
      viewHolder.finance_circlePro.setCricleProgressColor(0xFF3CAFFF);
      viewHolder.finance_noviceImg.setVisibility(View.GONE);
    }

    if (mProjectList.getType().equals("100")) {
      viewHolder.finance_direct_proType.setImageResource(R.drawable.loan_pro_enterprises);
    } else if (mProjectList.getType().equals("200") || mProjectList.getType().equals("600")) {
      viewHolder.finance_direct_proType.setImageResource(R.drawable.loan_pro_real);
    } else if (mProjectList.getType().equals("300")) {
      viewHolder.finance_direct_proType.setImageResource(R.drawable.loan_pro_type);
    } else if (mProjectList.getType().equals("400")) {
      viewHolder.finance_direct_proType.setImageResource(R.drawable.loan_pro_nuclear);
    } else if (mProjectList.getType().equals("500")) {
      viewHolder.finance_direct_proType.setImageResource(R.drawable.loan_pro_fire);
    }

    if (mProjectList.getIsVip().equals("1")) {
      viewHolder.finance_direct_Vip.setImageResource(R.drawable.loan_pro_vip);
    }

    viewHolder.finance_direct_name.setText(mProjectList.getProjectName());
    // viewHolder.finance_direct_proLogo.setImageResource(book.getmImage());
    viewHolder.finance_direct_projectName.setText(mProjectList.getBrandName());
    viewHolder.finance_yearRate.setText(mProjectList.getAnnualRate());

    if (mProjectList.getRewardRate().equals("0")) {
      viewHolder.finance_add.setVisibility(View.GONE);
      viewHolder.finance_yearRatePercentNum.setVisibility(View.GONE);
    } else {
      viewHolder.finance_add.setVisibility(View.VISIBLE);
      viewHolder.finance_yearRatePercentNum.setVisibility(View.VISIBLE);
      viewHolder.finance_yearRatePercentNum.setText(mProjectList.getRewardRate());
    }

    viewHolder.finance_project_duration.setText(DigitUtil.getMonth(mProjectList.getInvestmentPeriod()));
    viewHolder.finance_circlePro.setMax(100);
    viewHolder.finance_circlePro.setProgress(Long.parseLong(mProjectList.getInvestPercentage()));

    viewHolder.finance_investment_amount.setText(DigitUtil.getMoney(mProjectList.getInvestmentAmount()));

    this.handBidStatus(mProjectList.getStatus(), viewHolder.finance_circlePro, viewHolder.directMoneyTitle, viewHolder.finance_investment_amount, mProjectList.getComingInfo(), viewHolder.finance_noviceImg);

    return convertView;
  }

  class ViewHolder {
    // 产品详情
    TextView finance_direct_name;
    ImageView finance_direct_proLogo;
    TextView finance_direct_projectName;
    ImageView finance_direct_proType;
    ImageView finance_direct_Vip;
    TextView finance_yearRate;
    TextView finance_add;
    TextView finance_yearRatePercentNum;
    TextView finance_project_duration;
    RoundProgressBar finance_circlePro;
    TextView directMoneyTitle;
    TextView finance_investment_amount;
    ImageView finance_noviceImg;
  }
}
