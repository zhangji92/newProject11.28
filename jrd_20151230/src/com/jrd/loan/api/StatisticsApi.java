package com.jrd.loan.api;

import android.content.Context;

import com.jrd.loan.bean.BaseBean;
import com.jrd.loan.net.framework.HttpTask;
import com.jrd.loan.shared.prefs.UserInfoPrefs;
import com.jrd.loan.util.AppInfoUtil;
import com.jrd.loan.util.NetUtil;
import com.jrd.loan.util.SIMCardInfo;
import com.jrd.loan.util.ScreenUtil;
import com.jrd.loan.util.UuidUtil;

public class StatisticsApi {

  public static void getUserBehavior(Context mContext, String eventName, String eventId, String projectId, String isFristInvert, String money) {
    HttpTask task = HttpTask.getHttpTask(mContext, "appapi/getUserBehavior", HttpTask.METHOD_POST);
    task.putParam("userId", UserInfoPrefs.getUserId() == null ? "" : UserInfoPrefs.getUserId());// 用户ID
    task.putParam("platformType", "Android");//平台类型
    task.putParam("appName", AppInfoUtil.getAppName());// 项目名称
    task.putParam("appID", AppInfoUtil.getAppPkgName());// 项目包名
    task.putParam("channelID", AppInfoUtil.getChannel());// 渠道ＩＤ
    task.putParam("machineType", android.os.Build.MODEL);// 手机型号
    task.putParam("imei", UuidUtil.getUUID());// IMEI号
    task.putParam("appVer", AppInfoUtil.getVersionName());// 应用版本
    task.putParam("osVer", android.os.Build.VERSION.RELEASE);// 系统版本
    task.putParam("deviceToken", UuidUtil.getUUID());// 推送设备的ID
    task.putParam("isPushOpen", "1");// 是否开启推送 1打开 0是关闭
    task.putParam("screenWidth", ScreenUtil.getScreenWidth(mContext));// 获取屏幕宽度
    task.putParam("screenHeight", ScreenUtil.getScreenHeight(mContext));// 获取屏幕高度
    // 获取手机运营商
    task.putParam("carrier", SIMCardInfo.getInstance(mContext).getProvidersName() == null ? "用户未插卡" : SIMCardInfo.getInstance(mContext).getProvidersName());
    task.putParam("netWorkType", NetUtil.GetNetworkType());// 获取网络类型
    task.putParam("provice", UserInfoPrefs.getProvince(mContext));// 获取省
    task.putParam("city", UserInfoPrefs.getCity(mContext));// 获取市
    task.putParam("longitude", UserInfoPrefs.getLongitude(mContext));// 获取经度
    task.putParam("latitude", UserInfoPrefs.getLatitude(mContext));// 获取纬度
    task.putParam("eventName", eventName == null ? "" : eventName);
    task.putParam("eventID", eventId == null ? "" : eventId);
    task.putParam("projectID", projectId == null ? "-1" : projectId);
    task.putParam("isFristInvert", isFristInvert == null ? "-1" : isFristInvert);
    task.putParam("moneyAmount", money == null ? "-1" : money);
    task.putParam("remark", "");
    task.runTask(BaseBean.class, null);
  }
}
