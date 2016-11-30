package com.yoka.mrskin.main;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import cn.jpush.android.api.JPushInterface;

import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.umeng.socialize.PlatformConfig;
import com.yoka.mrskin.activity.base.YKActivityManager;
import com.yoka.mrskin.login.UmSharedAppID;
import com.yoka.mrskin.model.http.AsyncHttpMethod;
import com.yoka.mrskin.model.managers.YKCurrentUserManager;
import com.yoka.mrskin.model.managers.base.YKManager;
import com.yoka.mrskin.track.manager.TrackManager;
import com.yoka.mrskin.util.YKDeviceInfo;
import com.yoka.mrskin.util.YKFile;

public class MrSkinApplication extends Application
{
	private static Application mApp = null;
    @SuppressWarnings("static-access")
    @Override
    public void onCreate() {
        super.onCreate();
        mApp=this;
        AppContext.init(this);
        PlatformConfig.setWeixin(UmSharedAppID.SHARE_WX_APP_ID,UmSharedAppID.SHARE_WX_APP_SECRET);
        PlatformConfig.setQQZone(UmSharedAppID.SHARE_QQ_APP_ID, UmSharedAppID.SHARE_QQ_APP_SECRET);
        PlatformConfig.setSinaWeibo(UmSharedAppID.APP_KEY, UmSharedAppID.SHARE_SINA_APP_SECRET);
        //Config.REDIRECT_URL =SharedAppID.REDIRECT_URL;
        CustomCrashHandler mCustomCrashHandler = CustomCrashHandler.getInstance();
        mCustomCrashHandler.setCustomCrashHanler(getApplicationContext());
        AsyncHttpMethod.init();
        YKFile.setContext(this);
        YKDeviceInfo.init(this);
        YKCurrentUserManager.getInstance(this);
        //JPushInterface.setDebugMode(false);
        //JPushInterface.init(this);
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

            }
        };
        TrackManager.getInstance().setHandler(handler);
        TrackManager.init(this);
        //TrackManager.getInstance().addTrack(TrackUrl.APP_OPEN);
        YKManager.init(YKActivityManager.getInstance());
        initImageLoader(getApplicationContext());
    }
	/**
	 * 初始化Imageloader
	 * @since 
	 * @author wangnan
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		L.writeLogs(false);
		config.memoryCache(new LRULimitedMemoryCache(20 * 1024 * 1024));  
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        AsyncHttpClient.shutdownHttpClient();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AsyncHttpClient.shutdownHttpClient();
    }
    public static Application getApplication() {
		return mApp;
	}
}
