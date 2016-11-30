package com.yoka.mrskin.receiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;

import com.yoka.mrskin.R;
import com.yoka.mrskin.activity.PlanDetailsActivity;
import com.yoka.mrskin.activity.TaskFinishActivity;
import com.yoka.mrskin.main.MainActivity;
import com.yoka.mrskin.model.managers.task.YKTaskManager.HomeCardData;
import com.yoka.mrskin.util.AlarmHelper;
import com.yoka.mrskin.util.AppUtil;

public class AlarmReceiver extends BroadcastReceiver
{
	private final static boolean IS_DEBUG = true;
	private NotificationCompat.Builder mBuilder;
	private NotificationManager mNotificationManager;
	private String ns = Context.NOTIFICATION_SERVICE;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null) {
			System.out.println("Alarm   AlarmReceiver  intent == null");
			return;
		}
		if(null == intent.getSerializableExtra(AlarmHelper.EXTRA_DATA_CONTNET)){
			System.out.println("Alarm   AlarmReceiver  intent == null");
			return;
		}
		HomeCardData data = (HomeCardData) intent
				.getSerializableExtra(AlarmHelper.EXTRA_DATA_CONTNET);
		if (data == null) {
			System.out.println("Alarm   AlarmReceiver  data == null");
			return;
		}
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		@SuppressWarnings("deprecation")
		WakeLock waklock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "Gank");
		waklock.acquire();
		initNotify(context, data);
		if (IS_DEBUG) {
			System.out
			.println("Alarm   AlarmReceiver  NotificationManager  ok");
			System.out.println("Alarm  HomeCardData data.getmId() "
					+ data.getmId());
			System.out.println("Alarm  HomeCardData data.getmParent_id() "
					+ data.getmParent_id());
			System.out.println("Alarm  HomeCardData data.getmScore() "
					+ data.getmScore());
			System.out.println("Alarm  HomeCardData data.getmSubTitle()  "
					+ data.getmSubTitle());
			System.out.println("Alarm  HomeCardData data.getmTitle() "
					+ data.getmTitle());
			// System.out.println("Alarm  HomeCardData data.getmYkDate() "
			// + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data
			// .getmYkDate()));
		}
	}

	@SuppressLint("InlinedApi")
	private void initNotify(Context context, HomeCardData data) {
		mNotificationManager = (NotificationManager) context
				.getSystemService(ns);

		/*Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		resultIntent.putExtra(AlarmHelper.EXTRA_DATA_CONTNET, data);*/

		//点击跳转美丽计划页面（用户可点击结束）---zlz(v2.5.1)
		Intent resultIntent = new Intent(context, TaskFinishActivity.class);
		resultIntent.putExtra("taskId", data.getmParent_id());
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				Integer.parseInt(data.getmId()), resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setAutoCancel(true).setContentTitle(data.getmTitle())
		.setContentText(data.getmSubTitle())
		.setContentIntent(pendingIntent)
		// 通知首次出现在通知栏，带上升动画效果的
		.setTicker(data.getmSubTitle())
		// 通知产生的时间，会在通知信息里显示
		.setWhen(System.currentTimeMillis())
		// 设置该通知优先级
		// .setPriority(Notification.PRIORITY_DEFAULT)
		// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
		.setOngoing(false)
		// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
		.setSmallIcon(R.drawable.ic_launcher);
		Notification notification = null;
		if (AppUtil.getCanRemindRING(context)
				&& AppUtil.getCanRemindSHAKE(context)) {
			mBuilder.setDefaults(Notification.DEFAULT_VIBRATE
					| Notification.DEFAULT_SOUND);
			notification = mBuilder.build();
		} else if (AppUtil.getCanRemindRING(context)) {
			mBuilder.setDefaults(Notification.DEFAULT_SOUND);
			notification = mBuilder.build();
			notification.vibrate = null;
		} else if (AppUtil.getCanRemindSHAKE(context)) {
			mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
			notification = mBuilder.build();
			notification.sound = null;
		} else {
			notification = mBuilder.build();
			notification.sound = null;
			notification.vibrate = null;
		}
		mNotificationManager.notify(Integer.parseInt(data.getmId()),
				notification);
	}
}
