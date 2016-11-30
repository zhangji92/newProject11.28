package com.yoka.mrskin.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.base.BaseActivity;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.view.calendar.CalendarPickerView;
import com.yoka.mrskin.view.calendar.CalendarPickerView.SelectionMode;

public class CalendarActivity extends BaseActivity
{

     private CalendarPickerView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        YKActivityManager.getInstance().addActivity(this);
        // 显示范围为3个月，本月的前一个月和后一个月
        final Calendar minCalendar = Calendar.getInstance();
        minCalendar.add(Calendar.MONTH, -1);

        final Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.MONTH, 1);
        
         calendar = (CalendarPickerView) findViewById(R.id.calendar_view);

//         calendar.init(lastYear.getTime(), nextYear.getTime()) //
//         .inMode(SelectionMode.SINGLE) //
//         .withSelectedDate(new Date());

         Calendar today = Calendar.getInstance();
         ArrayList<Date> dates = new ArrayList<Date>();
         today.add(Calendar.DATE, -2);
         dates.add(today.getTime());
         today.add(Calendar.DATE, 5);
         dates.add(today.getTime());
         calendar.init(minCalendar.getTime(), maxCalendar.getTime())
         .inMode(SelectionMode.RANGE)
         .withSelectedDates(dates);
    }
    /**
     * 友盟统计需要的俩个方法
     */
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("CalendarActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
        JPushInterface.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CalendarActivity"); // 保证 onPageEnd 在onPause
                                                        // 之前调用,因为 onPause
                                                        // 中会保存信息
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        YKActivityManager.getInstance().removeActivity(this);
    }
}
