package com.jrd.loan.myaccount;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.jrd.loan.R;
import com.jrd.loan.api.PayApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.AssetsBean;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.DoubleUtil;
import com.jrd.loan.widget.WindowView;

import java.util.ArrayList;

public class MyAssetsAct extends BaseActivity {

    private WindowView windowView;
    private PieChart pieChart;
    private TextView principalTV;
    private TextView incomeTV;
    private TextView amountTV;
    private TextView frozenAMountTV;
    // private TextView totalInvestTV;
    // private TextView totalIncomeTV;
    private String totalInvest = "0"; // 总资产

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_my_assets_act;
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setTitle("我的资产");
        windowView.setVisibility(View.INVISIBLE);
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        pieChart = (PieChart) findViewById(R.id.loan_assets_piechart);
        principalTV = (TextView) findViewById(R.id.loan_assets_principal);
        incomeTV = (TextView) findViewById(R.id.loan_assets_income);
        amountTV = (TextView) findViewById(R.id.loan_assets_amount);
        frozenAMountTV = (TextView) findViewById(R.id.loan_assets_frozen_amount);
        // totalInvestTV = (TextView) findViewById(R.id.loan_assets_total_invest_amount);
        // totalIncomeTV = (TextView) findViewById(R.id.loan_assets_total_income_amount);
        InitPieChart();
        setNoNetworkClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestData();
            }
        });
    }

    private void InitPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(85f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.getLegend().setEnabled(false);
        RequestData();
    }

    private void RequestData() {
        PayApi.GetAssets(MyAssetsAct.this, new OnHttpTaskListener<AssetsBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowDrawDialog(MyAssetsAct.this);
            }

            @Override
            public void onFinishTask(AssetsBean bean) {
                DismissDialog();
                if (bean != null && bean.getResultCode() == 0) {
                    windowView.setVisibility(View.VISIBLE);
                    totalInvest = DoubleUtil.getMoney(bean.getTotalMoney());
                    // totalIncomeTV.setText(DoubleUtil.getMoney(bean.getBackedInterest()));
                    // totalInvestTV.setText(DoubleUtil.getMoney(bean.getSumInvestAmount()));
                    String principal = DoubleUtil.getMoney(bean.getBackingPrincipal());
                    principalTV.setText(principal);
                    String income = DoubleUtil.getMoney(bean.getBackingInterest());
                    incomeTV.setText(income);
                    String amount = DoubleUtil.getMoney(bean.getAccountBalance());
                    amountTV.setText(amount);
                    String frozenAmount = DoubleUtil.getMoney(bean.getFrozenMoney());
                    frozenAMountTV.setText(frozenAmount);
                    setData(principal, income, amount, frozenAmount);
                }
            }
        });
    }

    /**
     * @param principal    本金比例
     * @param income       收益比例
     * @param amount       可用余额比例
     * @param frozenAmount 冻结余额比例
     */
    private void setData(String principal, String income, String amount, String frozenAmount) {
        ArrayList<String> xValues = new ArrayList<String>(); // xVals用来表示每个饼块上的内容
        for (int i = 0; i < 4; i++) {
            // 饼块上显示成principal, income, amount, frozenAmount
            xValues.add("");
        }
        // yVals用来表示封装每个饼块的实际数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        float principalF = Float.valueOf(principal) / Float.valueOf(totalInvest) * 100;
        float incomeF = Float.valueOf(income) / Float.valueOf(totalInvest) * 100;
        float amountF = Float.valueOf(amount) / Float.valueOf(totalInvest) * 100;
        float frozenAmountF = Float.valueOf(frozenAmount) / Float.valueOf(totalInvest) * 100;
        yValues.add(new Entry(principalF, 0));
        yValues.add(new Entry(incomeF, 1));
        yValues.add(new Entry(amountF, 2));
        yValues.add(new Entry(frozenAmountF, 3));
        // y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "");
        pieDataSet.setSliceSpace(0f); // 设置个饼状图之间的距离
        pieDataSet.setSelectionShift(2f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        // 饼图颜色
        colors.add(Color.rgb(236, 130, 153));
        colors.add(Color.rgb(70, 187, 255));
        colors.add(Color.rgb(62, 179, 161));
        colors.add(Color.rgb(189, 189, 191));
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(xValues, pieDataSet);
        pieChart.setCenterText(generateCenterSpannableText());
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();
        for (DataSet<?> set : pieChart.getData().getDataSets())
            set.setDrawValues(false);
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("总资产（元）\n" + totalInvest);
        s.setSpan(new RelativeSizeSpan(1.5f), 0, 6, 0);
        s.setSpan(new RelativeSizeSpan(2.5f), 6, s.length(), 0);// 字体大小
        s.setSpan(new ForegroundColorSpan(Color.rgb(102, 94, 92)), 6, s.length(), 0);// 颜色
        return s;
    }
}
