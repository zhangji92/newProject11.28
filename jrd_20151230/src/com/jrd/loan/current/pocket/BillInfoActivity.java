package com.jrd.loan.current.pocket;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrd.loan.R;
import com.jrd.loan.api.PocketApi;
import com.jrd.loan.base.BaseActivity;
import com.jrd.loan.bean.BillInfoBean;
import com.jrd.loan.bean.BillInfoBean.Record;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.DateUtil;
import com.jrd.loan.widget.XListView;
import com.jrd.loan.widget.XListView.IXListViewListener;

/**
 * 活期口袋账单流水信息
 *
 * @author Jacky
 */
public class BillInfoActivity extends BaseActivity {
  private final static int MAX_RECORD_COUNT = 10;
  private final static int MENU_INCOME = 0;
  private final static int MENU_OUTCOME = 1;
  private final static int MENU_FINANCE = 2;
  private int currMenu = MENU_INCOME;

  private Context mContext;
  private XListView billInfoList;
  private BillInfoAdatper incomeAdatper;
  private BillInfoAdatper outcomeAdatper;
  private BillInfoAdatper profitAdatper;
  private PopupWindow popupWindow;
  private RelativeLayout titleBarLayout;
  private TextView billInfoType;
  private TextView totalProfit;
  private ImageView billInfoArrow;
  private Button btnRight;
  private ImageView menuIncomeChoosenIcon;
  private ImageView menuOutcomeChoosenIcon;
  private ImageView menuProfitChoosenIcon;
  private RelativeLayout totalProfitLayout;

  private List<Record> mList;

  // 日期选择PopupWindow
  private PopupWindow datePickerView;
  private TextView[] monthArr;
  private TextView prevYear;
  private TextView nextYear;

  private boolean isShowingDatePickerView;
  private boolean isClickDismissDatePickerView;

  private YearInfo yearInfo = new YearInfo();

  private int incomePageNo = 1;
  private int outcomePageNo = 1;
  private int financePageNo = 1;

  @Override
  protected int loadWindowLayout() {
    return R.layout.loan_bill_info_layout;
  }

  /**
   * 转入
   */
  private void obtainIncomeData() {
    this.totalProfitLayout.setVisibility(View.GONE);
    this.menuIncomeChoosenIcon.setVisibility(View.VISIBLE);
    this.menuOutcomeChoosenIcon.setVisibility(View.INVISIBLE);
    this.menuProfitChoosenIcon.setVisibility(View.INVISIBLE);
    this.billInfoType.setText(R.string.loan_current_pocket_income);
    this.billInfoArrow.setImageResource(R.drawable.loan_bill_info_menu_down);

    if (this.popupWindow != null) {
      this.popupWindow.dismiss();
    }

    requestIncomData("1", this.yearInfo.getSeledYearMonth(), incomePageNo, MAX_RECORD_COUNT);
  }

  /**
   * 获取转入记录
   */
  private void requestIncomData(String flag, String month, int pageNo, int pageSize) {
    PocketApi.IntoRecord(mContext, flag, month, pageNo, pageSize, new OnHttpTaskListener<BillInfoBean>() {
      @Override
      public void onStart() {}

      @Override
      public void onTaskError(int resposeCode) {
        billInfoList.stopRefresh();
        billInfoList.stopLoadMore();
      }

      @Override
      public void onFinishTask(BillInfoBean bean) {

        billInfoList.stopRefresh();
        billInfoList.stopLoadMore();

        if (bean != null && bean.getResultCode() == 0) {
          if (bean.getRecords() != null && bean.getRecords().size() > 0) {
            incomePageNo++;

            mList = bean.getRecords();

            if (mList.size() < 10) {
              billInfoList.viewGone();
            }

            if (incomeAdatper == null) {
              incomeAdatper = new BillInfoAdatper(mContext, mList, BillInfoAdatper.BILL_TYPE_INCOME);
              billInfoList.setAdapter(incomeAdatper);
            } else {
              billInfoList.setAdapter(incomeAdatper);
              incomeAdatper.updateBillInfoList(mList);
            }
          }
        }
      }
    });
  }

  /**
   * 获取转入记录
   */
  private void loadMoreIncomData(String flag, String month, int pageNo, int pageSize) {
    PocketApi.IntoRecord(mContext, flag, month, pageNo, pageSize, new OnHttpTaskListener<BillInfoBean>() {
      @Override
      public void onStart() {}

      @Override
      public void onTaskError(int resposeCode) {
        billInfoList.stopRefresh();
        billInfoList.stopLoadMore();
      }

      @Override
      public void onFinishTask(BillInfoBean bean) {
        billInfoList.stopRefresh();
        billInfoList.stopLoadMore();
        if (bean != null && bean.getResultCode() == 0) {
          if (bean.getRecords() != null && bean.getRecords().size() > 0) {
            incomePageNo++;

            mList = bean.getRecords();

            if (mList.size() < 10) {
              billInfoList.viewGone();
            }

            if (incomeAdatper == null) {
              incomeAdatper = new BillInfoAdatper(mContext, mList, BillInfoAdatper.BILL_TYPE_INCOME);
              billInfoList.setAdapter(incomeAdatper);
            } else {
              incomeAdatper.addNewBillInfoList(mList);
            }
          }
        }
      }
    });
  }

  /**
   * 转出记录
   */
  private void obtainOutcomeData() {
    this.totalProfitLayout.setVisibility(View.GONE);
    this.menuIncomeChoosenIcon.setVisibility(View.INVISIBLE);
    this.menuOutcomeChoosenIcon.setVisibility(View.VISIBLE);
    this.menuProfitChoosenIcon.setVisibility(View.INVISIBLE);
    this.billInfoType.setText(R.string.loan_current_pocket_outcome);
    this.billInfoArrow.setImageResource(R.drawable.loan_bill_info_menu_down);

    if (this.popupWindow != null) {
      this.popupWindow.dismiss();
    }

    requestOutoData("1", yearInfo.getSeledYearMonth(), outcomePageNo, MAX_RECORD_COUNT);
  }

  /**
   * 获取转出记录
   */
  private void requestOutoData(String flag, String month, int pageNo, int pageSize) {
    PocketApi.OutoRecord(mContext, flag, month, pageNo, pageSize, new OnHttpTaskListener<BillInfoBean>() {

      @Override
      public void onStart() {}

      @Override
      public void onTaskError(int resposeCode) {
        billInfoList.stopRefresh();
        billInfoList.stopLoadMore();
      }

      @Override
      public void onFinishTask(BillInfoBean bean) {
        billInfoList.stopRefresh();
        billInfoList.stopLoadMore();
        if (bean != null && bean.getResultCode() == 0) {
          if (bean.getRecords() != null && bean.getRecords().size() > 0) {
            outcomePageNo++;

            mList = bean.getRecords();

            if (mList.size() < 10) {
              billInfoList.viewGone();
            }

            if (outcomeAdatper == null) {
              outcomeAdatper = new BillInfoAdatper(mContext, mList, BillInfoAdatper.BILL_TYPE_OUTCOME);
              billInfoList.setAdapter(outcomeAdatper);
            } else {
              billInfoList.setAdapter(outcomeAdatper);
              outcomeAdatper.updateBillInfoList(mList);
            }
          }
        }
      }
    });
  }

  private void loadMoreOutoData(String flag, String month, int pageNo, int pageSize) {
    PocketApi.OutoRecord(mContext, flag, month, pageNo, pageSize, new OnHttpTaskListener<BillInfoBean>() {

      @Override
      public void onStart() {}

      @Override
      public void onTaskError(int resposeCode) {
        billInfoList.stopRefresh();
        billInfoList.stopLoadMore();
      }

      @Override
      public void onFinishTask(BillInfoBean bean) {
        billInfoList.stopRefresh();
        billInfoList.stopLoadMore();
        if (bean != null && bean.getResultCode() == 0) {
          if (bean.getRecords() != null && bean.getRecords().size() > 0) {
            outcomePageNo++;

            mList = bean.getRecords();

            if (mList.size() < 10) {
              billInfoList.viewGone();
            }

            if (outcomeAdatper == null) {
              outcomeAdatper = new BillInfoAdatper(mContext, mList, BillInfoAdatper.BILL_TYPE_OUTCOME);
              billInfoList.setAdapter(outcomeAdatper);
            } else {
              outcomeAdatper.addNewBillInfoList(mList);
            }
          }
        }
      }
    });
  }

  /**
   * 收益明细
   */
  private void obtainProfitData() {
    this.totalProfitLayout.setVisibility(View.VISIBLE);
    this.menuIncomeChoosenIcon.setVisibility(View.INVISIBLE);
    this.menuOutcomeChoosenIcon.setVisibility(View.INVISIBLE);
    this.menuProfitChoosenIcon.setVisibility(View.VISIBLE);
    this.billInfoType.setText(R.string.loan_current_pocket_profit);
    this.billInfoArrow.setImageResource(R.drawable.loan_bill_info_menu_down);

    if (this.popupWindow != null) {
      this.popupWindow.dismiss();
    }

    requestProfitData("1", yearInfo.getSeledYearMonth(), financePageNo, MAX_RECORD_COUNT);
  }

  /**
   * 获取收益明细
   */
  private void requestProfitData(String flag, String month, int pageNo, int pageSize) {
    PocketApi.IncomeRecord(mContext, flag, month, pageNo, pageSize, new OnHttpTaskListener<BillInfoBean>() {
      @Override
      public void onStart() {}

      @Override
      public void onTaskError(int resposeCode) {
        billInfoList.stopRefresh();
        billInfoList.stopLoadMore();
      }

      @Override
      public void onFinishTask(BillInfoBean bean) {
        billInfoList.stopRefresh();
        billInfoList.stopLoadMore();
        if (bean != null && bean.getResultCode() == 0) {
          if (bean.getRecords() != null && bean.getRecords().size() > 0) {
            financePageNo++;

            totalProfit.setText("+");
            totalProfit.append(String.valueOf(bean.getTotalProfit()));

            mList = bean.getRecords();

            if (mList.size() < 10) {
              billInfoList.viewGone();
            }

            if (profitAdatper == null) {
              profitAdatper = new BillInfoAdatper(mContext, mList, BillInfoAdatper.BILL_TYPE_PROFIT);
              billInfoList.setAdapter(profitAdatper);
            } else {
              billInfoList.setAdapter(profitAdatper);
              profitAdatper.updateBillInfoList(mList);
            }
          }
        }
      }
    });
  }

  private void loadMoreProfitData(String flag, String month, int pageNo, int pageSize) {
    PocketApi.IncomeRecord(mContext, flag, month, pageNo, pageSize, new OnHttpTaskListener<BillInfoBean>() {
      @Override
      public void onStart() {}

      @Override
      public void onTaskError(int resposeCode) {
        billInfoList.stopRefresh();
        billInfoList.stopLoadMore();
      }

      @Override
      public void onFinishTask(BillInfoBean bean) {
        billInfoList.stopRefresh();
        billInfoList.stopLoadMore();
        if (bean != null && bean.getResultCode() == 0) {
          if (bean.getRecords() != null && bean.getRecords().size() > 0) {
            totalProfit.setText("+");
            totalProfit.append(String.valueOf(bean.getTotalProfit()));

            financePageNo++;

            mList = bean.getRecords();

            if (mList.size() < 10) {
              billInfoList.viewGone();
            }

            if (profitAdatper == null) {
              profitAdatper = new BillInfoAdatper(mContext, mList, BillInfoAdatper.BILL_TYPE_PROFIT);
              billInfoList.setAdapter(profitAdatper);
            } else {
              profitAdatper.addNewBillInfoList(mList);
            }
          }
        }
      }
    });
  }

  private void initDatePickerView() {
    View view = LayoutInflater.from(this).inflate(R.layout.loan_date_picker_view_layout, null);

    this.initMonthView(view);

    this.datePickerView = new PopupWindow(this);
    this.datePickerView.setWidth(getResources().getDisplayMetrics().widthPixels);
    this.datePickerView.setHeight((int) (getResources().getDimension(R.dimen.loan_pop_menu_height) * 3 + getResources().getDimension(R.dimen.loan_divider_line_height) * 5));
    this.datePickerView.setTouchable(true);
    this.datePickerView.setOutsideTouchable(true);
    this.datePickerView.setContentView(view);
    this.datePickerView.setBackgroundDrawable(getResources().getDrawable(R.drawable.loan_pop_menu_bg));

    this.datePickerView.setOnDismissListener(new OnDismissListener() {
      @Override
      public void onDismiss() {
        if (!isClickDismissDatePickerView) {
          isShowingDatePickerView = false;
        }
      }
    });
  }

  private void initMonthView(View view) {
    this.monthArr =
        new TextView[] {(TextView) view.findViewById(R.id.monthJanuary), (TextView) view.findViewById(R.id.monthFebruary), (TextView) view.findViewById(R.id.monthMarch), (TextView) view.findViewById(R.id.monthApril), (TextView) view.findViewById(R.id.monthMay),
            (TextView) view.findViewById(R.id.monthJune), (TextView) view.findViewById(R.id.monthJuly), (TextView) view.findViewById(R.id.monthAugust), (TextView) view.findViewById(R.id.monthSeptember), (TextView) view.findViewById(R.id.monthOctober),
            (TextView) view.findViewById(R.id.monthNovember), (TextView) view.findViewById(R.id.monthDecember)};

    this.monthArr[this.yearInfo.getCurrMonth() - 1].setTextColor(getResources().getColor(R.color.loan_month_text_color));

    this.prevYear = (TextView) view.findViewById(R.id.prevYear);
    this.prevYear.setText(getString(R.string.loan_year_record, this.yearInfo.getCurrYear() - 1));
    this.prevYear.setOnClickListener(this.monthClickListener);

    this.nextYear = (TextView) view.findViewById(R.id.nextYear);
    this.nextYear.setText(getString(R.string.loan_year_record, this.yearInfo.getCurrYear()));
    this.nextYear.setEnabled(false);

    this.nextYear.setOnClickListener(this.monthClickListener);

    for (TextView month : this.monthArr) {
      month.setOnClickListener(this.monthClickListener);
    }
  }

  private OnClickListener monthClickListener = new OnClickListener() {
    @Override
    public void onClick(View view) {
      switch (view.getId()) {
        case R.id.nextYear:
          this.nextYear();
          break;
        case R.id.prevYear:
          this.prevYear();
          break;
        default:
          int monthTag = Integer.parseInt((String) view.getTag());
          setChooseMonthTextColor(monthTag);
          isClickDismissDatePickerView = false;
          hideDatePickerView();

          // 如果选中的年月没有改变, 不加载数据
          if (yearInfo.getSelYear() != yearInfo.getSeledYear() || yearInfo.getPrevSeledMonth() != yearInfo.getSeledMonth()) {
            if (mList != null) {
              mList.clear();
            }
            billInfoList.setAdapter(null);
            if (currMenu == MENU_INCOME) {
              incomePageNo = 1;
              requestIncomData("1", yearInfo.getSeledYearMonth(), incomePageNo, MAX_RECORD_COUNT);
            } else if (currMenu == MENU_OUTCOME) {
              outcomePageNo = 1;
              requestOutoData("1", yearInfo.getSeledYearMonth(), outcomePageNo, MAX_RECORD_COUNT);
            } else if (currMenu == MENU_FINANCE) {
              financePageNo = 1;
              requestProfitData("1", yearInfo.getSeledYearMonth(), financePageNo, MAX_RECORD_COUNT);
            }
          }
      }
    }

    private void nextYear() {
      yearInfo.setSelYear(yearInfo.getSelYear() + 1);

      prevYear.setText(getString(R.string.loan_year_record, yearInfo.getSelYear() - 1));
      nextYear.setText(getString(R.string.loan_year_record, yearInfo.getSelYear()));

      if (yearInfo.getSelYear() == yearInfo.getCurrYear()) {
        nextYear.setEnabled(false);
      } else {
        nextYear.setEnabled(true);
      }

      if (yearInfo.getSeledYear() == yearInfo.getSelYear()) {
        setChooseMonthTextColor(yearInfo.getSeledMonth() - 1);
      } else {
        setChooseMonthTextColor(-1);
      }
    }

    private void prevYear() {
      yearInfo.setSelYear(yearInfo.getSelYear() - 1);

      prevYear.setText(getString(R.string.loan_year_record, yearInfo.getSelYear() - 1));
      nextYear.setText(getString(R.string.loan_year_record, yearInfo.getSelYear()));

      if (yearInfo.getSelYear() == yearInfo.getCurrYear()) {
        nextYear.setEnabled(false);
      } else {
        nextYear.setEnabled(true);
      }

      if (yearInfo.getSeledYear() == yearInfo.getSelYear()) {
        setChooseMonthTextColor(yearInfo.getSeledMonth() - 1);
      } else {
        setChooseMonthTextColor(-1);
      }
    }
  };

  private void setChooseMonthTextColor(int currMonth) {
    if (currMonth == -1) {
      for (TextView month : this.monthArr) {
        month.setTextColor(getResources().getColor(R.color.loan_color_black));
      }

      return;
    }

    TextView monthView = this.monthArr[currMonth];

    for (TextView month : this.monthArr) {
      if (month == monthView) {
        month.setTextColor(getResources().getColor(R.color.loan_month_text_color));
      } else {
        month.setTextColor(getResources().getColor(R.color.loan_color_black));
      }
    }

    currMonth += 1;
    btnRight.setText(currMonth < 10 ? String.valueOf("0" + currMonth) : String.valueOf(currMonth));

    // 设置选中的年月
    this.yearInfo.setSeledMonth(currMonth);
    this.yearInfo.setSeledYear(this.yearInfo.getSelYear());
  }

  private void showDatePickerView() {
    if (this.isShowingDatePickerView) {
      this.isShowingDatePickerView = false;
      this.isClickDismissDatePickerView = true;
      this.hideDatePickerView();

      return;
    }

    this.isShowingDatePickerView = true;
    this.datePickerView.showAsDropDown(this.titleBarLayout);
  }

  private void hideDatePickerView() {
    this.isShowingDatePickerView = false;
    this.datePickerView.dismiss();
  }

  @Override
  protected void initTitleBar() {
    // 返回button
    Button btnLeft = (Button) findViewById(R.id.btnLeft);
    btnLeft.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View view) {
        finish();
      }
    });

    // 选择账单类型:转入/转出/收益
    final LinearLayout billInfoLayout = (LinearLayout) findViewById(R.id.billInfoLayout);
    billInfoLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View view) {
        billInfoArrow.setImageResource(R.drawable.loan_bill_info_menu_up);
        popupWindow.showAsDropDown(titleBarLayout);
      }
    });

    // 右侧button(选择年月)
    this.btnRight = (Button) findViewById(R.id.btnRight);
    int month = this.yearInfo.getSeledMonth();

    this.btnRight.setText(month < 10 ? String.valueOf("0" + month) : String.valueOf(month));
    this.btnRight.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        showDatePickerView();
      }
    });
  }

  @Override
  protected void initViews() {
    this.initPopWindow();

    this.initDatePickerView();

    this.totalProfit = (TextView) findViewById(R.id.totalProfit);
    this.billInfoList = (XListView) findViewById(R.id.billInfoList);
    this.billInfoList.setEmptyView(findViewById(R.id.nodatalayout));
    this.billInfoList.setPullRefreshEnable(true);
    this.billInfoList.setPullLoadEnable(true);
    this.billInfoList.setOverScrollMode(billInfoList.OVER_SCROLL_NEVER);
    this.billInfoList.setXListViewListener(this.xListViewListener);

    this.billInfoList.setSelector(R.drawable.loan_menu_item_selector);
    this.billInfoList.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position--;
        BillInfoAdatper adatper = null;

        if (currMenu == MENU_INCOME) {
          adatper = incomeAdatper;
        } else if (currMenu == MENU_OUTCOME) {
          adatper = outcomeAdatper;
        } else if (currMenu == MENU_FINANCE) {
          adatper = profitAdatper;
        }

        if (adatper.getBillType() == BillInfoAdatper.BILL_TYPE_INCOME) {// 转入
          Intent intent = new Intent(BillInfoActivity.this, BillInfoResult.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("title", getString(R.string.loan_current_pocket_income_success));
          intent.putExtra("bill_type", 0);
          intent.putExtra("id", ((Record) adatper.getItem(position)).getId());
          startActivity(intent);
        } else if (adatper.getBillType() == BillInfoAdatper.BILL_TYPE_OUTCOME) {// 转出
          Intent intent = new Intent(BillInfoActivity.this, BillInfoResult.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("title", getString(R.string.loan_current_pocket_outcome_success));
          intent.putExtra("bill_type", 1);
          intent.putExtra("kind", ((Record) adatper.getItem(position)).getKind());
          intent.putExtra("id", ((Record) adatper.getItem(position)).getId());
          startActivity(intent);
        }
      }
    });

    this.obtainIncomeData();
  }

  private void initPopWindow() {
    this.mContext = BillInfoActivity.this;
    this.totalProfitLayout = (RelativeLayout) findViewById(R.id.totalProfitLayout);
    this.billInfoArrow = (ImageView) findViewById(R.id.billInfoArrow);
    this.billInfoType = (TextView) findViewById(R.id.billInfoType);
    this.titleBarLayout = (RelativeLayout) findViewById(R.id.titleBarLayout);

    View contentView = LayoutInflater.from(this).inflate(R.layout.loan_pop_bill_menu_layout, null);

    this.menuIncomeChoosenIcon = (ImageView) contentView.findViewById(R.id.menuIncomeChoosenIcon);
    this.menuIncomeChoosenIcon.setVisibility(View.VISIBLE);
    this.menuOutcomeChoosenIcon = (ImageView) contentView.findViewById(R.id.menuOutcomeChoosenIcon);
    this.menuProfitChoosenIcon = (ImageView) contentView.findViewById(R.id.menuProfitChoosenIcon);

    this.popupWindow = new PopupWindow(this);
    this.popupWindow.setWidth(getResources().getDisplayMetrics().widthPixels);
    this.popupWindow.setHeight((int) (getResources().getDimension(R.dimen.loan_pop_menu_height) * 3 + getResources().getDimension(R.dimen.loan_divider_line_height) * 5));
    this.popupWindow.setTouchable(true);
    this.popupWindow.setOutsideTouchable(true);
    this.popupWindow.setContentView(contentView);
    this.popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.loan_pop_menu_bg));
    this.popupWindow.setOnDismissListener(new OnDismissListener() {
      @Override
      public void onDismiss() {
        billInfoArrow.setImageResource(R.drawable.loan_bill_info_menu_down);
      }
    });

    // 转入
    LinearLayout menuIncomeLayout = (LinearLayout) contentView.findViewById(R.id.menuIncomeLayout);
    menuIncomeLayout.setOnClickListener(this.menuClickListener);

    // 转出
    LinearLayout menuOutcomeLayout = (LinearLayout) contentView.findViewById(R.id.menuOutcomeLayout);
    menuOutcomeLayout.setOnClickListener(this.menuClickListener);

    // 收益
    LinearLayout menuProfitLayout = (LinearLayout) contentView.findViewById(R.id.menuProfitLayout);
    menuProfitLayout.setOnClickListener(this.menuClickListener);
  }

  private OnClickListener menuClickListener = new OnClickListener() {
    @Override
    public void onClick(View view) {
      switch (view.getId()) {
        case R.id.menuIncomeLayout: // 转入
          currMenu = MENU_INCOME;
          incomePageNo = 1;
          if (mList != null) {
            mList.clear();
          }
          billInfoList.setAdapter(null);
          obtainIncomeData();
          break;
        case R.id.menuOutcomeLayout: // 转出
          currMenu = MENU_OUTCOME;
          outcomePageNo = 1;
          if (mList != null) {
            mList.clear();
          }
          billInfoList.setAdapter(null);
          obtainOutcomeData();
          break;
        case R.id.menuProfitLayout: // 收益
          currMenu = MENU_FINANCE;
          financePageNo = 1;
          if (mList != null) {
            mList.clear();
          }
          billInfoList.setAdapter(null);
          obtainProfitData();
          break;
      }
    }
  };

  private class BillInfoAdatper extends BaseAdapter {
    private final static int BILL_TYPE_INCOME = 0;
    private final static int BILL_TYPE_OUTCOME = 1;
    private final static int BILL_TYPE_PROFIT = 2;
    private List<Record> billInfoList;
    private int billType;
    private LayoutInflater layoutInflater;

    public BillInfoAdatper(Context context, List<Record> billInfoList, int billType) {
      this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      this.billType = billType;
      this.billInfoList = billInfoList;
    }

    public void updateBillInfoList(List<Record> billInfoList) {
      this.billInfoList.clear();
      this.billInfoList.addAll(billInfoList);

      notifyDataSetChanged();
    }

    public void addNewBillInfoList(List<Record> billInfoList) {
      this.billInfoList.addAll(billInfoList);
      notifyDataSetChanged();
    }

    public int getBillType() {
      return this.billType;
    }

    @Override
    public int getCount() {
      return this.billInfoList.size();
    }

    @Override
    public Object getItem(int position) {
      return this.billInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public boolean isEnabled(int position) {
      if (this.billType == BILL_TYPE_PROFIT) {
        return false;
      }
      return super.isEnabled(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder = null;
      if (convertView == null) {
        convertView = this.layoutInflater.inflate(R.layout.loan_bill_info_item_layout, null);
        viewHolder = new ViewHolder();
        convertView.setTag(viewHolder);
        viewHolder.desc = (TextView) convertView.findViewById(R.id.billDesc);
        viewHolder.date = (TextView) convertView.findViewById(R.id.billDate);
        viewHolder.money = (TextView) convertView.findViewById(R.id.billMoney);
        viewHolder.billRightArrow = (ImageView) convertView.findViewById(R.id.billRightArrow);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      Record billInfo = this.billInfoList.get(position);
      if (this.billType != BILL_TYPE_PROFIT) {
        viewHolder.desc.setVisibility(View.VISIBLE);
        if (this.billType == BILL_TYPE_INCOME) {// 转入
          if (billInfo.getType().equals("0")) {
            viewHolder.desc.setText("余额转入");
          } else {
            viewHolder.desc.setText("银行卡转入");
          }
          viewHolder.money.setText(String.format("+%s", billInfo.getAmount()));
          viewHolder.money.setTextColor(getResources().getColor(R.color.loan_color_income));
          viewHolder.date.setText(DateUtil.getYADWBS(billInfo.getDatetime()));
        } else {// 转出
          if (billInfo.getType().equals("0")) {
            viewHolder.desc.setText("余额转出");
          } else {
            viewHolder.desc.setText("银行卡转出");
          }
          viewHolder.money.setText(String.format("-%s", billInfo.getAmount()));
          viewHolder.money.setTextColor(getResources().getColor(R.color.loan_color_outcome));
          viewHolder.date.setText(DateUtil.getYADWBS(billInfo.getDatetime()));
        }
      } else {// 收益
        viewHolder.desc.setVisibility(View.GONE);
        viewHolder.billRightArrow.setVisibility(View.GONE);
        viewHolder.money.setText(String.format("+%s", billInfo.getAmount()));
        viewHolder.money.setTextColor(getResources().getColor(R.color.loan_color_income));
        viewHolder.date.setText(DateUtil.getYMD(billInfo.getDate()));
      }
      return convertView;
    }

    private class ViewHolder {
      TextView desc;
      TextView date;
      TextView money;
      ImageView billRightArrow;
    }
  }

  private IXListViewListener xListViewListener = new IXListViewListener() {
    @Override
    public void onRefresh() {
      if (currMenu == MENU_INCOME) {
        incomePageNo = 1;
        requestIncomData("1", yearInfo.getSeledYearMonth(), incomePageNo, MAX_RECORD_COUNT);
      } else if (currMenu == MENU_OUTCOME) {
        outcomePageNo = 1;
        requestOutoData("1", yearInfo.getSeledYearMonth(), outcomePageNo, MAX_RECORD_COUNT);
      } else if (currMenu == MENU_FINANCE) {
        financePageNo = 1;
        requestProfitData("1", yearInfo.getSeledYearMonth(), financePageNo, MAX_RECORD_COUNT);
      }
    }

    @Override
    public void onLoadMore() {
      if (currMenu == MENU_INCOME) {
        loadMoreIncomData("1", yearInfo.getSeledYearMonth(), incomePageNo, MAX_RECORD_COUNT);
      } else if (currMenu == MENU_OUTCOME) {
        loadMoreOutoData("1", yearInfo.getSeledYearMonth(), outcomePageNo, MAX_RECORD_COUNT);
      } else if (currMenu == MENU_FINANCE) {
        loadMoreProfitData("1", yearInfo.getSeledYearMonth(), financePageNo, MAX_RECORD_COUNT);
      }
    }
  };

  private class YearInfo {
    private int currYear;
    private int currMonth;
    private int selYear;
    private int seledMonth;
    private int seledYear;
    private int prevSeledMonth;

    public YearInfo() {
      Calendar cal = Calendar.getInstance();

      this.currYear = cal.get(Calendar.YEAR);
      this.currMonth = cal.get(Calendar.MONTH) + 1;

      this.selYear = this.currYear;
      this.seledYear = this.currYear;
      this.seledMonth = this.currMonth;
      this.prevSeledMonth = this.currMonth;
    }

    public int getPrevSeledMonth() {
      return prevSeledMonth;
    }

    public int getSeledYear() {
      return seledYear;
    }

    public void setSeledYear(int seledYear) {
      this.seledYear = seledYear;
    }

    public int getCurrYear() {
      return currYear;
    }

    public int getCurrMonth() {
      return currMonth;
    }

    public int getSelYear() {
      return selYear;
    }

    public void setSelYear(int selYear) {
      this.selYear = selYear;
    }

    public int getSeledMonth() {
      return seledMonth;
    }

    public void setSeledMonth(int selMonth) {
      this.prevSeledMonth = this.seledMonth;
      this.seledMonth = selMonth;
    }

    public String getSeledYearMonth() {
      return new StringBuffer().append(this.seledYear).append("-").append(this.seledMonth < 10 ? ("0" + this.seledMonth) : this.seledMonth).toString();
    }
  }
}
