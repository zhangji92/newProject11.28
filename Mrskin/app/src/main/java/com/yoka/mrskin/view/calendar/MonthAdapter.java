package com.yoka.mrskin.view.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yoka.mrskin.R;

public class MonthAdapter extends BaseAdapter
{
    private DateFormat weekdayNameFormat;
    private int dividerColor;
    private int dayBackgroundResId;
    private int dayTextColorResId;
    private int titleTextColor;
    private boolean displayHeader;
    private int headerTextColor;
    private Typeface titleTypeface;
    private Typeface dateTypeface;

    private Calendar today;
    private final LayoutInflater inflater;
    private CalendarPickerView listview;

    public MonthAdapter(Context context, AttributeSet attrs,
            CalendarPickerView listview)
    {
        inflater = LayoutInflater.from(context);
        this.listview = listview;
        Resources res = context.getResources();
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CalendarPickerView);
        dividerColor = a.getColor(R.styleable.CalendarPickerView_dividerColor,
                res.getColor(R.color.calendar_divider));
        dayBackgroundResId = a.getResourceId(
                R.styleable.CalendarPickerView_dayBackground,
                R.drawable.calendar_bg_selector);
        dayTextColorResId = a.getResourceId(
                R.styleable.CalendarPickerView_dayTextColor,
                R.color.calendar_text_selector);
        titleTextColor = a.getColor(
                R.styleable.CalendarPickerView_titleTextColor,
                res.getColor(R.color.calendar_text_active));
        displayHeader = a.getBoolean(
                R.styleable.CalendarPickerView_displayHeader, true);
        headerTextColor = a.getColor(
                R.styleable.CalendarPickerView_headerTextColor,
                res.getColor(R.color.calendar_text_active));
        a.recycle();

        Locale locale = Locale.getDefault();
        weekdayNameFormat = new SimpleDateFormat(
                context.getString(R.string.day_name_format), locale);
        today = Calendar.getInstance(locale);
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public int getCount() {
        return listview.getMonth().size();
    }

    @Override
    public Object getItem(int position) {
        return listview.getMonth().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Calendar getToday() {
        return today;
    }

    public DateFormat getWeekdayNameFormat() {
        return weekdayNameFormat;
    }

    public void setWeekdayNameFormat(DateFormat weekdayNameFormat) {
        this.weekdayNameFormat = weekdayNameFormat;
    }

    public void setTitleTypeface(Typeface titleTypeface) {
        this.titleTypeface = titleTypeface;
    }

    public void setDateTypeface(Typeface dateTypeface) {
        this.dateTypeface = dateTypeface;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MonthView monthView = (MonthView) convertView;
        MonthView.Listener listener = listview.getMonthViewListener();
        if (monthView == null) {
            monthView = MonthView.create(parent, inflater, weekdayNameFormat,
                    listener, today);
            monthView.initStyle(dividerColor, dayBackgroundResId,
                    dayTextColorResId, titleTextColor, displayHeader,
                    headerTextColor, titleTypeface, dateTypeface);
        }
        monthView.initData(listview.getMonth().get(position), listview
                .getCell().get(position), listview.isDisplayOnly());
        return monthView;
    }

}
