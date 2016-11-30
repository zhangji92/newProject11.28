package com.yoka.mrskin.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.view.calendar.CalendarPickerView;
import com.yoka.mrskin.view.calendar.CalendarPickerView.SelectionMode;

public class CalendarFragment extends Fragment implements OnClickListener
{
    private CalendarPickerView calendar;

    @Override
    public void onClick(View v) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        final Calendar minCalendar = Calendar.getInstance();
        minCalendar.add(Calendar.MONTH, -1);

        final Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.MONTH, 1);

        Calendar today = Calendar.getInstance();
        ArrayList<Date> dates = new ArrayList<Date>();
        today.add(Calendar.DATE, -2);
        dates.add(today.getTime());
        today.add(Calendar.DATE, 5);
        dates.add(today.getTime());
        calendar.init(minCalendar.getTime(), maxCalendar.getTime()) //
                .inMode(SelectionMode.RANGE) //
                .withSelectedDates(dates);
    }

    private void init() {
        calendar = (CalendarPickerView) getActivity().findViewById(
                R.id.calendar_view);
    }
    /**
     * 有Fragment
     */
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("CalendarFragment"); // 统计页面
        MobclickAgent.onResume(getActivity()); // 统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CalendarFragment"); // 保证 onPageEnd 在onPause
                                                 // 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(getActivity());
    }
}
