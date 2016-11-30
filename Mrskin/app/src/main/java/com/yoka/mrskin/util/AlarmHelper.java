package com.yoka.mrskin.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.yoka.mrskin.main.AppContext;
import com.yoka.mrskin.model.managers.task.YKTaskManager.HomeCardData;
import com.yoka.mrskin.receiver.AlarmReceiver;

public class AlarmHelper
{
	private final static boolean IS_DEBUG = true;

	public static final String EXTRA_DATA_CONTNET = "content";
	private AlarmManager mAlarmManager;
	private static AlarmHelper singleton = null;
	private static Object lock = new Object();
	private ArrayList<HomeCardData> mAlarmIdArray;

	public static AlarmHelper getInstance() {
		synchronized (lock) {
			if (singleton == null) {
				singleton = new AlarmHelper(AppContext.getInstance());
			}
		}
		return singleton;
	}

	private AlarmHelper(Context c)
	{
		mAlarmManager = (AlarmManager) c
				.getSystemService(Context.ALARM_SERVICE);
	}

	public void openAlarm(ArrayList<HomeCardData> alarmIdArray) {
		closeAllAlarm();
		if (alarmIdArray == null) {
			return;
		}
		mAlarmIdArray = alarmIdArray;
		for (int i = 0; i < mAlarmIdArray.size(); i++) {
			openAlarm(mAlarmIdArray.get(i));
		}
	}
	//任务
	//    public void openTaskAlarm(ArrayList<YKTask> alarmTaskArray) {
	//        closeAllAlarm();
	//        mAlarmTaskArray = alarmTaskArray;
	//        if (alarmTaskArray == null) {
	//            return;
	//        }
	//        for (int i = 0; i < alarmTaskArray.size(); i++) {
	//            openTaskAlarm(alarmTaskArray.get(i));
	//        }
	//    }

	public void closeAllAlarm() {
		if (mAlarmIdArray == null) {
			return;
		}
		for (int i = 0; i < mAlarmIdArray.size(); i++) {
			closeAlarm(mAlarmIdArray.get(i));
		}
	}

	//任务
	//    public void closeAllTaskAlarm() {
	//        if (mAlarmTaskArray == null) {
	//            return;
	//        }
	//        for (int i = 0; i < mAlarmTaskArray.size(); i++) {
	//            closeTaskAlarm(mAlarmTaskArray.get(i));
	//        }
	//    }

	private void openAlarm(HomeCardData data) {
		if (IS_DEBUG) {
			System.out.println("************data == null ？ "
					+ null == data
					+ "getCanRemindAll:"
					+ !AppUtil.getCanRemindAll(AppContext.getInstance())
					+ " data.ismCanRemind():"
					+ data.ismCanRemind());

		}
		if (data == null || !AppUtil.getCanRemindAll(AppContext.getInstance())
				|| !data.ismCanRemind()) {
			return;
		}
		Intent intent = new Intent();
		intent.putExtra(EXTRA_DATA_CONTNET, data);
		intent.setClass(AppContext.getInstance(), AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(AppContext.getInstance(),
				Integer.parseInt(data.getmId()), intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		//设定一个五分钟前的时间
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(data.getmYkDate()
				.getmMills());
		calendar.add(Calendar.SECOND, -300);


		mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi); //设置一次性闹钟
		if (IS_DEBUG) {
			System.out.println("Alarm   AlarmHelper  openAlarm "
					+ data.getmId()
					+ " "
					+ data.getmTitle()
					+ "  "
					+ data.getmSubTitle()
					+ "  "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data
							.getmYkDate().getmMills()));

		}
	}
	//任务
	//    private void openTaskAlarm(YKTask data) {
	//        if (data == null || !AppUtil.getCanRemindAll(AppContext.getInstance())) {
	//            return;
	//        }
	//        Intent intent = new Intent();
	//        intent.putExtra(EXTRA_DATA_CONTNET, data);
	//        intent.setClass(AppContext.getInstance(), AlarmReceiver.class);
	//        PendingIntent pi = PendingIntent.getBroadcast(AppContext.getInstance(),
	//                Integer.parseInt(data.getmSubtask().get(0).getID()), intent,
	//                PendingIntent.FLAG_UPDATE_CURRENT);
	//        mAlarmManager.set(AlarmManager.RTC_WAKEUP, data.getmRemindTime()
	//                .getmMills(), pi);
	//        if (IS_DEBUG) {
	//            System.out.println("Alarm   AlarmHelper  openAlarm "
	//                    + data.getmSubtask().get(0).getID()
	//                    + " "
	//                    + data.getmTitle()
	//                    + "  "
	//                    + data.getmSubtask().get(0).getmDesc()
	//                    + "  "
	//                    + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data
	//                            .getmRemindTime().getmMills()));
	//
	//        }
	//    }

	public void closeAlarm(HomeCardData data) {
		if (data == null) {
			return;
		}
		Intent intent = new Intent();
		intent.putExtra(EXTRA_DATA_CONTNET, data);
		intent.setClass(AppContext.getInstance(), AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(AppContext.getInstance(),
				Integer.parseInt(data.getmId()), intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mAlarmManager.cancel(pi);

		if (IS_DEBUG) {
			System.out.println("Alarm   AlarmHelper  closeAlarm "
					+ data.getmId()
					+ " "
					+ data.getmTitle()
					+ "  "
					+ data.getmSubTitle()
					+ "  "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data
							.getmYkDate().getmMills()));
		}
	}
	
	
	//任务
	//    private void closeTaskAlarm(YKTask data) {
	//        if (data == null) {
	//            return;
	//        }
	//        Intent intent = new Intent();
	//        intent.putExtra(EXTRA_DATA_CONTNET, data);
	//        intent.setClass(AppContext.getInstance(), AlarmReceiver.class);
	//        PendingIntent pi = PendingIntent.getBroadcast(AppContext.getInstance(),
	//                Integer.parseInt(data.getmSubtask().get(0).getID()), intent,
	//                PendingIntent.FLAG_UPDATE_CURRENT);
	//        mAlarmManager.cancel(pi);
	//
	//        if (IS_DEBUG) {
	//            System.out.println("Alarm   AlarmHelper  closeAlarm "
	//                    + data.getmSubtask().get(0).getID()
	//                    + " "
	//                    + data.getmTitle()
	//                    + "  "
	//                    + data.getmSubtask().get(0).getmDesc()
	//                    + "  "
	//                    + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data
	//                            .getmRemindTime().getmMills()));
	//        }
	//    }

}
