// Copyright 2012 Square, Inc.
package com.yoka.mrskin.view.calendar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yoka.mrskin.R;

/**
 * @author qianda 每个月，由两部分构成： 1.Title部分(显示星期编号) 2.grid部分(显示具体日期)
 */
public class MonthView extends LinearLayout
{
    private TextView title;
    private CalendarGridView grid;
    private Listener listener;

    public static MonthView create(ViewGroup parent, LayoutInflater inflater,
            DateFormat weekdayNameFormat, Listener listener, Calendar today) {
        final MonthView view = (MonthView) inflater.inflate(R.layout.calendar_month,
                parent, false);

        final int originalDayOfWeek = today.get(Calendar.DAY_OF_WEEK);

        int firstDayOfWeek = today.getFirstDayOfWeek();
        final CalendarRowView headerRow = (CalendarRowView) view.grid
                .getChildAt(0);
        for (int offset = 0; offset < 7; offset++) {
            today.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + offset);
            final TextView textView = (TextView) headerRow.getChildAt(offset);
            textView.setText(weekdayNameFormat.format(today.getTime()));
        }
        today.set(Calendar.DAY_OF_WEEK, originalDayOfWeek);
        view.listener = listener;
        return view;
    }

    public MonthView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title = (TextView) findViewById(R.id.title);
        grid = (CalendarGridView) findViewById(R.id.calendar_grid);
    }

    public void initData(MonthDescriptor month,
            List<List<MonthCellDescriptor>> cells, boolean displayOnly) {
        Logr.d("Initializing MonthView (%d) for %s",
                System.identityHashCode(this), month);
        long start = System.currentTimeMillis();
        title.setText(month.getLabel());

        final int numRows = cells.size();
        grid.setNumRows(numRows);
        // 每个月最多六周
        for (int i = 0; i < 6; i++) {
            CalendarRowView weekRow = (CalendarRowView) grid.getChildAt(i + 1);
            weekRow.setListener(listener);
            if (i < numRows) {
                weekRow.setVisibility(VISIBLE);
                List<MonthCellDescriptor> week = cells.get(i);
                for (int c = 0; c < week.size(); c++) {
                    MonthCellDescriptor cell = week.get(c);
                    CalendarCellView cellView = (CalendarCellView) weekRow
                            .getChildAt(c);

                    String cellDate = Integer.toString(cell.getValue());
                    if (!cellView.getText().equals(cellDate)) {
                        cellView.setText(cellDate);
                    }
                    cellView.setEnabled(cell.isCurrentMonth());
                    cellView.setClickable(!displayOnly);

                    cellView.setSelectable(cell.isSelectable());
                    cellView.setSelected(cell.isSelected());
                    cellView.setCurrentMonth(cell.isCurrentMonth());
                    cellView.setToday(cell.isToday());
                    cellView.setRangeState(cell.getRangeState());
                    cellView.setHighlighted(cell.isHighlighted());
                    cellView.setTag(cell);
                }
            } else {
                weekRow.setVisibility(GONE);
            }
        }

        Logr.d("MonthView.init took %d ms", System.currentTimeMillis() - start);
    }

    public void initStyle(int dividerColor, int dayBackgroundResId,
            int dayTextColorResId, int titleTextColor, boolean displayHeader,
            int headerTextColor, Typeface titleTypeface, Typeface dateTypeface) {
        setDividerColor(dividerColor);
        setDayTextColor(dayTextColorResId);
        setTitleTextColor(titleTextColor);
        setDisplayHeader(displayHeader);
        setHeaderTextColor(headerTextColor);
        if (dayBackgroundResId != 0) {
            setDayBackground(dayBackgroundResId);
        }
        if (titleTypeface != null) {
            title.setTypeface(titleTypeface);
        }
        if (dateTypeface != null) {
            grid.setTypeface(dateTypeface);
        }
    }

    public void setDividerColor(int color) {
        grid.setDividerColor(color);
    }

    public void setDayBackground(int resId) {
        grid.setDayBackground(resId);
    }

    public void setDayTextColor(int resId) {
        grid.setDayTextColor(resId);
    }

    public void setTitleTextColor(int color) {
        title.setTextColor(color);
    }

    public void setDisplayHeader(boolean displayHeader) {
        grid.setDisplayHeader(displayHeader);
    }

    public void setHeaderTextColor(int color) {
        grid.setHeaderTextColor(color);
    }

    public interface Listener
    {
        void handleClick(MonthCellDescriptor cell);
    }
}
