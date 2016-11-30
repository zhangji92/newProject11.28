package com.jrd.loan.current.pocket;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jrd.loan.R;
import com.jrd.loan.account.LoginActivity;
import com.jrd.loan.api.PocketApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.MyPocketBean;
import com.jrd.loan.bean.SevenBean;
import com.jrd.loan.bean.SevenBean.Records;
import com.jrd.loan.current.pocket.rehcarge.BindBankCardActivity;
import com.jrd.loan.current.pocket.rehcarge.ExplainActivity;
import com.jrd.loan.current.pocket.rehcarge.IdCheckActivity;
import com.jrd.loan.current.pocket.rehcarge.RechargeAgainActivity;
import com.jrd.loan.graphview.CustomLabelFormatter;
import com.jrd.loan.graphview.GraphView;
import com.jrd.loan.graphview.GraphView.GraphViewData;
import com.jrd.loan.graphview.GraphViewSeries;
import com.jrd.loan.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jrd.loan.graphview.LineGraphView;
import com.jrd.loan.myInfomeation.SetTransactionPswActivity;
import com.jrd.loan.myaccount.AccountRollOutActivity;
import com.jrd.loan.myaccount.AccountSwitchToActivity;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.DensityUtil;
import com.jrd.loan.util.DoubleUtil;
import com.jrd.loan.util.FormatUtils;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.AnimTextView;
import com.jrd.loan.widget.WindowView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 活期口主界面
 *
 * @author Jacky
 */
public class CurrentPocketActivity extends BaseActivity {

    private WindowView windowView;
    private AnimTextView totalAmountTV;

    private AnimTextView principalTV;
    private AnimTextView todayIncomeTV;
    private AnimTextView totalIncomeTV;
    private List<Records> mList;
    private String annualRate;
    private String leftInvestAmount;

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_current_pocket_home_layout;
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setVisibility(View.INVISIBLE);
        // 设置title
        windowView.setTitle(R.string.loan_current_pocket);
        // 右边按钮的监听事件
        windowView.setRightButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 跳转到转入转出记录界面
                startActivity(new Intent(CurrentPocketActivity.this, BillInfoActivity.class));
            }
        });
        // 设置左侧返回按钮事件
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        totalAmountTV = (AnimTextView) findViewById(R.id.loan_mypocket_totalAmountTV);
        totalIncomeTV = (AnimTextView) findViewById(R.id.loan_mypocket_totalincomeTV);
        todayIncomeTV = (AnimTextView) findViewById(R.id.loan_mypocket_todayincomeTV);
        principalTV = (AnimTextView) findViewById(R.id.loan_mypocket_principalTV);

        Button shiftButton = (Button) findViewById(R.id.button1);
        shiftButton.setOnClickListener(this.ClickListener);
        Button TurnOutButton = (Button) findViewById(R.id.button2);
        TurnOutButton.setOnClickListener(this.ClickListener);

        RequestData();
        setNoNetworkClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestData();
            }
        });
    }

    private void RequestData() {
        PocketApi.MyPocketInfo(CurrentPocketActivity.this, new OnHttpTaskListener<MyPocketBean>() {

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onStart() {
                ShowDrawDialog(CurrentPocketActivity.this);
            }

            @Override
            public void onFinishTask(MyPocketBean bean) {
                if (bean != null && bean.getResultCode() == 0) {

                    if (!bean.getCount().equals("0")) {

                        annualRate = String.valueOf(bean.getAnnualRate());
                        leftInvestAmount = bean.getLeftInvestAmount();

                        // 可提现收益
                        double income = Double.valueOf(FormatUtils.ReplaceString(bean.getProfitCanTakeOut()));
                        // 加入本金
                        double principal = Double.valueOf(DoubleUtil.getMoney(FormatUtils.ReplaceString(bean.getRemainderAmt())));

                        // 总金额 = 可提现收益 + 加入本金
                        totalAmountTV.playNumber(income + principal);
                        todayIncomeTV.playNumber(Double.parseDouble(bean.getProfitToday()));
                        totalIncomeTV.playNumber(Double.parseDouble(bean.getBackedInterest()));
                        principalTV.playNumber(Double.parseDouble(bean.getRemainderAmt()));
                    }
                }
                getSevenYearDate();
            }
        });
    }

    private OnClickListener ClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button1: // 转入
                    showSwitchTo();
                    break;
                case R.id.button2: // 转出
                    showRollOut();
                    break;
            }
        }
    };
    /**
     * 获取最近七日年化利率
     */
    private void getSevenYearDate() {
        PocketApi.SevenYearIncome(CurrentPocketActivity.this, new OnHttpTaskListener<SevenBean>() {

            @Override
            public void onStart() {
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
            }

            @Override
            public void onFinishTask(SevenBean bean) {

                DismissDialog();
                if (bean != null && bean.getResultCode() == 0) {

                    if (bean.getRecords() != null && bean.getRecords().size() > 0) {
                        windowView.setVisibility(View.VISIBLE);
                        mList = bean.getRecords();
                        double one = mList.get(6).getProfit();
                        double oneTime = Double.valueOf(mList.get(6).getDate());
                        double two = mList.get(5).getProfit();
                        double twoTime = Double.valueOf(mList.get(5).getDate());
                        double three = mList.get(4).getProfit();
                        double threeTime = Double.valueOf(mList.get(4).getDate());
                        double four = mList.get(3).getProfit();
                        double fourTime = Double.valueOf(mList.get(3).getDate());
                        double five = mList.get(2).getProfit();
                        double fiveTime = Double.valueOf(mList.get(2).getDate());
                        double six = mList.get(1).getProfit();
                        double sixTime = Double.valueOf(mList.get(1).getDate());
                        double seven = mList.get(0).getProfit();
                        double sevenTime = Double.valueOf(mList.get(0).getDate());
                        SetGraphView(one, two, three, four, five, six, seven, oneTime, twoTime, threeTime, fourTime, fiveTime, sixTime, sevenTime);
                    }

                }

            }
        });
    }

    // 转入
    private void showSwitchTo() {
        if (!UserInfoPrefs.isLogin()) {// 判断用户是否登录
            startActivity(new Intent(this, LoginActivity.class));
            this.overridePendingTransition(R.anim.loan_bottom_to_top, R.anim.loan_default);
        } else if (UserInfoPrefs.loadLastLoginUserProfile().idNumberFlag.equals("0")) {// 用户是否身份认证
            startActivity(new Intent(this, IdCheckActivity.class));
        }  else {
            Intent intent = new Intent(this, AccountSwitchToActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // intent.putExtra("leftInvestAmount", leftInvestAmount);
            // intent.putExtra("annualRate", annualRate);
            startActivity(intent);
        }
    }

    // 转出
    private void showRollOut() {
        if (!UserInfoPrefs.isLogin()) {// 判断用户是否登录
            startActivity(new Intent(this, LoginActivity.class));
            this.overridePendingTransition(R.anim.loan_bottom_to_top, R.anim.loan_default);
        } else if (UserInfoPrefs.loadLastLoginUserProfile().idNumberFlag.equals("0")) {// 用户是否身份认证
            startActivity(new Intent(this, IdCheckActivity.class));
        } else if (UserInfoPrefs.loadLastLoginUserProfile().transPwdFlag.equals("0")) {// 未设置支付密码
            startActivity(new Intent(this, SetTransactionPswActivity.class));
        } else if (Double.parseDouble(totalAmountTV.getText().toString()) <= 0) {
            ToastUtil.showShort(this, R.string.loan_no_amount_money);
        } else {
            Intent intent = new Intent(this, AccountRollOutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    /**
     * 初始化近7日曲线图
     *
     * @param one   今天
     * @param two   前一天
     * @param three 前两天
     * @param four  前三天
     * @param five  前四天
     * @param six   前五天
     * @param seven 前6天
     */
    private void SetGraphView(double one, double two, double three, double four, double five, double six, double seven, double oneTime, double twoTime, double threeTime, double fourTime, double fiveTime, double sixTime, double sevenTime) {
        GraphViewSeries exampleSeries =
                new GraphViewSeries("", new GraphViewSeriesStyle(Color.rgb(250, 98, 65), DensityUtil.sp2px(this, 5)), new GraphViewData[]{new GraphViewData(sevenTime, seven), new GraphViewData(sixTime, six), new GraphViewData(fiveTime, five), new GraphViewData(fourTime, four),
                        new GraphViewData(threeTime, three), new GraphViewData(twoTime, two), new GraphViewData(oneTime, one)});

        GraphView graphView;
        graphView = new LineGraphView(this, "");
        ((LineGraphView) graphView).setDrawBackground(true);

        // 线条色
        graphView.setBackgroundColor(Color.TRANSPARENT);
        // ((LineGraphView) graphView).setBackgroundColor(Color.argb(128, 254, 232, 226));
        ((LineGraphView) graphView).setDataPointsRadius(0);

        // 字体色
        int fontColor = Color.parseColor("#9B9A9B");

        // 风格色
        graphView.getGraphViewStyle().setGridColor(Color.parseColor("#F2F1EF"));
        graphView.getGraphViewStyle().setHorizontalLabelsColor(fontColor);
        graphView.getGraphViewStyle().setVerticalLabelsColor(fontColor);
        graphView.getGraphViewStyle().setLegendWidth(10);
        graphView.getGraphViewStyle().setLegendBorder(10);
        graphView.getGraphViewStyle().setLegendMarginBottom(10);
        graphView.getGraphViewStyle().setLegendSpacing(10);

        // x轴标签数
        graphView.getGraphViewStyle().setNumHorizontalLabels(7);

        // y轴标签数
        graphView.getGraphViewStyle().setNumVerticalLabels(6);

        // 字号
        graphView.getGraphViewStyle().setTextSize(DensityUtil.sp2px(this, 12));
        graphView.getGraphViewStyle().setVerticalLabelsAlign(Align.CENTER);
        graphView.addSeries(exampleSeries);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", Locale.CHINESE);

        graphView.setCustomLabelFormatter(new CustomLabelFormatter() {

            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    Date d = new Date(Math.round(value));
                    return dateFormat.format(d);
                }

                return null;
            }
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.loan_current_graphView);
        layout.addView(graphView);
    }
}
