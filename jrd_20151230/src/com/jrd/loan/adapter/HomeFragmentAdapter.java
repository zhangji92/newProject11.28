package com.jrd.loan.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.bean.ProjectList;
import com.jrd.loan.util.DigitUtil;
import com.jrd.loan.widget.RoundProgressBar;

public class HomeFragmentAdapter extends BaseAdapter {
    public List<ProjectList> list;
    private final Context context;

    public HomeFragmentAdapter(Context context, List<ProjectList> list) {
        this.context = context;
        this.list = list;
    }

    public void addSeven(ProjectList projectList) {
        ProjectList project = this.list.get(0);

        if (project.getType().equals("-1")) {// 列表已经存在7付你数据
            this.list.remove(0);
            this.list.add(0, projectList);
        } else {
            this.list.add(0, projectList);
        }

        notifyDataSetChanged();
    }

    public void updatePageFirstBid(List<ProjectList> list) {
        this.list.clear();
        this.list.addAll(list);

        notifyDataSetChanged();
    }

    public void updateMoreBids(List<ProjectList> list) {
        this.list.addAll(list);

        notifyDataSetChanged();
    }

    // public void addBidList(List<ProjectList> list) {
    // this.list.addAll(list);
    //
    // notifyDataSetChanged();
    // }

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
            convertView = LayoutInflater.from(context).inflate(R.layout.loan_home_item_layout, null);
            viewHolder = new ViewHolder();
            // 产品详情
            viewHolder.home_details = (RelativeLayout) convertView.findViewById(R.id.loan_home_details);
            viewHolder.ProjectName = (TextView) convertView.findViewById(R.id.loan_home_direct_name);
            viewHolder.home_direct_proLogo = (ImageView) convertView.findViewById(R.id.loan_home_direct_proLogo);
            viewHolder.BrandName = (TextView) convertView.findViewById(R.id.loan_home_direct_projectName);
            viewHolder.home_direct_proType = (ImageView) convertView.findViewById(R.id.loan_home_direct_proType);
            viewHolder.home_direct_proVip = (ImageView) convertView.findViewById(R.id.loan_home_direct_vip);
            viewHolder.AnnualRate = (TextView) convertView.findViewById(R.id.loan_home_tvYearRate);
            viewHolder.home_add = (TextView) convertView.findViewById(R.id.loan_home_add);
            viewHolder.RewardRate = (TextView) convertView.findViewById(R.id.loan_home_yearRatePercentNum);
            viewHolder.InvestmentPeriod = (TextView) convertView.findViewById(R.id.loan_home_project_duration);
            viewHolder.moneyTitle = (TextView) convertView.findViewById(R.id.moneyTitle);
            viewHolder.home_circlePro = (RoundProgressBar) convertView.findViewById(R.id.loan_home_circlePro);
            viewHolder.InvestmentAmount = (TextView) convertView.findViewById(R.id.loan_home_investment_amount);
            viewHolder.home_noviceImg = (ImageView) convertView.findViewById(R.id.loan_home_noviceImg);

            // 7付你
            viewHolder.home_product = (RelativeLayout) convertView.findViewById(R.id.loan_home_product);
            viewHolder.loan_home_updateDesc = (TextView) convertView.findViewById(R.id.loan_home_updateDesc);
            viewHolder.home_interest_rate_test = (TextView) convertView.findViewById(R.id.loan_home_interest_rate_test);
            viewHolder.home_duration_test = (TextView) convertView.findViewById(R.id.loan_home_duration_test);
            viewHolder.home_amount_test = (TextView) convertView.findViewById(R.id.loan_home_amount_test);
            viewHolder.home_amount = (TextView) convertView.findViewById(R.id.loan_home_amount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ProjectList project = list.get(position);
        if (project.getType().equals("-1")) {
            viewHolder.home_product.setVisibility(View.VISIBLE);
            viewHolder.home_details.setVisibility(View.GONE);
            viewHolder.loan_home_updateDesc.setText(project.getBrandName());
            viewHolder.home_interest_rate_test.setText(project.getAnnualRate());

            viewHolder.home_duration_test.setText(DigitUtil.getMonth(project.getInvestmentPeriod()));

            viewHolder.home_amount_test.setText(DigitUtil.getMoneys(project.getInvestmentAmount()));

            if (Double.parseDouble(project.getInvestmentAmount()) < 10000) {
                viewHolder.home_amount.setText("元");
            } else {
                viewHolder.home_amount.setText("万");
            }

        } else {
            // 新手标的 是:1,否0
            if (project.getIsNovice().equals("1")) {
                viewHolder.home_circlePro.setCricleProgressColor(0xFFea6d8d);
                viewHolder.home_noviceImg.setVisibility(View.VISIBLE);
            } else if (project.getIsNovice().equals("0")) {
                viewHolder.home_circlePro.setCricleProgressColor(0xFF3CAFFF);
                viewHolder.home_noviceImg.setVisibility(View.GONE);
            }

            viewHolder.home_product.setVisibility(View.GONE);
            viewHolder.home_details.setVisibility(View.VISIBLE);
            viewHolder.home_circlePro.setMax(100);
            viewHolder.home_circlePro.setProgress(Long.parseLong(project.getInvestPercentage()));

            viewHolder.ProjectName.setText(project.getProjectName());// 标的名称
            // viewHolder.home_direct_proLogo.setImageResource(project.getInvestmentAmount());
            viewHolder.BrandName.setText(project.getBrandName());// 品牌名称
            if (project.getType().equals("100")) {
                viewHolder.home_direct_proType.setImageResource(R.drawable.loan_pro_enterprises);
            } else if (project.getType().equals("200") || project.getType().equals("600")) {
                viewHolder.home_direct_proType.setImageResource(R.drawable.loan_pro_real);
            } else if (project.getType().equals("300")) {
                viewHolder.home_direct_proType.setImageResource(R.drawable.loan_pro_type);
            } else if (project.getType().equals("400")) {
                viewHolder.home_direct_proType.setImageResource(R.drawable.loan_pro_nuclear);
            } else if (project.getType().equals("500")) {
                viewHolder.home_direct_proType.setImageResource(R.drawable.loan_pro_fire);
            }

            if (project.getIsVip().equals("1")) {
                viewHolder.home_direct_proVip.setImageResource(R.drawable.loan_pro_vip);
            }
            viewHolder.AnnualRate.setText(project.getAnnualRate());// 年化利率

            if (project.getRewardRate().equals("0")) {
                viewHolder.home_add.setVisibility(View.GONE);
                viewHolder.RewardRate.setVisibility(View.GONE);
            } else {
                viewHolder.home_add.setVisibility(View.VISIBLE);
                viewHolder.RewardRate.setVisibility(View.VISIBLE);
                viewHolder.RewardRate.setText(project.getRewardRate());// 年化利率复利
            }

            viewHolder.InvestmentPeriod.setText(DigitUtil.getMonth(project.getInvestmentPeriod()));// 标的期限

            viewHolder.InvestmentAmount.setText(DigitUtil.getMoney(project.getInvestmentAmount()));

        }

        handBidStatus(project.getStatus(), viewHolder.home_circlePro, viewHolder.moneyTitle, viewHolder.InvestmentAmount, project.getComingInfo());

        return convertView;
    }

    class ViewHolder {
        // 产品详情
        RelativeLayout home_details;
        TextView ProjectName;
        ImageView home_direct_proLogo;
        TextView BrandName;
        ImageView home_direct_proType;
        ImageView home_direct_proVip;
        TextView AnnualRate;
        TextView home_add;
        TextView RewardRate;
        TextView InvestmentPeriod;
        TextView moneyTitle;
        RoundProgressBar home_circlePro;
        TextView InvestmentAmount;
        ImageView home_noviceImg;

        // 理财产品item
        RelativeLayout home_product;
        TextView loan_home_updateDesc;
        TextView home_interest_rate_test;
        TextView home_duration_test;
        TextView home_amount_test;
        TextView home_amount;
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
    private void handBidStatus(String status, RoundProgressBar proBar, TextView moneyTitle, TextView InvestmentAmount, String bidStartTime) {
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
}
