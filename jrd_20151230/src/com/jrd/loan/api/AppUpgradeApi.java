package com.jrd.loan.api;

import android.content.Context;

import com.jrd.loan.api.WebApi.AppApi;
import com.jrd.loan.bean.AppInfoBean;
import com.jrd.loan.net.framework.HttpTask;
import com.jrd.loan.net.framework.OnHttpTaskListener;
import com.jrd.loan.util.AppInfoUtil;

public final class AppUpgradeApi {
  // 检查app更新升级
  public static void upgradeApp(Context context, OnHttpTaskListener<AppInfoBean> httpTaskListener) {
    HttpTask httpTask = HttpTask.getHttpTask(context, AppApi.APP_UPGRADE, HttpTask.METHOD_POST);
    httpTask.putParam("platformType", "Android");
    httpTask.putParam("osVer", android.os.Build.VERSION.RELEASE);
    httpTask.putParam("appName", AppInfoUtil.getAppName());
    httpTask.putParam("appID", AppInfoUtil.getAppPkgName());
    httpTask.putParam("appVer", AppInfoUtil.getVersionName());
    httpTask.runTask(AppInfoBean.class, httpTaskListener);
  }
}
