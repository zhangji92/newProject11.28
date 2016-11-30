package com.jrd.loan.myaccount;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.adapter.ReturnMoneyAdapter;
import com.jrd.loan.api.PayApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.ReturnBean;
import com.jrd.loan.bean.ReturnBean.BackPlans;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.ToastUtil;
import com.jrd.loan.widget.WindowView;
import com.jrd.loan.widget.XListView;
import com.jrd.loan.widget.XListView.IXListViewListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 回款计划
 *
 * @author Javen
 */
public class ReturnMoneyAct extends BaseActivity implements IXListViewListener {

    private WindowView windowView;
    private CustomDatePickerDialog dateDialog;
    private RelativeLayout dateLayout;
    private XListView mListView;
    private List<BackPlans> mList;
    private ReturnMoneyAdapter mAdapter;
    private TextView yearTV;
    private TextView mounthTV;
    private TextView endTV;
    private TextView notTv;
    private boolean isRefresh = false;
    private int pageNo = 1;
    private int pageSize = 1000;
    private int year;
    private int month;
    private int day;
    private boolean isMonth = false;// 是否是月份选择
    private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {

        /**
         * params：view：该事件关联的组件 params：myyear：当前选择的年 params：monthOfYear：当前选择的月 params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
            // 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year = myyear;
            month = monthOfYear;
            day = dayOfMonth;
            // 更新日期
            updateDate();
        }

        // 当DatePickerDialog关闭时，更新日期显示
        private void updateDate() {
            // 在TextView上显示日期
            yearTV.setText(year + "年");
            mounthTV.setText((month + 1) + "月");
            pageSize = 1000;
            pageNo = 1;
            isMonth = true;
            RequestData();
        }
    };

    @Override
    protected int loadWindowLayout() {
        return R.layout.loan_activity_returnmoney;
    }

    @Override
    protected void initTitleBar() {
        windowView = (WindowView) findViewById(R.id.windowView);
        windowView.setVisibility(View.INVISIBLE);
        windowView.setTitle("回款计划");
        windowView.setLeftButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        dateLayout = (RelativeLayout) findViewById(R.id.loan_return_dateLayout);
        mListView = (XListView) findViewById(R.id.loan_return_listview);
        mListView.setEmptyView(findViewById(R.id.nodatalayout));
        mListView.setPullLoadEnable(false);
        mListView.setXListViewListener(this);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        yearTV = (TextView) findViewById(R.id.loan_return_yearTV);
        mounthTV = (TextView) findViewById(R.id.loan_return_mounthTV);
        endTV = (TextView) findViewById(R.id.loan_return_endTV);
        notTv = (TextView) findViewById(R.id.loan_return_notTV);

        // 初始化Calendar日历对象
        Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
        Date mydate = new Date(); // 获取当前日期Date对象
        mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
        year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
        month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
        day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天
        yearTV.setText(year + "年");
        mounthTV.setText((month + 1) + "月");

        dateLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog = new CustomDatePickerDialog(ReturnMoneyAct.this, Datelistener, year, month, day);
                dateDialog.setTitle(year + "年" + (month + 1) + "月");
                dateDialog.show();
                DatePicker dp = findDatePicker((ViewGroup) dateDialog.getWindow().getDecorView());
                if (dp != null) {
                    ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                }
            }
        });
        RequestData();
        setNoNetworkClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestData();
            }
        });
    }

    // 请求数据
    private void RequestData() {
        PayApi.GetPlan(ReturnMoneyAct.this, pageNo + "", pageSize + "", year + "", (month + 1) + "", new OnHttpTaskListener<ReturnBean>() {

            // private View noNetworkLayoutView;

            @Override
            public void onStart() {
                if (isMonth) {// 显示普通进度条
                    ShowProDialog(ReturnMoneyAct.this);
                } else {// 显示动画进度条
                    if (!isRefresh) {
                        ShowDrawDialog(ReturnMoneyAct.this);
                    }
                }
            }

            @Override
            public void onTaskError(int resposeCode) {
                DismissDialog();
                mListView.stopRefresh();
                mListView.stopLoadMore();
                // this.noNetworkLayoutView.setVisibility(View.VISIBLE);
                windowView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        isMonth = false;
                        RequestData();
                    }
                });
            }

            @Override
            public void onFinishTask(ReturnBean bean) {
                DismissDialog();
                isRefresh = false;
                mListView.stopRefresh();
                mListView.stopLoadMore();
                if (bean != null) {
                    if (bean.getResultCode() == 0) {
                        windowView.setVisibility(View.VISIBLE);
                        notTv.setText(bean.getUnBackTotalMoney());
                        endTV.setText(bean.getBackedTotalMoney());
                        if (bean.getBackPlans() != null) {
                            mList = bean.getBackPlans();
                            mAdapter = new ReturnMoneyAdapter(ReturnMoneyAct.this, mList);
                            mListView.setAdapter(mAdapter);
                        }
                    } else {
                        ToastUtil.showShort(ReturnMoneyAct.this, bean.getResultMsg());
                    }
                }
            }
        });
    }

    // 去掉datepicker日选项
    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null) return result;
                }
            }
        }
        return null;
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        pageSize = 1000;
        isRefresh = true;
        isMonth = false;
        RequestData();
    }

    @Override
    public void onLoadMore() {

    }


    // 修改标题栏显示
    class CustomDatePickerDialog extends DatePickerDialog {

        public CustomDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            super.onDateChanged(view, year, month, day);
            dateDialog.setTitle(year + "年" + (month + 1) + "月");
        }
    }
}
