package com.jrd.loan.base;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.jrd.loan.R;
import com.jrd.loan.push.CustomNotificationHandler;
import com.jrd.loan.shared.prefs.AppInfoPrefs;
import com.jrd.loan.util.AppInfoUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

public class BaseApplication extends Application {
	private static Context context;
	private PushAgent mPushAgent;

	@Override
	public void onCreate() {
		super.onCreate();

		// 设置是否是测试环境(false 生产环境(默认), true 测试环境)
		JrdConfig.setDebug(true);

		this.initUmengPush();

		context = this;

		this.initAppVersionInfo();

		// 初始化ImageLoader配置
		this.initImageLoaderConfig();

		// 生产环境打开异常收集(异常信息发送到友盟)
		if (!JrdConfig.isDebug()) {
			this.initGlobalExpCatch();
		}
	}

	public static Context getContext() {
		return context;
	}

	private void initImageLoaderConfig() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().build();

		ImageLoader.getInstance().init(config);
	}

	private void initUmengPush() {
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.setDebugMode(true);

		UmengMessageHandler messageHandler = new UmengMessageHandler() {
			/**
			 * 参考集成文档的1.6.3 http://dev.umeng.com/push/android/integration#1_6_3
			 * */
			@Override
			public void dealWithCustomMessage(final Context context, final UMessage msg) {
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						// 对自定义消息的处理方式，点击或者忽略
						boolean isClickOrDismissed = true;

						if (isClickOrDismissed) {
							// 自定义消息的点击统计
							UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
						} else {
							// 自定义消息的忽略统计
							UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
						}

						Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
					}
				});
			}

			@Override
			public Notification getNotification(Context context, UMessage msg) {
				switch (msg.builder_id) {
					case 1:
						NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
						RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
						myNotificationView.setTextViewText(R.id.notification_title, msg.title);
						myNotificationView.setTextViewText(R.id.notification_text, msg.text);
						myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
						myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
						builder.setContent(myNotificationView);
						builder.setContentTitle(msg.title).setContentText(msg.text).setTicker(msg.ticker).setAutoCancel(true);
						Notification mNotification = builder.build();
						// 由于Android
						// v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
						mNotification.contentView = myNotificationView;
						return mNotification;
					default:
						// 默认为0，若填写的builder_id并不存在，也使用默认。
						return super.getNotification(context, msg);
				}
			}
		};

		mPushAgent.setMessageHandler(messageHandler);

		CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
	}

	private void initGlobalExpCatch() {
		CrashHandler handler = CrashHandler.getInstance();
		handler.init();
	}

	/**
	 * 保存程序的当前版本和上一个版本
	 */
	private void initAppVersionInfo() {
		String currVersion = AppInfoPrefs.getAppInfo(AppInfoPrefs.APP_INFO_KEY_CURR_VERSION, "");
		String lastVersion = AppInfoPrefs.getAppInfo(AppInfoPrefs.APP_INFO_KEY_LAST_VERSION, "");
		String appVersion = AppInfoUtil.getVersionName();

		// 如果当前版本和上一个版本都为空, 说明用户首次安装软件
		if (TextUtils.isEmpty(currVersion) && TextUtils.isEmpty(lastVersion)) {
			AppInfoPrefs.saveAppInfo(AppInfoPrefs.APP_INFO_KEY_CURR_VERSION, appVersion);
			AppInfoPrefs.saveAppInfo(AppInfoPrefs.APP_INFO_KEY_LAST_VERSION, appVersion);
		} else {
			// 如果app的版本和当前版本不相同, 说明用户升级了程序,
			// 需要将当前版本保存成上一个版本, 当前版本保存成app的版本
			if (!appVersion.equals(currVersion)) {
				AppInfoPrefs.saveAppInfo(AppInfoPrefs.APP_INFO_KEY_CURR_VERSION, appVersion);
				AppInfoPrefs.saveAppInfo(AppInfoPrefs.APP_INFO_KEY_LAST_VERSION, currVersion);
			}
		}
	}
}
