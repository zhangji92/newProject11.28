package com.example.computer.viewpagermostdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Computer on 2016/3/10.
 */
public class DataPickerActivity extends Activity{
    private DatePicker datePicker;
         private TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_aa);
        datePicker = (DatePicker) findViewById(R.id.dpPicker);
                 timePicker = (TimePicker) findViewById(R.id.tpPicker);

                datePicker.init(2013, 8, 20, new DatePicker.OnDateChangedListener() {

                                 @Override
                         public void onDateChanged(DatePicker view, int year,
                                                                        int monthOfYear, int dayOfMonth) {
                                 // 获取一个日历对象，并初始化为当前选中的时间
                               Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat(
                                              "yyyy年MM月dd日  HH:mm");
                                Toast.makeText(DataPickerActivity.this,
                                               format.format(calendar.getTime()), Toast.LENGTH_SHORT)
                                        .show();
                             }
                  });

                 timePicker.setIs24HourView(true);
               timePicker
                        .setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                               @Override
                                public void onTimeChanged(TimePicker view, int hourOfDay,
                                                                                         int minute) {
                                         Toast.makeText(DataPickerActivity.this,
                                                        hourOfDay + "小时" + minute + "分钟",
                                                      Toast.LENGTH_SHORT).show();
                                    }
                             });


    }
}
