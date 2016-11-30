package com.yoka.mrskin.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;

import com.yoka.mrskin.R;

public class GPS {
    private static LocationManager mLocationManager;
    public static boolean openGPSSettings(final Activity activity) {
        mLocationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(android.R.drawable.ic_dialog_info);

        builder.setTitle(R.string.alert);
        builder.setMessage(R.string.please_open_gps);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(intent, 0);
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();

        return false;
    }

    public static void getLocation(final Activity activity, LocationListener listener) {
        // 获取位置管理服务
        
        String serviceName = Context.LOCATION_SERVICE;
        mLocationManager = (LocationManager) activity.getSystemService(serviceName);
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.NO_REQUIREMENT); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

        String provider = mLocationManager.getBestProvider(criteria, true); // 获取GPS信息
        // 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
        if (listener != null) {
            mLocationManager.requestLocationUpdates(provider, 100 * 1000, 500, listener);
        }
    }
    
    public static void stopLocation(LocationListener listener) {
        if (listener != null) {
            if (mLocationManager != null) {
                mLocationManager.removeUpdates(listener);
            }
        }
    }

}
